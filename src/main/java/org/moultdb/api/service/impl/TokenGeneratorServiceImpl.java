package org.moultdb.api.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.moultdb.api.model.User;
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
    
    private static final long LONG_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 h
    
    private static final long SHORT_TOKEN_VALIDITY = 10 * 60 * 1000; // 10 minutes
    
    @Override
    public String generateLongExpirationToken(User user) {
        return generateToken(user.getEmail(), LONG_TOKEN_VALIDITY);
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
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
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
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public static long getShortTokenValidity() {
        return SHORT_TOKEN_VALIDITY;
    }
}
