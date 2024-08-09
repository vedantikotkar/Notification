//package com.example.NotificationSender.config;
//
//import org.keycloak.adapters.KeycloakConfigResolver;
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
//import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
//import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class KeycloakConfig {
//
//    @Bean
//    public KeycloakConfigResolver keycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
//
//    @Bean
//    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new NullAuthenticatedSessionStrategy();
//    }
//
//    @Bean
//    public KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
//        KeycloakAuthenticationProvider keycloakAuthenticationProvider = new KeycloakAuthenticationProvider();
//        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
//        return keycloakAuthenticationProvider;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/notifications/email").hasRole("USER")
//                        .anyRequest().permitAll()
//                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling.accessDeniedHandler(customAccessDeniedHandler()) // Register custom access denied handler
//                )
//                .csrf(csrf -> csrf.disable()); // Disable CSRF if not needed
//
//        return http.build();
//    }
//
//    @Bean
//    public CustomAccessDeniedHandler customAccessDeniedHandler() {
//        return new CustomAccessDeniedHandler(); // Register your custom access denied handler bean
//    }
//}



package com.example.NotificationSender.config;

//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.keycloak.adapters.KeycloakConfigResolver;
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//@Configuration
//@EnableWebSecurity
//public class KeycloakConfig {
//
//    @Bean
//    public KeycloakConfigResolver keycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
//
//    @Bean
//    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Allow anonymous access to Swagger UI and
//                        .requestMatchers("/notifications/email").hasRole("USER")
//                        .anyRequest().permitAll()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                        )
//                )
//                .csrf(csrf -> csrf.disable()); // Disable CSRF if not needed
//        return http.build();
//    }
//
//    @Bean
//    public OncePerRequestFilter cspFilter() {
//        return new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//                    throws ServletException, IOException {
//                response.setHeader("Content-Security-Policy", "script-src 'self'; object-src 'none'; base-uri 'self';");
//                filterChain.doFilter(request, response);
//            }
//        };
//    }
//
//    // Register the CSP filter with an order
//    @Bean
//    public FilterRegistrationBean<OncePerRequestFilter> cspFilterRegistrationBean() {
//        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>(cspFilter());
//        registrationBean.setOrder(1); // Set the order
//        return registrationBean;
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        String issuerUri = "http://localhost:8080/realms/Java"; // Update with your issuer URI
//        return NimbusJwtDecoder.withJwkSetUri(issuerUri + "/protocol/openid-connect/certs").build();
//    }
//    private JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//            Collection<GrantedAuthority> authorities = new ArrayList<>();
//            Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");
//
//            if (resourceAccess != null) {
//                Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get("springboot-keycloak");
//                if (clientRoles != null) {
//                    List<String> roles = (List<String>) clientRoles.get("roles");
//                    if (roles != null) {
//                        roles.forEach(role -> {
//                            // Ensure the role is prefixed with "ROLE_" to match Spring Security's expectations
//                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//                        });
//                    }
//                }
//            }
//
//            // Debugging: Log the extracted authorities
//            System.out.println("Extracted authorities: " + authorities);
//            return authorities;
//        });
//        return converter;
//    }
//}




import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class KeycloakConfig {

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow access to Swagger UI without authentication
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Require USER role for the notifications/email endpoint
                        .requestMatchers("/notifications/email").hasRole("USER")
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Your custom JWT converter
                        )
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF if not needed
        return http.build();
    }
    @Bean
    public OncePerRequestFilter cspFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                response.setHeader("Content-Security-Policy", "script-src 'self'; object-src 'none'; base-uri 'self';");
                filterChain.doFilter(request, response);
            }
        };
    }

    // Register the CSP filter with an order
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> cspFilterRegistrationBean() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>(cspFilter());
        registrationBean.setOrder(1); // Set the order
        return registrationBean;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String issuerUri = "http://localhost:8080/realms/Java"; // Update with your issuer URI
        return NimbusJwtDecoder.withJwkSetUri(issuerUri + "/protocol/openid-connect/certs").build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");

            if (resourceAccess != null) {
                Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get("springboot-keycloak");
                if (clientRoles != null) {
                    List<String> roles = (List<String>) clientRoles.get("roles");
                    if (roles != null) {
                        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                    }
                }
            }

            // Debugging: Log the extracted authorities
            System.out.println("Extracted authorities: " + authorities);
            return authorities;
        });
        return converter;
    }
}
