package commons;

import static org.testng.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.google.gson.Gson;

import pageobjects.los.PageGeneratorLos;
import pageobjects.los.SaleHomePageObject;
import pageuis.los.BasePageUI;

public class BasePage {

	public static BasePage getBasePage() {
		return new BasePage();
	}

	public void openPageUrl(WebDriver driver, String pageUrl) {
		driver.get(pageUrl);
	}

	public void openNewTab(WebDriver driver, String pageUrl) {
		String openNewTabWithLinkScript = "window.open('" + pageUrl + "','_blank');";
		((JavascriptExecutor) driver).executeScript(openNewTabWithLinkScript);
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
	}

	public void switchOldTab(WebDriver driver) {
		ArrayList<String> tabs1 = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs1.get(0));
	}

	public void switchTabTwo(WebDriver driver) {
		ArrayList<String> tabs1 = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs1.get(1));
	}

	public String getPageTitle(WebDriver driver) {
		return driver.getTitle();
	}

	public String getPageUrl(WebDriver driver) {
		return driver.getCurrentUrl();
	}

	public String getPageSource(WebDriver driver) {
		return driver.getPageSource();
	}

	public Set<Cookie> getAllCookies(WebDriver driver) {
		return driver.manage().getCookies();
	}

	public void setAllCookies(WebDriver driver, Set<Cookie> allCookies) {
		for (Cookie cookie : allCookies) {
			driver.manage().addCookie(cookie);
		}
	}

	public Alert waitForAlertPresence(WebDriver driver) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return explicitWait.until(ExpectedConditions.alertIsPresent());
	}

	public void acceptAlert(WebDriver driver) {
		alert = waitForAlertPresence(driver);
		alert.accept();
		sleepInSecond(2);
	}

	public void cancelAlert(WebDriver driver) {
		alert = waitForAlertPresence(driver);
		alert.dismiss();
	}

	public void sendkeyToAlert(WebDriver driver, String value) {
		alert = waitForAlertPresence(driver);
		alert.sendKeys(value);
	}

	public String getAlertText(WebDriver driver) {
		alert = waitForAlertPresence(driver);
		return alert.getText();
	}

	public void switchToWindowByID(WebDriver driver, String parentID) {
		Set<String> allTabIDs = driver.getWindowHandles();
		for (String id : allTabIDs) {
			if (!id.equals(parentID)) {
				driver.switchTo().window(id);
				break;
			}
		}
	}

	public void switchToWindowByTitle(WebDriver driver, String tabTitle) {
		Set<String> allTabIDs = driver.getWindowHandles();
		for (String id : allTabIDs) {
			driver.switchTo().window(id);
			String actualTitle = driver.getTitle();
			if (actualTitle.equals(tabTitle)) {
				break;
			}
		}
	}

	public void closeAllTabWithoutParent(WebDriver driver, String parentID) {
		Set<String> allTabIDs = driver.getWindowHandles();
		for (String id : allTabIDs) {
			if (!id.equals(parentID)) {
				driver.switchTo().window(id);
				driver.close();
			}
			driver.switchTo().window(parentID);
		}
	}

	public void sleepInSecond(long timeout) {
		try {
			Thread.sleep(timeout * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void backToPage(WebDriver driver) {
		driver.navigate().back();
	}

	public void refreshToPage(WebDriver driver) {
		driver.navigate().refresh();
	}

	public void forwardToPage(WebDriver driver) {
		driver.navigate().forward();
	}

	public WebElement getElement(WebDriver driver, String locator) {
		return driver.findElement(getByXpath(locator));
	}

	public WebElement getElement(WebDriver driver, String locator, String... params) {
		return driver.findElement(getByXpath(getDynamicLocator(locator, params)));
	}

	public List<WebElement> getElements(WebDriver driver, String locator) {
		return driver.findElements(getByXpath(locator));
	}

	public By getByXpath(String locator) {
		return By.xpath(locator);
	}

	public String getDynamicLocator(String locator, String... params) {
		return String.format(locator, (Object[]) params);
	}

	public void clickToElement(WebDriver driver, String locator) {
		getElement(driver, locator).click();
	}

	public void clickToElement(WebDriver driver, String locator, String... params) {
		getElement(driver, getDynamicLocator(locator, params)).click();
	}

	public void clickElementIfDisplay(WebDriver driver, String locator) {
		try {
			overrideGlobalTimeout(driver, shortTimeout);
			if (getElement(driver, locator).isDisplayed()) {
				clickToElement(driver, locator);
				overrideGlobalTimeout(driver, longTimeout);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendkeyToElement(WebDriver driver, String locator, String value) {
		getElement(driver, locator).clear();
		getElement(driver, locator).sendKeys(value);
	}

	public void sendkeyToElement(WebDriver driver, String locator, String value, String... params) {
		locator = getDynamicLocator(locator, params);
		getElement(driver, locator).clear();
		getElement(driver, locator).sendKeys(value);
	}

	public int getElementSize(WebDriver driver, String locator) {
		return getElements(driver, locator).size();
	}

	public int getElementSize(WebDriver driver, String locator, String... params) {
		locator = getDynamicLocator(locator, params);
		return getElements(driver, locator).size();
	}

	public void selectDropdownByText(WebDriver driver, String locator, String itemText) {
		select = new Select(getElement(driver, locator));
		select.selectByVisibleText(itemText);
	}

	public void selectDropdownByText(WebDriver driver, String locator, String itemText, String... params) {
		locator = getDynamicLocator(locator, params);
		select = new Select(getElement(driver, locator));
		select.selectByVisibleText(itemText);
	}

	public String getSelectedItemDropdown(WebDriver driver, String locator) {
		select = new Select(getElement(driver, locator));
		return select.getFirstSelectedOption().getText();
	}

	public String getSelectedItemDropdown(WebDriver driver, String locator, String... params) {
		locator = getDynamicLocator(locator, params);
		select = new Select(getElement(driver, locator));
		return select.getFirstSelectedOption().getText();
	}

	public boolean isDropdownMultiple(WebDriver driver, String locator) {
		select = new Select(getElement(driver, locator));
		return select.isMultiple();
	}

	public void selectItemInCustomDropdown(WebDriver driver, String parentLocator, String childItemLocator,
			String expectedItem) {
		getElement(driver, parentLocator).click();
		sleepInSecond(1);

		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		List<WebElement> allItems = explicitWait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(childItemLocator)));

		for (WebElement item : allItems) {
			if (item.getText().trim().equals(expectedItem)) {
				jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
				sleepInSecond(1);

				item.click();
				sleepInSecond(1);
				break;
			}
		}
	}

	public String getElementAttribute(WebDriver driver, String locator, String attriuteName) {
		return getElement(driver, locator).getAttribute(attriuteName);
	}

	public String getElementAttribute(WebDriver driver, String locator, String attriuteName, String... params) {
		return getElement(driver, getDynamicLocator(locator, params)).getAttribute(attriuteName);
	}

	public String getElementColor(WebDriver driver, String locator) {
		return getElement(driver, locator).getCssValue("background-color");
	}

	public String getElementText(WebDriver driver, String locator) {
		return getElement(driver, locator).getText();
	}

	public String getElementText(WebDriver driver, String locator, String... params) {
		return getElement(driver, getDynamicLocator(locator, params)).getText().trim();
	}

	public void checkToCheckboxOrRadio(WebDriver driver, String locator) {
		if (!isElementSelected(driver, locator)) {
			getElement(driver, locator).click();
		}
	}

	public void checkToCheckboxOrRadio(WebDriver driver, String locator, String... params) {
		locator = getDynamicLocator(locator, params);
		if (!isElementSelected(driver, locator)) {
			getElement(driver, locator).click();
		}
	}

	public void uncheckToCheckbox(WebDriver driver, String locator) {
		if (isElementSelected(driver, locator)) {
			getElement(driver, locator).click();
		}
	}

	public boolean isElementDisplayed(WebDriver driver, String locator) {
		try {
			// Displayed : Visible on UI + In DOM
			// undisplayed : invisible on ui + in dom
			return getElement(driver, locator).isDisplayed();
		} catch (Exception e) {
			// undisplayed : invisible on ui + not in dom
			e.printStackTrace();
			return false;
		}
	}

	public boolean isElementUndisplayed(WebDriver driver, String locator) {
		System.out.println("Start time =" + new Date().toString());
		overrideGlobalTimeout(driver, shortTimeout);
		List<WebElement> elements = getElements(driver, locator);
		overrideGlobalTimeout(driver, longTimeout);

		if (elements.size() == 0) {
			System.out.println("Element not in DOM and not visible on UI");
			System.out.println("End time = " + new Date().toString());
			return true;
		} else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
			System.out.println("Element in DOM but not visible on UI");
			System.out.println("End time = " + new Date().toString());
			return true;
		} else {
			System.out.println("Element in DOM and visible on UI");
			return false;
		}
	}

	public boolean isElementUndisplayed(WebDriver driver, String locator, String... params) {
		System.out.println("Start time =" + new Date().toString());
		overrideGlobalTimeout(driver, shortTimeout);
		List<WebElement> elements = getElements(driver, getDynamicLocator(locator, params));
		overrideGlobalTimeout(driver, longTimeout);

		if (elements.size() == 0) {
			System.out.println("Element not in DOM and not visible on UI");
			System.out.println("End time = " + new Date().toString());
			return true;
		} else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
			System.out.println("Element in DOM but not visible on UI");
			System.out.println("End time = " + new Date().toString());
			return true;
		} else {
			System.out.println("Element in DOM and visible on UI");
			return false;
		}
	}

	public void overrideGlobalTimeout(WebDriver driver, long timeout) {
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}

	public boolean isElementDisplayed(WebDriver driver, String locator, String... params) {
		return getElement(driver, getDynamicLocator(locator, params)).isDisplayed();
	}

	public boolean isElementEnabled(WebDriver driver, String locator) {
		return getElement(driver, locator).isEnabled();
	}

	public boolean isElementEnabled(WebDriver driver, String locator, String... params) {
		return getElement(driver, getDynamicLocator(locator, params)).isEnabled();
	}

	public boolean isElementSelected(WebDriver driver, String locator) {
		return getElement(driver, locator).isSelected();
	}

	public boolean isElementSelected(WebDriver driver, String locator, String... params) {
		return getElement(driver, getDynamicLocator(locator, params)).isSelected();
	}

	public WebDriver switchToIframeByElement(WebDriver driver, String locator) {
		return driver.switchTo().frame(getElement(driver, locator));
	}

	public WebDriver switchToDefaultContent(WebDriver driver) {
		return driver.switchTo().defaultContent();
	}

	public void hoverToElement(WebDriver driver, String locator) {
		action = new Actions(driver);
		action.moveToElement(getElement(driver, locator)).perform();
	}

	public void hoverToElement(WebDriver driver, String locator, String... params) {
		action = new Actions(driver);
		action.moveToElement(getElement(driver, getDynamicLocator(locator, params))).perform();
	}

	public void doubleClickToElement(WebDriver driver, String locator) {
		action = new Actions(driver);
		action.doubleClick(getElement(driver, locator)).perform();
	}

	public void rightClickToElement(WebDriver driver, String locator) {
		action = new Actions(driver);
		action.contextClick(getElement(driver, locator)).perform();
	}

	public void dragAndDropElement(WebDriver driver, String sourceLocator, String tagetLocator) {
		action = new Actions(driver);
		action.dragAndDrop(getElement(driver, sourceLocator), getElement(driver, tagetLocator)).perform();
	}

	public void pressKeyToElement(WebDriver driver, String locator, Keys key) {
		action = new Actions(driver);
		action.sendKeys(getElement(driver, locator), key).perform();
	}

	public void pressKeyToElement(WebDriver driver, String locator, String key) {
		action = new Actions(driver);
		action.sendKeys(getElement(driver, locator), key).perform();
	}

	public void pressKeyToElement(WebDriver driver, String locator, Keys key, String... params) {
		action = new Actions(driver);
		locator = getDynamicLocator(locator, params);
		action.sendKeys(getElement(driver, locator), key).perform();
	}

	public Object executeForBrowser(WebDriver driver, String javaScript) {
		jsExecutor = (JavascriptExecutor) driver;
		return jsExecutor.executeScript(javaScript);
	}

	public String getInnerText(WebDriver driver) {
		jsExecutor = (JavascriptExecutor) driver;
		return (String) jsExecutor.executeScript("return document.documentElement.innerText;");
	}

	public boolean areExpectedTextInInnerText(WebDriver driver, String textExpected) {
		jsExecutor = (JavascriptExecutor) driver;
		String textActual = (String) jsExecutor
				.executeScript("return document.documentElement.innerText.match('" + textExpected + "')[0]");
		return textActual.equals(textExpected);
	}

	public void scrollToBottomPage(WebDriver driver) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public void navigateToUrlByJS(WebDriver driver, String url) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.location = '" + url + "'");
	}

	public void highlightElement(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		WebElement element = getElement(driver, locator);
		String originalStyle = element.getAttribute("style");
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
				"border: 2px solid red; border-style: dashed;");
		sleepInSecond(1);
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
				originalStyle);
	}

	public void clickToElementByJS(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].click();", getElement(driver, locator));
	}

	public void scrollToElement(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getElement(driver, locator));
	}

	public void sendkeyToElementByJS(WebDriver driver, String locator, String value) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", getElement(driver, locator));
	}

	public void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');",
				getElement(driver, locator));
	}

	public boolean isJQueryAjaxLoadedSuccess(WebDriver driver) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		jsExecutor = (JavascriptExecutor) driver;
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				return (Boolean) jsExecutor.executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
			}
		};
		return explicitWait.until(jQueryLoad);
	}

	public boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		jsExecutor = (JavascriptExecutor) driver;

		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
			}
		};

		return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
	}

	public String getElementValidationMessage(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getElement(driver, locator));
	}

	public boolean isImageLoaded(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		boolean status = (boolean) jsExecutor.executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
				getElement(driver, locator));
		if (status) {
			return true;
		} else {
			return false;
		}
	}

	public void waitForElementVisible(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(locator)));
	}

	public void waitForElementVisible(WebDriver driver, String locator, String... params) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait
				.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(getDynamicLocator(locator, params))));
	}

	public void waitForAllElementVisible(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locator)));
	}

	public void waitForElementClickable(WebDriver driver, String locator, String... params) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(getDynamicLocator(locator, params))));
	}

	public void waitForElementClickable(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(locator)));
	}

	public void waitForElementInvisible(WebDriver driver, String locator, String... params) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait
				.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(getDynamicLocator(locator, params))));
	}

	public void waitForElementInvisible(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(locator)));
	}

	public void waitForElementInvisibleLongTime1(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(locator)));
	}
	
	public SaleHomePageObject loginToSystem(WebDriver driver, String userName, String password) {
		waitForElementVisible(driver, BasePageUI.EDT_USER_LOGIN);
		sendkeyToElement(driver, BasePageUI.EDT_USER_LOGIN, userName);
		waitForElementVisible(driver, BasePageUI.EDT_PASSWORD_LOGIN);
		sendkeyToElement(driver, BasePageUI.EDT_PASSWORD_LOGIN, password);
		clickToElement(driver, BasePageUI.BTN_LOGIN);
		return PageGeneratorLos.getSaleHomePageObject(driver);
	}

	public void connect(String... database) {
		hostName = database[0];
		dbName = database[1];
		userName = database[2];
		passWord = database[3];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
			conn = DriverManager.getConnection(connectionURL, userName, passWord);

			System.out.println("Connect success to DB: " + database);
		} catch (Exception e) {
			System.out.println("Can not connect to DB: " + database);
		}
	}

	public boolean isConnect(String... database) {
		hostName = database[0];
		dbName = database[1];
		userName = database[2];
		passWord = database[3];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
			conn = DriverManager.getConnection(connectionURL, userName, passWord);
			if (conn != null && !conn.isClosed()) {
				System.out.println("Connect success to DB: " + database);
				return true;
			} else {
				System.out.println("Can not connect to DB: " + database);
				return false;
			}

		} catch (Exception e) {
			System.out.println("Can not connect to DB: " + database);
			return false;
		}
	}

	/**
	 * lay du lieu tra ve tu cau lenh truy van, luu vao List 2 chieu
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public String[][] getData(String sql, String... params) {
		String[][] data = new String[100][100];
		try {
			System.out.println("Thuc thi cau lenh " + sql);
			ResultSet rs = executeSql(sql);
			int i = 0;
			if (rs != null) {
				while (rs.next()) {
					String[] currData = new String[params.length];
					for (int j = 0; j < params.length; j++) {
						System.out.println("Get collumn name " + params[j]);
						currData[j] = rs.getString(params[j]);
					}
					data[i] = currData;
					i++;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			e.printStackTrace();
			return data;
		}
		return data;
	}

	public List<List<Object>> getDataObject(String sql, String... params) {

		List<List<Object>> result = new ArrayList<List<Object>>();
		try {
			System.out.println("Thuc thi cau lenh " + sql);
			ResultSet rs = executeSql(sql);
			int i = 0;
			if (rs != null) {
				while (rs.next()) {
					List<Object> data = new ArrayList<Object>();// 1 ban ghi gom nhieu cot
					for (int j = 0; j < params.length; j++) {
						// System.out.println("Get collumn name " + params[j]);
						data.add(rs.getObject(params[j])); // lay data cua tung cot
					}
					result.add(data); // lay toan bo ban ghi
					i++;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			e.printStackTrace();
			return result;
		}
		return result;
	}
//	/**
//	 * So sanh du lieu tu cau lenh SQL tra ve voi mang du lieu
//	 * @param sql
//	 * @param params
//	 * @param expects
//	 * @return
//	 */
//	public boolean compareResult(String sql, String[] params, String[][] expects) {
//		String[][] data = getData(sql, params);
//		if (data != null){
//			if (expects.length > 0 && data.length > 0) {
//				for (int i = 0; i < expects.length; i++) {
//					if (expects[i] != null && data[i] != null) {
//						for (int j = 0; j < expects[i].length; j++) {
//							String s = StringUtils.safeTrim(data[i][j]);
//							System.out.println("Data get from DB: " + s);
//							String d = StringUtils.safeTrim(expects[i][j]);
//							System.out.println("Data expect: " + d);
//							if (d != null){
//								if (s != null && d != ""){
//								
//									if (!d.equalsIgnoreCase(s)) {
//										System.out.println("data not map " + s + " and " + d);
//										return false;
//									}
//								} else if (d.equals("")){
//									if (s != null){
//										return false;
//									}
//								}															
//							}
//						}
//					}
//				}
//			} else {
//				System.out.println("size of expects: " + expects.length);
//				System.out.println("size of data: " + data.length);
//				return false;
//			}
//		} else {
//			System.out.println("Data get from DB is null");
//			return false;
//		}
//		return true;
//	}

	/* thuc thi sql */
	public ResultSet executeSql(String sql) {
		try {
			if (conn == null) {
				connect();
			}
			Statement state = conn.createStatement();
			System.out.println("Thuc thi cau lenh " + sql);
			ResultSet rs = state.executeQuery(sql);
			// state.executeQuery("commit");
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Khong thuc thi duoc cau lenh: " + sql);
			Assert.assertFalse(false);
		}
		return null;
	}

	public boolean executeSql1(String sql) {
		boolean rs = false;
		try {
			if (conn == null) {
				connect();
			}
			Statement state = conn.createStatement();
			System.out.println("Thuc thi cau lenh " + sql);
			rs = state.execute(sql);
			// state.executeQuery("commit");
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Khong thuc thi duoc cau lenh: " + sql);
			Assert.assertFalse(false);
		}
		return rs;
	}

	/**
	 * Dem so luong ban ghi co ton tai theo dieu kien cau lenh truy van
	 * 
	 * @param sql
	 * @return
	 */
	public String checkRecordExist(String sql) {
		try {
			System.out.println("Thuc thu cau lenh " + sql);
			ResultSet rs = executeSql(sql);
			while (rs.next()) {
				return rs.getString("count(*)");
			}
		} catch (Exception e) {

		}
		return "";
	}

	// Tách chuỗi
	public String[] shouldMatchRegex(String pattern, String message) {
		String[] result = new String[10];
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(message);
		if (matcher.find()) {
			for (int i = 0; i < matcher.groupCount() + 1; i++) {
				result[i] = matcher.group(i);
			}
		} else {
			Assert.fail("String, patten does not match");
		}
		return result;
	}

	public void verifyContains(String s1, String s2) {
		if (s1 != null && s2 != null) {
			Assert.assertFalse(!s2.contains(s1), "Chuỗi " + s1 + " không nằm trong chuỗi " + s2);
		}
	}

	public String formatDateToString(Date date, String format) {
		DateFormat f = new SimpleDateFormat(format);
		String dat = "";
		try {
			dat = f.format(date);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			System.out.println("Can not convert date");
		}
		System.out.println("dat" + dat);
		return dat;
	}

	public Date formatDate(String format, String date) {
		DateFormat f = new SimpleDateFormat(format);
		Date dat = null;
		try {
			dat = (Date) f.parse(date);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			System.out.println("Can not convert date");
		}
		return dat;
	}

	public Date subtractDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, -days);

		return cal.getTime();
	}

	public String subtractDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.add(Calendar.DATE, days);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(cal.getTime());
	}

	public String getTimeCurrent(String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		return dtf.format(now);
	}

