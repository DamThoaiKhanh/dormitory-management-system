package com.example.dormitory.student;

import com.example.dormitory.common.exception.ConflictException;
import com.example.dormitory.common.exception.ResourceNotFoundException;
import com.example.dormitory.common.model.RecordStatus;
import com.example.dormitory.room.Room;
import com.example.dormitory.room.RoomRepository;
import com.example.dormitory.student.dto.StudentRequest;
import com.example.dormitory.student.dto.StudentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final StudentMapper studentMapper;

    public StudentService(
            StudentRepository studentRepository,
            RoomRepository roomRepository,
            StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.studentMapper = studentMapper;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return studentMapper.toResponse(getEntity(id));
    }

    public StudentResponse create(StudentRequest request) {
        String studentCode = normalizeCode(request.studentCode());

        if (studentRepository.existsByStudentCodeIgnoreCase(studentCode)) {
            throw new ConflictException(
                    "Student code already exists: " + studentCode);
        }

        String citizenId = trimToNull(request.citizenId());

        if (citizenId != null &&
                studentRepository.existsByCitizenId(citizenId)) {
            throw new ConflictException(
                    "Citizen ID already exists");
        }

        Room room = validateRoomAssignment(request.roomId(), null);

        Student student = studentMapper.toEntity(request, room);
        Student savedStudent = studentRepository.save(student);

        return studentMapper.toResponse(savedStudent);
    }

    public StudentResponse update(
            Long id,
            StudentRequest request) {
        Student student = getEntity(id);

        String studentCode = normalizeCode(request.studentCode());

        studentRepository.findByStudentCodeIgnoreCase(studentCode)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException(
                            "Student code already exists: " + studentCode);
                });

        String citizenId = trimToNull(request.citizenId());

        if (citizenId != null &&
                !citizenId.equals(student.getCitizenId()) &&
                studentRepository.existsByCitizenId(citizenId)) {
            throw new ConflictException(
                    "Citizen ID already exists");
        }

        Room room = validateRoomAssignment(request.roomId(), id);

        studentMapper.updateEntity(student, request, room);

        return studentMapper.toResponse(
                studentRepository.save(student));
    }

    public void delete(Long id) {
        Student student = getEntity(id);
        studentRepository.delete(student);
    }

    private Student getEntity(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found: " + id));
    }

    private Room validateRoomAssignment(
            Long roomId,
            Long studentId) {
        if (roomId == null) {
            return null;
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Room not found: " + roomId));

        if (room.getStatus() != RecordStatus.ACTIVE) {
            throw new ConflictException(
                    "Inactive room cannot be selected");
        }

        long occupancy;

        if (studentId == null) {
            occupancy = studentRepository.countByRoomId(roomId);
        } else {
            occupancy = studentRepository
                    .countByRoomIdAndIdNot(roomId, studentId);
        }

        if (occupancy >= room.getMaximumCapacity()) {
            throw new ConflictException(
                    "This room has reached maximum capacity");
        }

        return room;
    }

    private String normalizeCode(String value) {
        return value.trim().toUpperCase();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}