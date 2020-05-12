package org.apromore.plugin.services.impl;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

public class IndexIntegrationTest {
  private static WebDriver driver;

  /**
   * Setup selenium
   * @see swen90013-bp.atlassian.net/wiki/spaces/BPM/pages/129269866/Integration+Test#Run-the-Tests
   */
  @BeforeClass
  public static void setup () {
    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }

  @AfterClass
  public static void clean () {
    driver.quit();
  }

  @Test
  public void shouldFindUploadButton () {
    driver.get("https://bp.omjad.as/preprocessing-plugin/");

    assertEquals("Upload", driver.findElement(By.cssSelector("button")).getText());
  }
}