package com.example.hrms.service;

import com.example.hrms.entity.Document;
import com.example.hrms.entity.DocumentType;
import com.example.hrms.entity.Employee;
import com.example.hrms.repository.DocumentRepository;
import com.example.hrms.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class DocumentService {

    private final StorageService storageService;
    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;

    public DocumentService(StorageService storageService, DocumentRepository documentRepository, EmployeeRepository employeeRepository) {
        this.storageService = storageService;
        this.documentRepository = documentRepository;
        this.employeeRepository = employeeRepository;
    }

    public void store(MultipartFile file, Long employeeId, DocumentType documentType) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String newFilename = employeeId + "_" + documentType.name() + "_" + filename;

        storageService.store(file, newFilename);

        Document document = new Document();
        document.setEmployee(employee);
        document.setDocumentType(documentType);
        document.setFilename(newFilename);
        document.setUploadTimestamp(LocalDateTime.now());

        documentRepository.save(document);
    }
}
