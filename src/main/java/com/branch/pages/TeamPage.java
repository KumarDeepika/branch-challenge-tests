package com.branch.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.branch.helpers.Employee;

public class TeamPage extends BasePage {
	private WebDriver driver;
	private ExtentTest testInfo;

	public TeamPage(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.testInfo = logger;
		pageInitElements(driver, this);
		if (!waitUntilVisible(driver, teamCategories)) {
			assert false : "Navigation to Branch Team page error";
		}
		logger.log(Status.PASS, "Successfully navigated to Branch Team Page");

	}

	@FindBy(css = ".team-categories")
	public WebElement teamCategories;

	@FindBy(css = ".team-categories>li>a")
	public List<WebElement> teamCategoriesHeader;

	@FindBy(css = ".row.row-centered>div")
	public List<WebElement> teamEmployees;

	@FindBy(css = ".category-all")
	public List<WebElement> allCategoryContainer;

	@FindBy(css = ".info-block>h2")
	public List<WebElement> containerNameAll;

	@FindBy(css = ".info-block>h4")
	public List<WebElement> containerDepartmentAll;

	@FindBy(css = "a[rel='all']")
	public WebElement allHeader;

	public WebElement getDepartmentEmployee(String departmentName, int index) {
		return driver.findElement(By.xpath("//*[contains(@class,\'category-" + departmentName + "')][" + index
				+ "]//div[@class='info-block']/h4"));
	}

	public WebElement getNameEmployee(String departmentName, int index) {
		return driver.findElement(By.xpath("//*[contains(@class,\'category-" + departmentName + "')][" + index
				+ "]//div[@class='info-block']/h2"));
	}

	public List<WebElement> getTeamHeaderContents(String tabName) {
		return driver.findElements(By.cssSelector(".category-" + tabName));
	}

	public List<WebElement> getDepartmentContainer(String departmentName) {
		return driver.findElements(By.cssSelector(".category-" + departmentName));

	}

	public WebElement getImageEmployee(String departmentName, int index) {
		return driver.findElement(By.xpath(
				"//*[contains(@class,\'category-" + departmentName + "')][" + index + "]//div[@class='image-block']"));
	}

	public HashMap<String, List<Employee>> globalDeptDetails;
	public HashMap<String, Employee> globalAllDetails;

	/**
	 * Function to get the total employee count in All tab and other department
	 * tabs and compare the two.
	 **/

	public boolean getDepartmentsEmployeeCount() {
		int allEmpCount = 0, deptEmpCount = 0;
		for (WebElement elem : teamCategoriesHeader) {
			teamEmployees = getTeamHeaderContents(elem.getAttribute("rel"));
			if (elem.getAttribute("rel").equalsIgnoreCase("all")) {
				allEmpCount = teamEmployees.size();
				testInfo.log(Status.INFO, "All Tab has a total of " + allEmpCount + " employees");
			} else {
				deptEmpCount = deptEmpCount + teamEmployees.size();
			}
		}
		testInfo.log(Status.INFO, "Other department tabs have a total of " + deptEmpCount + " employees");
		return allEmpCount == deptEmpCount;
	}

	/**
	 * Function to get employee details(name, department, image) from all
	 * department. This method stores the all details in hashmap with name as
	 * the key and employee profile as value.
	 **/

	public HashMap<String, Employee> getAllDepartmentDetails() {

		if (globalAllDetails != null) {
			System.out.println("GlobalAllDetails not null");
			return globalAllDetails;
		}
		globalAllDetails = new HashMap<String, Employee>();
		String departmentName = "all";
		List<WebElement> employeeContainer = getTeamHeaderContents(departmentName);
		globalAllDetails = getAllEmployeeList(employeeContainer, departmentName);
		return globalAllDetails;

	}

	/**
	 * Function to get employee details(name, department, image) from all
	 * department.
	 **/

