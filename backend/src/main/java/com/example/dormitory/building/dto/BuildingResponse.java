package com.example.dormitory.building.dto;

import com.example.dormitory.common.model.RecordStatus;

public record BuildingResponse(
                String code,
                String name,
                String address,
                Integer numberOfFloors,
                String description,
                RecordStatus status) {
}
