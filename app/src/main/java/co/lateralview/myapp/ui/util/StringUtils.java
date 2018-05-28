package co.lateralview.myapp.ui.util;


public abstract class StringUtils {
    public static String upperCase(String string) {
        return upperCase(string, string.length());
    }

    public static String upperCase(String string, int upperCaseChars) {
        return string.substring(0, upperCaseChars).toUpperCase() + string.substring(upperCaseChars);
    }

    public static String removeChars(String s, String c) {
        return s.replaceAll(c, "");
    }
}
