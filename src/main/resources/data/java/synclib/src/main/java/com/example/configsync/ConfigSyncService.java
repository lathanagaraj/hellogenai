
package com.example.configsync;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.nio.file.*;
import java.util.Timer;
import java.util.TimerTask;

public class ConfigSyncService {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String objectKey;
    private final Path localConfigPath;
    private final long syncIntervalMillis;
    private final SyncFailureListener failureListener;
    private Timer timer;

    public interface SyncFailureListener {
        void onSyncFailure(Exception e);
    }

    public ConfigSyncService(String accessKey, String secretKey, String region, String bucketName, String objectKey,
                             Path localConfigPath, long syncIntervalMillis, SyncFailureListener failureListener) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.localConfigPath = localConfigPath;
        this.syncIntervalMillis = syncIntervalMillis;
        this.failureListener = failureListener;
    }

    public void start() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                syncConfig();
            }
        }, 0, syncIntervalMillis);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void syncConfig() {
        try {
            S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, objectKey));
            Path tempFile = Files.createTempFile("config-sync", ".tmp");
            Files.copy(s3object.getObjectContent(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            Files.move(tempFile, localConfigPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Configuration synced successfully.");
        } catch (IOException | RuntimeException e) {
            System.err.println("Failed to sync configuration: " + e.getMessage());
            if (failureListener != null) {
                failureListener.onSyncFailure(e);
            }
        }
    }
}
