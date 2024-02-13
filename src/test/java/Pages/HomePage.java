package Pages;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class HomePage {


    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    WebDriverWait wait;
    WebDriver driver;
    static ExtentTest test;

    @FindBy(css = "li.livingunit.topnav_item")
    private WebElement Living;

    @FindBy(css = "a.inverted[href=\"/coffee-table?src=g_topnav_living_tables_coffee-tables\"] span")
    private WebElement coffeeTableButton;

    public void testHomepage() throws IOException, InterruptedException {

        Actions actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        actions.moveToElement(Living).perform();

        wait.until(ExpectedConditions.visibilityOf(coffeeTableButton)).click();
    }

}
