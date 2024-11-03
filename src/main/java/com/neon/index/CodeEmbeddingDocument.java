package com.neon.index;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.util.List;

@Container(containerName = "neon-cosmosb-container")

public class CodeEmbeddingDocument {

    @Id
    private String chunkId;

    @PartitionKey
    private String codeChunk;

    private List<Double> embedding;

    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    public String getCodeChunk() {
        return codeChunk;
    }

    public void setCodeChunk(String codeChunk) {
        this.codeChunk = codeChunk;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    public String get_etag() {
        return _etag;
    }

    public void set_etag(String _etag) {
        this._etag = _etag;
    }

    @Version
    private String _etag;

    public CodeEmbeddingDocument(String codeChunk, List<Double> embedding) {
        this.chunkId = java.util.UUID.randomUUID().toString();
        this.codeChunk = codeChunk;
        this.embedding = embedding;
    }


}
