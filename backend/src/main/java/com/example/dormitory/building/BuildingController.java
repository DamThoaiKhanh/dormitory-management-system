package com.example.dormitory.building;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dormitory.building.dto.BuildingRequest;
import com.example.dormitory.building.dto.BuildingResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/buildings")
public class BuildingController {
    private final BuildingService service;

    BuildingController(BuildingService service) {
        this.service = service;
    }

    @GetMapping
    public List<BuildingResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{code}")
    public BuildingResponse findByCode(@PathVariable String code) {
        return service.findByCode(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BuildingResponse create(@Valid @RequestBody BuildingRequest request) {
        return service.create(request);
    }

    @PutMapping("/{code}")
    public BuildingResponse update(@PathVariable String code,
            @Valid @RequestBody BuildingRequest request) {
        return service.update(code, request);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String code) {
        service.delete(code);
    }
}
