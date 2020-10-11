import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.testng.annotations.*;

//  Create 7 automation tests (1 positive and 6 negative) for testing of authorization (http://demo.guru99.com/Agile_Project/Agi_V1/index.php)

public class LoginTest extends BaseTest {

    String loginUrl = "http://demo.guru99.com/Agile_Project/Agi_V1/index.php";
    String login = "1303";
    String pass = "Guru99";

    @BeforeMethod
    public void navigateToUrl() {
        driver.get(loginUrl);
    }

    @Test
    public void positiveLoginTest() {
        driver.findElement(By.name("uid")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(pass);
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        WebElement logoutButton = wait.until(presenceOfElementLocated(By.linkText("Log out")));
        logoutButton.click();

        wait.until(alertIsPresent());
        assertEquals(driver.switchTo().alert().getText(), "You Have Succesfully Logged Out!!");
        driver.switchTo().alert().accept();
        assertEquals(driver.getCurrentUrl(), loginUrl);
    }

    @Test
    public void wrongUserIDLoginTest() {
        driver.findElement(By.name("uid")).sendKeys("login");
        driver.findElement(By.name("password")).sendKeys(pass);
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        wait.until(alertIsPresent());
        assertEquals(driver.switchTo().alert().getText(), "User or Password is not valid");
        driver.switchTo().alert().accept();
        assertEquals(driver.getCurrentUrl(), loginUrl);
    }

    @Test
    public void wrongPassLoginTest() {
        driver.findElement(By.name("uid")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys("sdkjfhjk");
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        wait.until(alertIsPresent());
        assertEquals(driver.switchTo().alert().getText(), "User or Password is not valid");
        driver.switchTo().alert().accept();
        assertEquals(driver.getCurrentUrl(), loginUrl);
    }

    @Test
    public void wrongUserIDAndPassLoginTest() {
        driver.findElement(By.name("uid")).sendKeys("login");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        wait.until(alertIsPresent());
        assertEquals(driver.switchTo().alert().getText(), "User or Password is not valid");
        driver.switchTo().alert().accept();
        assertEquals(driver.getCurrentUrl(), loginUrl);
    }

    @Test
    public void emptyUserIDLoginTest() {
        driver.findElement(By.name("uid")).sendKeys("");
        driver.findElement(By.name("password")).sendKeys(pass);
        assertEquals(driver.findElement(By.id("message23")).getText(), "User-ID must not be blank");
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        wait.until(alertIsPresent());
        assertEquals(driver.switchTo().alert().getText(), "User or Password is not valid");
        driver.switchTo().alert().accept();
        assertEquals(driver.getCurrentUrl(), loginUrl);
    }

    @Test
    public void emptyPassLoginTest() {
        driver.findElement(By.name("uid")).sendKeys(login);
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        wait.until(alertIsPresent());
        assertEquals(driver.switchTo().alert().getText(), "User or Password is not valid");
        driver.switchTo().alert().accept();
        assertEquals(driver.getCurrentUrl(), loginUrl);
    }

    @Test
    public void emptyUserAndPassLoginTest() {
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        wait.until(alertIsPresent());
        assertEquals(driver.switchTo().alert().getText(), "User or Password is not valid");
        driver.switchTo().alert().accept();
        assertEquals(driver.getCurrentUrl(), loginUrl);
    }

    @Test
    public void resetInstedLoginButtonTest() {
        driver.findElement(By.name("uid")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys("sdkjfhjk");
        driver.findElement(By.xpath("//input[@name='btnReset']")).click();

        String noLogin = driver.findElement(By.name("uid")).getText();
        String noPass = driver.findElement(By.name("password")).getText();
        assertEquals(noLogin, "");
        assertEquals(noPass, "");
    }
}