package com.example.dormitory.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagementStaffRepository
        extends JpaRepository<ManagementStaff, Long> {

    boolean existsByEmployeeCodeIgnoreCase(String employeeCode);

    boolean existsByCitizenId(String citizenId);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<ManagementStaff> findByEmployeeCodeIgnoreCase(
            String employeeCode);
}