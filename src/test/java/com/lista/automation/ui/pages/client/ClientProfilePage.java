package com.lista.automation.ui.pages.client;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.ui.core.utils.Properties;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import io.qameta.allure.Step;

import java.nio.file.Paths;

import static io.qameta.allure.Allure.step;

public class ClientProfilePage extends BasePage {
    private Page page;
    private String birthdateYearOptions = "select.year option";
    private String birthdateMonthOptions = "select.month option";
    private String birthdateDayOptions = "select.day option";
    private String btnAddImage = ".camera";
    private String profileInfo = "[id='profile']";
    private String profileName = "[id='name-input']";
    private String profilePhone = "[type='tel']";
    private String profileAddress = "[name=address]";
    private String profileEmail = "[id='email-input']";
    private String profileGender = ".gender_item:not(.gender_active)";
    private String profileInfoSaveBtn = ".save-btn";
    private String debtsEditBtn = "#debts .right-side";
    private String debtsDescriptions = "#debts .description-input";
    private String notesEditBtn = "#notes .right-side";

    public ClientProfilePage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("initiate Profile Info editing")
    public ClientProfilePage initProfileInfoEdit() {
        clickBy(getLocator("#profile-header").getByRole(AriaRole.BUTTON),0,true);
        return this;
    }

    @Step("clean all inputs of profile info")
    public ClientProfilePage cleanProfileInfo() {
        for (Locator li : getLocator("[class^='del-info']").all())
            clickBy(li,0,false);
        return this;
    }

    @Step("initiate Profile debts editing")
    public ClientProfilePage initDebtsEdit() {
        clickBy(debtsEditBtn,0,true);
        return this;
    }

    @Step("initiate Profile notes editing")
    public ClientProfilePage initNotesEdit() {
        clickBy(notesEditBtn,0,true);
        return this;
    }

    @Step("initiate Profile gallery editing")
    public GalleryPage editGallery() {
        step("open gallery slot", () -> {
            String showAllFieldsBtn = "Show All Fields";
            boolean visible = isVisible(getByText(showAllFieldsBtn));
            if (visible) {
                clickBy(getByText(showAllFieldsBtn),0,true);
            }
        });
        step("load picture to gallery", () -> {
            getByText("Add New File").setInputFiles(Paths.get(Properties.getProp().imagePath2()));
        });

        return new GalleryPage(page);
    }

    @Step("set personal info")
    public ClientProfilePage setPersonalInfo(boolean recreate) {
        ClientCreateRequest simpleClient = generateClient(recreate);
        setImage();
        setName(simpleClient);
        setPhone(simpleClient);
        setMail(simpleClient);
        setGender();
        setBirthdate(AddClientPage.Birthdate.Year);
        setBirthdate(AddClientPage.Birthdate.Month);
        setBirthdate(AddClientPage.Birthdate.Day);
        setAddress(simpleClient);
//        setStatus();
//        setNotes();
//        saveNewClient();

        return this;
    }

    @Step("set debts")
    public ClientProfilePage setDebts(ClientCreateRequest data) {
        step("type new text", () -> {
            typeIn(debtsDescriptions, data.getNotes());
        });
        step("click on button minus", () -> {
            clickBy(getLocator(".debt-active .ink").first(),0,false);
        });
        step("click on button plus", () -> {
            dblClickBy(getLocator(".debt-active .ink").nth(1),false);
        });
        step("click on Success button", () -> {
            clickBy(getLocator("#debts").getByText("Success"),0,false);
        });
        return this;
    }

    @Step("set profile image")
    public ClientProfilePage setImage() {
        getLocator(btnAddImage).setInputFiles(Paths.get(Properties.getProp().imagePath2()));
        return this;
    }

    @Step("set profile name")
    private ClientProfilePage setName(ClientCreateRequest data) {
        getLocator(profileInfo).locator(profileName).fill(data.getName());
        return this;
    }

    @Step("set profile phone")
    private ClientProfilePage setPhone(ClientCreateRequest data) {
        getLocator(profileInfo).locator(profilePhone).fill(data.getPhone().replaceAll("\\D+", ""));
        return this;
    }

    @Step("set profile mail")
    private ClientProfilePage setMail(ClientCreateRequest data) {
        getLocator(profileInfo).locator(profileEmail).fill(data.getEmail());
        return this;
    }

    @Step("set profile gender")
    public ClientProfilePage setGender() {
        int random = getRandomInt(1, 2);
        clickBy(getLocator(profileGender).nth(random),0,false);
        return this;
    }

    @Step("set profile birthdate")
    public ClientProfilePage setBirthdate(AddClientPage.Birthdate opt) {
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

    @Step("set profile address")
    public ClientProfilePage setAddress(ClientCreateRequest data) {
        typeIn(profileAddress, data.getAddress());
        return this;
    }

    @Step("save profile info")
    public ClientCreateRequest saveProfileInfo() {
        page.waitForResponse(Response::ok, () -> {
            clickBy(profileInfoSaveBtn,0,true);
        });
        return simpleClient;
    }

    @Step("set profile note")
    public ClientProfilePage setNotes(ClientCreateRequest data) {
        step("type note text", () -> {
            getLocator("#notes").locator("textarea").fill(data.getNotes());
        });
        step("click on button Success", () -> {
            clickBy(getLocator("#notes").getByText("Success"),0,true);
        });
        return this;
    }



    public enum Birthdate {
        Year,
        Month,
        Day
    }
}
