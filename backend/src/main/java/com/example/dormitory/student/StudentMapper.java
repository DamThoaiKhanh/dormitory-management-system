package com.example.dormitory.student;

import com.example.dormitory.room.Room;
import com.example.dormitory.student.dto.StudentRequest;
import com.example.dormitory.student.dto.StudentResponse;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(
            StudentRequest request,
            Room room) {
        Student student = new Student();
        updateEntity(student, request, room);
        return student;
    }

    public void updateEntity(
            Student student,
            StudentRequest request,
            Room room) {
        student.setStudentCode(
                request.studentCode().trim().toUpperCase());
        student.setFullName(request.fullName().trim());
        student.setGender(request.gender());
        student.setDateOfBirth(request.dateOfBirth());
        student.setCitizenId(trimToNull(request.citizenId()));
        student.setPhoneNumber(trimToNull(request.phoneNumber()));
        student.setAddress(trimToNull(request.address()));
        student.setClassName(trimToNull(request.className()));
        student.setMajor(trimToNull(request.major()));
        student.setHometown(trimToNull(request.hometown()));
        student.setRoom(room);
    }

    public StudentResponse toResponse(Student student) {
        Room room = student.getRoom();

        return new StudentResponse(
                student.getId(),
                student.getStudentCode(),
                student.getFullName(),
                student.getGender(),
                student.getDateOfBirth(),
                student.getCitizenId(),
                student.getPhoneNumber(),
                student.getAddress(),
                student.getClassName(),
                student.getMajor(),
                student.getHometown(),
                room == null ? null : room.getId(),
                room == null ? null : room.getRoomNumber(),
                room == null ? null : room.getBuilding().getCode());
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}