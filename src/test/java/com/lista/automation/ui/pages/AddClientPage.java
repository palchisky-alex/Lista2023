package com.lista.automation.ui.pages;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.ui.core.utils.Properties;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import java.nio.file.Paths;

@Getter
public class AddClientPage extends BasePage {
    private Page page;
    private String inputName = "[name='name']";
    private String inputPhone = "[name='phone']";
    private String inputEmail = "[name='email']";
    private String checkboxGenders = ".gender_item .select";
    private String birthdateYearOptions = "select.year option";
    private String birthdateMonthOptions = "select.month option";
    private String birthdateDayOptions = "select.day option";
    private String btnAddImage = "#picture .control_btn";
    private String debts = "#debts";
    private String address = "#address";
    private String notes = ".notes";
    private String status = ".status";
    private String debtsValueContainer = ".value-container";
    private String debtsValueContainerInput = ".value-container input";
    private String debtsBtnPlus = ".increment";
    private String debtsBtnMinus = ".decrement";
    private String input = "input";
    private String btnApply = ".control-apply";

    public AddClientPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("set simple client")
    public AddClientPage setSimpleClient(boolean recreate) {
        ClientCreateRequest simpleClient = generateClient(recreate);
        setImage();
        setName(simpleClient);
        setPhone(simpleClient);
        setMail(simpleClient);
        setGender();
        setBirthdate(Birthdate.Year);
        setBirthdate(Birthdate.Month);
        setBirthdate(Birthdate.Day);
        setStatus(simpleClient);
        setAddress(simpleClient);
        setNotes(simpleClient);
        setDebts(simpleClient);
        return this;
    }

    @Step("set profile image")
    public AddClientPage setImage() {
        getLocator(btnAddImage).setInputFiles(Paths.get(Properties.getProp().imagePath()));
        return this;
    }

    @Step("set profile name")
    public AddClientPage setName(ClientCreateRequest data) {
        typeIn(inputName, data.getName());
        return this;
    }

    @Step("set profile phone")
    public AddClientPage setPhone(ClientCreateRequest data) {
        typeIn(inputPhone, data.getPhone().replaceAll("\\D+", ""));
        return this;
    }

    @Step("set profile mail")
    public AddClientPage setMail(ClientCreateRequest data) {
        typeIn(inputEmail, data.getEmail());
        return this;
    }

    @Step("set profile gender")
    public AddClientPage setGender() {
        getLocator(checkboxGenders).nth(getRandomInt(1, 3)).click();
        return this;
    }

    @Step("set profile birthdate")
    public AddClientPage setBirthdate(Birthdate opt) {
        int randomInt = 1;
        int countYearsOpts = page.locator(birthdateYearOptions).count();
        int countMonthsOpts = page.locator(birthdateMonthOptions).count();
        int countDaysOpts = page.locator(birthdateDayOptions).count();

        int randomYear = getRandomInt(0, countYearsOpts);
        int randomMonth = getRandomInt(1, countMonthsOpts);
        int randomDay = getRandomInt(1, countDaysOpts);

        // get random int from options
        randomInt = opt.name().equals("Year") ? randomYear :
                opt.name().equals("Month") ? randomMonth :
                        opt.name().equals("Day") ? randomDay :
                                randomInt;

        //select random birthdate
        getLocator("select." + opt.name().toLowerCase() + ":has(option[value])")
                .selectOption(new SelectOption().setIndex(randomInt));

        return this;
    }

    @Step("set profile status")
    public AddClientPage setStatus(ClientCreateRequest data) {
        clickBy(status);
        getLocator(status).locator(input).fill(data.getNotes());
        getLocator(status).locator(btnApply).click();
        return this;
    }

    @Step("set profile note")
    public AddClientPage setNotes(ClientCreateRequest data) {
        clickBy(notes);
        typeIn(".notes textarea", data.getNotes());
        getLocator(notes).locator(btnApply).click();
        return this;
    }

    @Step("set profile address")
    public AddClientPage setAddress(ClientCreateRequest data) {
        clickBy(address);
        getLocator(address).locator(input).fill(data.getAddress());
        getLocator(address).locator(btnApply).click();
        return this;
    }

    @Step("set profile debts")
    public AddClientPage setDebts(ClientCreateRequest data) {
        clickBy(debts);
        getLocator(debts).locator(input).fill(data.getNotes());
        getLocator(debts).locator(debtsValueContainer).click();
        getLocator(debts).locator(debtsValueContainerInput).fill(String.valueOf(getRandomInt(1,999999)));
        getLocator(debts).locator(debtsBtnPlus).dblclick();
        getLocator(debts).locator(debtsBtnMinus).click();
        getLocator(debts).locator(btnApply).click();
        return this;
    }

    @Step("save new client profile")
    public ClientCreateRequest submitNewClient() {
        page.waitForResponse(Response::ok, () -> {
            clickBy(".active_button");
        });
        return simpleClient;
    }

    public enum Birthdate {
        Year,
        Month,
        Day
    }

}
