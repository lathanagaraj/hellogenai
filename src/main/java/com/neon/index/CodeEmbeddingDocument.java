package com.neon.index;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.util.List;

@Container(containerName = "neon-cosmosdb-container")

public class CodeEmbeddingDocument {

    @Id
    private String chunkId;

    @PartitionKey
    private String artifactId;

    @Version
    private String _etag;


    private String codeChunk;

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    private float[] embedding;

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



    public String get_etag() {
        return _etag;
    }

    public void set_etag(String _etag) {
        this._etag = _etag;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }



    public CodeEmbeddingDocument(String artifactId, String codeChunk, float[] embedding) {
        this.chunkId = java.util.UUID.randomUUID().toString();
        this.artifactId = artifactId;
        this.codeChunk = codeChunk;
        this.embedding = embedding;
    }


}
