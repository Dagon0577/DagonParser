package parser.token;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public class TokenParser {
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
            case 'k':
            case 'K':
                return parseK(sql, offset, size);
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
            case 'x':
            case 'X':
                return parseX(sql, offset, size);
            case 'y':
            case 'Y':
                return parseY(sql, offset, size);
            case 'z':
            case 'Z':
                return parseZ(sql, offset, size);
        }
        return 0;
    }

    private static int parseA(byte[] sql, int offset, int size) {
        if (size < 2 || size > 10) {
            return 0;
        }
        if (size == 10 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c') && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'S'
            || sql[4 + offset] == 's') && (sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'I'
            || sql[6 + offset] == 'i') && (sql[7 + offset] == 'B' || sql[7 + offset] == 'b') && (sql[8 + offset] == 'L'
            || sql[8 + offset] == 'l') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
            return Token.KW_ACCESSIBLE;
        }
        if (size == 3 && (sql[1 + offset] == 'D' || sql[1 + offset] == 'd') && (sql[2 + offset] == 'D'
            || sql[2 + offset] == 'd')) {
            return Token.KW_ADD;
        }
        if (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') {
            if (size < 3 || size > 5) {
                return 0;
            }
            if (size == 3 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l')) {
                return Token.KW_ALL;
            }
            if (size == 5 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
                return Token.KW_ALTER;
            }
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 3 || size > 7) {
                return 0;
            }
            if (size == 7 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y') && (
                sql[5 + offset] == 'Z' || sql[5 + offset] == 'z') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Token.KW_ANALYZE;
            }
            if (size == 3 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd')) {
                return Token.KW_AND;
            }
        }
        if (sql[1 + offset] == 'S' || sql[1 + offset] == 's') {
            if (size == 2) {
                return Token.KW_AS;
            }
            if (size == 3 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c')) {
                return Token.KW_ASC;
            }
            if (size == 10 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'V'
                || sql[8 + offset] == 'v') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                return Token.KW_ASENSITIVE;
            }
        }
        return 0;
    }

    private static int parseB(byte[] sql, int offset, int size) {
        if (size < 2 || size > 7) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 6 || size > 7) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'F' || sql[2 + offset] == 'f') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Token.KW_BEFORE;
            }
            if (size == 7 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'W'
                || sql[3 + offset] == 'w') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'N'
                || sql[6 + offset] == 'n')) {
                return Token.KW_BETWEEN;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size != 6) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                return Token.KW_BIGINT;
            }
            if (size == 6 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'Y' || sql[5 + offset] == 'y')) {
                return Token.KW_BINARY;
            }
        }
        if (size == 4 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') && (sql[2 + offset] == 'O'
            || sql[2 + offset] == 'o') && (sql[3 + offset] == 'B' || sql[3 + offset] == 'b')) {
            return Token.KW_BLOB;
        }
        if (size == 4 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') && (sql[2 + offset] == 'T'
            || sql[2 + offset] == 't') && (sql[3 + offset] == 'H' || sql[3 + offset] == 'h')) {
            return Token.KW_BOTH;
        }
        if (size == 2 && (sql[1 + offset] == 'Y' || sql[1 + offset] == 'y')) {
            return Token.KW_BY;
        }
        return 0;
    }

    private static int parseC(byte[] sql, int offset, int size) {
        if (size < 4 || size > 17) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l')) {
                return Token.KW_CALL;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size == 7 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Token.KW_CASCADE;
                }
                if (size == 4 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')) {
                    return Token.KW_CASE;
                }
            }
        }
        if (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') {
            if (size < 4 || size > 9) {
                return 0;
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (size == 6 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == 'G'
                    || sql[4 + offset] == 'g') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Token.KW_CHANGE;
                }
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size == 4) {
                        return Token.KW_CHAR;
                    }
                    if (size == 9 && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (sql[5 + offset] == 'C'
                        || sql[5 + offset] == 'c') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'R'
                        || sql[8 + offset] == 'r')) {
                        return Token.KW_CHARACTER;
                    }
                }
            }
            if (size == 5 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'K' || sql[4 + offset] == 'k')) {
                return Token.KW_CHECK;
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 6 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size < 6 || size > 7) {
                    return 0;
                }
                if (size == 7 && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Token.KW_COLLATE;
                }
                if (size == 6 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') && (sql[4 + offset] == 'M'
                    || sql[4 + offset] == 'm') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')) {
                    return Token.KW_COLUMN;
                }
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 7 || size > 10) {
                    return 0;
                }
                if (size == 9 && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (
                    sql[6 + offset] == 'I' || sql[6 + offset] == 'i') && (sql[7 + offset] == 'O'
                    || sql[7 + offset] == 'o') && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')) {
                    return Token.KW_CONDITION;
                }
                if (size == 10 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'T'
                    || sql[4 + offset] == 't') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (sql[7 + offset] == 'I'
                    || sql[7 + offset] == 'i') && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n') && (
                    sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                    return Token.KW_CONSTRAINT;
                }
                if (size == 8 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (
                    sql[6 + offset] == 'U' || sql[6 + offset] == 'u') && (sql[7 + offset] == 'E'
                    || sql[7 + offset] == 'e')) {
                    return Token.KW_CONTINUE;
                }
                if (size == 7 && (sql[3 + offset] == 'V' || sql[3 + offset] == 'v') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                    return Token.KW_CONVERT;
                }
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 5 || size > 6) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Token.KW_CREATE;
            }
            if (size == 5 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                return Token.KW_CROSS;
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size == 4 && (sql[2 + offset] == 'B' || sql[2 + offset] == 'b') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e')) {
                return Token.KW_CUBE;
            }
            if (size == 9 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (
                sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't')) {
                return Token.KW_CUME_DIST;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size < 6 || size > 17) {
                    return 0;
                }
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size < 12 || size > 17) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (sql[7 + offset] == '_' || sql[7 + offset] == '_') {
                                    if (size == 12 && (sql[8 + offset] == 'D' || sql[8 + offset] == 'd') && (
                                        sql[9 + offset] == 'A' || sql[9 + offset] == 'a') && (sql[10 + offset] == 'T'
                                        || sql[10 + offset] == 't') && (sql[11 + offset] == 'E'
                                        || sql[11 + offset] == 'e')) {
                                        return Token.KW_CURRENT_DATE;
                                    }
                                    if (sql[8 + offset] == 'T' || sql[8 + offset] == 't') {
                                        if (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') {
                                            if (sql[10 + offset] == 'M' || sql[10 + offset] == 'm') {
                                                if (sql[11 + offset] == 'E' || sql[11 + offset] == 'e') {
                                                    if (size == 12) {
                                                        return Token.KW_CURRENT_TIME;
                                                    }
                                                    if (size == 17 && (sql[12 + offset] == 'S'
                                                        || sql[12 + offset] == 's') && (sql[13 + offset] == 'T'
                                                        || sql[13 + offset] == 't') && (sql[14 + offset] == 'A'
                                                        || sql[14 + offset] == 'a') && (sql[15 + offset] == 'M'
                                                        || sql[15 + offset] == 'm') && (sql[16 + offset] == 'P'
                                                        || sql[16 + offset] == 'p')) {
                                                        return Token.KW_CURRENT_TIMESTAMP;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (size == 12 && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u') && (
                                        sql[9 + offset] == 'S' || sql[9 + offset] == 's') && (sql[10 + offset] == 'E'
                                        || sql[10 + offset] == 'e') && (sql[11 + offset] == 'R'
                                        || sql[11 + offset] == 'r')) {
                                        return Token.KW_CURRENT_USER;
                                    }
                                }
                            }
                        }
                    }
                }
                if (size == 6 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'O'
                    || sql[4 + offset] == 'o') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
                    return Token.KW_CURSOR;
                }
            }
        }
        return 0;
    }

    private static int parseD(byte[] sql, int offset, int size) {
        if (size < 3 || size > 15) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 8 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 8 || size > 9) {
                    return 0;
                }
                if (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') {
                    if (sql[4 + offset] == 'B' || sql[4 + offset] == 'b') {
                        if (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') {
                            if (sql[6 + offset] == 'S' || sql[6 + offset] == 's') {
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (size == 8) {
                                        return Token.KW_DATABASE;
                                    }
                                    if (size == 9 && (sql[8 + offset] == 'S' || sql[8 + offset] == 's')) {
                                        return Token.KW_DATABASES;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'Y' || sql[2 + offset] == 'y') {
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 8 && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u') && (
                        sql[7 + offset] == 'R' || sql[7 + offset] == 'r')) {
                        return Token.KW_DAY_HOUR;
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
                                return Token.KW_DAY_MICROSECOND;
                            }
                            if (size == 10 && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (
                                sql[7 + offset] == 'U' || sql[7 + offset] == 'u') && (sql[8 + offset] == 'T'
                                || sql[8 + offset] == 't') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                                return Token.KW_DAY_MINUTE;
                            }
                        }
                    }
                    if (size == 10 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e') && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c') && (
                        sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (sql[8 + offset] == 'N'
                        || sql[8 + offset] == 'n') && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')) {
                        return Token.KW_DAY_SECOND;
                    }
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 3 || size > 13) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (size < 3 || size > 7) {
                    return 0;
                }
                if (size == 3) {
                    return Token.KW_DEC;
                }
                if (size == 7 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'M'
                    || sql[4 + offset] == 'm') && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (
                    sql[6 + offset] == 'L' || sql[6 + offset] == 'l')) {
                    return Token.KW_DECIMAL;
                }
                if (size == 7 && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Token.KW_DECLARE;
                }
            }
            if (size == 7 && (sql[2 + offset] == 'F' || sql[2 + offset] == 'f') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (
                sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'T'
                || sql[6 + offset] == 't')) {
                return Token.KW_DEFAULT;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size < 6 || size > 7) {
                    return 0;
                }
                if (size == 7 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'Y'
                    || sql[4 + offset] == 'y') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (
                    sql[6 + offset] == 'D' || sql[6 + offset] == 'd')) {
                    return Token.KW_DELAYED;
                }
                if (size == 6 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'T'
                    || sql[4 + offset] == 't') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Token.KW_DELETE;
                }
            }
            if (size == 10 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == '_' || sql[5 + offset] == '_') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (sql[8 + offset] == 'N'
                || sql[8 + offset] == 'n') && (sql[9 + offset] == 'K' || sql[9 + offset] == 'k')) {
                return Token.KW_DENSE_RANK;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 4 || size > 8) {
                    return 0;
                }
                if (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') {
                    if (size == 4) {
                        return Token.KW_DESC;
                    }
                    if (size == 8 && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'B' || sql[6 + offset] == 'b') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Token.KW_DESCRIBE;
                    }
                }
            }
            if (size == 13 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'M' || sql[5 + offset] == 'm') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'I'
                || sql[8 + offset] == 'i') && (sql[9 + offset] == 'S' || sql[9 + offset] == 's') && (
                sql[10 + offset] == 'T' || sql[10 + offset] == 't') && (sql[11 + offset] == 'I'
                || sql[11 + offset] == 'i') && (sql[12 + offset] == 'C' || sql[12 + offset] == 'c')) {
                return Token.KW_DETERMINISTIC;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 3 || size > 11) {
                return 0;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 8 || size > 11) {
                    return 0;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (sql[6 + offset] == 'C' || sql[6 + offset] == 'c') {
                                if (sql[7 + offset] == 'T' || sql[7 + offset] == 't') {
                                    if (size == 8) {
                                        return Token.KW_DISTINCT;
                                    }
                                    if (size == 11 && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r') && (
                                        sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (sql[10 + offset] == 'W'
                                        || sql[10 + offset] == 'w')) {
                                        return Token.KW_DISTINCTROW;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (size == 3 && (sql[2 + offset] == 'V' || sql[2 + offset] == 'v')) {
                return Token.KW_DIV;
            }
        }
        if (size == 6 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') && (sql[2 + offset] == 'U'
            || sql[2 + offset] == 'u') && (sql[3 + offset] == 'B' || sql[3 + offset] == 'b') && (sql[4 + offset] == 'L'
            || sql[4 + offset] == 'l') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
            return Token.KW_DOUBLE;
        }
        if (size == 4 && (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') && (sql[2 + offset] == 'O'
            || sql[2 + offset] == 'o') && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p')) {
            return Token.KW_DROP;
        }
        if (size == 4 && (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')) {
            return Token.KW_DUAL;
        }
        return 0;
    }

    private static int parseE(byte[] sql, int offset, int size) {
        if (size < 4 || size > 8) {
            return 0;
        }
        if (size == 4 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c') && (sql[3 + offset] == 'H' || sql[3 + offset] == 'h')) {
            return Token.KW_EACH;
        }
        if (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') {
            if (size < 4 || size > 6) {
                return 0;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 4) {
                        return Token.KW_ELSE;
                    }
                    if (size == 6 && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'F'
                        || sql[5 + offset] == 'f')) {
                        return Token.KW_ELSEIF;
                    }
                }
            }
        }
        if (size == 5 && (sql[1 + offset] == 'M' || sql[1 + offset] == 'm') && (sql[2 + offset] == 'P'
            || sql[2 + offset] == 'p') && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'Y'
            || sql[4 + offset] == 'y')) {
            return Token.KW_EMPTY;
        }
        if (size == 8 && (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c') && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'O'
            || sql[4 + offset] == 'o') && (sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E'
            || sql[6 + offset] == 'e') && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')) {
            return Token.KW_ENCLOSED;
        }
        if (size == 7 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c') && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'P'
            || sql[4 + offset] == 'p') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'D'
            || sql[6 + offset] == 'd')) {
            return Token.KW_ESCAPED;
        }
        if (sql[1 + offset] == 'X' || sql[1 + offset] == 'x') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                return Token.KW_EXCEPT;
            }
            if (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') {
                if (size == 6 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'T'
                    || sql[4 + offset] == 't') && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                    return Token.KW_EXISTS;
                }
                if (size == 4 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't')) {
                    return Token.KW_EXIT;
                }
            }
            if (size == 7 && (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N'
                || sql[6 + offset] == 'n')) {
                return Token.KW_EXPLAIN;
            }
        }
        return 0;
    }

    private static int parseF(byte[] sql, int offset, int size) {
        if (size < 3 || size > 11) {
            return 0;
        }
        if (size == 5 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (sql[2 + offset] == 'L'
            || sql[2 + offset] == 'l') && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e')) {
            return Token.LITERAL_BOOL_FALSE;
        }
        if (size == 5 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'T'
            || sql[2 + offset] == 't') && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'H'
            || sql[4 + offset] == 'h')) {
            return Token.KW_FETCH;
        }
        if (size == 11 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'T'
            || sql[4 + offset] == 't') && (sql[5 + offset] == '_' || sql[5 + offset] == '_') && (sql[6 + offset] == 'V'
            || sql[6 + offset] == 'v') && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (sql[8 + offset] == 'L'
            || sql[8 + offset] == 'l') && (sql[9 + offset] == 'U' || sql[9 + offset] == 'u') && (sql[10 + offset] == 'E'
            || sql[10 + offset] == 'e')) {
            return Token.KW_FIRST_VALUE;
        }
        if (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') {
            if (size < 5 || size > 6) {
                return 0;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') {
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (size == 5) {
                            return Token.KW_FLOAT;
                        }
                        if (size == 6 && (sql[5 + offset] == '4' || sql[5 + offset] == '4')) {
                            return Token.KW_FLOAT4;
                        }
                        if (size == 6 && (sql[5 + offset] == '8' || sql[5 + offset] == '8')) {
                            return Token.KW_FLOAT8;
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 3 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size == 3) {
                    return Token.KW_FOR;
                }
                if (size == 5 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e')) {
                    return Token.KW_FORCE;
                }
                if (size == 7 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'G' || sql[5 + offset] == 'g') && (
                    sql[6 + offset] == 'N' || sql[6 + offset] == 'n')) {
                    return Token.KW_FOREIGN;
                }
            }
        }
        if (size == 4 && (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') && (sql[2 + offset] == 'O'
            || sql[2 + offset] == 'o') && (sql[3 + offset] == 'M' || sql[3 + offset] == 'm')) {
            return Token.KW_FROM;
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size != 8) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'X' || sql[6 + offset] == 'x')
                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
                return Token.KW_FULLTEXT;
            }
            if (size == 8 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')) {
                return Token.KW_FUNCTION;
            }
        }
        return 0;
    }

    private static int parseG(byte[] sql, int offset, int size) {
        if (size < 3 || size > 9) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size == 9 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'D'
                || sql[8 + offset] == 'd')) {
                return Token.KW_GENERATED;
            }
            if (size == 3 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')) {
                return Token.KW_GET;
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 5 || size > 8) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
                return Token.KW_GRANT;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') {
                    if (sql[4 + offset] == 'P' || sql[4 + offset] == 'p') {
                        if (size == 5) {
                            return Token.KW_GROUP;
                        }
                        if (size == 8 && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N'
                            || sql[6 + offset] == 'n') && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g')) {
                            return Token.KW_GROUPING;
                        }
                        if (size == 6 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                            return Token.KW_GROUPS;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseH(byte[] sql, int offset, int size) {
        if (size < 6 || size > 16) {
            return 0;
        }
        if (size == 6 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (sql[2 + offset] == 'V'
            || sql[2 + offset] == 'v') && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'N'
            || sql[4 + offset] == 'n') && (sql[5 + offset] == 'G' || sql[5 + offset] == 'g')) {
            return Token.KW_HAVING;
        }
        if (size == 13 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'G'
            || sql[2 + offset] == 'g') && (sql[3 + offset] == 'H' || sql[3 + offset] == 'h') && (sql[4 + offset] == '_'
            || sql[4 + offset] == '_') && (sql[5 + offset] == 'P' || sql[5 + offset] == 'p') && (sql[6 + offset] == 'R'
            || sql[6 + offset] == 'r') && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'O'
            || sql[8 + offset] == 'o') && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r') && (sql[10 + offset] == 'I'
            || sql[10 + offset] == 'i') && (sql[11 + offset] == 'T' || sql[11 + offset] == 't') && (
            sql[12 + offset] == 'Y' || sql[12 + offset] == 'y')) {
            return Token.KW_HIGH_PRIORITY;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 11 || size > 16) {
                return 0;
            }
            if (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') {
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (sql[4 + offset] == '_' || sql[4 + offset] == '_') {
                        if (sql[5 + offset] == 'M' || sql[5 + offset] == 'm') {
                            if (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') {
                                if (size == 16 && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (
                                    sql[8 + offset] == 'R' || sql[8 + offset] == 'r') && (sql[9 + offset] == 'O'
                                    || sql[9 + offset] == 'o') && (sql[10 + offset] == 'S' || sql[10 + offset] == 's')
                                    && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e') && (sql[12 + offset] == 'C'
                                    || sql[12 + offset] == 'c') && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                                    && (sql[14 + offset] == 'N' || sql[14 + offset] == 'n') && (sql[15 + offset] == 'D'
                                    || sql[15 + offset] == 'd')) {
                                    return Token.KW_HOUR_MICROSECOND;
                                }
                                if (size == 11 && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (
                                    sql[8 + offset] == 'U' || sql[8 + offset] == 'u') && (sql[9 + offset] == 'T'
                                    || sql[9 + offset] == 't') && (sql[10 + offset] == 'E'
                                    || sql[10 + offset] == 'e')) {
                                    return Token.KW_HOUR_MINUTE;
                                }
                            }
                        }
                        if (size == 11 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E'
                            || sql[6 + offset] == 'e') && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (
                            sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N'
                            || sql[9 + offset] == 'n') && (sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                            return Token.KW_HOUR_SECOND;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseI(byte[] sql, int offset, int size) {
        if (size < 2 || size > 15) {
            return 0;
        }
        if (size == 2 && (sql[1 + offset] == 'F' || sql[1 + offset] == 'f')) {
            return Token.KW_IF;
        }
        if (size == 6 && (sql[1 + offset] == 'G' || sql[1 + offset] == 'g') && (sql[2 + offset] == 'N'
            || sql[2 + offset] == 'n') && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'R'
            || sql[4 + offset] == 'r') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
            return Token.KW_IGNORE;
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 2 || size > 11) {
                return 0;
            }
            if (size == 2) {
                return Token.KW_IN;
            }
            if (size == 5 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'X' || sql[4 + offset] == 'x')) {
                return Token.KW_INDEX;
            }
            if (size == 6 && (sql[2 + offset] == 'F' || sql[2 + offset] == 'f') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Token.KW_INFILE;
            }
            if (size == 5 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
                return Token.KW_INNER;
            }
            if (size == 5 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'U'
                || sql[3 + offset] == 'u') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
                return Token.KW_INOUT;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 6 || size > 11) {
                    return 0;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 11 && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (sql[5 + offset] == 'S'
                        || sql[5 + offset] == 's') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') && (
                        sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == 'I'
                        || sql[8 + offset] == 'i') && (sql[9 + offset] == 'V' || sql[9 + offset] == 'v') && (
                        sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                        return Token.KW_INSENSITIVE;
                    }
                    if (size == 6 && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (sql[5 + offset] == 'T'
                        || sql[5 + offset] == 't')) {
                        return Token.KW_INSERT;
                    }
                }
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 3 || size > 8) {
                    return 0;
                }
                if (size == 3) {
                    return Token.KW_INT;
                }
                if (size == 4 && (sql[3 + offset] == '1' || sql[3 + offset] == '1')) {
                    return Token.KW_INT1;
                }
                if (size == 4 && (sql[3 + offset] == '2' || sql[3 + offset] == '2')) {
                    return Token.KW_INT2;
                }
                if (size == 4 && (sql[3 + offset] == '3' || sql[3 + offset] == '3')) {
                    return Token.KW_INT3;
                }
                if (size == 4 && (sql[3 + offset] == '4' || sql[3 + offset] == '4')) {
                    return Token.KW_INT4;
                }
                if (size == 4 && (sql[3 + offset] == '8' || sql[3 + offset] == '8')) {
                    return Token.KW_INT8;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size < 7 || size > 8) {
                        return 0;
                    }
                    if (size == 7 && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')) {
                        return Token.KW_INTEGER;
                    }
                    if (size == 8 && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (sql[5 + offset] == 'V'
                        || sql[5 + offset] == 'v') && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (
                        sql[7 + offset] == 'L' || sql[7 + offset] == 'l')) {
                        return Token.KW_INTERVAL;
                    }
                }
                if (size == 4 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o')) {
                    return Token.KW_INTO;
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 14 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == '_' || sql[2 + offset] == '_') {
                if (size == 14 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'F'
                    || sql[4 + offset] == 'f') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e') && (sql[7 + offset] == 'R'
                    || sql[7 + offset] == 'r') && (sql[8 + offset] == '_' || sql[8 + offset] == '_') && (
                    sql[9 + offset] == 'G' || sql[9 + offset] == 'g') && (sql[10 + offset] == 'T'
                    || sql[10 + offset] == 't') && (sql[11 + offset] == 'I' || sql[11 + offset] == 'i') && (
                    sql[12 + offset] == 'D' || sql[12 + offset] == 'd') && (sql[13 + offset] == 'S'
                    || sql[13 + offset] == 's')) {
                    return Token.KW_IO_AFTER_GTIDS;
                }
                if (size == 15 && (sql[3 + offset] == 'B' || sql[3 + offset] == 'b') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'F' || sql[5 + offset] == 'f') && (
                    sql[6 + offset] == 'O' || sql[6 + offset] == 'o') && (sql[7 + offset] == 'R'
                    || sql[7 + offset] == 'r') && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (
                    sql[9 + offset] == '_' || sql[9 + offset] == '_') && (sql[10 + offset] == 'G'
                    || sql[10 + offset] == 'g') && (sql[11 + offset] == 'T' || sql[11 + offset] == 't') && (
                    sql[12 + offset] == 'I' || sql[12 + offset] == 'i') && (sql[13 + offset] == 'D'
                    || sql[13 + offset] == 'd') && (sql[14 + offset] == 'S' || sql[14 + offset] == 's')) {
                    return Token.KW_IO_BEFORE_GTIDS;
                }
            }
        }
        if (size == 2 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's')) {
            return Token.KW_IS;
        }
        if (size == 7 && (sql[1 + offset] == 'T' || sql[1 + offset] == 't') && (sql[2 + offset] == 'E'
            || sql[2 + offset] == 'e') && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'A'
            || sql[4 + offset] == 'a') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'E'
            || sql[6 + offset] == 'e')) {
            return Token.KW_ITERATE;
        }
        return 0;
    }

    private static int parseJ(byte[] sql, int offset, int size) {
        if (size < 4 || size > 10) {
            return 0;
        }
        if (size == 4 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') && (sql[2 + offset] == 'I'
            || sql[2 + offset] == 'i') && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')) {
            return Token.KW_JOIN;
        }
        if (size == 10 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's') && (sql[2 + offset] == 'O'
            || sql[2 + offset] == 'o') && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == '_'
            || sql[4 + offset] == '_') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'A'
            || sql[6 + offset] == 'a') && (sql[7 + offset] == 'B' || sql[7 + offset] == 'b') && (sql[8 + offset] == 'L'
            || sql[8 + offset] == 'l') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
            return Token.KW_JSON_TABLE;
        }
        return 0;
    }

    private static int parseK(byte[] sql, int offset, int size) {
        if (size < 3 || size > 4) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (sql[2 + offset] == 'Y' || sql[2 + offset] == 'y') {
                if (size == 3) {
                    return Token.KW_KEY;
                }
                if (size == 4 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')) {
                    return Token.KW_KEYS;
                }
            }
        }
        if (size == 4 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'L'
            || sql[2 + offset] == 'l') && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')) {
            return Token.KW_KILL;
        }
        return 0;
    }

    private static int parseL(byte[] sql, int offset, int size) {
        if (size < 3 || size > 14) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 3 || size > 10) {
                return 0;
            }
            if (size == 3 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g')) {
                return Token.KW_LAG;
            }
            if (size == 10 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (
                sql[5 + offset] == 'V' || sql[5 + offset] == 'v') && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (sql[8 + offset] == 'U'
                || sql[8 + offset] == 'u') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                return Token.KW_LAST_VALUE;
            }
            if (size == 7 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L'
                || sql[6 + offset] == 'l')) {
                return Token.KW_LATERAL;
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') {
                    if (size == 4) {
                        return Token.KW_LEAD;
                    }
                    if (size == 7 && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N'
                        || sql[5 + offset] == 'n') && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g')) {
                        return Token.KW_LEADING;
                    }
                }
                if (size == 5 && (sql[3 + offset] == 'V' || sql[3 + offset] == 'v') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e')) {
                    return Token.KW_LEAVE;
                }
            }
            if (size == 4 && (sql[2 + offset] == 'F' || sql[2 + offset] == 'f') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't')) {
                return Token.KW_LEFT;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 4 || size > 6) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'K' || sql[2 + offset] == 'k') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e')) {
                return Token.KW_LIKE;
            }
            if (size == 5 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
                return Token.KW_LIMIT;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 5 || size > 6) {
                    return 0;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 6 && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (sql[5 + offset] == 'R'
                        || sql[5 + offset] == 'r')) {
                        return Token.KW_LINEAR;
                    }
                    if (size == 5 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                        return Token.KW_LINES;
                    }
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 14) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'D'
                || sql[3 + offset] == 'd')) {
                return Token.KW_LOAD;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') {
                    if (size < 9 || size > 14) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') {
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') {
                                if (sql[7 + offset] == 'M' || sql[7 + offset] == 'm') {
                                    if (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') {
                                        if (size == 9) {
                                            return Token.KW_LOCALTIME;
                                        }
                                        if (size == 14 && (sql[9 + offset] == 'S' || sql[9 + offset] == 's') && (
                                            sql[10 + offset] == 'T' || sql[10 + offset] == 't') && (
                                            sql[11 + offset] == 'A' || sql[11 + offset] == 'a') && (
                                            sql[12 + offset] == 'M' || sql[12 + offset] == 'm') && (
                                            sql[13 + offset] == 'P' || sql[13 + offset] == 'p')) {
                                            return Token.KW_LOCALTIMESTAMP;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (size == 4 && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k')) {
                    return Token.KW_LOCK;
                }
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 4 || size > 8) {
                    return 0;
                }
                if (sql[3 + offset] == 'G' || sql[3 + offset] == 'g') {
                    if (size == 4) {
                        return Token.KW_LONG;
                    }
                    if (size == 8 && (sql[4 + offset] == 'B' || sql[4 + offset] == 'b') && (sql[5 + offset] == 'L'
                        || sql[5 + offset] == 'l') && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o') && (
                        sql[7 + offset] == 'B' || sql[7 + offset] == 'b')) {
                        return Token.KW_LONGBLOB;
                    }
                    if (size == 8 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e') && (sql[6 + offset] == 'X' || sql[6 + offset] == 'x') && (
                        sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
                        return Token.KW_LONGTEXT;
                    }
                }
            }
            if (size == 4 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'P'
                || sql[3 + offset] == 'p')) {
                return Token.KW_LOOP;
            }
            if (size == 12 && (sql[2 + offset] == 'W' || sql[2 + offset] == 'w') && (sql[3 + offset] == '_'
                || sql[3 + offset] == '_') && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p') && (
                sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (sql[8 + offset] == 'R'
                || sql[8 + offset] == 'r') && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (
                sql[10 + offset] == 'T' || sql[10 + offset] == 't') && (sql[11 + offset] == 'Y'
                || sql[11 + offset] == 'y')) {
                return Token.KW_LOW_PRIORITY;
            }
        }
        return 0;
    }

    private static int parseM(byte[] sql, int offset, int size) {
        if (size < 3 || size > 29) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 5 || size > 29) {
                return 0;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 11 || size > 29) {
                    return 0;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') {
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (size == 11 && (sql[7 + offset] == 'B' || sql[7 + offset] == 'b') && (
                                    sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (sql[9 + offset] == 'N'
                                    || sql[9 + offset] == 'n') && (sql[10 + offset] == 'D'
                                    || sql[10 + offset] == 'd')) {
                                    return Token.KW_MASTER_BIND;
                                }
                                if (size == 29 && (sql[7 + offset] == 'S' || sql[7 + offset] == 's') && (
                                    sql[8 + offset] == 'S' || sql[8 + offset] == 's') && (sql[9 + offset] == 'L'
                                    || sql[9 + offset] == 'l') && (sql[10 + offset] == '_' || sql[10 + offset] == '_')
                                    && (sql[11 + offset] == 'V' || sql[11 + offset] == 'v') && (sql[12 + offset] == 'E'
                                    || sql[12 + offset] == 'e') && (sql[13 + offset] == 'R' || sql[13 + offset] == 'r')
                                    && (sql[14 + offset] == 'I' || sql[14 + offset] == 'i') && (sql[15 + offset] == 'F'
                                    || sql[15 + offset] == 'f') && (sql[16 + offset] == 'Y' || sql[16 + offset] == 'y')
                                    && (sql[17 + offset] == '_' || sql[17 + offset] == '_') && (sql[18 + offset] == 'S'
                                    || sql[18 + offset] == 's') && (sql[19 + offset] == 'E' || sql[19 + offset] == 'e')
                                    && (sql[20 + offset] == 'R' || sql[20 + offset] == 'r') && (sql[21 + offset] == 'V'
                                    || sql[21 + offset] == 'v') && (sql[22 + offset] == 'E' || sql[22 + offset] == 'e')
                                    && (sql[23 + offset] == 'R' || sql[23 + offset] == 'r') && (sql[24 + offset] == '_'
                                    || sql[24 + offset] == '_') && (sql[25 + offset] == 'C' || sql[25 + offset] == 'c')
                                    && (sql[26 + offset] == 'E' || sql[26 + offset] == 'e') && (sql[27 + offset] == 'R'
                                    || sql[27 + offset] == 'r') && (sql[28 + offset] == 'T'
                                    || sql[28 + offset] == 't')) {
                                    return Token.KW_MASTER_SSL_VERIFY_SERVER_CERT;
                                }
                            }
                        }
                    }
                }
            }
            if (size == 5 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h')) {
                return Token.KW_MATCH;
            }
            if (size == 8 && (sql[2 + offset] == 'X' || sql[2 + offset] == 'x') && (sql[3 + offset] == 'V'
                || sql[3 + offset] == 'v') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                return Token.KW_MAXVALUE;
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 9 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') {
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') {
                        if (sql[5 + offset] == 'M' || sql[5 + offset] == 'm') {
                            if (size == 10 && (sql[6 + offset] == 'B' || sql[6 + offset] == 'b') && (
                                sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (sql[8 + offset] == 'O'
                                || sql[8 + offset] == 'o') && (sql[9 + offset] == 'B' || sql[9 + offset] == 'b')) {
                                return Token.KW_MEDIUMBLOB;
                            }
                            if (size == 9 && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') && (
                                sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'T'
                                || sql[8 + offset] == 't')) {
                                return Token.KW_MEDIUMINT;
                            }
                            if (size == 10 && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (
                                sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'X'
                                || sql[8 + offset] == 'x') && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                                return Token.KW_MEDIUMTEXT;
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 9 || size > 18) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'D'
                || sql[3 + offset] == 'd') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't')) {
                return Token.KW_MIDDLEINT;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 13 || size > 18) {
                    return 0;
                }
                if (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') {
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') {
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (size == 18 && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm') && (
                                    sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (sql[9 + offset] == 'C'
                                    || sql[9 + offset] == 'c') && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')
                                    && (sql[11 + offset] == 'O' || sql[11 + offset] == 'o') && (sql[12 + offset] == 'S'
                                    || sql[12 + offset] == 's') && (sql[13 + offset] == 'E' || sql[13 + offset] == 'e')
                                    && (sql[14 + offset] == 'C' || sql[14 + offset] == 'c') && (sql[15 + offset] == 'O'
                                    || sql[15 + offset] == 'o') && (sql[16 + offset] == 'N' || sql[16 + offset] == 'n')
                                    && (sql[17 + offset] == 'D' || sql[17 + offset] == 'd')) {
                                    return Token.KW_MINUTE_MICROSECOND;
                                }
                                if (size == 13 && (sql[7 + offset] == 'S' || sql[7 + offset] == 's') && (
                                    sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (sql[9 + offset] == 'C'
                                    || sql[9 + offset] == 'c') && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                                    && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n') && (sql[12 + offset] == 'D'
                                    || sql[12 + offset] == 'd')) {
                                    return Token.KW_MINUTE_SECOND;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 3 || size > 8) {
                return 0;
            }
            if (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') {
                if (size == 3) {
                    return Token.KW_MOD;
                }
                if (size == 8 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'F'
                    || sql[4 + offset] == 'f') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e') && (sql[7 + offset] == 'S'
                    || sql[7 + offset] == 's')) {
                    return Token.KW_MODIFIES;
                }
            }
        }
        return 0;
    }

    private static int parseN(byte[] sql, int offset, int size) {
        if (size < 3 || size > 18) {
            return 0;
        }
        if (size == 7 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (sql[2 + offset] == 'T'
            || sql[2 + offset] == 't') && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') && (sql[4 + offset] == 'R'
            || sql[4 + offset] == 'r') && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L'
            || sql[6 + offset] == 'l')) {
            return Token.KW_NATURAL;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size == 3 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')) {
                return Token.KW_NOT;
            }
            if (size == 18 && (sql[2 + offset] == '_' || sql[2 + offset] == '_') && (sql[3 + offset] == 'W'
                || sql[3 + offset] == 'w') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == '_'
                || sql[8 + offset] == '_') && (sql[9 + offset] == 'T' || sql[9 + offset] == 't') && (
                sql[10 + offset] == 'O' || sql[10 + offset] == 'o') && (sql[11 + offset] == '_'
                || sql[11 + offset] == '_') && (sql[12 + offset] == 'B' || sql[12 + offset] == 'b') && (
                sql[13 + offset] == 'I' || sql[13 + offset] == 'i') && (sql[14 + offset] == 'N'
                || sql[14 + offset] == 'n') && (sql[15 + offset] == 'L' || sql[15 + offset] == 'l') && (
                sql[16 + offset] == 'O' || sql[16 + offset] == 'o') && (sql[17 + offset] == 'G'
                || sql[17 + offset] == 'g')) {
                return Token.KW_NO_WRITE_TO_BINLOG;
            }
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (size < 5 || size > 9) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'H' || sql[2 + offset] == 'h') && (sql[3 + offset] == '_'
                || sql[3 + offset] == '_') && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                && (sql[7 + offset] == 'U' || sql[7 + offset] == 'u') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e')) {
                return Token.KW_NTH_VALUE;
            }
            if (size == 5 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Token.KW_NTILE;
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l')) {
                return Token.LITERAL_NULL;
            }
            if (size == 7 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'C'
                || sql[6 + offset] == 'c')) {
                return Token.KW_NUMERIC;
            }
        }
        return 0;
    }

    private static int parseO(byte[] sql, int offset, int size) {
        if (size < 2 || size > 15) {
            return 0;
        }
        if (size == 2 && (sql[1 + offset] == 'F' || sql[1 + offset] == 'f')) {
            return Token.KW_OF;
        }
        if (size == 2 && (sql[1 + offset] == 'N' || sql[1 + offset] == 'n')) {
            return Token.KW_ON;
        }
        if (sql[1 + offset] == 'P' || sql[1 + offset] == 'p') {
            if (size < 6 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') {
                        if (size < 8 || size > 15) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') {
                            if (sql[6 + offset] == 'Z' || sql[6 + offset] == 'z') {
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (size == 8) {
                                        return Token.KW_OPTIMIZE;
                                    }
                                    if (size == 15 && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r') && (
                                        sql[9 + offset] == '_' || sql[9 + offset] == '_') && (sql[10 + offset] == 'C'
                                        || sql[10 + offset] == 'c') && (sql[11 + offset] == 'O'
                                        || sql[11 + offset] == 'o') && (sql[12 + offset] == 'S'
                                        || sql[12 + offset] == 's') && (sql[13 + offset] == 'T'
                                        || sql[13 + offset] == 't') && (sql[14 + offset] == 'S'
                                        || sql[14 + offset] == 's')) {
                                        return Token.KW_OPTIMIZER_COSTS;
                                    }
                                }
                            }
                        }
                    }
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (size < 6 || size > 10) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (size == 6) {
                                return Token.KW_OPTION;
                            }
                            if (size == 10 && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (
                                sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (sql[8 + offset] == 'L'
                                || sql[8 + offset] == 'l') && (sql[9 + offset] == 'Y' || sql[9 + offset] == 'y')) {
                                return Token.KW_OPTIONALLY;
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 2 || size > 5) {
                return 0;
            }
            if (size == 2) {
                return Token.KW_OR;
            }
            if (size == 5 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
                return Token.KW_ORDER;
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 3 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size == 3) {
                    return Token.KW_OUT;
                }
                if (size == 5 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r')) {
                    return Token.KW_OUTER;
                }
                if (size == 7 && (sql[3 + offset] == 'F' || sql[3 + offset] == 'f') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Token.KW_OUTFILE;
                }
            }
        }
        if (size == 4 && (sql[1 + offset] == 'V' || sql[1 + offset] == 'v') && (sql[2 + offset] == 'E'
            || sql[2 + offset] == 'e') && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')) {
            return Token.KW_OVER;
        }
        return 0;
    }

    private static int parseP(byte[] sql, int offset, int size) {
        if (size < 5 || size > 12) {
            return 0;
        }
        if (size == 9 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'I'
            || sql[4 + offset] == 'i') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'I'
            || sql[6 + offset] == 'i') && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (sql[8 + offset] == 'N'
            || sql[8 + offset] == 'n')) {
            return Token.KW_PARTITION;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 7 || size > 12) {
                return 0;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size == 12 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (sql[7 + offset] == '_'
                    || sql[7 + offset] == '_') && (sql[8 + offset] == 'R' || sql[8 + offset] == 'r') && (
                    sql[9 + offset] == 'A' || sql[9 + offset] == 'a') && (sql[10 + offset] == 'N'
                    || sql[10 + offset] == 'n') && (sql[11 + offset] == 'K' || sql[11 + offset] == 'k')) {
                    return Token.KW_PERCENT_RANK;
                }
                if (sql[3 + offset] == 'S' || sql[3 + offset] == 's') {
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (sql[5 + offset] == 'S' || sql[5 + offset] == 's') {
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (size == 7) {
                                    return Token.KW_PERSIST;
                                }
                                if (size == 12 && (sql[7 + offset] == '_' || sql[7 + offset] == '_') && (
                                    sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N'
                                    || sql[9 + offset] == 'n') && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l')
                                    && (sql[11 + offset] == 'Y' || sql[11 + offset] == 'y')) {
                                    return Token.KW_PERSIST_ONLY;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 7 || size > 9) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (sql[8 + offset] == 'N'
                || sql[8 + offset] == 'n')) {
                return Token.KW_PRECISION;
            }
            if (size == 7 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'M'
                || sql[3 + offset] == 'm') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (sql[6 + offset] == 'Y'
                || sql[6 + offset] == 'y')) {
                return Token.KW_PRIMARY;
            }
            if (size == 9 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u')
                && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e')) {
                return Token.KW_PROCEDURE;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'G' || sql[3 + offset] == 'g') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e')) {
            return Token.KW_PURGE;
        }
        return 0;
    }

    private static int parseR(byte[] sql, int offset, int size) {
        if (size < 3 || size > 10) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 5) {
                return 0;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size == 5 && (sql[3 + offset] == 'G' || sql[3 + offset] == 'g') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e')) {
                    return Token.KW_RANGE;
                }
                if (size == 4 && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k')) {
                    return Token.KW_RANK;
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 4 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (sql[3 + offset] == 'D' || sql[3 + offset] == 'd') {
                    if (size == 4) {
                        return Token.KW_READ;
                    }
                    if (size == 5 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                        return Token.KW_READS;
                    }
                    if (size == 10 && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (sql[5 + offset] == 'W'
                        || sql[5 + offset] == 'w') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r') && (
                        sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'T'
                        || sql[8 + offset] == 't') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                        return Token.KW_READ_WRITE;
                    }
                }
                if (size == 4 && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')) {
                    return Token.KW_REAL;
                }
            }
            if (size == 9 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'U'
                || sql[3 + offset] == 'u') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'V' || sql[7 + offset] == 'v') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e')) {
                return Token.KW_RECURSIVE;
            }
            if (size == 10 && (sql[2 + offset] == 'F' || sql[2 + offset] == 'f') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e') && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')) {
                return Token.KW_REFERENCES;
            }
            if (size == 6 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'X' || sql[4 + offset] == 'x') && (
                sql[5 + offset] == 'P' || sql[5 + offset] == 'p')) {
                return Token.KW_REGEXP;
            }
            if (size == 7 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Token.KW_RELEASE;
            }
            if (size == 6 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Token.KW_RENAME;
            }
            if (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') {
                if (size < 6 || size > 7) {
                    return 0;
                }
                if (size == 6 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                    return Token.KW_REPEAT;
                }
                if (size == 7 && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Token.KW_REPLACE;
                }
            }
            if (size == 7 && (sql[2 + offset] == 'Q' || sql[2 + offset] == 'q') && (sql[3 + offset] == 'U'
                || sql[3 + offset] == 'u') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Token.KW_REQUIRE;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size != 8) {
                    return 0;
                }
                if (size == 8 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'G'
                    || sql[4 + offset] == 'g') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (
                    sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (sql[7 + offset] == 'L'
                    || sql[7 + offset] == 'l')) {
                    return Token.KW_RESIGNAL;
                }
                if (size == 8 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (
                    sql[6 + offset] == 'C' || sql[6 + offset] == 'c') && (sql[7 + offset] == 'T'
                    || sql[7 + offset] == 't')) {
                    return Token.KW_RESTRICT;
                }
            }
            if (size == 6 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'U'
                || sql[3 + offset] == 'u') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'N' || sql[5 + offset] == 'n')) {
                return Token.KW_RETURN;
            }
            if (size == 6 && (sql[2 + offset] == 'V' || sql[2 + offset] == 'v') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'K' || sql[4 + offset] == 'k') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Token.KW_REVOKE;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'G'
            || sql[2 + offset] == 'g') && (sql[3 + offset] == 'H' || sql[3 + offset] == 'h') && (sql[4 + offset] == 'T'
            || sql[4 + offset] == 't')) {
            return Token.KW_RIGHT;
        }
        if (size == 5 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') && (sql[2 + offset] == 'I'
            || sql[2 + offset] == 'i') && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e')) {
            return Token.KW_RLIKE;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (sql[2 + offset] == 'W' || sql[2 + offset] == 'w') {
                if (size == 3) {
                    return Token.KW_ROW;
                }
                if (size == 4 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')) {
                    return Token.KW_ROWS;
                }
                if (size == 10 && (sql[3 + offset] == '_' || sql[3 + offset] == '_') && (sql[4 + offset] == 'N'
                    || sql[4 + offset] == 'n') && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u') && (
                    sql[6 + offset] == 'M' || sql[6 + offset] == 'm') && (sql[7 + offset] == 'B'
                    || sql[7 + offset] == 'b') && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (
                    sql[9 + offset] == 'R' || sql[9 + offset] == 'r')) {
                    return Token.KW_ROW_NUMBER;
                }
            }
        }
        return 0;
    }

    private static int parseS(byte[] sql, int offset, int size) {
        if (size < 3 || size > 19) {
            return 0;
        }
        if (sql[1 + offset] == 'C' || sql[1 + offset] == 'c') {
            if (size < 6 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'H' || sql[2 + offset] == 'h') {
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') {
                        if (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') {
                            if (size == 6) {
                                return Token.KW_SCHEMA;
                            }
                            if (size == 7 && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                                return Token.KW_SCHEMAS;
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 3 || size > 18) {
                return 0;
            }
            if (size == 18 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (
                sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm') && (sql[8 + offset] == 'I'
                || sql[8 + offset] == 'i') && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') && (
                sql[10 + offset] == 'R' || sql[10 + offset] == 'r') && (sql[11 + offset] == 'O'
                || sql[11 + offset] == 'o') && (sql[12 + offset] == 'S' || sql[12 + offset] == 's') && (
                sql[13 + offset] == 'E' || sql[13 + offset] == 'e') && (sql[14 + offset] == 'C'
                || sql[14 + offset] == 'c') && (sql[15 + offset] == 'O' || sql[15 + offset] == 'o') && (
                sql[16 + offset] == 'N' || sql[16 + offset] == 'n') && (sql[17 + offset] == 'D'
                || sql[17 + offset] == 'd')) {
                return Token.KW_SECOND_MICROSECOND;
            }
            if (size == 6 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                return Token.KW_SELECT;
            }
            if (size == 9 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'V' || sql[7 + offset] == 'v') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e')) {
                return Token.KW_SENSITIVE;
            }
            if (size == 9 && (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (sql[8 + offset] == 'R'
                || sql[8 + offset] == 'r')) {
                return Token.KW_SEPARATOR;
            }
            if (size == 3 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')) {
                return Token.KW_SET;
            }
        }
        if (size == 4 && (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') && (sql[2 + offset] == 'O'
            || sql[2 + offset] == 'o') && (sql[3 + offset] == 'W' || sql[3 + offset] == 'w')) {
            return Token.KW_SHOW;
        }
        if (size == 6 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'G'
            || sql[2 + offset] == 'g') && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == 'A'
            || sql[4 + offset] == 'a') && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')) {
            return Token.KW_SIGNAL;
        }
        if (size == 8 && (sql[1 + offset] == 'M' || sql[1 + offset] == 'm') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'L'
            || sql[4 + offset] == 'l') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N'
            || sql[6 + offset] == 'n') && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
            return Token.KW_SMALLINT;
        }
        if (sql[1 + offset] == 'P' || sql[1 + offset] == 'p') {
            if (size < 7 || size > 8) {
                return 0;
            }
            if (size == 7 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L'
                || sql[6 + offset] == 'l')) {
                return Token.KW_SPATIAL;
            }
            if (size == 8 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'F' || sql[5 + offset] == 'f') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c')) {
                return Token.KW_SPECIFIC;
            }
        }
        if (sql[1 + offset] == 'Q' || sql[1 + offset] == 'q') {
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size == 3) {
                    return Token.KW_SQL;
                }
                if (size == 12 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'X'
                    || sql[4 + offset] == 'x') && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e') && (sql[7 + offset] == 'P'
                    || sql[7 + offset] == 'p') && (sql[8 + offset] == 'T' || sql[8 + offset] == 't') && (
                    sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (sql[10 + offset] == 'O'
                    || sql[10 + offset] == 'o') && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')) {
                    return Token.KW_SQLEXCEPTION;
                }
                if (size == 8 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'T'
                    || sql[4 + offset] == 't') && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (sql[7 + offset] == 'E'
                    || sql[7 + offset] == 'e')) {
                    return Token.KW_SQLSTATE;
                }
                if (size == 10 && (sql[3 + offset] == 'W' || sql[3 + offset] == 'w') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (sql[7 + offset] == 'I'
                    || sql[7 + offset] == 'i') && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n') && (
                    sql[9 + offset] == 'G' || sql[9 + offset] == 'g')) {
                    return Token.KW_SQLWARNING;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size < 14 || size > 19) {
                        return 0;
                    }
                    if (size == 14 && (sql[4 + offset] == 'B' || sql[4 + offset] == 'b') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g') && (
                        sql[7 + offset] == '_' || sql[7 + offset] == '_') && (sql[8 + offset] == 'R'
                        || sql[8 + offset] == 'r') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (
                        sql[10 + offset] == 'S' || sql[10 + offset] == 's') && (sql[11 + offset] == 'U'
                        || sql[11 + offset] == 'u') && (sql[12 + offset] == 'L' || sql[12 + offset] == 'l') && (
                        sql[13 + offset] == 'T' || sql[13 + offset] == 't')) {
                        return Token.KW_SQL_BIG_RESULT;
                    }
                    if (size == 19 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (sql[5 + offset] == 'A'
                        || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l') && (
                        sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (sql[8 + offset] == '_'
                        || sql[8 + offset] == '_') && (sql[9 + offset] == 'F' || sql[9 + offset] == 'f') && (
                        sql[10 + offset] == 'O' || sql[10 + offset] == 'o') && (sql[11 + offset] == 'U'
                        || sql[11 + offset] == 'u') && (sql[12 + offset] == 'N' || sql[12 + offset] == 'n') && (
                        sql[13 + offset] == 'D' || sql[13 + offset] == 'd') && (sql[14 + offset] == '_'
                        || sql[14 + offset] == '_') && (sql[15 + offset] == 'R' || sql[15 + offset] == 'r') && (
                        sql[16 + offset] == 'O' || sql[16 + offset] == 'o') && (sql[17 + offset] == 'W'
                        || sql[17 + offset] == 'w') && (sql[18 + offset] == 'S' || sql[18 + offset] == 's')) {
                        return Token.KW_SQL_CALC_FOUND_ROWS;
                    }
                    if (size == 16 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (sql[5 + offset] == 'M'
                        || sql[5 + offset] == 'm') && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (
                        sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (sql[8 + offset] == 'L'
                        || sql[8 + offset] == 'l') && (sql[9 + offset] == '_' || sql[9 + offset] == '_') && (
                        sql[10 + offset] == 'R' || sql[10 + offset] == 'r') && (sql[11 + offset] == 'E'
                        || sql[11 + offset] == 'e') && (sql[12 + offset] == 'S' || sql[12 + offset] == 's') && (
                        sql[13 + offset] == 'U' || sql[13 + offset] == 'u') && (sql[14 + offset] == 'L'
                        || sql[14 + offset] == 'l') && (sql[15 + offset] == 'T' || sql[15 + offset] == 't')) {
                        return Token.KW_SQL_SMALL_RESULT;
                    }
                }
            }
        }
        if (size == 3 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's') && (sql[2 + offset] == 'L'
            || sql[2 + offset] == 'l')) {
            return Token.KW_SSL;
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (size < 6 || size > 13) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g')) {
                return Token.KW_STARTING;
            }
            if (size == 6 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'D' || sql[5 + offset] == 'd')) {
                return Token.KW_STORED;
            }
            if (size == 13 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'G' || sql[5 + offset] == 'g') && (sql[6 + offset] == 'H' || sql[6 + offset] == 'h')
                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == '_'
                || sql[8 + offset] == '_') && (sql[9 + offset] == 'J' || sql[9 + offset] == 'j') && (
                sql[10 + offset] == 'O' || sql[10 + offset] == 'o') && (sql[11 + offset] == 'I'
                || sql[11 + offset] == 'i') && (sql[12 + offset] == 'N' || sql[12 + offset] == 'n')) {
                return Token.KW_STRAIGHT_JOIN;
            }
        }
        if (size == 6 && (sql[1 + offset] == 'Y' || sql[1 + offset] == 'y') && (sql[2 + offset] == 'S'
            || sql[2 + offset] == 's') && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e') && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm')) {
            return Token.KW_SYSTEM;
        }
        return 0;
    }

    private static int parseT(byte[] sql, int offset, int size) {
        if (size < 2 || size > 10) {
            return 0;
        }
        if (size == 5 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (sql[2 + offset] == 'B'
            || sql[2 + offset] == 'b') && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e')) {
            return Token.KW_TABLE;
        }
        if (size == 10 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'M' || sql[3 + offset] == 'm') && (sql[4 + offset] == 'I'
            || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'A'
            || sql[6 + offset] == 'a') && (sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == 'E'
            || sql[8 + offset] == 'e') && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')) {
            return Token.KW_TERMINATED;
        }
        if (size == 4 && (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') && (sql[2 + offset] == 'E'
            || sql[2 + offset] == 'e') && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')) {
            return Token.KW_THEN;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 7 || size > 8) {
                return 0;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (sql[3 + offset] == 'Y' || sql[3 + offset] == 'y') {
                    if (size == 8 && (sql[4 + offset] == 'B' || sql[4 + offset] == 'b') && (sql[5 + offset] == 'L'
                        || sql[5 + offset] == 'l') && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o') && (
                        sql[7 + offset] == 'B' || sql[7 + offset] == 'b')) {
                        return Token.KW_TINYBLOB;
                    }
                    if (size == 7 && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N'
                        || sql[5 + offset] == 'n') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                        return Token.KW_TINYINT;
                    }
                    if (size == 8 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e') && (sql[6 + offset] == 'X' || sql[6 + offset] == 'x') && (
                        sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
                        return Token.KW_TINYTEXT;
                    }
                }
            }
        }
        if (size == 2 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o')) {
            return Token.KW_TO;
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 4 || size > 8) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g')) {
                return Token.KW_TRAILING;
            }
            if (size == 7 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'G'
                || sql[3 + offset] == 'g') && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R'
                || sql[6 + offset] == 'r')) {
                return Token.KW_TRIGGER;
            }
            if (size == 4 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e')) {
                return Token.LITERAL_BOOL_TRUE;
            }
        }
        return 0;
    }

    private static int parseU(byte[] sql, int offset, int size) {
        if (size < 3 || size > 13) {
            return 0;
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 4 || size > 8) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o')) {
                return Token.KW_UNDO;
            }
            if (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') {
                if (size < 5 || size > 6) {
                    return 0;
                }
                if (size == 5 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'N'
                    || sql[4 + offset] == 'n')) {
                    return Token.KW_UNION;
                }
                if (size == 6 && (sql[3 + offset] == 'Q' || sql[3 + offset] == 'q') && (sql[4 + offset] == 'U'
                    || sql[4 + offset] == 'u') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Token.KW_UNIQUE;
                }
            }
            if (size == 6 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (
                sql[5 + offset] == 'K' || sql[5 + offset] == 'k')) {
                return Token.KW_UNLOCK;
            }
            if (size == 8 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g') && (
                sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')) {
                return Token.KW_UNSIGNED;
            }
        }
        if (size == 6 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p') && (sql[2 + offset] == 'D'
            || sql[2 + offset] == 'd') && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'T'
            || sql[4 + offset] == 't') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
            return Token.KW_UPDATE;
        }
        if (sql[1 + offset] == 'S' || sql[1 + offset] == 's') {
            if (size < 3 || size > 5) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'G'
                || sql[3 + offset] == 'g') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Token.KW_USAGE;
            }
            if (size == 3 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e')) {
                return Token.KW_USE;
            }
            if (size == 5 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g')) {
                return Token.KW_USING;
            }
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (size < 8 || size > 13) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 8 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd') && (sql[5 + offset] == 'A'
                        || sql[5 + offset] == 'a') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Token.KW_UTC_DATE;
                    }
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') {
                            if (sql[6 + offset] == 'M' || sql[6 + offset] == 'm') {
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (size == 8) {
                                        return Token.KW_UTC_TIME;
                                    }
                                    if (size == 13 && (sql[8 + offset] == 'S' || sql[8 + offset] == 's') && (
                                        sql[9 + offset] == 'T' || sql[9 + offset] == 't') && (sql[10 + offset] == 'A'
                                        || sql[10 + offset] == 'a') && (sql[11 + offset] == 'M'
                                        || sql[11 + offset] == 'm') && (sql[12 + offset] == 'P'
                                        || sql[12 + offset] == 'p')) {
                                        return Token.KW_UTC_TIMESTAMP;
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

    private static int parseV(byte[] sql, int offset, int size) {
        if (size < 6 || size > 12) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size == 6 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'U'
                || sql[3 + offset] == 'u') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                return Token.KW_VALUES;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size < 7 || size > 12) {
                    return 0;
                }
                if (size == 9 && (sql[3 + offset] == 'B' || sql[3 + offset] == 'b') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (
                    sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (sql[7 + offset] == 'R'
                    || sql[7 + offset] == 'r') && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y')) {
                    return Token.KW_VARBINARY;
                }
                if (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') {
                    if (sql[4 + offset] == 'H' || sql[4 + offset] == 'h') {
                        if (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') {
                            if (sql[6 + offset] == 'R' || sql[6 + offset] == 'r') {
                                if (size == 7) {
                                    return Token.KW_VARCHAR;
                                }
                                if (size == 12 && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (
                                    sql[8 + offset] == 'C' || sql[8 + offset] == 'c') && (sql[9 + offset] == 'T'
                                    || sql[9 + offset] == 't') && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')
                                    && (sql[11 + offset] == 'R' || sql[11 + offset] == 'r')) {
                                    return Token.KW_VARCHARACTER;
                                }
                            }
                        }
                    }
                }
                if (size == 7 && (sql[3 + offset] == 'Y' || sql[3 + offset] == 'y') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (
                    sql[6 + offset] == 'G' || sql[6 + offset] == 'g')) {
                    return Token.KW_VARYING;
                }
            }
        }
        if (size == 7 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'U'
            || sql[4 + offset] == 'u') && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L'
            || sql[6 + offset] == 'l')) {
            return Token.KW_VIRTUAL;
        }
        return 0;
    }

    private static int parseW(byte[] sql, int offset, int size) {
        if (size < 4 || size > 6) {
            return 0;
        }
        if (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') {
            if (size < 4 || size > 5) {
                return 0;
            }
            if (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') {
                if (size == 4 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')) {
                    return Token.KW_WHEN;
                }
                if (size == 5 && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e')) {
                    return Token.KW_WHERE;
                }
            }
            if (size == 5 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Token.KW_WHILE;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size == 6 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'D'
                || sql[3 + offset] == 'd') && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (
                sql[5 + offset] == 'W' || sql[5 + offset] == 'w')) {
                return Token.KW_WINDOW;
            }
            if (size == 4 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'H'
                || sql[3 + offset] == 'h')) {
                return Token.KW_WITH;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') && (sql[2 + offset] == 'I'
            || sql[2 + offset] == 'i') && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e')) {
            return Token.KW_WRITE;
        }
        return 0;
    }

    private static int parseX(byte[] sql, int offset, int size) {
        if (size != 3) {
            return 0;
        }
        if (size == 3 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r')) {
            return Token.KW_XOR;
        }
        return 0;
    }

    private static int parseY(byte[] sql, int offset, int size) {
        if (size != 10) {
            return 0;
        }
        if (size == 10 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == '_'
            || sql[4 + offset] == '_') && (sql[5 + offset] == 'M' || sql[5 + offset] == 'm') && (sql[6 + offset] == 'O'
            || sql[6 + offset] == 'o') && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'T'
            || sql[8 + offset] == 't') && (sql[9 + offset] == 'H' || sql[9 + offset] == 'h')) {
            return Token.KW_YEAR_MONTH;
        }
        return 0;
    }

    private static int parseZ(byte[] sql, int offset, int size) {
        if (size != 8) {
            return 0;
        }
        if (size == 8 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'F'
            || sql[4 + offset] == 'f') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'L'
            || sql[6 + offset] == 'l') && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')) {
            return Token.KW_ZEROFILL;
        }
        return 0;
    }

}

