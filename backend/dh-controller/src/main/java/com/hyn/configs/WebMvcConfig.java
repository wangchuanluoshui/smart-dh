package com.hyn.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


/**
 * MVC全局配置
 *
 * @version ： 1.0
 * @Title:：WebMvcConfig.java
 * @Package ：com.summit.homs.global.cfg
 * @Description： TODO
 * @author： hyn
 * @date： 2018年8月16日 下午5:25:18
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


    @Bean
    GlobalInterceptor globalInterceptor() {
        return new GlobalInterceptor();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {

        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用于排除拦截

        registry.addInterceptor(globalInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/v2/api-docs").addResourceLocations("classpath:/META-INF/resources/");
    }

}