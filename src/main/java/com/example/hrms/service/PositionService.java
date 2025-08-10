package com.example.hrms.service;

import com.example.hrms.dto.CreateUpdatePositionRequest;
import com.example.hrms.dto.PositionDto;
import com.example.hrms.entity.Department;
import com.example.hrms.entity.Position;
import com.example.hrms.mapper.PositionMapper;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.repository.PositionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, DepartmentRepository departmentRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.positionMapper = positionMapper;
    }

    public PositionDto createPosition(CreateUpdatePositionRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + request.getDepartmentId()));
        Position position = new Position();
        position.setTitle(request.getTitle());
        position.setDepartment(department);
        Position savedPosition = positionRepository.save(position);
        return positionMapper.toDto(savedPosition);
    }

    public PositionDto getPositionById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Position not found with id: " + id));
        return positionMapper.toDto(position);
    }

    public List<PositionDto> getAllPositionsByDepartment(Long departmentId) {
        return positionRepository.findByDepartmentId(departmentId).stream()
                .map(positionMapper::toDto)
                .collect(Collectors.toList());
    }

    public PositionDto updatePosition(Long id, CreateUpdatePositionRequest request) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Position not found with id: " + id));
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + request.getDepartmentId()));
        position.setTitle(request.getTitle());
        position.setDepartment(department);
        Position updatedPosition = positionRepository.save(position);
        return positionMapper.toDto(updatedPosition);
    }

    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }
}
