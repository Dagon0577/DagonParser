package parser.token;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public abstract class IntervalUnit {
    public static final int DAY = 1;
    public static final int DAY_HOUR = 2;
    public static final int DAY_MICROSECOND = 3;
    public static final int DAY_MINUTE = 4;
    public static final int DAY_SECOND = 5;
    public static final int HOUR = 6;
    public static final int HOUR_MICROSECOND = 7;
    public static final int HOUR_MINUTE = 8;
    public static final int HOUR_SECOND = 9;
    public static final int MICROSECOND = 10;
    public static final int MINUTE = 11;
    public static final int MINUTE_MICROSECOND = 12;
    public static final int MINUTE_SECOND = 13;
    public static final int MONTH = 14;
    public static final int QUARTER = 15;
    public static final int SECOND = 16;
    public static final int SECOND_MICROSECOND = 17;
    public static final int WEEK = 18;
    public static final int YEAR = 19;
    public static final int YEAR_MONTH = 20;

    public static final String getInfo(int type) {
        return info[type];
    }

    private static final String[] info = new String[24];

    static {
        info[1] = "DAY";
        info[2] = "DAY_HOUR";
        info[3] = "DAY_MICROSECOND";
        info[4] = "DAY_MINUTE";
        info[5] = "DAY_SECOND";
        info[6] = "HOUR";
        info[7] = "HOUR_MICROSECOND";
        info[8] = "HOUR_MINUTE";
        info[9] = "HOUR_SECOND";
        info[10] = "MICROSECOND";
        info[11] = "MINUTE";
        info[12] = "MINUTE_MICROSECOND";
        info[13] = "MINUTE_SECOND";
        info[14] = "MONTH";
        info[15] = "QUARTER";
        info[16] = "SECOND";
        info[17] = "SECOND_MICROSECOND";
        info[18] = "WEEK";
        info[19] = "YEAR";
        info[20] = "YEAR_MONTH";
    }
}
