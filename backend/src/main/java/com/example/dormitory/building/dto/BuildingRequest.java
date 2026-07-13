package com.example.dormitory.building.dto;

import com.example.dormitory.common.model.RecordStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BuildingRequest(
                @NotBlank(message = "Building code is required") @Size(max = 20, message = "Building code cannot exceed 20 characters") String code,

                @NotBlank(message = "Building name is required") @Size(max = 150, message = "Building name cannot exceed 150 characters") String name,

                @Size(max = 255, message = "Address cannot exceed 255 characters") String address,

                @Positive(message = "Number of floors must be greater than zero") Integer numberOfFloors,

                String description,

                @NotNull(message = "Status is required") RecordStatus status

) {
}
