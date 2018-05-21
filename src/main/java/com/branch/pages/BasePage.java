package com.branch.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

	public boolean waitUntilVisible(WebDriver driver, WebElement element) {
		return waitUntilElementVisible(driver, element, 60);
	}

	public boolean waitUntilElementVisible(WebDriver driver, final WebElement element, final long timeOutInSeconds) {
		if (element == null) {
			System.out.println("Element passed is null");
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			return false;
		}

		return true;

	}

	/**
	 * Waits till page is loaded.
	 * 
	 * @param timeOutInSeconds
	 *            time out
	 * @return true if the page loaded, else false.
	 */
	public boolean waitTillPageLoads(WebDriver driver, final int timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		ExpectedCondition<Boolean> successCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {

				return String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
						.equals("complete");
			}
		};
		boolean success;
		try {
			success = wait.until(successCondition);
		} catch (TimeoutException e) {
			System.out.println("Page was not loaded within the given timeout of %s seconds " + timeOutInSeconds);
			success = false;
		}
		return success;
	}

	public void pageInitElements(WebDriver driver, final BasePage expectedPage) {
		waitTillPageLoads(driver, 60000);
		PageFactory.initElements(driver, expectedPage);
	}

	public <T extends BasePage> T pageInitElements(WebDriver driver, final Class<?> expectedPage) {
		waitTillPageLoads(driver, 60000);
		@SuppressWarnings("unchecked")
		T expectedObject = (T) PageFactory.initElements(driver, expectedPage);
		return expectedObject;
	}

	public void pageScrollBottom(WebDriver driver) {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

}
