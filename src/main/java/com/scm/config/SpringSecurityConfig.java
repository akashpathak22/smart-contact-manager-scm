package com.scm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.service.Implementation.SecurityUserDetailService;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final OAuthSuccessHandler handler;
    private final SecurityUserDetailService securityUserDetailService;
    private final LoginFailureHandler loginFailureHandler;


    public SpringSecurityConfig(SecurityUserDetailService securityUserDetailService, OAuthSuccessHandler handler, LoginFailureHandler loginFailureHandler) {
        this.securityUserDetailService = securityUserDetailService;
        this.handler = handler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(securityUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ✅ ADD THIS - Allow static resources
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/favicon.ico", "/webjars/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
              .requestMatchers("/css/**","/assest/address-book.png", "/js/**", "/images/**", "/static/**").permitAll()
            .requestMatchers("/", "/home", "/register", "/login", "/do-register" ).permitAll()
            .requestMatchers("/auth/verify-email/**").permitAll()
            .requestMatchers("/user/**").authenticated()
            .anyRequest().authenticated()
            );
        
        http.formLogin(form -> {
            form.loginPage("/login");
            form.loginProcessingUrl("/authentication");
            form.defaultSuccessUrl("/user/dashboard", true);
            form.failureUrl("/login?error=true");
            form.usernameParameter("email");
            form.passwordParameter("password");
            form.permitAll();  // ✅ ADD THIS

            form.failureHandler(loginFailureHandler);

            
        });
        
        http.csrf(csrf -> csrf.disable());

        http.logout(logout -> {
            logout.logoutUrl("/do-logout");
            logout.logoutSuccessUrl("/login?logout=true");
            logout.permitAll();  // ✅ ADD THIS



        });

        http.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(handler);
            oauth.permitAll();  // ✅ ADD THIS
        });

        return http.build();
    }
}