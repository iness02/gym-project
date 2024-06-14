package com.example.GymProject.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {AppConfig.class,HibernateConfig.class};// No root configuration for this example
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{AppConfig.class}; // Specify your configuration class
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
