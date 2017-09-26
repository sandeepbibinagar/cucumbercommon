package com.experian.automation.helpers;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class CustomExpectedConditions {
    private CustomExpectedConditions() {
    }

    public static ExpectedCondition<List<WebElement>> waitForElementsCountChange(final List<WebElement> elements, final Integer oldCount) {
        return new ExpectedCondition<List<WebElement>>() {
            public List<WebElement> apply(WebDriver driver) {
                if (elements.size() != oldCount) {
                    return elements;
                } else {
                    return null;
                }
            }

            public String toString() {
                return "Elements count " + elements + " to be different than " + oldCount;
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> waitForElementsCountAtLeast(final List<WebElement> elements, final int expectedCount) {
        return new ExpectedCondition<List<WebElement>>() {
            public List<WebElement> apply(WebDriver driver) {
                try {
                    if (elements.size() >= expectedCount) {
                        return elements;
                    } else {
                        return null;
                    }
                } catch (WebDriverException ex) {
                    return null;
                }
            }

            public String toString() {
                return "Elements count " + elements.size() + " to be greater than or equal to " + expectedCount;
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> waitForElementsCountEquals(final List<WebElement> elements, final int expectedCount) {
        return new ExpectedCondition<List<WebElement>>() {
            public List<WebElement> apply(WebDriver driver) {
                try {
                    if (elements.size() == expectedCount) {
                        return elements;
                    } else {
                        return null;
                    }
                } catch (WebDriverException ex) {
                    return null;
                }
            }

            public String toString() {
                return "Elements count " + elements.size() + " to be equal to " + expectedCount;
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> visibilityOfAllElements(
            final List<WebElement> elements) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(WebDriver driver) {
                for (WebElement element : elements) {
                    try {
                        if (!element.isDisplayed()) {
                            return null;
                        }
                    } catch (StaleElementReferenceException e) {
                        return null;
                    }
                }
                return elements.size() > 0 ? elements : null;
            }

            @Override
            public String toString() {
                return "visibility of all " + elements;
            }
        };
    }

    public static ExpectedCondition<Boolean> waitForWindowToClose(
            final String windowHandle) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !driver.getWindowHandles().contains(windowHandle);
            }

            @Override
            public String toString() {
                return "Closing of window with handle id " + windowHandle;
            }
        };
    }

    public static ExpectedCondition<WebElement> elementWithTextVisible(final List<WebElement> elements, final String elementText, boolean partial) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                for (WebElement element : elements) {
                    try {
                        if ((!partial && element.getText().trim().equals(elementText) || (partial && element.getText().trim().contains(elementText)))
                                && element.isDisplayed()) {
                            return element;
                        }
                    } catch (StaleElementReferenceException e) {
                        return null;
                    }
                }
                return null;
            }

            @Override
            public String toString() {
                if (partial) {
                    return "Visibility of element with partial text " + elementText + " within list " + elements;
                } else {
                    return "Visibility of element with text " + elementText + " within list " + elements;
                }

            }
        };
    }

    public static ExpectedCondition<WebElement> elementWithAttributeValue(final List<WebElement> elements, final String attributeName, final String attributeValue) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                for (WebElement element : elements) {
                    try {
                        if (element.getAttribute(attributeName).trim().equals(attributeValue)) {
                            return element;
                        }
                    } catch (StaleElementReferenceException e) {
                        return null;
                    }
                }
                return null;
            }

            @Override
            public String toString() {
                return "Element with attribute " + attributeName + " value equal to " + attributeValue + " within list " + elements;
            }
        };
    }

    public static ExpectedCondition<Boolean> waitForDropDownValue(
            final Select dropdown, final String expectedValue, final Boolean equal) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String dropdownValue = dropdown.getFirstSelectedOption().getText();
                if (equal.equals(dropdownValue.equals(expectedValue))) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }

            @Override
            public String toString() {
                return "Value of dropdown " + dropdown + " " + (equal ? " equals to " : " not equals to ") + expectedValue;
            }
        };
    }

    public static ExpectedCondition<Boolean> waitForTextFieldValue(
            final WebElement element, final String expectedValue, final Boolean equal) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String textFieldValue = element.getAttribute("value");
                if (equal.equals(textFieldValue.equals(expectedValue))) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }

            @Override
            public String toString() {
                return "Value of text field " + element + " " + (equal ? " equals to " : " not equals to ") + expectedValue;
            }
        };
    }

    public static ExpectedCondition<WebElement> waitForElementContentChange(final WebElement element, final String oldContent) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    if (!element.getText().trim().equals(oldContent.trim())) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (StaleElementReferenceException ex) {
                    return null;
                }
            }

            public String toString() {
                return "Elements content " + element + " to be different than " + oldContent;
            }
        };
    }

    public static ExpectedCondition<WebElement> waitForElementAttributeChange(final WebElement element, final String attributeName, final String oldContent) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    if (!element.getAttribute(attributeName).trim().equals(oldContent.trim())) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (StaleElementReferenceException ex) {
                    return null;
                }
            }

            public String toString() {
                return "Attribute " + attributeName + " of element " + element + " to be different than " + oldContent;
            }
        };
    }

    public static ExpectedCondition<String> waitForWindowWithTitle(final String windowTitle) {
        return new ExpectedCondition<String>() {
            @Override
            public String apply(WebDriver driver) {
                for (String windowHandle : driver.getWindowHandles()) {
                    driver.switchTo().window(windowHandle);
                    if (driver.getTitle().equals(windowTitle)) {
                        return windowHandle;
                    }
                }
                return null;
            }

            @Override
            public String toString() {
                return "Waiting of window with handle id " + windowTitle;
            }
        };
    }


}