	private HashMap<String, Employee> getAllEmployeeList(List<WebElement> employeeContainer, String departmentName) {

		HashMap<String, Employee> allEmployeeMap = new HashMap<String, Employee>();
		for (int i = 1; i <= employeeContainer.size(); i++) {
			Employee employee = new Employee();
			WebElement empNameBlock = getNameEmployee(departmentName, i);
			String empName = empNameBlock.getText();
			WebElement empDepartmentBlock = getDepartmentEmployee(departmentName, i);
			String empDepartment = empDepartmentBlock.getText();
			WebElement empImage = getImageEmployee(departmentName, i);
			String empImageSource = empImage.getAttribute("style");
			employee.setEmpName(empName);
			employee.setEmpDepartment(empDepartment);
			employee.setEmpImage(empImageSource);
			allEmployeeMap.put(empName, employee);

		}

		return allEmployeeMap;

	}

	/**
	 * Function to get employee details(name, department, image) from other
	 * departments
	 **/

	public HashMap<String, List<Employee>> getOtherDepartmentDetails() {
		String departmentName = null;

		if (globalDeptDetails != null) {
			return globalDeptDetails;
		}

		globalDeptDetails = new HashMap<String, List<Employee>>();
		for (WebElement elem : teamCategoriesHeader) {
			if (!elem.getAttribute("rel").equals("all")) {
				departmentName = elem.getAttribute("rel");
				List<WebElement> employeeContainer = getTeamHeaderContents(departmentName);
				List<Employee> employeeListOtherDepartments = getDepartmentEmployeeList(departmentName,
						employeeContainer);
				globalDeptDetails.put(departmentName, employeeListOtherDepartments);
			}
		}

		return globalDeptDetails;

	}

	/**
	 * Function to get employee details(name, department, image) from other
	 * departments
	 **/

	public List<Employee> getDepartmentEmployeeList(String departmentName, List<WebElement> employeeContainer) {

		List<Employee> employeeList = new ArrayList<Employee>();
		for (int i = 1; i <= employeeContainer.size(); i++) {
			Employee employee = new Employee();
			WebElement empNameBlock = getNameEmployee(departmentName, i);
			String empName = empNameBlock.getText();
			WebElement empDepartmentBlock = getDepartmentEmployee(departmentName, i);
			String empDepartment = empDepartmentBlock.getText();
			WebElement empImage = getImageEmployee(departmentName, i);
			String empImageSource = empImage.getAttribute("style");
			employee.setEmpName(empName);
			employee.setEmpDepartment(empDepartment);
			employee.setEmpImage(empImageSource);
			employeeList.add(employee);

		}
		return employeeList;
	}

	/**
	 * Function to compare employee information in other tabs match with the
	 * information in All tab
	 **/

	public List<String> validateEmployeeDetails(String departmentName,
			HashMap<String, List<Employee>> departmentDetails, HashMap<String, Employee> allEmployees) {

		List<String> errorList = new ArrayList<String>();
		if (departmentName != null && !departmentName.isEmpty()) {

			List<Employee> employeeList = departmentDetails.get(departmentName);
			errorList = compareNamesandDepartment(employeeList, allEmployees);

		} else {
			for (String name : departmentDetails.keySet()) {
				List<Employee> employeeList = departmentDetails.get(name);
				errorList = compareNamesandDepartment(employeeList, allEmployees);
			}
		}
		return errorList;

	}

	/**
	 * Function to compare names and department values in other tab match with
	 * all tab.
	 **/

	public List<String> compareNamesandDepartment(List<Employee> employeeList, HashMap<String, Employee> allEmployees) {
		List<String> errorList = new ArrayList<String>();
		for (Employee emp : employeeList) {

			String empName = emp.getEmpName();
			String empDepartment = emp.getEmpDepartment();

			if (allEmployees.containsKey(empName)) {
				if (!allEmployees.get(empName).getEmpDepartment().equals(empDepartment)) {
					errorList.add(empName + " , " + empDepartment);
					testInfo.log(Status.FAIL,
							"Employee - " + empName + "is expected to be in department - " + empDepartment);
				}
			} else {
				errorList.add(empName);
				testInfo.log(Status.FAIL, "Employee - " + empName + "is not present in All department");

			}
		}
		return errorList;
	}

