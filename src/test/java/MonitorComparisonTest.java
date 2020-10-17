//  Write 2 tests (add usage of all 8 types of selectors):

//  1) Verification of comparison of 2 monitors
//  1. Navigate to https://rozetka.com.ua/
//  2. Hover "Ноутбуки и компьютеры", click "Мониторы", wait for page to load
//  3. Find first monitor with price less then 3000UAH, click on image of this monitor, wait for page to load
//  4. Add monitor to comparison. Verify icon (1) appears in header close to comparison image (scales). Remember price, name
//  5. Click back button in browser
//  6. Find first monitor which price is less then first monitor. Click on image of found monitor. Wait for page to load
//  7. Add second monitor to comparison. Verify icon (2) appears in header close to comparison image (scales). Remember price, name
//  8. Click on comparison image in header. Click on "Мониторы (2)". Wait for page to load
//  9. Verify that in comparison you have just 2 monitors
//  10. Verify that names are correct (equal to names which you stored in step4 and step7)
//  11. Verify that prices are correct (equal to prices which you stored in step4 and step7)

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.testng.Assert.assertEquals;

public class MonitorComparisonTest extends BaseTest {
    String url = "https://rozetka.com.ua/";

    @BeforeMethod
    public void navigateToUrl() {
//  1. Navigate to https://rozetka.com.ua/
        driver.get(url);
    }

    @Test
    public void compareTest() {
//  2. Hover "Ноутбуки и компьютеры", click "Мониторы", wait for page to load
        String currentUrl = driver.getCurrentUrl();
//        driver.manage().window().maximize();

        WebElement laptops = driver.findElement(By.xpath("//a[@class='menu-categories__link' and @href='"+currentUrl+"computers-notebooks/c80253/']"));
        action.moveToElement(laptops).build().perform();

//Variable monitors has this locator to use linkText.
        String siteLangMonitor = null;
        if (currentUrl.equals("https://rozetka.com.ua/ua/"))
        {
            siteLangMonitor = "Монітори";
        } else
            {
                siteLangMonitor = "Мониторы";
            }

        By monitors = linkText(siteLangMonitor);
        wait.until(presenceOfElementLocated(monitors));
        wait.until(ExpectedConditions.elementToBeClickable(monitors));
        driver.findElement(monitors).click();
        waitForPageLoaded();

//  3. Find first monitor with price less then 3000UAH, click on image of this monitor, wait for page to load
        List<WebElement> prices = driver.findElements(By.cssSelector("span.goods-tile__price-value"));

        for (int counter = 1; counter < prices.size() + 1; counter++) {
            String price = driver.findElement(By.xpath("//ul[@class = 'catalog-grid']//li[" + counter + "]//span[@class = 'goods-tile__price-value']")).getText().replaceAll("[^\\d.]", "");
            int firstComparePrice = Integer.parseInt(price);
            if (firstComparePrice < 3000) {
                driver.findElement(By.xpath("//ul[@class = 'catalog-grid']//li[" + counter + "]//span[@class = 'goods-tile__title']")).click();
                break;
            }
        }
        waitForPageLoaded();
        Assert.assertTrue(elementIsNotPresent("//span [@class='header-actions__button-counter']"));

//  4. Add monitor to comparison. Verify icon (1) appears in header close to comparison image (scales). Remember price, name
        driver.findElement(By.className("compare-button")).click();

        String scalesWithCounter = "//li[3]//*[@class='header-actions__button-counter']";
        wait.until(presenceOfElementLocated(By.xpath(scalesWithCounter)));
        isElementPresent(scalesWithCounter);

        String compareCounter = "//span[@class='header-actions__button-counter']";
        wait.until(presenceOfElementLocated(By.xpath(compareCounter)));
        assertEquals(driver.findElement(By.xpath(compareCounter)).getText(), "1");

        String firstPrice = driver.findElement(By.cssSelector("p.product-prices__big.product-prices__big_color_red")).getText().replaceAll("[^\\d.]", "");
        int firstComparePrice = Integer.parseInt(firstPrice);
        String firstName = driver.findElement(By.cssSelector("h1.product__title")).getText();
        System.out.println(firstPrice);
        System.out.println(firstName);

//  5. Click back button in browser
        driver.navigate().back();
        waitForPageLoaded();

//  6. Find first monitor which price is less then first monitor. Click on image of found monitor. Wait for page to load
        List<WebElement> secondPrices = driver.findElements(By.cssSelector("span.goods-tile__price-value"));
        for (int secondCount = 1; secondCount < secondPrices.size() + 1; secondCount++) {
            String secondPrice = driver.findElement(By.xpath("//ul[@class = 'catalog-grid']//li[" + secondCount + "]//span[@class = 'goods-tile__price-value']")).getText().replaceAll("[^\\d.]", "");
            int secondComparePrice = Integer.parseInt(secondPrice);
            if (secondComparePrice < firstComparePrice) {
                WebElement element = driver.findElement(By.xpath("//ul[@class = 'catalog-grid']//li[" + secondCount + "]//span[@class = 'goods-tile__title']"));
                wait.until(ExpectedConditions.elementToBeClickable(element));
                scrollToElement(element);
                element.click();
                break;
            }
        }
        waitForPageLoaded();

//  7. Add second monitor to comparison. Verify icon (2) appears in header close to comparison image (scales). Remember price, name
        driver.findElement(By.cssSelector("button.compare-button")).click();
        waitForPageLoaded();

        wait.until(presenceOfElementLocated(By.xpath(scalesWithCounter)));
        isElementPresent(scalesWithCounter);
        assertEquals("2", driver.findElement(By.xpath("//span[@class='header-actions__button-counter']")).getText());

        String secondPrice = driver.findElement(By.cssSelector("p.product-prices__big.product-prices__big_color_red")).getText().replaceAll("[^\\d.]", "");
        String secondName = driver.findElement(By.cssSelector("h1.product__title")).getText();
        System.out.println(secondPrice);
        System.out.println(secondName);

//  8. Click on comparison image in header. Click on "Мониторы (2)". Wait for page to load
        WebElement toWait = driver.findElement(By.partialLinkText("се про товар"));
        action.moveToElement(toWait).build().perform();

        wait.until(presenceOfElementLocated(By.id("icon-header-compare")));
        driver.findElement(By.className("header-actions__button-icon")).click();

        String monitorsLinkCompare = "a.comparison-modal__link";
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(monitorsLinkCompare)));
        driver.findElement(By.cssSelector(monitorsLinkCompare)).click();
        waitForPageLoaded();

