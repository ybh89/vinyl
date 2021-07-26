package com.hansung.vinyl.security;

import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.security.infrastructure.filter.JwtAuthorizationFilter;
import com.hansung.vinyl.security.infrastructure.filter.JwtProvider;
import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.security.application.UrlPathMapFactoryBean;
import com.hansung.vinyl.security.domain.PermitAllResourceGroup;
import com.hansung.vinyl.security.infrastructure.filter.JwtAuthenticationFilter;
import com.hansung.vinyl.security.infrastructure.filter.PermitAllFilter;
import com.hansung.vinyl.security.infrastructure.handler.JwtAuthenticationFailureHandler;
import com.hansung.vinyl.security.infrastructure.handler.JwtAuthenticationSuccessHandler;
import com.hansung.vinyl.security.infrastructure.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AccountService accountService;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(permitAllFilter(), FilterSecurityInterceptor.class)
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler());
        return jwtAuthenticationFilter;
    }

    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler(jwtProvider());
    }

    @Bean
    public JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtProvider());
    }

    @Bean
    public PermitAllFilter permitAllFilter() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(PermitAllResourceGroup.values());
        permitAllFilter.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        permitAllFilter.setAccessDecisionManager(affirmativeBased());
        permitAllFilter.setAuthenticationManager(authenticationManager());
        return permitAllFilter;
    }

    public AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(accessDecisionVoter());
    }

    public List<AccessDecisionVoter<?>> accessDecisionVoter() {
        return Arrays.asList(new RoleVoter());
    }

    @Bean
    public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(urlPathMapFactoryBean().getObject(), authorityService);
    }

    private UrlPathMapFactoryBean urlPathMapFactoryBean() {
        UrlPathMapFactoryBean urlPathMapFactoryBean = new UrlPathMapFactoryBean(authorityService);
        return urlPathMapFactoryBean;
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(accountService);
    }
}
