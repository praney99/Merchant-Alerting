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
        // This is only a helper method.  If you want to hand craft the HttpSecurity object
        // then no worries, just don't call this and copy the code inside of it, and adjust
        // exactly as you need it.
        THDIdentityHelper.defaultHTTPConfig(http, thdIdentityConfig);

    }


}