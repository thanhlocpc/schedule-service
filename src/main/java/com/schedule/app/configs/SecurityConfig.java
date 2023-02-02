package com.schedule.app.configs;

import com.schedule.app.security.AuthenticationFilter;
import com.schedule.app.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(tokenAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://thanhloc.azurewebsites.net");
        config.addAllowedOrigin("http://localhost:8585");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:3031");
        config.addAllowedOrigin("https://schedule-web-bbbd5.web.app");
        config.addAllowedOrigin("https://admin-schedule-web.web.app");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
//        httpSecurity.addFilterBefore(new AuthenticationFilter(authenticationManager(), Constants.HEADER_TOKEN_NAME), UsernamePasswordAuthenticationFilter.class)
//                .csrf()
//                .disable();
//                .and()
//                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        httpSecurity
                .cors().configurationSource(source)
                .and()
                .csrf().disable().authorizeRequests().antMatchers(Constants.AUTH_SERVICE_URL + "/register").permitAll()
                .and()
                .csrf().disable()
                .addFilterBefore(
                        new AuthenticationFilter(authenticationManager(), Constants.HEADER_TOKEN_NAME),
                        BasicAuthenticationFilter.class);
//        ;
    }



}
