package org.moultdb.api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.moultdb.api.exception.AuthenticationException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
public class TokenFilter extends GenericFilterBean {
    
    private final String secret;
    
    public TokenFilter(String secret) {
        this.secret = secret;
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("Authorization");
    
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Token does not begin with Bearer String");
        }
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        final String token = authHeader.substring(7);
        Claims claims = null;
        try {
             claims = Jwts.parserBuilder()
                          .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                          .build()
                          .parseClaimsJws(token)
                          .getBody();
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("Unable to get JWT Token", e);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("Token has expired", e);
        }
        request.setAttribute("claims", claims);
        request.setAttribute("user", servletRequest.getParameter("id"));
        filterChain.doFilter(request, response);
    }
}