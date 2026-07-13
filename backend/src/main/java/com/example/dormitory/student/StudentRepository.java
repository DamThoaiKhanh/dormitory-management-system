package com.example.dormitory.student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository
        extends JpaRepository<Student, Long> {

    boolean existsByStudentCodeIgnoreCase(String studentCode);

    boolean existsByCitizenId(String citizenId);

    Optional<Student> findByStudentCodeIgnoreCase(String studentCode);

    long countByRoomId(Long roomId);

    long countByRoomIdAndIdNot(Long roomId, Long studentId);
}