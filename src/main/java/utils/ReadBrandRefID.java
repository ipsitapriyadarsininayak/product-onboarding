package utils;

import net.bytebuddy.asm.Advice;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadBrandRefID {
    public static List<String> getAllBrandRefIds(String filepath) throws IOException {
        List<String> brandRefIds = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filepath));
             Workbook workbook = new HSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);//first sheet
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        String value = cell.getStringCellValue();
                        if (value != null && !value.isEmpty()) {
                            brandRefIds.add(value.trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brandRefIds;
    }
    public static String getCellValueAsString(Cell cell){
        switch (cell.getCellType()){
            case STRING :
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                else{
                    return String.valueOf((long) cell.getNumericCellValue());

                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            case ERROR:
            default:
                return "";

        }
    }
}


















