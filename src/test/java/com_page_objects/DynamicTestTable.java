package com_page_objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class DynamicTestTable {

	WebDriver driver;
	JSONArray testData;

	@BeforeClass
	public void setup() throws IOException {
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");

		// Load JSON data from file
		String jsonFilePath = "F:\\Eclipse work space\\CAW_Studios_Assesment\\testdata.json";
		String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
		testData = new JSONArray(content);
	}

	@Test
	public void testDynamicTablePopulation() {
		// Step 2: Click on the Table Data button
		WebElement tableDataButton = driver.findElement(By.id("tabledata"));
		tableDataButton.click();

		// Step 3: Insert JSON data into input box and click Refresh Table
		WebElement inputBox = driver.findElement(By.id("jsondata"));
		inputBox.clear();
		inputBox.sendKeys(testData.toString());

		WebElement refreshButton = driver.findElement(By.id("refreshtable"));
		refreshButton.click();

		// Step 4: Retrieve table rows
		WebElement table = driver.findElement(By.id("dynamictable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));

		// Validate each row matches expected JSON data
		for (int i = 1; i < rows.size(); i++) { // Start from 1 to skip header row
			WebElement row = rows.get(i);
			List<WebElement> cells = row.findElements(By.tagName("td"));

			// Extract actual values from UI
			String actualName = cells.get(0).getText();
			int actualAge = Integer.parseInt(cells.get(1).getText());
			String actualGender = cells.get(2).getText();

			// Extract expected values from JSON
			JSONObject expectedPerson = testData.getJSONObject(i - 1);
			String expectedName = expectedPerson.getString("name");
			int expectedAge = expectedPerson.getInt("age");
			String expectedGender = expectedPerson.getString("gender");

			// Step 5: Assert each field
			Assert.assertEquals(actualName, expectedName, "Name mismatch at row " + i);
			Assert.assertEquals(actualAge, expectedAge, "Age mismatch at row " + i);
			Assert.assertEquals(actualGender, expectedGender, "Gender mismatch at row " + i);
		}
	}

}


