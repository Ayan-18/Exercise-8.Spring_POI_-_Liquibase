package com.example.exerciseeight.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "meter_group")
@Getter
@Setter
public class MeterGroup {
    @Id
    @SequenceGenerator(name = "meter_group_id_seq",sequenceName = "meter_group_id_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "meterGroup")
    private List<Meter> meterList;

}
