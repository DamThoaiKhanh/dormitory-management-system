package com.example.dormitory.room;

import com.example.dormitory.building.Building;
import com.example.dormitory.building.BuildingRepository;
import com.example.dormitory.common.exception.ConflictException;
import com.example.dormitory.common.exception.ResourceNotFoundException;
import com.example.dormitory.room.dto.RoomRequest;
import com.example.dormitory.room.dto.RoomResponse;
import com.example.dormitory.student.StudentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;
    private final StudentRepository studentRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository,
            BuildingRepository buildingRepository,
            RoomMapper roomMapper,
            StudentRepository studentRepository) {
        this.roomRepository = roomRepository;
        this.buildingRepository = buildingRepository;
        this.roomMapper = roomMapper;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(room -> roomMapper.toResponse(room,
                        studentRepository.countByRoomId(room.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomResponse findById(Long id) {
        Room room = getEntity(id);
        return roomMapper.toResponse(room,
                studentRepository.countByRoomId(room.getId()));
    }

    public RoomResponse create(RoomRequest request) {
        String buildingCode = normalizeBuildingCode(request.buildingCode());

        String roomNumber = normalizeRoomNumber(request.roomNumber());

        Building building = findBuilding(buildingCode);

        if (roomRepository.existsByRoomNumberIgnoreCaseAndBuildingCode(
                roomNumber,
                buildingCode)) {
            throw new ConflictException(
                    "Room already exists in building "
                            + buildingCode
                            + ": "
                            + roomNumber);
        }

        Room room = roomMapper.toEntity(request, building);
        Room savedRoom = roomRepository.save(room);

        return roomMapper.toResponse(savedRoom, studentRepository.countByRoomId(room.getId()));
    }

    public RoomResponse update(Long id, RoomRequest request) {
        Room room = getEntity(id);

        String buildingCode = normalizeBuildingCode(request.buildingCode());

        String roomNumber = normalizeRoomNumber(request.roomNumber());

        Building building = findBuilding(buildingCode);

        roomRepository.findByRoomNumberIgnoreCaseAndBuildingCode(
                roomNumber,
                buildingCode)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException(
                            "Room already exists in building "
                                    + buildingCode
                                    + ": "
                                    + roomNumber);
                });

        roomMapper.updateEntity(room, request, building);

        Room savedRoom = roomRepository.save(room);

        return roomMapper.toResponse(savedRoom, studentRepository.countByRoomId(room.getId()));
    }

    public void delete(Long id) {
        Room room = getEntity(id);
        roomRepository.delete(room);
    }

    private Room getEntity(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Room not found: " + id));
    }

    private Building findBuilding(String code) {
        return buildingRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Building not found: " + code));
    }

    private String normalizeBuildingCode(String code) {
        return code.trim().toUpperCase();
    }

    private String normalizeRoomNumber(String roomNumber) {
        return roomNumber.trim().toUpperCase();
    }
}