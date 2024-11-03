package com.neon.index;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeEmbeddingRepository extends CosmosRepository<CodeEmbeddingDocument, String> {
    // Additional query methods can be defined here for specific vector search logic if needed
}
