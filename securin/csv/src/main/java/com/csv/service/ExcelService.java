package com.csv.service;

import com.csv.model.Weather;
import com.csv.repository.WeatherRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    private final WeatherRepository repository;

    public ExcelService(WeatherRepository repository) {
        this.repository = repository;
    }

    public void saveExcelData(MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            List<Weather> weatherList = new ArrayList<>();
            int totalRows = sheet.getLastRowNum();

            for (int i = 1; i <= totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                Weather weather = new Weather();

                
                String dateText = row.getCell(0).toString().trim();
                if (dateText.length() >= 8) {
                    String datePart = dateText.substring(0, 8);
                    int year = Integer.parseInt(datePart.substring(0, 4));
                    int month = Integer.parseInt(datePart.substring(4, 6));
                    int day = Integer.parseInt(datePart.substring(6, 8));
                    weather.setDate(LocalDate.of(year, month, day));
                }

                weather.setCondition(row.getCell(1).toString().trim());
                weather.setTemperature(Double.parseDouble(row.getCell(2).toString().trim()));
                weather.setHumidity(Double.parseDouble(row.getCell(3).toString().trim()));
                weather.setPressure(Double.parseDouble(row.getCell(4).toString().trim()));

                weatherList.add(weather);

             
                if (weatherList.size() >= 500) {
                    repository.saveAll(weatherList);
                    weatherList.clear();
                }
            }

            if (!weatherList.isEmpty()) {
                repository.saveAll(weatherList);
            }

            workbook.close();

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }
}