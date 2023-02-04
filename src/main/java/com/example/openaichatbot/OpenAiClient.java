package com.example.openaichatbot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAiClient {

    @Value("${openai.key}")
    private String apiKey;

    public String makeRequest() {
        String model = "text-davinci-002";
        String prompt = "In a shocking turn of events, scientists have discovered that";

        try {
            HttpResponse<JsonNode> response = Unirest.post("https://api.openai.com/v1/engines/" + model + "/jobs")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .body(new JSONObject()
                            .put("prompt", prompt)
                            .put("max_tokens", 1024)
                            .put("temperature", 0.5))
                    .asJson();

            System.out.println(response.getBody().toString());
            String message = response.getBody().getObject().getJSONArray("choices").getJSONObject(0).getString("text");
            System.out.println(message);
            return message;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "Error";
    }

}
