import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void tryCorrectFilledForm() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Чан Джеки");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+78005553535");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", message.strip());
    }

    @Test
    void tryCorrectFilledFormWithHyphen() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Чан-Иванов Джеки");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+78005553535");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", message.strip());
    }

    @Test
    void tryEmptyForm() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("button")).click();
        String message = driver.findElement(By.cssSelector("[class='input__sub']")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }

    @Test
    void tryWithoutName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+78005553535");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String message = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }

    @Test
    void tryIncorrectName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Chan 2");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+78005553535");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String message = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", message.strip());
    }

    @Test
    void tryWithoutPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Чан Джеки");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String message = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }

    @Test
    void tryIncorrectPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Чан Джеки");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("88005553535");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String message = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message.strip());
    }

    @Test
    void tryWithoutCheckbox() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Чан Джеки");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+78005553535");
        driver.findElement(By.cssSelector("button")).click();
        assertTrue(driver.findElement(By.cssSelector(".input_invalid > .checkbox__box")).isEnabled());
    }
}