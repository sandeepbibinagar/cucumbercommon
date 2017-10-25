package com.experian.automation.saas.screens;


import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.ImagesOperations;
import com.experian.automation.screens.Screen;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertTrue;


public class SolutionScreen extends Screen {

    @FindBy(xpath = "//div[contains(@class,'ajax_loader')]")
    WebElement ajaxLoader;

    String pageObjectModel;

    public SolutionScreen(TestHarness testHarness, WebHarness webHarness, String pageObjectModel) throws IOException, ConfigurationException {
        super(testHarness, webHarness);
        this.pageObjectModel = pageObjectModel;
    }

    public WebElement getScreenLoader() {
        return ajaxLoader;
    }

    /**
     * The method is used to find objects in the PageObjectModel by their label and to return them as web elements
     *
     * @param page The title of the page where the required web element resides
     * @param type The type of the web element which we search for
     * @param text The label of the element which we search for
     */

    private List<WebElement> getPageObjects(String page, String type, String text) {
        JSONArray pages = JsonPath.read(this.pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].id");
        JSONArray pageObjects = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='" + type + "')].[?(@.display-text=='" + text + "')].xpath");
        if (pages.size() > 1) {
            boolean duplicatePages = true;
            for (int k = 1; k < pages.size(); k++) {
                if (!pages.get(0).equals(pages.get(k))) {
                    duplicatePages = false;
                    break;
                }
            }
            if (duplicatePages) {
                JSONArray samePages = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')]");
                pageObjects = JsonPath.read(samePages.toJSONString(), "$[0].components.*[?(@.type=='" + type + "')].[?(@.display-text=='" + text + "')].xpath");
            }
        }

        List<WebElement> elements = new ArrayList<>();

        for (int j = 0; j < pageObjects.size(); j++) {
            try {
                WebElement element = waitForElementPresence(By.xpath((String) pageObjects.get(j)));
                elements.add(element);
            } catch (NoSuchElementException | TimeoutException exception) {
                exception.getMessage();
            }
        }

        return elements;
    }

    /**
     * The method is used to check the type of certain web element in the PageObject model
     *
     * @param label The label of the element for which we want to check the type in the PageObjectModel
     * @param page  The title of the page where the required web element resides
     */

    public String getPageObjectTypeByLabel(String label, String page) {
        JSONArray pageObjects = JsonPath.read(this.pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.display-text=='" + label + "')].type");
        JSONArray pages = JsonPath.read(this.pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].id");
        if (pageObjects.size() > 1) {
            boolean duplicatePages = true;
            for (int k = 1; k < pages.size(); k++) {
                if (!pages.get(0).equals(pages.get(k))) {
                    duplicatePages = false;
                    break;
                }
            }
            if (duplicatePages) {
                JSONArray samePages = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')]");
                pageObjects = JsonPath.read(samePages.toJSONString(), "$[0].components.*[?(@.display-text=='" + label + "')].type");
            }
        }

        if (pageObjects.size() == 0) {
            throw new NoSuchElementException("Cannot find element with label: " + label + ".List of available elements: " + pageObjects);
        }
        return (String) pageObjects.get(0);
    }

    /**
     * The method is used to get all elements from given type on a page and return them as List
     *
     * @param type The type of the elements we want to get get from the PageObjectModel
     * @param page The title of the page where the required web element resides
     */

    private List<WebElement> getAllPageObjectsByType(String page, String type) {
        JSONArray pageObjects = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='" + type + "')].xpath");
        JSONArray pages = JsonPath.read(this.pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].id");
        if (pageObjects.size() > 1) {
            boolean duplicatePages = true;
            for (int k = 1; k < pages.size(); k++) {
                if (!pages.get(0).equals(pages.get(k))) {
                    duplicatePages = false;
                    break;
                }
            }
            if (duplicatePages) {
                JSONArray samePages = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')]");
                pageObjects = JsonPath.read(samePages.toJSONString(), "$[0].components.*[?(@.type=='" + type + "')].xpath");
            }
        }

        List<WebElement> elements = new ArrayList<>();
        for (int j = 0; j < pageObjects.size(); j++) {
            try {
                WebElement element = waitForElementPresence(By.xpath((String) pageObjects.get(j)));
                elements.add(element);
            } catch (NoSuchElementException | TimeoutException exception) {
                exception.getMessage();
            }
        }

        return elements;
    }

    /**
     * The method is used to check the value of an element for the purpose of verification
     *
     * @param page          The title of the page where the required web element resides
     * @param type          The type of the elements we want to get get from the PageObjectModel
     * @param displayText   The label of the element
     * @param expectedValue The expected value of the element
     */

    public boolean verifyFieldValue(String page, String type, String displayText, String expectedValue) {
        if (!type.equals("dropdown")) {
            return getPageObjects(page, type, displayText).get(0).getAttribute("value").equals(expectedValue);
        } else {
            return new Select(getPageObjects(page, type, displayText).get(0)).getFirstSelectedOption().getText().equals(expectedValue);
        }
    }

    /**
     * The method is used to select menu items.
     *
     * @param menu The path for main menu and sub menu items in format MainMenu/SubMenu/SubSubMenu
     * @param page The title of the page where the required web element resides
     */

    public void selectMenu(String menu, String page) {
        Pattern pattern = Pattern.compile("[^\\/]+");
        Matcher matcher = pattern.matcher(menu);
        int position = 0;
        while (matcher.find()) {
            if (position != 0) {
                WebElement subMenuElement = getPageObjects(page, "sub-menu-item", matcher.group().trim()).get(0);
                clickWithScrollToView(subMenuElement);
            } else {
                WebElement mainMenuElement = getPageObjects(page, "menu-item", matcher.group().trim()).get(0);
                clickWithScrollToView(mainMenuElement);
            }
            position++;
        }
    }

    /**
     * The method is used to select sub menu when there are different
     * sections with sub-menu items which have the same text
     *
     * @param sectionText The text of the menu section
     * @param item        The text of the sub-menu item element
     * @param page        The title of the page where the required web element resides
     */
    public void selectSubMenuSectionItem(String sectionText, String item, String page) throws IOException {
        WebElement subMenu = getPageObjects(page, "sub-menu-item", sectionText).get(0);
        WebElement sectionItem = subMenu.findElement(By.xpath("..//*[contains(text(),'" + item + "') and @id]"));
        clickWithScrollToView(sectionItem);
    }

    /**
     * The method is used to fill consecutive inputs after a label
     *
     * @param labelText The text of the label for which the inputs has to be filled
     * @param data      A list with the values for the inputs ordered as in the UI
     * @param page      The title of the page where the required web element resides
     */
    public void fillMultipleInputs(String labelText, List<String> data, String page) throws IOException {
        List<WebElement> commonRoot = waitForElementsPresence(By.xpath("//input[parent::div[parent::div[child::div/label[contains(text(),'" + labelText + "')]]]]"));
        if (commonRoot.size() == data.size()) {
            for (int j = 0; j < commonRoot.size(); j++) {
                commonRoot.get(j).sendKeys(data.get(j));
            }
        } else {
            List<WebElement> textfieldItems = getPageObjects(page, "textfield", labelText);
            throw new NoSuchElementException("Cannot find inputs with label: " + labelText + " available input ID's:" + textfieldItems);
        }
    }

    /**
     * The method is used to click on a button by its text
     *
     * @param text The text of the button which has to be clicked
     * @param page The title of the page where the required web element resides
     */
    public void clickButton(String text, String page) throws IOException {

        waitForElementToDisappear(ajaxLoader);
        assertTrue(getWindowTitle().equals(page), "Page loaded: " + page);

        List<WebElement> buttonsList = getAllPageObjectsByType(page, "button");
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
                    throw new IllegalArgumentException("There is another button with text: " + text + "\n Available buttons by ID: " + buttonsList);
                }
            }
        }

        WebElement button = waitForElementPresence(By.xpath("//button[contains(text(),'" + text + "')]"));
        button.click();
    }

    /**
     * The method is used to click on element in some column inside a table
     * based on value in the same row in different column inside the table
     *
     * @param title        The title of the element which has to be clicked
     * @param header       The header of the table where the element resides
     * @param searchHeader The header in the table where the element which we use for search resides
     * @param searchValue  The value of the element which we use for search
     * @param locatorValue The value of the locator element (id|class|vale)
     */
    public void clickOnElementWithTitleInTable(String title, String header, String searchHeader, String searchValue, String locator, String locatorValue) {
        List<WebElement> tableHeaders = waitForElementsPresence(By.xpath("//table[@" + locator + "='" + locatorValue + "']//th"));
        List<WebElement> tableCells = waitForElementsPresence(By.xpath("//table[@" + locator + "='" + locatorValue + "']//tr/td"));
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
            WebElement element = tableCells.get(elementPosition).findElement(By.xpath(".//*[@title='" + title + "' or text()='" + title + "']"));
            clickWithScrollToView(element);
        }
    }

    /**
     * The method is used to verify the values in the cells in a table
     *
     * @param selector The type of attribute of the table headers which is used for locating
     * @param value    The value of attribute of the table headers which is used for locating
     * @param data     The table data which has to be verified
     */
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

    /**
     * The method is used to set the values for dropdown fields in given page
     *
     * @param label The field label
     * @param value The field value to be set
     * @param page  The title of the page where the element resides
     */
    public void setDropdownValueByLabel(String label, String value, String page) throws IOException {
        waitForElementToDisappear(ajaxLoader);
        assertTrue(getWindowTitle().equals(page), "Page loaded: " + page);

        String elementType = getPageObjectTypeByLabel(label, page);
        List<WebElement> pageObjects = getPageObjects(page, elementType, label);
        if (pageObjects.size() == 0) {
            throw new NoSuchElementException("There is no element with label " + label);
        }

        if (pageObjects.size() > 1) {
            boolean isSameElement = true;
            for (int k = 1; k < pageObjects.size(); k++) {
                if (!pageObjects.get(0).equals(pageObjects.get(k))) {
                    isSameElement = false;
                    break;
                }
            }
            if (!isSameElement) {
                throw new IllegalArgumentException("There is more than one element with label " + label);
            }
        }

        WebElement element = pageObjects.get(0);
        if ((!element.isDisplayed())) {
            WebElement option = element.findElement(By.xpath("./following-sibling::*[1]//*[contains(text(),'" + value + "')]"));
            clickWithScrollToView(option);
        } else {
            new Select(element).selectByVisibleText(value);
        }
    }

    /**
     * The method is used to set the values for text fields in given page
     *
     * @param label The field label
     * @param value The field value to be set
     * @param page  The title of the page where the element resides
     */
    public void setTextfieldValueByLabel(String label, String value, String page) throws IOException {
        waitForElementToDisappear(ajaxLoader);
        List<WebElement> textfieldItems = getPageObjects(page, "textfield", label);
        if (textfieldItems.size() > 1) {
            throw new IllegalArgumentException("There is more than one field with label: " + label);
        }
        if (textfieldItems.size() == 0) {
            throw new NoSuchElementException("There is no textfield with label: " + label + ".List of available fields:" + getAllPageObjectsByType(page, "textfield"));
        }
        typeWithClear(textfieldItems.get(0), value);
    }

    /**
     * The method is used to set the values for datepickers in given page
     *
     * @param label The field label
     * @param value The field value to be set
     * @param page  The title of the page where the element resides
     */
    public void setDatepickerValueByLabel(String label, String value, String page) throws IOException {
        WebElement element = getPageObjects(page, "datepicker", label).get(0);
        typeWithClear(element, value);
        element.sendKeys(Keys.TAB);
    }

    /**
     * The method is used to select a tab in given page by the tab title
     *
     * @param title The title of the tab to be selected
     * @param page  The title of the page where the element resides
     */
    public void selectTabWithTitle(String title, String page) throws IOException {

        waitForElementToDisappear(ajaxLoader);
        assertTrue(getWindowTitle().equals(page), "Page loaded: " + page);

        JSONArray textfieldItems = JsonPath.read(pageObjectModel, "$.pages.*[?(@.title=='" + page + "')].components.*[?(@.type=='title')].[?(@.text=='" + title + "')].xpath");
        String xpath = (String) textfieldItems.get(0);
        WebElement element = waitForElementPresence(By.xpath(xpath));
        clickWithScrollToView(element);
    }

    /**
     * The method is used to click a button by some of its attributes
     *
     * @param type  The attribute used for location
     * @param value The value of the attribute used for location
     */
    public void clickButtonBy(String type, String value) {
        if (type.equals("id")) {
            getElementById(value).click();
        }
        if (type.equals("value")) {
            getElementByValue(value).click();
        }

    }

    public void clickOnLinkWithText(String text) {
        WebElement link = waitForElementPresence(By.xpath("//*[contains(text(),'" + text + "')]"));
        clickWithScrollToView(link);
    }

    public WebElement getElementById(String id) {
        WebElement element = waitForElementPresence(By.id(id));
        return element;
    }

    public WebElement getElementByValue(String value) {
        WebElement element = waitForElementPresence(By.xpath("//*[@value='" + value + "']"));
        return element;
    }

    public WebElement getElementByName(String name) {
        WebElement element = waitForElementPresence(By.xpath("//*[@name='" + name + "']"));
        return element;
    }

    public WebElement getElementByClass(String value) {
        WebElement element = waitForElementPresence(By.xpath("//*[@class='" + value + "']"));
        return element;
    }


    /**
     * The method is used to compare web image with certain label with some local image
     *
     * @param webImageLabel The label of the image in the web page
     * @param localImageSrc The name of the local image to which the web image will be compared
     */
    public void verifyImageWithLabel(String webImageLabel, String localImageSrc) throws IOException {

        WebElement image = waitForElementPresence(By.xpath("(//img[@src and parent::div[parent::div[parent::div[parent::div[preceding-sibling::div/label[contains(text(),'" + webImageLabel + "')]]]]]])[1]"));
        String link = image.getAttribute("src");
        URL url = new URL(link);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url.toString());

        CookieStore cookieStore = new BasicCookieStore();
        HttpClientContext context = new HttpClientContext();
        context.setCookieStore(cookieStore);
        HttpResponse response = client.execute(request, context);

        File webImage = new File(testHarness.config.get("temp.dir") + java.lang.System.currentTimeMillis() + ".png");
        FileUtils.copyToFile(response.getEntity().getContent(), webImage);
        File localImage = new File(testHarness.config.get("temp.dir") + File.separator + localImageSrc);

        ImagesOperations imageOps = new ImagesOperations();
        assertTrue(imageOps.compareImages(webImage, localImage));
    }

    /**
     * The method is used to find and click on element with some text in one of the table columns
     *
     * @param elementText  The text used to find the element to click on
     * @param columnText   The text of the header of the column where the element is
     * @param locator      The type of attrbute used to locate the table
     * @param locatorValue The value of the attrbute used to locate the table
     */
    public void clickOnCellWithText(String elementText, String columnText, String locator, String locatorValue) {
        List<WebElement> tableHeaders = waitForElementsPresence(By.xpath("//table[@" + locator + "='" + locatorValue + "']//th"));
        List<WebElement> tableCells = waitForElementsPresence(By.xpath("//table[@" + locator + "='" + locatorValue + "']//tr/td"));

        Integer headerIndex = -1;

        for (int headerCell = 0; headerCell < tableHeaders.size(); headerCell++) {
            if (tableHeaders.get(headerCell).getText().equals(columnText)) {
                headerIndex = headerCell;
                break;
            }
        }

        if (headerIndex == -1) {
            throw new NoSuchElementException("There is no column with text:" + columnText + " in table with " + locator + ": " + locatorValue);
        }

        boolean found = false;
        int num = 0;
        for (int rowCellIndex = headerIndex; rowCellIndex < tableCells.size(); rowCellIndex = rowCellIndex + tableHeaders.size()) {
            if (tableCells.get(rowCellIndex).getText().equals(elementText)) {
                num++;
                found = true;
                if (num > 1) {
                    throw new IllegalArgumentException("There is more than one element with text: " + elementText + "in column: " + columnText + " in table with " + locator + ": " + locatorValue);
                }
                WebElement element = tableCells.get(rowCellIndex).findElement(By.xpath(".//*[@title='" + elementText + "' or text()='" + elementText + "']"));
                clickWithScrollToView(element);
            }
        }
        if (!found) {
            throw new NoSuchElementException("There is no element with text: " + elementText + " in column: " + columnText + " in table with " + locator + ": " + locatorValue);
        }
    }
}



