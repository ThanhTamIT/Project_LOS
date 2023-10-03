package pageobjects.los;

import org.openqa.selenium.WebDriver;

public class PageGeneratorLos {
	
	public static CustomerIdentificationPageObject getCustomerIdentificationPage(WebDriver driver) {
		return new CustomerIdentificationPageObject(driver);
	}
	
	public static InformationSalePageObject getInformationSalePageObject(WebDriver driver) {
		return new InformationSalePageObject(driver);
	}
	
	public static NewCaseHomePageObject getNewCaseHomePageObject(WebDriver driver) {
		return new NewCaseHomePageObject(driver);
	}
	
	public static SaleHomePageObject getSaleHomePageObject(WebDriver driver) {
		return new SaleHomePageObject(driver);
	}
	
	public static SaleLoginPageObject getSaleLoginPageObject(WebDriver driver) {
		return new SaleLoginPageObject(driver);
	}

}
