package com.yunsoo.api.security;

import com.yunsoo.api.security.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/4
 * Descriptions:
 */
@EnableWebSecurity
@Configuration
@Order(1)
public class TokenAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${yunsoo.debug}")
    private Boolean debug;

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public TokenAuthenticationSecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().authenticationEntryPoint(new TokenInvalidAuthenticationEntryPoint())
                .and()
                .anonymous().and()
                .servletApi().and()
                .headers().frameOptions().disable().xssProtection().disable().and()
                .authorizeRequests()

                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/auth/login/**").permitAll()
                .antMatchers("/marketing/alipay/notify").permitAll()
                .antMatchers("/auth/accesstoken/**").permitAll()
                .antMatchers("/debug/**").access(debug ? "permitAll" : "authenticated")
                .antMatchers(HttpMethod.GET, "/image/*").permitAll()
                .antMatchers(HttpMethod.GET, "/organization/*/logo/*").permitAll()
                .anyRequest().authenticated().and()

                // custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new TokenAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountDetailsService);
    }

    @Override
    protected AccountDetailsService userDetailsService() {
        return accountDetailsService;
    }
}
