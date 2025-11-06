package utils;

import  org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDXExcelReader {
    private Workbook workbook;
    private Sheet sheet;
    private Map<String, Integer> headermap = new HashMap<>();

    public PDXExcelReader(String filePath, String SheetName) throws IOException {

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Excel path is null or empty");
        }

        File file = new File(filePath);
        if (!file.exists()) {

            throw new IllegalArgumentException("Excel file doesnot exist at:" + filePath);


        }

        FileInputStream fis = new FileInputStream(file);
        workbook = WorkbookFactory.create(fis);

        sheet = workbook.getSheet(SheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet '" + SheetName + "'not found in file");
        }

        Row headerRow = sheet.getRow(0);
        for (
                int col = 0; col < headerRow.getLastCellNum(); col++) {
            String headerName = headerRow.getCell(col).toString().trim();
            headermap.put(headerName, col);
        }
    }

    public String getCellValue(int rowNUM, String columnName) {
        if (!headermap.containsKey(columnName)) {
            throw new IllegalArgumentException("Column'" + columnName + "'not found in header");
        }
        int colIndex = headermap.get(columnName);
        Row row = sheet.getRow(rowNUM);
        if (row == null || row.getCell(colIndex) == null) {
            return "";

        }
        return row.getCell(colIndex).toString();
    }

    public int getRowCount() {
        return sheet.getPhysicalNumberOfRows() - 1;

    }

    public void close() throws IOException {
        workbook.close();
    }

    public List<String> getCellValues(String imageLocation) {


        return List.of();
    }
}





