package com.example.openaichatbot;

import com.example.openaichatbot.service.openai.OpenAi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAi openAi;

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @PostMapping("/generateImages")
    public String generateImages(@RequestBody GenerateImageRequest request) {
        return openAi.generateImage2(request.getPrompt(), request.getTemperature(), request.getMaxTokens(), request.getStop(),
                request.getLogprobs(), request.isEcho(), request.getN());
    }
}