package com.example.dormitory.staff;

import com.example.dormitory.staff.dto.ManagementStaffRequest;
import com.example.dormitory.staff.dto.ManagementStaffResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/staff")
public class ManagementStaffController {

    private final ManagementStaffService staffService;

    public ManagementStaffController(
            ManagementStaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public List<ManagementStaffResponse> findAll() {
        return staffService.findAll();
    }

    @GetMapping("/{id}")
    public ManagementStaffResponse findById(
            @PathVariable Long id) {
        return staffService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManagementStaffResponse create(
            @Valid @RequestBody ManagementStaffRequest request) {
        return staffService.create(request);
    }

    @PutMapping("/{id}")
    public ManagementStaffResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ManagementStaffRequest request) {
        return staffService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        staffService.delete(id);
    }
}