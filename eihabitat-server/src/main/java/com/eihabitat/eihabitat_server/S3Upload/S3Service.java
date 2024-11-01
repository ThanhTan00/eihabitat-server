package com.eihabitat.eihabitat_server.S3Upload;

import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    UserRepository userRepo;
    private final S3Client s3Client;

    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String bucketName = "user-post";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .build();

        try {
            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, uniqueFileName);
        } catch (Exception e) {
            System.err.println("S3 Upload Error: " + e.getMessage());  // Log detailed error message
            throw new IOException("Error uploading file to S3", e);
        }
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




