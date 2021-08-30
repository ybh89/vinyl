package com.hansung.vinyl.security;

import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.application.Oauth2UserService;
import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.security.factory.UrlPathMapFactoryBean;
import com.hansung.vinyl.security.filter.*;
import com.hansung.vinyl.security.handler.JwtAuthenticationEntryPoint;
import com.hansung.vinyl.security.handler.JwtAuthenticationFailureHandler;
import com.hansung.vinyl.security.handler.JwtAuthenticationSuccessHandler;
import com.hansung.vinyl.security.handler.OAuth2AuthenticationSuccessHandler;
import com.hansung.vinyl.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AccountService accountService;
    private final Oauth2UserService oauth2UserService;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(permitAllFilter(), FilterSecurityInterceptor.class)
                ;

        http
                .oauth2Login()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .userInfoEndpoint()
                .userService(oauth2UserService)
                ;
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
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

    /*@Bean
    public OAuth2LoginAuthenticationFilter oAuth2LoginAuthenticationFilter() {
        OAuth2LoginAuthenticationFilter oAuth2LoginAuthenticationFilter =
                new OAuth2LoginAuthenticationFilter(clientRegistrationRepository, oAuth2AuthorizedClientService);
        oAuth2LoginAuthenticationFilter.setAuthenticationSuccessHandler();
        oAuth2LoginAuthenticationFilter.set
    }*/

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(jwtProvider());
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
