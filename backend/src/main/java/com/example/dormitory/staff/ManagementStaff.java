package com.example.dormitory.staff;

import com.example.dormitory.building.Building;
import com.example.dormitory.common.model.Gender;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "management_staff", uniqueConstraints = {
        @UniqueConstraint(name = "uk_staff_employee_code", columnNames = "employee_code"),
        @UniqueConstraint(name = "uk_staff_citizen_id", columnNames = "citizen_id"),
        @UniqueConstraint(name = "uk_staff_phone_number", columnNames = "phone_number")
})
public class ManagementStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", nullable = false, length = 30)
    private String employeeCode;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "citizen_id", length = 30)
    private String citizenId;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_building_code")
    private Building assignedBuilding;

    public ManagementStaff() {
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Building getAssignedBuilding() {
        return assignedBuilding;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAssignedBuilding(Building assignedBuilding) {
        this.assignedBuilding = assignedBuilding;
    }
}