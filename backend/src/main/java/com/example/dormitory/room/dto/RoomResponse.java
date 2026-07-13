package com.example.dormitory.room.dto;

import com.example.dormitory.common.model.RecordStatus;

import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        String roomNumber,
        String buildingCode,
        String buildingName,
        BigDecimal monthlyRent,
        Integer maximumCapacity,
        Long currentOccupancy,
        Integer availableBeds,
        String occupancyStatus,
        RecordStatus status) {
}