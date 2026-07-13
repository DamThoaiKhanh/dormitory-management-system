package com.example.dormitory.staff.dto;

import com.example.dormitory.common.model.Gender;

import java.time.LocalDate;

public record ManagementStaffResponse(
        Long id,
        String employeeCode,
        String fullName,
        Gender gender,
        LocalDate dateOfBirth,
        String citizenId,
        String phoneNumber,
        String address,
        String assignedBuildingCode,
        String assignedBuildingName) {
}