package com.abhay.bajaj_test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BajajTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BajajTestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            try {
                // 1) Call generateWebhook
                String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

                Map<String, String> body = new HashMap<>();
                body.put("name", "Abhay Kumar");
                body.put("regNo", "22BCE11152");
                body.put("email", "abhaysingh1090@gmail.com");

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

                ResponseEntity<Map> response =
                        restTemplate.postForEntity(generateUrl, request, Map.class);

                Map<String, Object> responseBody = response.getBody();
                if (responseBody == null) {
                    System.out.println("No response body from generateWebhook");
                    return;
                }

                String webhook = (String) responseBody.get("webhook");
                String accessToken = (String) responseBody.get("accessToken");
                System.out.println("webhook = " + webhook);
                System.out.println("accessToken = " + accessToken);

                // 2) TODO: put actual SQL for Question 2 here after you read the Google Drive file
                String finalQuery = "SELECT 1;";

                // 3) Submit final SQL query
                String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

                HttpHeaders submitHeaders = new HttpHeaders();
                submitHeaders.setContentType(MediaType.APPLICATION_JSON);
                submitHeaders.set("Authorization", accessToken);

                Map<String, String> submitBody = new HashMap<>();
                submitBody.put("finalQuery", finalQuery);

                HttpEntity<Map<String, String>> submitRequest =
                        new HttpEntity<>(submitBody, submitHeaders);

                ResponseEntity<String> submitResponse =
                        restTemplate.postForEntity(submitUrl, submitRequest, String.class);

                System.out.println("Submit status = " + submitResponse.getStatusCode());
                System.out.println("Submit body   = " + submitResponse.getBody());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}