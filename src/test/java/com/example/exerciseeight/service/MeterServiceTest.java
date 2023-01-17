package com.example.exerciseeight.service;

import com.example.exerciseeight.dto.MeterDto;
import com.example.exerciseeight.entity.Meter;
import com.example.exerciseeight.entity.MeterData;
import com.example.exerciseeight.entity.MeterGroup;
import com.example.exerciseeight.repository.MeterDataRepository;
import com.example.exerciseeight.repository.MeterGroupRepository;
import com.example.exerciseeight.repository.MeterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        meterService.excell(mockHttpServletResponse);
        verify(meterRepository).findAll();
        verify(meterGroupRepository).findAll();
        assertTrue(mockHttpServletResponse.isCommitted());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals(2, mockHttpServletResponse.getHeaderNames().size());
        assertEquals("application/xls", mockHttpServletResponse.getContentType());
    }

    @Test
    void testExcellRead() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "multipartFile",
                "templates/test.xls",
                "application/x-xls",
                new ClassPathResource("templates/test.xls").getInputStream());
        testSave();
        meterService.excellRead(mockMultipartFile);
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
        Double expectedGetResult = 0.0d;
        assertEquals(expectedGetResult, actualReportTotalResult.get("all"));
        verify(meterGroupRepository).findAll();
    }
}

