package com.example.openaichatbot.service.openai;

import com.example.openaichatbot.dto.OpenAiResponseDto;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAiChat {

    @Value("${openai_key}")
    private String apiKey;
    private String OPENAI_URL = "https://api.openai.com/v1/completions";

    public OpenAiResponseDto getResponseFromChatGPT(String prompt) {
        String botResponse = "";
        String finishReason = "";
        try {
            HttpResponse<JsonNode> response = Unirest.post(OPENAI_URL)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .body(new JSONObject()
                            .put("model", "text-davinci-003")
                            .put("prompt", prompt)
                            .put("max_tokens", 1500))
                    .asJson();
            JSONArray responseData = response.getBody().getObject().getJSONArray("choices");
            JSONObject botResponseObj = (JSONObject) responseData.get(0);
            System.out.println(responseData);
            botResponse = botResponseObj.get("text").toString();
            finishReason = botResponseObj.get("finish_reason").toString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        OpenAiResponseDto responseDto = new OpenAiResponseDto(finishReason, botResponse);
        return responseDto;
    }
}
