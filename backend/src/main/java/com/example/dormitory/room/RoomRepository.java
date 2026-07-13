package com.example.dormitory.room;

import com.example.dormitory.common.model.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomNumberIgnoreCaseAndBuildingCode(
            String roomNumber,
            String buildingCode);

    Optional<Room> findByRoomNumberIgnoreCaseAndBuildingCode(
            String roomNumber,
            String buildingCode);

    List<Room> findByBuildingCode(String buildingCode);

    List<Room> findByStatus(RecordStatus status);

    List<Room> findByRoomNumberContainingIgnoreCase(
            String roomNumber);
}