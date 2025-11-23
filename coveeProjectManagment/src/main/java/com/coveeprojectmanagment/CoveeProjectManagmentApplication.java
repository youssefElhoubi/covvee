package com.coveeprojectmanagment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class CoveeProjectManagmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoveeProjectManagmentApplication.class, args);
    }

}
