//package com.example.demo.security;
package com.example.demo.service.impl;
import com.example.demo.exceptionhandling.ErrorResponse;
import com.example.demo.security.JwtAuthFilter;
import com.example.demo.security.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
@Profile("!test")
public class SecurityConfig {


    @Bean
    public AuthenticationProvider authenticationProvider(UserInfoService userInfoService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userInfoService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }





    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthFilter jwtAuthFilter,
                                                   UserInfoService userInfoService)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(new AntPathRequestMatcher("/auth/employee/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/employee/**")).authenticated()

                        .requestMatchers(new AntPathRequestMatcher("/department/**")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/project/**")).authenticated()


                        .requestMatchers(new AntPathRequestMatcher("/task/**")).authenticated()


                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex

                        // ✅ 401 — no token or expired token
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                            String message;

                            if (authException.getClass().getSimpleName().equals("BadCredentialsException")) {
                                message = "Invalid email or password";
                            } else {
                                message = "Token missing or expired";
                            }

                            ErrorResponse error = ErrorResponse.builder()
                                    .status(401)
                                    .message(message)
                                    .path(request.getRequestURI())
                                    .build();

                            new ObjectMapper().writeValue(response.getOutputStream(), error);
                        })

                        // ✅ 403 — valid token but wrong role
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                            ErrorResponse error = ErrorResponse.builder()
                                    .status(403)
                                    .message("Access denied: you don't have permission")
                                    .path(request.getRequestURI())
                                    .build();

                            new ObjectMapper().writeValue(response.getOutputStream(), error);
                        })
                )

                .authenticationProvider(authenticationProvider(userInfoService, passwordEncoder()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }





    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}