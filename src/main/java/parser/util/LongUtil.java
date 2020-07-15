package parser.util;

import java.math.BigInteger;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public final class LongUtil {

    private static final byte[] minValue = "-9223372036854775808".getBytes();

    private static final BigInteger TWO_COMPL_REF = BigInteger.ONE.shiftLeft(64);

    public static byte[] toBytes(long i) {
        if (i == Long.MIN_VALUE)
            return minValue;
        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);
        byte[] buf = new byte[size];
        getBytes(i, size, buf);
        return buf;
    }

    static int stringSize(long x) {
        long p = 10;
        for (int i = 1; i < 19; i++) {
            if (x < p)
                return i;
            p = 10 * p;
        }
        return 19;
    }

    static void getBytes(long i, int index, byte[] buf) {
        long q;
        int r;
        int charPos = index;
        byte sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Get 2 digits/iteration using longs until quotient fits into an int
        while (i > Integer.MAX_VALUE) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = (int)(i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = IntegerUtil.digitOnes[r];
            buf[--charPos] = IntegerUtil.digitTens[r];
        }

        // Get 2 digits/iteration using ints
        int q2;
        int i2 = (int)i;
        while (i2 >= 65536) {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = IntegerUtil.digitOnes[r];
            buf[--charPos] = IntegerUtil.digitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        for (; ; ) {
            q2 = (i2 * 52429) >>> (16 + 3);
            r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
            buf[--charPos] = IntegerUtil.digits[r];
            i2 = q2;
            if (i2 == 0)
                break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    public static BigInteger convert2UnsignedInteger(long i) {
        if (i < 0) {
            return TWO_COMPL_REF.add(BigInteger.valueOf(i));
        }
        return BigInteger.valueOf(i);
    }
}
