//  Write 2 tests (add usage of all 8 types of selectors):
//2) Verification of 3 filters (manufacturer, price, your own choice)
//        1. Navigate to https://rozetka.com.ua/
//        2. Search by "samsung"
//        3. Click "Мобильные телефоны" in the product filters panel
//        4. Add to filters "Apple" and "Honor"
//        5. Verify all filtered products are products made by Samsung, Apple or Honor
//
//        1. Navigate to https://rozetka.com.ua/
//        2. Search by "samsung"
//        3. Click "Мобильные телефоны" in the product filters panel
//        4. Add to price filter: 5000<price<15000
//        5. Verify all filtered products are products with price from range
//
//        1. Navigate to https://rozetka.com.ua/
//        2. Search by "samsung"
//        3. Click "Мобильные телефоны" in the product filters panel
//        4. Add filter value (your choice)
//        5. Verify all filtered products are products according to filter

import org.openqa.selenium.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.testng.Assert.fail;

public class FiltersTest extends BaseTest {
    String url = "https://rozetka.com.ua/";
    String searchText = "samsung";

    @BeforeMethod
    public void navigateToUrl() {
//  1. Navigate to https://rozetka.com.ua/
        driver.get(url);
//  2. Search by "samsung"
        driver.findElement(By.name("search")).sendKeys(searchText + Keys.ENTER);
        waitForPageLoaded();
//  3. Click "Мобильные телефоны" in the product filters panel
//        wait.until(presenceOfElementLocated(By.xpath("//a[@class = 'categories-filter__link'][@href = 'https://rozetka.com.ua/ua/mobile-phones/c80003/producer=samsung/']")));
//        driver.findElement(By.xpath("//a[@href = 'https://rozetka.com.ua/ua/mobile-phones/c80003/producer=samsung/'][@class = 'categories-filter__link']")).click();
//        waitForPageLoaded();

        List<WebElement> link = driver.findElements(By.cssSelector("a.categories-filter__link"));
        String mobilePhonesLink = "";
        for (int i = 1; i < link.size(); i++) {
            if (link.get(i).getAttribute("href").contains("mobile-phones")) {
                mobilePhonesLink = link.get(i).getAttribute("href");
                break;
            }
        }
        wait.until(presenceOfElementLocated(By.xpath("//a[@href = '" + mobilePhonesLink + "'][@class = 'categories-filter__link']")));
        driver.findElement(By.xpath("//a[@href = '" + mobilePhonesLink + "'][@class = 'categories-filter__link']")).click();
        waitForPageLoaded();
    }

    @Test
    public void filterBrandTest() {
//        4. Add to filters "Apple" and "Honor"
        driver.findElement(By.cssSelector("label[for=Apple]")).click();

        WebElement toWait = driver.findElement(By.tagName("select"));
        action.moveToElement(toWait).build().perform();

        driver.findElement(By.cssSelector("label[for=Honor]")).click();
        action.moveToElement(toWait).build().perform();
//        5. Verify all filtered products are products made by Samsung, Apple or Honor
        List<WebElement> searchResults1 = driver.findElements(By.cssSelector("span.goods-tile__title"));
        for (WebElement searchResult : searchResults1) {
//System.out.println(searchResults.get(i).getText());
            if (!searchResult.getText().contains("Samsung")) {
                if (!searchResult.getText().contains("Apple")) {
                    if (!searchResult.getText().contains("Honor")) {
                        fail("There is no Samsung, Apple or Honor in the name of item.");
                    }
                }
            }
        }
    }

    @Test
    public void filterPriceTest() {
//        4. Add to price filter: 5000<price<15000
        String minInput = "input[formcontrolname=min]";
        String maxInput = "input[formcontrolname=max]";
        String submitButton = "button[type=submit]";
        int minValue = 5000;
        int maxValue = 15000;

        isCSSElementPresent(minInput);
        isCSSElementPresent(maxInput);
        isCSSElementPresent(submitButton);

        driver.findElement(By.cssSelector(minInput)).clear();
        driver.findElement(By.cssSelector(maxInput)).clear();

        driver.findElement(By.cssSelector(minInput)).sendKeys(String.valueOf(minValue));
        driver.findElement(By.cssSelector(maxInput)).sendKeys(String.valueOf(maxValue));
        driver.findElement(By.cssSelector(submitButton)).click();
        waitForPageLoaded();
//        5. Verify all filtered products are products with price from range
        List<WebElement> prices = driver.findElements(By.cssSelector("span.goods-tile__price-value"));
        for (int secondCount = 1; secondCount < prices.size() + 1; secondCount++) {
            String strPrice = driver.findElement(By.xpath("//ul[@class = 'catalog-grid']//li[" + secondCount + "]//span[@class = 'goods-tile__price-value']")).getText().replaceAll("[^\\d.]", "");
            int intPrice = Integer.parseInt(strPrice);
            if (intPrice < minValue) {
                fail("Wrong price value.");
            }
            if (intPrice > maxValue) {
                fail("Wrong price value.");
            }
        }
    }

    @Test
    public void filterMemoryTest() {
//        4. Add filter value (your choice)
        WebElement checkboxGB = driver.findElement(By.cssSelector("label[for=\"16 ГБ\"]"));
        scrollToElement(checkboxGB);
        driver.findElement(By.cssSelector("label[for=\"16 ГБ\"]")).click();
        waitForPageLoaded();
//        5. Verify all filtered products are products according to filter
        List<WebElement> searchResults2 = driver.findElements(By.cssSelector("span.goods-tile__title"));
        for (WebElement searchResult : searchResults2) {
            if (!searchResult.getText().contains("16/")) {
                fail("RAM size filter error.");
            }
        }
    }
}
