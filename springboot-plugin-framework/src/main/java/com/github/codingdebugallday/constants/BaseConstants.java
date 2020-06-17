package com.github.codingdebugallday.constants;

/**
 * <p>
 * 常量类
 * </p>
 *
 * @author isaac 2020/6/17 11:45
 * @since 1.0
 */
public class BaseConstants {

    private BaseConstants() {
        throw new IllegalStateException("constant class");
    }

    public static class Symbol {

        private Symbol() {
            throw new IllegalStateException("constant class");
        }

        public static final String SIGH = "!";
        public static final String AT = "@";
        public static final String WELL = "#";
        public static final String DOLLAR = "$";
        public static final String RMB = "￥";
        public static final String SPACE = " ";
        public static final String LB = System.getProperty("line.separator");
        public static final String PERCENTAGE = "%";
        public static final String AND = "&";
        public static final String STAR = "*";
        public static final String MIDDLE_LINE = "-";
        public static final String LOWER_LINE = "_";
        public static final String EQUAL = "=";
        public static final String PLUS = "+";
        public static final String COLON = ":";
        public static final String SEMICOLON = ";";
        public static final String COMMA = ",";
        public static final String POINT = ".";
        public static final String SLASH = "/";
        public static final String VERTICAL_BAR = "|";
        public static final String DOUBLE_SLASH = "//";
        public static final String BACKSLASH = "\\";
        public static final String QUESTION = "?";
        public static final String LEFT_BIG_BRACE = "{";
        public static final String RIGHT_BIG_BRACE = "}";
        public static final String LEFT_MIDDLE_BRACE = "[";
        public static final String RIGHT_MIDDLE_BRACE = "]";
        public static final String BACK_QUOTE = "`";
    }

    public static class Pattern {

        private Pattern() {
            throw new IllegalStateException("constant class");
        }

        public static String DATE = "yyyy-MM-dd";
        public static String DATETIME = "yyyy-MM-dd HH:mm:ss";
        public static String DATETIME_MM = "yyyy-MM-dd HH:mm";
        public static String DATETIME_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
        public static String TIME = "HH:mm";
        public static String TIME_SS = "HH:mm:ss";
        public static String SYS_DATE = "yyyy/MM/dd";
        public static String SYS_DATETIME = "yyyy/MM/dd HH:mm:ss";
        public static String SYS_DATETIME_MM = "yyyy/MM/dd HH:mm";
        public static String SYS_DATETIME_SSS = "yyyy/MM/dd HH:mm:ss.SSS";
        public static String NONE_DATE = "yyyyMMdd";
        public static String NONE_DATETIME = "yyyyMMddHHmmss";
        public static String NONE_DATETIME_MM = "yyyyMMddHHmm";
        public static String NONE_DATETIME_SSS = "yyyyMMddHHmmssSSS";
        public static String CST_DATETIME = "EEE MMM dd HH:mm:ss 'CST' yyyy";
        public static String NONE_DECIMAL = "0";
        public static String ONE_DECIMAL = "0.0";
        public static String TWO_DECIMAL = "0.00";
        public static String TB_NONE_DECIMAL = "#,##0";
        public static String TB_ONE_DECIMAL = "#,##0.0";
        public static String TB_TWO_DECIMAL = "#,##0.00";
    }

    public static class Suffix {

        private Suffix() { throw new IllegalStateException("constant class");
        }

        public static String JAR = "jar";
        public static String ZIP = "zip";
    }
}
