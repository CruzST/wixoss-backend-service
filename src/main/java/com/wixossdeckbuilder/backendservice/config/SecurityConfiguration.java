package com.wixossdeckbuilder.backendservice.config;

import com.wixossdeckbuilder.backendservice.config.filter.JTWTokenValidatorFilter;
import com.wixossdeckbuilder.backendservice.config.filter.JWTTokenGeneratorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and().addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                //.addFilterBefore(new JTWTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests()
//                    .antMatchers("/api/deck/**").hasAnyRole(CustomRole.ADMIN.toString(), CustomRole.PUBLIC_USER.toString())
//                    .antMatchers("/api/card/new").hasAnyRole(CustomRole.ADMIN.toString())
//                    .antMatchers("/api/card/update").hasAnyRole(CustomRole.ADMIN.toString())
//                    .antMatchers("/api/card/**").hasAnyRole(CustomRole.ADMIN.toString(), CustomRole.PUBLIC_USER.toString())
//                    .antMatchers("/api/auth/register").permitAll()
                .anyRequest().permitAll()

                .and().formLogin().and().httpBasic();
        return http.build();
    }
}
