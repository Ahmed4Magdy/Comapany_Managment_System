//package com.example.demo.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
////@Configuration
////@EnableWebSecurity
//
////public class SecurityConfig {
//
//
////
//////    @Bean
//////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//////
//////        http
//////                .csrf(csrf -> csrf.disable()) // disable CSRF for APIs/testing
//////                .authorizeHttpRequests(auth -> auth
//////                        // Permit all requests to department and employee endpoints
//////                        .requestMatchers("/department/**").permitAll()
//////                        .requestMatchers("/employee/**").permitAll()
//////                        // Any other endpoint requires authentication
//////                        .anyRequest().authenticated()
//////                )
//////
//////
//////        return http.build();
//////    }
////}
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())        // disables CSRF for API testing
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults()); // enables Basic Auth
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
//
//
