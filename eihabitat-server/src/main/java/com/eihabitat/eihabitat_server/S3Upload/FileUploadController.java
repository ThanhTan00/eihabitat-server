//package com.eihabitat.eihabitat_server.S3Upload;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/s3")
//public class FileUploadController {
//
//    private S3Service s3Service;
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileName = s3Service.uploadFile(file);
//            return ResponseEntity.ok("File uploaded successfully: " + fileName);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
//        try {
//            // Download the file from S3
//            byte[] fileContent = s3Service.downloadFile(fileName);
//
//            // Create headers to set the content type and the attachment disposition
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
//
//            // Return the file as a byte array with headers
//            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
//
//        } catch (Exception e) {
//            // Handle the error if the file is not found or other issues
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}
//
