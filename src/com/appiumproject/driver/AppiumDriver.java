package com.appiumproject.driver;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.appiumproject.util.PropertiesReader;
import com.appiumproject.util.ReadExcel;


public class AppiumDriver {
  //Set constants
  private static final String TARGETURL = "http://google.com";
  private static final String WEBSITEURL = "http://cnn.com";
  private static final String FILEPATH = "F:\\Workspace\\Eclipse Plugins\\platform-tools";
  private static final String ECOMMERCEURL = "http://amazon.com";
  public  static final String TESTDATAFILE =  "Sheet1";
  public  static final String TESTDATAPATH =  "resources/ChromeTest.xlsx";
  private ChromeDriver driver;
  private PropertiesReader propReader;  
  
  @BeforeClass
  public void setUp() throws Exception {
	  //set the datasheet
	  ReadExcel.setExcelFile(TESTDATAPATH, TESTDATAFILE);
	  //Set required capabilities and instantiate driver
	  ChromeOptions chromeOptions = new ChromeOptions();
	  chromeOptions.setExperimentalOption("androidPackage", ReadExcel.getCellData(1, 1));
	  chromeOptions.setExperimentalOption("androidDeviceSerial", ReadExcel.getCellData(1, 0));
	  System.setProperty("webdriver.chrome.driver", "F:\\Workspace\\Eclipse Plugins\\chromedriver.exe");  
	  driver = new ChromeDriver(chromeOptions);
	  
	  //use Runtime class to launch the adb service
	  String command = "cmd /c start cmd.exe /K \"cd "+ FILEPATH +" && adb devices && exit\"";
	  Runtime.getRuntime().exec(command);
	  Thread.sleep(10000);  //10 sec pause to allow adb to connect	  
	  
	  //Create Object of Properties Class.
	  propReader = new PropertiesReader("resources/objectrepo.properties");
	  
	  //config log4j to avoid warnings
	  PropertyConfigurator.configure(System.getProperty("user.dir") + "/resources/log4j.properties");
  }
  
  @Test
  public void StartChromeTest() throws Exception{
	  //navigate to google.com and search for string
	  driver.get(TARGETURL);
	  driver.findElement(propReader.getLocator("googleSearchField")).sendKeys("Bloodborne witches");
	  driver.findElement(propReader.getLocator("googleSearchSubmit")).click();
	  
	  //click first link in search
	  driver.findElement(propReader.getLocator("wikiLink")).click();
	  
	  //check to make sure we are on the correct page (bloodborne wiki)
	  Assert.assertTrue(driver.getTitle().equals("Witch of Hemwick | Bloodborne Wiki"));
	  
	  //navigate to google.com
	  driver.get(WEBSITEURL);

	  //verify that we are on the correct webpage by title
	  Assert.assertTrue(driver.getTitle().equals("CNN - Breaking News, Latest News and Videos"));

	  //navigate to next website (ecommerce) and validate we are on the correct page
	  driver.get(ECOMMERCEURL);
	  Assert.assertTrue(driver.getTitle().equals("Amazon.com"));

	  //search for item
	  driver.findElement(propReader.getLocator("searchField")).sendKeys(ReadExcel.getCellData(1, 2));
	  driver.findElement(propReader.getLocator("searchSubmit"));

	  // verify product page exists
  }

  @AfterClass
  public void tesrDown() throws Exception {
  	  //quit
  	  driver.quit();
  }
}
