package demo.wrappers;

// import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import java.util.List;
import dev.failsafe.internal.util.Assert;
import dev.failsafe.internal.util.Durations;

import java.time.Duration;

public class Wrappers {

    public static boolean navigateToUrl(ChromeDriver driver, String uRL) {
        boolean status= false;
       try {
        driver.get(uRL);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
        );
       String pageUrl= driver.getCurrentUrl();
       if(pageUrl.equals(pageUrl)){
        status=true;
        System.out.println("Navigated to the page Successfully");
       }else{
        System.out.println("Navigation failed");
        status=false;
       }
       } catch (Exception e) {
       System.out.println(e.getMessage());
       }
       return status;
    }
    /*
     * Write your selenium wrappers here
     */

    public static void clickElement(ChromeDriver driver, By xpath) throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(xpath));
        wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
        } catch (Exception e) {
           e.getMessage();
        }
        Thread.sleep(1000);
    }

    public static void printMessage(ChromeDriver driver) {
       try {
        WebElement aboutHeadline =driver.findElement(By.xpath("//h1[contains(text(),'About')]"));
        System.out.println(aboutHeadline.getText());
        WebElement printMessageFirst =driver.findElement(By.xpath("(//p[contains(@class,'text-primary')])[1]"));
        System.out.println(printMessageFirst.getText());
        WebElement printMessageSecond =driver.findElement(By.xpath("(//p[contains(@class,'text-primary')])[2]"));
        System.out.println(printMessageSecond.getText());
       } catch (Exception e) {
        System.out.println(e.getMessage());
       }
    }

    public static void maturityLastOfMovie(ChromeDriver driver) {
      try {
        Thread.sleep(1000);
          List<WebElement> matureElements = driver.findElements(By.xpath("//*[@class='badges style-scope ytd-grid-movie-renderer']/div[2]/p"));

          if (!matureElements.isEmpty()) {
              WebElement lastElement = matureElements.get(matureElements.size() - 2);

              String matureElementText = lastElement.getText().replaceAll("[^A-Za-z/\\d+]", "").trim();
                 System.out.println(matureElementText);
              if (matureElementText.equals("A")) {
                  System.out.println("This is an Adult movie. Please watch it if you are older than 18 years.");
              } else if (matureElementText.equals("U/A")) {
                  System.out.println("Please watch this movie under Parental Guidance.");
              } else {
                  System.out.println("This is a Universal movie. Anybody can watch this movie.");
              }

              // Soft assert 
              SoftAssert softAssert = new SoftAssert();
              softAssert.assertEquals(matureElementText, "A", "The movie is not marked 'A' for Mature");

              softAssert.assertAll();
          } else {
              System.out.println("No mature elements found.");
          }
      } catch (Exception e) {
          System.out.println("Exception occurred: " + e.getMessage());
          // Log or handle the exception as needed
      }
  }

  public static void genreOfLastMovie(ChromeDriver driver) {
    try {
      List<WebElement> movieGenreElement = driver.findElements(By.xpath("//*[@class='yt-simple-endpoint style-scope ytd-grid-movie-renderer']/span"));
      WebElement lastElement = movieGenreElement.get(movieGenreElement.size()-1);
      String[] movieGenre = lastElement.getText().trim().split(" • ");
      String genreText = movieGenre[0];
      System.out.println(genreText);
      SoftAssert softAssert = new SoftAssert();
      softAssert.assertTrue(genreText.startsWith("Comedy")|| genreText.startsWith("Animation"), "Expected genre to be Comedy or Animation but found " +genreText);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }


    public static void clickOnTab(ChromeDriver driver, String movieName) throws InterruptedException {
      try {
        Thread.sleep(1000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tabElement= driver.findElement(By.xpath("//a[@title='"+movieName+"']"));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)", tabElement);
        wait.until(ExpectedConditions.visibilityOf(tabElement)).click();
        System.out.println(movieName+" is Clicked successfully");
      } catch (Exception e) {
        System.out.println(movieName+" Element is not clicked");
       System.out.println(e.getMessage());
      }
      Thread.sleep(1000);
    }

    public static void jumpToTheFirstSection(ChromeDriver driver) {
       try {
        List<WebElement> sectionElements = driver.findElements(By.xpath("//*[@id='dismissible' and @class='style-scope ytd-shelf-renderer' ]"));
        WebElement firstSection = sectionElements.get(0);
       ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)", firstSection);
         Thread.sleep(1000);  
       } catch (Exception e) { 
       System.out.println(e.getMessage());
       }
    }

    public static void clickOnNextButton(ChromeDriver driver, String sectionName, int times) {
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
       WebElement sectionElement = driver.findElement(By.xpath("//span[@id='title' and contains(text(),'"+sectionName+"')]"));
       ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)", sectionElement);
       WebElement nextButtonElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='title' and contains(text(),'"+sectionName+"')]//ancestor::div[@id='dismissible']//child::button[@aria-label='Next']")));
       try {
        for(int i=0;i<times;i++){
        if(nextButtonElement.isDisplayed() && nextButtonElement.isEnabled()){
            nextButtonElement.click();
            System.out.println("Next click is Done");
        }
      }
       } catch (Exception e) {
        System.out.println(e.getMessage());
       }

    
    }

    public static void nameOfLastPlayList(ChromeDriver driver,String sectionName) throws InterruptedException {
         List<WebElement> playListNames = driver.findElements(By.xpath("//a[contains(@title,'"+sectionName+"')]//ancestor::div[@id='dismissible']//child::h3"));
         WebElement lastPlayListElement = playListNames.get(playListNames.size()-1);
           String playListHeadLineText= lastPlayListElement.getText().trim();
           System.out.println(playListHeadLineText);
           Thread.sleep(1000);
    }


    public static void noOfTracks(ChromeDriver driver,String sectionName, String playListName) throws InterruptedException {
      try {
        WebElement tracksElement = driver.findElement(By.xpath("//a[contains(@title,'"+sectionName+"')]//ancestor::div[@id='dismissible']//h3[contains(text(),'"+playListName+"')]/../p"));
        String tracks = tracksElement.getText();
        String[] trackArray = tracks.split(" ");
        System.out.println(trackArray[0]);
        int trackCount = Integer.parseInt(trackArray[0]);
        SoftAssert softAssert  = new SoftAssert();
        softAssert.assertEquals(trackCount<=50, "The number of tracks listed is greater than 50: " + trackCount);
        
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      Thread.sleep(1000);
    }

    public static void titleOfNews(ChromeDriver driver) throws InterruptedException {
        Thread.sleep(1000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement latestNewsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Latest news posts')]")));
       ( (JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)", latestNewsElement);
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List< WebElement> titleElements = driver.findElements(By.xpath("//*[@class='style-scope ytd-post-renderer' and @id='header']/div[@id='author']/a/span"));
        List <WebElement> bodyElements= driver.findElements(By.xpath("//*[@class='style-scope ytd-post-renderer' and @id='body']//*[@id='home-content-text']"));
        for(int i=0;i<=3 && i < titleElements.size() && i<bodyElements.size();i++){
              System.out.println(titleElements.get(i).getText().trim());
              System.out.println(bodyElements.get(i).getText().trim());
        }   
        Thread.sleep(1000);
    }

    public static void sumOfTheLikes(ChromeDriver driver) throws NumberFormatException {
      int sum = 0;
      List<WebElement> likesElements = driver.findElements(By.xpath("//span[@id='vote-count-middle']"));
      for (int i = 0; i < 3 && i < likesElements.size(); i++) {
          String likes = likesElements.get(i).getText().trim();
          try {
              if (!likes.isEmpty()) {
                  sum += Wrappers.convertToNumericValue(likes);
              } else {
                  System.out.println("Empty likes count encountered at index " + i);
              }
          } catch (IllegalArgumentException e) {
              System.out.println("Error parsing likes text: " + likes + " - " + e.getMessage());
          }
      }
      System.out.println("Total likes sum: " + sum);
  }

    public static long convertToNumericValue(String value) {
      // Trim the string to remove any leading or trailing spaces
      value = value.trim().toUpperCase();
  
      // Check if the string is empty or too short to perform operations
      if (value.isEmpty()) {
          throw new IllegalArgumentException("Invalid format: " + value);
      }
  
      // Check if the last character is non-numeric and determine the multiplier
      char lastChar = value.charAt(value.length() - 1);
      int multiplier = 1;
      switch (lastChar) {
          case 'K':
              multiplier = 1000;
              break;
          case 'M':
              multiplier = 1000000;
              break;
          case 'B':
              multiplier = 1000000000;
              break;
          default:
              // If the last character is numeric, parse the entire string
              if (Character.isDigit(lastChar)) {
                  return Long.parseLong(value);
              }
              throw new IllegalArgumentException("Invalid format: " + value);
      }
  
      // Extract the numeric part before the last character
      String numericPart = value.substring(0, value.length() - 1);
      if (numericPart.isEmpty()) {
          throw new IllegalArgumentException("Invalid format: " + value);
      }
      double number = Double.parseDouble(numericPart);
  
      // Calculate the final value
      return (long) (number * multiplier);
  }

    public static void click(WebElement searchBox) {
      
      try {
        searchBox.click();
      } catch (Exception e) {
       System.out.println(e.getMessage());
      }
    }

    public static void sendKeys(WebElement searchBox, String to_be_searched) {
       try {
        searchBox.clear();
        searchBox.sendKeys(to_be_searched);
       } catch (Exception e) {
        System.out.println(e.getMessage());
       }
    }
    
    

}
