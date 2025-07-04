package Day16;

import java.time.Duration;


import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Amazon {
	
	public static WebDriver driver;
	public static int sellector=1;
	public static int cartvalue;
	public static String expectedProductName;
	public static String expectedProductPrice;
	public static String cartProductName;
	public static String cartProductPrice;
	
	public static void main(String[] args) throws InterruptedException {
		browserSellector();
		browserSetting();
		navigation();
		getInformation();
		window();
		getCartValue();
		//Computers & Accessories   keyboard
		search("Computers & Accessories", "keyboard");
		productSearchResult();
		cartAdded();
		cartValodation();
		validateProductDetails();
		signOut();
		closeBrowser();
		
		

	}
	public static void browserSellector() {
		switch (sellector) {
		case 1:
			System.out.println("User Select Chrome Browser");
			driver=new ChromeDriver();
			break;
		case 2:
			System.out.println("User Select Edge Browser");
			driver=new EdgeDriver();
			break;
		default:
			System.out.println("User Select Unknow Value So Open Chrome Browser");
			driver=new ChromeDriver();
			break;
		}
	}
	public static void browserSetting() {
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(50));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
	}
	public static void navigation() {
		driver.navigate().to("https://www.amazon.in/");
		//driver.navigate().refresh();
	   // driver.navigate().back();
		//driver.navigate().forward();
	}
	public static void getInformation() {
		String Url=driver.getCurrentUrl();
		System.out.println("The current URL is: "+Url);
		@Nullable
		String title = driver.getTitle();
		System.out.println("The Current page title: "+title);
		String handle = driver.getWindowHandle();
		System.out.println("The window handle: "+handle);
	}
	public static void window() throws InterruptedException {
		//1 . Login to Amazon and validate your login.
		WebElement Oselect,Osign,Omail,Ocon,Otp,Owath,Overf,Oselout;
		WebDriverWait Owait=new WebDriverWait(driver, Duration.ofSeconds(20));
		
		Oselect=driver.findElement(By.xpath("//div[@id='nav-link-accountList']"));
		Actions actions=new Actions(driver);
		actions.moveToElement(Oselect).perform();
		
		Osign=driver.findElement(By.xpath("//span[text()='Sign in']"));
		actions.moveToElement(Osign).perform();
		Osign.click();
		
		Omail=driver.findElement(By.xpath("//input[@id='ap_email_login']"));
		actions.moveToElement(Omail).perform();
		Omail.sendKeys("XXXXXXXXXXXX");
		
		Ocon=driver.findElement(By.xpath("//input[@type='submit']"));
		Ocon.click();
		
		Otp=driver.findElement(By.xpath("//input[@id='continue']"));
		Otp.click();
		
		Owath=driver.findElement(By.xpath("//span[@id='secondary_channel_button']"));
		Owath.click();
		
		Thread.sleep(2000);//wait to put OTP  
		
		WebElement element = Owait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='nav-line-1-container']/span")));
		String text = element.getText();
		if (text.contains("logitha")) {
			System.out.println("successfully sign in your Amazon Account: "+text);
		} else {
			System.out.println("Not crated Amazon Account");
		}
		
		
	}
	public static void getCartValue() {
		//2 . Get the Cart value and store it in a Global variable . 
		WebElement cartElement;
		WebDriverWait Owait= new WebDriverWait(driver, Duration.ofSeconds(20));
		Actions actions=new Actions(driver);
		cartElement = Owait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='nav-cart-count-container']/span")));
		actions.moveToElement(cartElement).perform();
		String cart = cartElement.getText();
		cartvalue = Integer.parseInt(cart);
		System.out.println("Current cart item: "+cartvalue);	
	}
	public static void search(String categories,String text) {
		
		//3.Search the product and select the catagory and click on search button.
		WebElement Ocategories,OsearchText,Obutton;
		
		Ocategories=driver.findElement(By.xpath("//select[@id='searchDropdownBox']"));
		Select Oselect=new Select(Ocategories);
		Oselect.selectByVisibleText(categories);
		
		OsearchText=driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
		OsearchText.sendKeys(text);
		
		Obutton=driver.findElement(By.xpath("//input[@type='submit']"));
		Obutton.click();
	}
	
	public static void productSearchResult() {
		//4 . Get the product name and price of 1st resulted product and store it in a variable 
		//div[@class='s-main-slot s-result-list s-search-results sg-row']//div[@role='listitem'][1]//div[@data-cy='title-recipe']//h2
		
		WebElement Oclick, Oproduct,Oprice;
		WebDriverWait Owait= new WebDriverWait(driver, Duration.ofSeconds(40));
		Actions actions=new Actions(driver);
		
		Oclick=driver.findElement(By.xpath("//div[@class='s-main-slot s-result-list s-search-results sg-row']"
				+ "//div[@role='listitem'][1]//div[@data-cy='title-recipe']//h2"));
		actions.moveToElement(Oclick).perform();
		Oclick.click();
		
		Oproduct=Owait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='productTitle']")));
		expectedProductName= Oproduct.getText();
		System.out.println("The product name: "+expectedProductName);
		
		
		
		Oprice=driver.findElement(By.xpath("//span[@class='a-price aok-align-center reinventPricePriceToPayMargin priceToPay']"));
		actions.moveToElement(Oprice).perform();
		expectedProductPrice = Oprice.getText(); 
		System.out.println("The prodout cost: "+expectedProductPrice);
		
		
		
	}
	public static void cartAdded() throws InterruptedException {
		
		WebElement AddCart,GoTocart,CartValidation;
		WebDriverWait Owait= new WebDriverWait(driver, Duration.ofSeconds(40));
		Actions actions=new Actions(driver);
		
		//6.Click on Add to cart button .
		
		AddCart=driver.findElement(By.xpath("//div[@id='desktop_qualifiedBuyBox']//span[@id='submit.add-to-cart']/span//input"));
		Owait.until(ExpectedConditions.elementToBeClickable(AddCart));
		actions.moveToElement(AddCart).perform();
		AddCart.click();
		
		//7. Click on Go To Cart Button 
		Thread.sleep(4000);
		
		//8 . Validate the Cart Value .
		WebElement cartElement = Owait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-cart-count-container']/span")));
		actions.moveToElement(cartElement).perform();
		String cart = cartElement.getText();
		System.out.println("The current Cart iteam: "+cart);
			
	}
	public static void cartValodation() {
		WebElement ProductName,Odelet;
		WebDriverWait Owait= new WebDriverWait(driver, Duration.ofSeconds(40));
		Actions actions=new Actions(driver);
		
		//9. validate the product name and price in cart page.
		ProductName=driver.findElement(By.xpath("//div[@class='a-cardui-body a-scroller-none']//div[@class='sc-item-content-group']/ul/li"));
		actions.moveToElement(ProductName).perform();
		cartProductName=ProductName.getText();
		System.out.println("Product name in cart: " +cartProductName);
		
		WebElement ProductCost = driver.findElement(By.xpath("//div[@class='sc-apex-cart-price']//span[@class='a-price-whole']"));
		actions.moveToElement(ProductCost).perform();
		cartProductPrice=ProductCost.getText();
		System.out.println("Product price in cart: " + cartProductPrice);
		
		//10. Click on Delete link and Validate the Cart Value 
		
		Odelet=driver.findElement(By.xpath("//span[@class='a-icon a-icon-small-trash']"));
		Owait.until(ExpectedConditions.elementToBeClickable(Odelet));
		Odelet.click();
	}
	public static void validateProductDetails() {
	    System.out.println("\n===== VALIDATION START =====");

	    if (cartProductName.equalsIgnoreCase(expectedProductName)) {
	            System.out.println("Name match ");
	        } else System.out.println("Name mismatch");

	        if (cartProductPrice.equalsIgnoreCase(expectedProductPrice)) {
	            System.out.println("Price match");
	        } else System.out.println("Price mismatch");
	    
	}
	//10. Switch to Amazon home page and click on Signout .
	public static void signOut() {
		WebElement Point,Oout;
		WebDriverWait Owait= new WebDriverWait(driver, Duration.ofSeconds(40));
		WebElement element = Owait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='nav-link-accountList']")));
		Actions actions=new Actions(driver);
				actions.moveToElement(element).perform();
				
				Oout=driver.findElement(By.xpath("//span[text()='Sign Out']"));
				Owait.until(ExpectedConditions.elementToBeClickable(Oout));
				actions.moveToElement(Oout).perform();
				Oout.click();
	}
	public static void closeBrowser() {
		driver.quit();
	}
	
}







/**
 * Amazon
=============
1 . Login to Amazon and validate your login .
2 . Get the Cart value and store it in a Global variable .
3 . Search the product and select the catagory and click on search button.
4 . Get the product name and price of 1st resulted product and store it in a variable .
5 . Click on the first product link .
6 . Click on Add to cart button .
7 . Click on Go To Cart Button .
8 . Validate the Cart Value .
9. validate the product name and price in cart page.
10. Click on Delete link and Validate the Cart Value .
11. Switch to Amazon home page and click on Signout .
 */
