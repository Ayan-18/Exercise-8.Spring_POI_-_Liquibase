package com.example.exerciseeight.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meter_data")
@Getter
@Setter
public class MeterData {
    @Id
    @SequenceGenerator(name = "meter_data_id_seq",sequenceName = "meter_data_id_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "meter_id")
    private Meter meter;
    private LocalDateTime dateOfData;
    private Double reading;
}
