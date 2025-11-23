package com.covveeauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class CovveeAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CovveeAuthServiceApplication.class, args);
    }

}
