package com.example.dormitory.building;

import org.springframework.stereotype.Component;

import com.example.dormitory.building.dto.BuildingRequest;
import com.example.dormitory.building.dto.BuildingResponse;

@Component
public class BuildingMapper {

    public Building toEntity(BuildingRequest request) {
        Building building = new Building();
        updateEntity(building, request);
        return building;
    }

    public void updateEntity(Building building, BuildingRequest request) {
        building.setCode(normalizeCode(request.code()));
        building.setName(request.name());
        building.setAddress(trimToNull(request.address()));
        building.setNumberOfFloors(request.numberOfFloors());
        building.setDescription(trimToNull(request.description()));
        building.setStatus(request.status());
    }

    public BuildingResponse toResponse(Building building) {
        return new BuildingResponse(
                building.getCode(),
                building.getName(),
                building.getAddress(),
                building.getNumberOfFloors(),
                building.getDescription(),
                building.getStatus());
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }

    private String trimToNull(String value) {
        if (value == null)
            return null;

        String trimed = value.trim();

        return trimed.isEmpty() ? null : trimed;
    }
}
