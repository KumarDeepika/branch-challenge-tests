package com.branch.webdriver;

import java.lang.reflect.Method;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.branch.pages.GoogleSearchPage;
import com.branch.pages.HomePage;
import com.branch.webdriver.PropertyUtil;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class BaseWebDriver {

	protected WebDriver driver;

	protected PropertyUtil propertyUtil = new PropertyUtil();
	protected ExtentHtmlReporter htmlReporter;
	protected ExtentTest reporter;
	protected String pageURL = propertyUtil.getProperty("page.url");
	protected String browser = propertyUtil.getProperty("browser");

	protected static ExtentReports extent;
	private static ThreadLocal parentTest = new ThreadLocal();
	private static ThreadLocal methods = new ThreadLocal();
	protected ExtentTest parent;

	/**
	 * Method to create the report instance to be used by all of the tests being
	 * executed
	 * 
	 */

	@BeforeSuite
	public void createReportInstance() {
		extent = ExtentManager.createInstance("test-output/report.html");

	}

	/**
	 * Method to get the test name and store it as parent. All test methods
	 * under this test will be grouped
	 * 
	 */

	@BeforeMethod
	public synchronized void getTestName(Method method) {
		reporter = ((ExtentTest) parentTest.get()).createNode(method.getName());
		methods.set(reporter);
	}

	/**
	 * Get the test method names from the above <test name> field
	 * 
	 */

	@BeforeTest
	public void createHTMLReport(ITestContext testName) {

		extent = ExtentManager.getInstance();
		ExtentTest parent = extent.createTest(testName.getName());
		parentTest.set(parent);

	}

	/**
	 * Method to set up driver based on the parameter browser passed in from
	 * testng.xml
	 * 
	 */

	@Parameters("browser")
	@BeforeMethod(alwaysRun = true)

	public void setUp(String browser) {

		if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "geckodriver");
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "chromedriver");
			driver = new ChromeDriver();

		}

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

	}

	/**
	 * Get the loggers for each test method
	 * 
	 */

	@AfterMethod(alwaysRun = true)
	public void getResults(ITestResult result) {
		if (result.getStatus() == ITestResult.SUCCESS) {
			reporter.log(Status.PASS, MarkupHelper
					.createLabel("The test method named as " + result.getName() + " passed", ExtentColor.GREEN));
		} else if (result.getStatus() == ITestResult.FAILURE) {
			reporter.log(Status.FAIL,
					MarkupHelper.createLabel("Test Case Failed - " + result.getThrowable(), ExtentColor.RED));
		} else if (result.getStatus() == ITestResult.SKIP) {

			reporter.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		}
		extent.flush();
	}

	/**
	 * flush the report after execution
	 * 
	 */

	@AfterSuite
	public void endReport() {
		extent.flush();
	}

	/**
	 * Open the report automatically after test run
	 * 
	 */

	@AfterSuite
	public void showHTMLReport() {
		if (driver == null) {
			System.setProperty("webdriver.chrome.driver", "chromedriver");
			driver = new ChromeDriver();
		}
		driver = new ChromeDriver();

		driver.get("file:///" + System.getProperty("user.dir") + "/test-output/report.html");
	}

	/**
	 * Tear down driver after each test method
	 * 
	 */

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			try {
				reporter.log(Status.INFO, "Closing WebDriver");

				driver.close();
				driver.quit();
			} catch (Exception ex) {
			}
		}

	}

	public WebDriver getWebDriver() {
		return driver;
	}

	public void setWebDriver(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Open google, type in branch and search for the keyword
	 * 
	 */

	public HomePage openGoogle(WebDriver driver) {
		driver.get(pageURL);
		GoogleSearchPage googlePage = new GoogleSearchPage(driver, reporter);
		googlePage.SearchFor("branch");
		HomePage homePage = googlePage.clickLinkSearchResultsPage();
		return homePage;

	}

	public ExtentTest getExtentTestLogger() {
		return reporter;
	}
}
