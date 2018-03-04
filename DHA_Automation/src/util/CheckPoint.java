package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class CheckPoint {
	
	private static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();

	// Assertions - If fails scripts will abort. 
	/**
	 * String Assertion
	 * @param actual
	 * @param expected
	 */
	public static void assertText(String actual,String expected) {
		 if(actual.equals(expected)){
			 Assert.assertEquals(actual, expected);
			 BaseClass.test.info("<B> <span style='color:blue;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
			 BaseClass.test.log(Status.PASS,
						MarkupHelper.createLabel("Expected and Actual are equal", ExtentColor.GREEN));
		 }
		 else {
			 BaseClass.test.info("<B> <span style='color:ORANGERED;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
			 BaseClass.test.log(Status.FAIL,
				MarkupHelper.createLabel("Expected and Actual texts are mismatch: ", ExtentColor.RED));
			 Assert.assertEquals(actual, expected);
		}
	   }
	
	/**
	 * Check for the found text contains the given sub string. 
	 * @param found (getText- From application)
	 * @param subStr (expected text)
	 */
	public static void assertTextContains(String found,String subStr) {
		 if(found.contains(subStr)){
			 Assert.assertEquals(true, found.contains(subStr));
			 BaseClass.test.info("<B> <span style='color:blue;'>"+"Found : ["+found+"] contains given String: ["+subStr+"]");
			 BaseClass.test.log(Status.PASS,
						MarkupHelper.createLabel("Expected text contains given string(Actual)", ExtentColor.GREEN));
		 }
		 else {
			 BaseClass.test.info("<B> <span style='color:ORANGERED;'>"+"Found : ["+found+"] contains given String: ["+subStr+"]");
			 BaseClass.test.log(Status.FAIL,
				MarkupHelper.createLabel("Expected and Actual texts are mismatch: ", ExtentColor.RED));
			 Assert.assertEquals(true, found.contains(subStr));
		}
	   }
	 
	 /**
	 * Integer Assertions
	 * @param actual
	 * @param expected
	 */
	public static void assertValue(int actual,int expected) {
		 if(actual==expected){
			 Assert.assertEquals(actual, expected);
			 BaseClass.test.info("<B> <span style='color:DARKGREEN;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
			 BaseClass.test.log(Status.PASS,
						MarkupHelper.createLabel("Expected and Actual are equal", ExtentColor.GREEN));
		 }
		 else {
			 BaseClass.test.info("<B> <span style='color:ORANGERED;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
			 BaseClass.test.log(Status.FAIL,
				MarkupHelper.createLabel("Expected and Actual texts mismatch: ", ExtentColor.RED));
			 Assert.assertEquals(actual, expected);
		}
	    }

	
    /**
     * Check for the boolean condition
     * @param condition
     */
    public static void assertTrue(boolean condition) {
    	Assert.assertTrue(condition);
    }
    
    // here is isDisplay() , isselected(), isEnabled() will come
    
    public static void assertTrue(boolean condition, String message) {
    	Assert.assertTrue(condition, message);
    }
    
    public static void assertFalse(boolean condition) {
    	Assert.assertFalse(condition);
    }
    
    public static void assertFalse(boolean condition, String message) {
    	Assert.assertFalse(condition, message);
    }
    
    public static void assertEquals(boolean actual, boolean expected) {
    	Assert.assertEquals(actual, expected);
    }
    
    public static void assertEquals(Object actual, Object expected) {
    	Assert.assertEquals(actual, expected);
    }
    
    public static void assertEquals(Object[] actual, Object[] expected) {
    	Assert.assertEquals(actual, expected);
    }
    
    public static void assertEquals(Object actual, Object expected, String message) {
    	Assert.assertEquals(actual, expected, message);
    }
    
    public static void verifyTrue(boolean condition) {
    	try {
    		assertTrue(condition);
    	} catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyTrue(boolean condition, String message) {
    	try {
    		assertTrue(condition, message);
    	} catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyFalse(boolean condition) {
    	try {
    		assertFalse(condition);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }
    
    public static void verifyFalse(boolean condition, String message) {
    	try {
    		assertFalse(condition, message);
    	} catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyEquals(boolean actual, boolean expected) {
    	try {
    		assertEquals(actual, expected);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }

    public static void verifyEquals(Object actual, Object expected)  {
    	try {
    		assertEquals(actual, expected);
    		//BaseClass.test.info("<B> <span style='color:GREEN;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
    		BaseClass.test.log(Status.PASS,
					MarkupHelper.createLabel("Actual:["+actual+"],Expectted:["+expected+"]", ExtentColor.GREEN));
		} catch(Throwable e) {
			BaseClass base =  new BaseClass();
    		addVerificationFailure(e);
    		BaseClass.test.info("<B> <span style='color:ORANGERED;'>"+"Actual : ["+actual+"] , Expectted : ["+expected+"]");
    		BaseClass.test.log(Status.WARNING,
					MarkupHelper.createLabel("Expected and Actual texts mismatch", ExtentColor.AMBER));
    		try {
    			BaseClass.test.log(Status.WARNING, "Expected and Actual are mismatch",
    		MediaEntityBuilder.createScreenCaptureFromPath(base.captureScreenshot(BaseClass.driver, "verification")).build());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
    
    public static void verifyEquals(Object[] actual, Object[] expected) {
    	try {
    		assertEquals(actual, expected);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }

    public static void fail(String message) {
    	Assert.fail(message);
    }
    
	public static List<Throwable> getVerificationFailures() {
		List<Throwable> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<Throwable>() : verificationFailures;
	}
	
	private static void addVerificationFailure(Throwable e) {
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}
	
}
