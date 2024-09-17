package org.moultdb.api.repository.dao;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.TransfertObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
// TODO create EntityDAO with methods T findById(U id), List<T> findByIds(Set<U> ids), U getLastId();
// TODO add findAll() to force method implementation in implementation classes
public interface DAO<T extends TransfertObject> {
    
    static GeologicalAgeTO mapToGeologicalAgeTO(ResultSet rs, String geoAgeTableAlias) throws SQLException {
        String alias = "";
        if (StringUtils.isNotBlank(geoAgeTableAlias)) {
            alias = geoAgeTableAlias + ".";
        }
    
        if (StringUtils.isBlank(rs.getString(alias + "notation"))) {
            return null;
        }
        return new GeologicalAgeTO(rs.getString(alias + "notation"), rs.getString(alias + "name"), rs.getString(alias + "rank_type"),
                rs.getBigDecimal(alias + "younger_bound"), rs.getBigDecimal(alias + "younger_bound_imprecision"),
                rs.getBigDecimal(alias + "older_bound"), rs.getBigDecimal(alias + "older_bound_imprecision"), null);
    }
    
    static Integer getInteger(ResultSet rs, String strColName) throws SQLException {
        int nValue = rs.getInt(strColName);
        return rs.wasNull() ? null : nValue;
    }
    
    static Float getFloat(ResultSet rs, String strColName) throws SQLException {
        float nValue = rs.getFloat(strColName);
        return rs.wasNull() ? null : nValue;
    }
    
    static Boolean getBoolean(ResultSet rs, String strColName) throws SQLException {
        boolean nValue = rs.getBoolean(strColName);
        return rs.wasNull() ? null : nValue;
    }
    
}
