package com.example.exerciseeight.service;

import com.example.exerciseeight.dto.GroupDto;
import com.example.exerciseeight.repository.MeterGroupRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.util.List;

@RequiredArgsConstructor
public class GroupService {
    private Session session;
    private MeterGroupRepository meterGroupRepository;

    public GroupService(Session session) {
        this.session=session;
    }


    public List<GroupDto> group(){
        return meterGroupRepository.findAllByName();
    }
}
