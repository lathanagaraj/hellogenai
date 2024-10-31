package com.neon.service;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.client.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.ai.retriever.VectorStoreRetriever;
import org.springframework.ai.vectorstore.InMemoryVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.ai.reader.JsonReader;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CodeIndexService {

    private static final Logger logger = Logger.getLogger(CodeIndexService.class.getName());

    @Value("classpath:/data/bikes.json")
    private Resource bikeData;

    @Value("classpath:/prompt/bike.st")
    private Resource promptTemplate;

    private AiClient aiClient;
    private EmbeddingClient embeddingClient;


    public CodeIndexService(AiClient aiClient, EmbeddingClient embeddingClient) {
        this.aiClient = aiClient;
        this.embeddingClient = embeddingClient;
    }

    public VectorStore laodData(){
        JsonReader jsonReader = new JsonReader(bikeData,  "name", "price", "shortDescription", "description");
        List<Document> documents = jsonReader.get();
        logger.info("Loaded Json Data");

        VectorStore vectorStore = new InMemoryVectorStore(embeddingClient);
        vectorStore.add(documents);
        logger.info("Embeddings added to vector store");

        return vectorStore;
    }

    public Generation generate(String query){
        VectorStore vectorStore = laodData();

        VectorStoreRetriever vectorStoreRetriever = new VectorStoreRetriever(vectorStore);
        List<Document> similarDocs = vectorStoreRetriever.retrieve(query);
        logger.info(String.format("Found %s relevant documents.", similarDocs.size()));

        Message systemMessage = generateBikeSystemPrompt(similarDocs);
        UserMessage userMessage = new UserMessage(query);

        Prompt prompt = new Prompt(List.of(systemMessage,userMessage));
        logger.info("prompt is "+prompt.toString());

        AiResponse response = aiClient.generate(prompt);

        return response.getGeneration();

    }

    public Message generateBikeSystemPrompt(List<Document> similarDocs){
        String docs = similarDocs.stream().map(entry -> entry.getContent()).collect(Collectors.joining("/n"));
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(promptTemplate);
        return systemPromptTemplate.createMessage(Map.of("documents", docs));
    }
}
