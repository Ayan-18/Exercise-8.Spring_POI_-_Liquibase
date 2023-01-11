package com.example.exerciseeight.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "meter")
@Getter
@Setter
public class Meter {
    @Id
    private Long id;

    private String type;
    @ManyToOne
    @JoinColumn(name = "meter_group_id")
    private MeterGroup meterGroup;

    @OneToMany(mappedBy = "meter")
    private List<MeterData> meterDataList;

}
