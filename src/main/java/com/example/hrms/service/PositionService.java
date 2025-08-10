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

/**
 * Service for managing positions.
 */
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

    /**
     * Creates a new position.
     * @param request the request object containing the position details
     * @return the created position
     */
    public PositionDto createPosition(CreateUpdatePositionRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + request.getDepartmentId()));
        Position position = new Position();
        position.setTitle(request.getTitle());
        position.setDepartment(department);
        Position savedPosition = positionRepository.save(position);
        return positionMapper.toDto(savedPosition);
    }

    /**
     * Retrieves a position by its ID.
     * @param id the ID of the position to retrieve
     * @return the position
     */
    public PositionDto getPositionById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Position not found with id: " + id));
        return positionMapper.toDto(position);
    }

    /**
     * Retrieves all positions for a given department.
     * @param departmentId the ID of the department
     * @return a list of positions
     */
    public List<PositionDto> getAllPositionsByDepartment(Long departmentId) {
        return positionRepository.findByDepartmentId(departmentId).stream()
                .map(positionMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates a position's details.
     * @param id the ID of the position to update
     * @param request the request object containing the updated details
     * @return the updated position
     */
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

    /**
     * Deletes a position by its ID.
     * @param id the ID of the position to delete
     */
    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }
}
