package com.neon.config;

import com.neon.service.CodeIndexService;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public CodeIndexService bikeService(AiClient aiClient, EmbeddingClient embeddingClient){
        return new CodeIndexService(aiClient, embeddingClient);
    }
}
