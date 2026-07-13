package com.example.dormitory.staff;

import com.example.dormitory.building.Building;
import com.example.dormitory.staff.dto.ManagementStaffRequest;
import com.example.dormitory.staff.dto.ManagementStaffResponse;
import org.springframework.stereotype.Component;

@Component
public class ManagementStaffMapper {

    public ManagementStaff toEntity(
            ManagementStaffRequest request,
            Building building) {
        ManagementStaff staff = new ManagementStaff();
        updateEntity(staff, request, building);
        return staff;
    }

    public void updateEntity(
            ManagementStaff staff,
            ManagementStaffRequest request,
            Building building) {
        staff.setEmployeeCode(
                request.employeeCode().trim().toUpperCase());
        staff.setFullName(request.fullName().trim());
        staff.setGender(request.gender());
        staff.setDateOfBirth(request.dateOfBirth());
        staff.setCitizenId(trimToNull(request.citizenId()));
        staff.setPhoneNumber(trimToNull(request.phoneNumber()));
        staff.setAddress(trimToNull(request.address()));
        staff.setAssignedBuilding(building);
    }

    public ManagementStaffResponse toResponse(
            ManagementStaff staff) {
        Building building = staff.getAssignedBuilding();

        return new ManagementStaffResponse(
                staff.getId(),
                staff.getEmployeeCode(),
                staff.getFullName(),
                staff.getGender(),
                staff.getDateOfBirth(),
                staff.getCitizenId(),
                staff.getPhoneNumber(),
                staff.getAddress(),
                building == null ? null : building.getCode(),
                building == null ? null : building.getName());
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}