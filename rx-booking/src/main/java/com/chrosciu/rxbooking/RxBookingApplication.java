package com.chrosciu.rxbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class RxBookingApplication {

    public static void main(String[] args) {
        BlockHound.install();
        SpringApplication.run(RxBookingApplication.class, args);
    }

}
