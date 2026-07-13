package com.example.dormitory.student;

import com.example.dormitory.common.model.Gender;
import com.example.dormitory.room.Room;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_code", columnNames = "student_code"),
        @UniqueConstraint(name = "uk_student_citizen_id", columnNames = "citizen_id")
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code", nullable = false, length = 30)
    private String studentCode;

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

    @Column(name = "class_name", length = 100)
    private String className;

    @Column(length = 150)
    private String major;

    @Column(length = 150)
    private String hometown;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public String getStudentCode() {
        return studentCode;
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

    public String getClassName() {
        return className;
    }

    public String getMajor() {
        return major;
    }

    public String getHometown() {
        return hometown;
    }

    public Room getRoom() {
        return room;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
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

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}