package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class BaseClass extends CheckPoint {

	public static String currentWorkingDir;
	public static WebDriver driver;

	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;

	public static String ExecutionBrowser;
	public static WebDriverWait explicitWait;
	private static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();
	
	// TestNG Before and After annotation methods (ends in Line no: 176)
	
	/**
	 * Create the Extent report objects
	 */
	@BeforeSuite
	public void setup() {
		try {
			System.out.println("Executing : Before Suite");
			currentWorkingDir = System.getProperty("user.dir");
			htmlReporter = new ExtentHtmlReporter(currentWorkingDir + "\\ExtentReports\\Report.html");
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Flush the all Test logs/status into Report
	 */
	@AfterSuite
	public void aftersuite() {
		extent.flush();
		System.out.println("Executing : After Suite");
	}

	/**
	 * Before method launch desired browser specified in testng.xml
	 * @param browser
	 * @param Application_URL
	 * @throws InterruptedException
	 */
	@Parameters({ "browser", "Application_URL" })
	@BeforeMethod
	public void OpenApplication(String browser, String Application_URL) throws InterruptedException {
		try {
			System.out.println("Executing : Before method");
			currentWorkingDir = System.getProperty("user.dir");
			ExecutionBrowser = browser;
			switch (browser) {
			case "Chrome":
				String ChromePath = currentWorkingDir + "\\webDrivers\\chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", ChromePath);
				driver = new ChromeDriver();
				Thread.sleep(2000);
				driver.manage().window().maximize();
				Thread.sleep(2000);
				driver.get(Application_URL);
				break;
			case "FireFox":
				String geeckoPath = currentWorkingDir + "\\webDrivers\\geckodriver.exe";
				System.setProperty("webdriver.gecko.driver", geeckoPath);
				driver = new FirefoxDriver();
				Thread.sleep(2000);
				// driver.manage().window().maximize();
				Thread.sleep(2000);
				driver.get(Application_URL);
				break;
			case "IE":
				String IEpath = currentWorkingDir + "\\webDrivers\\IEDriverServer.exe";
				System.setProperty("webdriver.ie.driver", IEpath);
				driver = new InternetExplorerDriver();
				Thread.sleep(2000);
				driver.manage().window().maximize();
				Thread.sleep(2000);
				driver.get(Application_URL);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Take The test result and reports in Extent report
	 * @param result
	 */
	@AfterMethod
	public void getTestresultStatus(ITestResult result) {
		System.out.println("Executing : After Method");
		System.out.println("*******" + result.getName() + "  test case execution completed *********** ");
		try {
			if (result.getStatus() == ITestResult.SUCCESS) {
				System.out.println("**********" + result.getName() + " : Passed ***********");
				test.log(Status.PASS,
						MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
			} else if (result.getStatus() == ITestResult.FAILURE) {
				System.out.println("**********" + result.getName() + " : Failed ***********");
				test.log(Status.FAIL, MarkupHelper
						.createLabel(result.getName() + " Test case FAILED due to below issues:", ExtentColor.RED));
				try {
					test.fail(result.getThrowable());
					test.fail("Snapshot below: "
							+ test.addScreenCaptureFromPath(captureScreenshot(driver, result.getName())));
				} catch (IOException e) {
					e.printStackTrace();
				}
				// test.fail(result.getThrowable());
			} else if (result.getStatus() == ITestResult.SKIP) {
				System.out.println("**********" + result.getName() + " : Skip ***********");
				test.log(Status.SKIP,
						MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.YELLOW));
				test.skip(result.getThrowable());
			}
		} catch (Exception e) {
			System.out.println("Get Results- Catch Block");
		}
		try {
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			System.out.println("In quit catch block");
		}
	}
	//   *********** TestNG Before and After annotation methods (started from Line no : 58) *********************
   //    *********** Extent Report methods and screenShot methods (ends at: 240) ***************************
	
	/**
	 * Create the test in Extent Report 
	 * @param value- testName
	 * its take ExecutionBrowser and append to testcase in Extent Report
	 * @return 
	 */
	public ExtentTest createTest(String value) {
		return test = extent.createTest(value +" : " +ExecutionBrowser);
	}
	
	/**
	 * log the info statement in extent report -> in blue color
	 * @param info statement
	 */
	public void reportInfo(String info) {
		test.info("<B> <span style='color:blue;'>" + info + "</span>");
	}

	/**
	 * log the log info statement in extent report -> in Pass Label
	 * @param info statement
	 */
	public void pass(String info) {
		test.log(Status.PASS, MarkupHelper.createLabel(info, ExtentColor.GREEN));
	}
	
	/**
	 * @param driver
	 * @param screenShotName
	 * @return - the screenshot path
	 * @throws IOException
	 */
	public static String captureScreenshot(WebDriver driver, String screenShotName) throws IOException {
		String dest = null;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			dest = System.getProperty("user.dir") + screenShotName + System.currentTimeMillis() + ".png";
			File destination = new File(dest);
			FileUtils.copyFile(source, destination);
			return dest;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dest;
	}

	/**
	 * Taking Screenshot using yandex Ashot jar - it will capture the entire page screenshot
	 * @param driver
	 * @param screenShotName
	 * @return - the screenshot path
	 * @throws IOException
	 */
	public String aShot(WebDriver driver, String screenShotName) throws IOException {
		String destintionPath = System.getProperty("user.dir") + screenShotName + System.currentTimeMillis() + ".png";
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(driver);
		ImageIO.write(screenshot.getImage(), "PNG", new File(destintionPath));
		return destintionPath;
	}
	//    *********** Extent Report methods and screenShot methods (Starts from : 178)***************************
	/**
	 * Properties file Reading
	 * @param path - create inside testData and just povide properties file
	 * @return Properties object
	 */
	public Properties loadPropertiesFile(String path) {
		String uploadFilepath = currentWorkingDir + "\\TestData" + "\\" + path;
		File file = new File(uploadFilepath);
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();

		// load properties file
		try {
			prop.load(fileInput);
			test.info("Loaded properties file :  " + uploadFilepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	// *************** Selenium wrapper methods ************************************

	/**
	 * @param WebElement
	 * @return scraped text
	 */
	public String getText(WebElement element) {
		String value = element.getText();
		test.info("getText of element :  " + value);
		return value;
	}

	/**
	 * @param Webelement
	 * Log the info in report using WebElement getText
	 * Based on your application we need to log the Button names by : getText (or) Value Attibute
	 */
	public void click(WebElement element) {
		//String buttonName = element.getAttribute("value");
		String buttonName = element.getText();
		test.info("Clicked on button :  " + buttonName);
		element.click();
	}

	/**
	 * @param Webelement
	 * @param buttonNaame
	 */
	public void scrollToViewClick(WebElement element, String buttonNaame) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView()", element);
		js.executeScript("arguments[0].click();", element);
		test.info("Clicked on button :"+ buttonNaame);
	}

	/**
	 * @param Webelement
	 * @param buttonNaame
	 */
	public void clickJS(WebElement element,String buttonNaame) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
		test.info("Clicked on button :"+ buttonNaame);
	}

	/**
	 * @param Webelement
	 * @param sendkeys value
	 */
	public void sendKeys(WebElement element, String arg) {
		element.sendKeys(arg);
		test.info("sent keys to Input Box : " + arg);
	}

	/**
	 * By this we will bypass the FileUpload dilog box.
	 * It will work only element tag is <Input>
	 * @param Webelement
	 * @param filepath
	 */
	public void upload(WebElement element, String arg) {
		
		//added
		test.info("upload File location :  " + arg);
		element.sendKeys(arg);
	}

	/**
	 * select option by value
	 * @param selectBoxWebElement
	 * @param value
	 */
	public void selectByValue(WebElement selectWebElement, String value) {
		Select dropdown = new Select(selectWebElement);
		dropdown.selectByValue(value);
		test.info("selected option by value :  " + value);
	}

	/**
	 * select option by Index
	 * @param selectWebElement
	 * @param index
	 */
	public void selectByIndex(WebElement selectWebElement, int index) {
		Select dropdown = new Select(selectWebElement);
		dropdown.selectByIndex(index);
		test.info("selected by Index :  " + index);
	}

	/**
	 * select option by visible text
	 * @param selectWebElement
	 * @param visibleText
	 */
	public void selectByVisibleText(WebElement selectWebElement, String visibleText) {
		Select dropdown = new Select(selectWebElement);
		dropdown.selectByVisibleText(visibleText);
		test.info("selected by visible Text :  " + visibleText);
	}

	/**
	 * switch the frame by using its name or ID.
	 * @param frameid
	 */
	public void switchFrameByID(String frameid) {
		driver.switchTo().frame(frameid);
		test.info("switched the Frame by ID/name: " + frameid);
	}

	/**
	 * switch the frame by using its index(zero-based index)
	 * @param frameIndex
	 */
	public void switchFrameByIndex(int frameIndex) {
		driver.switchTo().frame(frameIndex);
		test.info("switched the Frame by ID/name: " + frameIndex);
	}

	/**
	 * switch the frame by using WebElement.
	 * @param webElement
	 */
	public void switchFrameByWebElement(WebElement element) {
		driver.switchTo().frame(element);
		test.info("switched the Frame by web eleemt");
	}

	// frame switching methods using explicit waits
	/**
	 * @param frameId_Name -> ID/Name
	 */
	public void frameToBeAvailableAndSwitchToItByID_Name(String frameId_Name) {
		explicitWait = new WebDriverWait(driver, 20);
		explicitWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameId_Name));
		test.info("switched the Frame by ID/Name :  " + frameId_Name);
	}

	/**
	 * @param frameIndex -> based index(int zero based index)
	 */
	public void frameToBeAvailableAndSwitchToItByIndex(int frameIndex) {
		explicitWait = new WebDriverWait(driver, 20);
		explicitWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
		test.info("switched the Frame by Index :  " + frameIndex);
	}

	/**
	 * @param frameWebElement -> Argument type is web Element object
	 */
	public void frameToBeAvailableAndSwitchToItByWebElement(WebElement frameWebElement) {
		explicitWait = new WebDriverWait(driver, 20);
		explicitWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameWebElement));
		test.info("switched the Frame by WebElement :  " + frameWebElement);
	}

	/**
	 * @param locator   -> Argument type is By object
	 */
	public void frameToBeAvailableAndSwitchToItByLocator(By locator) {
		explicitWait = new WebDriverWait(driver, 20);
		explicitWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}

	// ************** Wait methods  ***************
	/**
	 * An expectation for checking that an element, known to be present on the DOM of a page, is visible. 
	 * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.(Selenium java Doc)
	 * 
	 * Wait for up to element is present in the DOM and visible on page -- I am using this 
	 * method for non clickable type webElements
	 * @param webelement
	 * @return webelement if its visible
	 */
	public WebElement waitForElement(WebElement element) {
		explicitWait = new WebDriverWait(driver, 15);
		WebElement obj = explicitWait.until(ExpectedConditions.visibilityOf(element));
		return obj;
	}

	/**
	 * Overridden method to above method but here using by object
	 * @param locator -> By object
	 * @return webelelement
	 */
	public WebElement waitForElement(By locator) {
		explicitWait = new WebDriverWait(driver, 15);
		WebElement element = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return element;
	}

	
	/**
	 * Wait until element is clickable--We can use it for clickable elements like
	 * buttons,links,check boxes, radio buttons,etc
	 * @param WebElement
	 * @return
	 */
	public WebElement elementToBeClickable(WebElement element) {
		explicitWait = new WebDriverWait(driver, 15);
		WebElement ele = explicitWait.until(ExpectedConditions.elementToBeClickable(element));
		return ele;
	}

	/**
	 * Overridden method to above method but here using by object
	 * @param locator
	 * @return
	 */
	public WebElement elementToBeClickableBy(By locator) {
		explicitWait = new WebDriverWait(driver, 15);
		WebElement ele = explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
		return ele;
	}

	
	//worrk on this 
	public void isElementDisplyed(WebElement element) {
		explicitWait = new WebDriverWait(driver, 15);
		WebElement obj = explicitWait.until(ExpectedConditions.visibilityOf(element));
		if (obj.isDisplayed()) {
			String objtext = obj.getText();
			test.log(Status.PASS,
					MarkupHelper.createLabel("Element is displayed on the page: " + objtext, ExtentColor.GREEN));
		} else {
			// if fails its update in testNG report and Extent report
			Assert.assertTrue(obj.isDisplayed(), "Element is NOT displayed on the page ");
		}

	}


	
	//work on this 
	public void verifyText(String actualText, String expectedText) {
		try {
			if (actualText.equals(expectedText)) {
				test.log(Status.PASS, MarkupHelper.createLabel("Expected and Actual texts are equal :" + actualText,
						ExtentColor.GREEN));
			} else {
				Assert.assertEquals(actualText, expectedText);
			}
		} catch (Exception e) {
			test.log(Status.FAIL,
					MarkupHelper.createLabel("Expected and Actual text are not equal: " + actualText, ExtentColor.RED));
			e.printStackTrace();
		}
	}
	/**
	 * Switchs to newly open window
	 * Works only for 2 windows exits.
	 */
	public void switchToNewRegWindow() {
		// It will return the parent window name as a String
		String parent = driver.getWindowHandle();
		// This will return the number of windows opened by Webdriver and will
		Set<String> s1 = driver.getWindowHandles();
		Iterator<String> I1 = s1.iterator();
		while (I1.hasNext()) {
			String child_window = I1.next();
			if (!parent.equals(child_window)) {
				driver.switchTo().window(child_window);
			}
		}
	}

	/**
	 * Explicit wait with expected conditions are returns
	 * WebElement/s if its found
	 * Throws Timeout Exception if not found in given time frame.
	 * if encounters staleElement exception it will through the stale exception.
	 * This method is written for overcomes the staleElement exception to looping n- types
	 * @param WebElement
	 * @return
	 */
	public boolean elementFound(WebElement element) {
		Boolean found = false;
		try {
			if (element.isDisplayed()) {
				found = true;
				return found;
			}
		} 
		catch (NoSuchElementException e) {
			found = false;
			System.out.println("encountered - NoSuchElementException");
		}
		catch (StaleElementReferenceException e) {
			found = false;
			System.out.println("encountered - StaleElementReferenceException");
		}
		catch (Exception e) {
			found = false;
			System.out.println("encountered - genral Exception");
		}
		return found;
	}

	/**
	 * written for overcome the staleElemenstRef Exception
	 * @param elementxpath
	 * @return webElement
	 * @throws InterruptedException
	 */
	public static WebElement getLocator(String elementxpath) throws InterruptedException {
		WebElement yourSlipperyElement = null;
		int count = 0;
		while (count < 5) {
			try {
				yourSlipperyElement = driver.findElement(By.xpath(elementxpath));
				count = 6;
				break;
			} catch (Exception e) {
				System.out.println("doesnt find the locator");
				Thread.sleep(500);
				count = count + 1;
			}
		}
		return yourSlipperyElement;
	}
	// ************** Assertions and Verification methods starts here***********************************
	/**
	 * String Assertion
	 * @param actual
	 * @param expected
	 */
	public static void assertText(String actual,String expected) {
		 if(actual.equals(expected)){
			 Assert.assertEquals(actual, expected);
			 BaseClass.test.info("<B> <span style='color:GREEN;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
			 BaseClass.test.log(Status.PASS,
						MarkupHelper.createLabel("Expected and Actual are equal", ExtentColor.GREEN));
		 }
		 else {
			 BaseClass.test.info("<B> <span style='color:ORANGERED;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
			 BaseClass.test.log(Status.FAIL,
				MarkupHelper.createLabel("Expected and Actual texts are mismatch: ", ExtentColor.RED));
			 Assert.assertEquals(actual, expected);
		}
	   }
	/**
	 * Check for the found text contains the given sub string. 
	 * @param found (getText- From application)
	 * @param subStr (expected text)
	 */
	public static void assertTextContains(String found,String subStr) {
		 if(found.contains(subStr)){
			 Assert.assertEquals(true, found.contains(subStr));
			 BaseClass.test.info("<B> <span style='color:GREEN;'>"+"Found : ["+found+"] contains given String: ["+subStr+"]");
			 BaseClass.test.log(Status.PASS,
						MarkupHelper.createLabel("Expected text contains given string(Actual)", ExtentColor.GREEN));
		 }
		 else {
			 BaseClass.test.info("<B> <span style='color:ORANGERED;'>"+"Found : ["+found+"] contains given String: ["+subStr+"]");
			 BaseClass.test.log(Status.FAIL,
				MarkupHelper.createLabel("Expected and Actual texts are mismatch: ", ExtentColor.RED));
			 Assert.assertEquals(true, found.contains(subStr));
		}
	   }
	 /**
		 * Integer Assertions
		 * @param actual
		 * @param expected
		 */
		public static void assertValue(int actual,int expected) {
			 if(actual==expected){
				 Assert.assertEquals(actual, expected);
				 BaseClass.test.info("<B> <span style='color:DARKGREEN;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
				 BaseClass.test.log(Status.PASS,
							MarkupHelper.createLabel("Expected and Actual are equal", ExtentColor.GREEN));
			 }
			 else {
				 BaseClass.test.info("<B> <span style='color:ORANGERED;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
				 BaseClass.test.log(Status.FAIL,
					MarkupHelper.createLabel("Expected and Actual texts mismatch: ", ExtentColor.RED));
				 Assert.assertEquals(actual, expected);
			}
	  }
		
		public static void verifyEquals(Object actual, Object expected)  {
	    	try {
	    		assertEquals(actual, expected);
	    		//BaseClass.test.info("<B> <span style='color:GREEN;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
	    		BaseClass.test.log(Status.PASS,
						MarkupHelper.createLabel("Actual:["+actual+"],Expectted:["+expected+"]", ExtentColor.GREEN));
			} catch(Throwable e) {
				//BaseClass base =  new BaseClass();
	    		addVerificationFailure(e);
	    		test.info("<B> <span style='color:ORANGERED;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
	    		test.log(Status.WARNING,
						MarkupHelper.createLabel("Expected and Actual texts mismatch", ExtentColor.AMBER));
	    		try {
	    			BaseClass.test.log(Status.WARNING, "Expected and Actual are mismatch",
	    		MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(BaseClass.driver, "verification")).build());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	    }
	
		
		// verification dependent methods
		 public static void fail(String message) {
		    	Assert.fail(message);
		    }
		    
			public static List<Throwable> getVerificationFailures() {
				List<Throwable> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
				return verificationFailures == null ? new ArrayList<Throwable>() : verificationFailures;
			}
			
			private static void addVerificationFailure(Throwable e) {
				List<Throwable> verificationFailures = getVerificationFailures();
				verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
				verificationFailures.add(e);
			}
	// ************** Assertions and Verification methods ends here***********************************
}