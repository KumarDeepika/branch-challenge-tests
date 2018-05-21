package com.branch.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseWebDriver {
	
	protected WebDriver driver;
	
	@BeforeMethod(alwaysRun = true)
	public void setUp() {

		// WebDriverManager.chromedriver().setup();
		// System.setProperty("webdriver.chrome.driver",
		// "/Users/t_deepikak/chromedriver 2");
		// driver = new ChromeDriver();
		System.out.println(System.getProperty("user.dir"));
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/geckodriver 2");
		// DesiredCapabilities capabilities=DesiredCapabilities.firefox();

		// capabilities.setCapability("marionette", true);
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@AfterMethod(alwaysRun = true)
	public void closeRemoteSession() {
		if (driver != null) {
			try {
				driver.close();
				driver.quit();
				//logger.info("Remote driver has been closed");
			} catch (Exception ex) {
				//logger.info(ex.getMessage());
			}
		} 
		
	}
	
	public WebDriver getWebDriver() {
		return driver;
	}
	
	public void setWebDriver(WebDriver driver) {
		this.driver = driver;
	}
}
