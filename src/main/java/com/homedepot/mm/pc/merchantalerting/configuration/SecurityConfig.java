package com.homedepot.mm.pc.merchantalerting.configuration;

import com.homedepot.appsecurecommunity.resourceserver.THDIdentityConfig;
import com.homedepot.appsecurecommunity.resourceserver.THDIdentityHelper;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final THDIdentityConfig thdIdentityConfig;

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
        http.cors();
        THDIdentityHelper.defaultHTTPConfig(http, thdIdentityConfig);
    }
}
