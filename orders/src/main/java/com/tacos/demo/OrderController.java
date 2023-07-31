package com.tacos.demo;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController

public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping("/{numTacos}")
    public String handleGetRequest(HttpServletRequest request, @PathVariable String numTacos) throws IOException {
        log.info("Received HTTP GET request. Path: {}, Remote Address: {}", request.getRequestURL(), request.getRemoteAddr());
        int tacosInt;
        try {
            tacosInt = Integer.parseInt(numTacos);
        } catch (NumberFormatException e) {
            log.warn("could not parse number from string '{}'. Using 1 instead.", numTacos);
            tacosInt = 1;
        }
        String orderResponse = getOrders(tacosInt);
        
        return "order received: " + orderResponse;
    }

    public String getOrders(int tacos) throws IOException {
        URL url = new URL("http://deliveries:8081/" + tacos);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            String returnMessage = " successfully delivered " + tacos + " tacos";
            log.info(returnMessage);
            return returnMessage;
        } else {
            String returnMessage = " failed to deliver " + tacos + " tacos";
            log.error(returnMessage);
            return returnMessage;
        }
    }
}
