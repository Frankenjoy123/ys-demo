//package com.yunsoo.util;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
///**
// * @Time: 2015.1.6
// * @author Zhe Zhang;
// *
// */
//@ComponentScan
//public class SpringAppContextUtil {
//
//    private static ApplicationContext applicationContext;
//
//    static {
//        try {
//            // Create the SessionFactory from standard (applicationContext.xml.bc)
//            // config file.
//            applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml.bc");
//        } catch (Throwable ex) {
//            //to-do: Log the exception.
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
//
//    public static ApplicationContext getApplicationContext() {
//        return applicationContext;
//    }
//}