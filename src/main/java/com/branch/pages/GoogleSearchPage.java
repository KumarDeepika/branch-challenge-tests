package com.branch.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class GoogleSearchPage extends BasePage {
	private WebDriver driver;
	private ExtentTest logger;

	public GoogleSearchPage(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
		pageInitElements(driver, this);

	}

	@FindBy(name = "q")
	public WebElement googleSearchBox;

	@FindBy(id = "resultStats")
	public WebElement googleResults;

	@FindBy(partialLinkText = "Branch - A mobile linking platform")
	public WebElement branchSearchResult;

	@FindBy(css = "input[value='Google Search']")
	public WebElement googleSearchButton;

	public void SearchFor(String keyword) {
		waitTillPageLoads(driver, 20);
		if (driver.getTitle().equalsIgnoreCase("google")) {
			waitUntilVisible(driver, googleSearchBox);
			googleSearchBox.sendKeys(keyword);
			googleSearchButton.submit();
			waitTillPageLoads(driver, 20);

		} else {
			assert false : "Cannot find a page with title - google";
		}

	}

	public HomePage clickLinkSearchResultsPage() {

		if (!waitUntilVisible(driver, googleResults)) {
			assert false : "Expected to see  Google Search Results Page instead see " + driver.getTitle();
		} else {
			logger.log(Status.PASS, "User is in Google Search Results Page");
		}
		if (!waitUntilVisible(driver, branchSearchResult)) {
			assert false : "Did not see a link to Branch Website";

		}
		branchSearchResult.click();
		return pageInitElements(driver, HomePage.class);

	}

}
