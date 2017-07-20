package com.experian.common.screens;

import com.experian.common.WebClient;
import com.experian.common.helpers.CustomExpectedConditions;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Screen
{
    private final int waitIntervalElement = 10;
    private final int waitIntervalScreen = 20;
    private final int waitIntervalAlert = 5;
    private String screenWindowHandle;

    protected WebClient webClient;

    public Screen(WebClient webClient)
    {
        this.webClient = webClient;

        // Set current screen
        this.webClient.setCurrentScreen(this.getClass().getSimpleName());

        initElements();
    }

    public Screen initElements()
    {
        PageFactory.initElements(webClient.driver, this);

        return this;
    }

    public Screen refreshElements()
    {
        return initElements();
    }

    public Screen waitForElement(WebElement element)
    {
        return waitForElement(element, waitIntervalElement);
    }

    public Screen waitForElements(List<WebElement> elements)
    {
        return waitForElements(elements, waitIntervalElement);
    }

    public Screen waitForElementToDisappear(WebElement element)
    {
        return waitForElementToDisappear(element, waitIntervalElement);
    }

    public Screen waitForScreen(WebElement element)
    {
        return waitForElement(element, waitIntervalScreen);
    }

    public Screen waitForScreenProgress(WebElement element)
    {
        return waitForElementToDisappear(element, waitIntervalScreen);
    }

    public int getWaitIntervalElement() {
        return waitIntervalElement;
    }

    public int getWaitIntervalScreen() {
        return waitIntervalScreen;
    }

    public String getScreenWindowHandle() {
        return screenWindowHandle;
    }

    public Point getElementLocation(WebElement element, int retries)
    {
        Point location = null;

        if (retries > 0)
        {
            try
            {
                location = element.getLocation();
            } catch (Exception e)
            {
                location = getElementLocation(element, --retries);
            }

            if ( location.getY() == 0  && location.getX() == 0 )
                location = getElementLocation(element, --retries);
        }

        return location;
    }

    public Point getElementLocation(List<WebElement> elements, int position, int retries)
    {
        Point location = null;

        if (retries > 0)
        {
            try
            {
                location = elements.get(position).getLocation();
            } catch (Exception e)
            {
                location = getElementLocation(elements, position, --retries);
            }

            if ( location.getY() == 0  && location.getY() == 0 )
                location = getElementLocation(elements, position, --retries);
        }

        return location;
    }

    public boolean isElementPresented(WebElement element)
    {
        try
        {
            return elementPresented(element);
        } catch (InvocationTargetException | StaleElementReferenceException | NoSuchElementException e)
        {
            return false;
        }
    }

    public boolean isAlertPresented() {
        try {
            new WebDriverWait(webClient.driver, waitIntervalAlert).until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public Screen scrollTo(WebElement fromElement, WebElement toElement)
    {
        int x = fromElement.getLocation().getX();

        int fromHeight = fromElement.getSize().getHeight();
        int fromY = fromElement.getLocation().getY() + fromHeight / 2;

        int toHeight = toElement.getSize().getHeight();
        int toY = toElement.getLocation().getY() + toHeight / 2;

        return this;
    }

    public Screen scrollUp(List<WebElement> elements)
    {
        WebElement fromElement = elements.get(0);
        WebElement toElement = elements.get(elements.size() - 1);

        scrollTo(fromElement, toElement);

        return this;
    }

    public Screen scrollDown(List<WebElement> elements)
    {
        WebElement fromElement = elements.get(elements.size() - 1);
        WebElement toElement = elements.get(0);

        scrollTo(fromElement, toElement);

        return this;
    }

    public void scrollToView(WebElement element) {
        ((JavascriptExecutor) webClient.driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToTop(WebElement element) {
        ((JavascriptExecutor) webClient.driver).executeScript("arguments[0].scrollTop = 0;", element);
    }

    public void scrollToElement(WebElement container, WebElement element) {
        ((JavascriptExecutor) webClient.driver).executeScript("arguments[0].scrollTop = arguments[1];", container, element.getLocation().getY());
    }

    public void jsClick(WebElement element){
        ((JavascriptExecutor) webClient.driver).executeScript("arguments[0].click();", element);
    }


    public void clickWithScrollToView(WebElement element) {
        scrollToView(element);
        element.click();
    }

    public WebElement scrollDownUntil(List<WebElement> elements, String searchText)
    {
        WebElement matchElement = null;
        String firstElementText = "";
        boolean scroll = true;

        do
        {

            for (int i = 0; i < elements.size() && scroll; i++)
            {
                WebElement element = elements.get(i);
                String elementText = element.getText();

                if (elementText.equals(searchText))
                {
                    matchElement = element;
                    scroll = false;
                }

                if (i == 0)
                {
                    if (firstElementText.equals(elementText))
                        scroll = false;

                    firstElementText = elementText;
                }
            }

            if (scroll)
                scrollDown(elements);

        } while (scroll);

        return matchElement;
    }

    public WebElement getElement(String name)
    {
        WebElement element = null;

        // Convert to lower underscore
        name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        // Convert back to lower camel with fixed spaces
        name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name.replace(" ","_"));

        try
        {
            element = (WebElement) this.getClass().getField(name).get(this);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        return element;
    }

    public List<WebElement> getElements(String name)
    {
        List<WebElement> elements = null;

        try
        {
            elements = (List<WebElement>) this.getClass().getField(name).get(this);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        return elements;
    }

    public WebElement getElementByText(List<WebElement> elements, String searchText)
    {
        WebElement matchElement = null;

        int i = 0;
        while (i < elements.size() ) {
            if (elements.get(i).getText().equals(searchText))
                matchElement = elements.get(i);
            i++;
        }

        return matchElement;
    }


    public void type(WebElement field, String text)
    {
        field.sendKeys(text);
    }

    public void typeWithClear(WebElement field, String text)
    {
        //clickWithScrollToView(field);
        field.clear();
        type(field, text);
    }

    public void typeWithBackspaceClear(WebElement field, String text) {
        field.click();
        String backSpaceSuffix = StringUtils.repeat(Keys.BACK_SPACE.toString(), field.getAttribute("value").length());
        type(field, backSpaceSuffix + text);
    }

    public void typeWithValueReplace(WebElement field,String value){
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }

    public void typeWithRetry(WebElement field, String text, int retries)
    {
        if (retries > 0)
        {
            type(field, text);

            if( !field.getText().equals(text) )
                typeWithRetry(field, text, --retries);
        }
    }

    public void removeElementAttribute(WebElement field, String attribute)
    {
        ((JavascriptExecutor) webClient.driver).executeScript(
                "arguments[0].removeAttribute('" + attribute + "')", field);
    }

    public void setDateTimePickerValue(WebElement dateTimeInputField, String value) {
        clickWithScrollToView(dateTimeInputField);
        WebElement dateTimePickerControl = getParentElement(dateTimeInputField);
        if (StringUtils.isNotEmpty(value.trim())) {
            ((JavascriptExecutor) webClient.driver).executeScript(
                    "window.$(arguments[0]).datetimepicker('date','" + value + "');", dateTimePickerControl);

        } else {
            ((JavascriptExecutor) webClient.driver).executeScript(
                    "window.$(arguments[0]).data('DateTimePicker').clear();", dateTimePickerControl);
        }

        if (dateTimeInputField.getAttribute("value").matches("(\\d+)\\/(\\d+)\\/(\\d+) (\\d+):(\\d+)")) {
            String hour = dateTimeInputField.getAttribute("value").split(" ")[1];
            waitForTextFieldValue(dateTimeInputField, value + " " + hour);
        } else if (dateTimeInputField.getAttribute("value").matches("(\\d+)\\/(\\d+)\\/(\\d+) (\\d+):(\\d+) (AM|PM)")){
            String hour = dateTimeInputField.getAttribute("value").split(" ")[1] + " " + dateTimeInputField.getAttribute("value").split(" ")[2];
            waitForTextFieldValue(dateTimeInputField, value + " " + hour);
        } else {
            waitForTextFieldValue(dateTimeInputField, value);
        }
    }

    public void setElementAttribute(WebElement field, String attribute, String value)
    {
        ((JavascriptExecutor) webClient.driver).executeScript(
                "arguments[0].setAttribute('" + attribute + "','" + value +"')", field);
    }

    public void appendBaseURLandGoTo(String url, boolean alwaysNavigate){

        String baseURL = webClient.config.get("base.url");

        goToURL(baseURL + url, alwaysNavigate);
    }

    public void appendBaseURLandGoTo(String url) {
        appendBaseURLandGoTo(url, false);
    }

    public void goToURL(String url, boolean alwaysNavigate){

        if (!webClient.driver.getCurrentUrl().equals(url) || alwaysNavigate){
            this.webClient.driver.navigate().to(url);
            initElements();
        }
    }

    public void goToURL(String url) {
        goToURL(url, false);
    }

    public void switchToWindow(String name) {
        switchToWindow(name, true);
    }

    public void switchToWindow(String name, Boolean returnToDefaultFrame) {

        if(returnToDefaultFrame) {
            this.webClient.driver.switchTo().defaultContent();
        }

        int retry = 0;
        int maxRetry = 50;
        try {
            while (!trySwitchToWindow(name) && retry < maxRetry) {
                Thread.sleep(this.waitIntervalScreen * 1000 / maxRetry);
                retry++;
            }
        } catch (InterruptedException e) {

        }
        if (retry >= maxRetry) {
            throw new NoSuchWindowException("Unable to switch to window " + name + " for " + this.waitIntervalScreen + " seconds");
        }
        screenWindowHandle = webClient.driver.getWindowHandle();
    }

    private boolean trySwitchToWindow(String name) {
        try {
            this.webClient.driver.switchTo().window(name);
            return true;
        } catch (NoSuchWindowException e) {
            return false;
        }
    }

    public void switchToFrame(WebElement frameElement, Boolean returnToDefaultFrame)
    {
        if ( returnToDefaultFrame ) {
            webClient.driver.switchTo().defaultContent();
        }

        WebDriverWait wait = new WebDriverWait(webClient.driver, this.waitIntervalScreen);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
    }

    public void checkbox(WebElement element, Boolean select)
    {
        if ((element.isSelected() && !select) || (!element.isSelected() && select)) {
            element.click();
        }
    }

    private Screen waitForElement(WebElement element, int waitInterval)
    {
        List<WebElement> elements = Arrays.asList(element);

        return waitForElements(elements, waitInterval);
    }

    private Screen waitForElements(List<WebElement> elements, int waitInterval)
    {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitInterval);
        wait.until(CustomExpectedConditions.visibilityOfAllElements(elements));

        return this;
    }

    public List<WebElement> waitForElementsCountChange(List<WebElement> elements, Integer oldCount) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalElement);
       return wait.until(CustomExpectedConditions.waitForElementsCountChange(elements, oldCount));
    }

    public Screen waitForElementContentChange(WebElement element, String oldContent) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalScreen);
        wait.until(CustomExpectedConditions.waitForElementContentChange(element, oldContent));
        return this;
    }

    public Screen waitForNumberOfWindowsToBe(int expectedNumberOfWindows) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalScreen);
        wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
        return this;
    }

    public Screen waitForElementAttributeChange(WebElement element, String attributeName, String oldContent) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalScreen);
        wait.until(CustomExpectedConditions.waitForElementAttributeChange(element, attributeName, oldContent));
        return this;
    }

    public Screen waitForWindowToClose() {
        return waitForWindowToClose(null);
    }

    public Screen waitForWindowToClose(Screen screenToSwitch) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalScreen);
        wait.until(CustomExpectedConditions.waitForWindowToClose(screenWindowHandle));

        if (screenToSwitch != null) {
            switchToWindow(screenToSwitch.getScreenWindowHandle(), false);
        } else {
            switchToWindow(webClient.driver.getWindowHandles().iterator().next(), false);
        }

        return this;
    }

    public Screen closeWindow() {
        webClient.driver.close();
        return waitForWindowToClose();
    }

    public Screen waitForElementsCountAtLeast(List<WebElement> elements, Integer expectedCount) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalScreen);
        wait.until(CustomExpectedConditions.waitForElementsCountAtLeast(elements, expectedCount));
        return this;
    }

    public Screen waitForElementsCountEquals(List<WebElement> elements, Integer expectedCount) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalScreen);
        wait.until(CustomExpectedConditions.waitForElementsCountEquals(elements, expectedCount));
        return this;
    }

    public Screen waitForDropDownValue(Select dropDown, String expectedValue) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalElement);
        wait.until(CustomExpectedConditions.waitForDropDownValue(dropDown, expectedValue, true));
        return this;
    }

    public Screen waitForDropDownValueChange(Select dropDown, String oldValue) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalElement);
        wait.until(CustomExpectedConditions.waitForDropDownValue(dropDown, oldValue, false));
        return this;
    }


    public Screen waitForTextFieldValue(WebElement element, String expectedValue) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalElement);
        wait.until(CustomExpectedConditions.waitForTextFieldValue(element, expectedValue, true));
        return this;
    }

    public Screen waitForTextFieldValueChange(WebElement element, String oldValue) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalElement);
        wait.until(CustomExpectedConditions.waitForTextFieldValue(element, oldValue, false));
        return this;
    }


    private Screen waitForElementToDisappear(WebElement element, int waitInterval) {

        int retry = 0;
        int maxRetry = 100;
        boolean isElementVisible = true;

        try {
            while (isElementPresented(element) && retry < maxRetry) {
                Thread.sleep(waitInterval * 1000 / maxRetry);
                retry++;
            }
        } catch (InterruptedException e) {

        }
        if (retry >= maxRetry) {
            throw new NoSuchElementException("Element " + element + "  does not disappear for " + waitInterval + " seconds");
        }
        return this;
    }

    private boolean elementPresented(WebElement element) throws InvocationTargetException, StaleElementReferenceException, NoSuchElementException
    {
        return element.isDisplayed();
    }

    public List<Integer> findDataInTable(List<List<String>> expectedData, List<WebElement> tableHeaders, List<WebElement> tableCells,
                                         Boolean expectedDataIncludesHeaders, Boolean columnsAreSorted) throws NoSuchElementException {
        return findDataInTable(expectedData, tableHeaders, tableCells, expectedDataIncludesHeaders, columnsAreSorted, true);
    }

    public List<Integer> findDataInTable(List<List<String>> expectedData, List<WebElement> tableHeaders, List<WebElement> tableCells,
                                         Boolean expectedDataIncludesHeaders, Boolean columnsAreSorted, Boolean rowsAreSorted) throws NoSuchElementException {
        //returned list with found row indexes
        List<Integer> foundRowsIndexes = new ArrayList<>();

        //Actual data headers
        List<String> actualTableHeaders = new ArrayList<>();
        Integer actualTableColumnsCount = tableHeaders.size();
        for (WebElement header : tableHeaders) {
            actualTableHeaders.add(header.getText().trim());
        }

        //Expected data headers, if findUsingAllColumns is equal to true, check for all headers and expectedData must not contain header values
        List<String> expectedDataHeaders;
        Integer expectedDataStartIndex;
        if (!expectedDataIncludesHeaders) {
            expectedDataHeaders = actualTableHeaders;
            expectedDataStartIndex = 0;
        } else {
            //First list from expectedData is the header
            expectedDataHeaders = expectedData.get(0);
            expectedDataStartIndex = 1;

            Integer lastColumnIndex = -1;
            String lastColumnName = "";
            //Check that all expected headers are present in the
            for (String expectedHeader : expectedDataHeaders) {
                if (!actualTableHeaders.contains(expectedHeader)) {
                    throw new NoSuchElementException("Table does not have header " + expectedHeader + ", found headers " + actualTableHeaders);
                } else {
                    Integer currentColumnIndex = actualTableHeaders.indexOf(expectedHeader);
                    if (columnsAreSorted && lastColumnIndex >= 0 && lastColumnIndex > currentColumnIndex) {
                        throw new NoSuchElementException("Table header " + expectedHeader + " index " + currentColumnIndex + " " +
                                "is less than the previous header " + lastColumnName + " index " + lastColumnIndex);
                    }
                    lastColumnIndex = currentColumnIndex;
                    lastColumnName = expectedHeader;
                }
            }
        }

        List<String> allCellsText = getElementsText(tableCells);
        List<String> cellsData = new ArrayList<>();
        for (int i = 0; i < allCellsText.size(); i = i + actualTableColumnsCount) {
            for (String header : expectedDataHeaders) {
                String cellText = allCellsText.get(i + actualTableHeaders.indexOf(header));
                cellsData.add(cellText);
            }
        }
        List<List<String>> actualData = Lists.partition(cellsData, expectedDataHeaders.size());
        Integer actualDataStartIndex = 0;
        //Skip the first row since it is header
        for (int i = expectedDataStartIndex; i < expectedData.size(); i++) {
            List<String> expectedRow = expectedData.get(i);
            //Check that the row is present on the screen
            Boolean found = false;
            for (Integer actualRowIndex = actualDataStartIndex; actualRowIndex < actualData.size(); actualRowIndex++) {
                List<String> actualRow = actualData.get(actualRowIndex);
                if (areTableRowValuesEqual(actualRow, expectedRow) && (!foundRowsIndexes.contains(actualRowIndex))) {
                    foundRowsIndexes.add(actualRowIndex);
                    if (rowsAreSorted) {
                        actualDataStartIndex = actualRowIndex + 1;
                    } else {
                        actualDataStartIndex = 0;
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new NoSuchElementException("Row with values '" + expectedRow.toString() +
                        " for columns " + expectedDataHeaders + "' does not exist in table");
            }
        }
        return foundRowsIndexes;
    }

    public boolean areTableRowValuesEqual(List<String> actualRow, List<String> expectedRow) {
        if (actualRow.size() != expectedRow.size()) {
            return false;
        }
        for (int i = 0; i < actualRow.size(); i++) {
            String expectedValue = expectedRow.get(i);
            String actualValue = actualRow.get(i);
            if (expectedValue.startsWith("regex%") && expectedValue.endsWith("%")) {
                //Get the regex
                expectedValue = expectedValue.substring(expectedValue.indexOf("%") + 1, expectedValue.lastIndexOf("%"));
                if (!actualValue.matches(expectedValue)) {
                    return false;
                }
            } else {
                if (!actualValue.equals(expectedValue)) {
                    return false;
                }
            }

        }
        return true;
    }


    public List<String> getSelectOptions(WebElement selectElement) {
        Select select = new Select(selectElement);
        List<String> selectOptions = new ArrayList<>();
        for (WebElement option : select.getOptions()) {
            selectOptions.add(option.getText());
        }
        return selectOptions;
    }

    public WebElement getParentElement(WebElement childElement) {
        return childElement.findElement(By.xpath("./.."));
    }
    public List<String> getElementsText(List<WebElement> elements) {

        List<String> elementsText = new ArrayList<>(elements.size());

        for (WebElement element : elements) {
            elementsText.add(element.getText().trim());
        }

        return elementsText;
    }

    public WebElement getElementWithText( List<WebElement> elements,String elementText) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalElement);
        return wait.until(CustomExpectedConditions.elementWithTextVisible(elements, elementText, false));
    }

    public WebElement getElementWithPartialText( List<WebElement> elements,String partialElementText) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalElement);
        return wait.until(CustomExpectedConditions.elementWithTextVisible(elements, partialElementText, true));
    }

    public WebElement getElementWithAttributeValue( List<WebElement> elements,String attributeName, String attributeValue) {
        WebDriverWait wait = new WebDriverWait(webClient.driver, waitIntervalElement);
        return wait.until(CustomExpectedConditions.elementWithAttributeValue(elements, attributeName, attributeValue));
    }



    public String getWindowTitle(){
        return webClient.driver.getTitle();
    }

    public void mouseOver(WebElement element) {
        if (webClient.driver instanceof FirefoxDriver || webClient.driver instanceof InternetExplorerDriver) {
            String strJavaScript = "var element = arguments[0];"
                    + "var mouseEventObj = document.createEvent('MouseEvents');"
                    + "mouseEventObj.initEvent( 'mouseover', true, true );"
                    + "element.dispatchEvent(mouseEventObj);";
            ((JavascriptExecutor) webClient.driver).executeScript(strJavaScript, element);
        } else {
            Actions builder = new Actions(webClient.driver);
            builder.moveToElement(element).build().perform();
        }
    }

    public void refreshScreen() {
        webClient.driver.switchTo().defaultContent();
        webClient.driver.navigate().refresh();
    }

    public void clickElement(WebElement element) {
        if (webClient.driver instanceof InternetExplorerDriver) {
            String strJavaScript = "arguments[0].click();";
            ((JavascriptExecutor) webClient.driver).executeScript(strJavaScript, element);
        } else {
            element.click();
        }
    }
}
