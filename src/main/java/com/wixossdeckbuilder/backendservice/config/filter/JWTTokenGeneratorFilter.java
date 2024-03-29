// Generator filter only worked with basic auth. couldnt set the security context before this was invoked?

package com.wixossdeckbuilder.backendservice.config.filter;

import com.wixossdeckbuilder.backendservice.config.security.jwt.JWTTokenProvider;
import com.wixossdeckbuilder.backendservice.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    public static final Logger logger = LoggerFactory.getLogger(JWTTokenGeneratorFilter.class);

    @Autowired
    JWTTokenProvider tokenProvider;

    @Value("${jwt.key}")
    private String JWT_KEY;

    @Value("${jwt.header}")
    private String JWT_HEADER;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            /* Commented out because it is not required with the use of the token provider
            SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder()
                    .setIssuer("Cruz")
                    .setSubject("Authentication token for User") // change later?
                    .claim("username", authentication.getName())
                    .claim("authorities", populateAuth(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + SecurityConstants.JWT_EXP_IN_MS)) // 2hrs
                    .signWith(key).compact();
             */
            String jwt = tokenProvider.generateToken(authentication);
            response.addHeader("Access-Control-Expose-Headers", JWT_HEADER);
            response.addHeader(JWT_HEADER, jwt);
        } else {
            logger.info("NO AUTHENTICATION SET");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/api/auth/login/");
    }

    // Not needed if using tokenProvider
    private String populateAuth(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority auth: collection) {
            authoritiesSet.add(auth.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
