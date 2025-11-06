package utils;
import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ReadExcelData {


    public static String readCellValue(String sheetName, int rowIndex, int cellIndex) throws Exception {
        String excelPath = System.getenv("EXCEL_PATH");
        FileInputStream fis = new FileInputStream(new File(excelPath));
        Workbook workbook = new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(cellIndex);
        String value = cell.getStringCellValue();
        workbook.close();
        return value;
    }
}

