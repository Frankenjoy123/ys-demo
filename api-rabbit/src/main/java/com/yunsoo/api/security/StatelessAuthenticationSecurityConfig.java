package com.yunsoo.api.security;

import com.yunsoo.api.security.filter.StatelessAuthenticationFilter;
import com.yunsoo.api.security.filter.StatelessLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by Zhe on 2015/3/4.
 */

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class StatelessAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public StatelessAuthenticationSecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .headers().cacheControl().and()
                .authorizeRequests()

                        //allow anonymous resource requests
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/resources/**").permitAll()

                //allow anonymous to All controller by default
                .antMatchers("/**").permitAll()

                //allow anonymous POSTs to login
                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/account/**").permitAll()

                //allow anonymous GETs to API
                // .antMatchers(HttpMethod.GET, "/api/**").permitAll()

                //defined Admin only API area
                .antMatchers("/admin/**").hasRole("YUNSOO_ADMIN")
                .antMatchers("/productbase/**").hasRole("YUNSOO_ADMIN")
                //.antMatchers("/user").permitAll()

                //all other request need to be authenticated
                .anyRequest().hasRole("COM_USER").and()

                // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
                .addFilterBefore(new StatelessLoginFilter("/api/login", tokenAuthenticationService, accountDetailService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

                        // custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected AccountDetailService userDetailsService() {
        return accountDetailService;
    }
}
