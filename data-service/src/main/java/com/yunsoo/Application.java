package com.yunsoo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by:   Lijian
 * Created on:   2015/3/19
 * Descriptions: It's added only for avoid bootRepackage issue during building.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.println("Do no thing in data-service main method");

    }
}
