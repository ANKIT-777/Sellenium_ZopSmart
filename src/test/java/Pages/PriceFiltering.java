package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class PriceFiltering {

    private final WebDriver driver;
    private final Actions actions;

    public PriceFiltering(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        actions = new Actions(driver);

    }

    @FindBy(css = "li.item[data-group=\"price\"")
    private WebElement priceButton;

    @FindBy(css = "a.close-reveal-modal.hide-mobile")
    private WebElement PopUpCloser;



    public void priceButton(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(PopUpCloser));

    }

    public void testpriceFiltering(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(priceButton));
        priceButton.click();
        WebElement slider = driver.findElement(By.cssSelector("div.range-slider"));
        WebElement minHandle = slider.findElement(By.className("noUi-handle-lower"));
        WebElement maxHandle = slider.findElement(By.className("noUi-handle-upper"));
        actions.dragAndDropBy(minHandle,30,0).perform();

        WebElement productBox = driver.findElement(By.cssSelector("div.productbox"));
        wait.until(ExpectedConditions.stalenessOf(productBox));
    }

    public List<WebElement> getFilteredProducts(WebDriverWait wait) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("span.name")));
    }
    public List<WebElement> getFilteredProductsPrices(WebDriverWait wait) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy((By.cssSelector("div.price-number > span"))));
    }


}
