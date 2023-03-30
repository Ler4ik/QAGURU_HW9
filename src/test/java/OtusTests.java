import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class OtusTests {

    @BeforeEach
    public void setUp(){
        open("https://otus.ru/");
    }

    @ValueSource(
            strings = {"Аналитика", "Тестирование"}
    )

    @ParameterizedTest(name = "test {0} test {0}")
    public void test(String title){
        $("#categories_id").$("a[title = " + title +"]").click();
        $$("label").findBy(text(title)).preceding(0).$("input[type = checkbox]").shouldBe(checked);
    }

    @AfterEach
    public void closeWindow(){
        Selenide.closeWindow();
    }
}
