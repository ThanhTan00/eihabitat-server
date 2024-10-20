package com.eihabitat.eihabitat_server.S3Upload;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                "AKIAW3MD6K7IEBEMIRVS",
                "h9qH0duW7dPzqcM9NCKZinbPISTOLIefWuLo0dYE"
        );

        return S3Client.builder()
                .region(Region.AP_SOUTHEAST_1)  // Replace with your region
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
}

