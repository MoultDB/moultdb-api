package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.AuthenticationException;
import org.moultdb.api.repository.dao.UserDAO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.moultdb.api.repository.dto.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLUserDAO implements UserDAO {
    
    
    @Autowired private PasswordEncoder passwordEncoder;
    
    private final static Logger logger = LogManager.getLogger(MySQLUserDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private final static String SELECT_STATEMENT = "SELECT * FROM user ";
    
    public MySQLUserDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<UserTO> findAll() {
        return template.query(SELECT_STATEMENT, new UserRowMapper());
    }
    
    @Override
    public UserTO findById(int id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<UserTO> findByIds(Set<Integer> ids) {
        if (ids == null || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new UserRowMapper());
    }
    
    @Override
    public UserTO findByEmailAndPassword(String email, String password) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("An e-mail or a password can not be null");
        }
        UserTO userTO = findByEmail(email);
        if (userTO == null || !passwordEncoder.matches(password, userTO.getPassword())) {
            throw new AuthenticationException("The e-mail or the password is incorrect");
        }
        return userTO;
    }
    
    @Override
    public UserTO findByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("E-mail is empty");
        }
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE email = (:email)",
                new MapSqlParameterSource().addValue("email", email), new UserRowMapper()));
    }
    
    @Override
    public UserTO findByOrcidId(String orcidId) {
        if (StringUtils.isBlank(orcidId)) {
            throw new IllegalArgumentException("Orcid ID is empty");
        }
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE orcid_id = (:orcidId)",
                new MapSqlParameterSource().addValue("orcidId", orcidId), new UserRowMapper()));
    }
    
    @Override
    public UserTO findByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username is empty");
        }
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE username = (:username)",
                new MapSqlParameterSource().addValue("username", username), new UserRowMapper()));
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM user ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'user' table");
            return 0;
        }
    }
    
    @Override
    public int[] insertUser(UserTO userTO) {
        return batchUpdate(Collections.singleton(userTO));
    }
    
    @Override
    public int[] batchUpdate(Set<UserTO> userTOs) {
        
        String insertStmt = "INSERT INTO user(username, full_name, email, pwd, roles, orcid_id, verified) " +
                "VALUES (:username, :fullName, :email, :password, :roles, :orcidId, :verified)" +
                "AS new " +
                "ON DUPLICATE KEY UPDATE full_name = new.full_name, email = new.email, orcid_id = new.orcid_id";
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (UserTO userTO : userTOs) {
            MapSqlParameterSource userSource = new MapSqlParameterSource();
            userSource.addValue("username", userTO.getUsername());
            userSource.addValue("fullName", userTO.getName());
            userSource.addValue("email", userTO.getEmail());
            userSource.addValue("password", StringUtils.isBlank(userTO.getPassword()) ?
                    null : passwordEncoder.encode(userTO.getPassword()));
            userSource.addValue("roles", userTO.getRoles());
            userSource.addValue("orcidId", userTO.getOrcidId());
            userSource.addValue("verified", userTO.isVerified() == null? "0": userTO.isVerified());
            params.add(userSource);
        }
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logResult(ints);
        return ints;
    }
    
    @Override
    public int[] updateUserPassword(String email, String password) {
        String taxonSql = "UPDATE user " +
                "SET user.pwd = :password " +
                "WHERE user.email = :email";
    
        List<MapSqlParameterSource> params = new ArrayList<>();
        MapSqlParameterSource userSource = new MapSqlParameterSource();
        userSource.addValue("email", email);
        userSource.addValue("password", passwordEncoder.encode(password));
        params.add(userSource);
    
        int[] ints = template.batchUpdate(taxonSql, params.toArray(MapSqlParameterSource[]::new));
        logResult(ints);
        return ints;
    }
    
    @Override
    public int[] updateUserAsVerified(String email) {
        String taxonSql = "UPDATE user " +
                "SET user.verified = 1 " +
                "WHERE user.email = :email";
    
        List<MapSqlParameterSource> params = new ArrayList<>();
        MapSqlParameterSource userSource = new MapSqlParameterSource();
        userSource.addValue("email", email);
        params.add(userSource);
    
        int[] ints = template.batchUpdate(taxonSql, params.toArray(MapSqlParameterSource[]::new));
        logResult(ints);
        return ints;
    }
    
    private static void logResult(int[] ints) {
        logger.info("{} updated row(s) in 'user' table", Arrays.stream(ints).sum());
    }
    
    private static class UserRowMapper implements RowMapper<UserTO> {
        @Override
        public UserTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserTO(rs.getInt("id"),
                    rs.getString("username"), rs.getString("full_name"), rs.getString("email"),
                    rs.getString("pwd"), rs.getString("roles"),
                    rs.getString("orcid_id"), rs.getBoolean("verified"));
        }
    }
}
