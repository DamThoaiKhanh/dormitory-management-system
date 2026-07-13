package com.example.dormitory.student.dto;

import com.example.dormitory.common.model.Gender;

import java.time.LocalDate;

public record StudentResponse(
        Long id,
        String studentCode,
        String fullName,
        Gender gender,
        LocalDate dateOfBirth,
        String citizenId,
        String phoneNumber,
        String address,
        String className,
        String major,
        String hometown,
        Long roomId,
        String roomNumber,
        String buildingCode) {
}