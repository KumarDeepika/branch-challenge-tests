package com.branch.tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.branch.helpers.Employee;
import com.branch.pages.HomePage;
import com.branch.pages.TeamPage;
import com.branch.webdriver.BaseWebDriver;

public class TeamPageTests extends BaseWebDriver {

	/**
	 * Test case to verify sum of employees in individual department tab totals
	 * to the employee count in ALL Tab.
	 *
	 * 
	 **/

	@Test(priority = 1, description = "Verify that number of employees match between All tab and sum of other tabs", enabled = true)
	public void verifyEmployeeCountAcrossTabs() {

		HomePage homePage = openGoogle(driver);
		homePage.clickLinkTeam();
		TeamPage teamPage = new TeamPage(driver, reporter);
		Assert.assertTrue(teamPage.getDepartmentsEmployeeCount(),
				"Sum of employees in other departments is not equal to the total count of employees in the All Tab");

	}

	/**
	 * Test case to verify employee names and departments in other tabs match
	 * with all tab.
	 *
	 * 
	 **/

	@Test(priority = 3, description = "Verify that employee names and departments in other tabs match with all tab", enabled = true)

	public void compareEmpDetailsOtherTabsWithAll() {

		HomePage homePage = openGoogle(driver);
		homePage.clickLinkTeam();
		TeamPage teamPage = new TeamPage(driver, reporter);
		HashMap<String, Employee> allEmployees = teamPage.getAllDepartmentDetails();
		HashMap<String, List<Employee>> departmentDetails = teamPage.getOtherDepartmentDetails();
		List<String> errorList = teamPage.validateEmployeeDetails("", departmentDetails, allEmployees);
		Assert.assertTrue(errorList.isEmpty(),
				"Found mismatches in names/departments between All tab and other departments tab");
		reporter.log(Status.PASS,
				"Employee names and departments listed in OTHER tabs match with the names listed in ALL tab");

	}

	/**
	 * Test case to ensure employees are displayed under correct department tab
	 *
	 * 
	 **/

	@Test(priority = 2, description = "Verify employees have the correct departments listed in their profile", enabled = true)
	public void verifyEmployeeCategorizationInDepartment() {

		HomePage homePage = openGoogle(driver);
		homePage.clickLinkTeam();
		TeamPage teamPage = new TeamPage(driver, reporter);
		Assert.assertTrue(teamPage.ValidateEmployeeDepartmentAssignment(),
				"Some employees are not listed under the department tab specified for that employee");

	}

	/**
	 * Test case to verify all employees in ALL tab are present under respective
	 * department tab
	 *
	 * 
	 **/

	@Test(priority = 4, description = "Find if there are employees that do not belong to ANY department tab", enabled = true)
	public void findEmployeesNotCategorizedUnderAnyDepartment() {

		HomePage homePage = openGoogle(driver);
		homePage.clickLinkTeam();
		TeamPage teamPage = new TeamPage(driver, reporter);

		HashSet<String> allTabEmployeeNameList = teamPage.ValidateEmployeeName();
		if (allTabEmployeeNameList.size() > 0) {
			reporter.log(Status.FAIL, "All Tab has employees that are not present in any other department");
			reporter.log(Status.INFO, "These are the employees that are not present in any of the department tabs "
					+ allTabEmployeeNameList.toString());
		}
		Assert.assertTrue(allTabEmployeeNameList.isEmpty(),
				"There are employees in All tab that are not categorized into the department tab based on their department");

	}

	/**
	 * Test case to verify images of employees are displayed as per all tab in
	 * other tabs
	 *
	 * 
	 **/

	@Test(priority = 5, description = "Verify images of employees match between all and other tabs", enabled = true)

	public void compareEmpPhotoOtherTabsWithAll() {

		String departmentName = "";

		HomePage homePage = openGoogle(driver);
		homePage.clickLinkTeam();
		TeamPage teamPage = new TeamPage(driver, reporter);

		HashMap<String, Employee> allEmployees = teamPage.getAllDepartmentDetails();
		HashMap<String, List<Employee>> departmentDetails = teamPage.getOtherDepartmentDetails();

		List<String> errorList = teamPage.validateEmployeeImage(departmentName, departmentDetails, allEmployees);
		Assert.assertTrue(errorList.isEmpty(),
				"Found mismatches in pictures of employees between All tab and other departments tab");
		reporter.log(Status.PASS, "Photos of Employees match between ALL tab and other tabs");

	}

}
