package com.example.hrms.controller;

import com.example.hrms.dto.CreateUpdatePositionRequest;
import com.example.hrms.dto.PositionDto;
import com.example.hrms.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<PositionDto> createPosition(@Valid @RequestBody CreateUpdatePositionRequest request) {
        return new ResponseEntity<>(positionService.createPosition(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionDto> getPositionById(@PathVariable Long id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }

    @GetMapping
    public ResponseEntity<List<PositionDto>> getAllPositionsByDepartment(@RequestParam Long departmentId) {
        return ResponseEntity.ok(positionService.getAllPositionsByDepartment(departmentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<PositionDto> updatePosition(@PathVariable Long id, @Valid @RequestBody CreateUpdatePositionRequest request) {
        return ResponseEntity.ok(positionService.updatePosition(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
