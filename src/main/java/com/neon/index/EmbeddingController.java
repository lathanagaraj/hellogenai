package com.neon.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/embedding")
public class EmbeddingController {

    private CodeEmbeddingService embeddingService;

    @Autowired
    public EmbeddingController(CodeEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }


    @PostMapping("/index")
    public String generateCode() {
        embeddingService.processRepositories();
        return "Embedding generated";
    }
}

