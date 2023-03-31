import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
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
    @ParameterizedTest(name = "При переходе в пункт меню {0} в каталоге выбран фильтр {0}")
    public void test(String title){
        $("#categories_id").$("a[title = " + title +"]").click();
        $$("label").findBy(text(title)).preceding(0).$("input[type = checkbox]").shouldBe(checked);
    }

    @AfterEach
    public void closeWindow(){
        Selenide.closeWindow();
    }

    @CsvFileSource(resources = "/TestData.csv")
    @ParameterizedTest(name = "При выборе уровня {0} в разделе тестирования отображается курс {1}")
    public void test2(String level, String courseName){
        $("#categories_id").$("a[title = Тестирование]").click();
        $$("p").findBy(text("Уровень")).parent().sibling(0)
                .$$("label").findBy(text(level)).preceding(0).$("input[type = checkbox]").click();
        $("#__next").shouldBe(text(courseName));
    }
}


