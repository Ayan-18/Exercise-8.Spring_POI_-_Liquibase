package com.example.exerciseeight.dto;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private String name;
    private Long number;

    @Override
    public String toString() {
        return "GroupDto{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}