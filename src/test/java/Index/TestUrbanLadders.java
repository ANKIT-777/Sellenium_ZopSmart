package Index;

import Pages.HomePage;
import Pages.PriceFiltering;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.example.ConfigLoader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import utills.BrowserSetup;
import utills.ExcelWritter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.testng.annotations.Listeners;
import utills.TestListener;


@Listeners(TestListener.class)
public class TestUrbanLadders {
    public static WebDriver driver;
    private ConfigLoader configLoader;
    WebDriverWait wait;
    List<WebElement> filteredNames;
    List<WebElement> filteredPrices;
    static ExtentReports reports;
    static ExtentTest test;


    @BeforeTest
    public void setup() throws IOException {
        String browser = "chrome";
        driver = BrowserSetup.getDriver(browser);
        driver.manage().window().maximize();
        driver.get("https://www.urbanladder.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        reports = new ExtentReports("/Users/ankitsharma/Desktop/JAVA_Testing/Sellenium_ZopSmart/src/main/Reports/report2.html", true);
    }


    @Test(priority = 1)
    public void homePage() throws IOException, InterruptedException {
        HomePage homePage = new HomePage(driver);
        homePage.testHomepage();
        reports.endTest(test);

    }

    @Test(priority = 2)
    public void priceFiltering(){
        PriceFiltering priceFiltering = new PriceFiltering(driver);
        priceFiltering.priceButton();
        priceFiltering.testpriceFiltering(30, 40);
        filteredNames =  priceFiltering.getFilteredProducts(wait);
        filteredPrices = priceFiltering.getFilteredProductsPrices(wait);

        Assert.assertFalse(filteredNames.isEmpty());
        Assert.assertFalse(filteredPrices.isEmpty());

        reports.endTest(test);

    }


    @AfterClass
    public void exprotingValues() throws IOException {
        ExcelWritter excelExport = new ExcelWritter();
        ExcelWritter.writeProductsToExcel(filteredNames,filteredPrices);
        driver.quit();
        reports.flush();
    }

}





