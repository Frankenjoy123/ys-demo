package com.yunsoo;

import com.yunsoo.config.DBSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by:   Lijian
 * Created on:   2015/3/19
 * Descriptions: It's added only for avoid bootRepackage issue during building.
 */
public class Application {

    public static void main(String[] args) {
        System.out.println("Do no thing in data-service main method");

    }
}
