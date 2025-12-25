package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            // Public endpoints
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/public/**").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            
            // Swagger documentation endpoints
            .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
            
            // Patient endpoints - authenticated
            .antMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("CLINICIAN", "ADMIN", "HEALTH_ASSISTANT")
            .antMatchers(HttpMethod.POST, "/api/patients/**").hasAnyRole("CLINICIAN", "ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/patients/**").hasAnyRole("CLINICIAN", "ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("ADMIN")
            
            // Symptom log endpoints
            .antMatchers("/api/symptoms/**").hasAnyRole("CLINICIAN", "ADMIN", "HEALTH_ASSISTANT")
            
            // Recovery curve endpoints
            .antMatchers(HttpMethod.GET, "/api/recovery-curves/**").hasAnyRole("CLINICIAN", "ADMIN", "HEALTH_ASSISTANT")
            .antMatchers(HttpMethod.POST, "/api/recovery-curves/**").hasAnyRole("ADMIN")
            
            // Deviation rules endpoints
            .antMatchers(HttpMethod.GET, "/api/rules/**").hasAnyRole("CLINICIAN", "ADMIN")
            .antMatchers(HttpMethod.POST, "/api/rules/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/rules/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/rules/**").hasRole("ADMIN")
            
            // Alert endpoints
            .antMatchers("/api/alerts/**").hasAnyRole("CLINICIAN", "ADMIN")
            
            // User management
            .antMatchers("/api/users/**").hasRole("ADMIN")
            
            // All other endpoints require authentication
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().disable(); // For H2 console

        // Add JWT filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}