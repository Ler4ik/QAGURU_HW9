import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class OtusTests {

    @BeforeEach
    public void setUp(){

        open("https://otus.ru/");
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";
    }

    @ValueSource(
            strings = {"Аналитика", "Тестирование"}
    )

    @Tag("simple")
    @ParameterizedTest(name = "При переходе в пункт меню {0} в каталоге выбран фильтр {0}")
    public void test(String title){
        $("#categories_id").$("a[title = " + title +"]").click();
        $$("label").findBy(text(title)).preceding(0).$("input[type = checkbox]").shouldBe(checked);
    }

    @AfterEach
    public void closeWindow(){
        Selenide.closeWindow();
    }

    @Tag("simple")
    @CsvFileSource(resources = "/TestData.csv")
    @ParameterizedTest(name = "При выборе уровня {0} в разделе тестирования отображается курс {1}")
    public void test2(String level, String courseName){
        $("#categories_id").$("a[title = Тестирование]").click();
        $$("p").findBy(text("Уровень")).parent().sibling(0)
                .$$("label").findBy(text(level)).preceding(0).$("input[type = checkbox]").click();
        $("#__next").shouldBe(text(courseName));
    }

    static Stream<Arguments> dataProvider(){
        return Stream.of(
                Arguments.of(MenuItem.Обучение, List.of("Все курсы", "События", "OTUS рекомендует")),
                Arguments.of(MenuItem.Информация, List.of("OTUS", "Студентам", "B2B", "Преподавателям"))
        );
    }

    @MethodSource("dataProvider")
    @ParameterizedTest(name = "При наведении на пункт меню {0} отокрывается попап с разделами: {1}")
    public void test3(
            MenuItem item,
            List<String> columns
    ){
        $(".header3__nav span[title = " + item +" ]").hover();
        $$(".header3__nav-item-popup-wrapper p").filter(visible).shouldHave(texts(columns));
    }
}


