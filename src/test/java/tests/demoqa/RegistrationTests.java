package tests.demoqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class RegistrationTests {

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        // адрес удаленного selenoid сервера, где user1 - login, 1234 - password, wd - webdriver
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";

        /* Jenkins не имеет графического интерфейса поэтому для тестирования web интерфейса необходимо
           подключить selenoid
         */
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }

    @Test
    void successPracticeFormTest() throws InterruptedException {
        open("/automation-practice-form");
        $(".main-header").shouldHave(text("Practice Form"));

        $("#firstName").setValue("Ivan");
        $("#lastName").setValue("Petrov");
        $("#userEmail").setValue("ivan@bk.ru");
        $("#genterWrapper").$(byText("Other")).click();
        $("#userNumber").setValue("4955552244");

        // working with the calendar
        $("#dateOfBirthInput").click();
        // <option value="1990">1990</option>
        $(".react-datepicker__year-select").selectOptionByValue("1990");
        // <option value="6">July</option>
        $(".react-datepicker__month-select").selectOptionByValue("6");
        $(".react-datepicker__month").$(byText("16")).click();

        $("#subjectsInput").setValue("En").pressEnter();
        $(".practice-form-wrapper").$(byText("Reading")).click();
        // $("#uploadPicture").uploadFromClasspath("img.png");
        $("#currentAddress").setValue("Russia");


        executeJavaScript("scroll(0,250)");
        $("#state").click();
        $(byText("NCR")).click();
        $("#city").click();
        $(byText("Delhi")).click();

        $("#submit").click();

        // checking the correctness of the entered value
        $(".table-responsive").shouldHave(
                text("Ivan"),
                text("Petrov"),
                text("ivan@bk.ru"),
                text("Other"),
                text("4955552244"),
                text("16 July,1990"),
                text("En"),
                text("Reading"),
                //text("img.png"),
                text("Russia"),
                text("NCR"),
                text("Delhi")
        );
    }


}
