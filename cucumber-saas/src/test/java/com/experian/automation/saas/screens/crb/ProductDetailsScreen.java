package com.experian.automation.saas.screens.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by B04342A on 6/21/2017.
 */
public class ProductDetailsScreen extends BasicApplicationScreen {

    @FindBy(xpath = "//div[@id='MasterProdContParent']//ul//li//p")
    public List<WebElement> products;

    public ProductDetailsScreen(TestHarness testHarness, WebHarness webHarness) { super(testHarness, webHarness); }

    public void selectProduct(String productName) {
        for(WebElement product: products){
            if(product.getText().equals(productName)) {
                product.click();
            }
        }
    }
}