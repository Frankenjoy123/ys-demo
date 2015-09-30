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
public class StatelessAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public StatelessAuthenticationSecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(new TokenInvalidAuthenticationEntryPoint())
                .and()
                .anonymous().and()
                .servletApi().and()
                .headers().cacheControl().and()
                .authorizeRequests()

                .antMatchers("/").permitAll()
                //.antMatchers("/favicon.ico").permitAll()
                .antMatchers("/application/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/message/**").permitAll()
                .antMatchers("/organization/**").permitAll()
                .antMatchers("/productbase/**").permitAll()
                .antMatchers("/scan/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/*/gravatar").permitAll()

                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/swagger-resources").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()

                .anyRequest().authenticated().and()
                //.antMatchers("/**").permitAll()

                //allow anonymous POSTs to login
                // .antMatchers(HttpMethod.POST, "/auth/**").permitAll().and()

                //allow anonymous GETs to API
                // .antMatchers(HttpMethod.GET, "/api/**").permitAll()

                //all other request need to be authenticated
//               .anyRequest().hasRole("COM_USER").and()

                // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
                //.addFilterBefore(new StatelessLoginFilter("/auth/login", tokenAuthenticationService, accountDetailService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

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
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}
