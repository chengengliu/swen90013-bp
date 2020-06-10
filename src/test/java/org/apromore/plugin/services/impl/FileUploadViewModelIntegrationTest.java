package org.apromore.plugin.services.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.junit.Assert.assertNotNull;

/**
 * Integration test for file upload viewmodel.
 */
public class FileUploadViewModelIntegrationTest {
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
     * Test that after the file is uploaded, the eye icon is present.
     */
    @Test
    public void findEyeIcon() {
        String path = System.getProperty("user.dir") + "/src/test/resources";
        System.out.println("PATH : " + path);
        driver.get("http://localhost:8080/preprocessing-plugin/");

        WebElement uploadButton = null;
        if (driver.findElement(By.cssSelector("button")).getText()
                .equals("Upload")) {
            uploadButton = driver.findElement(By.cssSelector("button"));
        }
        uploadButton.click();
        driver.findElement(By.name("file")).sendKeys(path +
                "/userdata1.parquet");
        List<WebElement> list = driver.findElements(By.className("z-button"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // The seventh button is Upload the file.
        list.get(5).click();
        WebElement eyeIcon = driver.findElement(By.className("z-icon-eye"));

        assertNotNull(eyeIcon);
    }
}
