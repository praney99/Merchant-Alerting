package com.homedepot.mm.pc.merchantalerting.configuration;

import com.homedepot.appsecurecommunity.resourceserver.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static com.homedepot.appsecurecommunity.resourceserver.THDIdentityHelper.isNullOrEmpty;

@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {


@Autowired
    private final THDIdentityConfig thdIdentityConfig;


    public SecurityConfig(THDIdentityConfig config) {
        this.thdIdentityConfig = config;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // This is only a helper method.  If you want to hand craft the WebSecurity object
        // then no worries, just don't call this and copy the code inside of it, and adjust
        // exactly as you need it.
        THDIdentityHelper.defaultWebConfig(web, thdIdentityConfig);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        THDAuthenticationManagerResolver amr = new THDAuthenticationManagerResolver(thdIdentityConfig);
        THDBearerTokenResolver thdBTR = new THDBearerTokenResolver();

        http
                .csrf().disable()
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );


        if  (thdIdentityConfig.getResourceServer().isEnabled()) {
            http.oauth2ResourceServer(resourceServer -> resourceServer
                    .authenticationManagerResolver(new THDJwtIssuerAuthenticationManagerResolver(amr))
                    .bearerTokenResolver(thdBTR)
            );

            for (THDIdentityConfig.ResourceServer.PathCheck pathCheck : thdIdentityConfig.getResourceServer().getPathChecks()) {

                if (!isNullOrEmpty(pathCheck.getMethod()) && !isNullOrEmpty(pathCheck.getPath())) {
                    // if given both a verb and a path
                    http.authorizeRequests().antMatchers(HttpMethod.resolve(pathCheck.getMethod()), pathCheck.getPath()).hasAnyAuthority(pathCheck.getNeededAuthorities());
                } else if (!isNullOrEmpty(pathCheck.getMethod()) && isNullOrEmpty(pathCheck.getPath())) {
                    // if given a verb but no path
                    http.authorizeRequests().antMatchers(HttpMethod.resolve(pathCheck.getMethod())).hasAnyAuthority(pathCheck.getNeededAuthorities());
                } else if (isNullOrEmpty(pathCheck.getMethod()) && !isNullOrEmpty(pathCheck.getPath())) {
                    // if path but no verb
                    http.authorizeRequests().antMatchers(pathCheck.getPath()).hasAnyAuthority(pathCheck.getNeededAuthorities());
                }
            }

        }

    }


}