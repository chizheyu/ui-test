package org.ChiTest;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Wait tool class.  Provides Wait methods for an elements, and AJAX elements to load.  
 * It uses WebDriverWait (explicit wait) for waiting an element or javaScript.  
 * 
 * To use implicitlyWait() and WebDriverWait() in the same test, 
 * we would have to nullify implicitlyWait() before calling WebDriverWait(), 
 * and reset after it.  This class takes care of it. 
 * 
 * 
 * Generally relying on implicitlyWait slows things down 
 * so use WaitTool�s explicit wait methods as much as possible.
 * Also, consider (DEFAULT_WAIT_4_PAGE = 0) for not using implicitlyWait 
 * for a certain test.
 * 
 * @author Chon Chung, Mark Collin, Andre, Tarun Kumar 
 * 
 *
 * Copyright [2012] [Chon Chung]
 * 
 * Licensed under the Apache Open Source License, Version 2.0  
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 */
public class WaitTool {

	/** Default wait time for an element. 7  seconds. */ 
	public static final int DEFAULT_WAIT_4_ELEMENT = 7; 
	/** Default wait time for a page to be displayed.  12 seconds.  
	 * The average webpage load time is 6 seconds in 2012. 
	 * Based on your tests, please set this value. 
	 * "0" will nullify implicitlyWait and speed up a test. */ 
	public static final int DEFAULT_WAIT_4_PAGE = 12;

    public void setDriver(EventFiringWebDriver driver) {
        this.driver = driver;
    }
    private static Logger waitlog = Logger.getLogger("friend_log");
    private EventFiringWebDriver driver;

	

