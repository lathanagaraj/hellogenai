package com.neon.suggestions;

import com.neon.config.OpenAiConfig;
import com.neon.index.CodeEmbeddingService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeSuggestionService {

    private CodeEmbeddingService codeEmbeddingService;

    private final OpenAiConfig openAiConfig;

    @Autowired
    public CodeSuggestionService(OpenAiConfig openAiConfig, CodeEmbeddingService codeEmbeddingService) {
        this.openAiConfig = openAiConfig;
        this.codeEmbeddingService = codeEmbeddingService;
    }

    public String suggestCode(String query) {
        List<Document> search = codeEmbeddingService.search(query);
        if(search==null || search.size()== 0 ) {
            return "no results found";
        }else {
            return search.get(0).getContent();
        }
    }


}
