package com.blpsteam.blpslab1.jca;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
@Slf4j
public class YookassaConnectionImpl implements YookassaConnection{
    private final String shopId;
    private final String apiKey;

    public YookassaConnectionImpl(String shopId, String apiKey) {
        this.shopId = shopId;
        this.apiKey = apiKey;
    }

    @Override
    public String createPayment(Long amount) {
        try {
            var json = """
                {
                  "amount": {
                    "value": "%s",
                    "currency": "RUB"
                  },
                  "confirmation": {
                    "type": "redirect",
                    "return_url": "%s"
                  },
                  "capture": true,
                  "description": "%s"
                }
                """.formatted(amount.toString(), "https://se.ifmo.ru/", "Test");
            log.info("Created Payment {}", json);
            log.info(json);

            String auth=encodeBasicAuth(shopId, apiKey);

            log.info("Encoded basic auth {}", auth);

            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.yookassa.ru/v3/payments"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Idempotence-Key", UUID.randomUUID().toString())
                    .header("Authorization", "Basic " + encodeBasicAuth(shopId, apiKey))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            log.info("Request {}", request);

            var client = HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("create payment");
            log.info(String.valueOf(response.statusCode()));
            log.info("Responsy body");
            log.info(response.body());

            String body = response.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(body);
            return node.path("confirmation").path("confirmation_url").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String encodeBasicAuth(String shopId, String apiKey) {
        var creds = shopId + ":" + apiKey;
        return java.util.Base64.getEncoder().encodeToString(creds.getBytes());
    }
}
