package com.los.newcase;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import commons.BaseTest;
import pageobjects.los.PageGeneratorLos;
import pageobjects.los.SaleHomePageObject;
import pageobjects.los.SaleLoginPageObject;
import pageuis.los.SaleHomePageUI;
import reportconfig.ExtentTestManager;

public class CreateNewCase extends BaseTest {
	WebDriver driver;
	SaleLoginPageObject loginPage;
	SaleHomePageObject homePage;
	String account, password, frontIDCartIMG, frontIDCartText, backIDCartIMG, backIDCartText, fullFaceIMG, fullFaceText,
			iDCardIssue, mobilePhone, temporaryAddressProvince, temporaryAddressDistrict, productCategory,
			schemeProduct, billType, billOwner, customerCodeBill, billAmount1, billAmount2, billAmount3, loanConsumer,
			loanTerm, applicationDate, saleDecision, pOADocument, customerAndStaffPhoto, customerPhotoInsideHouse,
			customerPhotoFrontHouse, serviceBill;

	@Parameters({ "brower", "url" })
	@BeforeClass
	public void beforeClass(String browserName, String appUrl) {
		driver = getBrowerDriver(browserName, appUrl);
		loginPage = PageGeneratorLos.getSaleLoginPageObject(driver);
		account = "sale1";
		password = "It123456";
		homePage = loginPage.loginToSystem(driver, account, password);
		homePage = PageGeneratorLos.getSaleHomePageObject(driver);
	}

	@Test(priority = 1)
	public void Los_Sale_CheckLoginSuccess(Method method) {
		ExtentTestManager.startTest(method.getName(), "Los_Sale_CheckLoginSuccess");
		ExtentTestManager.getTest().log(Status.INFO, "Test ...");
		Assert.assertTrue(homePage.getElementText(driver, SaleHomePageUI.ACCOUNT_SALE).contains(account));

	}

}