	/**
	  * Wait for the element to be present in the DOM, and displayed on the page. 
	  * And returns the first WebElement using the given method.
	  * 
	  * @param "WebDriver" The driver object to be used
	  * @param "By"	selector to find the element
	  * @param "int"	The time in seconds to wait until returning a failure
	  *
	  * @return WebElement	the first WebElement using the given method, or null (if the timeout is reached)
	  */
	public WebElement waitForElement( final By by, int timeOutInSeconds) {
		WebElement element;
		try{
			//To use WebDriverWait(), we would have to nullify implicitlyWait(). 
			//Because implicitlyWait time also set "driver.findElement()" wait time.  
			//info from: https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/6VO_7IXylgY
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
			  
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds); 
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            waitlog.info("wait " + by.toString() + " successfully ");
            return element; //return the element
		} catch (Exception e) {
            System.out.println("the element "+by.toString()+ " may not visible");
            //e.printStackTrace();
            return null;
		}

	}
    public WebElement waitForElement( final WebElement webElement, int timeOutInSeconds) {
        WebElement element;
        try{
            //To use WebDriverWait(), we would have to nullify implicitlyWait().
            //Because implicitlyWait time also set "driver.findElement()" wait time.
            //info from: https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/6VO_7IXylgY
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait.until(ExpectedConditions.visibilityOf(webElement));
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            waitlog.info("wait "+ webElement.toString()+" successfully ");
            return element; //return the element
        }
        catch (Exception e) {
            waitlog.info("the element "+webElement.toString()+ " may not visible");
            return null;
        }
    }
    public WebElement waitForElement( final WebElement webElement, long sleepInMills) {
        WebElement element;
        try{
            //To use WebDriverWait(), we would have to nullify implicitlyWait().
            //Because implicitlyWait time also set "driver.findElement()" wait time.
            //info from: https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/6VO_7IXylgY
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            WebDriverWait wait = new WebDriverWait(driver, 0,sleepInMills);
            element = wait.until(ExpectedConditions.visibilityOf(webElement));
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            waitlog.info("wait "+ webElement.toString()+" successfully ");
            return element; //return the element
        }catch (NoSuchElementException e){
            waitlog.info("NoSuchElementException "+webElement.toString());
            return null;
        }
        catch (Exception e) {
            waitlog.info("the element "+webElement.toString()+ " may not visible");
            return null;
        }
    }
    public WebElement waitForElementClickable( final By by, int timeOutInSeconds) {
        WebElement element;
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait.until(ExpectedConditions.elementToBeClickable(by));
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return element; //return the element
        } catch (Exception e) {
            //assertEquals("the element " + by.toString() + " may not clickable", 1,2 );
            //e.printStackTrace();
            waitlog.info("the element "+by.toString()+ " may not clickable");
            return null;
        }

    }
    public WebElement waitForElementClickable( WebElement element, int timeOutInSeconds) {

        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return element;
        } catch (Exception e) {
            waitlog.info("the element "+ element.toString() +" may not clickable");
            return null;
            //e.printStackTrace();
        }

    }
	
	
	

	/**
	  * Wait for the element to be present in the DOM, regardless of being displayed or not.
	  * And returns the first WebElement using the given method.
	  *

	  * @return WebElement	the first WebElement using the given method, or null (if the timeout is reached)
	  */
	public  WebElement waitForElementPresent( final By by, int timeOutInSeconds) {
		WebElement element; 
		try{
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
			
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
			
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return element; //return the element
		} catch (Exception e) {
            waitlog.info("the element "+ by.toString() +" may not present");
            return null;
            //e.printStackTrace();
		}
	}
    public  WebElement waitForElementPresent( final By by) {
        WebElement element;
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, 5);
            element = wait.until(ExpectedConditions.presenceOfElementLocated(by));

            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return element; //return the element
        } catch (Exception e) {
            waitlog.info("the element "+ by.toString() +" may not present");
            return null;
            //e.printStackTrace();
        }

    }
	

	/**
	  * Wait for the List<WebElement> to be present in the DOM, regardless of being displayed or not.
	  * Returns all elements within the current page DOM. 

	  * @return List<WebElement> all elements within the current page DOM, or null (if the timeout is reached)
	  */
	public  List<WebElement> waitForListElementsPresent( final By by, int timeOutInSeconds) {
		List<WebElement> elements; 
		try{	
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
			  
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds); 
			wait.until((new ExpectedCondition<Boolean>() {
	            @Override
	            public Boolean apply(WebDriver driverObject) {
	                return areElementsPresent(by);
	            }
	        }));
			
			elements = driver.findElements(by); 
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return elements; //return the element	
		} catch (Exception e) {
            waitlog.info("the elements may not present");
            //e.printStackTrace();
		} 
		return null; 
	}

	/**
	  * Wait for an element to appear on the refreshed web-page.
	  * And returns the first WebElement using the given method.
	  *
	  * This method is to deal with dynamic pages.
	  * 
	  * Some sites I (Mark) have tested have required a page refresh to add additional elements to the DOM.  
	  * Generally you (Chon) wouldn't need to do this in a typical AJAX scenario.
	  * 

	  * @return WebElement	the first WebElement using the given method, or null(if the timeout is reached)
	  * 
	  * @author Mark Collin 
	  */
	 public  WebElement waitForElementRefresh (final By by,  int timeOutInSeconds) {
		WebElement element; 
		try{	
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
		        new WebDriverWait(driver, timeOutInSeconds) {
		        }.until(new ExpectedCondition<Boolean>() {

		            @Override
		            public Boolean apply(WebDriver driverObject) {
		                driverObject.navigate().refresh(); //refresh the page ****************
		                return isElementPresentAndDisplay( by);
		            }
		        });
		    element = driver.findElement(by);
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return element; //return the element
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null; 
	 }
	 
	/**
	  * Wait for the Text to be present in the given element, regardless of being displayed or not.
	  *

	  * @return boolean 
	  */
	public boolean waitForTextPresent(final By by, final String text, int timeOutInSeconds) {
		boolean isPresent = false; 
		try{	
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
	        new WebDriverWait(driver, timeOutInSeconds) {
	        }.until(new ExpectedCondition<Boolean>() {
	
	            @Override
	            public Boolean apply(WebDriver driverObject) {
	            	return isTextPresent( by, text); //is the Text in the DOM
	            }
	        });
	        isPresent = isTextPresent( by, text);
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return isPresent; 
		} catch (Exception e) {
			e.printStackTrace();
            return false;
        }

	}
	



	/** 
	 * Waits for the Condition of JavaScript.  
	 *
	 *

	 * @return boolean true or false(condition fail, or if the timeout is reached)
	 **/
    public    boolean waitForContentChange( final String cssSelectorForContent,final int num,final String verifyInfo,int timeOutInSeconds) {
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return !driverObject.findElements(By.cssSelector(cssSelectorForContent)).get(num).getText().equals(verifyInfo);
                }
            });
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return !driver.findElements(By.cssSelector(cssSelectorForContent)).get(num).getText().equals(verifyInfo);
        } catch (Exception e) {
            waitlog.error("the text has not changed for " + verifyInfo  );
            return false;
        }
    }
    public  boolean waitForAttributeChange( final String cssSelectorForContent, final String attributeName,final String verifyInfo,int timeOutInSeconds) {
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return !driverObject.findElement(By.cssSelector(cssSelectorForContent)).getAttribute(attributeName).equals(verifyInfo);
                }
            });
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return !driver.findElement(By.cssSelector(cssSelectorForContent)).getAttribute(attributeName).equals(verifyInfo);
        } catch (Exception e) {
            waitlog.error("the attribute has not changed for " + verifyInfo  );
            return false;
        }
    }
    public  boolean waitForAttributeChange( final String cssSelectorForContent,final String attributeName,
                                            final String verifyInfo, final int itemNum,int timeOutInSeconds) {
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return !driverObject.findElements(By.cssSelector(cssSelectorForContent)).get(itemNum).getAttribute(attributeName).equals(verifyInfo);
                }
            });
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return !driver.findElements(By.cssSelector(cssSelectorForContent)).get(itemNum).getAttribute(attributeName).equals(verifyInfo);
        } catch (Exception e) {
            waitlog.error("the text has not changed for " + verifyInfo  );
            return false;
        }
    }
    public  boolean waitForItemCountChange( final String cssSelectorForItem, final int originalCount,int timeOutInSeconds) {
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {

                    return driverObject.findElements(By.cssSelector(cssSelectorForItem)).size()!=originalCount;
                }
            });
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_ELEMENT, TimeUnit.SECONDS); //reset implicitlyWait
            return driver.findElements(By.cssSelector(cssSelectorForItem)).size()!=originalCount;
        }catch (NoSuchElementException e){
            if(originalCount != 0){
                return true;
            }
            return false;
        }
        catch (Exception e) {
            waitlog.error("the itemCount"+ cssSelectorForItem +" has not changed" );
            return false;
        }
    }
    public  boolean waitForContentNotNull( final String cssSelectorForContent,final int num, int timeOutInSeconds) {
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return !(driverObject.findElements(By.cssSelector(cssSelectorForContent)).get(num).getText().trim().equals("")
                            || driverObject.findElements(By.cssSelector(cssSelectorForContent)).get(num).getText() == null);
                }
            });
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return !driver.findElements(By.cssSelector(cssSelectorForContent)).get(num).getText().trim().equals("");
        } catch (Exception e) {
            waitlog.info("the text of " + cssSelectorForContent + " is null" );
            return false;
        }
    }
    public  boolean waitForElementDisappear( final String cssSelectorForContent,final int num,final String childElement,int timeOutInSeconds) {
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return driverObject.findElements(By.cssSelector(cssSelectorForContent)).get(num).findElement(By.cssSelector(childElement)).getText().trim().equals("")
                            || driverObject.findElements(By.cssSelector(cssSelectorForContent)).get(num).findElement(By.cssSelector(childElement)).getText() == null;
                }
            });
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return !driver.findElements(By.cssSelector(cssSelectorForContent)).get(num).getText().trim().equals("");
        } catch (Exception e) {
            waitlog.error("the text of " + cssSelectorForContent + " is not" );
            return false;
        }
    }
    public  boolean waitForElementCountChange( final String cssSelectorForContent,final int num,final String childElement,final int countSize,int timeOutInSeconds) {
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return driverObject.findElements(By.cssSelector(cssSelectorForContent)).get(num).findElements(By.cssSelector(childElement)).size() != countSize;
                }
            });
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return !driver.findElements(By.cssSelector(cssSelectorForContent)).get(num).getText().trim().equals("");
        } catch (Exception e) {
            waitlog.error("the text of " + cssSelectorForContent + " is null" );
            return false;
        }
    }
    public  boolean waitForContentNotNull( final String cssSelectorForContent,int timeOutInSeconds) throws TimeoutException {
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return !(driverObject.findElement(By.cssSelector(cssSelectorForContent)).getText().trim().equals("") ||
                            driverObject.findElement(By.cssSelector(cssSelectorForContent)).getText() == null);
                }
            });
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return !(driver.findElement(By.cssSelector(cssSelectorForContent)).getText().trim().equals("")|| driver.findElement(By.cssSelector(cssSelectorForContent)).getText() == null);
        } catch (NoSuchElementException e) {
            waitlog.error("the text is null" );
            return false;
        }

    }


	public  boolean waitForJavaScriptCondition(WebDriver driver, final String javaScript, int timeOutInSeconds) {
		boolean jscondition;
		try{	
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
	        new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
	            @Override
	            public Boolean apply(WebDriver driverObject) {
                   waitlog.info( ((JavascriptExecutor) driverObject).executeScript(javaScript) );

                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
	            }
	        });
	        jscondition =  (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);

			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return jscondition; 
		} catch (Exception e) {
            waitlog.error("the JavaScript condition "+ javaScript +" may not true ");
            return false;
		} 

	}

    public  boolean waitForJavaScriptCondition( final String javaScript, int timeOutInSeconds) {
        boolean jscondition ;
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    // waitlog.info( "apply " + ((JavascriptExecutor) driverObject).executeScript(javaScript) );
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
                }
            });
            jscondition =  (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);

            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return jscondition;
        } catch (Exception e) {
            waitlog.error("the JavaScript condition "+ javaScript +" may not true ");
            return false;
            //assertEquals("the JavaScript condition may not true wait failure",1,2);
        }

    }



	
	/** Waits for the completion of Ajax jQuery processing by checking "return jQuery.active == 0" condition.  
	 *

	 * @return boolean true or false(condition fail, or if the timeout is reached)
	 * */
	public  boolean waitForJQueryProcessing( int timeOutInSeconds){
		boolean jQcondition = false; 
		try{	
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
	        new WebDriverWait(driver, timeOutInSeconds) {
	        }.until(new ExpectedCondition<Boolean>() {
	
	            @Override
	            public Boolean apply(WebDriver driverObject) {
	            	return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
	            }
	        });
	        jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return jQcondition; 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jQcondition; 
    }
	

	/**
	 * Coming to implicit wait, If you have set it once then you would have to explicitly set it to zero to nullify it -
	 */
	public  void nullifyImplicitWait() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
	} 
	

	/**
	 * Set driver implicitlyWait() time. 
	 */
	public  void setImplicitWait(int waitTime_InSeconds) {
		driver.manage().timeouts().implicitlyWait(waitTime_InSeconds, TimeUnit.SECONDS);  
	} 
	
	/**
	 * Reset ImplicitWait.  
	 * To reset ImplicitWait time you would have to explicitly 
	 * set it to zero to nullify it before setting it with a new time value. 
	 */
	public static void resetImplicitWait(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
		driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
	} 
	

	/**
	 * Reset ImplicitWait.  

	 */
	public  void resetImplicitWait( int newWaittime_InSeconds) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
		driver.manage().timeouts().implicitlyWait(newWaittime_InSeconds, TimeUnit.SECONDS); //reset implicitlyWait
	} 
		    

     /**
	   * Checks if the text is present in the element. 
       * 


	   * @return true or false
	   */
	private  boolean isTextPresent( By by, String text)
	{
		try {
				return driver.findElement(by).getText().contains(text);
		} catch (NullPointerException e) {
				return false;
		}
	}
		

	/**
	 * Checks if the elment is in the DOM, regardless of being displayed or not.
	 * 

	 * @return boolean
	 */
	private  boolean isElementPresent( By by) {
		try {
			driver.findElement(by);//if it does not find the element throw NoSuchElementException, which calls "catch(Exception)" and returns false; 
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	

	/**
	 * Checks if the List<WebElement> are in the DOM, regardless of being displayed or not.
	 * 
	 * @return boolean
	 */
	private  boolean areElementsPresent( By by) {
		try {
			driver.findElements(by); 
			return true; 
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Checks if the elment is in the DOM and displayed. 
	 * 
	 *@return boolean
	 */
	private  boolean isElementPresentAndDisplay( By by) {
		try {			
			return driver.findElement(by).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	

	
 }
/*
 * References: 
 * 1. Mark Collin's post on: https://groups.google.com/forum/?fromgroups#!topic/webdriver/V9KqskkHmIs%5B1-25%5D
 * 	  Mark's code inspires me to write this class. Thank you! Mark. 
 * 2. Andre, and Tarun Kumar's post on: https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/6VO_7IXylgY  
 * 3. Explicit and Implicit Waits: http://seleniumhq.org/docs/04_webdriver_advanced.html
 * 
 * Note: 
 * 1. Instead of creating new WebDriverWait() instance every time in each methods, 
 *    I tried to reuse a single WebDriverWait() instance, but I found and tested 
 *    that creating 100 WebDriverWait() instances takes less than one millisecond. 
 *    So, it seems not necessary.  
 */
