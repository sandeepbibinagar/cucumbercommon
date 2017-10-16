package com.experian.automation.saas.screens;


import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertTrue;


public class SolutionScreen extends Screen {

    @FindBy(xpath = "//div[contains(@class,'ajax_loader')]")
    WebElement ajaxLoader;

    String pageObjectModel;

    public SolutionScreen(TestHarness testHarness, WebHarness webHarness, String pageObjectModel) throws IOException, ConfigurationException {
        super(testHarness, webHarness);
        this.pageObjectModel=pageObjectModel;
    }

    public WebElement getScreenLoader() {
        return ajaxLoader;
    }

    public void selectMenu(String mainMenu, String subMenu, String page) throws IOException {
        selectMainMenu(mainMenu, page);
        selectSubMenu(subMenu, page);
    }

    public void selectMainMenu(String mainMenu, String page) throws IOException {
        JSONArray menuItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='menu-item')].[?(@.text=='" + mainMenu + "')].xpath");
        String xpath = (String) menuItems.get(0);
        WebElement element = waitForElementPresence(By.xpath(xpath));
        clickWithScrollToView(element);
    }

    public void selectSubMenu(String subMenu, String page) throws IOException {

        waitForElementToDisappear(ajaxLoader);
        assertTrue(getWindowTitle().equals(page),"Page loaded: "+page);

        JSONArray subMenuItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='sub-menu-item')].[?(@.text=='" + subMenu + "')].xpath");
        String xpath = (String) subMenuItems.get(0);
        WebElement element = waitForElementPresence(By.xpath(xpath));
        clickWithScrollToView(element);
    }

    public void selectSubMenuSectionItem(String subMenuText, String item, String page) throws IOException {
        JSONArray subMenuItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='sub-menu-item')].[?(@.text=='" + subMenuText + "')].xpath");
        WebElement subMenu = waitForElementPresence(By.xpath((String) subMenuItems.get(0)));
        WebElement sectionItem = subMenu.findElement(By.xpath("..//*[contains(text(),'" + item + "') and @id]"));
        clickWithScrollToView(sectionItem);
    }

    public void verifySelectLabelPairs(String page, Map<String, String> data) throws IOException {
        waitForElementToDisappear(ajaxLoader);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            JSONArray textfieldItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='select')].[?(@.display-text=='" + entry.getKey() + "')].xpath");
            if (textfieldItems.size() == 0) {
                WebElement common = waitForElementPresence(By.xpath("//select[parent::div[parent::div[child::div/label[contains(text(),'" + entry.getKey() + "')]]]]"));
                Select select = new Select(common);
                assertTrue(select.getFirstSelectedOption().getText().equals(entry.getValue()), "Field with title: "+ entry.getKey()+" contains value " + entry.getValue());
            } else {
                String xpath = (String) textfieldItems.get(0);
                WebElement element = waitForElementPresence(By.xpath(xpath));
                Select select = new Select(element);
                assertTrue(select.getFirstSelectedOption().getText().equals(entry.getValue()), "Field with title: "+ entry.getKey()+" contains value " + entry.getValue());
            }
        }
    }

    public void verifyInputLabelPairs(String page, Map<String, String> data) throws IOException {
        waitForElementToDisappear(ajaxLoader);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            JSONArray textfieldItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='textfield')].[?(@.display-text=='" + entry.getKey() + "')].xpath");
            if (textfieldItems.size() == 0) {
                WebElement common = waitForElementPresence(By.xpath("//input[parent::div[parent::div[child::div/label[contains(text(),'" + entry.getKey() + "')]]]]"));
                assertTrue(common.getAttribute("value").equals(entry.getValue()),"Field with title: "+ entry.getKey()+" contains value " + entry.getValue());
            } else {
                String xpath = (String) textfieldItems.get(0);
                WebElement element = waitForElementPresence(By.xpath(xpath));
                String inputValue = element.getAttribute("value");
                assertTrue(entry.getValue().equals(inputValue),"Field with title: "+ entry.getKey()+" contains value " + entry.getValue());
            }
        }
    }

    public void fillConsequtiveInputs(String labelText, List<String> data,String page) throws IOException {
        List<WebElement> commonRoot = waitForElementsPresence(By.xpath("//input[parent::div[parent::div[child::div/label[contains(text(),'" + labelText + "')]]]]"));
        if(commonRoot.size()==data.size()){
            for(int j=0;j<commonRoot.size();j++){
                commonRoot.get(j).sendKeys(data.get(j));
            }
        }else{
            JSONArray textfieldItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='textfield')].[?(@.display-text=='" + labelText + "')].xpath");
            throw new NoSuchElementException("Cannot find inputs with label: "+labelText+" available input ID's:"+textfieldItems);
        }
    }

    public void clickButton(String text, String page) throws IOException {

        waitForElementToDisappear(ajaxLoader);
        assertTrue(getWindowTitle().equals(page),"Page loaded: "+page);

        JSONArray buttons = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='button')].id");

        List<WebElement> buttonsList = new ArrayList<>();

        List<WebElement> buttonsWithSameText = new ArrayList<>();

        for (int j = 0; j < buttonsList.size(); j++) {
            for (int k = 0; k < buttonsList.size(); k++) {
                if (j != k) {
                    if (buttonsList.get(j).getText().equals(buttonsList.get(k).getText())) {
                        buttonsWithSameText.add(buttonsList.get(j));
                    }
                }
            }
        }

        if (buttonsWithSameText.size() != 0) {
            for (WebElement el : buttonsWithSameText) {
                if (el.getText().equals(text)) {
                    throw new IllegalArgumentException("There is another button with text: " + text + "\n Available buttons by ID: " + buttons);
                }
            }
        }

        WebElement button = waitForElementPresence(By.xpath("//button[contains(text(),'" + text + "')]"));
        button.click();
    }


    public void clickOnElementWithTitleInTable(String title, String header, String searchHeader, String searchValue, String tableId) {
        List<WebElement> tableHeaders = waitForElementsPresence(By.xpath("//table[@id='" + tableId + "']//th"));
        List<WebElement> tableCells = waitForElementsPresence(By.xpath("//table[@id='" + tableId + "']//tr/td"));
        List<String> headersText = new ArrayList<>();
        List<String> cellsText = new ArrayList<>();

        for (WebElement el : tableHeaders) {
            headersText.add(el.getText().trim());
        }

        for (WebElement el : tableCells) {
            cellsText.add(el.getText().trim());
        }

        Integer searchHeaderIndex = headersText.indexOf(searchHeader);
        Integer elementHeaderIndex = headersText.indexOf(header);
        int row = 0;
        boolean found = false;
        for (int k = searchHeaderIndex; k < cellsText.size(); k = k + headersText.size()) {
            if (cellsText.get(k).equals(searchValue)) {
                found = true;
                break;
            }
            row++;
        }
        if (!found) {
            throw new NoSuchElementException("Cannot find row with value: " + searchValue + " in column: " + searchHeader);
        } else {
            Integer elementPosition = ((row + 1) * (elementHeaderIndex + 1)) - 1;
            WebElement element = tableCells.get(elementPosition).findElement(By.xpath(".//*[@title='" + title + "']"));
            clickWithScrollToView(element);
        }
    }

    public void verifyDataInTable(String selector, String value, List<List<String>> data) {
        List<WebElement> tableHeaders = new ArrayList<>();
        List<WebElement> tableCells = new ArrayList<>();
        if (selector.equals("id")) {
            tableHeaders = waitForElementsPresence(By.xpath("//*[@id='" + value + "']//th"));
            tableCells = waitForElementsPresence(By.xpath("//*[@id='" + value + "']//tr/td"));
        }
        if (selector.equals("class")) {
            tableHeaders = waitForElementsPresence(By.xpath("//*[@class='" + value + "']//th"));
            tableCells = waitForElementsPresence(By.xpath("//*[@class='" + value + "']//tr/td"));
        }

        findDataInTable(data, tableHeaders, tableCells, true, false, false);
    }

    public void setDropdownValueByLabel(String label, String value, String page) throws IOException {
        waitForElementToDisappear(ajaxLoader);
        assertTrue(getWindowTitle().equals(page),"Page loaded: "+page);

        JSONArray pageObject = JsonPath.read(this.pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='select'||@.type=='combobox')].[?(@.display-text=='" + label + "')].xpath");
        JSONArray objectType = JsonPath.read(this.pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='select'||@.type=='combobox')].[?(@.display-text=='" + label + "')].type");
        JSONArray allSelects = JsonPath.read(this.pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='select'||@.type=='combobox')].xpath");

        if (pageObject.size() == 0) {
            if (webHarness.driver.findElement(By.xpath("//select[parent::div[parent::div[child::div/label[contains(text(),'" + label + "')]]]]")) != null) {
                new Select(webHarness.driver.findElement(By.xpath("//select[parent::div[parent::div[child::div/label[contains(text(),'" + label + "')]]]]"))).selectByVisibleText(value);
            } else {
                throw new NoSuchElementException("There is no element with label " + label + " in the object file. Available objects:" + allSelects);
            }
        }
        if (pageObject.size() > 1) {
            if (webHarness.driver.findElement(By.xpath("//select[parent::div[parent::div[child::div/label[contains(text(),'" + label + "')]]]]")) != null) {
                new Select(webHarness.driver.findElement(By.xpath("//select[parent::div[parent::div[child::div/label[contains(text(),'" + label + "')]]]]"))).selectByVisibleText(value);
            } else {
                throw new NoSuchElementException("There are several elements with label " + label + " in the object file. Available objects:" + pageObject);
            }
        }
        String xpath = (String) pageObject.get(0);
        WebElement element = waitForElementPresence(By.xpath(xpath));
        if ((!element.isDisplayed()) && objectType.get(0).equals("select")) {
            WebElement option = element.findElement(By.xpath("./following-sibling::*[1]//*[contains(text(),'" + value + "')]"));
            clickWithScrollToView(option);
        } else {
            new Select(element).selectByVisibleText(value);
        }
    }

    public void setTextfieldValueByLabel(String label, String value, String page) throws IOException {
        waitForElementToDisappear(ajaxLoader);
        JSONArray textfieldItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='textfield')].[?(@.display-text=='" + label + "')].xpath");
        if (textfieldItems.size() == 0) {
            WebElement common = waitForElementPresence(By.xpath("//input[parent::div[parent::div[child::div/label[contains(text(),'" + label + "')]]]]"));
            common.sendKeys(value);
        } else {
            String xpath = (String) textfieldItems.get(0);
            waitForElementPresence(By.xpath(xpath));
            typeWithClear(webHarness.driver.findElement(By.xpath((String) textfieldItems.get(0))), value);
        }
    }

    public void setDatepickerValueByLabel(String label, String value, String page) throws IOException {
        JSONArray textfieldItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='jquerydatepicker')].[?(@.display-text=='" + label + "')].xpath");
        WebElement element = waitForElementPresence(By.xpath((String) textfieldItems.get(0)));
        typeWithClear(element, value);
        element.sendKeys(Keys.TAB);

    }

    public void selectTabWithTitle(String title, String page) throws IOException {

        waitForElementToDisappear(ajaxLoader);
        assertTrue(getWindowTitle().equals(page),"Page loaded: "+page);

        JSONArray textfieldItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='title')].[?(@.text=='" + title + "')].xpath");
        String xpath = (String) textfieldItems.get(0);
        WebElement element = waitForElementPresence(By.xpath(xpath));
        clickWithScrollToView(element);
    }

    public void clickButtonBy(String type, String value) {
        if (type.equals("id")) {
            getElementById(value).click();
        }
        if (type.equals("value")) {
            getElementByValue(value).click();
        }

    }

    public void selectDynamicComboBoxById(String id, String text) {
        new Select(getElementById(id)).selectByVisibleText(text);
    }

    public void verifyLabelInputPairInSection(String labelText, Map<String, String> elementsText) throws InterruptedException {
        List<WebElement> allMapLabels = new ArrayList<>();
        for (Map.Entry<String, String> entry : elementsText.entrySet()) {
            allMapLabels.add(waitForElementPresence(By.xpath("//label[contains(text(),'" + entry.getKey() + "')]")));
        }

        WebElement label = waitForElementPresence(By.xpath("//label[contains(text(),'" + labelText + "')]"));

        WebElement root;
        if (allMapLabels.size() == 1) {
            root = findCommonRootOfElements(label, allMapLabels.get(0));
        } else {
            WebElement commonRootOfList = webHarness.driver.findElement(By.xpath("//html"));
            if (allMapLabels.size() == 2) {
                commonRootOfList = findCommonRootOfElements(allMapLabels.get(0), allMapLabels.get(1));
            }

            if (allMapLabels.size() > 2) {
                commonRootOfList = findCommonRootOfList(allMapLabels, allMapLabels.get(0), 1);
            }
            root = findCommonRootOfElements(commonRootOfList, label);
        }

        List<WebElement> rootInnerLeaf = root.findElements(By.xpath("./descendant::*[local-name()='label' or local-name()='input']"));
        List<String> rootInnerLeafTexts = new ArrayList<>();
        for (WebElement el : rootInnerLeaf) {
            if (el.getTagName().equals("label")) {
                rootInnerLeafTexts.add(el.getText());
            }
            if (el.getTagName().equals("input")) {
                rootInnerLeafTexts.add(el.getAttribute("value"));
            }
        }

        assertTrue(rootInnerLeafTexts.get(0).equals(labelText));

        int index = 1;
        for (Map.Entry<String, String> entry : elementsText.entrySet()) {
            if (entry.getKey().equals(rootInnerLeafTexts.get(index))) {
                assertTrue(entry.getValue().equals(rootInnerLeafTexts.get(index + 1)));
            }
            index++;
        }
    }

    public void clickOnElementWithText(String text) {
        WebElement button = waitForElementPresence(By.xpath("//*[contains(text(),'" + text + "')]"));
        clickWithScrollToView(button);
    }

    public WebElement getElementById(String id) {
        WebElement element = waitForElementPresence(By.id(id));
        return element;
    }

    public WebElement getElementByValue(String value) {
        WebElement element = waitForElementPresence(By.xpath("//*[@value='" + value + "']"));
        return element;
    }

    public WebElement findCommonRootOfElements(WebElement element1, WebElement element2) {
        List<WebElement> descendatsOfElement1 = element1.findElements(By.xpath("./descendant::*"));
        List<WebElement> descendatsOfElement2 = element2.findElements(By.xpath("./descendant::*"));
        if (descendatsOfElement1.contains(element2)) {
            return element1;
        } else if (descendatsOfElement2.contains(element1)) {
            return element2;
        } else {
            WebElement temp = element1.findElement(By.xpath(".."));
            List<WebElement> descendantsOfUpperElement = temp.findElements(By.xpath("./descendant::*"));
            if (descendantsOfUpperElement.contains(element2)) {
                descendantsOfUpperElement = null;
                return temp;
            }
            return findCommonRootOfElements(temp, element2);
        }
    }

    public WebElement findCommonRootOfList(List<WebElement> list, WebElement firstElement, Integer iterationStartNum) {
        WebElement root = firstElement;
        List<WebElement> descendants = firstElement.findElements(By.xpath("./descendant::*"));
        if (descendants.contains(list.get(iterationStartNum))) {
            firstElement = root;
            iterationStartNum++;
            if (iterationStartNum.equals(list.size())) {
                return root;
            }
            return findCommonRootOfList(list, firstElement, iterationStartNum);
        } else {
            firstElement = firstElement.findElement(By.xpath(".."));
            return findCommonRootOfList(list, firstElement, iterationStartNum);
        }

    }
}


