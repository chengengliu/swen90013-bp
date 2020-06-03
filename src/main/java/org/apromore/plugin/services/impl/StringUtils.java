package org.apromore.plugin.services.impl;

/**
 * String utilities used elsewhere.
 */
public class StringUtils {
    public static final String INT = "INT";
    public static final String DOUBLE = "DOUBLE";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String STRING = "STRING";

    /**
     * Get the type of a string.
     *
     * @param string string to check
     * @return type of the string
     */
    public static String getColumnType(String string) {
        if (isInt(string)) {
            return "INT";
        } else if (isDouble(string)) {
            return "DOUBLE";
        } else if (isBool(string)) {
            return "BOOLEAN";
        } else {
            return "STRING";
        }
    }

    /**
     * Determine whether a string is an integer.
     *
     * @param string string to check
     * @return true if the string is an integer, false otherwise
     */
    private static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Determine whether a string is a double.
     *
     * @param string string to check
     * @return true if the string is a double, false otherwise
     */
    private static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Determine whether a string is a boolean.
     *
     * @param string string to check
     * @return true if the string is a boolean, false otherwise
     */
    private static boolean isBool(String string) {
        return (
            string.toLowerCase().equals("true") ||
            string.toLowerCase().equals("false"));
    }
}
