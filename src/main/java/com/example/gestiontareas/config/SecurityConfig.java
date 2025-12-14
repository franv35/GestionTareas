package com.example.gestiontareas.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.example.gestiontareas.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 🔥 JWT → sin CSRF
            .csrf(csrf -> csrf.disable())

            // 🌍 CORS
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of(
                        "http://127.0.0.1:5500"
                ));
                config.setAllowedMethods(List.of(
                        "GET", "POST", "PUT", "DELETE", "OPTIONS"
                ));
                config.setAllowedHeaders(List.of(
                        "Authorization", "Content-Type"
                ));
                config.setAllowCredentials(false);
                return config;
            }))

            // 🔐 Stateless
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔓 Autorización
            .authorizeHttpRequests(auth -> auth

                // ===== AUTH PÚBLICA =====
                .requestMatchers(
                        "/api/usuarios/login",
                        "/api/usuarios/register"
                ).permitAll()

                // ===== ARCHIVOS ESTÁTICOS =====
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/login.html",
                        "/registro.html",
                        "/proyectos.html",

                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico"
                ).permitAll()

                // ===== PREFLIGHT =====
                .requestMatchers("OPTIONS", "/**").permitAll()

                // ===== API PROTEGIDA =====
                .requestMatchers(
                        "/api/proyectos/**",
                        "/api/tareas/**",
                        "/api/recursos/**",
                        "/api/usuarios/**"
                ).authenticated()

                // ===== TODO LO DEMÁS =====
                .anyRequest().authenticated()
            )

            // 🔑 JWT Filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
