package scripts;

import org.testng.annotations.Test;

import pages.InputForms;
import pages.MenuList;
import util.BaseClass;
import util.CheckPoint;

public class SeleniumEasy extends BaseClass{
	
	@Test
	public void testVaidLogIn() throws Exception {
		test=createTest("seleniumeasyWebsite Launching");
		MenuList menu = new MenuList(driver);
		menu.clickSimpleForm();
		InputForms inputform = new InputForms(driver);
		waitForElement(inputform.enterMessage);
		sendKeys(inputform.enterMessage,"Hellow India");
		click(inputform.buttons.get(0));
		waitForElement(inputform.yourMsg);
		String msg = inputform.yourMsg.getText();
		verifyEquals(msg, "Hellow World");
		int value1= 5;
		int value2= 5;
		int sum= value1+value2;
		inputform.enterA.sendKeys(Integer.toString(value1));
		sendKeys(inputform.enterB, Integer.toString(value2));
		click(inputform.buttons.get(1));
		waitForElement(inputform.total);
		int actual = Integer.parseInt(getText(inputform.total));
		assertValue(actual, sum);
	}

}
