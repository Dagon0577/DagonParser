package parser.util;

import parser.ast.expression.primary.literal.Literal;
import parser.ast.expression.primary.literal.LiteralNull;
import parser.ast.expression.primary.literal.LiteralNumber;
import parser.ast.expression.primary.literal.LiteralString;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public abstract class BytesUtil {
    private static final long OFFSET_MASK = 0xFFFFFFFF00000000L;
    private static final long SIZE_MASK = 0xFFFFFFFFL;

    public static final int getOffset(long info) {
        return (int)((info & OFFSET_MASK) >> 32);
    }

    public static final int getSize(long info) {
        return (int)(info & SIZE_MASK);
    }

    public static final byte[] getValue(byte[] sql, long info) {
        return getValue(sql, getOffset(info), getSize(info));
    }

    public static final byte[] getValue(byte[] sql, int offset, int size) {
        byte[] data = new byte[size];
        System.arraycopy(sql, offset, data, 0, data.length);
        return data;
    }

    public static final boolean equalsIgnoreCase(byte[] sql, byte[] upper) {
        if (sql.length != upper.length) {
            return false;
        }
        for (int i = 0, size = sql.length; i < size; i++) {
            byte l = sql[i];
            byte r = upper[i];
            if (l == r) {
                continue;
            }
            if (l >= 'A' && l <= 'Z') {
                return false;
            } else if (l >= 'a' && l <= 'z') {
                if (l == r + 32) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public static final boolean equalsIgnoreCase(byte[] sql, long info, byte[] upper) {
        int size = getSize(info);
        if (size != upper.length) {
            return false;
        }
        int offset = getOffset(info);
        for (int i = 0; i < size; i++) {
            byte l = sql[offset + i];
            byte r = upper[i];
            if (l == r) {
                continue;
            }
            if (l >= 'A' && l <= 'Z') {
                return false;
            } else if (l >= 'a' && l <= 'z') {
                if (l == r + 32) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 去除反引号进行判断
     */
    public static final boolean equalsIgnoreCase(byte[] sql, long info, byte[] upper, boolean withOpenQuate) {
        if (withOpenQuate) {
            int size = getSize(info) - 2;
            if (size != upper.length) {
                return false;
            }
            int offset = getOffset(info) + 1;
            for (int i = 0; i < size; i++) {
                byte l = sql[offset + i];
                byte r = upper[i];
                if (l == r) {
                    continue;
                }
                if (l >= 'A' && l <= 'Z') {
                    return false;
                } else if (l >= 'a' && l <= 'z') {
                    if (l == r + 32) {
                        continue;
                    }
                    return false;
                }
            }
            return true;
        }
        return equalsIgnoreCase(sql, info, upper);
    }

    public static final boolean equalsIgnoreCase(byte[] left, byte[] right, boolean leftWithOpenQuate,
        boolean rightWithOpenQuate) {
        if (leftWithOpenQuate ^ rightWithOpenQuate) {
            if (leftWithOpenQuate) {
                // 左边有反引号
                if (left.length != right.length + 2) {
                    return false;
                }
                int size = right.length;
                for (int i = 0; i < size; i++) {
                    byte l = left[i + 1];
                    byte r = right[i];
                    if (l == r) {
                        continue;
                    } else if (l >= 'A' && l <= 'Z') {
                        if (r - l == 32) {
                            continue;
                        }
                        return false;
                    } else if (l >= 'a' && l <= 'z') {
                        if (l - r == 32) {
                            continue;
                        }
                    }
                    return false;
                }
            } else {
                // 右边有反引号
                if (left.length != right.length - 2) {
                    return false;
                }
                int size = left.length;
                for (int i = 0; i < size; i++) {
                    byte l = left[i];
                    byte r = right[i + 1];
                    if (l == r) {
                        continue;
                    } else if (l >= 'A' && l <= 'Z') {
                        if (r - l == 32) {
                            continue;
                        }
                        return false;
                    } else if (l >= 'a' && l <= 'z') {
                        if (l - r == 32) {
                            continue;
                        }
                    }
                    return false;
                }
            }
        } else {
            if (left.length != right.length) {
                return false;
            }
            int size = left.length;
            for (int i = 0; i < size; i++) {
                byte l = left[i];
                byte r = right[i];
                if (l == r) {
                    continue;
                } else if (l >= 'A' && l <= 'Z') {
                    if (r - l == 32) {
                        continue;
                    }
                    return false;
                } else if (l >= 'a' && l <= 'z') {
                    if (l - r == 32) {
                        continue;
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static final byte[] toUpperCaseWithoutQuote(byte[] data, boolean withOpenQuate) {
        int length = data.length;
        if (length == 0) {
            return data;
        }
        byte[] tmp = null;
        if (withOpenQuate) {
            length = length - 2;
            tmp = new byte[length];
            for (int i = 0; i < length; i++) {
                if (data[i + 1] >= 'a' && data[i + 1] <= 'z') {
                    tmp[i] = (byte)(data[i + 1] - 32);
                } else {
                    tmp[i] = data[i + 1];
                }
            }
        } else {
            tmp = new byte[length];
            for (int i = 0; i < length; i++) {
                if (data[i] >= 'a' && data[i] <= 'z') {
                    tmp[i] = (byte)(data[i] - 32);
                } else {
                    tmp[i] = data[i];
                }
            }
            while (length > 0 && 0 <= tmp[length - 1] && tmp[length - 1] == ' ') {
                length--;
            }
        }
        if (length == tmp.length) {
            return tmp;
        }
        byte[] result = new byte[length];
        System.arraycopy(tmp, 0, result, 0, length);
        return result;
    }

    public static byte[] backTrimAndToLower(byte[] data) {
        int length = data.length;
        if (length == 0) {
            return data;
        }
        byte[] tmp = new byte[length];
        for (int i = 0; i < length; i++) {
            if (data[i] >= 'A' && data[i] <= 'Z') {
                tmp[i] = (byte)(data[i] + 32);
            } else {
                tmp[i] = data[i];
            }
        }
        while (length > 0 && 0 <= tmp[length - 1] && tmp[length - 1] == ' ') {
            length--;
        }
        if (length == tmp.length) {
            return tmp;
        }
        byte[] result = new byte[length];
        System.arraycopy(tmp, 0, result, 0, length);
        return result;
    }

    public static final byte[] NULL_BYTES = "null".getBytes();

    public static byte[] getBytesValue(Literal literal, byte[] sql) {
        byte[] value = null;
        if (literal == null || literal instanceof LiteralNull) {
            value = NULL_BYTES;
        } else if (literal instanceof LiteralString) {
            long info = ((LiteralString)literal).getString();
            int offset = BytesUtil.getOffset(info) + 1;
            int size = BytesUtil.getSize(info) - 2;
            value = BytesUtil.getValue(sql, offset, size);
        } else if (literal instanceof LiteralNumber) {
            value = String.valueOf(((LiteralNumber)literal).getNumber()).getBytes();
        } else {
            value = String.valueOf(literal.evaluation(Collections.emptyMap(), sql)).getBytes();
        }
        return value;
    }

    /**
     * 特殊情况下使用，仅ASCII码比较
     *
     * @param sql
     * @param bytes
     * @return
     */
    public static boolean equals(String sql, byte[] bytes) {
        if (sql.length() == bytes.length) {
            for (int i = 0; i < bytes.length; i++) {
                if (sql.charAt(i) != bytes[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
