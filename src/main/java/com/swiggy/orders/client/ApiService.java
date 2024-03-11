package com.swiggy.orders.client;

import org.springframework.web.client.RestTemplate;

public class ApiService {

    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getDataFromAPI() {
        String apiUrl = "http://localhost:8080/api/v1/restaurants";
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
