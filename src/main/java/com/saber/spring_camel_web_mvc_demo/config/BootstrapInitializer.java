package com.saber.spring_camel_web_mvc_demo.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class BootstrapInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext rootContextConfiguration = new AnnotationConfigWebApplicationContext();
        rootContextConfiguration.register(RootContextConfiguration.class);

        servletContext.addListener(new ContextLoaderListener(rootContextConfiguration));

        AnnotationConfigWebApplicationContext   webContextConfiguration = new AnnotationConfigWebApplicationContext();
        rootContextConfiguration.register(AppConfig.class,SwaggerConfig.class);

        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("springDispatcherServlet",new DispatcherServlet(webContextConfiguration));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");
    }
}
