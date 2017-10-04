package com.experian.automation.saas.screens;

import com.experian.automation.WebClient;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimePickerScreen extends Screen {

     @FindBy(xpath = "//select[@class='ui-datepicker-year']")
    public WebElement calendarYearSelect;

    @FindBy(xpath = "//select[@class='ui-datepicker-month']")
    public WebElement calendarMonthSelect;

    @FindBy(xpath = "//table[@class='ui-datepicker-calendar']//tr//td[@data-handler='selectDay']/a")
    public List<WebElement> calendarDate;

    public DateTimePickerScreen(WebClient webClient) {
        super(webClient);
    }

    public void setDate(WebElement calendarButton,String entry,String simpleDateFormat){

        SimpleDateFormat dateFormat = new SimpleDateFormat(simpleDateFormat);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        try {
            date = dateFormat.parse(entry);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);

        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);

        List<String> monthNames = new ArrayList<>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nob", "Dec"));

        calendarButton.click();
        new Select(calendarYearSelect).selectByVisibleText(year.toString());
        new Select(calendarMonthSelect).selectByVisibleText(monthNames.get(month));
        getElementWithText(calendarDate,day.toString()).click();
    }

}
