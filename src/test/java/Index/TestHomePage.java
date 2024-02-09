package Index;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.interactions.Actions;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.example.ConfigLoader;

import static utills.BrowserSetup.getDriver;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;



public class TestHomePage implements ITestListener {
    private WebDriver driver;
    public ConfigLoader configure;
    List<WebElement> NamesOfProducts;
    List<WebElement> ActualPriceList;
    WebDriverWait wait;

    static ExtentReports reports;
    static ExtentTest test;



    @FindBy(css = "li.livingunit.topnav_item")
    private WebElement livingButton;

    @FindBy(css = "a[href=\"/coffee-table?src=g_topnav_living_tables_coffee-tables\"] > span" )
    private WebElement coffeeButton;

    @FindBy(css = "div.gname:contains(\"Price\")")
    private WebElement priceButton;

    private String excelFileName;
    private Actions actions;


    @BeforeTest
    public void setup() throws IOException {
        configure = new ConfigLoader();
        String browser = configure.getBrowser();
        excelFileName = configure.getExcel();

        driver = getDriver(browser);

        driver.manage().window().maximize();
        driver.get("https://www.urbanladder.com/");

        reports = new ExtentReports("/Users/ankitsharma/Desktop/JAVA_Testing/Sellenium_ZopSmart/src/main/Reports/report.html",true);
        test = reports.startTest("Extent report Demo");
        test.log(LogStatus.INFO,"Test Calss started");

        String title = driver.getTitle();
        test.log(LogStatus.PASS,title);
    }




    @Test(priority = 1)
    public void Homepage() throws IOException, InterruptedException {

        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement Living = driver.findElement(By.cssSelector("li.livingunit.topnav_item"));
        actions.moveToElement(Living).perform();
            test.log(LogStatus.INFO,driver.getTitle());

        WebElement coffeeTableButton = driver.findElement(By.cssSelector("a.inverted[href=\"/coffee-table?src=g_topnav_living_tables_coffee-tables\"] span"));
        wait.until(ExpectedConditions.visibilityOf(coffeeTableButton)).click();
    }

    @Test(priority = 2)
    public void priceFiltering() throws IOException {

                test = reports.startTest("price page test");
        WebElement popupCloser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.close-reveal-modal.hide-mobile")));
        Assert.assertTrue(popupCloser.isDisplayed());

        popupCloser.click();
                test.log(LogStatus.PASS,"Popup is gelling closed successfully");

        WebElement priceBox = driver.findElement(By.cssSelector("li.item[data-group=\"price\"]"));
        wait.until(ExpectedConditions.visibilityOf(priceBox)).click();

        WebElement slider = driver.findElement(By.cssSelector("div.range-slider"));
        WebElement minHandle = slider.findElement(By.className("noUi-handle-lower"));
        WebElement maxHandle = slider.findElement(By.className("noUi-handle-upper"));

        actions.dragAndDropBy(minHandle, 30, 0).perform();
        actions.dragAndDropBy(maxHandle, -30, 0).perform();

        WebElement productBox = driver.findElement(By.cssSelector("div.productbox"));
        wait.until(ExpectedConditions.stalenessOf(productBox));


        NamesOfProducts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("span.name")));
        ActualPriceList = driver.findElements(By.cssSelector("div.price-number > span"));

        Assert.assertFalse(NamesOfProducts.isEmpty());
        Assert.assertFalse(ActualPriceList.isEmpty());

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

            FileOutputStream outputStream = new FileOutputStream(excelFileName+ System.currentTimeMillis()  +".xlsx");
            workbook.write(outputStream);
            outputStream.close();
        }

        public static String captureScreen(WebDriver driver) throws IOException {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destinationFile = new File("src/main/Reports/screenshots/" + System.currentTimeMillis() + ".png");
            String absolutePath  = destinationFile.getAbsolutePath();

            FileUtils.copyFile(scrFile,destinationFile);
            return absolutePath;
        }


    @AfterMethod
    public void afterMethod(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(LogStatus.FAIL, result.getThrowable());
            test.log(LogStatus.FAIL, test.addScreenCapture(captureScreen(driver)));
        }
    }

    @AfterTest
        public void Close() throws IOException {
            test.log(LogStatus.PASS, test.addScreenCapture(captureScreen(driver)) +"Test Has completed successfully");
            driver.quit();
            reports.endTest(test);
            reports.flush();
        }
}


