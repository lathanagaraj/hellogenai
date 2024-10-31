package com.neon.controller;

import com.neon.service.CodeIndexService;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DevBuddyController {


    private final AiClient aiClient;
    private final CodeIndexService bikeService;

   // private final AzureOpenAiChatModel chatModel;

    @Autowired
    public DevBuddyController(AiClient aiClient, CodeIndexService bikeService) {
        this.aiClient = aiClient;
        this.bikeService = bikeService;
    }
    @GetMapping("/ai/simple")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", aiClient.generate(message));
    }


    @GetMapping("/ai/bike")
    public Generation completeBike(@RequestParam(value = "message", defaultValue = "What bike is good for city commuting?") String message) {
        return bikeService.generate(message);
    }



}
