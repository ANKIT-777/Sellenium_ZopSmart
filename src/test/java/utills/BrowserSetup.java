package utills;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserSetup {
    public static WebDriver getDriver(String browser) throws IllegalArgumentException {
        WebDriver driver;

        if (browser.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
            driver = new ChromeDriver();
        } else if (browser.equals("firefox"));{
                System.setProperty("webdriver.gecko.driver", "/path/to/geckodriver");
                driver = new FirefoxDriver();
        }
        return driver;
    }

}
