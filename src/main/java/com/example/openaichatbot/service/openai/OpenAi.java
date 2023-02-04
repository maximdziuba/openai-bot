package com.example.openaichatbot.service.openai;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAi {
    private static final String OPENAI_URL = "https://api.openai.com/v1/images/generations";

    @Value("${openai_key}")
    private String apiKey;

    public String generateImage(String prompt, float temperature, int maxTokens, String stop, final int logprobs, final boolean echo, int n) {
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