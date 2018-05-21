package com.branch.team.tests;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.branch.team.helper.DepartmentDetails;
import com.branch.team.pages.BranchPage;
import com.branch.team.pages.BranchTeamPage;
import com.branch.team.pages.GoogleSearchPage;
import com.branch.webdriver.BaseWebDriver;

public class BranchTeamPageTests extends BaseWebDriver {

	@Test(description = "Tests to validate branch website ", enabled = true)

	public void verifyNumberOfEmployee1s() {

		driver.get("https://www.google.com/");
		GoogleSearchPage obj = new GoogleSearchPage(driver);
		obj.SearchFor("Branch");
		obj.clickLinkSearchResultsPage();

		BranchPage branchObj = new BranchPage(driver);
		branchObj.clickLinkTeam();

		BranchTeamPage branchTeamObject = new BranchTeamPage(driver);
		HashMap<String, Integer> teamCategories = branchTeamObject.clickTeamCategoryHeader();
		int departmentCount = branchTeamObject.iterateHashMap(teamCategories);
		branchTeamObject.verifytest(teamCategories.get("all"), departmentCount);

	}
	
	
	@Test(description = "Tests to validate employee names match between all and individual departments ", enabled = true)
	public void verifyEmployeeNames1() {

		driver.get("https://www.google.com/");
		GoogleSearchPage obj = new GoogleSearchPage(driver);
		obj.SearchFor("Branch");
		obj.clickLinkSearchResultsPage();

		BranchPage branchObj = new BranchPage(driver);
		branchObj.clickLinkTeam();
		
		BranchTeamPage branchTeamObject = new BranchTeamPage(driver);
		HashMap<String, Integer> teamCategories = branchTeamObject.clickTeamCategoryHeader();

		HashMap<String, String> getEmployeeDeatilsAll = branchTeamObject.storeEmployeeDetails();
		branchTeamObject.VerifyEmployeeNames(getEmployeeDeatilsAll, teamCategories);

	}
	
	
	@Test(description = "Tests to validate branch website ", enabled = true)

	public void verifyNumberOfEmployees() {

		driver.get("https://www.google.com/");
		GoogleSearchPage obj = new GoogleSearchPage(driver);
		obj.SearchFor("Branch");
		obj.clickLinkSearchResultsPage();

		BranchPage branchObj = new BranchPage(driver);
		branchObj.clickLinkTeam();

		BranchTeamPage branchTeamObject = new BranchTeamPage(driver);
		HashMap<String, DepartmentDetails> teamCategories = branchTeamObject.storeTeamCategory();
		
		
		//int departmentCount = branchTeamObject.iterateHashMap(teamCategories);
		//branchTeamObject.verifytest(teamCategories.get("all"), departmentCount);

	}
	

}
