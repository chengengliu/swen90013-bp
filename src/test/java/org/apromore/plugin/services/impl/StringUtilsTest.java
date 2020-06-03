package org.apromore.plugin.services.impl;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for StringUtils class.
 */
public class StringUtilsTest {
    /**
     * Test that strings are given the correct type.
     */
    @Test
    public void stringTest() {
        Assert.assertEquals(
            StringUtils.getColumnType("abc"),
            StringUtils.STRING);
        Assert.assertEquals(StringUtils.getColumnType(""), StringUtils.STRING);
    }

    /**
     * Test that ints are given the correct type.
     */
    @Test
    public void intTest() {
        Assert.assertEquals(StringUtils.getColumnType("123"), StringUtils.INT);
    }

    /**
     * Test that doubles are given the correct type.
     */
    @Test
    public void doubleTest() {
        Assert.assertEquals(
            StringUtils.getColumnType("123.456"),
            StringUtils.DOUBLE);
    }

    /**
     * Test that booleans are given the correct type.
     */
    @Test
    public void booleanTest() {
        Assert.assertEquals(
            StringUtils.getColumnType("true"),
            StringUtils.BOOLEAN);
        Assert.assertEquals(
            StringUtils.getColumnType("True"),
            StringUtils.BOOLEAN);
        Assert.assertEquals(
            StringUtils.getColumnType("TRUE"),
            StringUtils.BOOLEAN);
        Assert.assertEquals(
            StringUtils.getColumnType("false"),
            StringUtils.BOOLEAN);
        Assert.assertEquals(
            StringUtils.getColumnType("False"),
            StringUtils.BOOLEAN);
        Assert.assertEquals(
            StringUtils.getColumnType("FALSE"),
            StringUtils.BOOLEAN);
    }
}
