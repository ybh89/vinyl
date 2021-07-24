package com.hansung.vinyl.security.infrastructure;

import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.infrastructure.JwtProvider;
import com.hansung.vinyl.security.infrastructure.filter.JwtAuthenticationFilter;
import com.hansung.vinyl.security.infrastructure.handler.JwtAuthenticationFailureHandler;
import com.hansung.vinyl.security.infrastructure.handler.JwtAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(POST, "/accounts")
                .permitAll()
                .and()
                .addFilter(customAuthenticationFilter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public JwtAuthenticationFilter customAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler());
        return jwtAuthenticationFilter;
    }

    @Bean
    public JwtAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler(jwtProvider);
    }

    @Bean
    public JwtAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }
}
