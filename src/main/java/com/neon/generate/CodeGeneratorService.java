package com.neon.generate;

import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.neon.config.OpenAiConfig;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.azure.ai.openai.OpenAIClient;
import org.springframework.ai.azure.openai.client.AzureOpenAiClient;

import java.util.List;
import java.util.Map;

@Service
public class CodeGeneratorService {

    private final OpenAiConfig openAiConfig;

    @Value("classpath:/prompt/samplecodegen.st")
    private Resource promptTemplate;

    @Autowired
    public CodeGeneratorService(OpenAiConfig openAiConfig) {
        this.openAiConfig = openAiConfig;
    }

    public String generateCode(String message) {

        OpenAIClient openAIClient = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(openAiConfig.getApiKey()))
                .endpoint(openAiConfig.getEndpoint())
                .buildClient();

        AzureOpenAiClient azureOpenAiClient = new AzureOpenAiClient(openAIClient);
        azureOpenAiClient.setModel(openAiConfig.getCodegen().getOptions().getModel());



        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(promptTemplate);
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemPromptTemplate.createMessage(), userMessage));

        AiResponse response = azureOpenAiClient.generate(prompt);
        return response.getGeneration().getText();

    }




}


