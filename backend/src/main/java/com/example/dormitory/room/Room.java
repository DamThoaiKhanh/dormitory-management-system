package com.example.dormitory.room;

import com.example.dormitory.building.Building;
import com.example.dormitory.common.model.RecordStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms", uniqueConstraints = {
        @UniqueConstraint(name = "uk_room_building_number", columnNames = {
                "room_number",
                "building_code"
        })
})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, length = 30)
    private String roomNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_code", nullable = false)
    private Building building;

    @Column(name = "monthly_rent", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyRent;

    @Column(name = "maximum_capacity", nullable = false)
    private Integer maximumCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecordStatus status;

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Building getBuilding() {
        return building;
    }

    public BigDecimal getMonthlyRent() {
        return monthlyRent;
    }

    public Integer getMaximumCapacity() {
        return maximumCapacity;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setMonthlyRent(BigDecimal monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public void setMaximumCapacity(Integer maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }
}