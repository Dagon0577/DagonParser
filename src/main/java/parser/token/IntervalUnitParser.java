package parser.token;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class IntervalUnitParser {
    public static int get(byte[] sql, int offset, int size) {
        switch (sql[offset]) {
            case 'd':
            case 'D':
                return parseD(sql, offset, size);
            case 'h':
            case 'H':
                return parseH(sql, offset, size);
            case 'm':
            case 'M':
                return parseM(sql, offset, size);
            case 'q':
            case 'Q':
                return parseQ(sql, offset, size);
            case 's':
            case 'S':
                return parseS(sql, offset, size);
            case 'w':
            case 'W':
                return parseW(sql, offset, size);
            case 'y':
            case 'Y':
                return parseY(sql, offset, size);
        }
        return 0;
    }

    private static int parseD(byte[] sql, int offset, int size) {
        if (size < 3 || size > 15) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (sql[2 + offset] == 'Y' || sql[2 + offset] == 'y') {
                if (size == 3) {
                    return IntervalUnit.DAY;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size < 8 || size > 15) {
                        return 0;
                    }
                    if (size == 8 && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u') && (
                        sql[7 + offset] == 'R' || sql[7 + offset] == 'r')) {
                        return IntervalUnit.DAY_HOUR;
                    }
                    if (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') {
                        if (size < 10 || size > 15) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') {
                            if (size == 15 && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c') && (
                                sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (sql[8 + offset] == 'O'
                                || sql[8 + offset] == 'o') && (sql[9 + offset] == 'S' || sql[9 + offset] == 's') && (
                                sql[10 + offset] == 'E' || sql[10 + offset] == 'e') && (sql[11 + offset] == 'C'
                                || sql[11 + offset] == 'c') && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o') && (
                                sql[13 + offset] == 'N' || sql[13 + offset] == 'n') && (sql[14 + offset] == 'D'
                                || sql[14 + offset] == 'd')) {
                                return IntervalUnit.DAY_MICROSECOND;
                            }
                            if (size == 10 && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (
                                sql[7 + offset] == 'U' || sql[7 + offset] == 'u') && (sql[8 + offset] == 'T'
                                || sql[8 + offset] == 't') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                                return IntervalUnit.DAY_MINUTE;
                            }
                        }
                    }
                    if (size == 10 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e') && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c') && (
                        sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (sql[8 + offset] == 'N'
                        || sql[8 + offset] == 'n') && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')) {
                        return IntervalUnit.DAY_SECOND;
                    }
                }
            }
        }
        return 0;
    }

    private static int parseH(byte[] sql, int offset, int size) {
        if (size < 4 || size > 16) {
            return 0;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') {
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size == 4) {
                        return IntervalUnit.HOUR;
                    }
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (size < 11 || size > 16) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'M' || sql[5 + offset] == 'm') {
                            if (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') {
                                if (size == 16 && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (
                                    sql[8 + offset] == 'R' || sql[8 + offset] == 'r') && (sql[9 + offset] == 'O'
                                    || sql[9 + offset] == 'o') && (sql[10 + offset] == 'S' || sql[10 + offset] == 's')
                                    && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e') && (sql[12 + offset] == 'C'
                                    || sql[12 + offset] == 'c') && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                                    && (sql[14 + offset] == 'N' || sql[14 + offset] == 'n') && (sql[15 + offset] == 'D'
                                    || sql[15 + offset] == 'd')) {
                                    return IntervalUnit.HOUR_MICROSECOND;
                                }
                                if (size == 11 && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (
                                    sql[8 + offset] == 'U' || sql[8 + offset] == 'u') && (sql[9 + offset] == 'T'
                                    || sql[9 + offset] == 't') && (sql[10 + offset] == 'E'
                                    || sql[10 + offset] == 'e')) {
                                    return IntervalUnit.HOUR_MINUTE;
                                }
                            }
                        }
                        if (size == 11 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E'
                            || sql[6 + offset] == 'e') && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (
                            sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N'
                            || sql[9 + offset] == 'n') && (sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                            return IntervalUnit.HOUR_SECOND;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseM(byte[] sql, int offset, int size) {
        if (size < 5 || size > 18) {
            return 0;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 6 || size > 18) {
                return 0;
            }
            if (size == 11 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (sql[8 + offset] == 'O'
                || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n') && (
                sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                return IntervalUnit.MICROSECOND;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') {
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') {
                            if (size == 6) {
                                return IntervalUnit.MINUTE;
                            }
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (size < 13 || size > 18) {
                                    return 0;
                                }
                                if (size == 18 && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm') && (
                                    sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (sql[9 + offset] == 'C'
                                    || sql[9 + offset] == 'c') && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')
                                    && (sql[11 + offset] == 'O' || sql[11 + offset] == 'o') && (sql[12 + offset] == 'S'
                                    || sql[12 + offset] == 's') && (sql[13 + offset] == 'E' || sql[13 + offset] == 'e')
                                    && (sql[14 + offset] == 'C' || sql[14 + offset] == 'c') && (sql[15 + offset] == 'O'
                                    || sql[15 + offset] == 'o') && (sql[16 + offset] == 'N' || sql[16 + offset] == 'n')
                                    && (sql[17 + offset] == 'D' || sql[17 + offset] == 'd')) {
                                    return IntervalUnit.MINUTE_MICROSECOND;
                                }
                                if (size == 13 && (sql[7 + offset] == 'S' || sql[7 + offset] == 's') && (
                                    sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (sql[9 + offset] == 'C'
                                    || sql[9 + offset] == 'c') && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                                    && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n') && (sql[12 + offset] == 'D'
                                    || sql[12 + offset] == 'd')) {
                                    return IntervalUnit.MINUTE_SECOND;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (size == 5 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') && (sql[2 + offset] == 'N'
            || sql[2 + offset] == 'n') && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'H'
            || sql[4 + offset] == 'h')) {
            return IntervalUnit.MONTH;
        }
        return 0;
    }

    private static int parseQ(byte[] sql, int offset, int size) {
        if (size != 7) {
            return 0;
        }
        if (size == 7 && (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'T'
            || sql[4 + offset] == 't') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R'
            || sql[6 + offset] == 'r')) {
            return IntervalUnit.QUARTER;
        }
        return 0;
    }

    private static int parseS(byte[] sql, int offset, int size) {
        if (size < 6 || size > 18) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') {
                        if (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') {
                            if (size == 6) {
                                return IntervalUnit.SECOND;
                            }
                            if (size == 18 && (sql[6 + offset] == '_' || sql[6 + offset] == '_') && (
                                sql[7 + offset] == 'M' || sql[7 + offset] == 'm') && (sql[8 + offset] == 'I'
                                || sql[8 + offset] == 'i') && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') && (
                                sql[10 + offset] == 'R' || sql[10 + offset] == 'r') && (sql[11 + offset] == 'O'
                                || sql[11 + offset] == 'o') && (sql[12 + offset] == 'S' || sql[12 + offset] == 's') && (
                                sql[13 + offset] == 'E' || sql[13 + offset] == 'e') && (sql[14 + offset] == 'C'
                                || sql[14 + offset] == 'c') && (sql[15 + offset] == 'O' || sql[15 + offset] == 'o') && (
                                sql[16 + offset] == 'N' || sql[16 + offset] == 'n') && (sql[17 + offset] == 'D'
                                || sql[17 + offset] == 'd')) {
                                return IntervalUnit.SECOND_MICROSECOND;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseW(byte[] sql, int offset, int size) {
        if (size != 4) {
            return 0;
        }
        if (size == 4 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'E'
            || sql[2 + offset] == 'e') && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k')) {
            return IntervalUnit.WEEK;
        }
        return 0;
    }

    private static int parseY(byte[] sql, int offset, int size) {
        if (size < 4 || size > 10) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size == 4) {
                        return IntervalUnit.YEAR;
                    }
                    if (size == 10 && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (sql[5 + offset] == 'M'
                        || sql[5 + offset] == 'm') && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o') && (
                        sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'T'
                        || sql[8 + offset] == 't') && (sql[9 + offset] == 'H' || sql[9 + offset] == 'h')) {
                        return IntervalUnit.YEAR_MONTH;
                    }
                }
            }
        }
        return 0;
    }

}
