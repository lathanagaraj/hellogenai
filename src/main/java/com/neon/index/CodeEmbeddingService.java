package com.neon.index;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class CodeEmbeddingService {

    private static final Logger logger = Logger.getLogger(CodeEmbeddingService.class.getName());

    private EmbeddingClient embeddingClient;
    private CodeEmbeddingRepository repository;


    @Autowired
    public CodeEmbeddingService(EmbeddingClient embeddingClient, CodeEmbeddingRepository repository) {
        this.embeddingClient = embeddingClient;
        this.repository = repository;
    }


    public void processRepositories() {
        CodeEmbeddingDocument doc = generateEmbedding("public static void main()");
        repository.save(doc);
    }

    private CodeEmbeddingDocument generateEmbedding(String codeChunk){
        List<Double> embedding = embeddingClient.embed(codeChunk);
        CodeEmbeddingDocument document = new CodeEmbeddingDocument(codeChunk, embedding);
        return document;
    }
}
