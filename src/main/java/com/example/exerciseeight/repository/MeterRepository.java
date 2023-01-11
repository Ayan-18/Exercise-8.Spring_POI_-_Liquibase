package com.example.exerciseeight.repository;

import com.example.exerciseeight.entity.Meter;
import com.example.exerciseeight.entity.MeterGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeterRepository extends JpaRepository<Meter,Long> {

    List<Meter> findAllByMeterGroup(MeterGroup meterGroup);

}
