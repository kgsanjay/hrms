package com.example.hrms.controller;

import com.example.hrms.entity.DocumentType;
import com.example.hrms.service.DocumentService;
import com.example.hrms.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees/{employeeId}/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final StorageService storageService;


    public DocumentController(DocumentService documentService, StorageService storageService) {
        this.documentService = documentService;
        this.storageService = storageService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<Void> uploadDocument(@PathVariable Long employeeId,
                                               @RequestParam("file") MultipartFile file,
                                               @RequestParam("type") DocumentType documentType) {
        documentService.store(file, employeeId, documentType);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
