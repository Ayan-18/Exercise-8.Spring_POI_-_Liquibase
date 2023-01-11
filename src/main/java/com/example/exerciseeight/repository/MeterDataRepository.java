package com.example.exerciseeight.repository;

import com.example.exerciseeight.entity.Meter;
import com.example.exerciseeight.entity.MeterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MeterDataRepository extends JpaRepository<MeterData,Long> {

    @Query(nativeQuery = true,value = "SELECT md.* from meter_data md where md.meter_id = :meter and md.date_of_data >= :date order by md.date_of_data limit 1")
    MeterData findFirstByMeterIdAndDate(@Param("meter") long meter,@Param("date") LocalDate date);

    @Query(nativeQuery = true,value ="select md.* from meter_data md where md.meter_id = :meter and md.date_of_data < :date  order by md.date_of_data desc limit 1")
    MeterData findLastByMeterIdAndDate(@Param("meter") long meterId,@Param("date") LocalDate date);

    List<MeterData> findAllByMeter(Meter meter);
}
