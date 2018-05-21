package com.branch.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class HomePage extends BasePage {

	private WebDriver driver;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		pageInitElements(driver, this);
		if (!waitUntilVisible(driver, branchLogo)) {
			assert false : "Branch Home Page Load Error";
		}

	}

	@FindBy(id = "logo")
	public WebElement branchLogo;

	@FindBy(css = "[data-element-tag=team]")
	public WebElement footerLinkTeam;

	public void clickLinkTeam() {
		pageScrollBottom(driver);
		waitUntilVisible(driver, footerLinkTeam);
		footerLinkTeam.click();
	}

}
