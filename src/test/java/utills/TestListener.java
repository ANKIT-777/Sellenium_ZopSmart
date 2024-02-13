package utills;

import Index.TestUrbanLadders;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;




public class TestListener implements ITestListener {
    static ExtentReports reports;
    static ExtentTest test;
    public WebDriver driver;


    public TestListener() {

    }

    public TestListener(WebDriver driver){
        this.driver =driver;
        PageFactory.initElements(driver,this);
    }

    public void onTestFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            reports = new ExtentReports("/Users/ankitsharma/Desktop/JAVA_Testing/Sellenium_ZopSmart/src/main/Reports/report.html",true);
            test = reports.startTest(result.getMethod().getMethodName());
            test.log(LogStatus.FAIL, result.getThrowable());
            try {
                test.log(LogStatus.FAIL, test.addScreenCapture(captureScreen()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            reports.endTest(test);

        }
    }

    public String captureScreen() throws IOException {
        File scrFile = ((TakesScreenshot) TestUrbanLadders.driver).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File("src/main/Reports/screenshots/" + System.currentTimeMillis() + ".png");
        String absolutePath  = destinationFile.getAbsolutePath();
        FileUtils.copyFile(scrFile,destinationFile);
        return absolutePath;
    }


}
