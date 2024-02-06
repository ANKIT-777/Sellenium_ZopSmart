package Index;

import org.checkerframework.checker.units.qual.N;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.interactions.Actions;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;


import java.io.IOException;
import java.util.List;

public class homePage {

    private WebDriver driver;
    List<WebElement> NamesOfProducts;
    List<WebElement> ActualPriceList;


    @FindBy(css = "li.livingunit.topnav_item")
    private WebElement livingButton;

    @FindBy(css = "a[href=\"/coffee-table?src=g_topnav_living_tables_coffee-tables\"] > span" )
    private WebElement coffeeButton;

    @FindBy(css = "div.gname:contains(\"Price\")")
    private WebElement priceButton;


    @Test(priority = 1)
    public void setup() throws InterruptedException, IOException {
        driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver","/Users/ankitsharma/Desktop/JAVA_Testing/Sellenium_ZopSmart/src/main/resources/chromedriver");
        driver.manage().window().maximize();
        driver.get("https://www.urbanladder.com/");
        Actions actions = new Actions(driver);
        actions.moveToElement(
                driver.findElement(By.cssSelector("li.livingunit.topnav_item"))).perform();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("a.inverted[href=\"/coffee-table?src=g_topnav_living_tables_coffee-tables\"] span")).click();
        Thread.sleep(2000);
        actions.moveToElement(driver.findElement(By.cssSelector("li.item[data-group=\"price\"] "))).perform();
//        WebElement slider = driver.findElement(By.className("range-slider"));
//        WebElement minHandle = slider.findElement(By.className("noUi-handle-lower"));
//        WebElement maxHandle = slider.findElement(By.className("noUi-handle-upper"));
//        actions.dragAndDropBy(minHandle, 40, 0).perform();
//
//        // Drag the max handle to the desired position
//        actions.dragAndDropBy(maxHandle, -30, 0).perform();
        NamesOfProducts = driver.findElements(By.cssSelector("span.name"));

        System.out.println(NamesOfProducts.size());
        System.out.println("----------------------------");

        for ( WebElement x : NamesOfProducts){
            System.out.println(x.getText());
        }

        ActualPriceList = driver.findElements(By.cssSelector("div.price-number>span"));
        System.out.println(ActualPriceList.size());
        System.out.println("---------------------------");

        for (WebElement y : ActualPriceList){
            System.out.println(y.getText());
        }

        putValueinExcel();

    }

    public void putValueinExcel() throws IOException {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Products");
            for (int i = 0; i < NamesOfProducts.size(); i++) {
                Row row = sheet.createRow(i);
                Cell nameCell = row.createCell(0);
                nameCell.setCellValue(NamesOfProducts.get(i).getText());

                Cell priceCell = row.createCell(1);
                priceCell.setCellValue(ActualPriceList.get(i).getText());
            }

            FileOutputStream outputStream = new FileOutputStream("products.xlsx");
            workbook.write(outputStream);

    }



}
