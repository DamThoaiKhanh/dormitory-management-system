package com.example.dormitory.room.dto;

import java.math.BigDecimal;

import com.example.dormitory.common.model.RecordStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RoomRequest(

        @NotBlank(message = "Room number is required") @Size(max = 30, message = "Room number cannot exceed 30 characters") String roomNumber,

        @NotBlank(message = "Building code is required") @Size(max = 20, message = "Building code cannot exceed 20 characters") String buildingCode,

        @NotNull(message = "Monthly rent is required") @DecimalMin(value = "0.00", message = "Monthly rent cannot be negative") BigDecimal monthlyRent,

        @NotNull(message = "Maximum capacity is required") @Positive(message = "Maximum capacity must be greater than zero") Integer maximumCapacity,

        @NotNull(message = "Status is required") RecordStatus status

) {
}
