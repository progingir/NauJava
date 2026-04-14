package ru.valerii.NauJava;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthSeleniumTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestUserHelper testUserHelper;

    private WebDriver driver;
    private WebDriverWait wait;

    private final String TEST_USER = "selenium_user";
    private final String TEST_PASS = "selenium_pass123";

    @BeforeEach
    void setupTest() {
        testUserHelper.createTestUserIfNotExists(TEST_USER, TEST_PASS);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void teardown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("Предупреждение: ошибка при закрытии WebDriver: " + e.getMessage());
        } finally {
            testUserHelper.deleteTestUser(TEST_USER);
        }
    }

    private void performLogin() {
        driver.get("http://localhost:" + port + "/login");
        wait.until(ExpectedConditions.titleContains("Вход"));

        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameInput.sendKeys(TEST_USER);
        passwordInput.sendKeys(TEST_PASS);
        loginButton.click();
    }

    @Test
    @DisplayName("Пользователь может успешно авторизоваться")
    void testLoginSuccess() {
        performLogin();

        wait.until(ExpectedConditions.urlContains("/view/clients"));
        assertTrue(driver.getPageSource().contains("Наши клиенты"),
                "После успешного логина должен отображаться список клиентов");
    }

    @Test
    @DisplayName("Авторизованный пользователь может выйти из системы")
    void testLogoutSuccess() {
        performLogin();
        wait.until(ExpectedConditions.urlContains("/view/clients"));

        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Выйти из системы')]")
        ));
        logoutButton.click();

        wait.until(ExpectedConditions.urlContains("/login?logout"));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".alert-success")
        ));

        assertTrue(successMessage.getText().contains("успешно вышли"),
                "На странице должно появиться сообщение об успешном выходе из системы");
    }
}