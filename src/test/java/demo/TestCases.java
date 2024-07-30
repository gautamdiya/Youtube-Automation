package demo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.List;
import com.google.common.base.Equivalence.Wrapper;
import java.time.Duration;
import java.util.logging.Level;
import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider{ // Lets us read the data
        ChromeDriver driver;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */

        

        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
        }
        @Test(priority = 1,enabled = true, description = "URL assertion and about page message printing")
        public void testCase01() throws InterruptedException{
                System.out.println("Started testCase01");
                String URL = "https://www.youtube.com/";
                Assert.assertTrue(Wrappers.navigateToUrl(driver, URL),"Navigation to the URL failed.");
                Wrappers.clickElement(driver, By.xpath("//a[contains(text(),'About')]"));
                Wrappers.printMessage(driver);
                System.out.println("Ended testCase01");
        }

        @Test(priority = 2,enabled = true, description = "Assertion for the movie is marked “A” for Mature or not and whether the movie is either “Comedy” or “Animation")
        public void testCase02() throws InterruptedException{
                System.out.println("Started testCase02");
                String URL = "https://www.youtube.com/";
                Assert.assertTrue(Wrappers.navigateToUrl(driver, URL),"Navigation to the URL failed.");
                Wrappers.clickOnTab(driver,"Movies");
                Wrappers.clickOnNextButton(driver, "Top selling",3);
                Wrappers.maturityLastOfMovie(driver);
                Wrappers.genreOfLastMovie(driver);  
                System.out.println("Ended testCase02");
        }
        @Test(priority = 3, enabled = true,description = "URL assertion and about page message printing")
        public void testCase03() throws InterruptedException{
                System.out.println("Started testCase03");
                String URL = "https://www.youtube.com/";
                Assert.assertTrue(Wrappers.navigateToUrl(driver, URL),"Navigation to the URL failed.");
                Wrappers.clickOnTab(driver,"Music");
                Wrappers.jumpToTheFirstSection(driver);
                Wrappers.clickOnNextButton(driver, "Biggest Hits",3);
                Wrappers.nameOfLastPlayList(driver,"Biggest Hits");
                Wrappers.noOfTracks(driver,"Biggest Hits","Bollywood Dance");
                System.out.println("Ended testCase03");
        }

        @Test(priority = 4, enabled = true, description = "URL assertion and about page message printing")
        public void testCase04() throws InterruptedException{
                System.out.println("Started testCase04");
                String URL = "https://www.youtube.com/";
                Assert.assertTrue(Wrappers.navigateToUrl(driver, URL),"Navigation to the URL failed.");
                Wrappers.clickOnTab(driver, "News");
                Wrappers.titleOfNews(driver);
                Wrappers.sumOfTheLikes(driver);
                System.out.println("Ended testCase04");
        }

       @Test(dataProvider = "excelData",dataProviderClass = ExcelDataProvider.class,enabled = true,priority = 5)
        public void testCase05(String to_be_searched) throws InterruptedException {
                driver.get("https://www.youtube.com");
                Thread.sleep(1000);
                WebElement searchBox = driver.findElement(By.xpath("//input[@placeholder='Search']"));
                // searchBox.sendKeys(searchName);
                Wrappers.click(searchBox);
                Wrappers.sendKeys(searchBox, to_be_searched);

                WebElement search = driver.findElement(By.id("search-icon-legacy"));
                Wrappers.click(search);
                Thread.sleep(5000);
        
                long totalViews = 0;
                JavascriptExecutor js = (JavascriptExecutor) driver;
                while (totalViews < 1000000000) { // 10 Crore views
                    List<WebElement> videoElements = driver.findElements(By.xpath("//span[contains(@class,'inline-metadata') and contains(text(),'views')]"));
        
                    for (WebElement videoElement : videoElements) {
                        String viewsText = videoElement.getText();
                        if (viewsText.contains("views")) {
                            viewsText = viewsText.split(" ")[0]; // Get the number part
                            totalViews += parseViews(viewsText);
                        }
        
                        if (totalViews >= 1000000000) {
                            break;
                            
                        }
                    }
        
                    js.executeScript("window.scrollBy(0, 1000);");
                    Thread.sleep(2000); // Wait for new videos to load
                }
        
                System.out.println("Total views for " + to_be_searched + ": " + totalViews);
            }
        
            private long parseViews(String viewsText) {
                long views = 0;
                if (viewsText.endsWith("K")) {
                    views = (long) (Double.parseDouble(viewsText.replace("K", "")) * 1_000);
                } else if (viewsText.endsWith("M")) {
                    views = (long) (Double.parseDouble(viewsText.replace("M", "")) * 1_000_000);
                } else if (viewsText.endsWith("B")) {
                    views = (long) (Double.parseDouble(viewsText.replace("B", "")) * 1_000_000_000);
                } else {
                    views = Long.parseLong(viewsText.replace(",", ""));
                }
                return views;
            }

              @DataProvider(name = "excelData")
    public static Object[][] provideData() {
        // Example data, replace with your actual data retrieval logic
        return new Object[][] {
            {"Movies"},
            {"Music"},
            {"Games"},
            {"India"},
            {"UK"}
            // Add more data as needed
        };
    }




        // @AfterTest
        // public void endTest() {
        //         driver.close();
        //         driver.quit();

        // }
}