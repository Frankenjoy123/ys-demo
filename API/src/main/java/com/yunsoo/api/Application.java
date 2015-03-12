package com.yunsoo.api;

import com.yunsoo.api.security.StatelessAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Filter;
import java.util.*;

@SpringBootApplication
//@Import(StatelessAuthenticationSecurityConfig.class)
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        //set throw exception if no handler mapping
        Object dispatcherServlet = context.getBean("dispatcherServlet");
        if (dispatcherServlet != null && dispatcherServlet instanceof DispatcherServlet) {
            ((DispatcherServlet) dispatcherServlet).setThrowExceptionIfNoHandlerFound(true);
        }

        System.out.println("Run API by Spring Boot. Successfully started...");
        test();
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    public static void test() {
        List<Dog> list = new ArrayList<Dog>();

        list.add(new Dog("Shaggy", 3));
        list.add(new Dog("Lacy", 2));
        list.add(new Dog("Roger", 10));
        list.add(new Dog("Tommy", 4));
        list.add(new Dog("Tammy", 1));
        Collections.sort(list);// Sorts the array list

        for (Dog a : list)//printing the sorted list of names
            System.out.print(a.getDogName() + ", ");

        // Sorts the array list using comparator
        Collections.sort(list, new Dog());
        System.out.println(" --wait---- ");
        for (Dog a : list)//printing the sorted list of ages
            System.out.println(a.getDogName() + "  : " +
                    a.getDogAge() + ", ");
    }

    protected static class Dog implements Comparator<Dog>, Comparable<Dog> {
        private String name;
        private int age;

        Dog() {
        }

        Dog(String n, int a) {
            name = n;
            age = a;
        }

        public String getDogName() {
            return name;
        }

        public int getDogAge() {
            return age;
        }

        // Overriding the compareTo method
        public int compareTo(Dog d) {
            return (this.name).compareTo(d.name);
        }

        // Overriding the compare method to sort the age
        public int compare(Dog d, Dog d1) {
            return d.age - d1.age;
        }
    }
}
