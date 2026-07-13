package com.example.dormitory.building;

import com.example.dormitory.building.dto.BuildingRequest;
import com.example.dormitory.building.dto.BuildingResponse;
import com.example.dormitory.common.exception.ConflictException;
import com.example.dormitory.common.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BuildingService {
    private final BuildingRepository repository;
    private final BuildingMapper mapper;

    public BuildingService(BuildingRepository repository, BuildingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<BuildingResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BuildingResponse findByCode(String code) {
        return mapper.toResponse(getEntity(code));
    }

    public BuildingResponse create(BuildingRequest request) {
        String normCode = normalizeCode(request.code());

        if (repository.existsById(normCode)) {
            throw new ConflictException("Building code already exists: " + normCode);
        }
        Building building = mapper.toEntity(request);
        return mapper.toResponse(repository.save(building));
    }

    public BuildingResponse update(String code, BuildingRequest request) {
        String normalizedCode = normalizeCode(code);
        Building building = getEntity(normalizedCode);

        String requestedCode = normalizeCode(request.code());

        if (!normalizedCode.equals(requestedCode)) {
            throw new ConflictException(
                    "Building code cannot be changed");
        }

        mapper.updateEntity(building, request);

        return mapper.toResponse(repository.save(building));
    }

    public void delete(String code) {
        Building building = getEntity(code);
        repository.delete(building);
    }

    private Building getEntity(String code) {
        String normCode = normalizeCode(code);

        return repository.findById(normCode)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found: " + normCode));
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}
