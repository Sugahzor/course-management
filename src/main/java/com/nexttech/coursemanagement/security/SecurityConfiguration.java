package com.nexttech.coursemanagement.security;

import com.nexttech.coursemanagement.security.properties.CorsProperties;
import com.nexttech.coursemanagement.util.RoleConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {
    public static final String ROLE_PREFIX = "ROLE_";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter,
                                           CorsProperties corsProperties) throws Exception{
        http.csrf().disable();
        http.cors().configurationSource(request -> {
            final var configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Optional.ofNullable(corsProperties.getAllowedOrigins()).orElseGet(Collections::emptyList));
            configuration.setAllowedMethods(Optional.ofNullable(corsProperties.getAllowedMethods()).orElseGet(Collections::emptyList));
            configuration.setAllowedHeaders(Optional.ofNullable(corsProperties.getAllowedHeaders()).orElseGet(Collections::emptyList));
            configuration.setExposedHeaders(Optional.ofNullable(corsProperties.getExposedHeaders()).orElseGet(Collections::emptyList));

            return configuration;
        });
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests()
                .antMatchers("/api/v1/auth/login", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
                    .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/users")
                    .hasRole(RoleConstants.ADMIN)
                .antMatchers("/api/v1/users/info")
                    .hasAnyRole(RoleConstants.ADMIN, RoleConstants.PROFESSOR, RoleConstants.STUDENT, RoleConstants.USER)
                .antMatchers(HttpMethod.GET, "/api/v1/courses", "/api/v1/lessons")
                    .hasAnyRole(RoleConstants.ADMIN,RoleConstants.PROFESSOR, RoleConstants.STUDENT)
                .antMatchers(HttpMethod.PUT, "/api/v1/courses")
                    .hasAnyRole(RoleConstants.ADMIN, RoleConstants.PROFESSOR)
                .antMatchers(HttpMethod.POST, "/api/v1/courses")
                    .hasAnyRole(RoleConstants.ADMIN, RoleConstants.PROFESSOR)
                .antMatchers(HttpMethod.DELETE, "/api/v1/courses")
                    .hasAnyRole(RoleConstants.ADMIN, RoleConstants.PROFESSOR)
                .anyRequest()
                .authenticated();
        http.exceptionHandling()
                .authenticationEntryPoint(
                        ((request, response, authException) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                authException.getMessage()
                        ))
                );
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Disable ROLE_ prefix for @RolesAllowed annotation
     *
     * @return GrantedAuthorityDefaults
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(ROLE_PREFIX);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}
