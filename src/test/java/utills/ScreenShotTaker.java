package utills;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenShotTaker {

    public static String captureScreen(WebDriver driver) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File("src/main/Reports/screenshots1/" + System.currentTimeMillis() + ".png");
        String absolutePath  = destinationFile.getAbsolutePath();
        FileUtils.copyFile(scrFile,destinationFile);
        return absolutePath;
    }

}
