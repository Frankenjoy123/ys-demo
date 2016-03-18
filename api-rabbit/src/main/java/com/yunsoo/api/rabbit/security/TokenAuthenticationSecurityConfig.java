package com.yunsoo.api.rabbit.security;

import com.yunsoo.api.rabbit.security.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by:   Zhe
 * Created on:   2015/3/4
 * Descriptions:
 */
@EnableWebSecurity
@Configuration
@Order(1)
public class TokenAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

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
                .headers().and()
                .authorizeRequests()

                .antMatchers("/").permitAll()
                //.antMatchers("/favicon.ico").permitAll()
                .antMatchers("/application/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/message/**").permitAll()
                .antMatchers("/organization/**").permitAll()
                .antMatchers("/productbase/**").permitAll()
                .antMatchers("/scan/**").permitAll()
                .antMatchers("/productwarranty/**").permitAll()
                .antMatchers("/webScan/**").permitAll()
                .antMatchers("/marketing/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/*/gravatar").permitAll()
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/swagger-resources").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()

                .anyRequest().authenticated().and()

                .addFilterBefore(new TokenAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}
