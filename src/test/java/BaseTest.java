import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.NoSuchElementException;

public class BaseTest {
    WebDriver driver;
    WebDriverWait wait;
    Actions action;

    @BeforeClass
    public void setUpDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 500);
        action = new Actions(driver);
    }

        @AfterClass
    public void closeDriver() {
        driver.quit();
    }

    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }

    public void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boolean isElementPresent(String path) {
        try {
            driver.findElement(By.xpath(path));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean elementIsNotPresent(String path){
        return driver.findElements(By.xpath(path)).isEmpty();
    }

    public Boolean isCSSElementPresent(String path) {
        try {
            driver.findElement(By.cssSelector(path));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void scrollToElement(WebElement elem) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elem);
        try {
            Thread.sleep((10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}