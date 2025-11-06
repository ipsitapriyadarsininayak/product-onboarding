package utils;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

public class ExcelReader {

    //use absolute path to desktop
    private static final String EXCEL_PATH = System.getProperty("user.home") + "/Desktop/credentials.xlsx";

    public static String getDecodedPassword() throws FileNotFoundException {
        //Initialize decodedPassword as null; will store the decode password once found
        String decodedPassword = null;


        //Try with resource to safely open and code the excel file input stream & workbook
         try(FileInputStream fis = new FileInputStream(new File(EXCEL_PATH));
            Workbook workbook = WorkbookFactory.create(fis)){

            //Get the first sheet in the excel workbook (index 0)
             Sheet sheet = workbook.getSheetAt(0);

            //Get the header row(assumed to first row, index 0
             Row headerRow = sheet.getRow(0);

             //Get the value row (assumed to be second row, index 1
             Row valueRow = sheet.getRow(1);

             //Ensure both rows exist before processing
             if (headerRow!=null && valueRow !=null){
                 //loop through each coloumn in the header row
                 for (int i=0; i<headerRow.getLastCellNum(); i++){

                     //Read the cell from the header row
                     Cell headerCell = headerRow.getCell(i);

                     //Check if the cell's value is "Password"
                     if(headerCell !=null && headerCell.getStringCellValue().trim().equalsIgnoreCase("Password")) {

                         //get the correspomding cell from the value row
                         Cell valueCell = valueRow.getCell(i);
                         if (valueCell != null) {
                            String encodedPassword = valueCell.getStringCellValue().trim();

                             //Decode the Base64 encoded password to the plain string
                             decodedPassword = new String(Base64.getDecoder().decode(encodedPassword));
                             break;
                         }
                     }
                 }
             }
         } catch (Exception e) {
            e.printStackTrace();
        }
        if(decodedPassword == null){
            throw new RuntimeException("Password not found in the Excel File");
        }
        return decodedPassword;
    }
}
