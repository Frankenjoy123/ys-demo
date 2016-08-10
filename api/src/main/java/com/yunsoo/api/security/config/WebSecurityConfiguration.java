package com.yunsoo.api.security.config;

import com.yunsoo.api.security.authentication.AuthenticationFilter;
import com.yunsoo.api.security.authentication.TokenAuthenticationService;
import com.yunsoo.api.security.authentication.TokenInvalidAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .antMatchers(HttpMethod.GET, "/health/**").permitAll()
                .antMatchers(HttpMethod.GET, "/debug/**").access(debug ? "permitAll" : "authenticated")
                .antMatchers(HttpMethod.GET, "/application/*/config").permitAll()
                .antMatchers(HttpMethod.GET, "/organization/public/config").permitAll()
                .antMatchers(HttpMethod.GET, "/organization/*/logo/**").permitAll()
                .antMatchers(HttpMethod.GET, "/image/*").permitAll()

                .antMatchers("/attachment/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/organization/*/brand_logo").permitAll()
                .antMatchers(HttpMethod.POST, "/brand/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/brand/*").permitAll()
                .antMatchers(HttpMethod.GET, "/brand/attachment/**").permitAll()
                .antMatchers(HttpMethod.POST, "/brand").permitAll()

                .antMatchers(HttpMethod.GET, "/payment/brand/alipay/**").permitAll()
                .antMatchers(HttpMethod.POST, "/marketing/alipay/notify").permitAll()
                .anyRequest().authenticated().and()

                // custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new AuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
    }

}
