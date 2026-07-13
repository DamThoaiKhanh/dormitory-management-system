package com.example.dormitory.room;

import com.example.dormitory.building.Building;
import com.example.dormitory.room.dto.RoomRequest;
import com.example.dormitory.room.dto.RoomResponse;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room toEntity(RoomRequest request, Building building) {
        Room room = new Room();
        updateEntity(room, request, building);
        return room;
    }

    public void updateEntity(Room room, RoomRequest request, Building building) {
        room.setRoomNumber(normalizeRoomNumber(request.roomNumber()));
        room.setBuilding(building);
        room.setMonthlyRent(request.monthlyRent());
        room.setMaximumCapacity(request.maximumCapacity());
        room.setStatus(request.status());
    }

    public RoomResponse toResponse(Room room, long occupancy) {
        int availableBeds = room.getMaximumCapacity() - Math.toIntExact(occupancy);

        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getBuilding().getCode(),
                room.getBuilding().getName(),
                room.getMonthlyRent(),
                room.getMaximumCapacity(),
                occupancy,
                Math.max(availableBeds, 0),
                occupancyStatus(occupancy, room.getMaximumCapacity()),
                room.getStatus());
    }

    private String normalizeRoomNumber(String roomNumber) {
        return roomNumber.trim().toUpperCase();
    }

    private String occupancyStatus(long occupancy, int maximumCapacity) {
        if (occupancy == 0) {
            return "EMPTY";
        }

        if (occupancy >= maximumCapacity) {
            return "FULL";
        }

        return "AVAILABLE";
    }
}