package com.example.dormitory.building;

import java.util.List;

import com.example.dormitory.common.model.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, String> {

    List<Building> findByStatus(RecordStatus status);

    List<Building> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            String code,
            String name);

}
