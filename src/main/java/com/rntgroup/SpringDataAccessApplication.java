package com.rntgroup;

import com.rntgroup.facade.BookingFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class SpringDataAccessApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SpringDataAccessApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(SpringDataAccessApplication.class, args);
    }

//    public static void main(String[] args) {
//        LOG.info("Method {}#main was called with param: {}", SpringDataAccessApplication.class.getSimpleName(), args);
//
//        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
//        LOG.info("ApplicationContext loaded successfully");
//
//        BookingFacade bookingFacade = context.getBean("bookingFacade", BookingFacade.class);
//    }

}
