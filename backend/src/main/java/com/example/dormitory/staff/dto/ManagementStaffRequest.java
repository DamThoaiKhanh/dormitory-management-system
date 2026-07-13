package com.example.dormitory.staff.dto;

import com.example.dormitory.common.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ManagementStaffRequest(

        @NotBlank(message = "Employee code is required") @Size(max = 30) String employeeCode,

        @NotBlank(message = "Full name is required") @Size(max = 150) String fullName,

        Gender gender,

        LocalDate dateOfBirth,

        @Size(max = 30) String citizenId,

        @Pattern(regexp = "^$|^[0-9+() -]{8,20}$", message = "Enter a valid phone number") String phoneNumber,

        String address,

        @Size(max = 20) String assignedBuildingCode) {
}