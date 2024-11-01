package com.eihabitat.eihabitat_server.S3Upload;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class S3Controller {

    private final S3Service s3Service;

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            // Upload file and return the URL
//            String fileUrl = s3Service.uploadFile(file);
//            return ResponseEntity.ok(fileUrl);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
//        }
//    }

//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) throws IOException {
//        var s3Object = s3Service.downloadFile(fileName);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(s3Object.readAllBytes());
//    }
}

