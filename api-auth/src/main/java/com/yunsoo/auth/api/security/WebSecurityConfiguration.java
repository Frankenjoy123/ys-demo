package com.yunsoo.auth.api.security;

import com.yunsoo.auth.api.security.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by:   Lijian
 * Created on:   2016-03-30
 * Descriptions:
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${yunsoo.debug}")
    private Boolean debug;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public WebSecurityConfiguration() {
        super(true); //disableDefaults
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
                .antMatchers("/auth/accesstoken/**").permitAll()
                .antMatchers("/debug/**").access(debug ? "permitAll" : "authenticated")
                .anyRequest().authenticated().and()

                // custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new TokenAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
    }

}