	/**
	 * Function to validate employee image for each employee in other tabs match
	 * with the information in All tab
	 **/

	public List<String> validateEmployeeImage(String departmentName, HashMap<String, List<Employee>> departmentDetails,
			HashMap<String, Employee> allEmployees) {

		List<String> errorList = new ArrayList<String>();
		if (departmentName != null && !departmentName.isEmpty()) {

			List<Employee> employeeList = departmentDetails.get(departmentName);
			errorList = compareImages(employeeList, allEmployees);

		} else {
			for (String name : departmentDetails.keySet()) {
				List<Employee> employeeList = departmentDetails.get(name);
				errorList = compareImages(employeeList, allEmployees);
			}
		}
		return errorList;

	}

	/**
	 * Function to compare employee image of each employee in other tabs match
	 * with the information in All tab
	 **/

	private List<String> compareImages(List<Employee> employeeList, HashMap<String, Employee> allEmployees) {
		List<String> errorList = new ArrayList<String>();
		for (Employee emp : employeeList) {

			String empName = emp.getEmpName();
			String empImage = emp.getEmpImage();

			if (allEmployees.containsKey(empName)) {
				if (!allEmployees.get(empName).getEmpImage().equals(empImage)) {
					errorList.add(empName + " , " + empImage);
					testInfo.log(Status.FAIL,
							"Employee photo for this employee " + empName + "is expected to be  - " + empImage);
				}
			} else {
				errorList.add(empName);

				testInfo.log(Status.FAIL, "Employee - " + empName + "is not present in All department");

			}
		}
		return errorList;
	}

	/**
	 * Function to validate each employee is categorized in the right department
	 * tab. Department in employee profile matches with the tab name.
	 **/

	public boolean ValidateEmployeeDepartmentAssignment() {

		boolean departmentMisMatch = true;
		String departmentName = null;
		allCategoryContainer.size();
		for (WebElement elem : teamCategoriesHeader) {
			departmentName = elem.getAttribute("rel");
			String departmentValue = elem.getText();
			if (!departmentName.equals("all")) {
				List<WebElement> employees = getTeamHeaderContents(departmentName);

				for (int i = 1; i <= employees.size(); i++) {
					WebElement empDepartmentBlock = getDepartmentEmployee(departmentName, i);
					String empDeptName = empDepartmentBlock.getText();
					if (!empDeptName.toLowerCase().contains(departmentValue.toLowerCase())) {
						WebElement elementEmpName = getNameEmployee(departmentName, i);
						String empName = elementEmpName.getText();
						departmentMisMatch = false;
						testInfo.log(Status.FAIL, "Employee - " + empName + " is expected to be in department "
								+ empDeptName + " but he is present under " + departmentValue);
					}
				}
			}

		}
		return departmentMisMatch;

	}

	/**
	 * Function to get additional employees, if any, in the all tab that have
	 * not been assigned to any department tab
	 **/

	public HashSet<String> ValidateEmployeeName() {

		HashSet<String> allTabEmployeeNames = new HashSet<String>();
		HashSet<String> otherTabEmployeeNames = new HashSet<String>();

		allCategoryContainer.size();
		for (WebElement elem : teamCategoriesHeader) {
			String departmentName = elem.getAttribute("rel");
			if (!departmentName.equals("all")) {
				List<WebElement> departmentContainer = getDepartmentContainer(departmentName);
				for (int i = 1; i <= departmentContainer.size(); i++) {
					String empNameDept = getNameEmployee(departmentName, i).getText();
					otherTabEmployeeNames.add(empNameDept);

				}
			} else if (departmentName.equals("all")) {
				List<WebElement> departmentContainer = getDepartmentContainer(departmentName);
				for (int i = 1; i <= departmentContainer.size(); i++) {
					String empNameDept = getNameEmployee(departmentName, i).getText();
					allTabEmployeeNames.add(empNameDept);

				}
			}
		}

		allTabEmployeeNames.removeAll(otherTabEmployeeNames);
		return allTabEmployeeNames;

	}

}
