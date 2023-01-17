package com.example.exerciseeight.controller;

import com.example.exerciseeight.dto.MeterDto;
import com.example.exerciseeight.service.MeterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1.0")
@RequiredArgsConstructor
public class MeterRestController {
    private final MeterService meterService;

    @PostMapping(path = "/example", consumes = "application/json")
    public void getReading(@Valid @RequestBody MeterDto meterDto) {
        meterService.save(meterDto);
    }

    @GetMapping(path = "/excell")
    public void excell(HttpServletResponse response) throws IOException {
        meterService.excell(response);
    }

    @GetMapping(path = "/group", produces = "application/json")
    public Map<String, Long> group() {
        return meterService.group();
    }
}