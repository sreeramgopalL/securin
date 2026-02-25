package com.csv.controller;

import com.csv.service.ExcelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class ExcelUploadController {

    private final ExcelService excelService;

    public ExcelUploadController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/excel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file");
        }

        try {
            excelService.saveExcelData(file);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}