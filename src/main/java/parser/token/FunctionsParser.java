package cn.hotdb.parser.token;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:15:05
 * 
 */
public class FunctionsParser {
    public static int get(byte[] sql, int offset, int size) {
        switch (sql[offset]) {
            case 'a':
            case 'A':
                return parseA(sql, offset, size);
            case 'b':
            case 'B':
                return parseB(sql, offset, size);
            case 'c':
            case 'C':
                return parseC(sql, offset, size);
            case 'd':
            case 'D':
                return parseD(sql, offset, size);
            case 'e':
            case 'E':
                return parseE(sql, offset, size);
            case 'f':
            case 'F':
                return parseF(sql, offset, size);
            case 'g':
            case 'G':
                return parseG(sql, offset, size);
            case 'h':
            case 'H':
                return parseH(sql, offset, size);
            case 'i':
            case 'I':
                return parseI(sql, offset, size);
            case 'j':
            case 'J':
                return parseJ(sql, offset, size);
            case 'l':
            case 'L':
                return parseL(sql, offset, size);
            case 'm':
            case 'M':
                return parseM(sql, offset, size);
            case 'n':
            case 'N':
                return parseN(sql, offset, size);
            case 'o':
            case 'O':
                return parseO(sql, offset, size);
            case 'p':
            case 'P':
                return parseP(sql, offset, size);
            case 'q':
            case 'Q':
                return parseQ(sql, offset, size);
            case 'r':
            case 'R':
                return parseR(sql, offset, size);
            case 's':
            case 'S':
                return parseS(sql, offset, size);
            case 't':
            case 'T':
                return parseT(sql, offset, size);
            case 'u':
            case 'U':
                return parseU(sql, offset, size);
            case 'v':
            case 'V':
                return parseV(sql, offset, size);
            case 'w':
            case 'W':
                return parseW(sql, offset, size);
            case 'y':
            case 'Y':
                return parseY(sql, offset, size);
        }
        return 0;
    }

