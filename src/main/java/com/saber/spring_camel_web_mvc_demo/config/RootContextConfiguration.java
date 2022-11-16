package com.saber.spring_camel_web_mvc_demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@ComponentScan(
        basePackages = "com.saber.spring_camel_web_mvc_demo.site"
        ,excludeFilters = @ComponentScan.Filter({Controller.class, ControllerAdvice.class})
)
public class RootContextConfiguration {
}
