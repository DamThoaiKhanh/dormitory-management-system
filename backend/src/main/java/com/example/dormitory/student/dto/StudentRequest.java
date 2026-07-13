package com.example.dormitory.student.dto;

import com.example.dormitory.common.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StudentRequest(

        @NotBlank(message = "Student code is required") @Size(max = 30) String studentCode,

        @NotBlank(message = "Full name is required") @Size(max = 150) String fullName,

        Gender gender,

        LocalDate dateOfBirth,

        @Size(max = 30) String citizenId,

        @Pattern(regexp = "^$|^[0-9+() -]{8,20}$", message = "Enter a valid phone number") String phoneNumber,

        String address,

        @Size(max = 100) String className,

        @Size(max = 150) String major,

        @Size(max = 150) String hometown,

        Long roomId) {
}