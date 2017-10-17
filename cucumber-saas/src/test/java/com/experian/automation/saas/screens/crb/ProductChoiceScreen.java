package com.experian.automation.saas.screens.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by B04342A on 6/21/2017.
 */
public class ProductChoiceScreen extends BasicApplicationScreen {


    @FindBy(xpath = "//input[@type='radio']")
    public List<WebElement> radioButtons;

    @FindBy(xpath = "//table/tbody/tr/td[count(//table/thead/tr/th[.='Product Type']/preceding-sibling::th)+1]")
    public List<WebElement> productTypes;

    @FindBy(id = "textfieldParamMinAmount1")
    public WebElement minAmount;

    @FindBy(id = "textfieldParamMaxAmount1")
    public WebElement maxAmount;

    public ProductChoiceScreen(TestHarness testHarness, WebHarness webHarness) {
        super(testHarness, webHarness);
    }

    public void selectProductByType(String productType) {
       for (WebElement product : productTypes) {
            if (product.getText().equals(productType)) {
                int index = productTypes.indexOf(product);
                WebElement radioButton = radioButtons.get(index);
                radioButton.click();

                // Workaround for non-visible radioButtons
                try{
                    jsClick(radioButton);
                } catch(StaleElementReferenceException e) {
                }
                break;
            }
        }
    }

    public void set(Map<String, String> dataTable) {
        for (Map.Entry<String, String> entry : dataTable.entrySet()) {
            switch (entry.getKey()) {
                case "Minimum Amount":
                    typeWithClear(minAmount, entry.getValue());
                    break;

                case "Maximum Amount":
                    typeWithClear(maxAmount, entry.getValue());
                    break;

                // Implement the rest of the field

                default:
                    throw new NoSuchElementException("Unable to set value for field " + entry.getKey());
            }
        }
    }
}
