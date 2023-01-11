package com.example.exerciseeight.repository;

import com.example.exerciseeight.dto.GroupDto;
import com.example.exerciseeight.entity.MeterGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MeterGroupRepository extends JpaRepository<MeterGroup, Long> {

    @Query("SELECT  new com.example.exerciseeight.dto.GroupDto(mg.name, count(m.id)) from MeterGroup mg join Meter m on mg.id = m.meterGroup.id group by mg.name order by mg.name")
    List<GroupDto> findAllByName();
    MeterGroup findByName(String name);

    List<MeterGroup> findAll();

}
