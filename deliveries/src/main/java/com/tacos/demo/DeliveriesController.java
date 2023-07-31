package com.tacos.demo;
import org.slf4j.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

import java.io.IOException;
import java.util.Random;

@RestController

public class DeliveriesController {

    private static final Logger log = LoggerFactory.getLogger(DeliveriesController.class);

    @GetMapping("/{numTacos}")
    
    public String handleGetRequest(HttpServletRequest request, @PathVariable String numTacos, HttpServletResponse response) throws IOException {
   
        Counter orders = Metrics.counter("orders");
        int tacosInt;

        try {
            tacosInt = Integer.parseInt(numTacos);
        } catch (NumberFormatException e) {
            log.warn("could not parse number from string '{}'. Using 1 instead.", numTacos);
            tacosInt = 1;
        }

        try {

            Counter successfulDeliveries = Metrics.counter("deliveries", "success", "true");
            Counter unsuccessfulDeliveries = Metrics.counter("deliveries", "success", "false");
            orders.increment(tacosInt);
            Random random = new Random();
            double probability = 0.98;
                if (random.nextDouble() < probability){
                    log.info("successfully delivered " + tacosInt + " tacos");
                    successfulDeliveries.increment(tacosInt);
                } else {
                    for (int i = 0; i < 40; i++) {
                        fibonacci(i);
                    }
                    log.error("unsuccessful delivery of " + tacosInt + " tacos");
                    unsuccessfulDeliveries.increment(tacosInt);
                    throw new Exception("Delivery failure");
                }

        } catch (Exception e) {
        
             response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // Write the error message to the response body
            try {
                response.getWriter().write("An internal server error occurred.");
            } catch (IOException ex) {
                // Handle the exception if unable to write to the response
                ex.printStackTrace();
            }

    }
    return "Success";

    }

    public static long fibonacci(long n) {
        if (n == 0 || n == 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}