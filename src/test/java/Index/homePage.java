package Index;
import com.beust.ah.A;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.interactions.Actions;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;


import static utills.BrowserSetup.getDriver;

public class homePage {

    private WebDriver driver;
    List<WebElement> NamesOfProducts;
    List<WebElement> ActualPriceList;
    WebDriverWait wait;


    @FindBy(css = "li.livingunit.topnav_item")
    private WebElement livingButton;

    @FindBy(css = "a[href=\"/coffee-table?src=g_topnav_living_tables_coffee-tables\"] > span" )
    private WebElement coffeeButton;

    @FindBy(css = "div.gname:contains(\"Price\")")
    private WebElement priceButton;

    private String excelFileName;


    @BeforeTest
    public void setup() {

        System.out.println("Enter the browser name (chrome, firefox");
        String browser = "chrome";

        System.out.println("Enter the Excel file name: ");
        excelFileName = "newfile";

        driver = getDriver(browser);
        driver.manage().window().maximize();
        driver.get("https://www.urbanladder.com/");
    }

    @Test(priority = 1)
    public void Homepage() throws IOException, InterruptedException {

        Actions actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));


        WebElement Living = driver.findElement(
                By.cssSelector("li.livingunit.topnav_item"));

        actions.moveToElement(Living).perform();

        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(
                        By.cssSelector("a.inverted[href=\"/coffee-table?src=g_topnav_living_tables_coffee-tables\"] span"))))
                .click();

        WebElement closeButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("a.close-reveal-modal.hide-mobile")));
        closeButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("li.item[data-group=\"price\"]"))).
                click();



        WebElement slider = driver.findElement(By.cssSelector("div.range-slider"));
        WebElement minHandle = slider.findElement(By.className("noUi-handle-lower"));
        WebElement maxHandle = slider.findElement(By.className("noUi-handle-upper"));

        actions.dragAndDropBy(minHandle, 30, 0).perform();
        actions.dragAndDropBy(maxHandle, -30, 0).perform();

        WebElement productBox = driver.findElement(By.cssSelector("div.productbox"));
        wait.until(ExpectedConditions.stalenessOf(productBox));
        NamesOfProducts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("span.name")));
        ActualPriceList = driver.findElements(By.cssSelector("div.price-number > span"));

        ValueForExcel(NamesOfProducts, ActualPriceList);
    }


    public void ValueForExcel(List<WebElement>namesOfProducts,List<WebElement>ActualPrice) throws IOException {

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


            FileOutputStream outputStream = new FileOutputStream(excelFileName+".xlsx");
            workbook.write(outputStream);
            outputStream.close();
        }


        @AfterTest
        public void Close() {
            driver.quit();
        }



}