//  9. Verify that in comparison you have just 2 monitors
        By searchToWait = By.name("search");
        driver.findElement(searchToWait).click();

        int monitorsToCompareAmount = driver.findElements(By.cssSelector("a.product__heading")).size();
        assertEquals(2, monitorsToCompareAmount);

//  10. Verify that names are correct (equal to names which you stored in step4 and step7)
        List<WebElement> namesToCompare = driver.findElements(By.cssSelector("a.product__heading"));
        assertEquals(namesToCompare.get(0).getText(), firstName);
        assertEquals(namesToCompare.get(1).getText(), secondName);

        System.out.println(namesToCompare.get(0).getText() + " " + firstName);
        System.out.println(namesToCompare.get(1).getText() + " " + secondName);

//  11. Verify that prices are correct (equal to prices which you stored in step4 and step7)
        String firstOldPrice = driver.findElement(By.xpath("//ul[@class = 'products-grid']//li[1]//*[@class = 'product__prices']/div/div")).getText().replaceAll("\\s+", "");
        String secondOldPrice = driver.findElement(By.xpath("//ul[@class = 'products-grid']//li[2]//*[@class = 'product__prices']/div/div")).getText().replaceAll("\\s+", "");
        String firstAllPrices = driver.findElement(By.xpath("//ul[@class = 'products-grid']//li[1]//*[@class = 'product__prices']/div")).getText().replaceAll("\\s+", "");
        String secondAllPrices = driver.findElement(By.xpath("//ul[@class = 'products-grid']//li[2]//*[@class = 'product__prices']/div")).getText().replaceAll("\\s+", "");
        String firstPriceToCompare = firstAllPrices.replace(firstOldPrice, "").replaceAll("[^0-9]", "");
        String secondPriceToCompare = secondAllPrices.replace(secondOldPrice, "").replaceAll("[^0-9]", "");
        assertEquals(firstPrice, firstPriceToCompare);
        assertEquals(secondPrice, secondPriceToCompare);
        System.out.println(firstPrice + " " + firstPriceToCompare);
        System.out.println(secondPrice + " " + secondPriceToCompare);
    }
}