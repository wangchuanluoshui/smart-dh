package com.hyn.configs.security.othersys.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
public class OtherSysOauth2LoginAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    // ------------ 用户认证失败处理程序 -------------
    @Autowired
    private OtherSysUserAuthenticationFailureHandler userAuthenticationFailureHandler;

    // ------------ 用户认证成功处理程序 -------------
    @Autowired
    private OtherSysUserAuthenticationSuccessHandler userAuthenticationSuccessHandler;

    @Autowired
    private OtherSysOauth2LoginProvider otherSysOauth2LoginProvider;

    @Autowired
    private OtherSysOauth2LoginFilter otherSysOauth2LoginFilter;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        ProviderManager providerManager =
                new ProviderManager(Collections.singletonList(otherSysOauth2LoginProvider));
        otherSysOauth2LoginFilter.setAuthenticationSuccessHandler(userAuthenticationSuccessHandler);
        otherSysOauth2LoginFilter.setAuthenticationFailureHandler(userAuthenticationFailureHandler);
        otherSysOauth2LoginFilter.setAuthenticationManager(providerManager);

        builder.authenticationProvider(otherSysOauth2LoginProvider).addFilterBefore(otherSysOauth2LoginFilter, UsernamePasswordAuthenticationFilter.class);
        ;

        super.configure(builder);
    }
}

