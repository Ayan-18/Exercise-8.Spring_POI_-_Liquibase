package com.example.exerciseeight.controller;

import com.example.exerciseeight.dto.MeterDto;
import com.example.exerciseeight.service.MeterService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLConnection;
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
        meterService.excell();
        File file = new File("C:\\Users\\77757\\Desktop\\JAVA\\Elasticsearch\\123.xls");
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());
        response.setContentType(mimeType);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping(path = "/read")
    public void excellRead() throws IOException {
        meterService.excellRead();
    }

    @GetMapping(path = "/group",produces = "application/json")
    public Map<String, Long> group(){
        return meterService.group();
    }
}