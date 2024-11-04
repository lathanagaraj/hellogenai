package com.neon.index;

import ch.qos.logback.core.util.FileUtil;
import com.azure.ai.openai.OpenAIClient;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.neon.config.OpenAiConfig;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingModel;
import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.CosmosDBVectorStore;
import org.springframework.ai.vectorstore.CosmosDBVectorStoreConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CodeEmbeddingService {

    private OpenAiConfig openAiConfig;



    @Lazy
    @Autowired
    private VectorStore vectorStore;

    @Autowired
    public CodeEmbeddingService(OpenAiConfig openAiConfig) {
        this.openAiConfig = openAiConfig;
    }



    private static final Logger logger = Logger.getLogger(CodeEmbeddingService.class.getName());

 //   private CodeEmbeddingRepository repository;


//    @Autowired
//    public CodeEmbeddingService(EmbeddingClient embeddingClient, CodeEmbeddingRepository repository) {
//        this.embeddingClient = embeddingClient;
//        this.repository = repository;
//    }
//
//
    public void processRepositories() throws IOException {
        String content = readFile("data/java/synclib/src/main/java/com/example/configsync/ConfigSyncService.java");
        Document document1 = new Document(java.util.UUID.randomUUID().toString(), content, Map.of("language", "java"));
        String explanation = readFile("data/java/synclib/src/main/java/com/example/configsync/explanation.md");
        Document document2 = new Document(java.util.UUID.randomUUID().toString(), explanation, Map.of("language", "java"));
        vectorStore.add(List.of(document1, document2));
        //vectorStore.add(List.of(document2));
    }

//    private CodeEmbeddingDocument generateEmbedding(String codeChunk){
//        float[] embedding = embeddingModel.embed(codeChunk);
//        CodeEmbeddingDocument document = new CodeEmbeddingDocument("com.example.configsync", codeChunk, embedding);
//        return document;
//    }

    public static String readFile(String filePath) throws IOException {
        Path path = Paths.get(FileUtil.class.getClassLoader().getResource(filePath).getPath());
        return new String(Files.readAllBytes(path));
    }


    @Bean
    public ObservationRegistry observationRegistry() {
        return ObservationRegistry.create();
    }

    @Bean
    public VectorStore vectorStore(ObservationRegistry observationRegistry) {
        CosmosDBVectorStoreConfig config = new CosmosDBVectorStoreConfig();
        config.setDatabaseName("neon-cosmosdb-database");
        config.setContainerName("neon-cosmosdb-container");
        config.setMetadataFields("language");
        config.setVectorStoreThroughput(400);

        CosmosAsyncClient cosmosClient = new CosmosClientBuilder()
                .endpoint("https://neon-cosmosdb.documents.azure.com:443/")
                .userAgentSuffix("SpringAI-CDBNoSQL-VectorStore")
                .key("qNR6UUukfcuFYtDYqwrzU0XTxxWYYdesEKofNvZxldB01wjBWeY5PF94iut1g2cjLARVaQVutizcACDbELnU6g")
                .gatewayMode()
                .buildAsyncClient();

        return new CosmosDBVectorStore(observationRegistry, null, cosmosClient, config, embeddingModel(openAiConfig.openAIClient()));
    }


    public EmbeddingModel embeddingModel(OpenAIClient openAIClient) {
        AzureOpenAiEmbeddingOptions options =  AzureOpenAiEmbeddingOptions.builder().withDeploymentName("embedding-model").build();
        AzureOpenAiEmbeddingModel embeddingModel =  new AzureOpenAiEmbeddingModel(openAIClient, MetadataMode.EMBED, options);
        return embeddingModel;
    }


    public List<Document> search(String query) {
        SearchRequest searchRequest = SearchRequest.query(query).withTopK(2);
        searchRequest.withSimilarityThreshold(0.7);
        List<Document> results = this.vectorStore.similaritySearch(searchRequest);
        return results;
    }
}
