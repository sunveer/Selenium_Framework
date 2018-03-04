package pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import util.BaseClass;

public class InputForms extends BaseClass{

	WebDriver driver;
	
	@FindBy(id="user-message") public WebElement enterMessage;
	@FindBy(css=".btn-default") public List<WebElement> buttons;
	@FindBy(id="display") public WebElement yourMsg;
	
	@FindBy(id="sum1") public WebElement enterA;
	@FindBy(id="sum2") public WebElement enterB;
	@FindBy(id="displayvalue") public WebElement total;
	
	
	public InputForms(WebDriver driver) {
		this.driver = driver; 
		PageFactory.initElements(driver, this);
	}
}
