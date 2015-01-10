package com.yunsoo.dao.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Time: 2015.1.6
 * @author Zhe Zhang;
 * 
 */
public class SpringDaoUtil {
    
    private static ApplicationContext applicationContext;
    
    static {
        try {
            // Create the SessionFactory from standard (applicationContext.xml) 
            // config file.           
            applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        } catch (Throwable ex) {
            //to-do: Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}