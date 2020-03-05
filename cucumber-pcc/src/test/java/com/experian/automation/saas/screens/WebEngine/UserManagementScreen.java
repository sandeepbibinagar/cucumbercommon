package com.experian.automation.saas.screens.WebEngine;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class UserManagementScreen extends Screen {

  private String window = "myWindow";

  @FindBy(xpath = "//div[@role='tablist']/h3/a")
  public List<WebElement> tabsList;

  @FindBy(xpath = "//div[@role='tabpanel' and @aria-hidden='false']/ul/li/a")
  public List<WebElement> tabsListItems;

  @FindBy(xpath = "//thead/tr/th[@class='profileColHeader ' or not(@class)]")
  public List<WebElement> businessRulesTableHeaderCells;

  @FindBy(xpath = "//td[@class='deny']")
  public List<WebElement> listOfDeniedPermissions;

  @FindBy(xpath = "//a[contains(text(),' Allow all')]")
  public WebElement allowAllButton;

  @FindBy(xpath = "//table[@id='spTable']//tbody/tr/td")
  public List<WebElement> businessRulesTableRowCells;

  @FindBy(xpath = "//a[@title='Edit']")
  public WebElement editButton;

  @FindBy(xpath = " //button[child::span[contains(text(),'Ok')]]")
  public WebElement okButton;

  public UserManagementScreen(WebHarness webHarness) {
    super(webHarness);
    switchToWindow(window);
  }

  public void selectTab(String tab) {
    waitForElements(tabsList);
    clickWithScrollToView(getElementByText(tabsList, tab));
  }

  public void selectTabItem(String tabItem) {
    waitForElements(tabsListItems);
    clickWithScrollToView(getElementByText(tabsListItems, tabItem));
  }

  public void setProcessRules(String permission, String feature) {
    System.out.println("Feature: " + feature);
    Integer headerIndex = -1;
    Integer rowIndex = -1;
    Integer elementToClickPosition = -1;

    for (int headerCell = 0; headerCell <= businessRulesTableHeaderCells.size(); headerCell++) {
      if (businessRulesTableHeaderCells.get(headerCell).getText().equals(permission)) {
        headerIndex = headerCell;
        break;
      }
    }

    for (int rowCellIndex = 0; rowCellIndex <= businessRulesTableRowCells.size(); rowCellIndex++) {
      if (businessRulesTableRowCells.get(rowCellIndex).getText().equals(feature)) {
        rowIndex = rowCellIndex;
        break;
      }
    }
    elementToClickPosition = headerIndex + rowIndex;
    if (headerIndex < 0 || rowIndex < 0) {
      throw new NoSuchElementException("No such element at row: " + rowIndex + " position:" + headerIndex);
    } else {
      String oldClassAttribute = businessRulesTableRowCells.get(elementToClickPosition).getAttribute("class");
      businessRulesTableRowCells.get(elementToClickPosition).click();
      waitForElementAttributeChange(businessRulesTableRowCells.get(elementToClickPosition), "class", oldClassAttribute);
    }
  }

}
