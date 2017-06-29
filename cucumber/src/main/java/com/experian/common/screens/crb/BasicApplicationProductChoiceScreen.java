package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by B04342A on 6/21/2017.
 */
public class BasicApplicationProductChoiceScreen extends BasicApplicationScreen {


    @FindBy(xpath = "//table[@id='idb5']/tbody/tr/td/input[@type=radio]")
    public List<WebElement> radioButtons;

    @FindBy(xpath = "//table[@id='idb5']/tbody/tr/td[count(//table[@id='idb5']/thead/tr/th[.=\"Product Type\"]/preceding-sibling::th)+1]")
    public List<WebElement> productTypes;

    @FindBy(id = "textfieldParamMinAmount1")
    public List<WebElement> minAmount;

    @FindBy(id = "textfieldParamMaxAmount1")
    public List<WebElement> maxAmount;

    public BasicApplicationProductChoiceScreen(WebClient webClient) {
        super(webClient);
    }

    public void selectProductByType(String productType) {
        for (WebElement product : productTypes) {
            if (product.getText().equals(productType)) {
                product.click();
                int index = productTypes.indexOf(product);
                radioButtons.get(index).click();
            }
        }
    }
}
