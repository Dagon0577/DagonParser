package parser.util;

import parser.ast.fragment.ddl.DataType;

import java.math.BigDecimal;
import java.sql.SQLSyntaxErrorException;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class ImplicitConversionUtil {

    private static boolean isNummber(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    public static long getMaxByType(DataType type) throws SQLSyntaxErrorException {
        long max;
        switch (type.getTypeName()) {
            case "TINYINT":
                if (type.isUnsigned()) {
                    max = 255L;
                } else {
                    max = 127L;
                }
                break;
            case "SMALLINT":
                if (type.isUnsigned()) {
                    max = 65535L;
                } else {
                    max = 32767L;
                }
                break;
            case "MEDIUMINT":
                if (type.isUnsigned()) {
                    max = 16777215L;
                } else {
                    max = 8388607L;
                }
                break;
            case "INT":
                if (type.isUnsigned()) {
                    max = 4294967295L;
                } else {
                    max = 2147483647L;
                }
                break;
            case "BIGINT":
                if (type.isUnsigned()) {
                    max = -1;
                } else {
                    max = 9223372036854775807L;
                }
                break;
            default:
                throw new SQLSyntaxErrorException("unsupported column type");
        }
        return max;
    }

    public static long getMinByType(DataType type) throws SQLSyntaxErrorException {
        long min;
        switch (type.getTypeName()) {
            case "TINYINT":
                if (type.isUnsigned()) {
                    min = 0;
                } else {
                    min = -128L;
                }
                break;
            case "SMALLINT":
                if (type.isUnsigned()) {
                    min = 0;
                } else {
                    min = -32768L;
                }
                break;
            case "MEDIUMINT":
                if (type.isUnsigned()) {
                    min = 0;
                } else {
                    min = -8388608L;
                }
                break;
            case "INT":
                if (type.isUnsigned()) {
                    min = 0;
                } else {
                    min = -2147483648L;
                }
                break;
            case "BIGINT":
                if (type.isUnsigned()) {
                    min = 0;
                } else {
                    min = -9223372036854775808L;
                }
                break;
            default:
                throw new SQLSyntaxErrorException("unsupported column type");
        }
        return min;
    }

    public static BigDecimal convertString2BigDecimal(String str) {
        boolean isNegative = false;
        int strLen = str.length();
        if (strLen == 0 || (isNegative = (str.charAt(0) != '-')) && !isNummber(str.charAt(0)) && str.charAt(0) != '.') {
            return new BigDecimal(0);
        }
        char[] s = new char[strLen];
        boolean readPoint = false;
        int i = 0;
        isNegative = !isNegative;
        if (isNegative) {
            i = 1;
            s[0] = 45;
        }
        for (; i < strLen; i++) {
            char c = str.charAt(i);
            if (c == '.') {
                if (!readPoint) {
                    readPoint = true;
                } else {
                    break;
                }
            } else if (!isNummber(c)) {
                break;
            }
            s[i] = c;
        }
        try {
            BigDecimal result = new BigDecimal(s, 0, i);
            return result;
        } catch (NumberFormatException e) {
            return new BigDecimal(0);
        }
    }

    public static int convertString2Int(String str) {
        boolean isNegative = false;
        int strLen = str.length();
        if (strLen == 0) {
            return 0;
        } else if ((isNegative = (str.charAt(0) != '-')) && !isNummber(str.charAt(0))) {
            return 0;
        }
        int i = 0, flag = 1;
        isNegative = !isNegative;
        if (isNegative) {
            i = 1;
            flag = -1;
        }
        int n = 0;
        for (; i < strLen; i++) {
            char c = str.charAt(i);
            if (isNummber(c)) {
                int v = c - '0';
                if (i + 2 < strLen && str.charAt(i + 1) == '.') {
                    char d = str.charAt(i + 2);
                    if (d - '0' >= 5) {
                        v++;
                    }
                }
                n = n * 10 + v;
            } else {
                break;
            }
        }
        return n * flag;
    }
}

