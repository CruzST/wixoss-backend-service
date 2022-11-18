package com.wixossdeckbuilder.backendservice.config;

import com.wixossdeckbuilder.backendservice.config.filter.JWTTokenGeneratorFilter;
import com.wixossdeckbuilder.backendservice.config.filter.JWTTokenValidatorFilter;
import com.wixossdeckbuilder.backendservice.model.enums.CustomRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Configuration
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    JWTTokenValidatorFilter jwtTokenValidatorFilter;

    @Autowired
    JWTTokenGeneratorFilter jwtTokenGeneratorFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200")); // subject to change
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                })
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(jwtTokenGeneratorFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers("/api/deck/**").hasAnyAuthority(CustomRole.ADMIN.toString(), CustomRole.PUBLIC_USER.toString(), CustomRole.REGISTERED_USER.toString())
                    //.antMatchers("/api/deck/**").hasAnyAuthority(CustomRole.REGISTERED_USER.toString()) // For testing authorities
                    .antMatchers("/api/card/new").hasAnyAuthority(CustomRole.ADMIN.toString())
                    .antMatchers("/api/card/**").hasAnyAuthority(CustomRole.ADMIN.toString(), CustomRole.PUBLIC_USER.toString(), CustomRole.REGISTERED_USER.toString())
                    .antMatchers("/api/auth/register").permitAll()
                    .antMatchers("/api/auth/login/**").permitAll();
                //.and().formLogin().and().httpBasic();
        return http.build();
    }
}
