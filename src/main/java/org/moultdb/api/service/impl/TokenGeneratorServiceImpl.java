package org.moultdb.api.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.service.TokenGeneratorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
@Service
public class TokenGeneratorServiceImpl implements TokenGeneratorService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    private static final long LONG_TOKEN_VALIDITY = 7* 24 * 60 * 60 * 1000; // 7 jours
    
    private static final long MIDDLE_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 h
    
    private static final long SHORT_TOKEN_VALIDITY = 10 * 60 * 1000; // 10 minutes
    
    @Override
    public String generateLongExpirationToken(String email) {
        return generateToken(email, LONG_TOKEN_VALIDITY);
    }
    
    @Override
    public String generateMiddleExpirationToken(String email) {
        return generateToken(email, MIDDLE_TOKEN_VALIDITY);
    }
    
    @Override
    public String generateShortExpirationToken(String email) {
        return generateToken(email, SHORT_TOKEN_VALIDITY);
    }
    
    private String generateToken(String email, long validity) {
        return Jwts.builder()
                   .setHeaderParam("typ","JWT")
                   .setSubject(email)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + validity))
                   .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                   .compact();
    }
    
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    
    /**
     * Retrieve username from JWT token.
     *
     * @param token A {@code String} that is the token to decode.
     * @return      The {@code String} that is the username represented by the token.
     */
    private String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Retrieve the {@code Claims} allowing to retrieve any information from the provided token.
     *
     * @param token A {@code String} that is the token to decode.
     * @return      The {@code Claims} from the token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }
    
    /**
     * Check if the token has expired.
     *
     * @param token A {@code String} that is the token to decode.
     * @return      The {@code Boolean} defining whether {@code token} has expired.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    @Override
    public boolean validateToken(String email, String token) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("E-mail is empty");
        }
        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("Token is empty");
        }
        final String username = getUsernameFromToken(token);
        return (username.equals(email) && !isTokenExpired(token));
    }
    
    public static long getShortTokenValidity() {
        return SHORT_TOKEN_VALIDITY;
    }
    
    public static long getMiddleTokenValidity() {
        return MIDDLE_TOKEN_VALIDITY;
    }
    
    public static long getLongTokenValidity() {
        return LONG_TOKEN_VALIDITY;
    }
}
