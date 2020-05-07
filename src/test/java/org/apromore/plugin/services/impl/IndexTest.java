package org.apromore.plugin.services.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.concurrent.TimeUnit;
import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

public class IndexTest {
  private static WebDriver driver;

  @BeforeClass
  public static void setup () {
    System.setProperty("webdriver.chrome.driver", "/Users/fenprace/Binaries/Selenium/chromedriver");
    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }

  @AfterClass
  public static void clean () {
    driver.quit();
  }

  @Test
  public void shouldFindAskButton () {
    driver.get("http://localhost:8080/preprocessing-plugin/");

    assertEquals("?", driver.findElement(By.className("date")).getText());
    driver.findElement(By.className("ask")).click();
    
    String dateString = DateFormat.getDateInstance().format(new Date());
    assertEquals(dateString, driver.findElement(By.className("date")).getText());
  }
}