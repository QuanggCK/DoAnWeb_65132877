package clc65.quanggck.controllers;

import clc65.quanggck.services.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin("*")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = uploadService.storeFile(file);

            String imageUrl = "http://localhost:8080/uploads/" + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("url", imageUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload ảnh thất bại: " + e.getMessage());
        }
    }
}