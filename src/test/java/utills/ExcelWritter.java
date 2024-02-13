package utills;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWritter {
    public static void writeProductsToExcel(List<WebElement> NamesOfProducts,List<WebElement> ActualPriceList) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("Index");
        headerRow.createCell(1).setCellValue("Product Detail");
        headerRow.createCell(2).setCellValue("Price");

        for (int i = 0; i < NamesOfProducts.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell indexCell = row.createCell(0);
            indexCell.setCellValue(i + 1);

            Cell nameCell = row.createCell(1);
            nameCell.setCellValue(NamesOfProducts.get(i).getText());

            Cell priceCell = row.createCell(2);
            priceCell.setCellValue(ActualPriceList.get(i).getText());
        }

        FileOutputStream outputStream = new FileOutputStream( System.currentTimeMillis()  +".xlsx");
        workbook.write(outputStream);
        outputStream.close();
    }

}
