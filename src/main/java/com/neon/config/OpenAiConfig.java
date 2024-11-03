package com.neon.config;


import org.springframework.ai.autoconfigure.openai.OpenAiProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.azure.openai")
public class OpenAiConfig {
    private String apiKey;
    private String endpoint;
    private Codegen codegen;

    public Codegen getCodegen() {
        return codegen;
    }

    public void setCodegen(Codegen codegen) {
        this.codegen = codegen;
    }


    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }



    public static class Codegen {
        public Options getOptions() {
            return options;
        }

        public void setOptions(Options options) {
            this.options = options;
        }

        private Options options;
    }


    public static class Options {
        private String model;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }



}