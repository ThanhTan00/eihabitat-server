package com.eihabitat.eihabitat_server.configuration;

import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import com.eihabitat.eihabitat_server.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINT = {"/login/oauth2/code/google","/oauth2/authorization/google","/users","/auth/token","/auth/introspect", "/auth/logout", "/auth/refresh", "/auth/loginWithGG/**", "/ws/**","/users/demo/**", "/users/testCreateUser"};

    private CustomJwtDecoder customJwtDecoder;

    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Bean(name = "securityFilterChainOauth2")
    @Order(1)
    public SecurityFilterChain securityFilterChainOauth2(HttpSecurity http) throws Exception {
        http.securityMatcher("/login/**", "/oauth2/**")
            .authorizeHttpRequests(registry->{
                    registry.requestMatchers("/").permitAll();
                    registry.requestMatchers("/oauth2/**").permitAll();
                    registry.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll();
                    registry.requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT).permitAll();
                    registry.anyRequest().authenticated();
                })
                .oauth2Login(oauth2login->{
                    oauth2login.successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
                            String email = authToken.getPrincipal().getAttribute("email");
                            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

                            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                                    .subject(email)
                                    .issuer("eihabitat")
                                    .issueTime(new java.util.Date())
                                    .expirationTime(new Date(
                                            Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()
                                    ))
                                    .jwtID(UUID.randomUUID().toString())
                                    .build();
                            Payload payload = new Payload(jwtClaimsSet.toJSONObject());

                            JWSObject jwsObject = new JWSObject(header, payload);
                            String token="";
                            try {
                                jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
                                token = jwsObject.serialize();
                            } catch (JOSEException e) {
                                log.error("Cannot sign JWT object", e);
                                throw new RuntimeException(e);
                            }
                            response.sendRedirect("http://localhost:3000/loginWithGoogle?email="+email+"&token="+token);
                        }
                    });
                })
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean(name = "securityFilterChainJwt")
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/auth/**", "/users/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers("/auth/confirm-email").permitAll()
                        .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)

                        .jwtAuthenticationConverter(jwtConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000/");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

    @Bean
    JwtAuthenticationConverter jwtConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
