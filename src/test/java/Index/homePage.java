package Index;

import org.checkerframework.checker.units.qual.N;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.interactions.Actions;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;


import java.io.IOException;
import java.time.Duration;
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


    @BeforeTest
    public void setup() throws InterruptedException, IOException {

        driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver","/Users/ankitsharma/Desktop/JAVA_Testing/Sellenium_ZopSmart/src/main/resources/chromedriver");
        driver.manage().window().maximize();
        driver.get("https://www.urbanladder.com/");

    }

    @Test(priority = 1)
    public void Homepage() throws InterruptedException, IOException {

        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement Living = driver.findElement(By.cssSelector("li.livingunit.topnav_item"));
        actions.moveToElement(Living).perform();

        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(By.cssSelector("a.inverted[href=\"/coffee-table?src=g_topnav_living_tables_coffee-tables\"] span")
                ))).click();


        WebElement closeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.close-reveal-modal.hide-mobile")));
        closeButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("li.item[data-group=\"price\"]"))).
                click();



        WebElement slider = driver.findElement(By.cssSelector("div.range-slider"));
        WebElement minHandle = slider.findElement(By.className("noUi-handle-lower"));
        WebElement maxHandle = slider.findElement(By.className("noUi-handle-upper"));

        actions.dragAndDropBy(minHandle, 30, 0).perform();
        actions.dragAndDropBy(maxHandle, -30, 0).perform();


        NamesOfProducts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("span.name")));
        ActualPriceList = driver.findElements(By.cssSelector("div.price-number>span"));
        putValueinExcel();

        driver.quit();

    }





    public void putValueinExcel() throws IOException {
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

            FileOutputStream outputStream = new FileOutputStream("Result.xlsx");
            workbook.write(outputStream);
            outputStream.close();
        }


}


