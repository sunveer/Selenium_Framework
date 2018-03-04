package pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import util.BaseClass;

public class MenuList extends BaseClass {

WebDriver driver;
	
	
	@FindBy(css = "i.glyphicon-chevron-right+a") public List<WebElement> menuList;
	@FindBy(xpath = "//a[text()='Simple Form Demo']") public List<WebElement> simpleForm;
	@FindBy(id = "btn_basic_example") public WebElement startpractice;
	
	public MenuList(WebDriver driver) {
		this.driver = driver; 
		PageFactory.initElements(driver, this);
	}
	
	public void clickSimpleForm(){
		click(startpractice);
		elementToBeClickable(menuList.get(0));
		click(menuList.get(0));
		waitForElement(simpleForm.get(1));
		click(simpleForm.get(1));
	}
}
