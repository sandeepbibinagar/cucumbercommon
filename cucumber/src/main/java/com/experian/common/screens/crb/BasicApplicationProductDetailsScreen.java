package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by B04342A on 6/21/2017.
 */
public class BasicApplicationProductDetailsScreen extends BasicApplicationScreen {

    @FindBy(xpath = "//div[@id='MasterProdContParent']//ul//li//p")
    public List<WebElement> products;

    public BasicApplicationProductDetailsScreen(WebClient webClient) { super(webClient); }

    public void selectProduct(String productName) {
        for(WebElement product: products){
            if(product.getText().equals(productName)) {
                product.click();
            }
        }
    }
}
