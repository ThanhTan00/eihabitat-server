//package com.eihabitat.eihabitat_server.S3Upload;
//
//import lombok.RequiredArgsConstructor;
//import software.amazon.awssdk.core.sync.ResponseTransformer;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Service
//@RequiredArgsConstructor
//public class S3Service {
//
//    @Autowired
//    private S3Client s3Client;
//
//    @Value("${aws.s3.bucket-name}")
//    private String bucketName;
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//
//        // Upload file to S3 bucket
//        s3Client.putObject(
//                PutObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(fileName)
//                        .build(),
//                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
//        );
//
//        return fileName;
//    }
//
//     public byte[] downloadFile(String fileName) throws IOException {
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(fileName)
//                .build();
//
//        // Download the file from S3 and return it as a byte array
//        try (InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream())) {
//            return inputStream.readAllBytes();
//        }
//    }
//
//}
//
//
