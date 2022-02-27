package interview;

import java.util.concurrent.TimeUnit;

import org.testng.ITestContext;

import java.util.Hashtable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestCases {
	WebDriver driver;

	@BeforeTest
	public void beforeClass(ITestContext context) {

		String[] browser = context.getCurrentXmlTest().getParameter("browser").split("V");

		//Decide the Browser base on parameter browser in testSuite
		switch (browser[0]) {
		case "chrome":
			// for window
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "\\src\\drivers\\chromeV" + browser[1] + ".exe");
			driver = new ChromeDriver();
			break;
		case "firefox":
			// for window
			System.setProperty("webdriver.gecko.driver",
					System.getProperty("user.dir") + "\\src\\drivers\\geckoV" + browser[1] + ".exe");
			driver = new FirefoxDriver();
			break;
		default:
			System.out.print("Can't find specific browser version, use ChromeV98");
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "\\src\\drivers\\chromeV98.exe");
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		//Test URL
		driver.get("https://react-redux.realworld.io/#/");
	}

	@Test(dataProvider = "dataMap", dataProviderClass = ReadData.class)	
	public void TC_01_VerifyUserAtHomepage(Hashtable testData) {
		
		//get data from dataFile
		String username = testData.get("Username").toString();
		String[] email = testData.get("Email").toString().split("@");
		String password = testData.get("Password").toString();
		
		//test steps
		driver.findElement(By.xpath("//a[contains(text(),'Sign up')]")).click();
		
		String randomStringFromURL = driver.getCurrentUrl().replace("https://react-redux.realworld.io/#/register?_k=", "");
		
		driver.findElement(By.xpath("//input[@placeholder=\"Username\"]"))
				.sendKeys(username + randomStringFromURL);
		driver.findElement(By.xpath("//input[@placeholder=\"Email\"]"))
				.sendKeys(email[0] + randomStringFromURL + "@" + email[1]);
		driver.findElement(By.xpath("//input[@placeholder=\"Password\"]")).sendKeys(password);
		
		//software defect, should be Sign up button
		driver.findElement(By.xpath("//button[@type=\"submit\" and contains(text(),'Sign in')]")).click();
		
		
		//assertion
		boolean isHome = driver.findElement(By.xpath("//div[@class='home-page']")).isDisplayed();
		Assert.assertTrue(isHome);
	}

	@Test(dataProvider = "dataMap", dataProviderClass = ReadData.class)	
	public void TC_02_CantLoginWithWrongPass(Hashtable testData) {
		
		//get data from dataFile
		String username = testData.get("Username").toString();
		String password = testData.get("Password").toString();
		
		//test steps
		driver.findElement(By.xpath("//a[contains(text(),'Sign in')]")).click();
		
		driver.findElement(By.xpath("//input[@placeholder=\"Email\"]")).sendKeys(username);
		driver.findElement(By.xpath("//input[@placeholder=\"Password\"]")).sendKeys(password);
		
		driver.findElement(By.xpath("//button[@type=\"submit\" and contains(text(),'Sign in')]")).click();
		
		String errorMess = driver.findElement(By.xpath("//ul[@class=\"error-messages\"]")).getText().trim();
		
		//assertion
		Assert.assertEquals(errorMess, "email or password is invalid");
	}

	@AfterTest
	public void afterClass() {
		driver.quit();
	}
}