//    public Long subtractTime(Date start, Date end) {
//    	Long diff;
//    	diff = start.getTime()-end.getTime();
//    	diff = TimeUnit.MILLISECONDS.toSeconds(diff);
//    	return diff;
//    }
	public String convertDateTest(String format1, String format2, String oldstring) {
		// String oldstring = "2011-01-18 00:00:00.0";
		Date date = new Date();
		try {
			date = new SimpleDateFormat(format1).parse(oldstring);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String test = new SimpleDateFormat(format2).format(date);
		System.out.println(test); // 2011-01-18

		return test;
	}

	public Long subtractTimeTest(String format, String date2) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));

		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss");

		LocalDateTime dateTime1 = LocalDateTime.parse(dtf.format(now), dtf);
		LocalDateTime dateTime2 = LocalDateTime.parse(date2, dtf);

		long diffInMilli = java.time.Duration.between(dateTime1, dateTime2).toMillis();

		return diffInMilli;
	}

	public int subtractTime(Date start, Date end) {
		Long diff;
		diff = start.getTime() - end.getTime();
		diff = TimeUnit.MILLISECONDS.toSeconds(diff);
		int dis = diff.intValue();
		return dis;
	}

	/**
	 * Dong connection
	 */
	public void close() {
		try {
			if (conn != null) {
				conn.close();
				System.out.println("Close connect to DB");
			}
		} catch (Exception e) {
			System.out.println("Can not close connection to DB");
		}
	}

	public static String read(InputStream input) throws IOException {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
			return buffer.lines().collect(Collectors.joining("\n"));
		}
	}

	public static String[] callWS(String urlText, String data, String... column) {
		String output = "";
		String[] da = new String[100];
		try {
			URL url = new URL(urlText);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("Accept", "text/xml");
			System.out.println("connection: " + connection);
			OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			osw.write(data);
			osw.flush();
			osw.close();
			InputStream in = connection.getInputStream();
			output = read(in);
			System.out.println("output cua WS: " + output);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(output));
			Document doc = builder.parse(src);
			if (column.length > 0) {
				for (int i = 0; i < column.length; i++) {
					if (doc.getElementsByTagName(column[i]).item(0).getTextContent() != null) {
						da[i] = doc.getElementsByTagName(column[i]).item(0).getTextContent();
						System.out.println("Gia tri trong tag " + column[i] + "la: " + da[i]);
					}
				}
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			assertFalse(true, "Co loi khi thuc hien WS");

		}
		return da;
	}

	public static boolean callWS2(String urlText, String token, String method) {
		int responscode = 0;
		try {
			URL url = new URL(urlText);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("Accept", "text/xml");
			connection.setRequestProperty("Authorization", token);
			connection.setRequestMethod(method);
			connection.setFixedLengthStreamingMode(0);
			System.out.println("connection: " + connection);
			// OutputStreamWriter osw = new
			// OutputStreamWriter(connection.getOutputStream());
			responscode = connection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (responscode == 200) ? true : false;
	}

	public static boolean callWS3(String urlText, String token, String method) {
		int responscode = 0;
		try {
			URL url = new URL(urlText);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", token);
			connection.setRequestMethod(method);
			connection.setFixedLengthStreamingMode(0);
			System.out.println("connection: " + connection);
			// OutputStreamWriter osw = new
			// OutputStreamWriter(connection.getOutputStream());
			responscode = connection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (responscode == 200) ? true : false;
	}

	/**
	 * 
	 * @param sql
	 */
	public void executeManySQL(String... sql) {
		if (sql.length > 0) {
			try {
				if (conn == null) {
					connect();
				}
				Statement state = conn.createStatement();
				for (int i = 0; i < sql.length; i++) {
					state.executeQuery(sql[i]);
				}
			} catch (Exception e) {
				System.out.println("Khong thuc hien duoc cau lenh sql " + sql);
			}

		}
	}

	public void executeManySQL1(String... sql) {
		if (sql.length > 0) {
			try {
				if (conn == null) {
					connect();
				}
				Statement state = conn.createStatement();
				for (int i = 0; i < sql.length; i++) {
					state.execute(sql[i]);
				}
			} catch (Exception e) {
				System.out.println("Khong thuc hien duoc cau lenh sql " + sql);
			}

		}
	}

//		public String getResponseFromUrl(String urlText, String method,List<RequestHeaderUrl> header, String body, String key) {
//
//			String value = new String();
//			String responseText = new String();
//			try {
//				URL url = new URL(urlText);
//				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//				connection.setDoOutput(true);
//				//sconnection.setRequestProperty("Accept", "application/json");
//				for(int i =0; i<header.size(); i++) {
//				connection.setRequestProperty(header.get(i).getKey(), header.get(i).getValue());
//				}
//				while(!connection.getRequestMethod().equals(method))
//				     connection.setRequestMethod(method);
//				connection.setDoOutput(true);
//				try (OutputStream os = connection.getOutputStream()) {
//					byte[] input = body.getBytes("utf-8");
//					os.write(input, 0, input.length);
//				}
//				try (BufferedReader br = new BufferedReader(
//						new InputStreamReader(connection.getInputStream(), "utf-8"))) {
//					StringBuilder response = new StringBuilder();
//					String responseLine = null;
//					while ((responseLine = br.readLine()) != null) {
//						response.append(responseLine.trim());
//					}
//					responseText = response.toString();
//				}
//				
//
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			String[] tmp = responseText.split(":");
//	         for(int i =0; i<tmp.length;i++){
//	             if(tmp[i].indexOf(key)>0) {
//	            	 value = new String(tmp[i+1].split(",")[0]);
//	            	 value = new String(value.replace('\"','\0'));
//	                 break;
//	             }
//	         }
//           return value.trim();
//		}

//		public String getResponseFromUrlObject(String urlText, String method,List<RequestHeaderUrl> header, String body, String key) {
//			String value = new String();
//			String responseText = new String();
//			try {
//				URL url = new URL(urlText);
//				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			
//				connection.setRequestProperty("Accept", "application/json, text/plain");
//				for(int i =0; i<header.size(); i++) {
//				connection.setRequestProperty(header.get(i).getKey(), header.get(i).getValue());
//				}
//			
//				connection.setRequestMethod(method);
//				connection.setDoInput(true);
//				connection.setDoOutput(true);
//				if(method == "POST") {
//					
//					try (OutputStream os = connection.getOutputStream()) {
//						byte[] input = body.getBytes("utf-8");
//						os.write(input, 0, input.length);
//					}
//				}
//				try (BufferedReader br = new BufferedReader(
//						new InputStreamReader(connection.getInputStream()))) {
//					StringBuilder response = new StringBuilder();
//					String responseLine = null;
//					while ((responseLine = br.readLine()) != null) {
//						response.append(responseLine.trim());
//					}
//					responseText = response.toString();
//				}
//				
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//           return responseText.trim();
//		}
//		

	public String getItemFromLocalStorage(WebDriver driver, String key) {
		jsExecutor = (JavascriptExecutor) driver;
		return (String) jsExecutor.executeScript(String.format("return window.localStorage.getItem('%s');", key));
	}

	public String getScreenhot(WebDriver driver) {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots" under src
		// folder
		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + "errorCapture_" + dateName
				+ ".png";
		File finalDestination = new File(destination);
		try {
			FileUtils.copyFile(source, finalDestination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destination;
	}

	public String format(long timestampMilliSec, String timezone, String pattern) {
		Instant instant = Instant.ofEpochMilli(timestampMilliSec);
		ZoneId zoneId = ZoneId.of(timezone);
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return zonedDateTime.format(formatter);
	}

	private String hostName;
	private String pt;
	private String dbName;
	private String userName;
	private String passWord;

	private Connection conn;
	private Alert alert;
	private Select select;
	private Actions action;
	private long longTimeout = GlobalConstants.LONG_TIMEOUT;
	private long shortTimeout = GlobalConstants.SHOT_TIMEOUT;
	private long timeout = GlobalConstants.LONG_TIMEOUT;

	private WebDriverWait explicitWait;
	private JavascriptExecutor jsExecutor;

}
