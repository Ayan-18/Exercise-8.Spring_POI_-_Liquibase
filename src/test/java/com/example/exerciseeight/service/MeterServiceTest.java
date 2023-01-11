package com.example.exerciseeight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.exerciseeight.dto.GroupDto;
import com.example.exerciseeight.dto.MeterDto;
import com.example.exerciseeight.dto.ReportDto;
import com.example.exerciseeight.entity.Meter;
import com.example.exerciseeight.entity.MeterData;
import com.example.exerciseeight.entity.MeterGroup;
import com.example.exerciseeight.repository.MeterDataRepository;
import com.example.exerciseeight.repository.MeterGroupRepository;
import com.example.exerciseeight.repository.MeterRepository;

import java.io.*;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;

@ContextConfiguration(classes = {MeterService.class})
@ExtendWith(SpringExtension.class)
class MeterServiceTest {
    @MockBean
    private MeterDataRepository meterDataRepository;

    @MockBean
    private MeterGroupRepository meterGroupRepository;

    @MockBean
    private MeterRepository meterRepository;

    @Autowired
    private MeterService meterService;




    @Test
    void testExcell() throws IOException {
        when(meterRepository.findAll()).thenReturn(new ArrayList<>());
        when(meterGroupRepository.findAll()).thenReturn(new ArrayList<>());
        meterService.excell();
    }

    @Test
    void testExcellRead() throws IOException {
        MeterGroup meterGroup = new MeterGroup();
        meterGroup.setId(1L);
        meterGroup.setName("Name");

        Meter meter = new Meter();
        meter.setId(1L);
        meter.setMeterGroup(meterGroup);
        meter.setType("Type");
        when(meterRepository.save((Meter) any())).thenReturn(meter);
        when(meterRepository.findById((Long) any())).thenReturn(Optional.empty());
        when(meterGroupRepository.save((MeterGroup) any())).thenReturn(meterGroup);
        when(meterGroupRepository.findByName((String) any())).thenReturn(meterGroup);

        MeterData meterData = new MeterData();
        meterData.setDateOfData(LocalDateTime.of(2022, 11, 23, 1, 0, 0));
        meterData.setId(1L);
        meterData.setMeter(meter);
        meterData.setReading(5.0d);
        when(meterDataRepository.save((MeterData) any())).thenReturn(meterData);
        meterService.excellRead();
        verify(meterRepository).save((Meter) any());
        verify(meterRepository).findById((Long) any());
        verify(meterGroupRepository, atLeast(1)).findByName((String) any());
        verify(meterGroupRepository).save((MeterGroup) any());
        verify(meterDataRepository).save((MeterData) any());
    }

    @Test
    void testSave() {
        MeterGroup meterGroup = new MeterGroup();
        meterGroup.setId(1L);
        meterGroup.setName("Name");

        Meter meter = new Meter();
        meter.setId(1L);
        meter.setMeterGroup(meterGroup);
        meter.setType("Type");

        Optional<Meter> ofResult = Optional.of(meter);
        when(meterRepository.save((Meter) any())).thenReturn(meter);
        when(meterRepository.findById((Long) any())).thenReturn(ofResult);
        when(meterGroupRepository.save((MeterGroup) any())).thenReturn(meterGroup);
        when(meterGroupRepository.findByName((String) any())).thenReturn(meterGroup);

        MeterData meterData = new MeterData();
        meterData.setDateOfData(LocalDateTime.of(2022, 11, 23, 1, 0, 0));
        meterData.setId(1L);
        meterData.setMeter(meter);
        meterData.setReading(5.0d);
        when(meterDataRepository.save((MeterData) any())).thenReturn(meterData);
        meterService.save(new MeterDto());
        verify(meterRepository).findById((Long) any());
        verify(meterGroupRepository, atLeast(1)).findByName((String) any());
        verify(meterGroupRepository).save((MeterGroup) any());
        verify(meterDataRepository).save((MeterData) any());
    }

    @Test
    void testReport() {
        when(meterRepository.findAllByMeterGroup((MeterGroup) any())).thenReturn(new ArrayList<>());

        MeterGroup meterGroup = new MeterGroup();
        meterGroup.setId(1L);
        meterGroup.setName("Name");

        ArrayList<MeterGroup> meterGroupList = new ArrayList<>();
        meterGroupList.add(meterGroup);
        when(meterGroupRepository.findAll()).thenReturn(meterGroupList);
        assertEquals(1, meterService.report().size());
        verify(meterRepository).findAllByMeterGroup((MeterGroup) any());
        verify(meterGroupRepository).findAll();
    }

    @Test
    void testReportTotal() {
        when(meterGroupRepository.findAll()).thenReturn(new ArrayList<>());
        Map<String, Double> actualReportTotalResult = meterService.reportTotal();
        assertEquals(1, actualReportTotalResult.size());
        Double expectedGetResult = new Double(0.0d);
        assertEquals(expectedGetResult, actualReportTotalResult.get("all"));
        verify(meterGroupRepository).findAll();
    }
}

