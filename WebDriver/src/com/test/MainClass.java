package com.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MainClass {
	
	public static void main(String[] args) throws InterruptedException, ParseException, FileNotFoundException {
		
		HashMap<String, String> testData = getTestDataFromCSV(args);
		
		String dayList = testData.get("days").toString();
		
		System.setProperty("webdriver.gecko.driver","C:\\Users\\rinson\\Desktop\\MyBin\\Selenium\\geckodriver.exe");
		
		WebDriver driver = new FirefoxDriver();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		System.out.println("Going to Home page ASASAS..................................................... : "+ new Date());
		
		driver.navigate().to("https://www.ultimatix.net/");
		
		Thread.sleep(2000);
		
		//new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.id("login_button")));
		
		driver.findElement(By.id("USER")).sendKeys(testData.get("empid").toString());
		
		driver.findElement(By.id("PASSWORD")).sendKeys(testData.get("password").toString());  
		
		driver.findElement(By.id("login_button")).click();
		
		Thread.sleep(5000);
		
		System.out.println("Going to GESS page ..................................................... : "+ new Date());
		
		driver.navigate().to("https://gess.ultimatix.net/gess/pages/GESS/Travel/Home/HolidayHomesInitationHome.jsf");
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		
		WebElement elementGESS = null;
		
		if(testData.get("destination").equalsIgnoreCase("goa")){
			
			elementGESS = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='holidayHomesInitForm:_idJsp109:2:_idJsp110']")));
			
		}else if(testData.get("destination").equalsIgnoreCase("coorg")){
			
			elementGESS = wait.until(ExpectedConditions.elementToBeClickable(By.id("holidayHomesInitForm:_idJsp109:1:_idJsp110")));
			
		}else if(testData.get("destination").equalsIgnoreCase("munnar")){
			
			elementGESS = wait.until(ExpectedConditions.elementToBeClickable(By.id("holidayHomesInitForm:_idJsp109:5:_idJsp110")));
			
		}else if(testData.get("destination").equalsIgnoreCase("Coonoor")){
			
			elementGESS = wait.until(ExpectedConditions.elementToBeClickable(By.id("holidayHomesInitForm:_idJsp109:0:_idJsp110")));
			
		}
				
		elementGESS.click();
		
		WebDriverWait wait1 = new WebDriverWait(driver, 15);
		
		wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("holidayHomesInitForm:roomType")));
		
		WebElement mySelectElement = driver.findElement(By.id("holidayHomesInitForm:roomType"));
		
		Select dropdown = new Select(mySelectElement);
		
		System.out.println("Hitting roomType........................................... : "+ new Date());
		
		int roomTypeIndex = Integer.parseInt(testData.get("roomType").toString());
			
		dropdown.selectByIndex(roomTypeIndex);
			
		//Thread.sleep(doSetMilliSecond(testData.get("targetTime").toString()));
		
		WebElement table = driver.findElement(By.id("holidayHomesInitForm:row1"));
		
		List<WebElement> tds = table.findElements(By.tagName("td"));  
		
		doClick(tds, dayList);
		
		System.out.println("Checking terms and Conditions............................... : "+ new Date());
		
		WebElement termsAndCondition = driver.findElement(By.id("holidayHomesInitForm:termsConditions"));
		
		termsAndCondition.click();
		
		WebElement bookButton = driver.findElement(By.id("holidayHomesInitForm:book1"));
		
		Thread.sleep(doSetMilliSecond(testData.get("targetTime").toString()));
		
		bookButton.click();
		
		WebDriverWait wait2 = new WebDriverWait(driver, 15);
		
		WebElement elmConfirm = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("bookform:Submit10")));
		
		System.out.println("Submit popup confirm.......................................... : "+ new Date());
		
		elmConfirm.click();
		
		driver.close();
	}

	private static long doSetMilliSecond(String target) throws ParseException {
		
		DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		
		String today = formatter.format(new Date());
		
		System.out.println("today : "+ today);
		
		System.out.println("target : "+ target);
		
		long threadSleep = formatter.parse(target).getTime() - formatter.parse(today).getTime();
		
		System.out.println("threadSleep : "+ threadSleep);
		
		return ((threadSleep > 0)? threadSleep : 0);
		
	}

	private static void doClick(List<WebElement> tds, String dayList) {
		
		for(WebElement td : tds){
					
			WebElement span = td.findElement(By.tagName("span")); 
						
			doClickDays(span, dayList.split("-"));
		}
	}

	private static void doClickDays(WebElement span, String[] days) {
		
		for( String day : days){
			
			if(span.getText().equals(day)){
				
				System.out.println("Text : " + span.getText());
				
				System.out.println("Hitting day..................................................... : "+ new Date());
				
				span.click();
			}
		}
	}

	private static HashMap<String, String> getTestDataFromCSV(String[] args) throws FileNotFoundException {
		
		HashMap<String, String> testData = new HashMap<String,String>(); 
		
		args = new String[]{"C:\\Users\\rinson\\Desktop\\MyBin\\Selenium\\TestData.csv"};
		
		
		if(null != args && args.length > 0){
			
			System.out.println("path : " + args[0]);
			
			Scanner sc = new Scanner(new File(args[0]));
			
			while(sc.hasNext()){
				
				String[] arr = sc.nextLine().split(",");
				
				testData.put("empid", arr[0]);
				
				testData.put("password", arr[1]);
				
				testData.put("days", arr[2]);
				
				testData.put("destination", arr[3]);
				
				testData.put("targetTime", arr[4]);
				
				testData.put("roomType", arr[5]);
				
				System.out.println(testData.toString());
				
			}
			
		}
		
		return testData;
	}
}

