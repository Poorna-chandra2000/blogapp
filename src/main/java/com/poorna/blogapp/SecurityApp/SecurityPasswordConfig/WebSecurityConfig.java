package com.poorna.blogapp.SecurityApp.SecurityPasswordConfig;


import com.poorna.blogapp.SecurityApp.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;




@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
   // private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private static final String[] publicRoutes = {
            "/error", "/auth/**", "/home.html","/ws",         // 👈 Add this explicitly
            "/ws/**",      // 👈 SockJS fallback uses /ws/** path
            "/app/**",     // 👈 STOMP client may send messages to /app/...
            "/user/**"     // 👈 STOMP subscriptions to /user/queue/...
    };

    //cors for security
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedOrigin("http://127.0.0.1:5500"); // Allow your frontend's URL
        configuration.addAllowedOrigin("http://localhost:5173"); // Allow your frontend's URL
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies, Authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Add this line for cors
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicRoutes).permitAll()//in postman for public routes keep Authorization as No Auth
                        //.requestMatchers("/blog/upload")
                        //to add role
                        .requestMatchers("/blog/upload").hasRole("ADMIN")
                        .requestMatchers("/Share/**").permitAll()
                        //.requestMatchers("/blog/**").authenticated()
                        .anyRequest().authenticated())
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);//theCustom Jwt bearer filter
//                .oauth2Login(oauth2Config -> oauth2Config
//                        .failureUrl("/login?error=true")
//                        .successHandler(oAuth2SuccessHandler)
//                );
//                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }


}
