package com.myowncompany.account.manager.config;

import com.myowncompany.account.manager.ws.impl.AccountManagerServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    @Bean
    public ServletRegistrationBean<Servlet> servletRegistrationBean(ApplicationContext context) {
        return new ServletRegistrationBean<Servlet>(new CXFServlet(), "/ws/*");
    }

    @Bean
    public Endpoint wsAccountService() {
        EndpointImpl endpoint = new EndpointImpl(bus, new AccountManagerServiceImpl());
        endpoint.publish("/account-manager");
        return endpoint;
    }
}
