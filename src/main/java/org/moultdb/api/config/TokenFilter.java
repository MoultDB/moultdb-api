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
import org.moultdb.api.model.moutldbenum.Role;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.moultdb.api.config.TokenFilterConfig.SECURED_ADMIN_URL;
import static org.moultdb.api.config.TokenFilterConfig.SECURED_USER_URL;

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
            response.getWriter().write("No token or token does not begin with 'Bearer '");
            response.setStatus(403);
            return;
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
        
        // Store the token in TokenHolder
        UserHolder.setUsername(claims.getSubject());
        
        if(!hasAuthorizedRole(request, claims, SECURED_ADMIN_URL, Role.ROLE_ADMIN) 
            || !hasAuthorizedRole(request, claims, SECURED_USER_URL, Role.ROLE_USER)) {
            response.getWriter().write("Access Denied. Insufficient privileges for this URL [" + request.getRequestURI() +
                    "] with these privileges [" + claims.get("roles", List.class) + "]");
            response.setStatus(403);
            return;
        }
        
        try {
            // Continue filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Clean the token once the request has been processed
            UserHolder.clearUsername();
        }
    }
    
    private boolean hasAuthorizedRole(HttpServletRequest request, Claims claims, String[] securedUrl, Role role) {
        return Arrays.stream(securedUrl).noneMatch(u -> Pattern.compile(u).matcher(request.getRequestURI()).find())
                || claims == null
                || claims.get("roles", List.class).contains(role.getStringRepresentation());
    }
}