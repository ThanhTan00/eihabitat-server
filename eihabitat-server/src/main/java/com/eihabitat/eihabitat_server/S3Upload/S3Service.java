package com.eihabitat.eihabitat_server.S3Upload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    public String uploadFile(MultipartFile file, String keyName) throws IOException {

        // Create a PutObjectRequest
        String bucketName = "user-post";
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        // Upload the file
        PutObjectResponse response = s3Client.putObject(putObjectRequest,
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

        // Optionally, return the file URL
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, keyName);
    }

//    public ResponseInputStream downloadFile(String fileName) {
//        try {
//            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(fileName)
//                    .build();
//
//            return s3Client.getObject(getObjectRequest);
//        } catch (S3Exception e) {
//            throw new RuntimeException("Error while downloading file from S3", e);
//        }
//    }
}




