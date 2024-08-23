package com.example.weatherui.RoomExcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public List<ExcelData> readLocationsFromExcel(InputStream inputStream) {
        List<ExcelData> excelDataList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져옵니다.

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // 첫 행(헤더)은 건너뜁니다.
                }

                ExcelData excelData = new ExcelData();
                excelData.setRegionCode(row.getCell(0).getStringCellValue());
                excelData.setStep1(row.getCell(1).getStringCellValue());
                excelData.setStep2(row.getCell(2).getStringCellValue());
                excelData.setStep3(row.getCell(3).getStringCellValue());
                excelData.setGridX((int) row.getCell(4).getNumericCellValue());
                excelData.setGridY((int) row.getCell(5).getNumericCellValue());
                excelData.setLongitude(row.getCell(6).getNumericCellValue());
                excelData.setLatitude(row.getCell(7).getNumericCellValue());

                excelDataList.add(excelData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return excelDataList;
    }
}