    private static int parseA(byte[] sql, int offset, int size) {
        if (size < 3 || size > 18) {
            return 0;
        }
        if (size == 3 && (sql[1 + offset] == 'B' || sql[1 + offset] == 'b')
                && (sql[2 + offset] == 'S' || sql[2 + offset] == 's')) {
            return Functions.ABS;
        }
        if (size == 4 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c')
                && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o')
                && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')) {
            return Functions.ACOS;
        }
        if (sql[1 + offset] == 'D' || sql[1 + offset] == 'd') {
            if (size < 7 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') {
                if (size == 7 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.ADDDATE;
                }
                if (size == 7 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')
                        && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                        && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.ADDTIME;
                }
                if (size == 10 && (sql[3 + offset] == '_') && (sql[4 + offset] == 'M'
                        || sql[4 + offset] == 'm') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'N'
                        || sql[6 + offset] == 'n') && (sql[7 + offset] == 'T'
                        || sql[7 + offset] == 't') && (sql[8 + offset] == 'H'
                        || sql[8 + offset] == 'h') && (sql[9 + offset] == 'S'
                        || sql[9 + offset] == 's')) {
                    return Functions.ADD_MONTHS;
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size != 11) {
                return 0;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 11 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                            && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                            && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                            && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                            && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y')
                            && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                            && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                        return Functions.AES_DECRYPT;
                    }
                    if (size == 11 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                            && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                            && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                            && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                            && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y')
                            && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                            && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                        return Functions.AES_ENCRYPT;
                    }
                }
            }
        }
        if (size == 9 && (sql[1 + offset] == 'N' || sql[1 + offset] == 'n')
                && (sql[2 + offset] == 'Y' || sql[2 + offset] == 'y')
                && (sql[3 + offset] == '_' || sql[3 + offset] == '_')
                && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v')
                && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                && (sql[7 + offset] == 'U' || sql[7 + offset] == 'u')
                && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')) {
            return Functions.ANY_VALUE;
        }
        if (sql[1 + offset] == 'S' || sql[1 + offset] == 's') {
            if (size < 4 || size > 18) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c')
                    && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                    && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')) {
                return Functions.ASCII;
            }
            if (size == 8 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (
                    sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                    && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (sql[7 + offset] == 'R'
                    || sql[7 + offset] == 'r')) {
                return Functions.ASCIISTR;
            }
            if (size == 4 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i')
                    && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')) {
                return Functions.ASIN;
            }
            if (sql[2 + offset] == 'Y' || sql[2 + offset] == 'y') {
                if (size < 15 || size > 18) {
                    return 0;
                }
                if (sql[3 + offset] == 'M' || sql[3 + offset] == 'm') {
                    if (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') {
                        if (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') {
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') {
                                    if (sql[8 + offset] == 'I' || sql[8 + offset] == 'i') {
                                        if (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') {
                                            if (sql[10 + offset] == '_'
                                                    || sql[10 + offset] == '_') {
                                                if (sql[11 + offset] == 'D'
                                                        || sql[11 + offset] == 'd') {
                                                    if (size < 17 || size > 18) {
                                                        return 0;
                                                    }
                                                    if (sql[12 + offset] == 'E'
                                                            || sql[12 + offset] == 'e') {
                                                        if (size == 18
                                                                && (sql[13 + offset] == 'C'
                                                                        || sql[13 + offset] == 'c')
                                                                && (sql[14 + offset] == 'R'
                                                                        || sql[14 + offset] == 'r')
                                                                && (sql[15 + offset] == 'Y'
                                                                        || sql[15 + offset] == 'y')
                                                                && (sql[16 + offset] == 'P'
                                                                        || sql[16 + offset] == 'p')
                                                                && (sql[17 + offset] == 'T'
                                                                        || sql[17
                                                                                + offset] == 't')) {
                                                            return Functions.ASYMMETRIC_DECRYPT;
                                                        }
                                                        if (size == 17
                                                                && (sql[13 + offset] == 'R'
                                                                        || sql[13 + offset] == 'r')
                                                                && (sql[14 + offset] == 'I'
                                                                        || sql[14 + offset] == 'i')
                                                                && (sql[15 + offset] == 'V'
                                                                        || sql[15 + offset] == 'v')
                                                                && (sql[16 + offset] == 'E'
                                                                        || sql[16
                                                                                + offset] == 'e')) {
                                                            return Functions.ASYMMETRIC_DERIVE;
                                                        }
                                                    }
                                                }
                                                if (size == 18
                                                        && (sql[11 + offset] == 'E'
                                                                || sql[11 + offset] == 'e')
                                                        && (sql[12 + offset] == 'N'
                                                                || sql[12 + offset] == 'n')
                                                        && (sql[13 + offset] == 'C'
                                                                || sql[13 + offset] == 'c')
                                                        && (sql[14 + offset] == 'R'
                                                                || sql[14 + offset] == 'r')
                                                        && (sql[15 + offset] == 'Y'
                                                                || sql[15 + offset] == 'y')
                                                        && (sql[16 + offset] == 'P'
                                                                || sql[16 + offset] == 'p')
                                                        && (sql[17 + offset] == 'T'
                                                                || sql[17 + offset] == 't')) {
                                                    return Functions.ASYMMETRIC_ENCRYPT;
                                                }
                                                if (size == 15
                                                        && (sql[11 + offset] == 'S'
                                                                || sql[11 + offset] == 's')
                                                        && (sql[12 + offset] == 'I'
                                                                || sql[12 + offset] == 'i')
                                                        && (sql[13 + offset] == 'G'
                                                                || sql[13 + offset] == 'g')
                                                        && (sql[14 + offset] == 'N'
                                                                || sql[14 + offset] == 'n')) {
                                                    return Functions.ASYMMETRIC_SIGN;
                                                }
                                                if (size == 17
                                                        && (sql[11 + offset] == 'V'
                                                                || sql[11 + offset] == 'v')
                                                        && (sql[12 + offset] == 'E'
                                                                || sql[12 + offset] == 'e')
                                                        && (sql[13 + offset] == 'R'
                                                                || sql[13 + offset] == 'r')
                                                        && (sql[14 + offset] == 'I'
                                                                || sql[14 + offset] == 'i')
                                                        && (sql[15 + offset] == 'F'
                                                                || sql[15 + offset] == 'f')
                                                        && (sql[16 + offset] == 'Y'
                                                                || sql[16 + offset] == 'y')) {
                                                    return Functions.ASYMMETRIC_VERIFY;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (size < 4 || size > 5) {
                return 0;
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') {
                    if (size == 4) {
                        return Functions.ATAN;
                    }
                    if (size == 5 && (sql[4 + offset] == '2' || sql[4 + offset] == '2')) {
                        return Functions.ATAN2;
                    }
                }
            }
        }
        if (size == 3 && (sql[1 + offset] == 'V' || sql[1 + offset] == 'v')
                && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g')) {
            return Functions.AVG;
        }
        return 0;
    }

    private static int parseB(byte[] sql, int offset, int size) {
        if (size < 3 || size > 11) {
            return 0;
        }
        if (size == 9 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e')
                && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')
                && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c')
                && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h')
                && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                && (sql[8 + offset] == 'K' || sql[8 + offset] == 'k')) {
            return Functions.BENCHMARK;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size == 3) {
                    return Functions.BIN;
                }
                if (size == 11 && (sql[3 + offset] == '_' || sql[3 + offset] == '_')
                        && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                        && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                        && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                        && (sql[7 + offset] == 'U' || sql[7 + offset] == 'u')
                        && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u')
                        && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i')
                        && (sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                    return Functions.BIN_TO_UUID;
                }
                if (size == 10 && (sql[3 + offset] == '_' || sql[3 + offset] == '_') && (
                        sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == '_') && (
                        sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'U'
                        || sql[8 + offset] == 'u') && (sql[9 + offset] == 'M'
                        || sql[9 + offset] == 'm')) {
                    return Functions.BIN_TO_NUM;
                }
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 6 || size > 10) {
                    return 0;
                }
                if (size == 6 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (
                        sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (sql[5 + offset] == 'D'
                        || sql[5 + offset] == 'd')) {
                    return Functions.BITAND;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 7 && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                            && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                            && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd')) {
                        return Functions.BIT_AND;
                    }
                    if (size == 9 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u')
                            && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                            && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')) {
                        return Functions.BIT_COUNT;
                    }
                    if (size == 10 && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')
                            && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                            && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                            && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g')
                            && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                            && (sql[9 + offset] == 'H' || sql[9 + offset] == 'h')) {
                        return Functions.BIT_LENGTH;
                    }
                    if (size == 6 && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o')
                            && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
                        return Functions.BIT_OR;
                    }
                    if (size == 7 && (sql[4 + offset] == 'X' || sql[4 + offset] == 'x')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')) {
                        return Functions.BIT_XOR;
                    }
                }
            }
        }
        return 0;
    }

    private static int parseC(byte[] sql, int offset, int size) {
        if (size < 3 || size > 26) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 19) {
                return 0;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 15 || size > 19) {
                    return 0;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') {
                        if (sql[5 + offset] == 'C' || sql[5 + offset] == 'c') {
                            if (sql[6 + offset] == 'C' || sql[6 + offset] == 'c') {
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (sql[8 + offset] == 'S' || sql[8 + offset] == 's') {
                                        if (sql[9 + offset] == 'S' || sql[9 + offset] == 's') {
                                            if (sql[10 + offset] == '_'
                                                    || sql[10 + offset] == '_') {
                                                if (size == 17
                                                        && (sql[11 + offset] == 'C'
                                                                || sql[11 + offset] == 'c')
                                                        && (sql[12 + offset] == 'O'
                                                                || sql[12 + offset] == 'o')
                                                        && (sql[13 + offset] == 'L'
                                                                || sql[13 + offset] == 'l')
                                                        && (sql[14 + offset] == 'U'
                                                                || sql[14 + offset] == 'u')
                                                        && (sql[15 + offset] == 'M'
                                                                || sql[15 + offset] == 'm')
                                                        && (sql[16 + offset] == 'N'
                                                                || sql[16 + offset] == 'n')) {
                                                    return Functions.CAN_ACCESS_COLUMN;
                                                }
                                                if (size == 19
                                                        && (sql[11 + offset] == 'D'
                                                                || sql[11 + offset] == 'd')
                                                        && (sql[12 + offset] == 'A'
                                                                || sql[12 + offset] == 'a')
                                                        && (sql[13 + offset] == 'T'
                                                                || sql[13 + offset] == 't')
                                                        && (sql[14 + offset] == 'A'
                                                                || sql[14 + offset] == 'a')
                                                        && (sql[15 + offset] == 'B'
                                                                || sql[15 + offset] == 'b')
                                                        && (sql[16 + offset] == 'A'
                                                                || sql[16 + offset] == 'a')
                                                        && (sql[17 + offset] == 'S'
                                                                || sql[17 + offset] == 's')
                                                        && (sql[18 + offset] == 'E'
                                                                || sql[18 + offset] == 'e')) {
                                                    return Functions.CAN_ACCESS_DATABASE;
                                                }
                                                if (size == 16
                                                        && (sql[11 + offset] == 'T'
                                                                || sql[11 + offset] == 't')
                                                        && (sql[12 + offset] == 'A'
                                                                || sql[12 + offset] == 'a')
                                                        && (sql[13 + offset] == 'B'
                                                                || sql[13 + offset] == 'b')
                                                        && (sql[14 + offset] == 'L'
                                                                || sql[14 + offset] == 'l')
                                                        && (sql[15 + offset] == 'E'
                                                                || sql[15 + offset] == 'e')) {
                                                    return Functions.CAN_ACCESS_TABLE;
                                                }
                                                if (size == 15
                                                        && (sql[11 + offset] == 'V'
                                                                || sql[11 + offset] == 'v')
                                                        && (sql[12 + offset] == 'I'
                                                                || sql[12 + offset] == 'i')
                                                        && (sql[13 + offset] == 'E'
                                                                || sql[13 + offset] == 'e')
                                                        && (sql[14 + offset] == 'W'
                                                                || sql[14 + offset] == 'w')) {
                                                    return Functions.CAN_ACCESS_VIEW;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (size == 4 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's')
                    && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')) {
                return Functions.CAST;
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') {
                if (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') {
                    if (size == 4) {
                        return Functions.CEIL;
                    }
                    if (size == 7 && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                            && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                            && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g')) {
                        return Functions.CEILING;
                    }
                }
            }
        }
        if (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') {
            if (size < 3 || size > 16) {
                return 0;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                return Functions.CHR;
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size == 4) {
                        return Functions.CHAR;
                    }
                    if (size == 11 && (sql[4 + offset] == '_' || sql[4 + offset] == '_')
                            && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                            && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                            && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                            && (sql[8 + offset] == 'G' || sql[8 + offset] == 'g')
                            && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')
                            && (sql[10 + offset] == 'H' || sql[10 + offset] == 'h')) {
                        return Functions.CHAR_LENGTH;
                    }
                    if (size == 16 && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                            && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                            && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                            && (sql[9 + offset] == '_' || sql[9 + offset] == '_')
                            && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                            && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')
                            && (sql[12 + offset] == 'N' || sql[12 + offset] == 'n')
                            && (sql[13 + offset] == 'G' || sql[13 + offset] == 'g')
                            && (sql[14 + offset] == 'T' || sql[14 + offset] == 't')
                            && (sql[15 + offset] == 'H' || sql[15 + offset] == 'h')) {
                        return Functions.CHARACTER_LENGTH;
                    }
                    if (size == 11 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                            sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (
                            sql[6 + offset] == 'R' || sql[6 + offset] == 'r') && (
                            sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (
                            sql[8 + offset] == 'W' || sql[8 + offset] == 'w') && (
                            sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (
                            sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                        return Functions.CHARTOROWID;
                    }
                    if (size == 7 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')
                            && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                        return Functions.CHARSET;
                    }
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 3 || size > 13) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a')
                    && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')
                    && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                    && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                    && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                    && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                return Functions.COALESCE;
            }
            if (size == 12 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e')
                    && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')
                    && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c')
                    && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                    && (sql[6 + offset] == 'B' || sql[6 + offset] == 'b')
                    && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                    && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                    && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i')
                    && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                    && (sql[11 + offset] == 'Y' || sql[11 + offset] == 'y')) {
                return Functions.COERCIBILITY;
            }
            if (size == 9 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l')
                    && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')
                    && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                    && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                    && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                    && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                    && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')) {
                return Functions.COLLATION;
            }
            if (size == 8 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm')
                    && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p')
                    && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                    && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                    && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')
                    && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')) {
                return Functions.COMPRESS;
            }
            if (size == 7 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (
                    sql[3 + offset] == 'P' || sql[3 + offset] == 'p') && (sql[4 + offset] == 'O'
                    || sql[4 + offset] == 'o') && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                    && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                return Functions.COMPOSE;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 4 || size > 13) {
                    return 0;
                }
                if (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') {
                    if (size < 6 || size > 9) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') {
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (size == 6) {
                                return Functions.CONCAT;
                            }
                            if (size == 9 && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                                    && (sql[7 + offset] == 'W' || sql[7 + offset] == 'w')
                                    && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')) {
                                return Functions.CONCAT_WS;
                            }
                        }
                    }
                }
                if (size == 13 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                        && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                        && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c')
                        && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                        && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                        && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                        && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                        && (sql[10 + offset] == '_' || sql[10 + offset] == '_')
                        && (sql[11 + offset] == 'I' || sql[11 + offset] == 'i')
                        && (sql[12 + offset] == 'D' || sql[12 + offset] == 'd')) {
                    return Functions.CONNECTION_ID;
                }
                if (sql[3 + offset] == 'V' || sql[3 + offset] == 'v') {
                    if (size < 4 || size > 10) {
                        return 0;
                    }
                    if (size == 4) {
                        return Functions.CONV;
                    }
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (size < 7 || size > 10) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') {
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (size == 7) {
                                    return Functions.CONVERT;
                                }
                                if (size == 10 && (sql[7 + offset] == '_' || sql[7 + offset] == '_')
                                        && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                        && (sql[9 + offset] == 'Z' || sql[9 + offset] == 'z')) {
                                    return Functions.CONVERT_TZ;
                                }
                            }
                        }
                    }
                }
            }
            if (size == 3 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's')) {
                return Functions.COS;
            }
            if (size == 4 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (
                    sql[3 + offset] == 'H' || sql[3 + offset] == 'h')) {
                return Functions.COSH;
            }
            if (size == 3 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')) {
                return Functions.COT;
            }
            if (size == 5 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u')
                    && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                    && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
                return Functions.COUNT;
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 5 || size > 26) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c')
                    && (sql[3 + offset] == '3' || sql[3 + offset] == '3')
                    && (sql[4 + offset] == '2' || sql[4 + offset] == '2')) {
                return Functions.CRC32;
            }
            if (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') {
                if (size < 13 || size > 26) {
                    return 0;
                }
                if (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') {
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') {
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') {
                                    if (size < 25 || size > 26) {
                                        return 0;
                                    }
                                    if (sql[8 + offset] == 'S' || sql[8 + offset] == 's') {
                                        if (sql[9 + offset] == 'Y' || sql[9 + offset] == 'y') {
                                            if (sql[10 + offset] == 'M'
                                                    || sql[10 + offset] == 'm') {
                                                if (sql[11 + offset] == 'M'
                                                        || sql[11 + offset] == 'm') {
                                                    if (sql[12 + offset] == 'E'
                                                            || sql[12 + offset] == 'e') {
                                                        if (sql[13 + offset] == 'T'
                                                                || sql[13 + offset] == 't') {
                                                            if (sql[14 + offset] == 'R'
                                                                    || sql[14 + offset] == 'r') {
                                                                if (sql[15 + offset] == 'I'
                                                                        || sql[15
                                                                                + offset] == 'i') {
                                                                    if (sql[16 + offset] == 'C'
                                                                            || sql[16
                                                                                    + offset] == 'c') {
                                                                        if (sql[17 + offset] == '_'
                                                                                || sql[17
                                                                                        + offset] == '_') {
                                                                            if (sql[18
                                                                                    + offset] == 'P'
                                                                                    || sql[18
                                                                                            + offset] == 'p') {
                                                                                if (size == 26
                                                                                        && (sql[19
                                                                                                + offset] == 'R'
                                                                                                || sql[19
                                                                                                        + offset] == 'r')
                                                                                        && (sql[20
                                                                                                + offset] == 'I'
                                                                                                || sql[20
                                                                                                        + offset] == 'i')
                                                                                        && (sql[21
                                                                                                + offset] == 'V'
                                                                                                || sql[21
                                                                                                        + offset] == 'v')
                                                                                        && (sql[22
                                                                                                + offset] == '_'
                                                                                                || sql[22
                                                                                                        + offset] == '_')
                                                                                        && (sql[23
                                                                                                + offset] == 'K'
                                                                                                || sql[23
                                                                                                        + offset] == 'k')
                                                                                        && (sql[24
                                                                                                + offset] == 'E'
                                                                                                || sql[24
                                                                                                        + offset] == 'e')
                                                                                        && (sql[25
                                                                                                + offset] == 'Y'
                                                                                                || sql[25
                                                                                                        + offset] == 'y')) {
                                                                                    return Functions.CREATE_ASYMMETRIC_PRIV_KEY;
                                                                                }
                                                                                if (size == 25
                                                                                        && (sql[19
                                                                                                + offset] == 'U'
                                                                                                || sql[19
                                                                                                        + offset] == 'u')
                                                                                        && (sql[20
                                                                                                + offset] == 'B'
                                                                                                || sql[20
                                                                                                        + offset] == 'b')
                                                                                        && (sql[21
                                                                                                + offset] == '_'
                                                                                                || sql[21
                                                                                                        + offset] == '_')
                                                                                        && (sql[22
                                                                                                + offset] == 'K'
                                                                                                || sql[22
                                                                                                        + offset] == 'k')
                                                                                        && (sql[23
                                                                                                + offset] == 'E'
                                                                                                || sql[23
                                                                                                        + offset] == 'e')
                                                                                        && (sql[24
                                                                                                + offset] == 'Y'
                                                                                                || sql[24
                                                                                                        + offset] == 'y')) {
                                                                                    return Functions.CREATE_ASYMMETRIC_PUB_KEY;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (sql[7 + offset] == 'D' || sql[7 + offset] == 'd') {
                                    if (size < 13 || size > 20) {
                                        return 0;
                                    }
                                    if (size == 20
                                            && (sql[8 + offset] == 'H' || sql[8 + offset] == 'h')
                                            && (sql[9 + offset] == '_' || sql[9 + offset] == '_')
                                            && (sql[10 + offset] == 'P' || sql[10 + offset] == 'p')
                                            && (sql[11 + offset] == 'A' || sql[11 + offset] == 'a')
                                            && (sql[12 + offset] == 'R' || sql[12 + offset] == 'r')
                                            && (sql[13 + offset] == 'A' || sql[13 + offset] == 'a')
                                            && (sql[14 + offset] == 'M' || sql[14 + offset] == 'm')
                                            && (sql[15 + offset] == 'E' || sql[15 + offset] == 'e')
                                            && (sql[16 + offset] == 'T' || sql[16 + offset] == 't')
                                            && (sql[17 + offset] == 'E' || sql[17 + offset] == 'e')
                                            && (sql[18 + offset] == 'R' || sql[18 + offset] == 'r')
                                            && (sql[19 + offset] == 'S'
                                                    || sql[19 + offset] == 's')) {
                                        return Functions.CREATE_DH_PARAMETERS;
                                    }
                                    if (size == 13
                                            && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                            && (sql[9 + offset] == 'G' || sql[9 + offset] == 'g')
                                            && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                                            && (sql[11 + offset] == 'S' || sql[11 + offset] == 's')
                                            && (sql[12 + offset] == 'T'
                                                    || sql[12 + offset] == 't')) {
                                        return Functions.CREATE_DIGEST;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 7 || size > 17) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm')
                    && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                    && (sql[4 + offset] == '_' || sql[4 + offset] == '_')
                    && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd')
                    && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                    && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                    && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')) {
                return Functions.CUME_DIST;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size == 7 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.CURDATE;
                }
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size < 12 || size > 17) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (sql[7 + offset] == '_' || sql[7 + offset] == '_') {
                                    if (size == 12
                                            && (sql[8 + offset] == 'D' || sql[8 + offset] == 'd')
                                            && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a')
                                            && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                                            && (sql[11 + offset] == 'E'
                                                    || sql[11 + offset] == 'e')) {
                                        return Functions.CURRENT_DATE;
                                    }
                                    if (size == 12
                                            && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                            && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                                            && (sql[11 + offset] == 'E'
                                                    || sql[11 + offset] == 'e')) {
                                        return Functions.CURRENT_ROLE;
                                    }
                                    if (sql[8 + offset] == 'T' || sql[8 + offset] == 't') {
                                        if (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') {
                                            if (sql[10 + offset] == 'M'
                                                    || sql[10 + offset] == 'm') {
                                                if (sql[11 + offset] == 'E'
                                                        || sql[11 + offset] == 'e') {
                                                    if (size == 12) {
                                                        return Functions.CURRENT_TIME;
                                                    }
                                                    if (size == 17
                                                            && (sql[12 + offset] == 'S'
                                                                    || sql[12 + offset] == 's')
                                                            && (sql[13 + offset] == 'T'
                                                                    || sql[13 + offset] == 't')
                                                            && (sql[14 + offset] == 'A'
                                                                    || sql[14 + offset] == 'a')
                                                            && (sql[15 + offset] == 'M'
                                                                    || sql[15 + offset] == 'm')
                                                            && (sql[16 + offset] == 'P'
                                                                    || sql[16 + offset] == 'p')) {
                                                        return Functions.CURRENT_TIMESTAMP;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (size == 12
                                            && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u')
                                            && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')
                                            && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                                            && (sql[11 + offset] == 'R'
                                                    || sql[11 + offset] == 'r')) {
                                        return Functions.CURRENT_USER;
                                    }
                                }
                            }
                        }
                    }
                }
                if (size == 7 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')
                        && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                        && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.CURTIME;
                }
            }
        }
        return 0;
    }

    private static int parseD(byte[] sql, int offset, int size) {
        if (size < 3 || size > 11) {
            return 0;
        }
        if (size == 4 && (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') && (
                sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'P'
                || sql[3 + offset] == 'p')) {
            return Functions.DUMP;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 4 || size > 11) {
                    return 0;
                }
                if (size == 8 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a')
                        && (sql[4 + offset] == 'B' || sql[4 + offset] == 'b')
                        && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                        && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')
                        && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                    return Functions.DATABASE;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 4) {
                        return Functions.DATE;
                    }
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (size < 8 || size > 11) {
                            return 0;
                        }
                        if (size == 8 && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                                && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd')
                                && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')) {
                            return Functions.DATE_ADD;
                        }
                        if (size == 11 && (sql[5 + offset] == 'F' || sql[5 + offset] == 'f')
                                && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                                && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                                && (sql[8 + offset] == 'M' || sql[8 + offset] == 'm')
                                && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a')
                                && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                            return Functions.DATE_FORMAT;
                        }
                        if (size == 8 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                                && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u')
                                && (sql[7 + offset] == 'B' || sql[7 + offset] == 'b')) {
                            return Functions.DATE_SUB;
                        }
                    }
                    if (size == 8 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                            && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                            && (sql[6 + offset] == 'F' || sql[6 + offset] == 'f')
                            && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f')) {
                        return Functions.DATEDIFF;
                    }
                }
            }
            if (sql[2 + offset] == 'Y' || sql[2 + offset] == 'y') {
                if (size < 3 || size > 10) {
                    return 0;
                }
                if (size == 3) {
                    return Functions.DAY;
                }
                if (size == 7 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.DAYNAME;
                }
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (size < 9 || size > 10) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'F' || sql[4 + offset] == 'f') {
                        if (size == 10 && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                                && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                                && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                && (sql[9 + offset] == 'H' || sql[9 + offset] == 'h')) {
                            return Functions.DAYOFMONTH;
                        }
                        if (size == 9 && (sql[5 + offset] == 'W' || sql[5 + offset] == 'w')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                                && (sql[8 + offset] == 'K' || sql[8 + offset] == 'k')) {
                            return Functions.DAYOFWEEK;
                        }
                        if (size == 9 && (sql[5 + offset] == 'Y' || sql[5 + offset] == 'y')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a')
                                && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')) {
                            return Functions.DAYOFYEAR;
                        }
                    }
                }
            }
        }
        if (size == 10 && (sql[1 + offset] == 'B' || sql[1 + offset] == 'b') && (
                sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm')
                && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'Z'
                || sql[6 + offset] == 'z') && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n') && (sql[9 + offset] == 'E'
                || sql[9 + offset] == 'e')) {
            return Functions.DBTIMEZONE;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 6 || size > 11) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c')
                    && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o')
                    && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                    && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Functions.DECODE;
            }
            if (size == 7 && (sql[2 + offset] == 'F' || sql[2 + offset] == 'f')
                    && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a')
                    && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                    && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                    && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                return Functions.DEFAULT;
            }
            if (size == 7 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g')
                    && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')
                    && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                    && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                    && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                return Functions.DEGREES;
            }
            if (size == 9 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (
                    sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'M'
                    || sql[4 + offset] == 'm') && (sql[5 + offset] == 'P' || sql[5 + offset] == 'p')
                    && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o') && (sql[7 + offset] == 'S'
                    || sql[7 + offset] == 's') && (sql[8 + offset] == 'E'
                    || sql[8 + offset] == 'e')) {
                return Functions.DECOMPOSE;
            }
            if (size == 10 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')
                    && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                    && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                    && (sql[5 + offset] == '_' || sql[5 + offset] == '_')
                    && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                    && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a')
                    && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                    && (sql[9 + offset] == 'K' || sql[9 + offset] == 'k')) {
                return Functions.DENSE_RANK;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size != 11) {
                    return 0;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 11 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                            && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                            && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                            && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                            && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y')
                            && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                            && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                        return Functions.DES_DECRYPT;
                    }
                    if (size == 11 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                            && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                            && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                            && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                            && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y')
                            && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                            && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                        return Functions.DES_ENCRYPT;
                    }
                }
            }
        }
        return 0;
    }

    private static int parseE(byte[] sql, int offset, int size) {
        if (size < 3 || size > 12) {
            return 0;
        }
        if (size == 3 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l')
                && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')) {
            return Functions.ELT;
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 6 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (size == 6 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o')
                        && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                        && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Functions.ENCODE;
                }
                if (size == 7 && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')
                        && (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y')
                        && (sql[5 + offset] == 'P' || sql[5 + offset] == 'p')
                        && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                    return Functions.ENCRYPT;
                }
            }
        }
        if (sql[1 + offset] == 'X' || sql[1 + offset] == 'x') {
            if (size < 3 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') {
                if (size == 3) {
                    return Functions.EXP;
                }
                if (size == 10 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o')
                        && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                        && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                        && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                        && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                    return Functions.EXPORT_SET;
                }
            }
            if (size == 7 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')
                    && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')
                    && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                    && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c')
                    && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                return Functions.EXTRACT;
            }
        }
        if (size == 12 && (sql[1 + offset] == 'X' || sql[1 + offset] == 'x')
                && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')
                && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')
                && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c')
                && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                && (sql[7 + offset] == 'V' || sql[7 + offset] == 'v')
                && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                && (sql[9 + offset] == 'L' || sql[9 + offset] == 'l')
                && (sql[10 + offset] == 'U' || sql[10 + offset] == 'u')
                && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')) {
            return Functions.ExtractValue;
        }
        return 0;
    }

    private static int parseF(byte[] sql, int offset, int size) {
        if (size < 5 || size > 13) {
            return 0;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 5 || size > 11) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e')
                    && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')
                    && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')) {
                return Functions.FIELD;
            }
            if (size == 11 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')
                    && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                    && (sql[4 + offset] == '_' || sql[4 + offset] == '_')
                    && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                    && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                    && (sql[7 + offset] == '_' || sql[7 + offset] == '_')
                    && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                    && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                    && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                return Functions.FIND_IN_SET;
            }
            if (size == 11 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r')
                    && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                    && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                    && (sql[5 + offset] == '_' || sql[5 + offset] == '_')
                    && (sql[6 + offset] == 'V' || sql[6 + offset] == 'v')
                    && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a')
                    && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                    && (sql[9 + offset] == 'U' || sql[9 + offset] == 'u')
                    && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                return Functions.FIRST_VALUE;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l')
                && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o')
                && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o')
                && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
            return Functions.FLOOR;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 6 || size > 10) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r')
                    && (sql[3 + offset] == 'M' || sql[3 + offset] == 'm')
                    && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                    && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                return Functions.FORMAT;
            }
            if (size == 10 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u')
                    && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                    && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                    && (sql[5 + offset] == '_' || sql[5 + offset] == '_')
                    && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                    && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                    && (sql[8 + offset] == 'W' || sql[8 + offset] == 'w')
                    && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')) {
                return Functions.FOUND_ROWS;
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 7 || size > 13) {
                return 0;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (sql[3 + offset] == 'M' || sql[3 + offset] == 'm') {
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (size == 11 && (sql[5 + offset] == 'B' || sql[5 + offset] == 'b')
                                && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                                && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                                && (sql[9 + offset] == '6' || sql[9 + offset] == '6')
                                && (sql[10 + offset] == '4' || sql[10 + offset] == '4')) {
                            return Functions.FROM_BASE64;
                        }
                        if (size == 9 && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd')
                                && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                && (sql[7 + offset] == 'Y' || sql[7 + offset] == 'y')
                                && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')) {
                            return Functions.FROM_DAYS;
                        }
                        if (size == 7 && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (
                                sql[6 + offset] == 'Z' || sql[6 + offset] == 'z')) {
                            return Functions.FROM_TZ;
                        }
                        if (size == 13 && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u')
                                && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                                && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                                && (sql[8 + offset] == 'X' || sql[8 + offset] == 'x')
                                && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')
                                && (sql[10 + offset] == 'I' || sql[10 + offset] == 'i')
                                && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')
                                && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')) {
                            return Functions.FROM_UNIXTIME;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseG(byte[] sql, int offset, int size) {
        if (size < 8 || size > 28) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 14 || size > 18) {
                return 0;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (sql[3 + offset] == 'M' || sql[3 + offset] == 'm') {
                    if (size == 14 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                            && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                            && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                            && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c')
                            && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                            && (sql[11 + offset] == 'I' || sql[11 + offset] == 'i')
                            && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o')
                            && (sql[13 + offset] == 'N' || sql[13 + offset] == 'n')) {
                        return Functions.GeomCollection;
                    }
                    if (size == 18 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                            && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                            && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                            && (sql[7 + offset] == 'Y' || sql[7 + offset] == 'y')
                            && (sql[8 + offset] == 'C' || sql[8 + offset] == 'c')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                            && (sql[11 + offset] == 'L' || sql[11 + offset] == 'l')
                            && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')
                            && (sql[13 + offset] == 'C' || sql[13 + offset] == 'c')
                            && (sql[14 + offset] == 'T' || sql[14 + offset] == 't')
                            && (sql[15 + offset] == 'I' || sql[15 + offset] == 'i')
                            && (sql[16 + offset] == 'O' || sql[16 + offset] == 'o')
                            && (sql[17 + offset] == 'N' || sql[17 + offset] == 'n')) {
                        return Functions.GeometryCollection;
                    }
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (sql[4 + offset] == 'D' || sql[4 + offset] == 'd') {
                        if (size < 21 || size > 28) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') {
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') {
                                    if (size < 21 || size > 24) {
                                        return 0;
                                    }
                                    if (size == 24
                                            && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                            && (sql[9 + offset] == 'L' || sql[9 + offset] == 'l')
                                            && (sql[10 + offset] == 'U' || sql[10 + offset] == 'u')
                                            && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')
                                            && (sql[12 + offset] == 'N' || sql[12 + offset] == 'n')
                                            && (sql[13 + offset] == '_' || sql[13 + offset] == '_')
                                            && (sql[14 + offset] == 'P' || sql[14 + offset] == 'p')
                                            && (sql[15 + offset] == 'R' || sql[15 + offset] == 'r')
                                            && (sql[16 + offset] == 'I' || sql[16 + offset] == 'i')
                                            && (sql[17 + offset] == 'V' || sql[17 + offset] == 'v')
                                            && (sql[18 + offset] == 'I' || sql[18 + offset] == 'i')
                                            && (sql[19 + offset] == 'L' || sql[19 + offset] == 'l')
                                            && (sql[20 + offset] == 'E' || sql[20 + offset] == 'e')
                                            && (sql[21 + offset] == 'G' || sql[21 + offset] == 'g')
                                            && (sql[22 + offset] == 'E' || sql[22 + offset] == 'e')
                                            && (sql[23 + offset] == 'S'
                                                    || sql[23 + offset] == 's')) {
                                        return Functions.GET_DD_COLUMN_PRIVILEGES;
                                    }
                                    if (size == 21
                                            && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                            && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                                            && (sql[10 + offset] == 'A' || sql[10 + offset] == 'a')
                                            && (sql[11 + offset] == 'T' || sql[11 + offset] == 't')
                                            && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')
                                            && (sql[13 + offset] == '_' || sql[13 + offset] == '_')
                                            && (sql[14 + offset] == 'O' || sql[14 + offset] == 'o')
                                            && (sql[15 + offset] == 'P' || sql[15 + offset] == 'p')
                                            && (sql[16 + offset] == 'T' || sql[16 + offset] == 't')
                                            && (sql[17 + offset] == 'I' || sql[17 + offset] == 'i')
                                            && (sql[18 + offset] == 'O' || sql[18 + offset] == 'o')
                                            && (sql[19 + offset] == 'N' || sql[19 + offset] == 'n')
                                            && (sql[20 + offset] == 'S'
                                                    || sql[20 + offset] == 's')) {
                                        return Functions.GET_DD_CREATE_OPTIONS;
                                    }
                                }
                                if (size == 28 && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                                        && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                                        && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')
                                        && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                                        && (sql[11 + offset] == 'X' || sql[11 + offset] == 'x')
                                        && (sql[12 + offset] == '_' || sql[12 + offset] == '_')
                                        && (sql[13 + offset] == 'S' || sql[13 + offset] == 's')
                                        && (sql[14 + offset] == 'U' || sql[14 + offset] == 'u')
                                        && (sql[15 + offset] == 'B' || sql[15 + offset] == 'b')
                                        && (sql[16 + offset] == '_' || sql[16 + offset] == '_')
                                        && (sql[17 + offset] == 'P' || sql[17 + offset] == 'p')
                                        && (sql[18 + offset] == 'A' || sql[18 + offset] == 'a')
                                        && (sql[19 + offset] == 'R' || sql[19 + offset] == 'r')
                                        && (sql[20 + offset] == 'T' || sql[20 + offset] == 't')
                                        && (sql[21 + offset] == '_' || sql[21 + offset] == '_')
                                        && (sql[22 + offset] == 'L' || sql[22 + offset] == 'l')
                                        && (sql[23 + offset] == 'E' || sql[23 + offset] == 'e')
                                        && (sql[24 + offset] == 'N' || sql[24 + offset] == 'n')
                                        && (sql[25 + offset] == 'G' || sql[25 + offset] == 'g')
                                        && (sql[26 + offset] == 'T' || sql[26 + offset] == 't')
                                        && (sql[27 + offset] == 'H' || sql[27 + offset] == 'h')) {
                                    return Functions.GET_DD_INDEX_SUB_PART_LENGTH;
                                }
                            }
                        }
                    }
                    if (size == 10 && (sql[4 + offset] == 'F' || sql[4 + offset] == 'f')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                            && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm')
                            && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                            && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                        return Functions.GET_FORMAT;
                    }
                    if (size == 8 && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                            && (sql[7 + offset] == 'K' || sql[7 + offset] == 'k')) {
                        return Functions.GET_LOCK;
                    }
                }
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 8 || size > 12) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e')
                    && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a')
                    && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                    && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                    && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')
                    && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
                return Functions.GREATEST;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') {
                    if (sql[4 + offset] == 'P' || sql[4 + offset] == 'p') {
                        if (size == 12 && (sql[5 + offset] == '_' || sql[5 + offset] == '_')
                                && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                                && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c')
                                && (sql[10 + offset] == 'A' || sql[10 + offset] == 'a')
                                && (sql[11 + offset] == 'T' || sql[11 + offset] == 't')) {
                            return Functions.GROUP_CONCAT;
                        }
                        if (size == 8 && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                                && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                                && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g')) {
                            return Functions.GROUPING;
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (size < 11 || size > 13) {
                return 0;
            }
            if (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') {
                if (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') {
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (sql[5 + offset] == 'S' || sql[5 + offset] == 's') {
                            if (sql[6 + offset] == 'U' || sql[6 + offset] == 'u') {
                                if (sql[7 + offset] == 'B' || sql[7 + offset] == 'b') {
                                    if (size == 11
                                            && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                                            && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                                            && (sql[10 + offset] == 'T'
                                                    || sql[10 + offset] == 't')) {
                                        return Functions.GTID_SUBSET;
                                    }
                                    if (size == 13
                                            && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                            && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                            && (sql[10 + offset] == 'A' || sql[10 + offset] == 'a')
                                            && (sql[11 + offset] == 'C' || sql[11 + offset] == 'c')
                                            && (sql[12 + offset] == 'T'
                                                    || sql[12 + offset] == 't')) {
                                        return Functions.GTID_SUBTRACT;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseH(byte[] sql, int offset, int size) {
        if (size < 3 || size > 8) {
            return 0;
        }
        if (size == 3 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e')
                && (sql[2 + offset] == 'X' || sql[2 + offset] == 'x')) {
            return Functions.HEX;
        }
        if (size == 8 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (
                sql[2 + offset] == 'X' || sql[2 + offset] == 'x') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o')
                && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (sql[6 + offset] == 'A'
                || sql[6 + offset] == 'a') && (sql[7 + offset] == 'W' || sql[7 + offset] == 'w')) {
            return Functions.HEXTORAW;
        }
        if (size == 4 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o')
                && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u')
                && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')) {
            return Functions.HOUR;
        }
        return 0;
    }

    private static int parseI(byte[] sql, int offset, int size) {
        if (size < 2 || size > 34) {
            return 0;
        }
        if (size == 11 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c')
                && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u')
                && (sql[3 + offset] == '_' || sql[3 + offset] == '_')
                && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v')
                && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                && (sql[10 + offset] == 'N' || sql[10 + offset] == 'n')) {
            return Functions.ICU_VERSION;
        }
        if (sql[1 + offset] == 'F' || sql[1 + offset] == 'f') {
            if (size < 2 || size > 6) {
                return 0;
            }
            if (size == 2) {
                return Functions.IF;
            }
            if (size == 6 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')
                    && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u')
                    && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')
                    && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')) {
                return Functions.IFNULL;
            }
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size == 2) {
                return Functions.IN;
            }
            if (size == 7 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (
                    sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'C'
                    || sql[4 + offset] == 'c') && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                    && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p')) {
                return Functions.INITCAP;
            }
            if (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') {
                if (size < 9 || size > 10) {
                    return 0;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (size != 9) {
                            return 0;
                        }
                        if (size == 9 && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                                && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')) {
                            return Functions.INET_ATON;
                        }
                        if (size == 9 && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                                && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')) {
                            return Functions.INET_NTOA;
                        }
                    }
                    if (sql[4 + offset] == '6' || sql[4 + offset] == '6') {
                        if (size != 10) {
                            return 0;
                        }
                        if (sql[5 + offset] == '_' || sql[5 + offset] == '_') {
                            if (size == 10 && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                    && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                                    && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                    && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')) {
                                return Functions.INET6_ATON;
                            }
                            if (size == 10 && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                                    && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                                    && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                    && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a')) {
                                return Functions.INET6_NTOA;
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 5 || size > 6) {
                    return 0;
                }
                if (size == 6 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                        && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                    return Functions.INSERT;
                }
                if (size == 5 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')
                        && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
                    return Functions.INSTR;
                }
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 8 || size > 34) {
                    return 0;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') {
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (size < 17 || size > 34) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') {
                                if (sql[7 + offset] == 'L' || sql[7 + offset] == 'l') {
                                    if (sql[8 + offset] == '_' || sql[8 + offset] == '_') {
                                        if (sql[9 + offset] == 'A' || sql[9 + offset] == 'a') {
                                            if (size != 23) {
                                                return 0;
                                            }
                                            if (size == 23
                                                    && (sql[10 + offset] == 'U'
                                                            || sql[10 + offset] == 'u')
                                                    && (sql[11 + offset] == 'T'
                                                            || sql[11 + offset] == 't')
                                                    && (sql[12 + offset] == 'O'
                                                            || sql[12 + offset] == 'o')
                                                    && (sql[13 + offset] == '_'
                                                            || sql[13 + offset] == '_')
                                                    && (sql[14 + offset] == 'I'
                                                            || sql[14 + offset] == 'i')
                                                    && (sql[15 + offset] == 'N'
                                                            || sql[15 + offset] == 'n')
                                                    && (sql[16 + offset] == 'C'
                                                            || sql[16 + offset] == 'c')
                                                    && (sql[17 + offset] == 'R'
                                                            || sql[17 + offset] == 'r')
                                                    && (sql[18 + offset] == 'E'
                                                            || sql[18 + offset] == 'e')
                                                    && (sql[19 + offset] == 'M'
                                                            || sql[19 + offset] == 'm')
                                                    && (sql[20 + offset] == 'E'
                                                            || sql[20 + offset] == 'e')
                                                    && (sql[21 + offset] == 'N'
                                                            || sql[21 + offset] == 'n')
                                                    && (sql[22 + offset] == 'T'
                                                            || sql[22 + offset] == 't')) {
                                                return Functions.INTERNAL_AUTO_INCREMENT;
                                            }
                                            if (size == 23
                                                    && (sql[10 + offset] == 'V'
                                                            || sql[10 + offset] == 'v')
                                                    && (sql[11 + offset] == 'G'
                                                            || sql[11 + offset] == 'g')
                                                    && (sql[12 + offset] == '_'
                                                            || sql[12 + offset] == '_')
                                                    && (sql[13 + offset] == 'R'
                                                            || sql[13 + offset] == 'r')
                                                    && (sql[14 + offset] == 'O'
                                                            || sql[14 + offset] == 'o')
                                                    && (sql[15 + offset] == 'W'
                                                            || sql[15 + offset] == 'w')
                                                    && (sql[16 + offset] == '_'
                                                            || sql[16 + offset] == '_')
                                                    && (sql[17 + offset] == 'L'
                                                            || sql[17 + offset] == 'l')
                                                    && (sql[18 + offset] == 'E'
                                                            || sql[18 + offset] == 'e')
                                                    && (sql[19 + offset] == 'N'
                                                            || sql[19 + offset] == 'n')
                                                    && (sql[20 + offset] == 'G'
                                                            || sql[20 + offset] == 'g')
                                                    && (sql[21 + offset] == 'T'
                                                            || sql[21 + offset] == 't')
                                                    && (sql[22 + offset] == 'H'
                                                            || sql[22 + offset] == 'h')) {
                                                return Functions.INTERNAL_AVG_ROW_LENGTH;
                                            }
                                        }
                                        if (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') {
                                            if (size < 17 || size > 19) {
                                                return 0;
                                            }
                                            if (sql[10 + offset] == 'H'
                                                    || sql[10 + offset] == 'h') {
                                                if (sql[11 + offset] == 'E'
                                                        || sql[11 + offset] == 'e') {
                                                    if (sql[12 + offset] == 'C'
                                                            || sql[12 + offset] == 'c') {
                                                        if (sql[13 + offset] == 'K'
                                                                || sql[13 + offset] == 'k') {
                                                            if (size == 19 && (sql[14
                                                                    + offset] == '_'
                                                                    || sql[14 + offset] == '_')
                                                                    && (sql[15 + offset] == 'T'
                                                                            || sql[15
                                                                                    + offset] == 't')
                                                                    && (sql[16 + offset] == 'I'
                                                                            || sql[16
                                                                                    + offset] == 'i')
                                                                    && (sql[17 + offset] == 'M'
                                                                            || sql[17
                                                                                    + offset] == 'm')
                                                                    && (sql[18 + offset] == 'E'
                                                                            || sql[18
                                                                                    + offset] == 'e')) {
                                                                return Functions.INTERNAL_CHECK_TIME;
                                                            }
                                                            if (size == 17 && (sql[14
                                                                    + offset] == 'S'
                                                                    || sql[14 + offset] == 's')
                                                                    && (sql[15 + offset] == 'U'
                                                                            || sql[15
                                                                                    + offset] == 'u')
                                                                    && (sql[16 + offset] == 'M'
                                                                            || sql[16
                                                                                    + offset] == 'm')) {
                                                                return Functions.INTERNAL_CHECKSUM;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (sql[9 + offset] == 'D' || sql[9 + offset] == 'd') {
                                            if (size < 18 || size > 23) {
                                                return 0;
                                            }
                                            if (sql[10 + offset] == 'A'
                                                    || sql[10 + offset] == 'a') {
                                                if (size < 18 || size > 20) {
                                                    return 0;
                                                }
                                                if (sql[11 + offset] == 'T'
                                                        || sql[11 + offset] == 't') {
                                                    if (sql[12 + offset] == 'A'
                                                            || sql[12 + offset] == 'a') {
                                                        if (sql[13 + offset] == '_'
                                                                || sql[13 + offset] == '_') {
                                                            if (size == 18 && (sql[14
                                                                    + offset] == 'F'
                                                                    || sql[14 + offset] == 'f')
                                                                    && (sql[15 + offset] == 'R'
                                                                            || sql[15
                                                                                    + offset] == 'r')
                                                                    && (sql[16 + offset] == 'E'
                                                                            || sql[16
                                                                                    + offset] == 'e')
                                                                    && (sql[17 + offset] == 'E'
                                                                            || sql[17
                                                                                    + offset] == 'e')) {
                                                                return Functions.INTERNAL_DATA_FREE;
                                                            }
                                                            if (size == 20 && (sql[14
                                                                    + offset] == 'L'
                                                                    || sql[14 + offset] == 'l')
                                                                    && (sql[15 + offset] == 'E'
                                                                            || sql[15
                                                                                    + offset] == 'e')
                                                                    && (sql[16 + offset] == 'N'
                                                                            || sql[16
                                                                                    + offset] == 'n')
                                                                    && (sql[17 + offset] == 'G'
                                                                            || sql[17
                                                                                    + offset] == 'g')
                                                                    && (sql[18 + offset] == 'T'
                                                                            || sql[18
                                                                                    + offset] == 't')
                                                                    && (sql[19 + offset] == 'H'
                                                                            || sql[19
                                                                                    + offset] == 'h')) {
                                                                return Functions.INTERNAL_DATA_LENGTH;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (size == 23
                                                    && (sql[10 + offset] == 'D'
                                                            || sql[10 + offset] == 'd')
                                                    && (sql[11 + offset] == '_'
                                                            || sql[11 + offset] == '_')
                                                    && (sql[12 + offset] == 'C'
                                                            || sql[12 + offset] == 'c')
                                                    && (sql[13 + offset] == 'H'
                                                            || sql[13 + offset] == 'h')
                                                    && (sql[14 + offset] == 'A'
                                                            || sql[14 + offset] == 'a')
                                                    && (sql[15 + offset] == 'R'
                                                            || sql[15 + offset] == 'r')
                                                    && (sql[16 + offset] == '_'
                                                            || sql[16 + offset] == '_')
                                                    && (sql[17 + offset] == 'L'
                                                            || sql[17 + offset] == 'l')
                                                    && (sql[18 + offset] == 'E'
                                                            || sql[18 + offset] == 'e')
                                                    && (sql[19 + offset] == 'N'
                                                            || sql[19 + offset] == 'n')
                                                    && (sql[20 + offset] == 'G'
                                                            || sql[20 + offset] == 'g')
                                                    && (sql[21 + offset] == 'T'
                                                            || sql[21 + offset] == 't')
                                                    && (sql[22 + offset] == 'H'
                                                            || sql[22 + offset] == 'h')) {
                                                return Functions.INTERNAL_DD_CHAR_LENGTH;
                                            }
                                        }
                                        if (sql[9 + offset] == 'G' || sql[9 + offset] == 'g') {
                                            if (size < 29 || size > 34) {
                                                return 0;
                                            }
                                            if (sql[10 + offset] == 'E'
                                                    || sql[10 + offset] == 'e') {
                                                if (sql[11 + offset] == 'T'
                                                        || sql[11 + offset] == 't') {
                                                    if (sql[12 + offset] == '_'
                                                            || sql[12 + offset] == '_') {
                                                        if (size == 29
                                                                && (sql[13 + offset] == 'C'
                                                                        || sql[13 + offset] == 'c')
                                                                && (sql[14 + offset] == 'O'
                                                                        || sql[14 + offset] == 'o')
                                                                && (sql[15 + offset] == 'M'
                                                                        || sql[15 + offset] == 'm')
                                                                && (sql[16 + offset] == 'M'
                                                                        || sql[16 + offset] == 'm')
                                                                && (sql[17 + offset] == 'E'
                                                                        || sql[17 + offset] == 'e')
                                                                && (sql[18 + offset] == 'N'
                                                                        || sql[18 + offset] == 'n')
                                                                && (sql[19 + offset] == 'T'
                                                                        || sql[19 + offset] == 't')
                                                                && (sql[20 + offset] == '_'
                                                                        || sql[20 + offset] == '_')
                                                                && (sql[21 + offset] == 'O'
                                                                        || sql[21 + offset] == 'o')
                                                                && (sql[22 + offset] == 'R'
                                                                        || sql[22 + offset] == 'r')
                                                                && (sql[23 + offset] == '_'
                                                                        || sql[23 + offset] == '_')
                                                                && (sql[24 + offset] == 'E'
                                                                        || sql[24 + offset] == 'e')
                                                                && (sql[25 + offset] == 'R'
                                                                        || sql[25 + offset] == 'r')
                                                                && (sql[26 + offset] == 'R'
                                                                        || sql[26 + offset] == 'r')
                                                                && (sql[27 + offset] == 'O'
                                                                        || sql[27 + offset] == 'o')
                                                                && (sql[28 + offset] == 'R'
                                                                        || sql[28
                                                                                + offset] == 'r')) {
                                                            return Functions.INTERNAL_GET_COMMENT_OR_ERROR;
                                                        }
                                                        if (size == 34
                                                                && (sql[13 + offset] == 'V'
                                                                        || sql[13 + offset] == 'v')
                                                                && (sql[14 + offset] == 'I'
                                                                        || sql[14 + offset] == 'i')
                                                                && (sql[15 + offset] == 'E'
                                                                        || sql[15 + offset] == 'e')
                                                                && (sql[16 + offset] == 'W'
                                                                        || sql[16 + offset] == 'w')
                                                                && (sql[17 + offset] == '_'
                                                                        || sql[17 + offset] == '_')
                                                                && (sql[18 + offset] == 'W'
                                                                        || sql[18 + offset] == 'w')
                                                                && (sql[19 + offset] == 'A'
                                                                        || sql[19 + offset] == 'a')
                                                                && (sql[20 + offset] == 'R'
                                                                        || sql[20 + offset] == 'r')
                                                                && (sql[21 + offset] == 'N'
                                                                        || sql[21 + offset] == 'n')
                                                                && (sql[22 + offset] == 'I'
                                                                        || sql[22 + offset] == 'i')
                                                                && (sql[23 + offset] == 'N'
                                                                        || sql[23 + offset] == 'n')
                                                                && (sql[24 + offset] == 'G'
                                                                        || sql[24 + offset] == 'g')
                                                                && (sql[25 + offset] == '_'
                                                                        || sql[25 + offset] == '_')
                                                                && (sql[26 + offset] == 'O'
                                                                        || sql[26 + offset] == 'o')
                                                                && (sql[27 + offset] == 'R'
                                                                        || sql[27 + offset] == 'r')
                                                                && (sql[28 + offset] == '_'
                                                                        || sql[28 + offset] == '_')
                                                                && (sql[29 + offset] == 'E'
                                                                        || sql[29 + offset] == 'e')
                                                                && (sql[30 + offset] == 'R'
                                                                        || sql[30 + offset] == 'r')
                                                                && (sql[31 + offset] == 'R'
                                                                        || sql[31 + offset] == 'r')
                                                                && (sql[32 + offset] == 'O'
                                                                        || sql[32 + offset] == 'o')
                                                                && (sql[33 + offset] == 'R'
                                                                        || sql[33
                                                                                + offset] == 'r')) {
                                                            return Functions.INTERNAL_GET_VIEW_WARNING_OR_ERROR;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') {
                                            if (size < 21 || size > 33) {
                                                return 0;
                                            }
                                            if (sql[10 + offset] == 'N'
                                                    || sql[10 + offset] == 'n') {
                                                if (sql[11 + offset] == 'D'
                                                        || sql[11 + offset] == 'd') {
                                                    if (sql[12 + offset] == 'E'
                                                            || sql[12 + offset] == 'e') {
                                                        if (sql[13 + offset] == 'X'
                                                                || sql[13 + offset] == 'x') {
                                                            if (sql[14 + offset] == '_'
                                                                    || sql[14 + offset] == '_') {
                                                                if (size == 33 && (sql[15
                                                                        + offset] == 'C'
                                                                        || sql[15 + offset] == 'c')
                                                                        && (sql[16 + offset] == 'O'
                                                                                || sql[16
                                                                                        + offset] == 'o')
                                                                        && (sql[17 + offset] == 'L'
                                                                                || sql[17
                                                                                        + offset] == 'l')
                                                                        && (sql[18 + offset] == 'U'
                                                                                || sql[18
                                                                                        + offset] == 'u')
                                                                        && (sql[19 + offset] == 'M'
                                                                                || sql[19
                                                                                        + offset] == 'm')
                                                                        && (sql[20 + offset] == 'N'
                                                                                || sql[20
                                                                                        + offset] == 'n')
                                                                        && (sql[21 + offset] == '_'
                                                                                || sql[21
                                                                                        + offset] == '_')
                                                                        && (sql[22 + offset] == 'C'
                                                                                || sql[22
                                                                                        + offset] == 'c')
                                                                        && (sql[23 + offset] == 'A'
                                                                                || sql[23
                                                                                        + offset] == 'a')
                                                                        && (sql[24 + offset] == 'R'
                                                                                || sql[24
                                                                                        + offset] == 'r')
                                                                        && (sql[25 + offset] == 'D'
                                                                                || sql[25
                                                                                        + offset] == 'd')
                                                                        && (sql[26 + offset] == 'I'
                                                                                || sql[26
                                                                                        + offset] == 'i')
                                                                        && (sql[27 + offset] == 'N'
                                                                                || sql[27
                                                                                        + offset] == 'n')
                                                                        && (sql[28 + offset] == 'A'
                                                                                || sql[28
                                                                                        + offset] == 'a')
                                                                        && (sql[29 + offset] == 'L'
                                                                                || sql[29
                                                                                        + offset] == 'l')
                                                                        && (sql[30 + offset] == 'I'
                                                                                || sql[30
                                                                                        + offset] == 'i')
                                                                        && (sql[31 + offset] == 'T'
                                                                                || sql[31
                                                                                        + offset] == 't')
                                                                        && (sql[32 + offset] == 'Y'
                                                                                || sql[32
                                                                                        + offset] == 'y')) {
                                                                    return Functions.INTERNAL_INDEX_COLUMN_CARDINALITY;
                                                                }
                                                                if (size == 21 && (sql[15
                                                                        + offset] == 'L'
                                                                        || sql[15 + offset] == 'l')
                                                                        && (sql[16 + offset] == 'E'
                                                                                || sql[16
                                                                                        + offset] == 'e')
                                                                        && (sql[17 + offset] == 'N'
                                                                                || sql[17
                                                                                        + offset] == 'n')
                                                                        && (sql[18 + offset] == 'G'
                                                                                || sql[18
                                                                                        + offset] == 'g')
                                                                        && (sql[19 + offset] == 'T'
                                                                                || sql[19
                                                                                        + offset] == 't')
                                                                        && (sql[20 + offset] == 'H'
                                                                                || sql[20
                                                                                        + offset] == 'h')) {
                                                                    return Functions.INTERNAL_INDEX_LENGTH;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (size == 22
                                                && (sql[9 + offset] == 'K'
                                                        || sql[9 + offset] == 'k')
                                                && (sql[10 + offset] == 'E'
                                                        || sql[10 + offset] == 'e')
                                                && (sql[11 + offset] == 'Y'
                                                        || sql[11 + offset] == 'y')
                                                && (sql[12 + offset] == 'S'
                                                        || sql[12 + offset] == 's')
                                                && (sql[13 + offset] == '_'
                                                        || sql[13 + offset] == '_')
                                                && (sql[14 + offset] == 'D'
                                                        || sql[14 + offset] == 'd')
                                                && (sql[15 + offset] == 'I'
                                                        || sql[15 + offset] == 'i')
                                                && (sql[16 + offset] == 'S'
                                                        || sql[16 + offset] == 's')
                                                && (sql[17 + offset] == 'A'
                                                        || sql[17 + offset] == 'a')
                                                && (sql[18 + offset] == 'B'
                                                        || sql[18 + offset] == 'b')
                                                && (sql[19 + offset] == 'L'
                                                        || sql[19 + offset] == 'l')
                                                && (sql[20 + offset] == 'E'
                                                        || sql[20 + offset] == 'e')
                                                && (sql[21 + offset] == 'D'
                                                        || sql[21 + offset] == 'd')) {
                                            return Functions.INTERNAL_KEYS_DISABLED;
                                        }
                                        if (size == 24
                                                && (sql[9 + offset] == 'M'
                                                        || sql[9 + offset] == 'm')
                                                && (sql[10 + offset] == 'A'
                                                        || sql[10 + offset] == 'a')
                                                && (sql[11 + offset] == 'X'
                                                        || sql[11 + offset] == 'x')
                                                && (sql[12 + offset] == '_'
                                                        || sql[12 + offset] == '_')
                                                && (sql[13 + offset] == 'D'
                                                        || sql[13 + offset] == 'd')
                                                && (sql[14 + offset] == 'A'
                                                        || sql[14 + offset] == 'a')
                                                && (sql[15 + offset] == 'T'
                                                        || sql[15 + offset] == 't')
                                                && (sql[16 + offset] == 'A'
                                                        || sql[16 + offset] == 'a')
                                                && (sql[17 + offset] == '_'
                                                        || sql[17 + offset] == '_')
                                                && (sql[18 + offset] == 'L'
                                                        || sql[18 + offset] == 'l')
                                                && (sql[19 + offset] == 'E'
                                                        || sql[19 + offset] == 'e')
                                                && (sql[20 + offset] == 'N'
                                                        || sql[20 + offset] == 'n')
                                                && (sql[21 + offset] == 'G'
                                                        || sql[21 + offset] == 'g')
                                                && (sql[22 + offset] == 'T'
                                                        || sql[22 + offset] == 't')
                                                && (sql[23 + offset] == 'H'
                                                        || sql[23 + offset] == 'h')) {
                                            return Functions.INTERNAL_MAX_DATA_LENGTH;
                                        }
                                        if (size == 19
                                                && (sql[9 + offset] == 'T'
                                                        || sql[9 + offset] == 't')
                                                && (sql[10 + offset] == 'A'
                                                        || sql[10 + offset] == 'a')
                                                && (sql[11 + offset] == 'B'
                                                        || sql[11 + offset] == 'b')
                                                && (sql[12 + offset] == 'L'
                                                        || sql[12 + offset] == 'l')
                                                && (sql[13 + offset] == 'E'
                                                        || sql[13 + offset] == 'e')
                                                && (sql[14 + offset] == '_'
                                                        || sql[14 + offset] == '_')
                                                && (sql[15 + offset] == 'R'
                                                        || sql[15 + offset] == 'r')
                                                && (sql[16 + offset] == 'O'
                                                        || sql[16 + offset] == 'o')
                                                && (sql[17 + offset] == 'W'
                                                        || sql[17 + offset] == 'w')
                                                && (sql[18 + offset] == 'S'
                                                        || sql[18 + offset] == 's')) {
                                            return Functions.INTERNAL_TABLE_ROWS;
                                        }
                                        if (size == 20
                                                && (sql[9 + offset] == 'U'
                                                        || sql[9 + offset] == 'u')
                                                && (sql[10 + offset] == 'P'
                                                        || sql[10 + offset] == 'p')
                                                && (sql[11 + offset] == 'D'
                                                        || sql[11 + offset] == 'd')
                                                && (sql[12 + offset] == 'A'
                                                        || sql[12 + offset] == 'a')
                                                && (sql[13 + offset] == 'T'
                                                        || sql[13 + offset] == 't')
                                                && (sql[14 + offset] == 'E'
                                                        || sql[14 + offset] == 'e')
                                                && (sql[15 + offset] == '_'
                                                        || sql[15 + offset] == '_')
                                                && (sql[16 + offset] == 'T'
                                                        || sql[16 + offset] == 't')
                                                && (sql[17 + offset] == 'I'
                                                        || sql[17 + offset] == 'i')
                                                && (sql[18 + offset] == 'M'
                                                        || sql[18 + offset] == 'm')
                                                && (sql[19 + offset] == 'E'
                                                        || sql[19 + offset] == 'e')) {
                                            return Functions.INTERNAL_UPDATE_TIME;
                                        }
                                    }
                                }
                            }
                        }
                        if (size == 8 && (sql[5 + offset] == 'V' || sql[5 + offset] == 'v')
                                && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')) {
                            return Functions.INTERVAL;
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'S' || sql[1 + offset] == 's') {
            if (size < 6 || size > 14) {
                return 0;
            }
            if (sql[2 + offset] == '_' || sql[2 + offset] == '_') {
                if (size < 7 || size > 14) {
                    return 0;
                }
                if (size == 12 && (sql[3 + offset] == 'F' || sql[3 + offset] == 'f')
                        && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                        && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                        && (sql[7 + offset] == '_' || sql[7 + offset] == '_')
                        && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                        && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                        && (sql[10 + offset] == 'C' || sql[10 + offset] == 'c')
                        && (sql[11 + offset] == 'K' || sql[11 + offset] == 'k')) {
                    return Functions.IS_FREE_LOCK;
                }
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (sql[4 + offset] == 'P' || sql[4 + offset] == 'p') {
                        if (sql[5 + offset] == 'V' || sql[5 + offset] == 'v') {
                            if (sql[6 + offset] == '4' || sql[6 + offset] == '4') {
                                if (size == 7) {
                                    return Functions.IS_IPV4;
                                }
                                if (sql[7 + offset] == '_' || sql[7 + offset] == '_') {
                                    if (size != 14) {
                                        return 0;
                                    }
                                    if (size == 14
                                            && (sql[8 + offset] == 'C' || sql[8 + offset] == 'c')
                                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                            && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm')
                                            && (sql[11 + offset] == 'P' || sql[11 + offset] == 'p')
                                            && (sql[12 + offset] == 'A' || sql[12 + offset] == 'a')
                                            && (sql[13 + offset] == 'T'
                                                    || sql[13 + offset] == 't')) {
                                        return Functions.IS_IPV4_COMPAT;
                                    }
                                    if (size == 14
                                            && (sql[8 + offset] == 'M' || sql[8 + offset] == 'm')
                                            && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a')
                                            && (sql[10 + offset] == 'P' || sql[10 + offset] == 'p')
                                            && (sql[11 + offset] == 'P' || sql[11 + offset] == 'p')
                                            && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')
                                            && (sql[13 + offset] == 'D'
                                                    || sql[13 + offset] == 'd')) {
                                        return Functions.IS_IPV4_MAPPED;
                                    }
                                }
                            }
                            if (size == 7 && (sql[6 + offset] == '6' || sql[6 + offset] == '6')) {
                                return Functions.IS_IPV6;
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') {
                    if (size < 7 || size > 12) {
                        return 0;
                    }
                    if (size == 12 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')
                            && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                            && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd')
                            && (sql[7 + offset] == '_' || sql[7 + offset] == '_')
                            && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'C' || sql[10 + offset] == 'c')
                            && (sql[11 + offset] == 'K' || sql[11 + offset] == 'k')) {
                        return Functions.IS_USED_LOCK;
                    }
                    if (size == 7 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                            && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                            && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd')) {
                        return Functions.IS_UUID;
                    }
                }
            }
            if (size == 6 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')
                    && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u')
                    && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')
                    && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')) {
                return Functions.ISNULL;
            }
        }
        return 0;
    }

    private static int parseJ(byte[] sql, int offset, int size) {
        if (size < 8 || size > 19) {
            return 0;
        }
        if (sql[1 + offset] == 'S' || sql[1 + offset] == 's') {
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') {
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') {
                            if (size < 10 || size > 17) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'R' || sql[6 + offset] == 'r') {
                                if (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') {
                                    if (sql[8 + offset] == 'A' || sql[8 + offset] == 'a') {
                                        if (sql[9 + offset] == 'Y' || sql[9 + offset] == 'y') {
                                            if (size == 10) {
                                                return Functions.JSON_ARRAY;
                                            }
                                            if (sql[10 + offset] == '_'
                                                    || sql[10 + offset] == '_') {
                                                if (size != 17) {
                                                    return 0;
                                                }
                                                if (size == 17
                                                        && (sql[11 + offset] == 'A'
                                                                || sql[11 + offset] == 'a')
                                                        && (sql[12 + offset] == 'P'
                                                                || sql[12 + offset] == 'p')
                                                        && (sql[13 + offset] == 'P'
                                                                || sql[13 + offset] == 'p')
                                                        && (sql[14 + offset] == 'E'
                                                                || sql[14 + offset] == 'e')
                                                        && (sql[15 + offset] == 'N'
                                                                || sql[15 + offset] == 'n')
                                                        && (sql[16 + offset] == 'D'
                                                                || sql[16 + offset] == 'd')) {
                                                    return Functions.JSON_ARRAY_APPEND;
                                                }
                                                if (size == 17
                                                        && (sql[11 + offset] == 'I'
                                                                || sql[11 + offset] == 'i')
                                                        && (sql[12 + offset] == 'N'
                                                                || sql[12 + offset] == 'n')
                                                        && (sql[13 + offset] == 'S'
                                                                || sql[13 + offset] == 's')
                                                        && (sql[14 + offset] == 'E'
                                                                || sql[14 + offset] == 'e')
                                                        && (sql[15 + offset] == 'R'
                                                                || sql[15 + offset] == 'r')
                                                        && (sql[16 + offset] == 'T'
                                                                || sql[16 + offset] == 't')) {
                                                    return Functions.JSON_ARRAY_INSERT;
                                                }
                                            }
                                            if (size == 13
                                                    && (sql[10 + offset] == 'A'
                                                            || sql[10 + offset] == 'a')
                                                    && (sql[11 + offset] == 'G'
                                                            || sql[11 + offset] == 'g')
                                                    && (sql[12 + offset] == 'G'
                                                            || sql[12 + offset] == 'g')) {
                                                return Functions.JSON_ARRAYAGG;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (sql[5 + offset] == 'C' || sql[5 + offset] == 'c') {
                            if (size < 13 || size > 18) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'O' || sql[6 + offset] == 'o') {
                                if (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') {
                                    if (sql[8 + offset] == 'T' || sql[8 + offset] == 't') {
                                        if (sql[9 + offset] == 'A' || sql[9 + offset] == 'a') {
                                            if (sql[10 + offset] == 'I'
                                                    || sql[10 + offset] == 'i') {
                                                if (sql[11 + offset] == 'N'
                                                        || sql[11 + offset] == 'n') {
                                                    if (sql[12 + offset] == 'S'
                                                            || sql[12 + offset] == 's') {
                                                        if (size == 13) {
                                                            return Functions.JSON_CONTAINS;
                                                        }
                                                        if (size == 18
                                                                && (sql[13 + offset] == '_'
                                                                        || sql[13 + offset] == '_')
                                                                && (sql[14 + offset] == 'P'
                                                                        || sql[14 + offset] == 'p')
                                                                && (sql[15 + offset] == 'A'
                                                                        || sql[15 + offset] == 'a')
                                                                && (sql[16 + offset] == 'T'
                                                                        || sql[16 + offset] == 't')
                                                                && (sql[17 + offset] == 'H'
                                                                        || sql[17
                                                                                + offset] == 'h')) {
                                                            return Functions.JSON_CONTAINS_PATH;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (size == 10 && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'P' || sql[7 + offset] == 'p')
                                && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                && (sql[9 + offset] == 'H' || sql[9 + offset] == 'h')) {
                            return Functions.JSON_DEPTH;
                        }
                        if (size == 12 && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                                && (sql[6 + offset] == 'X' || sql[6 + offset] == 'x')
                                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                                && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a')
                                && (sql[10 + offset] == 'C' || sql[10 + offset] == 'c')
                                && (sql[11 + offset] == 'T' || sql[11 + offset] == 't')) {
                            return Functions.JSON_EXTRACT;
                        }
                        if (size == 11 && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                                && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                                && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                                && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                            return Functions.JSON_INSERT;
                        }
                        if (size == 9 && (sql[5 + offset] == 'K' || sql[5 + offset] == 'k')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'Y' || sql[7 + offset] == 'y')
                                && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')) {
                            return Functions.JSON_KEYS;
                        }
                        if (size == 11 && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                                && (sql[8 + offset] == 'G' || sql[8 + offset] == 'g')
                                && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')
                                && (sql[10 + offset] == 'H' || sql[10 + offset] == 'h')) {
                            return Functions.JSON_LENGTH;
                        }
                        if (sql[5 + offset] == 'M' || sql[5 + offset] == 'm') {
                            if (size < 10 || size > 19) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') {
                                    if (sql[8 + offset] == 'G' || sql[8 + offset] == 'g') {
                                        if (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') {
                                            if (size == 10) {
                                                return Functions.JSON_MERGE;
                                            }
                                            if (sql[10 + offset] == '_'
                                                    || sql[10 + offset] == '_') {
                                                if (size < 16 || size > 19) {
                                                    return 0;
                                                }
                                                if (sql[11 + offset] == 'P'
                                                        || sql[11 + offset] == 'p') {
                                                    if (size == 16
                                                            && (sql[12 + offset] == 'A'
                                                                    || sql[12 + offset] == 'a')
                                                            && (sql[13 + offset] == 'T'
                                                                    || sql[13 + offset] == 't')
                                                            && (sql[14 + offset] == 'C'
                                                                    || sql[14 + offset] == 'c')
                                                            && (sql[15 + offset] == 'H'
                                                                    || sql[15 + offset] == 'h')) {
                                                        return Functions.JSON_MERGE_PATCH;
                                                    }
                                                    if (size == 19
                                                            && (sql[12 + offset] == 'R'
                                                                    || sql[12 + offset] == 'r')
                                                            && (sql[13 + offset] == 'E'
                                                                    || sql[13 + offset] == 'e')
                                                            && (sql[14 + offset] == 'S'
                                                                    || sql[14 + offset] == 's')
                                                            && (sql[15 + offset] == 'E'
                                                                    || sql[15 + offset] == 'e')
                                                            && (sql[16 + offset] == 'R'
                                                                    || sql[16 + offset] == 'r')
                                                            && (sql[17 + offset] == 'V'
                                                                    || sql[17 + offset] == 'v')
                                                            && (sql[18 + offset] == 'E'
                                                                    || sql[18 + offset] == 'e')) {
                                                        return Functions.JSON_MERGE_PRESERVE;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (sql[5 + offset] == 'O' || sql[5 + offset] == 'o') {
                            if (size < 11 || size > 14) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'B' || sql[6 + offset] == 'b') {
                                if (sql[7 + offset] == 'J' || sql[7 + offset] == 'j') {
                                    if (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') {
                                        if (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') {
                                            if (sql[10 + offset] == 'T'
                                                    || sql[10 + offset] == 't') {
                                                if (size == 11) {
                                                    return Functions.JSON_OBJECT;
                                                }
                                                if (size == 14
                                                        && (sql[11 + offset] == 'A'
                                                                || sql[11 + offset] == 'a')
                                                        && (sql[12 + offset] == 'G'
                                                                || sql[12 + offset] == 'g')
                                                        && (sql[13 + offset] == 'G'
                                                                || sql[13 + offset] == 'g')) {
                                                    return Functions.JSON_OBJECTAGG;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (size == 11 && (sql[5 + offset] == 'P' || sql[5 + offset] == 'p')
                                && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                                && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')
                                && (sql[10 + offset] == 'Y' || sql[10 + offset] == 'y')) {
                            return Functions.JSON_PRETTY;
                        }
                        if (size == 10 && (sql[5 + offset] == 'Q' || sql[5 + offset] == 'q')
                                && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u')
                                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                            return Functions.JSON_QUOTE;
                        }
                        if (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') {
                            if (size < 11 || size > 12) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (size == 11 && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm')
                                        && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                        && (sql[9 + offset] == 'V' || sql[9 + offset] == 'v')
                                        && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                                    return Functions.JSON_REMOVE;
                                }
                                if (size == 12 && (sql[7 + offset] == 'P' || sql[7 + offset] == 'p')
                                        && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                                        && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a')
                                        && (sql[10 + offset] == 'C' || sql[10 + offset] == 'c')
                                        && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')) {
                                    return Functions.JSON_REPLACE;
                                }
                            }
                        }
                        if (sql[5 + offset] == 'S' || sql[5 + offset] == 's') {
                            if (size < 8 || size > 17) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (size < 8 || size > 11) {
                                    return 0;
                                }
                                if (size == 11 && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a')
                                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                        && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c')
                                        && (sql[10 + offset] == 'H' || sql[10 + offset] == 'h')) {
                                    return Functions.JSON_SEARCH;
                                }
                                if (size == 8
                                        && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
                                    return Functions.JSON_SET;
                                }
                            }
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') {
                                    if (sql[8 + offset] == 'R' || sql[8 + offset] == 'r') {
                                        if (sql[9 + offset] == 'A' || sql[9 + offset] == 'a') {
                                            if (sql[10 + offset] == 'G'
                                                    || sql[10 + offset] == 'g') {
                                                if (sql[11 + offset] == 'E'
                                                        || sql[11 + offset] == 'e') {
                                                    if (sql[12 + offset] == '_'
                                                            || sql[12 + offset] == '_') {
                                                        if (size == 17
                                                                && (sql[13 + offset] == 'F'
                                                                        || sql[13 + offset] == 'f')
                                                                && (sql[14 + offset] == 'R'
                                                                        || sql[14 + offset] == 'r')
                                                                && (sql[15 + offset] == 'E'
                                                                        || sql[15 + offset] == 'e')
                                                                && (sql[16 + offset] == 'E'
                                                                        || sql[16
                                                                                + offset] == 'e')) {
                                                            return Functions.JSON_STORAGE_FREE;
                                                        }
                                                        if (size == 17
                                                                && (sql[13 + offset] == 'S'
                                                                        || sql[13 + offset] == 's')
                                                                && (sql[14 + offset] == 'I'
                                                                        || sql[14 + offset] == 'i')
                                                                && (sql[15 + offset] == 'Z'
                                                                        || sql[15 + offset] == 'z')
                                                                && (sql[16 + offset] == 'E'
                                                                        || sql[16
                                                                                + offset] == 'e')) {
                                                            return Functions.JSON_STORAGE_SIZE;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (size < 9 || size > 10) {
                                return 0;
                            }
                            if (size == 10 && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                    && (sql[7 + offset] == 'B' || sql[7 + offset] == 'b')
                                    && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                                    && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                                return Functions.JSON_TABLE;
                            }
                            if (size == 9 && (sql[6 + offset] == 'Y' || sql[6 + offset] == 'y')
                                    && (sql[7 + offset] == 'P' || sql[7 + offset] == 'p')
                                    && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')) {
                                return Functions.JSON_TYPE;
                            }
                        }
                        if (size == 12 && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u')
                                && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                                && (sql[7 + offset] == 'Q' || sql[7 + offset] == 'q')
                                && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u')
                                && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                                && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')) {
                            return Functions.JSON_UNQUOTE;
                        }
                        if (size == 10 && (sql[5 + offset] == 'V' || sql[5 + offset] == 'v')
                                && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                                && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')) {
                            return Functions.JSON_VALID;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseL(byte[] sql, int offset, int size) {
        if (size < 2 || size > 14) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 3 || size > 14) {
                return 0;
            }
            if (size == 3 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g')) {
                return Functions.LAG;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 8 || size > 14) {
                    return 0;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (size == 8 && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (
                                sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (
                                sql[7 + offset] == 'Y' || sql[7 + offset] == 'y')) {
                            return Functions.LAST_DAY;
                        }
                        if (size == 14 && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                                && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                                && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                                && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                                && (sql[11 + offset] == '_' || sql[11 + offset] == '_')
                                && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i')
                                && (sql[13 + offset] == 'D' || sql[13 + offset] == 'd')) {
                            return Functions.LAST_INSERT_ID;
                        }
                        if (size == 10 && (sql[5 + offset] == 'V' || sql[5 + offset] == 'v')
                                && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                                && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u')
                                && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                            return Functions.LAST_VALUE;
                        }
                    }
                }
            }
        }
        if (size == 5 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c')
                && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a')
                && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
            return Functions.LCASE;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (size < 4 || size > 5) {
                    return 0;
                }
                if (size == 4 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')) {
                    return Functions.LEAD;
                }
                if (size == 5 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                        && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
                    return Functions.LEAST;
                }
            }
            if (size == 4 && (sql[2 + offset] == 'F' || sql[2 + offset] == 'f')
                    && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')) {
                return Functions.LEFT;
            }
            if (size == 6 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')
                    && (sql[3 + offset] == 'G' || sql[3 + offset] == 'g')
                    && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                    && (sql[5 + offset] == 'H' || sql[5 + offset] == 'h')) {
                return Functions.LENGTH;
            }
            if (size == 7 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (
                    sql[3 + offset] == 'G' || sql[3 + offset] == 'g') && (sql[4 + offset] == 'T'
                    || sql[4 + offset] == 't') && (sql[5 + offset] == 'H' || sql[5 + offset] == 'h')
                    && (sql[6 + offset] == 'B' || sql[6 + offset] == 'b')) {
                return Functions.LENGTHB;
            }
        }
        if (size == 10 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i')
                && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')
                && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')
                && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                && (sql[9 + offset] == 'G' || sql[9 + offset] == 'g')) {
            return Functions.LineString;
        }
        if (size == 2 && (sql[1 + offset] == 'N' || sql[1 + offset] == 'n')) {
            return Functions.LN;
        }
        if (size == 5 && (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') && (
                sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'V'
                || sql[3 + offset] == 'v') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')) {
            return Functions.LNNVL;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size == 9 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a')
                    && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                    && (sql[4 + offset] == '_' || sql[4 + offset] == '_')
                    && (sql[5 + offset] == 'F' || sql[5 + offset] == 'f')
                    && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                    && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                    && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')) {
                return Functions.LOAD_FILE;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (size < 6 || size > 14) {
                    return 0;
                }
                if (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') {
                    if (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') {
                        if (size < 9 || size > 14) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') {
                                if (sql[7 + offset] == 'M' || sql[7 + offset] == 'm') {
                                    if (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') {
                                        if (size == 9) {
                                            return Functions.LOCALTIME;
                                        }
                                        if (size == 14
                                                && (sql[9 + offset] == 'S'
                                                        || sql[9 + offset] == 's')
                                                && (sql[10 + offset] == 'T'
                                                        || sql[10 + offset] == 't')
                                                && (sql[11 + offset] == 'A'
                                                        || sql[11 + offset] == 'a')
                                                && (sql[12 + offset] == 'M'
                                                        || sql[12 + offset] == 'm')
                                                && (sql[13 + offset] == 'P'
                                                        || sql[13 + offset] == 'p')) {
                                            return Functions.LOCALTIMESTAMP;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (size == 6 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                            && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                        return Functions.LOCATE;
                    }
                }
            }
            if (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') {
                if (size < 3 || size > 5) {
                    return 0;
                }
                if (size == 3) {
                    return Functions.LOG;
                }
                if (size == 5 && (sql[3 + offset] == '1' || sql[3 + offset] == '1')
                        && (sql[4 + offset] == '0' || sql[4 + offset] == '0')) {
                    return Functions.LOG10;
                }
                if (size == 4 && (sql[3 + offset] == '2' || sql[3 + offset] == '2')) {
                    return Functions.LOG2;
                }
            }
            if (size == 5 && (sql[2 + offset] == 'W' || sql[2 + offset] == 'w')
                    && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                    && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
                return Functions.LOWER;
            }
        }
        if (size == 4 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p')
                && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a')
                && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')) {
            return Functions.LPAD;
        }
        if (size == 5 && (sql[1 + offset] == 'T' || sql[1 + offset] == 't')
                && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r')
                && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm')) {
            return Functions.LTRIM;
        }
        return 0;
    }

    private static int parseM(byte[] sql, int offset, int size) {
        if (size < 3 || size > 15) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (sql[2 + offset] == 'K' || sql[2 + offset] == 'k') {
                if (size != 8) {
                    return 0;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 8 && (sql[4 + offset] == '_' || sql[4 + offset] == '_')
                            && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                            && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                            && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
                        return Functions.MAKE_SET;
                    }
                    if (size == 8 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                            && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Functions.MAKEDATE;
                    }
                    if (size == 8 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                            && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                            && (sql[6 + offset] == 'M' || sql[6 + offset] == 'm')
                            && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Functions.MAKETIME;
                    }
                }
            }
            if (size == 15 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's')
                    && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')
                    && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                    && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r')
                    && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                    && (sql[7 + offset] == 'P' || sql[7 + offset] == 'p')
                    && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                    && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')
                    && (sql[10 + offset] == '_' || sql[10 + offset] == '_')
                    && (sql[11 + offset] == 'W' || sql[11 + offset] == 'w')
                    && (sql[12 + offset] == 'A' || sql[12 + offset] == 'a')
                    && (sql[13 + offset] == 'I' || sql[13 + offset] == 'i')
                    && (sql[14 + offset] == 'T' || sql[14 + offset] == 't')) {
                return Functions.MASTER_POS_WAIT;
            }
            if (size == 3 && (sql[2 + offset] == 'X' || sql[2 + offset] == 'x')) {
                return Functions.MAX;
            }
        }
        if (sql[1 + offset] == 'B' || sql[1 + offset] == 'b') {
            if (size < 9 || size > 13) {
                return 0;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') {
                    if (size < 9 || size > 12) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (size == 11 && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                                && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                                && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a')
                                && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                                && (sql[10 + offset] == 'S' || sql[10 + offset] == 's')) {
                            return Functions.MBRContains;
                        }
                        if (sql[5 + offset] == 'V' || sql[5 + offset] == 'v') {
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') {
                                    if (size == 12
                                            && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                                            && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')
                                            && (sql[10 + offset] == 'B' || sql[10 + offset] == 'b')
                                            && (sql[11 + offset] == 'Y'
                                                    || sql[11 + offset] == 'y')) {
                                        return Functions.MBRCoveredBy;
                                    }
                                    if (size == 9
                                            && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')) {
                                        return Functions.MBRCovers;
                                    }
                                }
                            }
                        }
                    }
                }
                if (size == 11 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                        && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                        && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                        && (sql[6 + offset] == 'J' || sql[6 + offset] == 'j')
                        && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                        && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                        && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                        && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                    return Functions.MBRDisjoint;
                }
                if (size == 9 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                        && (sql[4 + offset] == 'Q' || sql[4 + offset] == 'q')
                        && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u')
                        && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                        && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                        && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')) {
                    return Functions.MBREquals;
                }
                if (size == 13 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                        && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                        && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                        && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                        && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                        && (sql[10 + offset] == 'C' || sql[10 + offset] == 'c')
                        && (sql[11 + offset] == 'T' || sql[11 + offset] == 't')
                        && (sql[12 + offset] == 'S' || sql[12 + offset] == 's')) {
                    return Functions.MBRIntersects;
                }
                if (size == 11 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o')
                        && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v')
                        && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                        && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                        && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                        && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                        && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                        && (sql[10 + offset] == 'S' || sql[10 + offset] == 's')) {
                    return Functions.MBROverlaps;
                }
                if (size == 10 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')
                        && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o')
                        && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u')
                        && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                        && (sql[7 + offset] == 'H' || sql[7 + offset] == 'h')
                        && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                        && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')) {
                    return Functions.MBRTouches;
                }
                if (size == 9 && (sql[3 + offset] == 'W' || sql[3 + offset] == 'w')
                        && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == 'H' || sql[6 + offset] == 'h')
                        && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                        && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')) {
                    return Functions.MBRWithin;
                }
            }
        }
        if (size == 3 && (sql[1 + offset] == 'D' || sql[1 + offset] == 'd')
                && (sql[2 + offset] == '5' || sql[2 + offset] == '5')) {
            return Functions.MD5;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 3 || size > 11) {
                return 0;
            }
            if (size == 11 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c')
                    && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')
                    && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o')
                    && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                    && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                    && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c')
                    && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                    && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                    && (sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                return Functions.MICROSECOND;
            }
            if (size == 3 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd')) {
                return Functions.MID;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 3 || size > 6) {
                    return 0;
                }
                if (size == 3) {
                    return Functions.MIN;
                }
                if (size == 6 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u')
                        && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                        && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Functions.MINUTE;
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 3 || size > 14) {
                return 0;
            }
            if (size == 3 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd')) {
                return Functions.MOD;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 5 || size > 14) {
                    return 0;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == 'H' || sql[4 + offset] == 'h') {
                        if (size == 5) {
                            return Functions.MONTH;
                        }
                        if (size == 9 && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                                && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm')
                                && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')) {
                            return Functions.MONTHNAME;
                        }
                        if (size == 14 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (
                                sql[6 + offset] == '_') && (sql[7 + offset] == 'B'
                                || sql[7 + offset] == 'b') && (sql[8 + offset] == 'E'
                                || sql[8 + offset] == 'e') && (sql[9 + offset] == 'T'
                                || sql[9 + offset] == 't') && (sql[10 + offset] == 'W'
                                || sql[10 + offset] == 'w') && (sql[11 + offset] == 'E'
                                || sql[11 + offset] == 'e') && (sql[12 + offset] == 'E'
                                || sql[12 + offset] == 'e') && (sql[13 + offset] == 'N'
                                || sql[13 + offset] == 'n')) {
                            return Functions.MONTHS_BETWEEN;
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 10 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (size == 15 && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                                && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                                && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                                && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')
                                && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                                && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')
                                && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i')
                                && (sql[13 + offset] == 'N' || sql[13 + offset] == 'n')
                                && (sql[14 + offset] == 'G' || sql[14 + offset] == 'g')) {
                            return Functions.MultiLineString;
                        }
                        if (sql[5 + offset] == 'P' || sql[5 + offset] == 'p') {
                            if (size < 10 || size > 12) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'O' || sql[6 + offset] == 'o') {
                                if (size == 10 && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                                        && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                                        && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                                    return Functions.MultiPoint;
                                }
                                if (size == 12 && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                                        && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y')
                                        && (sql[9 + offset] == 'G' || sql[9 + offset] == 'g')
                                        && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                                        && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')) {
                                    return Functions.MultiPolygon;
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseN(byte[] sql, int offset, int size) {
        if (size < 3 || size > 20) {
            return 0;
        }
        if (size == 5 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (
                sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'V'
                || sql[3 + offset] == 'v') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')) {
            return Functions.NANVL;
        }
        if (size == 10 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a')
                && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm')
                && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                && (sql[4 + offset] == '_' || sql[4 + offset] == '_')
                && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c')
                && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
            return Functions.NAME_CONST;
        }
        if (size == 8 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e')) {
            if ((sql[2 + offset] == 'X' || sql[2 + offset] == 'x') && (sql[3 + offset] == 'T'
                    || sql[3 + offset] == 't') && (sql[4 + offset] == '_' || sql[4 + offset] == '_')
                    && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (sql[6 + offset] == 'A'
                    || sql[6 + offset] == 'a') && (sql[7 + offset] == 'Y'
                    || sql[7 + offset] == 'y')) {
                return Functions.NEXT_DAY;
            }
            if ((sql[2 + offset] == 'W' || sql[2 + offset] == 'w') && (sql[3 + offset] == '_') && (
                    sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (sql[5 + offset] == 'I'
                    || sql[5 + offset] == 'i') && (sql[6 + offset] == 'M' || sql[6 + offset] == 'm')
                    && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                return Functions.NEW_TIME;
            }
        }
        if (size == 7 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') && (
                sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o')
                && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (sql[6 + offset] == 'T'
                || sql[6 + offset] == 't')) {
            return Functions.NLSSORT;
        }
        if ((sql[1 + offset] == 'L' || sql[1 + offset] == 'l') && (
                sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == '_')) {
            if (size == 9 && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                    sql[5 + offset] == 'O'
                    || sql[5 + offset] == 'o') && (sql[6 + offset] == 'W' || sql[6 + offset] == 'w')
                    && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'R'
                    || sql[8 + offset] == 'r')) {
                return Functions.NLS_LOWER;
            }
            if (size == 9 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (
                    sql[5 + offset] == 'P'
                    || sql[5 + offset] == 'p') && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p')
                    && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'R'
                    || sql[8 + offset] == 'r')) {
                return Functions.NLS_UPPER;
            }
            if (size == 11 && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                    sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'I'
                    || sql[6 + offset] == 'i') && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                    && (sql[8 + offset] == 'C' || sql[8 + offset] == 'c') && (sql[9 + offset] == 'A'
                    || sql[9 + offset] == 'a') && (sql[10 + offset] == 'P'
                    || sql[10 + offset] == 'p')) {
                return Functions.NLS_INITCAP;
            }
            if (size == 20 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (
                    sql[5 + offset] == 'H' || sql[5 + offset] == 'h') && (sql[6 + offset] == 'A'
                    || sql[6 + offset] == 'a') && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                    && (sql[8 + offset] == 'S' || sql[8 + offset] == 's') && (sql[9 + offset] == 'E'
                    || sql[9 + offset] == 'e') && (sql[10 + offset] == 'T'
                    || sql[10 + offset] == 't') && (sql[11 + offset] == '_') && (
                    sql[12 + offset] == 'D' || sql[12 + offset] == 'd') && (sql[13 + offset] == 'E'
                    || sql[13 + offset] == 'e') && (sql[14 + offset] == 'C'
                    || sql[14 + offset] == 'c') && (sql[15 + offset] == 'L'
                    || sql[15 + offset] == 'l') && (sql[16 + offset] == '_') && (
                    sql[17 + offset] == 'L' || sql[17 + offset] == 'l') && (sql[18 + offset] == 'E'
                    || sql[18 + offset] == 'e') && (sql[19 + offset] == 'N'
                    || sql[19 + offset] == 'n')) {
                return Functions.NLS_CHARSET_DECL_LEN;
            }
            if (size == 14 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (
                    sql[5 + offset] == 'H' || sql[5 + offset] == 'h') && (sql[6 + offset] == 'A'
                    || sql[6 + offset] == 'a') && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                    && (sql[8 + offset] == 'S' || sql[8 + offset] == 's') && (sql[9 + offset] == 'E'
                    || sql[9 + offset] == 'e') && (sql[10 + offset] == 'T'
                    || sql[10 + offset] == 't') && (sql[11 + offset] == '_') && (
                    sql[12 + offset] == 'I' || sql[12 + offset] == 'i') && (sql[13 + offset] == 'D'
                    || sql[13 + offset] == 'd')) {
                return Functions.NLS_CHARSET_ID;
            }
            if (size == 16 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (
                    sql[5 + offset] == 'H' || sql[5 + offset] == 'h') && (sql[6 + offset] == 'A'
                    || sql[6 + offset] == 'a') && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                    && (sql[8 + offset] == 'S' || sql[8 + offset] == 's') && (sql[9 + offset] == 'E'
                    || sql[9 + offset] == 'e') && (sql[10 + offset] == 'T'
                    || sql[10 + offset] == 't') && (sql[11 + offset] == '_') && (
                    sql[12 + offset] == 'N' || sql[12 + offset] == 'n') && (sql[13 + offset] == 'A'
                    || sql[13 + offset] == 'a') && (sql[14 + offset] == 'M'
                    || sql[14 + offset] == 'm') && (sql[15 + offset] == 'E'
                    || sql[15 + offset] == 'e')) {
                return Functions.NLS_CHARSET_NAME;
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 3 || size > 5) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')
                    && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                    && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n')) {
                return Functions.NOTIN;
            }
            if (size == 3 && (sql[2 + offset] == 'W' || sql[2 + offset] == 'w')) {
                return Functions.NOW;
            }
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (size < 5 || size > 9) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'H' || sql[2 + offset] == 'h')
                    && (sql[3 + offset] == '_' || sql[3 + offset] == '_')
                    && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v')
                    && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                    && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                    && (sql[7 + offset] == 'U' || sql[7 + offset] == 'u')
                    && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')) {
                return Functions.NTH_VALUE;
            }
            if (size == 5 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i')
                    && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')
                    && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Functions.NTILE;
            }
        }
        if (sql[1 + offset] == 'V' || sql[1 + offset] == 'v') {
            if (size < 3 || size > 4) {
                return 0;
            }
            if (size == 3 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l')) {
                return Functions.NVL;
            }
            if (size == 4 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset]
                    == '2')) {
                return Functions.NVL2;
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size == 6 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (
                    sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'F'
                    || sql[5 + offset] == 'f')) {
                return Functions.NULLIF;
            }
            if (size == 15 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (
                    sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'O'
                    || sql[4 + offset] == 'o') && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd')
                    && (sql[6 + offset] == 'S' || sql[6 + offset] == 's') && (sql[7 + offset] == 'I'
                    || sql[7 + offset] == 'i') && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                    && (sql[9 + offset] == 'T' || sql[9 + offset] == 't') && (
                    sql[10 + offset] == 'E' || sql[10 + offset] == 'e') && (sql[11 + offset] == 'R'
                    || sql[11 + offset] == 'r') && (sql[12 + offset] == 'V'
                    || sql[12 + offset] == 'v') && (sql[13 + offset] == 'A'
                    || sql[13 + offset] == 'a') && (sql[14 + offset] == 'L'
                    || sql[14 + offset] == 'l')) {
                return Functions.NUMTODSINTERVAL;
            }
            if (size == 15 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (
                    sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'O'
                    || sql[4 + offset] == 'o') && (sql[5 + offset] == 'Y' || sql[5 + offset] == 'y')
                    && (sql[6 + offset] == 'M' || sql[6 + offset] == 'm') && (sql[7 + offset] == 'I'
                    || sql[7 + offset] == 'i') && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                    && (sql[9 + offset] == 'T' || sql[9 + offset] == 't') && (
                    sql[10 + offset] == 'E' || sql[10 + offset] == 'e') && (sql[11 + offset] == 'R'
                    || sql[11 + offset] == 'r') && (sql[12 + offset] == 'V'
                    || sql[12 + offset] == 'v') && (sql[13 + offset] == 'A'
                    || sql[13 + offset] == 'a') && (sql[14 + offset] == 'L'
                    || sql[14 + offset] == 'l')) {
                return Functions.NUMTOYMINTERVAL;
            }
        }
        return 0;
    }

    private static int parseO(byte[] sql, int offset, int size) {
        if (size < 3 || size > 12) {
            return 0;
        }
        if (sql[1 + offset] == 'C' || sql[1 + offset] == 'c') {
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size == 3) {
                    return Functions.OCT;
                }
                if (size == 12 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                        && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                        && (sql[5 + offset] == '_' || sql[5 + offset] == '_')
                        && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                        && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                        && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                        && (sql[9 + offset] == 'G' || sql[9 + offset] == 'g')
                        && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                        && (sql[11 + offset] == 'H' || sql[11 + offset] == 'h')) {
                    return Functions.OCTET_LENGTH;
                }
            }
        }
        if (size == 3 && (sql[1 + offset] == 'R' || sql[1 + offset] == 'r')
                && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd')) {
            return Functions.ORD;
        }
        if (size == 8 && (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') && (
                sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == '_'
                || sql[3 + offset] == '_') && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h')
                && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'S'
                || sql[6 + offset] == 's') && (sql[7 + offset] == 'H' || sql[7 + offset] == 'h')) {
            return Functions.ORA_HASH;
        }
        return 0;
    }

    private static int parseP(byte[] sql, int offset, int size) {
        if (size < 2 || size > 12) {
            return 0;
        }
        if (size == 8 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a')
                && (sql[2 + offset] == 'S' || sql[2 + offset] == 's')
                && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                && (sql[4 + offset] == 'W' || sql[4 + offset] == 'w')
                && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')) {
            return Functions.PASSWORD;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 10 || size > 12) {
                return 0;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size == 12 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c')
                        && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                        && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                        && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                        && (sql[7 + offset] == '_' || sql[7 + offset] == '_')
                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                        && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a')
                        && (sql[10 + offset] == 'N' || sql[10 + offset] == 'n')
                        && (sql[11 + offset] == 'K' || sql[11 + offset] == 'k')) {
                    return Functions.PERCENT_RANK;
                }
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (size < 10 || size > 11) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') {
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (size == 10 && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a')
                                        && (sql[8 + offset] == 'D' || sql[8 + offset] == 'd')
                                        && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')) {
                                    return Functions.PERIOD_ADD;
                                }
                                if (size == 11 && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')
                                        && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                        && (sql[9 + offset] == 'F' || sql[9 + offset] == 'f')
                                        && (sql[10 + offset] == 'F' || sql[10 + offset] == 'f')) {
                                    return Functions.PERIOD_DIFF;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (size == 2 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i')) {
            return Functions.PI;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 5 || size > 7) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i')
                    && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                    && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
                return Functions.Point;
            }
            if (size == 7 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l')
                    && (sql[3 + offset] == 'Y' || sql[3 + offset] == 'y')
                    && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g')
                    && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                    && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')) {
                return Functions.Polygon;
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 3 || size > 8) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's')
                    && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                    && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                    && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                    && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                    && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')) {
                return Functions.POSITION;
            }
            if (sql[2 + offset] == 'W' || sql[2 + offset] == 'w') {
                if (size < 3 || size > 5) {
                    return 0;
                }
                if (size == 3) {
                    return Functions.POW;
                }
                if (size == 5 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                        && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
                    return Functions.POWER;
                }
            }
        }
        return 0;
    }

    private static int parseQ(byte[] sql, int offset, int size) {
        if (size < 5 || size > 7) {
            return 0;
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size == 7 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a')
                    && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')
                    && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                    && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                    && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')) {
                return Functions.QUARTER;
            }
            if (size == 5 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o')
                    && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')
                    && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Functions.QUOTE;
            }
        }
        return 0;
    }

    private static int parseR(byte[] sql, int offset, int size) {
        if (size < 4 || size > 17) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 12) {
                return 0;
            }
            if (size == 7 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd')
                    && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                    && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                    && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                    && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                return Functions.RADIANS;
            }
            if ((sql[2 + offset] == 'W' || sql[2 + offset] == 'w') && (sql[3 + offset] == 'T'
                    || sql[3 + offset] == 't')) {
                if (size == 8 && (sql[4 + offset] == 'O' || sql[3 + offset] == 'o') && (
                        sql[5 + offset] == 'H' || sql[3 + offset] == 'h') && (sql[6 + offset] == 'E'
                        || sql[3 + offset] == 'e') && (sql[7 + offset] == 'X'
                        || sql[3 + offset] == 'x')) {
                    return Functions.RAWTOHEX;
                }
                if (size == 9 && (sql[4 + offset] == 'O' || sql[3 + offset] == 'o') && (
                        sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'H'
                        || sql[6 + offset] == 'h') && (sql[7 + offset] == 'E'
                        || sql[7 + offset] == 'e') && (sql[8 + offset] == 'X'
                        || sql[8 + offset] == 'x')) {
                    return Functions.RAWTONHEX;
                }
            }

            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') {
                    if (size == 4) {
                        return Functions.RAND;
                    }
                    if (size == 12 && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o')
                            && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                            && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                            && (sql[7 + offset] == 'B' || sql[7 + offset] == 'b')
                            && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y')
                            && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')
                            && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                            && (sql[11 + offset] == 'S' || sql[11 + offset] == 's')) {
                        return Functions.RANDOM_BYTES;
                    }
                }
                if (size == 4 && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k')) {
                    return Functions.RANK;
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 6 || size > 17) {
                return 0;
            }
            if (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') {
                if (size < 11 || size > 14) {
                    return 0;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (sql[4 + offset] == 'X' || sql[4 + offset] == 'x') {
                        if (sql[5 + offset] == 'P' || sql[5 + offset] == 'p') {
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (size == 12 && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                                        && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                                        && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')
                                        && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                                        && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')) {
                                    return Functions.REGEXP_INSTR;
                                }
                                if (size == 11 && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                                        && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                        && (sql[9 + offset] == 'K' || sql[9 + offset] == 'k')
                                        && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                                    return Functions.REGEXP_LIKE;
                                }
                                if (size == 14 && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                                        && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                                        && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                                        && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                                        && (sql[11 + offset] == 'A' || sql[11 + offset] == 'a')
                                        && (sql[12 + offset] == 'C' || sql[12 + offset] == 'c')
                                        && (sql[13 + offset] == 'E' || sql[13 + offset] == 'e')) {
                                    return Functions.REGEXP_REPLACE;
                                }
                                if (size == 13 && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                                        && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u')
                                        && (sql[9 + offset] == 'B' || sql[9 + offset] == 'b')
                                        && (sql[10 + offset] == 'S' || sql[10 + offset] == 's')
                                        && (sql[11 + offset] == 'T' || sql[11 + offset] == 't')
                                        && (sql[12 + offset] == 'R' || sql[12 + offset] == 'r')) {
                                    return Functions.REGEXP_SUBSTR;
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size < 12 || size > 17) {
                    return 0;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') {
                        if (sql[5 + offset] == 'S' || sql[5 + offset] == 's') {
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (sql[7 + offset] == '_' || sql[7 + offset] == '_') {
                                    if (size == 17
                                            && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                                            && (sql[9 + offset] == 'L' || sql[9 + offset] == 'l')
                                            && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                                            && (sql[11 + offset] == '_' || sql[11 + offset] == '_')
                                            && (sql[12 + offset] == 'L' || sql[12 + offset] == 'l')
                                            && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                                            && (sql[14 + offset] == 'C' || sql[14 + offset] == 'c')
                                            && (sql[15 + offset] == 'K' || sql[15 + offset] == 'k')
                                            && (sql[16 + offset] == 'S'
                                                    || sql[16 + offset] == 's')) {
                                        return Functions.RELEASE_ALL_LOCKS;
                                    }
                                    if (size == 12
                                            && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                            && (sql[10 + offset] == 'C' || sql[10 + offset] == 'c')
                                            && (sql[11 + offset] == 'K'
                                                    || sql[11 + offset] == 'k')) {
                                        return Functions.RELEASE_LOCK;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (size == 9 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (
                    sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                    && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd') && (sql[7 + offset] == 'E'
                    || sql[7 + offset] == 'e') && (sql[8 + offset] == 'R'
                    || sql[8 + offset] == 'r')) {
                return Functions.REMAINDER;
            }
            if (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') {
                if (size < 6 || size > 7) {
                    return 0;
                }
                if (size == 6 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                    return Functions.REPEAT;
                }
                if (size == 7 && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.REPLACE;
                }
            }
            if (size == 7 && (sql[2 + offset] == 'V' || sql[2 + offset] == 'v')
                    && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                    && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                    && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                    && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                return Functions.REVERSE;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i')
                && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g')
                && (sql[3 + offset] == 'H' || sql[3 + offset] == 'h')
                && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
            return Functions.RIGHT;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 5 || size > 13) {
                return 0;
            }
            if (size == 13 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l')
                    && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                    && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')
                    && (sql[5 + offset] == '_' || sql[5 + offset] == '_')
                    && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g')
                    && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                    && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                    && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                    && (sql[10 + offset] == 'H' || sql[10 + offset] == 'h')
                    && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')
                    && (sql[12 + offset] == 'L' || sql[12 + offset] == 'l')) {
                return Functions.ROLES_GRAPHML;
            }
            if (size == 5 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u')
                    && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                    && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')) {
                return Functions.ROUND;
            }
            if (sql[2 + offset] == 'W' || sql[2 + offset] == 'w') {
                if (size < 9 || size > 12) {
                    return 0;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 9 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u')
                            && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                            && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')) {
                        return Functions.ROW_COUNT;
                    }
                    if (size == 10 && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n')
                            && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u')
                            && (sql[6 + offset] == 'M' || sql[6 + offset] == 'm')
                            && (sql[7 + offset] == 'B' || sql[7 + offset] == 'b')
                            && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                            && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')) {
                        return Functions.ROW_NUMBER;
                    }
                }
                if (size == 11 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (
                        sql[4 + offset] == 'D' || sql[4 + offset] == 'd') && (sql[5 + offset] == 'T'
                        || sql[5 + offset] == 't') && (sql[6 + offset] == 'O'
                        || sql[6 + offset] == 'o') && (sql[7 + offset] == 'C'
                        || sql[7 + offset] == 'c') && (sql[8 + offset] == 'H'
                        || sql[8 + offset] == 'h') && (sql[9 + offset] == 'A'
                        || sql[9 + offset] == 'a') && (sql[10 + offset] == 'R'
                        || sql[9 + offset] == 'r')) {
                    return Functions.ROWIDTOCHAR;
                }
                if (size == 12 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (
                        sql[4 + offset] == 'D' || sql[4 + offset] == 'd') && (sql[5 + offset] == 'T'
                        || sql[5 + offset] == 't') && (sql[6 + offset] == 'O'
                        || sql[6 + offset] == 'o') && (sql[7 + offset] == 'N'
                        || sql[7 + offset] == 'n') && (sql[8 + offset] == 'C'
                        || sql[8 + offset] == 'c') && (sql[9 + offset] == 'H'
                        || sql[9 + offset] == 'h') && (sql[10 + offset] == 'A'
                        || sql[10 + offset] == 'a') && (sql[11 + offset] == 'R'
                        || sql[11 + offset] == 'r')) {
                    return Functions.ROWIDTONCHAR;
                }
            }
        }
        if (size == 4 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p')
                && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a')
                && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')) {
            return Functions.RPAD;
        }
        if (size == 5 && (sql[1 + offset] == 'T' || sql[1 + offset] == 't')
                && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r')
                && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm')) {
            return Functions.RTRIM;
        }
        return 0;
    }

    private static int parseS(byte[] sql, int offset, int size) {
        if (size < 3 || size > 29) {
            return 0;
        }
        if (size == 6 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c')
                && (sql[2 + offset] == 'H' || sql[2 + offset] == 'h')
                && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm')
                && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')) {
            return Functions.SCHEMA;
        }
        if (size == 16 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c') && (
                sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == '_'
                || sql[3 + offset] == '_') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == '_'
                || sql[6 + offset] == '_') && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (sql[9 + offset] == 'M'
                || sql[9 + offset] == 'm') && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                && (sql[11 + offset] == 'S' || sql[11 + offset] == 's') && (sql[12 + offset] == 'T'
                || sql[12 + offset] == 't') && (sql[13 + offset] == 'A' || sql[13 + offset] == 'a')
                && (sql[14 + offset] == 'M' || sql[14 + offset] == 'm') && (sql[15 + offset] == 'P'
                || sql[15 + offset] == 'p')) {
            return Functions.SCN_TO_TIMESTAMP;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 6 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (size < 6 || size > 11) {
                    return 0;
                }
                if (size == 11 && (sql[3 + offset] == '_' || sql[3 + offset] == '_')
                        && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                        && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                        && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                        && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                        && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                        && (sql[9 + offset] == 'M' || sql[9 + offset] == 'm')
                        && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                    return Functions.SEC_TO_TIME;
                }
                if (size == 6 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o')
                        && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n')
                        && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd')) {
                    return Functions.SECOND;
                }
            }
            if (size == 12 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's')
                    && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                    && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                    && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                    && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                    && (sql[7 + offset] == '_' || sql[7 + offset] == '_')
                    && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u')
                    && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')
                    && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                    && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')) {
                return Functions.SESSION_USER;
            }
            if (size == 15 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (
                    sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                    && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (sql[7 + offset] == 'T'
                    || sql[7 + offset] == 't') && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                    && (sql[9 + offset] == 'M' || sql[9 + offset] == 'm') && (
                    sql[10 + offset] == 'E' || sql[10 + offset] == 'e') && (sql[11 + offset] == 'Z'
                    || sql[11 + offset] == 'z') && (sql[12 + offset] == 'O'
                    || sql[12 + offset] == 'o') && (sql[13 + offset] == 'N'
                    || sql[13 + offset] == 'n') && (sql[14 + offset] == 'E'
                    || sql[14 + offset] == 'e')) {
                return Functions.SESSIONTIMEZONE;
            }
        }
        if (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') {
            if (size < 3 || size > 4) {
                return 0;
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (size == 4 && (sql[3 + offset] == '1' || sql[3 + offset] == '1')) {
                    return Functions.SHA1;
                }
                if (size == 3) {
                    return Functions.SHA;
                }
                if (size == 4 && (sql[3 + offset] == '2' || sql[3 + offset] == '2')) {
                    return Functions.SHA2;
                }
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size == 4 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g')
                    && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')) {
                return Functions.SIGN;
            }
            if (size == 3 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')) {
                return Functions.SIN;
            }
            if (size == 4 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (
                    sql[3 + offset] == 'H' || sql[3 + offset] == 'h')) {
                return Functions.SINH;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l')
                && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e')
                && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p')) {
            return Functions.SLEEP;
        }
        if (size == 7 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o')
                && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u')
                && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                && (sql[6 + offset] == 'X' || sql[6 + offset] == 'x')) {
            return Functions.SOUNDEX;
        }
        if (size == 5 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p')
                && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a')
                && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c')
                && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
            return Functions.SPACE;
        }
        if (size == 4 && (sql[1 + offset] == 'Q' || sql[1 + offset] == 'q')
                && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r')
                && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')) {
            return Functions.SQRT;
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (sql[2 + offset] == '_' || sql[2 + offset] == '_') {
                if (size < 4 || size > 29) {
                    return 0;
                }
                if (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') {
                    if (size < 7 || size > 12) {
                        return 0;
                    }
                    if (size == 7 && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                            && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                            && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')) {
                        return Functions.ST_Area;
                    }
                    if (sql[4 + offset] == 'S' || sql[4 + offset] == 's') {
                        if (size < 8 || size > 12) {
                            return 0;
                        }
                        if (size == 11 && (sql[5 + offset] == 'B' || sql[5 + offset] == 'b')
                                && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                                && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                                && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                && (sql[10 + offset] == 'Y' || sql[10 + offset] == 'y')) {
                            return Functions.ST_AsBinary;
                        }
                        if (size == 8 && (sql[5 + offset] == 'W' || sql[5 + offset] == 'w')
                                && (sql[6 + offset] == 'K' || sql[6 + offset] == 'k')
                                && (sql[7 + offset] == 'B' || sql[7 + offset] == 'b')) {
                            return Functions.ST_AsWKB;
                        }
                        if (size == 12 && (sql[5 + offset] == 'G' || sql[5 + offset] == 'g')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                && (sql[8 + offset] == 'J' || sql[8 + offset] == 'j')
                                && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')
                                && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                                && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')) {
                            return Functions.ST_AsGeoJSON;
                        }
                        if (size == 9 && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'X' || sql[7 + offset] == 'x')
                                && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')) {
                            return Functions.ST_AsText;
                        }
                        if (size == 8 && (sql[5 + offset] == 'W' || sql[5 + offset] == 'w')
                                && (sql[6 + offset] == 'K' || sql[6 + offset] == 'k')
                                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
                            return Functions.ST_AsWKT;
                        }
                    }
                }
                if (sql[3 + offset] == 'B' || sql[3 + offset] == 'b') {
                    if (size < 9 || size > 18) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') {
                        if (sql[5 + offset] == 'F' || sql[5 + offset] == 'f') {
                            if (sql[6 + offset] == 'F' || sql[6 + offset] == 'f') {
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (sql[8 + offset] == 'R' || sql[8 + offset] == 'r') {
                                        if (size == 9) {
                                            return Functions.ST_Buffer;
                                        }
                                        if (size == 18
                                                && (sql[9 + offset] == '_'
                                                        || sql[9 + offset] == '_')
                                                && (sql[10 + offset] == 'S'
                                                        || sql[10 + offset] == 's')
                                                && (sql[11 + offset] == 'T'
                                                        || sql[11 + offset] == 't')
                                                && (sql[12 + offset] == 'R'
                                                        || sql[12 + offset] == 'r')
                                                && (sql[13 + offset] == 'A'
                                                        || sql[13 + offset] == 'a')
                                                && (sql[14 + offset] == 'T'
                                                        || sql[14 + offset] == 't')
                                                && (sql[15 + offset] == 'E'
                                                        || sql[15 + offset] == 'e')
                                                && (sql[16 + offset] == 'G'
                                                        || sql[16 + offset] == 'g')
                                                && (sql[17 + offset] == 'Y'
                                                        || sql[17 + offset] == 'y')) {
                                            return Functions.ST_Buffer_Strategy;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') {
                    if (size < 10 || size > 13) {
                        return 0;
                    }
                    if (size == 11 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                            && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                            && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                            && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i')
                            && (sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                        return Functions.ST_Centroid;
                    }
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (size < 11 || size > 13) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (size == 11 && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                                    && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a')
                                    && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                    && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                                    && (sql[10 + offset] == 'S' || sql[10 + offset] == 's')) {
                                return Functions.ST_Contains;
                            }
                            if (size == 13 && (sql[6 + offset] == 'V' || sql[6 + offset] == 'v')
                                    && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                                    && (sql[8 + offset] == 'X' || sql[8 + offset] == 'x')
                                    && (sql[9 + offset] == 'H' || sql[9 + offset] == 'h')
                                    && (sql[10 + offset] == 'U' || sql[10 + offset] == 'u')
                                    && (sql[11 + offset] == 'L' || sql[11 + offset] == 'l')
                                    && (sql[12 + offset] == 'L' || sql[12 + offset] == 'l')) {
                                return Functions.ST_ConvexHull;
                            }
                        }
                    }
                    if (size == 10 && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')
                            && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                            && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                            && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')) {
                        return Functions.ST_Crosses;
                    }
                }
                if (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') {
                    if (size < 11 || size > 18) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (size == 13 && (sql[5 + offset] == 'F' || sql[5 + offset] == 'f')
                                && (sql[6 + offset] == 'F' || sql[6 + offset] == 'f')
                                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                                && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                                && (sql[10 + offset] == 'N' || sql[10 + offset] == 'n')
                                && (sql[11 + offset] == 'C' || sql[11 + offset] == 'c')
                                && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')) {
                            return Functions.ST_Difference;
                        }
                        if (size == 12 && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                                && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                                && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i')
                                && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                                && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')) {
                            return Functions.ST_Dimension;
                        }
                        if (sql[5 + offset] == 'S' || sql[5 + offset] == 's') {
                            if (size == 11 && (sql[6 + offset] == 'J' || sql[6 + offset] == 'j')
                                    && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                    && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                    && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                                    && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                                return Functions.ST_Disjoint;
                            }
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') {
                                    if (sql[8 + offset] == 'N' || sql[8 + offset] == 'n') {
                                        if (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') {
                                            if (sql[10 + offset] == 'E'
                                                    || sql[10 + offset] == 'e') {
                                                if (size == 11) {
                                                    return Functions.ST_Distance;
                                                }
                                                if (size == 18
                                                        && (sql[11 + offset] == '_'
                                                                || sql[11 + offset] == '_')
                                                        && (sql[12 + offset] == 'S'
                                                                || sql[12 + offset] == 's')
                                                        && (sql[13 + offset] == 'P'
                                                                || sql[13 + offset] == 'p')
                                                        && (sql[14 + offset] == 'H'
                                                                || sql[14 + offset] == 'h')
                                                        && (sql[15 + offset] == 'E'
                                                                || sql[15 + offset] == 'e')
                                                        && (sql[16 + offset] == 'R'
                                                                || sql[16 + offset] == 'r')
                                                        && (sql[17 + offset] == 'E'
                                                                || sql[17 + offset] == 'e')) {
                                                    return Functions.ST_Distance_Sphere;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size < 9 || size > 15) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') {
                        if (size != 11) {
                            return 0;
                        }
                        if (size == 11 && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd')
                                && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p')
                                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                                && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                            return Functions.ST_EndPoint;
                        }
                        if (size == 11 && (sql[5 + offset] == 'V' || sql[5 + offset] == 'v')
                                && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                                && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                                && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                                && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                            return Functions.ST_Envelope;
                        }
                    }
                    if (size == 9 && (sql[4 + offset] == 'Q' || sql[4 + offset] == 'q')
                            && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u')
                            && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                            && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                            && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')) {
                        return Functions.ST_Equals;
                    }
                    if (size == 15 && (sql[4 + offset] == 'X' || sql[4 + offset] == 'x')
                            && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                            && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                            && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                            && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')
                            && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')
                            && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i')
                            && (sql[13 + offset] == 'N' || sql[13 + offset] == 'n')
                            && (sql[14 + offset] == 'G' || sql[14 + offset] == 'g')) {
                        return Functions.ST_ExteriorRing;
                    }
                }
                if (sql[3 + offset] == 'G' || sql[3 + offset] == 'g') {
                    if (size < 10 || size > 29) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'O' || sql[5 + offset] == 'o') {
                            if (size == 10 && (sql[6 + offset] == 'H' || sql[6 + offset] == 'h')
                                    && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a')
                                    && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                                    && (sql[9 + offset] == 'H' || sql[9 + offset] == 'h')) {
                                return Functions.ST_GeoHash;
                            }
                            if (sql[6 + offset] == 'M' || sql[6 + offset] == 'm') {
                                if (size < 12 || size > 29) {
                                    return 0;
                                }
                                if (size == 19 && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c')
                                        && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                        && (sql[9 + offset] == 'L' || sql[9 + offset] == 'l')
                                        && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                                        && (sql[11 + offset] == 'F' || sql[11 + offset] == 'f')
                                        && (sql[12 + offset] == 'R' || sql[12 + offset] == 'r')
                                        && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                                        && (sql[14 + offset] == 'M' || sql[14 + offset] == 'm')
                                        && (sql[15 + offset] == 'T' || sql[15 + offset] == 't')
                                        && (sql[16 + offset] == 'E' || sql[16 + offset] == 'e')
                                        && (sql[17 + offset] == 'X' || sql[17 + offset] == 'x')
                                        && (sql[18 + offset] == 'T' || sql[18 + offset] == 't')) {
                                    return Functions.ST_GeomCollFromText;
                                }
                                if (size == 29 && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                                        && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                        && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                        && (sql[10 + offset] == 'Y' || sql[10 + offset] == 'y')
                                        && (sql[11 + offset] == 'C' || sql[11 + offset] == 'c')
                                        && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o')
                                        && (sql[13 + offset] == 'L' || sql[13 + offset] == 'l')
                                        && (sql[14 + offset] == 'L' || sql[14 + offset] == 'l')
                                        && (sql[15 + offset] == 'E' || sql[15 + offset] == 'e')
                                        && (sql[16 + offset] == 'C' || sql[16 + offset] == 'c')
                                        && (sql[17 + offset] == 'T' || sql[17 + offset] == 't')
                                        && (sql[18 + offset] == 'I' || sql[18 + offset] == 'i')
                                        && (sql[19 + offset] == 'O' || sql[19 + offset] == 'o')
                                        && (sql[20 + offset] == 'N' || sql[20 + offset] == 'n')
                                        && (sql[21 + offset] == 'F' || sql[21 + offset] == 'f')
                                        && (sql[22 + offset] == 'R' || sql[22 + offset] == 'r')
                                        && (sql[23 + offset] == 'O' || sql[23 + offset] == 'o')
                                        && (sql[24 + offset] == 'M' || sql[24 + offset] == 'm')
                                        && (sql[25 + offset] == 'T' || sql[25 + offset] == 't')
                                        && (sql[26 + offset] == 'E' || sql[26 + offset] == 'e')
                                        && (sql[27 + offset] == 'X' || sql[27 + offset] == 'x')
                                        && (sql[28 + offset] == 'T' || sql[28 + offset] == 't')) {
                                    return Functions.ST_GeometryCollectionFromText;
                                }
                                if (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') {
                                    if (size != 18) {
                                        return 0;
                                    }
                                    if (sql[8 + offset] == 'O' || sql[8 + offset] == 'o') {
                                        if (sql[9 + offset] == 'L' || sql[9 + offset] == 'l') {
                                            if (sql[10 + offset] == 'L'
                                                    || sql[10 + offset] == 'l') {
                                                if (sql[11 + offset] == 'F'
                                                        || sql[11 + offset] == 'f') {
                                                    if (sql[12 + offset] == 'R'
                                                            || sql[12 + offset] == 'r') {
                                                        if (sql[13 + offset] == 'O'
                                                                || sql[13 + offset] == 'o') {
                                                            if (sql[14 + offset] == 'M'
                                                                    || sql[14 + offset] == 'm') {
                                                                if (size == 18 && (sql[15
                                                                        + offset] == 'T'
                                                                        || sql[15 + offset] == 't')
                                                                        && (sql[16 + offset] == 'X'
                                                                                || sql[16
                                                                                        + offset] == 'x')
                                                                        && (sql[17 + offset] == 'T'
                                                                                || sql[17
                                                                                        + offset] == 't')) {
                                                                    return Functions.ST_GeomCollFromTxt;
                                                                }
                                                                if (size == 18 && (sql[15
                                                                        + offset] == 'W'
                                                                        || sql[15 + offset] == 'w')
                                                                        && (sql[16 + offset] == 'K'
                                                                                || sql[16
                                                                                        + offset] == 'k')
                                                                        && (sql[17 + offset] == 'B'
                                                                                || sql[17
                                                                                        + offset] == 'b')) {
                                                                    return Functions.ST_GeomCollFromWKB;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (size < 12 || size > 28) {
                                        return 0;
                                    }
                                    if (sql[8 + offset] == 'T' || sql[8 + offset] == 't') {
                                        if (sql[9 + offset] == 'R' || sql[9 + offset] == 'r') {
                                            if (sql[10 + offset] == 'Y'
                                                    || sql[10 + offset] == 'y') {
                                                if (size == 28
                                                        && (sql[11 + offset] == 'C'
                                                                || sql[11 + offset] == 'c')
                                                        && (sql[12 + offset] == 'O'
                                                                || sql[12 + offset] == 'o')
                                                        && (sql[13 + offset] == 'L'
                                                                || sql[13 + offset] == 'l')
                                                        && (sql[14 + offset] == 'L'
                                                                || sql[14 + offset] == 'l')
                                                        && (sql[15 + offset] == 'E'
                                                                || sql[15 + offset] == 'e')
                                                        && (sql[16 + offset] == 'C'
                                                                || sql[16 + offset] == 'c')
                                                        && (sql[17 + offset] == 'T'
                                                                || sql[17 + offset] == 't')
                                                        && (sql[18 + offset] == 'I'
                                                                || sql[18 + offset] == 'i')
                                                        && (sql[19 + offset] == 'O'
                                                                || sql[19 + offset] == 'o')
                                                        && (sql[20 + offset] == 'N'
                                                                || sql[20 + offset] == 'n')
                                                        && (sql[21 + offset] == 'F'
                                                                || sql[21 + offset] == 'f')
                                                        && (sql[22 + offset] == 'R'
                                                                || sql[22 + offset] == 'r')
                                                        && (sql[23 + offset] == 'O'
                                                                || sql[23 + offset] == 'o')
                                                        && (sql[24 + offset] == 'M'
                                                                || sql[24 + offset] == 'm')
                                                        && (sql[25 + offset] == 'W'
                                                                || sql[25 + offset] == 'w')
                                                        && (sql[26 + offset] == 'K'
                                                                || sql[26 + offset] == 'k')
                                                        && (sql[27 + offset] == 'B'
                                                                || sql[27 + offset] == 'b')) {
                                                    return Functions.ST_GeometryCollectionFromWKB;
                                                }
                                                if (size == 12 && (sql[11 + offset] == 'N'
                                                        || sql[11 + offset] == 'n')) {
                                                    return Functions.ST_GeometryN;
                                                }
                                                if (size == 15
                                                        && (sql[11 + offset] == 'T'
                                                                || sql[11 + offset] == 't')
                                                        && (sql[12 + offset] == 'Y'
                                                                || sql[12 + offset] == 'y')
                                                        && (sql[13 + offset] == 'P'
                                                                || sql[13 + offset] == 'p')
                                                        && (sql[14 + offset] == 'E'
                                                                || sql[14 + offset] == 'e')) {
                                                    return Functions.ST_GeometryType;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (sql[7 + offset] == 'F' || sql[7 + offset] == 'f') {
                                    if (size < 15 || size > 18) {
                                        return 0;
                                    }
                                    if (sql[8 + offset] == 'R' || sql[8 + offset] == 'r') {
                                        if (sql[9 + offset] == 'O' || sql[9 + offset] == 'o') {
                                            if (sql[10 + offset] == 'M'
                                                    || sql[10 + offset] == 'm') {
                                                if (size == 18
                                                        && (sql[11 + offset] == 'G'
                                                                || sql[11 + offset] == 'g')
                                                        && (sql[12 + offset] == 'E'
                                                                || sql[12 + offset] == 'e')
                                                        && (sql[13 + offset] == 'O'
                                                                || sql[13 + offset] == 'o')
                                                        && (sql[14 + offset] == 'J'
                                                                || sql[14 + offset] == 'j')
                                                        && (sql[15 + offset] == 'S'
                                                                || sql[15 + offset] == 's')
                                                        && (sql[16 + offset] == 'O'
                                                                || sql[16 + offset] == 'o')
                                                        && (sql[17 + offset] == 'N'
                                                                || sql[17 + offset] == 'n')) {
                                                    return Functions.ST_GeomFromGeoJSON;
                                                }
                                                if (size == 15
                                                        && (sql[11 + offset] == 'T'
                                                                || sql[11 + offset] == 't')
                                                        && (sql[12 + offset] == 'E'
                                                                || sql[12 + offset] == 'e')
                                                        && (sql[13 + offset] == 'X'
                                                                || sql[13 + offset] == 'x')
                                                        && (sql[14 + offset] == 'T'
                                                                || sql[14 + offset] == 't')) {
                                                    return Functions.ST_GeomFromText;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (size == 19 && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                                        && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                        && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                        && (sql[10 + offset] == 'Y' || sql[10 + offset] == 'y')
                                        && (sql[11 + offset] == 'F' || sql[11 + offset] == 'f')
                                        && (sql[12 + offset] == 'R' || sql[12 + offset] == 'r')
                                        && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                                        && (sql[14 + offset] == 'M' || sql[14 + offset] == 'm')
                                        && (sql[15 + offset] == 'T' || sql[15 + offset] == 't')
                                        && (sql[16 + offset] == 'E' || sql[16 + offset] == 'e')
                                        && (sql[17 + offset] == 'X' || sql[17 + offset] == 'x')
                                        && (sql[18 + offset] == 'T' || sql[18 + offset] == 't')) {
                                    return Functions.ST_GeometryFromText;
                                }
                                if (size == 14 && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f')
                                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                        && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                        && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm')
                                        && (sql[11 + offset] == 'W' || sql[11 + offset] == 'w')
                                        && (sql[12 + offset] == 'K' || sql[12 + offset] == 'k')
                                        && (sql[13 + offset] == 'B' || sql[13 + offset] == 'b')) {
                                    return Functions.ST_GeomFromWKB;
                                }
                                if (size == 18 && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                                        && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                        && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                        && (sql[10 + offset] == 'Y' || sql[10 + offset] == 'y')
                                        && (sql[11 + offset] == 'F' || sql[11 + offset] == 'f')
                                        && (sql[12 + offset] == 'R' || sql[12 + offset] == 'r')
                                        && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                                        && (sql[14 + offset] == 'M' || sql[14 + offset] == 'm')
                                        && (sql[15 + offset] == 'W' || sql[15 + offset] == 'w')
                                        && (sql[16 + offset] == 'K' || sql[16 + offset] == 'k')
                                        && (sql[17 + offset] == 'B' || sql[17 + offset] == 'b')) {
                                    return Functions.ST_GeometryFromWKB;
                                }
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (size < 10 || size > 16) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') {
                        if (size < 13 || size > 16) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') {
                                    if (size == 16
                                            && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                            && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')
                                            && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')
                                            && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i')
                                            && (sql[13 + offset] == 'N' || sql[13 + offset] == 'n')
                                            && (sql[14 + offset] == 'G' || sql[14 + offset] == 'g')
                                            && (sql[15 + offset] == 'N'
                                                    || sql[15 + offset] == 'n')) {
                                        return Functions.ST_InteriorRingN;
                                    }
                                    if (sql[8 + offset] == 'S' || sql[8 + offset] == 's') {
                                        if (size < 13 || size > 15) {
                                            return 0;
                                        }
                                        if (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') {
                                            if (sql[10 + offset] == 'C'
                                                    || sql[10 + offset] == 'c') {
                                                if (sql[11 + offset] == 'T'
                                                        || sql[11 + offset] == 't') {
                                                    if (size == 15
                                                            && (sql[12 + offset] == 'I'
                                                                    || sql[12 + offset] == 'i')
                                                            && (sql[13 + offset] == 'O'
                                                                    || sql[13 + offset] == 'o')
                                                            && (sql[14 + offset] == 'N'
                                                                    || sql[14 + offset] == 'n')) {
                                                        return Functions.ST_Intersection;
                                                    }
                                                    if (size == 13 && (sql[12 + offset] == 'S'
                                                            || sql[12 + offset] == 's')) {
                                                        return Functions.ST_Intersects;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (sql[4 + offset] == 'S' || sql[4 + offset] == 's') {
                        if (size < 10 || size > 11) {
                            return 0;
                        }
                        if (size == 11 && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c')
                                && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                                && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                                && (sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                            return Functions.ST_IsClosed;
                        }
                        if (size == 10 && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                                && (sql[6 + offset] == 'M' || sql[6 + offset] == 'm')
                                && (sql[7 + offset] == 'P' || sql[7 + offset] == 'p')
                                && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                && (sql[9 + offset] == 'Y' || sql[9 + offset] == 'y')) {
                            return Functions.ST_IsEmpty;
                        }
                        if (size == 11 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                                && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                                && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm')
                                && (sql[8 + offset] == 'P' || sql[8 + offset] == 'p')
                                && (sql[9 + offset] == 'L' || sql[9 + offset] == 'l')
                                && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                            return Functions.ST_IsSimple;
                        }
                        if (size == 10 && (sql[5 + offset] == 'V' || sql[5 + offset] == 'v')
                                && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                                && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                                && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')) {
                            return Functions.ST_IsValid;
                        }
                    }
                }
                if (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') {
                    if (size < 9 || size > 21) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') {
                        if (size < 11 || size > 17) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (size == 17 && (sql[6 + offset] == 'F' || sql[6 + offset] == 'f')
                                    && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                                    && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                    && (sql[9 + offset] == 'M' || sql[9 + offset] == 'm')
                                    && (sql[10 + offset] == 'G' || sql[10 + offset] == 'g')
                                    && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')
                                    && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o')
                                    && (sql[13 + offset] == 'H' || sql[13 + offset] == 'h')
                                    && (sql[14 + offset] == 'A' || sql[14 + offset] == 'a')
                                    && (sql[15 + offset] == 'S' || sql[15 + offset] == 's')
                                    && (sql[16 + offset] == 'H' || sql[16 + offset] == 'h')) {
                                return Functions.ST_LatFromGeoHash;
                            }
                            if (size == 11 && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                                    && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                                    && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u')
                                    && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')
                                    && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                                return Functions.ST_Latitude;
                            }
                        }
                    }
                    if (size == 9 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                            && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                            && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g')
                            && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                            && (sql[8 + offset] == 'H' || sql[8 + offset] == 'h')) {
                        return Functions.ST_Length;
                    }
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (size < 14 || size > 21) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (size == 15 && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f')
                                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                        && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                        && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm')
                                        && (sql[11 + offset] == 'T' || sql[11 + offset] == 't')
                                        && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')
                                        && (sql[13 + offset] == 'X' || sql[13 + offset] == 'x')
                                        && (sql[14 + offset] == 'T' || sql[14 + offset] == 't')) {
                                    return Functions.ST_LineFromText;
                                }
                                if (size == 21 && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                                        && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                        && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                        && (sql[10 + offset] == 'I' || sql[10 + offset] == 'i')
                                        && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')
                                        && (sql[12 + offset] == 'G' || sql[12 + offset] == 'g')
                                        && (sql[13 + offset] == 'F' || sql[13 + offset] == 'f')
                                        && (sql[14 + offset] == 'R' || sql[14 + offset] == 'r')
                                        && (sql[15 + offset] == 'O' || sql[15 + offset] == 'o')
                                        && (sql[16 + offset] == 'M' || sql[16 + offset] == 'm')
                                        && (sql[17 + offset] == 'T' || sql[17 + offset] == 't')
                                        && (sql[18 + offset] == 'E' || sql[18 + offset] == 'e')
                                        && (sql[19 + offset] == 'X' || sql[19 + offset] == 'x')
                                        && (sql[20 + offset] == 'T' || sql[20 + offset] == 't')) {
                                    return Functions.ST_LineStringFromText;
                                }
                                if (size == 14 && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f')
                                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                        && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                        && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm')
                                        && (sql[11 + offset] == 'W' || sql[11 + offset] == 'w')
                                        && (sql[12 + offset] == 'K' || sql[12 + offset] == 'k')
                                        && (sql[13 + offset] == 'B' || sql[13 + offset] == 'b')) {
                                    return Functions.ST_LineFromWKB;
                                }
                                if (size == 20 && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                                        && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                        && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                                        && (sql[10 + offset] == 'I' || sql[10 + offset] == 'i')
                                        && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')
                                        && (sql[12 + offset] == 'G' || sql[12 + offset] == 'g')
                                        && (sql[13 + offset] == 'F' || sql[13 + offset] == 'f')
                                        && (sql[14 + offset] == 'R' || sql[14 + offset] == 'r')
                                        && (sql[15 + offset] == 'O' || sql[15 + offset] == 'o')
                                        && (sql[16 + offset] == 'M' || sql[16 + offset] == 'm')
                                        && (sql[17 + offset] == 'W' || sql[17 + offset] == 'w')
                                        && (sql[18 + offset] == 'K' || sql[18 + offset] == 'k')
                                        && (sql[19 + offset] == 'B' || sql[19 + offset] == 'b')) {
                                    return Functions.ST_LineStringFromWKB;
                                }
                            }
                        }
                    }
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (size < 12 || size > 18) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (sql[6 + offset] == 'G' || sql[6 + offset] == 'g') {
                                if (size == 18 && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f')
                                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                        && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                        && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm')
                                        && (sql[11 + offset] == 'G' || sql[11 + offset] == 'g')
                                        && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')
                                        && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                                        && (sql[14 + offset] == 'H' || sql[14 + offset] == 'h')
                                        && (sql[15 + offset] == 'A' || sql[15 + offset] == 'a')
                                        && (sql[16 + offset] == 'S' || sql[16 + offset] == 's')
                                        && (sql[17 + offset] == 'H' || sql[17 + offset] == 'h')) {
                                    return Functions.ST_LongFromGeoHash;
                                }
                                if (size == 12 && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                                        && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                                        && (sql[9 + offset] == 'U' || sql[9 + offset] == 'u')
                                        && (sql[10 + offset] == 'D' || sql[10 + offset] == 'd')
                                        && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')) {
                                    return Functions.ST_Longitude;
                                }
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'M' || sql[3 + offset] == 'm') {
                    if (size < 15 || size > 26) {
                        return 0;
                    }
                    if (size == 15 && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                            && (sql[5 + offset] == 'K' || sql[5 + offset] == 'k')
                            && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                            && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                            && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')
                            && (sql[9 + offset] == 'V' || sql[9 + offset] == 'v')
                            && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                            && (sql[11 + offset] == 'L' || sql[11 + offset] == 'l')
                            && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o')
                            && (sql[13 + offset] == 'P' || sql[13 + offset] == 'p')
                            && (sql[14 + offset] == 'E' || sql[14 + offset] == 'e')) {
                        return Functions.ST_MakeEnvelope;
                    }
                    if (size == 16 && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')
                            && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                            && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                            && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                            && (sql[8 + offset] == 'F' || sql[8 + offset] == 'f')
                            && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                            && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                            && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')
                            && (sql[12 + offset] == 'T' || sql[12 + offset] == 't')
                            && (sql[13 + offset] == 'E' || sql[13 + offset] == 'e')
                            && (sql[14 + offset] == 'X' || sql[14 + offset] == 'x')
                            && (sql[15 + offset] == 'T' || sql[15 + offset] == 't')) {
                        return Functions.ST_MLineFromText;
                    }
                    if (size == 26 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                            && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                            && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                            && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i')
                            && (sql[10 + offset] == 'N' || sql[10 + offset] == 'n')
                            && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')
                            && (sql[12 + offset] == 'S' || sql[12 + offset] == 's')
                            && (sql[13 + offset] == 'T' || sql[13 + offset] == 't')
                            && (sql[14 + offset] == 'R' || sql[14 + offset] == 'r')
                            && (sql[15 + offset] == 'I' || sql[15 + offset] == 'i')
                            && (sql[16 + offset] == 'N' || sql[16 + offset] == 'n')
                            && (sql[17 + offset] == 'G' || sql[17 + offset] == 'g')
                            && (sql[18 + offset] == 'F' || sql[18 + offset] == 'f')
                            && (sql[19 + offset] == 'R' || sql[19 + offset] == 'r')
                            && (sql[20 + offset] == 'O' || sql[20 + offset] == 'o')
                            && (sql[21 + offset] == 'M' || sql[21 + offset] == 'm')
                            && (sql[22 + offset] == 'T' || sql[22 + offset] == 't')
                            && (sql[23 + offset] == 'E' || sql[23 + offset] == 'e')
                            && (sql[24 + offset] == 'X' || sql[24 + offset] == 'x')
                            && (sql[25 + offset] == 'T' || sql[25 + offset] == 't')) {
                        return Functions.ST_MultiLineStringFromText;
                    }
                    if (size == 15 && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')
                            && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                            && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                            && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                            && (sql[8 + offset] == 'F' || sql[8 + offset] == 'f')
                            && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                            && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                            && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')
                            && (sql[12 + offset] == 'W' || sql[12 + offset] == 'w')
                            && (sql[13 + offset] == 'K' || sql[13 + offset] == 'k')
                            && (sql[14 + offset] == 'B' || sql[14 + offset] == 'b')) {
                        return Functions.ST_MLineFromWKB;
                    }
                    if (size == 25 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                            && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                            && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')
                            && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i')
                            && (sql[10 + offset] == 'N' || sql[10 + offset] == 'n')
                            && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')
                            && (sql[12 + offset] == 'S' || sql[12 + offset] == 's')
                            && (sql[13 + offset] == 'T' || sql[13 + offset] == 't')
                            && (sql[14 + offset] == 'R' || sql[14 + offset] == 'r')
                            && (sql[15 + offset] == 'I' || sql[15 + offset] == 'i')
                            && (sql[16 + offset] == 'N' || sql[16 + offset] == 'n')
                            && (sql[17 + offset] == 'G' || sql[17 + offset] == 'g')
                            && (sql[18 + offset] == 'F' || sql[18 + offset] == 'f')
                            && (sql[19 + offset] == 'R' || sql[19 + offset] == 'r')
                            && (sql[20 + offset] == 'O' || sql[20 + offset] == 'o')
                            && (sql[21 + offset] == 'M' || sql[21 + offset] == 'm')
                            && (sql[22 + offset] == 'W' || sql[22 + offset] == 'w')
                            && (sql[23 + offset] == 'K' || sql[23 + offset] == 'k')
                            && (sql[24 + offset] == 'B' || sql[24 + offset] == 'b')) {
                        return Functions.ST_MultiLineStringFromWKB;
                    }
                    if (size == 17 && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                            && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                            && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                            && (sql[9 + offset] == 'F' || sql[9 + offset] == 'f')
                            && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')
                            && (sql[11 + offset] == 'O' || sql[11 + offset] == 'o')
                            && (sql[12 + offset] == 'M' || sql[12 + offset] == 'm')
                            && (sql[13 + offset] == 'T' || sql[13 + offset] == 't')
                            && (sql[14 + offset] == 'E' || sql[14 + offset] == 'e')
                            && (sql[15 + offset] == 'X' || sql[15 + offset] == 'x')
                            && (sql[16 + offset] == 'T' || sql[16 + offset] == 't')) {
                        return Functions.ST_MPointFromText;
                    }
                    if (size == 21 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                            && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                            && (sql[8 + offset] == 'P' || sql[8 + offset] == 'p')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'I' || sql[10 + offset] == 'i')
                            && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')
                            && (sql[12 + offset] == 'T' || sql[12 + offset] == 't')
                            && (sql[13 + offset] == 'F' || sql[13 + offset] == 'f')
                            && (sql[14 + offset] == 'R' || sql[14 + offset] == 'r')
                            && (sql[15 + offset] == 'O' || sql[15 + offset] == 'o')
                            && (sql[16 + offset] == 'M' || sql[16 + offset] == 'm')
                            && (sql[17 + offset] == 'T' || sql[17 + offset] == 't')
                            && (sql[18 + offset] == 'E' || sql[18 + offset] == 'e')
                            && (sql[19 + offset] == 'X' || sql[19 + offset] == 'x')
                            && (sql[20 + offset] == 'T' || sql[20 + offset] == 't')) {
                        return Functions.ST_MultiPointFromText;
                    }
                    if (size == 16 && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                            && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                            && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                            && (sql[9 + offset] == 'F' || sql[9 + offset] == 'f')
                            && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')
                            && (sql[11 + offset] == 'O' || sql[11 + offset] == 'o')
                            && (sql[12 + offset] == 'M' || sql[12 + offset] == 'm')
                            && (sql[13 + offset] == 'W' || sql[13 + offset] == 'w')
                            && (sql[14 + offset] == 'K' || sql[14 + offset] == 'k')
                            && (sql[15 + offset] == 'B' || sql[15 + offset] == 'b')) {
                        return Functions.ST_MPointFromWKB;
                    }
                    if (size == 20 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                            && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                            && (sql[8 + offset] == 'P' || sql[8 + offset] == 'p')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'I' || sql[10 + offset] == 'i')
                            && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')
                            && (sql[12 + offset] == 'T' || sql[12 + offset] == 't')
                            && (sql[13 + offset] == 'F' || sql[13 + offset] == 'f')
                            && (sql[14 + offset] == 'R' || sql[14 + offset] == 'r')
                            && (sql[15 + offset] == 'O' || sql[15 + offset] == 'o')
                            && (sql[16 + offset] == 'M' || sql[16 + offset] == 'm')
                            && (sql[17 + offset] == 'W' || sql[17 + offset] == 'w')
                            && (sql[18 + offset] == 'K' || sql[18 + offset] == 'k')
                            && (sql[19 + offset] == 'B' || sql[19 + offset] == 'b')) {
                        return Functions.ST_MultiPointFromWKB;
                    }
                    if (size == 16 && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                            && (sql[7 + offset] == 'Y' || sql[7 + offset] == 'y')
                            && (sql[8 + offset] == 'F' || sql[8 + offset] == 'f')
                            && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                            && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                            && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')
                            && (sql[12 + offset] == 'T' || sql[12 + offset] == 't')
                            && (sql[13 + offset] == 'E' || sql[13 + offset] == 'e')
                            && (sql[14 + offset] == 'X' || sql[14 + offset] == 'x')
                            && (sql[15 + offset] == 'T' || sql[15 + offset] == 't')) {
                        return Functions.ST_MPolyFromText;
                    }
                    if (size == 23 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                            && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                            && (sql[8 + offset] == 'P' || sql[8 + offset] == 'p')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                            && (sql[11 + offset] == 'Y' || sql[11 + offset] == 'y')
                            && (sql[12 + offset] == 'G' || sql[12 + offset] == 'g')
                            && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                            && (sql[14 + offset] == 'N' || sql[14 + offset] == 'n')
                            && (sql[15 + offset] == 'F' || sql[15 + offset] == 'f')
                            && (sql[16 + offset] == 'R' || sql[16 + offset] == 'r')
                            && (sql[17 + offset] == 'O' || sql[17 + offset] == 'o')
                            && (sql[18 + offset] == 'M' || sql[18 + offset] == 'm')
                            && (sql[19 + offset] == 'T' || sql[19 + offset] == 't')
                            && (sql[20 + offset] == 'E' || sql[20 + offset] == 'e')
                            && (sql[21 + offset] == 'X' || sql[21 + offset] == 'x')
                            && (sql[22 + offset] == 'T' || sql[22 + offset] == 't')) {
                        return Functions.ST_MultiPolygonFromText;
                    }
                    if (size == 15 && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                            && (sql[7 + offset] == 'Y' || sql[7 + offset] == 'y')
                            && (sql[8 + offset] == 'F' || sql[8 + offset] == 'f')
                            && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                            && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                            && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')
                            && (sql[12 + offset] == 'W' || sql[12 + offset] == 'w')
                            && (sql[13 + offset] == 'K' || sql[13 + offset] == 'k')
                            && (sql[14 + offset] == 'B' || sql[14 + offset] == 'b')) {
                        return Functions.ST_MPolyFromWKB;
                    }
                    if (size == 22 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                            && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                            && (sql[8 + offset] == 'P' || sql[8 + offset] == 'p')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                            && (sql[11 + offset] == 'Y' || sql[11 + offset] == 'y')
                            && (sql[12 + offset] == 'G' || sql[12 + offset] == 'g')
                            && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                            && (sql[14 + offset] == 'N' || sql[14 + offset] == 'n')
                            && (sql[15 + offset] == 'F' || sql[15 + offset] == 'f')
                            && (sql[16 + offset] == 'R' || sql[16 + offset] == 'r')
                            && (sql[17 + offset] == 'O' || sql[17 + offset] == 'o')
                            && (sql[18 + offset] == 'M' || sql[18 + offset] == 'm')
                            && (sql[19 + offset] == 'W' || sql[19 + offset] == 'w')
                            && (sql[20 + offset] == 'K' || sql[20 + offset] == 'k')
                            && (sql[21 + offset] == 'B' || sql[21 + offset] == 'b')) {
                        return Functions.ST_MultiPolygonFromWKB;
                    }
                }
                if (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') {
                    if (size < 12 || size > 19) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') {
                        if (sql[5 + offset] == 'M' || sql[5 + offset] == 'm') {
                            if (size == 16 && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g')
                                    && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                                    && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                    && (sql[9 + offset] == 'M' || sql[9 + offset] == 'm')
                                    && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                                    && (sql[11 + offset] == 'T' || sql[11 + offset] == 't')
                                    && (sql[12 + offset] == 'R' || sql[12 + offset] == 'r')
                                    && (sql[13 + offset] == 'I' || sql[13 + offset] == 'i')
                                    && (sql[14 + offset] == 'E' || sql[14 + offset] == 'e')
                                    && (sql[15 + offset] == 'S' || sql[15 + offset] == 's')) {
                                return Functions.ST_NumGeometries;
                            }
                            if (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') {
                                if (size < 18 || size > 19) {
                                    return 0;
                                }
                                if (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') {
                                    if (sql[8 + offset] == 'T' || sql[8 + offset] == 't') {
                                        if (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') {
                                            if (sql[10 + offset] == 'R'
                                                    || sql[10 + offset] == 'r') {
                                                if (sql[11 + offset] == 'I'
                                                        || sql[11 + offset] == 'i') {
                                                    if (sql[12 + offset] == 'O'
                                                            || sql[12 + offset] == 'o') {
                                                        if (sql[13 + offset] == 'R'
                                                                || sql[13 + offset] == 'r') {
                                                            if (sql[14 + offset] == 'R'
                                                                    || sql[14 + offset] == 'r') {
                                                                if (sql[15 + offset] == 'I'
                                                                        || sql[15
                                                                                + offset] == 'i') {
                                                                    if (sql[16 + offset] == 'N'
                                                                            || sql[16
                                                                                    + offset] == 'n') {
                                                                        if (sql[17 + offset] == 'G'
                                                                                || sql[17
                                                                                        + offset] == 'g') {
                                                                            if (size == 18) {
                                                                                return Functions.ST_NumInteriorRing;
                                                                            }
                                                                            if (size == 19
                                                                                    && (sql[18
                                                                                            + offset] == 'S'
                                                                                            || sql[18
                                                                                                    + offset] == 's')) {
                                                                                return Functions.ST_NumInteriorRings;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (size == 12 && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p')
                                    && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                    && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                    && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                                    && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                                    && (sql[11 + offset] == 'S' || sql[11 + offset] == 's')) {
                                return Functions.ST_NumPoints;
                            }
                        }
                    }
                }
                if (size == 11 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o')
                        && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v')
                        && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                        && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                        && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                        && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                        && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                        && (sql[10 + offset] == 'S' || sql[10 + offset] == 's')) {
                    return Functions.ST_Overlaps;
                }
                if (sql[3 + offset] == 'P' || sql[3 + offset] == 'p') {
                    if (size < 9 || size > 19) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') {
                            if (sql[6 + offset] == 'N' || sql[6 + offset] == 'n') {
                                if (sql[7 + offset] == 'T' || sql[7 + offset] == 't') {
                                    if (sql[8 + offset] == 'F' || sql[8 + offset] == 'f') {
                                        if (size < 15 || size > 19) {
                                            return 0;
                                        }
                                        if (sql[9 + offset] == 'R' || sql[9 + offset] == 'r') {
                                            if (sql[10 + offset] == 'O'
                                                    || sql[10 + offset] == 'o') {
                                                if (sql[11 + offset] == 'M'
                                                        || sql[11 + offset] == 'm') {
                                                    if (size == 19
                                                            && (sql[12 + offset] == 'G'
                                                                    || sql[12 + offset] == 'g')
                                                            && (sql[13 + offset] == 'E'
                                                                    || sql[13 + offset] == 'e')
                                                            && (sql[14 + offset] == 'O'
                                                                    || sql[14 + offset] == 'o')
                                                            && (sql[15 + offset] == 'H'
                                                                    || sql[15 + offset] == 'h')
                                                            && (sql[16 + offset] == 'A'
                                                                    || sql[16 + offset] == 'a')
                                                            && (sql[17 + offset] == 'S'
                                                                    || sql[17 + offset] == 's')
                                                            && (sql[18 + offset] == 'H'
                                                                    || sql[18 + offset] == 'h')) {
                                                        return Functions.ST_PointFromGeoHash;
                                                    }
                                                    if (size == 16
                                                            && (sql[12 + offset] == 'T'
                                                                    || sql[12 + offset] == 't')
                                                            && (sql[13 + offset] == 'E'
                                                                    || sql[13 + offset] == 'e')
                                                            && (sql[14 + offset] == 'X'
                                                                    || sql[14 + offset] == 'x')
                                                            && (sql[15 + offset] == 'T'
                                                                    || sql[15 + offset] == 't')) {
                                                        return Functions.ST_PointFromText;
                                                    }
                                                    if (size == 15
                                                            && (sql[12 + offset] == 'W'
                                                                    || sql[12 + offset] == 'w')
                                                            && (sql[13 + offset] == 'K'
                                                                    || sql[13 + offset] == 'k')
                                                            && (sql[14 + offset] == 'B'
                                                                    || sql[14 + offset] == 'b')) {
                                                        return Functions.ST_PointFromWKB;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (size == 9
                                            && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')) {
                                        return Functions.ST_PointN;
                                    }
                                }
                            }
                        }
                        if (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') {
                            if (size < 14 || size > 18) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'Y' || sql[6 + offset] == 'y') {
                                if (size == 15 && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f')
                                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                        && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                        && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm')
                                        && (sql[11 + offset] == 'T' || sql[11 + offset] == 't')
                                        && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')
                                        && (sql[13 + offset] == 'X' || sql[13 + offset] == 'x')
                                        && (sql[14 + offset] == 'T' || sql[14 + offset] == 't')) {
                                    return Functions.ST_PolyFromText;
                                }
                                if (size == 18 && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g')
                                        && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                        && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                                        && (sql[10 + offset] == 'F' || sql[10 + offset] == 'f')
                                        && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')
                                        && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o')
                                        && (sql[13 + offset] == 'M' || sql[13 + offset] == 'm')
                                        && (sql[14 + offset] == 'T' || sql[14 + offset] == 't')
                                        && (sql[15 + offset] == 'E' || sql[15 + offset] == 'e')
                                        && (sql[16 + offset] == 'X' || sql[16 + offset] == 'x')
                                        && (sql[17 + offset] == 'T' || sql[17 + offset] == 't')) {
                                    return Functions.ST_PolygonFromText;
                                }
                                if (size == 14 && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f')
                                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                        && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                                        && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm')
                                        && (sql[11 + offset] == 'W' || sql[11 + offset] == 'w')
                                        && (sql[12 + offset] == 'K' || sql[12 + offset] == 'k')
                                        && (sql[13 + offset] == 'B' || sql[13 + offset] == 'b')) {
                                    return Functions.ST_PolyFromWKB;
                                }
                                if (size == 17 && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g')
                                        && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                        && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')
                                        && (sql[10 + offset] == 'F' || sql[10 + offset] == 'f')
                                        && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')
                                        && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o')
                                        && (sql[13 + offset] == 'M' || sql[13 + offset] == 'm')
                                        && (sql[14 + offset] == 'W' || sql[14 + offset] == 'w')
                                        && (sql[15 + offset] == 'K' || sql[15 + offset] == 'k')
                                        && (sql[16 + offset] == 'B' || sql[16 + offset] == 'b')) {
                                    return Functions.ST_PolygonFromWKB;
                                }
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'S' || sql[3 + offset] == 's') {
                    if (size < 7 || size > 16) {
                        return 0;
                    }
                    if (size == 11 && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                            && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                            && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p')
                            && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')
                            && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                            && (sql[9 + offset] == 'F' || sql[9 + offset] == 'f')
                            && (sql[10 + offset] == 'Y' || sql[10 + offset] == 'y')) {
                        return Functions.ST_Simplify;
                    }
                    if (size == 7 && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                            && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                            && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd')) {
                        return Functions.ST_SRID;
                    }
                    if (size == 13 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                            && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                            && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                            && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                            && (sql[8 + offset] == 'P' || sql[8 + offset] == 'p')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'I' || sql[10 + offset] == 'i')
                            && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')
                            && (sql[12 + offset] == 'T' || sql[12 + offset] == 't')) {
                        return Functions.ST_StartPoint;
                    }
                    if (size == 9 && (sql[4 + offset] == 'W' || sql[4 + offset] == 'w')
                            && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                            && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p')
                            && (sql[7 + offset] == 'X' || sql[7 + offset] == 'x')
                            && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y')) {
                        return Functions.ST_SwapXY;
                    }
                    if (size == 16 && (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y')
                            && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                            && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd')
                            && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                            && (sql[8 + offset] == 'F' || sql[8 + offset] == 'f')
                            && (sql[9 + offset] == 'F' || sql[9 + offset] == 'f')
                            && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                            && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')
                            && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e')
                            && (sql[13 + offset] == 'N' || sql[13 + offset] == 'n')
                            && (sql[14 + offset] == 'C' || sql[14 + offset] == 'c')
                            && (sql[15 + offset] == 'E' || sql[15 + offset] == 'e')) {
                        return Functions.ST_SymDifference;
                    }
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (size < 10 || size > 12) {
                        return 0;
                    }
                    if (size == 10 && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o')
                            && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u')
                            && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                            && (sql[7 + offset] == 'H' || sql[7 + offset] == 'h')
                            && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                            && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')) {
                        return Functions.ST_Touches;
                    }
                    if (size == 12 && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')
                            && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                            && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                            && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                            && (sql[8 + offset] == 'F' || sql[8 + offset] == 'f')
                            && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o')
                            && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')
                            && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')) {
                        return Functions.ST_Transform;
                    }
                }
                if (size == 8 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u')
                        && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n')
                        && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                        && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                        && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')) {
                    return Functions.ST_Union;
                }
                if (size == 11 && (sql[3 + offset] == 'V' || sql[3 + offset] == 'v')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                        && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                        && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')
                        && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                        && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')
                        && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                    return Functions.ST_Validate;
                }
                if (size == 9 && (sql[3 + offset] == 'W' || sql[3 + offset] == 'w')
                        && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == 'H' || sql[6 + offset] == 'h')
                        && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i')
                        && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')) {
                    return Functions.ST_Within;
                }
                if (size == 4 && (sql[3 + offset] == 'X' || sql[3 + offset] == 'x')) {
                    return Functions.ST_X;
                }
                if (size == 4 && (sql[3 + offset] == 'Y' || sql[3 + offset] == 'y')) {
                    return Functions.ST_Y;
                }
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (size < 16 || size > 21) {
                    return 0;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'M' || sql[5 + offset] == 'm') {
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') {
                                    if (sql[8 + offset] == 'T' || sql[8 + offset] == 't') {
                                        if (sql[9 + offset] == '_' || sql[9 + offset] == '_') {
                                            if (sql[10 + offset] == 'D'
                                                    || sql[10 + offset] == 'd') {
                                                if (sql[11 + offset] == 'I'
                                                        || sql[11 + offset] == 'i') {
                                                    if (sql[12 + offset] == 'G'
                                                            || sql[12 + offset] == 'g') {
                                                        if (sql[13 + offset] == 'E'
                                                                || sql[13 + offset] == 'e') {
                                                            if (sql[14 + offset] == 'S'
                                                                    || sql[14 + offset] == 's') {
                                                                if (sql[15 + offset] == 'T'
                                                                        || sql[15
                                                                                + offset] == 't') {
                                                                    if (size == 16) {
                                                                        return Functions.STATEMENT_DIGEST;
                                                                    }
                                                                    if (size == 21 && (sql[16
                                                                            + offset] == '_'
                                                                            || sql[16
                                                                                    + offset] == '_')
                                                                            && (sql[17
                                                                                    + offset] == 'T'
                                                                                    || sql[17
                                                                                            + offset] == 't')
                                                                            && (sql[18
                                                                                    + offset] == 'E'
                                                                                    || sql[18
                                                                                            + offset] == 'e')
                                                                            && (sql[19
                                                                                    + offset] == 'X'
                                                                                    || sql[19
                                                                                            + offset] == 'x')
                                                                            && (sql[20
                                                                                    + offset] == 'T'
                                                                                    || sql[20
                                                                                            + offset] == 't')) {
                                                                        return Functions.STATEMENT_DIGEST_TEXT;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') {
                if (size < 3 || size > 11) {
                    return 0;
                }
                if (size == 3) {
                    return Functions.STD;
                }
                if (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') {
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'V' || sql[5 + offset] == 'v') {
                            if (size == 6) {
                                return Functions.STDDEV;
                            }
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (size == 10 && (sql[7 + offset] == 'P' || sql[7 + offset] == 'p')
                                        && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o')
                                        && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')) {
                                    return Functions.STDDEV_POP;
                                }
                                if (size == 11 && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                                        && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                                        && (sql[9 + offset] == 'M' || sql[9 + offset] == 'm')
                                        && (sql[10 + offset] == 'P' || sql[10 + offset] == 'p')) {
                                    return Functions.STDDEV_SAMP;
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size == 11 && (sql[3 + offset] == '_' || sql[3 + offset] == '_')
                        && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                        && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                        && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                        && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')
                        && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                        && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')
                        && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                    return Functions.STR_TO_DATE;
                }
                if (size == 6 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c')
                        && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm')
                        && (sql[5 + offset] == 'P' || sql[5 + offset] == 'p')) {
                    return Functions.STRCMP;
                }
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 3 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'B' || sql[2 + offset] == 'b') {
                if (size < 6 || size > 15) {
                    return 0;
                }
                if (size == 7 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.SUBDATE;
                }
                if (sql[3 + offset] == 'S' || sql[3 + offset] == 's') {
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') {
                            if (size == 6) {
                                return Functions.SUBSTR;
                            }
                            if (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') {
                                if (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') {
                                    if (sql[8 + offset] == 'G' || sql[8 + offset] == 'g') {
                                        if (size == 9) {
                                            return Functions.SUBSTRING;
                                        }
                                        if (size == 15
                                                && (sql[9 + offset] == '_'
                                                        || sql[9 + offset] == '_')
                                                && (sql[10 + offset] == 'I'
                                                        || sql[10 + offset] == 'i')
                                                && (sql[11 + offset] == 'N'
                                                        || sql[11 + offset] == 'n')
                                                && (sql[12 + offset] == 'D'
                                                        || sql[12 + offset] == 'd')
                                                && (sql[13 + offset] == 'E'
                                                        || sql[13 + offset] == 'e')
                                                && (sql[14 + offset] == 'X'
                                                        || sql[14 + offset] == 'x')) {
                                            return Functions.SUBSTRING_INDEX;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (size == 7 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')
                        && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                        && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.SUBTIME;
                }
            }
            if (size == 3 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm')) {
                return Functions.SUM;
            }
        }
        if (sql[1 + offset] == 'Y' || sql[1 + offset] == 'y') {
            if (size < 7 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size == 7 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.SYSDATE;
                }
                if (size == 11 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')
                        && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                        && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                        && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                        && (sql[7 + offset] == 'U' || sql[7 + offset] == 'u')
                        && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                        && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                        && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')) {
                    return Functions.SYSTEM_USER;
                }
                if (size == 12 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (
                        sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'M'
                        || sql[5 + offset] == 'm') && (sql[6 + offset] == 'E'
                        || sql[6 + offset] == 'e') && (sql[7 + offset] == 'S'
                        || sql[7 + offset] == 's') && (sql[8 + offset] == 'T'
                        || sql[8 + offset] == 't') && (sql[9 + offset] == 'A'
                        || sql[9 + offset] == 'a') && (sql[10 + offset] == 'M'
                        || sql[10 + offset] == 'm') && (sql[11 + offset] == 'P'
                        || sql[11 + offset] == 'p')) {
                    return Functions.SYSTIMESTAMP;
                }
                if (size == 15 && (sql[3 + offset] == '_') && (sql[4 + offset] == 'E'
                        || sql[4 + offset] == 'e') && (sql[5 + offset] == 'X'
                        || sql[5 + offset] == 'x') && (sql[6 + offset] == 'T'
                        || sql[6 + offset] == 't') && (sql[7 + offset] == 'R'
                        || sql[7 + offset] == 'r') && (sql[8 + offset] == 'A'
                        || sql[8 + offset] == 'a') && (sql[9 + offset] == 'C'
                        || sql[9 + offset] == 'c') && (sql[10 + offset] == 'T'
                        || sql[10 + offset] == 't') && (sql[11 + offset] == '_') && (
                        sql[12 + offset] == 'U' || sql[12 + offset] == 'u') && (
                        sql[13 + offset] == 'T' || sql[13 + offset] == 't') && (
                        sql[14 + offset] == 'C' || sql[14 + offset] == 'c')) {
                    return Functions.SYS_EXTRACT_UTC;
                }
            }
        }
        return 0;
    }

    private static int parseT(byte[] sql, int offset, int size) {
        if (size < 3 || size > 16) {
            return 0;
        }
        if (size == 3 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a')
                && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n')) {
            return Functions.TAN;
        }
        if (size == 4 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (
                sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'H'
                || sql[3 + offset] == 'h')) {
            return Functions.TANH;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 4 || size > 16) {
                return 0;
            }
            if (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') {
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 4) {
                        return Functions.TIME;
                    }
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (size != 11) {
                            return 0;
                        }
                        if (size == 11 && (sql[5 + offset] == 'F' || sql[5 + offset] == 'f')
                                && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                                && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                                && (sql[8 + offset] == 'M' || sql[8 + offset] == 'm')
                                && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a')
                                && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')) {
                            return Functions.TIME_FORMAT;
                        }
                        if (size == 11 && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                                && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                                && (sql[7 + offset] == '_' || sql[7 + offset] == '_')
                                && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                                && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                                && (sql[10 + offset] == 'C' || sql[10 + offset] == 'c')) {
                            return Functions.TIME_TO_SEC;
                        }
                    }
                    if (size == 8 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                            && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i')
                            && (sql[6 + offset] == 'F' || sql[6 + offset] == 'f')
                            && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f')) {
                        return Functions.TIMEDIFF;
                    }
                    if (sql[4 + offset] == 'S' || sql[4 + offset] == 's') {
                        if (size < 9 || size > 16) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') {
                                if (sql[7 + offset] == 'M' || sql[7 + offset] == 'm') {
                                    if (sql[8 + offset] == 'P' || sql[8 + offset] == 'p') {
                                        if (size == 9) {
                                            return Functions.TIMESTAMP;
                                        }
                                        if (size == 12
                                                && (sql[9 + offset] == 'A'
                                                        || sql[9 + offset] == 'a')
                                                && (sql[10 + offset] == 'D'
                                                        || sql[10 + offset] == 'd')
                                                && (sql[11 + offset] == 'D'
                                                        || sql[11 + offset] == 'd')) {
                                            return Functions.TIMESTAMPADD;
                                        }
                                        if (size == 13
                                                && (sql[9 + offset] == 'D'
                                                        || sql[9 + offset] == 'd')
                                                && (sql[10 + offset] == 'I'
                                                        || sql[10 + offset] == 'i')
                                                && (sql[11 + offset] == 'F'
                                                        || sql[11 + offset] == 'f')
                                                && (sql[12 + offset] == 'F'
                                                        || sql[12 + offset] == 'f')) {
                                            return Functions.TIMESTAMPDIFF;
                                        }
                                        if (size == 16 && (sql[9 + offset] == '_'
                                                || sql[9 + offset] == '_') && (
                                                sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                                                && (sql[11 + offset] == 'O'
                                                || sql[11 + offset] == 'o') && (
                                                sql[12 + offset] == '_' || sql[12 + offset] == '_')
                                                && (sql[13 + offset] == 'S'
                                                || sql[13 + offset] == 's') && (
                                                sql[14 + offset] == 'C' || sql[14 + offset] == 'c')
                                                && (sql[15 + offset] == 'N'
                                                || sql[15 + offset] == 'n')) {
                                            return Functions.TIMESTAMP_TO_SCN;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 6 || size > 16) {
                return 0;
            }
            if (sql[2 + offset] == '_' || sql[2 + offset] == '_') {
                if (size == 6 && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (
                        sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (sql[5 + offset] == 'B'
                        || sql[5 + offset] == 'b')) {
                    return Functions.TO_LOB;
                }
                if (size == 9 && (sql[3 + offset] == 'B' || sql[3 + offset] == 'b')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                        && (sql[7 + offset] == '6' || sql[7 + offset] == '6')
                        && (sql[8 + offset] == '4' || sql[8 + offset] == '4')) {
                    return Functions.TO_BASE64;
                }
                if (size == 7 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'Y' || sql[5 + offset] == 'y')
                        && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                    return Functions.TO_DAYS;
                }
                if (size == 10 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                        && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                        && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c')
                        && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                        && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')
                        && (sql[8 + offset] == 'D' || sql[8 + offset] == 'd')
                        && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')) {
                    return Functions.TO_SECONDS;
                }
                if (size == 7 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c')
                        && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h')
                        && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                        && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')) {
                    return Functions.TO_CHAR;
                }
                if (size == 7 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (
                        sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'B'
                        || sql[6 + offset] == 'b')) {
                    return Functions.TO_CLOB;
                }
                if (size == 7 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                        && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Functions.TO_DATE;
                }
                if (size == 8 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                        && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c')
                        && (sql[5 + offset] == 'H' || sql[5 + offset] == 'h')
                        && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                        && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')) {
                    return Functions.TO_NCHAR;
                }
                if (size == 8 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (
                        sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (sql[5 + offset] == 'L'
                        || sql[5 + offset] == 'l') && (sql[6 + offset] == 'O'
                        || sql[6 + offset] == 'o') && (sql[7 + offset] == 'B'
                        || sql[7 + offset] == 'b')) {
                    return Functions.TO_NCHAR;
                }
                if (size == 9 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')
                        && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u')
                        && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')
                        && (sql[6 + offset] == 'B' || sql[6 + offset] == 'b')
                        && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                        && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')) {
                    return Functions.TO_NUMBER;
                }
                if (size == 12 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (
                        sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'M'
                        || sql[5 + offset] == 'm') && (sql[6 + offset] == 'E'
                        || sql[6 + offset] == 'e') && (sql[7 + offset] == 'S'
                        || sql[7 + offset] == 's') && (sql[8 + offset] == 'T'
                        || sql[8 + offset] == 't') && (sql[9 + offset] == 'A'
                        || sql[9 + offset] == 'a') && (sql[10 + offset] == 'M'
                        || sql[10 + offset] == 'm') && (sql[11 + offset] == 'P'
                        || sql[11 + offset] == 'p')) {
                    return Functions.TO_TIMESTAMP;
                }
                if (size == 13 && (sql[3 + offset] == 'M' || sql[3 + offset] == 'm') && (
                        sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (sql[5 + offset] == 'L'
                        || sql[5 + offset] == 'l') && (sql[6 + offset] == 'T'
                        || sql[6 + offset] == 't') && (sql[7 + offset] == 'I'
                        || sql[7 + offset] == 'i') && (sql[8 + offset] == '_'
                        || sql[8 + offset] == '_') && (sql[9 + offset] == 'B'
                        || sql[9 + offset] == 'b') && (sql[10 + offset] == 'Y'
                        || sql[10 + offset] == 'y') && (sql[11 + offset] == 'T'
                        || sql[11 + offset] == 't') && (sql[12 + offset] == 'E'
                        || sql[12 + offset] == 'e')) {
                    return Functions.TO_MULTI_BYTE;
                }
                if (size == 14 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (
                        sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N'
                        || sql[5 + offset] == 'n') && (sql[6 + offset] == 'G'
                        || sql[6 + offset] == 'g') && (sql[7 + offset] == 'L'
                        || sql[7 + offset] == 'l') && (sql[8 + offset] == 'E'
                        || sql[8 + offset] == 'e') && (sql[9 + offset] == '_'
                        || sql[9 + offset] == '_') && (sql[10 + offset] == 'B'
                        || sql[10 + offset] == 'b') && (sql[11 + offset] == 'Y'
                        || sql[11 + offset] == 'y') && (sql[12 + offset] == 'T'
                        || sql[12 + offset] == 't') && (sql[13 + offset] == 'E'
                        || sql[13 + offset] == 'e')) {
                    return Functions.TO_SINGLE_BYTE;
                }
                if (size == 15 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (
                        sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'M'
                        || sql[5 + offset] == 'm') && (sql[6 + offset] == 'E'
                        || sql[6 + offset] == 'e') && (sql[7 + offset] == 'S'
                        || sql[7 + offset] == 's') && (sql[8 + offset] == 'T'
                        || sql[8 + offset] == 't') && (sql[9 + offset] == 'A'
                        || sql[9 + offset] == 'a') && (sql[10 + offset] == 'M'
                        || sql[10 + offset] == 'm') && (sql[11 + offset] == 'P'
                        || sql[11 + offset] == 'p') && (sql[12 + offset] == '_') && (
                        sql[13 + offset] == 'T' || sql[13 + offset] == 't') && (
                        sql[14 + offset] == 'Z' || sql[14 + offset] == 'z')) {
                    return Functions.TO_TIMESTAMP_TZ;
                }
                if (size == 16 && (sql[3 + offset] == 'B' || sql[3 + offset] == 'b') && (
                        sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N'
                        || sql[5 + offset] == 'n') && (sql[6 + offset] == 'A'
                        || sql[6 + offset] == 'a') && (sql[7 + offset] == 'R'
                        || sql[7 + offset] == 'r') && (sql[8 + offset] == 'Y'
                        || sql[8 + offset] == 'y') && (sql[9 + offset] == '_') && (
                        sql[10 + offset] == 'D' || sql[10 + offset] == 'd') && (
                        sql[11 + offset] == 'O' || sql[11 + offset] == 'o') && (
                        sql[12 + offset] == 'U' || sql[12 + offset] == 'u') && (
                        sql[13 + offset] == 'B' || sql[13 + offset] == 'b') && (
                        sql[14 + offset] == 'L' || sql[14 + offset] == 'l') && (
                        sql[15 + offset] == 'E' || sql[15 + offset] == 'e')) {
                    return Functions.TO_BINARY_DOUBLE;
                }
                if (size == 15 && (sql[3 + offset] == 'B' || sql[3 + offset] == 'b') && (
                        sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N'
                        || sql[5 + offset] == 'n') && (sql[6 + offset] == 'A'
                        || sql[6 + offset] == 'a') && (sql[7 + offset] == 'R'
                        || sql[7 + offset] == 'r') && (sql[8 + offset] == 'Y'
                        || sql[8 + offset] == 'y') && (sql[9 + offset] == '_') && (
                        sql[10 + offset] == 'F' || sql[10 + offset] == 'f') && (
                        sql[11 + offset] == 'L' || sql[11 + offset] == 'l') && (
                        sql[12 + offset] == 'O' || sql[12 + offset] == 'o') && (
                        sql[13 + offset] == 'A' || sql[13 + offset] == 'a') && (
                        sql[14 + offset] == 'T' || sql[14 + offset] == 't')) {
                    return Functions.TO_BINARY_FLOAT;
                }
                if (size == 13 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') && (
                        sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N'
                        || sql[6 + offset] == 'n') && (sql[7 + offset] == 'T'
                        || sql[7 + offset] == 't') && (sql[8 + offset] == 'E'
                        || sql[8 + offset] == 'e') && (sql[9 + offset] == 'R'
                        || sql[9 + offset] == 'r') && (sql[10 + offset] == 'V'
                        || sql[10 + offset] == 'v') && (sql[11 + offset] == 'A'
                        || sql[11 + offset] == 'a') && (sql[12 + offset] == 'L'
                        || sql[12 + offset] == 'l')) {
                    return Functions.TO_DSINTERVAL;
                }
                if (size == 13 && (sql[3 + offset] == 'Y' || sql[3 + offset] == 'y') && (
                        sql[4 + offset] == 'M' || sql[4 + offset] == 'm') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N'
                        || sql[6 + offset] == 'n') && (sql[7 + offset] == 'T'
                        || sql[7 + offset] == 't') && (sql[8 + offset] == 'E'
                        || sql[8 + offset] == 'e') && (sql[9 + offset] == 'R'
                        || sql[9 + offset] == 'r') && (sql[10 + offset] == 'V'
                        || sql[10 + offset] == 'v') && (sql[11 + offset] == 'A'
                        || sql[11 + offset] == 'a') && (sql[12 + offset] == 'L'
                        || sql[12 + offset] == 'l')) {
                    return Functions.TO_YMINTERVAL;
                }
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 4 || size > 9) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i')
                    && (sql[3 + offset] == 'M' || sql[3 + offset] == 'm')) {
                return Functions.TRIM;
            }
            if (size == 5 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (
                    sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'D'
                    || sql[4 + offset] == 'd')) {
                return Functions.TREAD;
            }
            if (size == 9 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (
                    sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == 'S'
                    || sql[4 + offset] == 's') && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')
                    && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (sql[7 + offset] == 'T'
                    || sql[7 + offset] == 't') && (sql[8 + offset] == 'E'
                    || sql[8 + offset] == 'e')) {
                return Functions.TRANSLATE;
            }
            if ((sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'N'
                    || sql[3 + offset] == 'n') && (sql[4 + offset] == 'C'
                    || sql[4 + offset] == 'c')) {
                if (size < 5 || size > 8) {
                    return 0;
                }
                if (size == 5) {
                    return Functions.TRUNC;
                }
                if (size == 8 && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (
                        sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (sql[7 + offset] == 'E'
                        || sql[7 + offset] == 'e')) {
                    return Functions.TRUNCATE;
                }
            }
        }
        if (size == 9 && (sql[1 + offset] == 'Z' || sql[1 + offset] == 'z') && (sql[2 + offset]
                == '_') && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (
                sql[4 + offset] == 'F' || sql[4 + offset] == 'f') && (sql[5 + offset] == 'F'
                || sql[5 + offset] == 'f') && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't')) {
            return Functions.TZ_OFFSET;
        }
        return 0;
    }

    private static int parseU(byte[] sql, int offset, int size) {
        if (size < 4 || size > 19) {
            return 0;
        }
        if (size == 5 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c')
                && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a')
                && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
            return Functions.UCASE;
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 5 || size > 19) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (size < 10 || size > 19) {
                    return 0;
                }
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') {
                        if (sql[5 + offset] == 'P' || sql[5 + offset] == 'p') {
                            if (sql[6 + offset] == 'R' || sql[6 + offset] == 'r') {
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (sql[8 + offset] == 'S' || sql[8 + offset] == 's') {
                                        if (sql[9 + offset] == 'S' || sql[9 + offset] == 's') {
                                            if (size == 10) {
                                                return Functions.UNCOMPRESS;
                                            }
                                            if (size == 19
                                                    && (sql[10 + offset] == 'E'
                                                            || sql[10 + offset] == 'e')
                                                    && (sql[11 + offset] == 'D'
                                                            || sql[11 + offset] == 'd')
                                                    && (sql[12 + offset] == '_'
                                                            || sql[12 + offset] == '_')
                                                    && (sql[13 + offset] == 'L'
                                                            || sql[13 + offset] == 'l')
                                                    && (sql[14 + offset] == 'E'
                                                            || sql[14 + offset] == 'e')
                                                    && (sql[15 + offset] == 'N'
                                                            || sql[15 + offset] == 'n')
                                                    && (sql[16 + offset] == 'G'
                                                            || sql[16 + offset] == 'g')
                                                    && (sql[17 + offset] == 'T'
                                                            || sql[17 + offset] == 't')
                                                    && (sql[18 + offset] == 'H'
                                                            || sql[18 + offset] == 'h')) {
                                                return Functions.UNCOMPRESSED_LENGTH;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (size == 5 && (sql[2 + offset] == 'H' || sql[2 + offset] == 'h')
                    && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                    && (sql[4 + offset] == 'X' || sql[4 + offset] == 'x')) {
                return Functions.UNHEX;
            }
            if (size == 6 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (
                    sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'T'
                    || sql[4 + offset] == 't') && (sql[5 + offset] == 'R'
                    || sql[5 + offset] == 'r')) {
                return Functions.UNISTR;
            }
            if (size == 14 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i')
                    && (sql[3 + offset] == 'X' || sql[3 + offset] == 'x')
                    && (sql[4 + offset] == '_' || sql[4 + offset] == '_')
                    && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                    && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                    && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm')
                    && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')
                    && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')
                    && (sql[10 + offset] == 'T' || sql[10 + offset] == 't')
                    && (sql[11 + offset] == 'A' || sql[11 + offset] == 'a')
                    && (sql[12 + offset] == 'M' || sql[12 + offset] == 'm')
                    && (sql[13 + offset] == 'P' || sql[13 + offset] == 'p')) {
                return Functions.UNIX_TIMESTAMP;
            }
        }
        if (size == 9 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p')
                && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd')
                && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a')
                && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')
                && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                && (sql[6 + offset] == 'X' || sql[6 + offset] == 'x')
                && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm')
                && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')) {
            return Functions.UpdateXML;
        }
        if (size == 5 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p')
                && (sql[2 + offset] == 'P' || sql[2 + offset] == 'p')
                && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')
                && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
            return Functions.UPPER;
        }
        if (size == 4 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's')
                && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e')
                && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')) {
            return Functions.USER;
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (size < 8 || size > 13) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 8 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                            && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                            && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                            && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Functions.UTC_DATE;
                    }
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') {
                            if (sql[6 + offset] == 'M' || sql[6 + offset] == 'm') {
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (size == 8) {
                                        return Functions.UTC_TIME;
                                    }
                                    if (size == 13
                                            && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')
                                            && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')
                                            && (sql[10 + offset] == 'A' || sql[10 + offset] == 'a')
                                            && (sql[11 + offset] == 'M' || sql[11 + offset] == 'm')
                                            && (sql[12 + offset] == 'P'
                                                    || sql[12 + offset] == 'p')) {
                                        return Functions.UTC_TIMESTAMP;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 4 || size > 11) {
                return 0;
            }
            if (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') {
                if (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') {
                    if (size == 4) {
                        return Functions.UUID;
                    }
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (size < 10 || size > 11) {
                            return 0;
                        }
                        if (size == 10 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')
                                && (sql[6 + offset] == 'H' || sql[6 + offset] == 'h')
                                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o')
                                && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r')
                                && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                            return Functions.UUID_SHORT;
                        }
                        if (size == 11 && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                                && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                                && (sql[7 + offset] == '_' || sql[7 + offset] == '_')
                                && (sql[8 + offset] == 'B' || sql[8 + offset] == 'b')
                                && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i')
                                && (sql[10 + offset] == 'N' || sql[10 + offset] == 'n')) {
                            return Functions.UUID_TO_BIN;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseV(byte[] sql, int offset, int size) {
        if (size < 5 || size > 26) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size == 26 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                        && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                        && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                        && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                        && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                        && (sql[8 + offset] == '_' || sql[8 + offset] == '_')
                        && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p')
                        && (sql[10 + offset] == 'A' || sql[10 + offset] == 'a')
                        && (sql[11 + offset] == 'S' || sql[11 + offset] == 's')
                        && (sql[12 + offset] == 'S' || sql[12 + offset] == 's')
                        && (sql[13 + offset] == 'W' || sql[13 + offset] == 'w')
                        && (sql[14 + offset] == 'O' || sql[14 + offset] == 'o')
                        && (sql[15 + offset] == 'R' || sql[15 + offset] == 'r')
                        && (sql[16 + offset] == 'D' || sql[16 + offset] == 'd')
                        && (sql[17 + offset] == '_' || sql[17 + offset] == '_')
                        && (sql[18 + offset] == 'S' || sql[18 + offset] == 's')
                        && (sql[19 + offset] == 'T' || sql[19 + offset] == 't')
                        && (sql[20 + offset] == 'R' || sql[20 + offset] == 'r')
                        && (sql[21 + offset] == 'E' || sql[21 + offset] == 'e')
                        && (sql[22 + offset] == 'N' || sql[22 + offset] == 'n')
                        && (sql[23 + offset] == 'G' || sql[23 + offset] == 'g')
                        && (sql[24 + offset] == 'T' || sql[24 + offset] == 't')
                        && (sql[25 + offset] == 'H' || sql[25 + offset] == 'h')) {
                    return Functions.VALIDATE_PASSWORD_STRENGTH;
                }
                if (size == 6 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u')
                        && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')
                        && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                    return Functions.VALUES;
                }
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size < 7 || size > 8) {
                    return 0;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 7 && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p')
                            && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                            && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p')) {
                        return Functions.VAR_POP;
                    }
                    if (size == 8 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')
                            && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                            && (sql[6 + offset] == 'M' || sql[6 + offset] == 'm')
                            && (sql[7 + offset] == 'P' || sql[7 + offset] == 'p')) {
                        return Functions.VAR_SAMP;
                    }
                }
                if (size == 8 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i')
                        && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a')
                        && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')
                        && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                        && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                    return Functions.VARIANCE;
                }
            }
        }
        if (size == 7 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e')
                && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r')
                && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')
                && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i')
                && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o')
                && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')) {
            return Functions.VERSION;
        }
        if (size == 5 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's') && (
                sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'Z'
                || sql[3 + offset] == 'z') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
            return Functions.VSIZE;
        }
        return 0;
    }

    private static int parseW(byte[] sql, int offset, int size) {
        if (size < 4 || size > 33) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 26 || size > 33) {
                return 0;
            }
            if (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') {
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (size == 26 && (sql[5 + offset] == 'F' || sql[5 + offset] == 'f')
                                && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                                && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')
                                && (sql[8 + offset] == '_' || sql[8 + offset] == '_')
                                && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')
                                && (sql[10 + offset] == 'X' || sql[10 + offset] == 'x')
                                && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')
                                && (sql[12 + offset] == 'C' || sql[12 + offset] == 'c')
                                && (sql[13 + offset] == 'U' || sql[13 + offset] == 'u')
                                && (sql[14 + offset] == 'T' || sql[14 + offset] == 't')
                                && (sql[15 + offset] == 'E' || sql[15 + offset] == 'e')
                                && (sql[16 + offset] == 'D' || sql[16 + offset] == 'd')
                                && (sql[17 + offset] == '_' || sql[17 + offset] == '_')
                                && (sql[18 + offset] == 'G' || sql[18 + offset] == 'g')
                                && (sql[19 + offset] == 'T' || sql[19 + offset] == 't')
                                && (sql[20 + offset] == 'I' || sql[20 + offset] == 'i')
                                && (sql[21 + offset] == 'D' || sql[21 + offset] == 'd')
                                && (sql[22 + offset] == '_' || sql[22 + offset] == '_')
                                && (sql[23 + offset] == 'S' || sql[23 + offset] == 's')
                                && (sql[24 + offset] == 'E' || sql[24 + offset] == 'e')
                                && (sql[25 + offset] == 'T' || sql[25 + offset] == 't')) {
                            return Functions.WAIT_FOR_EXECUTED_GTID_SET;
                        }
                        if (size == 33 && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u')
                                && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')
                                && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i')
                                && (sql[9 + offset] == 'L' || sql[9 + offset] == 'l')
                                && (sql[10 + offset] == '_' || sql[10 + offset] == '_')
                                && (sql[11 + offset] == 'S' || sql[11 + offset] == 's')
                                && (sql[12 + offset] == 'Q' || sql[12 + offset] == 'q')
                                && (sql[13 + offset] == 'L' || sql[13 + offset] == 'l')
                                && (sql[14 + offset] == '_' || sql[14 + offset] == '_')
                                && (sql[15 + offset] == 'T' || sql[15 + offset] == 't')
                                && (sql[16 + offset] == 'H' || sql[16 + offset] == 'h')
                                && (sql[17 + offset] == 'R' || sql[17 + offset] == 'r')
                                && (sql[18 + offset] == 'E' || sql[18 + offset] == 'e')
                                && (sql[19 + offset] == 'A' || sql[19 + offset] == 'a')
                                && (sql[20 + offset] == 'D' || sql[20 + offset] == 'd')
                                && (sql[21 + offset] == '_' || sql[21 + offset] == '_')
                                && (sql[22 + offset] == 'A' || sql[22 + offset] == 'a')
                                && (sql[23 + offset] == 'F' || sql[23 + offset] == 'f')
                                && (sql[24 + offset] == 'T' || sql[24 + offset] == 't')
                                && (sql[25 + offset] == 'E' || sql[25 + offset] == 'e')
                                && (sql[26 + offset] == 'R' || sql[26 + offset] == 'r')
                                && (sql[27 + offset] == '_' || sql[27 + offset] == '_')
                                && (sql[28 + offset] == 'G' || sql[28 + offset] == 'g')
                                && (sql[29 + offset] == 'T' || sql[29 + offset] == 't')
                                && (sql[30 + offset] == 'I' || sql[30 + offset] == 'i')
                                && (sql[31 + offset] == 'D' || sql[31 + offset] == 'd')
                                && (sql[32 + offset] == 'S' || sql[32 + offset] == 's')) {
                            return Functions.WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS;
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 4 || size > 13) {
                return 0;
            }
            if (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') {
                if (size < 4 || size > 10) {
                    return 0;
                }
                if (sql[3 + offset] == 'K' || sql[3 + offset] == 'k') {
                    if (size == 4) {
                        return Functions.WEEK;
                    }
                    if (size == 7 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')
                            && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a')
                            && (sql[6 + offset] == 'Y' || sql[6 + offset] == 'y')) {
                        return Functions.WEEKDAY;
                    }
                    if (size == 10 && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o')
                            && (sql[5 + offset] == 'F' || sql[5 + offset] == 'f')
                            && (sql[6 + offset] == 'Y' || sql[6 + offset] == 'y')
                            && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')
                            && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a')
                            && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')) {
                        return Functions.WEEKOFYEAR;
                    }
                }
            }
            if (size == 13 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i')
                    && (sql[3 + offset] == 'G' || sql[3 + offset] == 'g')
                    && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h')
                    && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')
                    && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                    && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')
                    && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')
                    && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')
                    && (sql[10 + offset] == 'I' || sql[10 + offset] == 'i')
                    && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')
                    && (sql[12 + offset] == 'G' || sql[12 + offset] == 'g')) {
                return Functions.WEIGHT_STRING;
            }
        }
        if (size == 12 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (
                sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h')
                && (sql[5 + offset] == '_') && (sql[6 + offset] == 'B' || sql[6 + offset] == 'b')
                && (sql[7 + offset] == 'U' || sql[7 + offset] == 'u') && (sql[8 + offset] == 'C'
                || sql[8 + offset] == 'c') && (sql[9 + offset] == 'K' || sql[9 + offset] == 'k')
                && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e') && (sql[11 + offset] == 'T'
                || sql[11 + offset] == 't')) {
            return Functions.WIDTH_BUCKET;
        }
        return 0;
    }

    private static int parseY(byte[] sql, int offset, int size) {
        if (size < 4 || size > 8) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size == 4) {
                        return Functions.YEAR;
                    }
                    if (size == 8 && (sql[4 + offset] == 'W' || sql[4 + offset] == 'w')
                            && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')
                            && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                            && (sql[7 + offset] == 'K' || sql[7 + offset] == 'k')) {
                        return Functions.YEARWEEK;
                    }
                }
            }
        }
        return 0;
    }

}
