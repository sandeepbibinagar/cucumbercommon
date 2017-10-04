package com.experian.automation.saas.screens.crb;

import com.experian.automation.WebClient;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Created by c08358a on 8/3/2017.
 */
public class ApplicationOverviewScreen extends Screen {

    @FindBy(id = "AppSumLbl")
    public WebElement applicantSummaryLabel;

    @FindBy(xpath = "//div[@id='ApplicantSummaryDGRD']/div/table")
    public WebElement applicantSummaryTable;

    @FindBy(xpath = "//div[@id='ApplicantSummaryDGRD']/div/table//thead//tr//th")
    public List<WebElement> tableHeadersListTableApplicantSummary;

    @FindBy(xpath = "//div[@id='ApplicantSummaryDGRD']/div/table//tbody//tr//td")
    public List<WebElement> tableCellsListTableApplicantSummary;

    @FindBy(id = "ProdSumLbl")
    public WebElement productSummaryLabel;

    @FindBy(id = "AppDecLbl")
    public WebElement applicationDecisionsLabel;

    @FindBy(id = "SystemDecisionLbl")
    public WebElement systemDecisionLabel;

    @FindBy(id = "SystemDecisionTxt")
    public WebElement systemDecision;

    @FindBy(xpath = "//input[contains(@class, 'SystemDecisionDate')]")
    public WebElement date;

    @FindBy(id = "SystemDecisionTimeTxt")
    public WebElement time;

    @FindBy(xpath = "//img[@src and ancestor::div[preceding-sibling::div[child::label]]]")
    public List<WebElement> imagesProductSummary;

    @FindBy(xpath = "//label[parent::div[following-sibling::div[descendant::img[@src] and " +
            "not(descendant::input[string-length(@value)>0])]]]")
    public List<WebElement> labelsProductSummary;

    public ApplicationOverviewScreen(WebClient webClient) {
        super(webClient);
    }

    public String get(String elementName){
        String actualValue;
        switch(elementName){
            case "Credit Card Product":
                actualValue = getPresentedProductSummaryImageByLabel("Credit Card Product");
                break;

            case "Current Account Product":
                actualValue = getPresentedProductSummaryImageByLabel("Current Account Product");
                break;

            case "Loan Product":
                actualValue = getPresentedProductSummaryImageByLabel("Loan Product");
                break;

            case "Savings Account Product":
                actualValue = getPresentedProductSummaryImageByLabel("Savings Account Product");
                break;

            case "Mortgage Product":
                actualValue = getPresentedProductSummaryImageByLabel("Mortgage Product");
                break;

            case "Offer Bundle":
                actualValue = getPresentedProductSummaryImageByLabel("Offer Bundle");
                break;

            case "System Decision":
                actualValue = systemDecision.getAttribute("value");
                break;

            case "Date":
                actualValue = date.getAttribute("value");
                break;

            case "Time":
                actualValue = time.getAttribute("value");
                break;

            default:
                throw new NoSuchElementException("Unable to set value for field " + elementName);
        }
        return actualValue;
    }

    public String getPresentedProductSummaryImageByLabel(String label){
        WebElement img;
        String imageSrc;

        //Iterate through all labels in section Product Summary
        for (int i = 0; i < labelsProductSummary.size(); i++ ) {
            //Compare the current label from the lists with all labels and the set one
            if(Objects.equals(labelsProductSummary.get(i).getText(), label)) {
                //Get the image with the right index from the list with images
                img = imagesProductSummary.get(i);
                //Get the src attribute for the image
                imageSrc = img.getAttribute("src");

                //The presented icon is box/bundle
                if (imageSrc.contains("icon_bundle"))
                    return "Box";
                //The presented icon is mark/check
                if (imageSrc.contains("icon_ok"))
                    return "Check";
                //The presented icon is cross
                if (imageSrc.contains("icon_ko"))
                    return "Cross";
            }
        }
        return "No image found for label " + label;
    }
}
