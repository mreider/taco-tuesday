package com.tacos.demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ThreadLocalRandom;

@RestController

public class DeliveriesController {

    private static final Logger log = LoggerFactory.getLogger(DeliveriesController.class);

    @GetMapping("/")

    public ResponseEntity<String> rootEndpoint() {
             if (ThreadLocalRandom.current().nextDouble() > 0.8) {
                log.info("Success");
                return ResponseEntity.ok("Success");
            } else {
                for (int i = 0; i < 10; i++) {
                    fibonacci(i);
                }
                log.error("Delivery down");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No deliveries left");
            }
    }


    public static long fibonacci(long n) {
        if (n == 0 || n == 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}