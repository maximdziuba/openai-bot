package com.example.openaichatbot.service.openai;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MediaType;

@Component
public class OpenAi {
    private static final String OPENAI_URL = "https://api.openai.com/v1/images/generations";

    @Value("${openai_key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateImages(String prompt, float temperature, int maxTokens, String stop, final int logprobs, final boolean echo, int n) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.valueOf(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + apiKey);

        String requestJson = "{\"prompt\":\"" + prompt + "\",\"n\":" + n + "}";

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_URL, request, String.class);
        return response.getBody();
    }

    public String generateImage2(String prompt, float temperature, int maxTokens, String stop, final int logprobs, final boolean echo, int n) {
        try {
            HttpResponse<JsonNode> response = Unirest.post(OPENAI_URL)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .body(new JSONObject()
                            .put("prompt", prompt)
                            .put("n", n))
                    .asJson();
            return response.getBody().getObject().getJSONArray("data").getJSONObject(0).get("url").toString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "Error";
    }
}