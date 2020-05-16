package org.apromore.plugin.services.impl;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.junit.Assert.assertEquals;

/**
 * Integration Test for the index page.
 */
public class IndexIntegrationTest {
    private static WebDriver driver;

    /**
     * Setup Selenium.
     */
    @BeforeClass
    public static void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /**
     * Quit Selenium WebDriver.
     */
    @AfterClass
    public static void clean() {
        driver.quit();
    }

    /**
     * Test that there should be an upload button on index page.
     */
    @Test
    public void shouldFindUploadButton() {
        driver.get("http://localhost:8080/preprocessing-plugin/");

        WebElement button = driver.findElement(By.cssSelector("button"));
        assertEquals("Upload", button.getText());
    }
}
