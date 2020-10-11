import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
//  Create 1 automation test for testing of search result listing in google by search text "iphone kyiv buy".
//  Link to "stylus.ua" should be present on any of first 5 pages of results.
//  After test execution you have to log in console which page contained searched link ("STYLUS.UA found on 2 page") or print "STYLUS.UA not found on first 5 pages"

public class SearchTest extends BaseTest {

    String searchUrl = "https://google.com/ncr";
    String searchedSite = "stylus.ua";

    @BeforeMethod
    public void navigateToUrl() {
        driver.get(searchUrl);
    }

    @Test
    public void stylusSearchTest() {
        driver.findElement(By.name("q")).sendKeys("iphone kyiv buy" + Keys.ENTER);

        List<WebElement> tempSearchResults = new ArrayList<WebElement>();
        for (int i = 1; i <= 5; i++) {
            tempSearchResults = driver.findElements(By.tagName("cite"));
            for (int j = 0; j < tempSearchResults.size(); j++) {
                if (tempSearchResults.get(j).getText().contains(searchedSite) == true) {
                    System.out.printf("%S found on %d page%n", searchedSite, i);
                    return;
                }
            }
            WebElement nextBtn = wait.until(presenceOfElementLocated(By.xpath("//span[@style='display:block;margin-left:53px']")));
            nextBtn.click();
        }
        System.out.printf("%S not found on first 5 pages%n", searchedSite);
        assertEquals(tempSearchResults.get(0).getText(), searchedSite);
    }
}
