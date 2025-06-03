package com.example.skarte.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.skarte.filter.FormAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private FormAuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     // @formatter:off
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/users/new", "/users/add", "/css/**", "/images/**", "/scripts/**", "/error",
                        "/h2-console/**").permitAll() // 誰でもアクセス可
                .requestMatchers("/setting/**").hasRole("ADMIN") // 管理者のみアクセス可
                .anyRequest().authenticated()) // それ以外は認証が必要
        .formLogin(login -> login
                .loginPage("/login") // ログイン画面
                .loginProcessingUrl("/login") // ログイン情報の送信先
                .defaultSuccessUrl("/") // ログイン成功時の遷移先
                .failureUrl("/login-failure") // ログイン失敗時の遷移先
                .permitAll()) // 未ログインでもアクセス可能
        .logout(logout -> logout
                .permitAll())
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        .cors(cors -> cors.disable());
     // @formatter:on
        return http.build();
    }

    public FormAuthenticationProvider userDetailsService() {
        return this.authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
