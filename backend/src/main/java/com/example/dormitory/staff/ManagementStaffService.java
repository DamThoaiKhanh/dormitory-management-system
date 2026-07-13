package com.example.dormitory.staff;

import com.example.dormitory.building.Building;
import com.example.dormitory.building.BuildingRepository;
import com.example.dormitory.common.exception.ConflictException;
import com.example.dormitory.common.exception.ResourceNotFoundException;
import com.example.dormitory.staff.dto.ManagementStaffRequest;
import com.example.dormitory.staff.dto.ManagementStaffResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ManagementStaffService {

        private final ManagementStaffRepository staffRepository;
        private final BuildingRepository buildingRepository;
        private final ManagementStaffMapper staffMapper;

        public ManagementStaffService(
                        ManagementStaffRepository staffRepository,
                        BuildingRepository buildingRepository,
                        ManagementStaffMapper staffMapper) {
                this.staffRepository = staffRepository;
                this.buildingRepository = buildingRepository;
                this.staffMapper = staffMapper;
        }

        @Transactional(readOnly = true)
        public List<ManagementStaffResponse> findAll() {
                return staffRepository.findAll()
                                .stream()
                                .map(staffMapper::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public ManagementStaffResponse findById(Long id) {
                return staffMapper.toResponse(getEntity(id));
        }

        public ManagementStaffResponse create(
                        ManagementStaffRequest request) {
                String employeeCode = normalizeCode(request.employeeCode());

                if (staffRepository.existsByEmployeeCodeIgnoreCase(employeeCode)) {
                        throw new ConflictException(
                                        "Employee code already exists: " + employeeCode);
                }

                validateUniqueFields(request, null);

                Building building = findBuilding(
                                request.assignedBuildingCode());

                ManagementStaff staff = staffMapper.toEntity(request, building);

                return staffMapper.toResponse(
                                staffRepository.save(staff));
        }

        public ManagementStaffResponse update(
                        Long id,
                        ManagementStaffRequest request) {
                ManagementStaff staff = getEntity(id);

                String employeeCode = normalizeCode(request.employeeCode());

                staffRepository.findByEmployeeCodeIgnoreCase(employeeCode)
                                .filter(existing -> !existing.getId().equals(id))
                                .ifPresent(existing -> {
                                        throw new ConflictException(
                                                        "Employee code already exists: "
                                                                        + employeeCode);
                                });

                validateUniqueFields(request, staff);

                Building building = findBuilding(
                                request.assignedBuildingCode());

                staffMapper.updateEntity(staff, request, building);

                return staffMapper.toResponse(
                                staffRepository.save(staff));
        }

        public void delete(Long id) {
                staffRepository.delete(getEntity(id));
        }

        private ManagementStaff getEntity(Long id) {
                return staffRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Management staff not found: " + id));
        }

        private Building findBuilding(String code) {
                String normalizedCode = trimToNull(code);

                if (normalizedCode == null) {
                        return null;
                }

                String upperCode = normalizedCode.toUpperCase();

                return buildingRepository.findById(upperCode)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Building not found: " + upperCode));
        }

        private void validateUniqueFields(
                        ManagementStaffRequest request,
                        ManagementStaff currentStaff) {
                String citizenId = trimToNull(request.citizenId());

                if (citizenId != null &&
                                (currentStaff == null ||
                                                !citizenId.equals(currentStaff.getCitizenId()))
                                &&
                                staffRepository.existsByCitizenId(citizenId)) {
                        throw new ConflictException(
                                        "Citizen ID already exists");
                }

                String phoneNumber = trimToNull(request.phoneNumber());

                if (phoneNumber != null &&
                                (currentStaff == null ||
                                                !phoneNumber.equals(currentStaff.getPhoneNumber()))
                                &&
                                staffRepository.existsByPhoneNumber(phoneNumber)) {
                        throw new ConflictException(
                                        "Phone number already exists");
                }
        }

        private String normalizeCode(String value) {
                return value.trim().toUpperCase();
        }

        private String trimToNull(String value) {
                if (value == null) {
                        return null;
                }

                String trimmed = value.trim();
                return trimmed.isEmpty() ? null : trimmed;
        }
}