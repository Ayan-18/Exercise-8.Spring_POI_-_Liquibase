package com.example.exerciseeight.service;

import com.example.exerciseeight.dto.GroupDto;
import com.example.exerciseeight.dto.MeterDto;
import com.example.exerciseeight.dto.ReportDto;
import com.example.exerciseeight.entity.Meter;
import com.example.exerciseeight.entity.MeterData;
import com.example.exerciseeight.entity.MeterGroup;
import com.example.exerciseeight.repository.MeterDataRepository;
import com.example.exerciseeight.repository.MeterGroupRepository;
import com.example.exerciseeight.repository.MeterRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MeterService {
    private final MeterRepository meterRepository;
    private final MeterGroupRepository meterGroupRepository;
    private final MeterDataRepository meterDataRepository;


    public Map<String, Long> group() {
        List<GroupDto> groups = meterGroupRepository.findAllByName();
        Map<String, Long> map = new LinkedHashMap<>();
        for (GroupDto group : groups) {
            map.put((group.getName()), group.getNumber());
        }
        return map;
    }

    public void excell(HttpServletResponse response) throws IOException {
        LocalDate date = LocalDate.of(2022, 11, 23);
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = date.with(TemporalAdjusters.firstDayOfNextMonth());
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row1 = sheet.createRow(0);
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("Группа");
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue("Мин показание");
        Cell cell3 = row1.createCell(2);
        cell3.setCellValue("Макс показание");
        Cell cell4 = row1.createCell(3);
        cell4.setCellValue("Расход");
        List<MeterGroup> meterGroups = meterGroupRepository.findAll();
        List<Meter> meters = meterRepository.findAll();
        int rowCount = 1;
        double all = 0;
        for (MeterGroup meterGroup : meterGroups) {
            Row rowGroup = sheet.createRow(rowCount);
            Cell cellGroup = rowGroup.createCell(0);
            cellGroup.setCellValue(meterGroup.getName());
            rowCount++;
            double total = 0;
            for (Meter meter : meterGroup.getMeterList()) {
                Row rowMeter = sheet.createRow(rowCount);
                Cell cellMeter = rowMeter.createCell(0);
                cellMeter.setCellValue("Сч. " + meter.getId() + " (" + meter.getType() + ")");
                Cell cellDataMin = rowMeter.createCell(1);
                Cell cellDataMax = rowMeter.createCell(2);
                Cell cellDataTotal = rowMeter.createCell(3);
                double min = meterDataRepository.findFirstByMeterIdAndDate(meter.getId(), firstDay).getReading();
                double max = meterDataRepository.findLastByMeterIdAndDate(meter.getId(), lastDay).getReading();
                double used = max - min;
                cellDataMin.setCellValue(min);
                cellDataMax.setCellValue(max);
                cellDataTotal.setCellValue(used);
                total += used;
                rowCount++;
            }
            Row rowTotal = sheet.createRow(rowCount);
            Cell cellTotal = rowTotal.createCell(0);
            Cell cellTotal2 = rowTotal.createCell(3);
            cellTotal.setCellValue("Итого " + meterGroup.getName() + ": ");
            cellTotal2.setCellValue(total);
            all += total;
            rowCount++;
        }
        Row rowAll = sheet.createRow(rowCount);
        Cell cellAll = rowAll.createCell(0);
        Cell cellAll2 = rowAll.createCell(3);
        cellAll.setCellValue("Итого:");
        cellAll2.setCellValue(all);

        response.setHeader("Content-Disposition", "inline;filename=\"" + URLEncoder.encode("Отчет.xls", StandardCharsets.UTF_8) + "\"");
        response.setContentType("application/xls");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public void excellRead(MultipartFile multipartFile) throws IOException {
        Workbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
        Iterator<Row> rowIterator = workbook.getSheetAt(0).rowIterator();
        int i = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (i == 0) {
                i++;
            } else {
                long meterId = (long) workbook.getSheetAt(0).getRow(i).getCell(0).getNumericCellValue();
                String type = workbook.getSheetAt(0).getRow(i).getCell(1).getStringCellValue();
                String group = workbook.getSheetAt(0).getRow(i).getCell(2).getStringCellValue();
                LocalDateTime localDateTime = LocalDateTime.parse(workbook.getSheetAt(0).getRow(i).getCell(3).getStringCellValue());
                double reading = workbook.getSheetAt(0).getRow(i).getCell(4).getNumericCellValue();
                MeterDto meterDto = new MeterDto(meterId, type, group, localDateTime, reading);
                save(meterDto);
                i++;
            }
        }
    }

    public void save(MeterDto meterDto) {
        MeterGroup meterGroup = new MeterGroup();
        meterGroup.setName(meterDto.getMeterGroup());
        if (!meterGroupRepository.findByName(meterDto.getMeterGroup()).getName().equals(meterDto.getMeterGroup())) {
            meterGroupRepository.save(meterGroup);
        }
        Meter meter = new Meter();
        meter.setId(meterDto.getMeterId());
        meter.setType(meterDto.getType());
        meter.setMeterGroup(meterGroupRepository.findByName(meterGroup.getName()));
        if (meterRepository.findById(meterDto.getMeterId()).isEmpty()) {
            meterRepository.save(meter);
        }
        MeterData meterData = new MeterData();
        meterData.setMeter(meter);
        meterData.setDateOfData(meterDto.getDateTime());
        meterData.setReading(meterDto.getCurrentReading());
        meterDataRepository.save(meterData);
    }

    public Map<String, List<ReportDto>> report() {
        Map<String, List<ReportDto>> map = new LinkedHashMap<>();
        LocalDate date = LocalDate.of(2022, 11, 23);
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = date.with(TemporalAdjusters.firstDayOfNextMonth());
        List<MeterGroup> meterGroups = meterGroupRepository.findAll();
        for (MeterGroup meterGroup : meterGroups) {
            List<ReportDto> reportDtoList = new ArrayList<>();
            List<Meter> meters = meterRepository.findAllByMeterGroup(meterGroup);
            for (Meter meter : meters) {
                ReportDto reportDto = new ReportDto();
                reportDto.setMeterId(meter.getId());
                reportDto.setMeterType(meter.getType());
                reportDto.setFirstReading(meterDataRepository.findFirstByMeterIdAndDate(meter.getId(), firstDay).getReading());
                reportDto.setLastReading(meterDataRepository.findLastByMeterIdAndDate(meter.getId(), lastDay).getReading());
                reportDtoList.add(reportDto);
            }
            map.put(meterGroup.getName(), reportDtoList);
        }
        return map;
    }

    public Map<String, Double> reportTotal() {
        Map<String, Double> map = new LinkedHashMap<>();
        LocalDate date = LocalDate.of(2022, 11, 23);
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = date.with(TemporalAdjusters.firstDayOfNextMonth());
        List<MeterGroup> meterGroups = meterGroupRepository.findAll();
        double all = 0.0;
        for (MeterGroup meterGroup : meterGroups) {
            double total = 0.0;
            List<Meter> meters = meterRepository.findAllByMeterGroup(meterGroup);
            for (Meter meter : meters) {
                double used = 0.0;
                Double first = meterDataRepository.findFirstByMeterIdAndDate(meter.getId(), firstDay).getReading();
                Double last = meterDataRepository.findLastByMeterIdAndDate(meter.getId(), lastDay).getReading();
                used = last - first;
                total += used;
            }
            all += total;
            map.put(meterGroup.getName(), total);
        }
        map.put("all", all);
        return map;
    }
}