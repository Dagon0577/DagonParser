package parser.token;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class KeywordsParser {
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
            case 'x':
            case 'X':
                return parseX(sql, offset, size);
            case 'y':
            case 'Y':
                return parseY(sql, offset, size);
        }
        return 0;
    }

    private static int parseA(byte[] sql, int offset, int size) {
        if (size < 2 || size > 15) {
            return 0;
        }
        if (sql[1 + offset] == 'C' || sql[1 + offset] == 'c') {
            if (size < 6 || size > 7) {
                return 0;
            }
            if (size == 7 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (
                sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'T'
                || sql[6 + offset] == 't')) {
                return Keywords.ACCOUNT;
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size != 6) {
                    return 0;
                }
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (size == 6 && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (sql[5 + offset] == 'N'
                        || sql[5 + offset] == 'n')) {
                        return Keywords.ACTION;
                    }
                    if (size == 6 && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e')) {
                        return Keywords.ACTIVE;
                    }
                }
            }
        }
        if (size == 5 && (sql[1 + offset] == 'D' || sql[1 + offset] == 'd') && (sql[2 + offset] == 'M'
            || sql[2 + offset] == 'm') && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'N'
            || sql[4 + offset] == 'n')) {
            return Keywords.ADMIN;
        }
        if (size == 5 && (sql[1 + offset] == 'F' || sql[1 + offset] == 'f') && (sql[2 + offset] == 'T'
            || sql[2 + offset] == 't') && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R'
            || sql[4 + offset] == 'r')) {
            return Keywords.AFTER;
        }
        if (sql[1 + offset] == 'G' || sql[1 + offset] == 'g') {
            if (size < 7 || size > 9) {
                return 0;
            }
            if (size == 7 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'T'
                || sql[6 + offset] == 't')) {
                return Keywords.AGAINST;
            }
            if (size == 9 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'G' || sql[5 + offset] == 'g') && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e')) {
                return Keywords.AGGREGATE;
            }
        }
        if (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') {
            if (size < 6 || size > 9) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                && (sql[7 + offset] == 'H' || sql[7 + offset] == 'h') && (sql[8 + offset] == 'M'
                || sql[8 + offset] == 'm')) {
                return Keywords.ALGORITHM;
            }
            if (size == 6 && (sql[2 + offset] == 'W' || sql[2 + offset] == 'w') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                return Keywords.ALWAYS;
            }
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 3 || size > 7) {
                return 0;
            }
            if (size == 7 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Keywords.ANALYSE;
            }
            if (size == 3 && (sql[2 + offset] == 'Y' || sql[2 + offset] == 'y')) {
                return Keywords.ANY;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c') && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'I'
            || sql[4 + offset] == 'i')) {
            return Keywords.ASCII;
        }
        if (size == 2 && (sql[1 + offset] == 'T' || sql[1 + offset] == 't')) {
            return Keywords.AT;
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 14 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (size == 15 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (sql[5 + offset] == 'X'
                        || sql[5 + offset] == 'x') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'N'
                        || sql[8 + offset] == 'n') && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd') && (
                        sql[10 + offset] == '_' || sql[10 + offset] == '_') && (sql[11 + offset] == 'S'
                        || sql[11 + offset] == 's') && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i') && (
                        sql[13 + offset] == 'Z' || sql[13 + offset] == 'z') && (sql[14 + offset] == 'E'
                        || sql[14 + offset] == 'e')) {
                        return Keywords.AUTOEXTEND_SIZE;
                    }
                    if (size == 14 && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (
                        sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (sql[8 + offset] == 'R'
                        || sql[8 + offset] == 'r') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (
                        sql[10 + offset] == 'M' || sql[10 + offset] == 'm') && (sql[11 + offset] == 'E'
                        || sql[11 + offset] == 'e') && (sql[12 + offset] == 'N' || sql[12 + offset] == 'n') && (
                        sql[13 + offset] == 'T' || sql[13 + offset] == 't')) {
                        return Keywords.AUTO_INCREMENT;
                    }
                }
            }
        }
        if (sql[1 + offset] == 'V' || sql[1 + offset] == 'v') {
            if (size < 3 || size > 14) {
                return 0;
            }
            if (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') {
                if (size == 3) {
                    return Keywords.AVG;
                }
                if (size == 14 && (sql[3 + offset] == '_' || sql[3 + offset] == '_') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r') && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (
                    sql[6 + offset] == 'W' || sql[6 + offset] == 'w') && (sql[7 + offset] == '_'
                    || sql[7 + offset] == '_') && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l') && (
                    sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (sql[10 + offset] == 'N'
                    || sql[10 + offset] == 'n') && (sql[11 + offset] == 'G' || sql[11 + offset] == 'g') && (
                    sql[12 + offset] == 'T' || sql[12 + offset] == 't') && (sql[13 + offset] == 'H'
                    || sql[13 + offset] == 'h')) {
                    return Keywords.AVG_ROW_LENGTH;
                }
            }
        }
        return 0;
    }

    private static int parseB(byte[] sql, int offset, int size) {
        if (size < 3 || size > 7) {
            return 0;
        }
        if (size == 6 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c') && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k') && (sql[4 + offset] == 'U'
            || sql[4 + offset] == 'u') && (sql[5 + offset] == 'P' || sql[5 + offset] == 'p')) {
            return Keywords.BACKUP;
        }
        if (size == 5 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'G'
            || sql[2 + offset] == 'g') && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'N'
            || sql[4 + offset] == 'n')) {
            return Keywords.BEGIN;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 3 || size > 6) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (
                sql[5 + offset] == 'G' || sql[5 + offset] == 'g')) {
                return Keywords.BINLOG;
            }
            if (size == 3 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't')) {
                return Keywords.BIT;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') && (sql[2 + offset] == 'O'
            || sql[2 + offset] == 'o') && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'K'
            || sql[4 + offset] == 'k')) {
            return Keywords.BLOCK;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') {
                    if (size == 4) {
                        return Keywords.BOOL;
                    }
                    if (size == 7 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (sql[5 + offset] == 'A'
                        || sql[5 + offset] == 'a') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')) {
                        return Keywords.BOOLEAN;
                    }
                }
            }
        }
        if (size == 5 && (sql[1 + offset] == 'T' || sql[1 + offset] == 't') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e')) {
            return Keywords.BTREE;
        }
        if (size == 7 && (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c') && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'S'
            || sql[6 + offset] == 's')) {
            return Keywords.BUCKETS;
        }
        if (size == 4 && (sql[1 + offset] == 'Y' || sql[1 + offset] == 'y') && (sql[2 + offset] == 'T'
            || sql[2 + offset] == 't') && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')) {
            return Keywords.BYTE;
        }
        return 0;
    }

    private static int parseC(byte[] sql, int offset, int size) {
        if (size < 3 || size > 18) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 5 || size > 12) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'H'
                || sql[3 + offset] == 'h') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Keywords.CACHE;
            }
            if (size == 8 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')) {
                return Keywords.CASCADED;
            }
            if (size == 12 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g')
                && (sql[7 + offset] == '_' || sql[7 + offset] == '_') && (sql[8 + offset] == 'N'
                || sql[8 + offset] == 'n') && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a') && (
                sql[10 + offset] == 'M' || sql[10 + offset] == 'm') && (sql[11 + offset] == 'E'
                || sql[11 + offset] == 'e')) {
                return Keywords.CATALOG_NAME;
            }
        }
        if (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') {
            if (size < 5 || size > 8) {
                return 0;
            }
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (size < 5 || size > 7) {
                    return 0;
                }
                if (size == 5 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'N'
                    || sql[4 + offset] == 'n')) {
                    return Keywords.CHAIN;
                }
                if (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') {
                    if (size != 7) {
                        return 0;
                    }
                    if (size == 7 && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e') && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd')) {
                        return Keywords.CHANGED;
                    }
                    if (size == 7 && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')) {
                        return Keywords.CHANNEL;
                    }
                }
                if (size == 7 && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'S'
                    || sql[4 + offset] == 's') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                    return Keywords.CHARSET;
                }
            }
            if (size == 8 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'K' || sql[4 + offset] == 'k') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u')
                && (sql[7 + offset] == 'M' || sql[7 + offset] == 'm')) {
                return Keywords.CHECKSUM;
            }
        }
        if (size == 6 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'P'
            || sql[2 + offset] == 'p') && (sql[3 + offset] == 'H' || sql[3 + offset] == 'h') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
            return Keywords.CIPHER;
        }
        if (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') {
            if (size == 12 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (
                sql[5 + offset] == '_' || sql[5 + offset] == '_') && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (sql[8 + offset] == 'I'
                || sql[8 + offset] == 'i') && (sql[9 + offset] == 'G' || sql[9 + offset] == 'g') && (
                sql[10 + offset] == 'I' || sql[10 + offset] == 'i') && (sql[11 + offset] == 'N'
                || sql[11 + offset] == 'n')) {
                return Keywords.CLASS_ORIGIN;
            }
            if (size == 6 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                return Keywords.CLIENT;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (size != 5) {
                    return 0;
                }
                if (size == 5 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e')) {
                    return Keywords.CLONE;
                }
                if (size == 5 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e')) {
                    return Keywords.CLOSE;
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 18) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                return Keywords.COALESCE;
            }
            if (size == 4 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e')) {
                return Keywords.CODE;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size < 7 || size > 13) {
                    return 0;
                }
                if (size == 9 && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (
                    sql[6 + offset] == 'I' || sql[6 + offset] == 'i') && (sql[7 + offset] == 'O'
                    || sql[7 + offset] == 'o') && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')) {
                    return Keywords.COLLATION;
                }
                if (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') {
                    if (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') {
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (size == 7 && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                                return Keywords.COLUMNS;
                            }
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (size < 11 || size > 13) {
                                    return 0;
                                }
                                if (size == 13 && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f') && (
                                    sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (sql[9 + offset] == 'R'
                                    || sql[9 + offset] == 'r') && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm')
                                    && (sql[11 + offset] == 'A' || sql[11 + offset] == 'a') && (sql[12 + offset] == 'T'
                                    || sql[12 + offset] == 't')) {
                                    return Keywords.COLUMN_FORMAT;
                                }
                                if (size == 11 && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (
                                    sql[8 + offset] == 'A' || sql[8 + offset] == 'a') && (sql[9 + offset] == 'M'
                                    || sql[9 + offset] == 'm') && (sql[10 + offset] == 'E'
                                    || sql[10 + offset] == 'e')) {
                                    return Keywords.COLUMN_NAME;
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') {
                if (size < 6 || size > 11) {
                    return 0;
                }
                if (sql[3 + offset] == 'M' || sql[3 + offset] == 'm') {
                    if (size < 6 || size > 9) {
                        return 0;
                    }
                    if (size == 7 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (sql[5 + offset] == 'N'
                        || sql[5 + offset] == 'n') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                        return Keywords.COMMENT;
                    }
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (size == 6) {
                                return Keywords.COMMIT;
                            }
                            if (size == 9 && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (
                                sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'D'
                                || sql[8 + offset] == 'd')) {
                                return Keywords.COMMITTED;
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'P' || sql[3 + offset] == 'p') {
                    if (size < 7 || size > 11) {
                        return 0;
                    }
                    if (size == 7 && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (sql[5 + offset] == 'C'
                        || sql[5 + offset] == 'c') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                        return Keywords.COMPACT;
                    }
                    if (size == 10 && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (
                        sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'O'
                        || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')) {
                        return Keywords.COMPLETION;
                    }
                    if (size == 9 && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (sql[5 + offset] == 'N'
                        || sql[5 + offset] == 'n') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') && (
                        sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'T'
                        || sql[8 + offset] == 't')) {
                        return Keywords.COMPONENT;
                    }
                    if (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') {
                        if (size < 10 || size > 11) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') {
                            if (sql[6 + offset] == 'S' || sql[6 + offset] == 's') {
                                if (sql[7 + offset] == 'S' || sql[7 + offset] == 's') {
                                    if (size == 10 && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (
                                        sql[9 + offset] == 'D' || sql[9 + offset] == 'd')) {
                                        return Keywords.COMPRESSED;
                                    }
                                    if (size == 11 && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (
                                        sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (sql[10 + offset] == 'N'
                                        || sql[10 + offset] == 'n')) {
                                        return Keywords.COMPRESSION;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 7 || size > 18) {
                    return 0;
                }
                if (size == 10 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'U'
                    || sql[4 + offset] == 'u') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'R' || sql[6 + offset] == 'r') && (sql[7 + offset] == 'E'
                    || sql[7 + offset] == 'e') && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n') && (
                    sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                    return Keywords.CONCURRENT;
                }
                if (size == 10 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (sql[7 + offset] == 'I'
                    || sql[7 + offset] == 'i') && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (
                    sql[9 + offset] == 'N' || sql[9 + offset] == 'n')) {
                    return Keywords.CONNECTION;
                }
                if (sql[3 + offset] == 'S' || sql[3 + offset] == 's') {
                    if (size < 10 || size > 18) {
                        return 0;
                    }
                    if (size == 10 && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (sql[5 + offset] == 'S'
                        || sql[5 + offset] == 's') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'N'
                        || sql[8 + offset] == 'n') && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                        return Keywords.CONSISTENT;
                    }
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (size < 15 || size > 18) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') {
                            if (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') {
                                if (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') {
                                    if (sql[8 + offset] == 'N' || sql[8 + offset] == 'n') {
                                        if (sql[9 + offset] == 'T' || sql[9 + offset] == 't') {
                                            if (sql[10 + offset] == '_' || sql[10 + offset] == '_') {
                                                if (size == 18 && (sql[11 + offset] == 'C' || sql[11 + offset] == 'c')
                                                    && (sql[12 + offset] == 'A' || sql[12 + offset] == 'a') && (
                                                    sql[13 + offset] == 'T' || sql[13 + offset] == 't') && (
                                                    sql[14 + offset] == 'A' || sql[14 + offset] == 'a') && (
                                                    sql[15 + offset] == 'L' || sql[15 + offset] == 'l') && (
                                                    sql[16 + offset] == 'O' || sql[16 + offset] == 'o') && (
                                                    sql[17 + offset] == 'G' || sql[17 + offset] == 'g')) {
                                                    return Keywords.CONSTRAINT_CATALOG;
                                                }
                                                if (size == 15 && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')
                                                    && (sql[12 + offset] == 'A' || sql[12 + offset] == 'a') && (
                                                    sql[13 + offset] == 'M' || sql[13 + offset] == 'm') && (
                                                    sql[14 + offset] == 'E' || sql[14 + offset] == 'e')) {
                                                    return Keywords.CONSTRAINT_NAME;
                                                }
                                                if (size == 17 && (sql[11 + offset] == 'S' || sql[11 + offset] == 's')
                                                    && (sql[12 + offset] == 'C' || sql[12 + offset] == 'c') && (
                                                    sql[13 + offset] == 'H' || sql[13 + offset] == 'h') && (
                                                    sql[14 + offset] == 'E' || sql[14 + offset] == 'e') && (
                                                    sql[15 + offset] == 'M' || sql[15 + offset] == 'm') && (
                                                    sql[16 + offset] == 'A' || sql[16 + offset] == 'a')) {
                                                    return Keywords.CONSTRAINT_SCHEMA;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (size < 7 || size > 8) {
                        return 0;
                    }
                    if (size == 8 && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (
                        sql[7 + offset] == 'S' || sql[7 + offset] == 's')) {
                        return Keywords.CONTAINS;
                    }
                    if (size == 7 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (sql[5 + offset] == 'X'
                        || sql[5 + offset] == 'x') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                        return Keywords.CONTEXT;
                    }
                }
            }
        }
        if (size == 3 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p') && (sql[2 + offset] == 'U'
            || sql[2 + offset] == 'u')) {
            return Keywords.CPU;
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size == 7 && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                    return Keywords.CURRENT;
                }
                if (size == 11 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'O'
                    || sql[4 + offset] == 'o') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == '_' || sql[6 + offset] == '_') && (sql[7 + offset] == 'N'
                    || sql[7 + offset] == 'n') && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a') && (
                    sql[9 + offset] == 'M' || sql[9 + offset] == 'm') && (sql[10 + offset] == 'E'
                    || sql[10 + offset] == 'e')) {
                    return Keywords.CURSOR_NAME;
                }
            }
        }
        return 0;
    }

    private static int parseD(byte[] sql, int offset, int size) {
        if (size < 2 || size > 15) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 3 || size > 8) {
                return 0;
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 4 || size > 8) {
                    return 0;
                }
                if (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') {
                    if (size == 4) {
                        return Keywords.DATA;
                    }
                    if (size == 8 && (sql[4 + offset] == 'F' || sql[4 + offset] == 'f') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Keywords.DATAFILE;
                    }
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 4) {
                        return Keywords.DATE;
                    }
                    if (size == 8 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'M' || sql[6 + offset] == 'm') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Keywords.DATETIME;
                    }
                }
            }
            if (size == 3 && (sql[2 + offset] == 'Y' || sql[2 + offset] == 'y')) {
                return Keywords.DAY;
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 7 || size > 15) {
                return 0;
            }
            if (size == 10 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                return Keywords.DEALLOCATE;
            }
            if (sql[2 + offset] == 'F' || sql[2 + offset] == 'f') {
                if (size < 7 || size > 12) {
                    return 0;
                }
                if (size == 12 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'U'
                    || sql[4 + offset] == 'u') && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (sql[7 + offset] == '_'
                    || sql[7 + offset] == '_') && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a') && (
                    sql[9 + offset] == 'U' || sql[9 + offset] == 'u') && (sql[10 + offset] == 'T'
                    || sql[10 + offset] == 't') && (sql[11 + offset] == 'H' || sql[11 + offset] == 'h')) {
                    return Keywords.DEFAULT_AUTH;
                }
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (size < 7 || size > 10) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') {
                        if (size == 7 && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R'
                            || sql[6 + offset] == 'r')) {
                            return Keywords.DEFINER;
                        }
                        if (size == 10 && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'T'
                            || sql[6 + offset] == 't') && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (
                            sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N'
                            || sql[9 + offset] == 'n')) {
                            return Keywords.DEFINITION;
                        }
                    }
                }
            }
            if (size == 15 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y') && (
                sql[5 + offset] == '_' || sql[5 + offset] == '_') && (sql[6 + offset] == 'K' || sql[6 + offset] == 'k')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'Y'
                || sql[8 + offset] == 'y') && (sql[9 + offset] == '_' || sql[9 + offset] == '_') && (
                sql[10 + offset] == 'W' || sql[10 + offset] == 'w') && (sql[11 + offset] == 'R'
                || sql[11 + offset] == 'r') && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i') && (
                sql[13 + offset] == 'T' || sql[13 + offset] == 't') && (sql[14 + offset] == 'E'
                || sql[14 + offset] == 'e')) {
                return Keywords.DELAY_KEY_WRITE;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 11 || size > 12) {
                    return 0;
                }
                if (size == 11 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (
                    sql[6 + offset] == 'P' || sql[6 + offset] == 'p') && (sql[7 + offset] == 'T'
                    || sql[7 + offset] == 't') && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (
                    sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (sql[10 + offset] == 'N'
                    || sql[10 + offset] == 'n')) {
                    return Keywords.DESCRIPTION;
                }
                if (size == 12 && (sql[3 + offset] == '_' || sql[3 + offset] == '_') && (sql[4 + offset] == 'K'
                    || sql[4 + offset] == 'k') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (
                    sql[6 + offset] == 'Y' || sql[6 + offset] == 'y') && (sql[7 + offset] == '_'
                    || sql[7 + offset] == '_') && (sql[8 + offset] == 'F' || sql[8 + offset] == 'f') && (
                    sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (sql[10 + offset] == 'L'
                    || sql[10 + offset] == 'l') && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e')) {
                    return Keywords.DES_KEY_FILE;
                }
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 4 || size > 11) {
                return 0;
            }
            if (size == 11 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'G'
                || sql[3 + offset] == 'g') && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (
                sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')
                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == 'I'
                || sql[8 + offset] == 'i') && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') && (
                sql[10 + offset] == 'S' || sql[10 + offset] == 's')) {
                return Keywords.DIAGNOSTICS;
            }
            if (size == 9 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (sql[8 + offset] == 'Y'
                || sql[8 + offset] == 'y')) {
                return Keywords.DIRECTORY;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 4 || size > 7) {
                    return 0;
                }
                if (size == 7 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'B'
                    || sql[4 + offset] == 'b') && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Keywords.DISABLE;
                }
                if (size == 7 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'D' || sql[6 + offset] == 'd')) {
                    return Keywords.DISCARD;
                }
                if (size == 4 && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k')) {
                    return Keywords.DISK;
                }
            }
        }
        if (size == 2 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o')) {
            return Keywords.DO;
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 8 || size > 9) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'P'
                || sql[3 + offset] == 'p') && (sql[4 + offset] == 'F' || sql[4 + offset] == 'f') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                return Keywords.DUMPFILE;
            }
            if (size == 9 && (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'C' || sql[5 + offset] == 'c') && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e')) {
                return Keywords.DUPLICATE;
            }
        }
        if (size == 7 && (sql[1 + offset] == 'Y' || sql[1 + offset] == 'y') && (sql[2 + offset] == 'N'
            || sql[2 + offset] == 'n') && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'M'
            || sql[4 + offset] == 'm') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'C'
            || sql[6 + offset] == 'c')) {
            return Keywords.DYNAMIC;
        }
        return 0;
    }

    private static int parseE(byte[] sql, int offset, int size) {
        if (size < 3 || size > 11) {
            return 0;
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 3 || size > 10) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'B'
                || sql[3 + offset] == 'b') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Keywords.ENABLE;
            }
            if (size == 10 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y') && (
                sql[5 + offset] == 'P' || sql[5 + offset] == 'p') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'O'
                || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n')) {
                return Keywords.ENCRYPTION;
            }
            if (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') {
                if (size < 3 || size > 4) {
                    return 0;
                }
                if (size == 3) {
                    return Keywords.END;
                }
                if (size == 4 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')) {
                    return Keywords.ENDS;
                }
            }
            if (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') {
                if (size < 6 || size > 7) {
                    return 0;
                }
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') {
                        if (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') {
                            if (size == 6) {
                                return Keywords.ENGINE;
                            }
                            if (size == 7 && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                                return Keywords.ENGINES;
                            }
                        }
                    }
                }
            }
            if (size == 4 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'M'
                || sql[3 + offset] == 'm')) {
                return Keywords.ENUM;
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 5 || size > 6) {
                return 0;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') {
                        if (size == 5) {
                            return Keywords.ERROR;
                        }
                        if (size == 6 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                            return Keywords.ERRORS;
                        }
                    }
                }
            }
        }
        if (size == 6 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c') && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'P'
            || sql[4 + offset] == 'p') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
            return Keywords.ESCAPE;
        }
        if (sql[1 + offset] == 'V' || sql[1 + offset] == 'v') {
            if (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') {
                if (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') {
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (size == 5) {
                            return Keywords.EVENT;
                        }
                        if (size == 6 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                            return Keywords.EVENTS;
                        }
                    }
                }
                if (size == 5 && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'Y'
                    || sql[4 + offset] == 'y')) {
                    return Keywords.EVERY;
                }
            }
        }
        if (sql[1 + offset] == 'X' || sql[1 + offset] == 'x') {
            if (size < 6 || size > 11) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (size < 7 || size > 8) {
                    return 0;
                }
                if (size == 8 && (sql[3 + offset] == 'H' || sql[3 + offset] == 'h') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (
                    sql[6 + offset] == 'G' || sql[6 + offset] == 'g') && (sql[7 + offset] == 'E'
                    || sql[7 + offset] == 'e')) {
                    return Keywords.EXCHANGE;
                }
                if (size == 7 && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') && (sql[4 + offset] == 'U'
                    || sql[4 + offset] == 'u') && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Keywords.EXCLUDE;
                }
            }
            if (size == 7 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Keywords.EXECUTE;
            }
            if (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') {
                if (size < 6 || size > 9) {
                    return 0;
                }
                if (size == 9 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'N'
                    || sql[4 + offset] == 'n') && (sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (
                    sql[6 + offset] == 'I' || sql[6 + offset] == 'i') && (sql[7 + offset] == 'O'
                    || sql[7 + offset] == 'o') && (sql[8 + offset] == 'N' || sql[8 + offset] == 'n')) {
                    return Keywords.EXPANSION;
                }
                if (size == 6 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Keywords.EXPIRE;
                }
                if (size == 6 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                    return Keywords.EXPORT;
                }
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 8 || size > 11) {
                    return 0;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') {
                        if (size == 8 && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (sql[6 + offset] == 'E'
                            || sql[6 + offset] == 'e') && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')) {
                            return Keywords.EXTENDED;
                        }
                        if (size == 11 && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == '_'
                            || sql[6 + offset] == '_') && (sql[7 + offset] == 'S' || sql[7 + offset] == 's') && (
                            sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (sql[9 + offset] == 'Z'
                            || sql[9 + offset] == 'z') && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                            return Keywords.EXTENT_SIZE;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static int parseF(byte[] sql, int offset, int size) {
        if (size < 4 || size > 15) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 6) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't')) {
                return Keywords.FAST;
            }
            if (size == 6 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                return Keywords.FAULTS;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size == 6 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                return Keywords.FIELDS;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 4) {
                        return Keywords.FILE;
                    }
                    if (size == 15 && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (sql[5 + offset] == 'B'
                        || sql[5 + offset] == 'b') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l') && (
                        sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (sql[8 + offset] == 'C'
                        || sql[8 + offset] == 'c') && (sql[9 + offset] == 'K' || sql[9 + offset] == 'k') && (
                        sql[10 + offset] == '_' || sql[10 + offset] == '_') && (sql[11 + offset] == 'S'
                        || sql[11 + offset] == 's') && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i') && (
                        sql[13 + offset] == 'Z' || sql[13 + offset] == 'z') && (sql[14 + offset] == 'E'
                        || sql[14 + offset] == 'e')) {
                        return Keywords.FILE_BLOCK_SIZE;
                    }
                }
                if (size == 6 && (sql[3 + offset] == 'T' || sql[3 + offset] == 't') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
                    return Keywords.FILTER;
                }
            }
            if (size == 5 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
                return Keywords.FIRST;
            }
            if (size == 5 && (sql[2 + offset] == 'X' || sql[2 + offset] == 'x') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')) {
                return Keywords.FIXED;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') && (sql[2 + offset] == 'U'
            || sql[2 + offset] == 'u') && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'H'
            || sql[4 + offset] == 'h')) {
            return Keywords.FLUSH;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 5 || size > 9) {
                return 0;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size < 7 || size > 9) {
                    return 0;
                }
                if (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') {
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (sql[5 + offset] == 'W' || sql[5 + offset] == 'w') {
                            if (size == 9 && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') && (
                                sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'G'
                                || sql[8 + offset] == 'g')) {
                                return Keywords.FOLLOWING;
                            }
                            if (size == 7 && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                                return Keywords.FOLLOWS;
                            }
                        }
                    }
                }
            }
            if (size == 6 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'M'
                || sql[3 + offset] == 'm') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                return Keywords.FORMAT;
            }
            if (size == 5 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd')) {
                return Keywords.FOUND;
            }
        }
        if (size == 4 && (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') && (sql[2 + offset] == 'L'
            || sql[2 + offset] == 'l') && (sql[3 + offset] == 'L' || sql[3 + offset] == 'l')) {
            return Keywords.FULL;
        }
        return 0;
    }

    private static int parseG(byte[] sql, int offset, int size) {
        if (size < 6 || size > 21) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 7 || size > 21) {
                return 0;
            }
            if (size == 7 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L'
                || sql[6 + offset] == 'l')) {
                return Keywords.GENERAL;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (size < 8 || size > 18) {
                    return 0;
                }
                if (sql[3 + offset] == 'M' || sql[3 + offset] == 'm') {
                    if (size == 14 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l') && (
                        sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (sql[8 + offset] == 'E'
                        || sql[8 + offset] == 'e') && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') && (
                        sql[10 + offset] == 'T' || sql[10 + offset] == 't') && (sql[11 + offset] == 'I'
                        || sql[11 + offset] == 'i') && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o') && (
                        sql[13 + offset] == 'N' || sql[13 + offset] == 'n')) {
                        return Keywords.GEOMCOLLECTION;
                    }
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (sql[6 + offset] == 'R' || sql[6 + offset] == 'r') {
                                if (sql[7 + offset] == 'Y' || sql[7 + offset] == 'y') {
                                    if (size == 8) {
                                        return Keywords.GEOMETRY;
                                    }
                                    if (size == 18 && (sql[8 + offset] == 'C' || sql[8 + offset] == 'c') && (
                                        sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (sql[10 + offset] == 'L'
                                        || sql[10 + offset] == 'l') && (sql[11 + offset] == 'L'
                                        || sql[11 + offset] == 'l') && (sql[12 + offset] == 'E'
                                        || sql[12 + offset] == 'e') && (sql[13 + offset] == 'C'
                                        || sql[13 + offset] == 'c') && (sql[14 + offset] == 'T'
                                        || sql[14 + offset] == 't') && (sql[15 + offset] == 'I'
                                        || sql[15 + offset] == 'i') && (sql[16 + offset] == 'O'
                                        || sql[16 + offset] == 'o') && (sql[17 + offset] == 'N'
                                        || sql[17 + offset] == 'n')) {
                                        return Keywords.GEOMETRYCOLLECTION;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 10 || size > 21) {
                    return 0;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 10 && (sql[4 + offset] == 'F' || sql[4 + offset] == 'f') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r') && (
                        sql[7 + offset] == 'M' || sql[7 + offset] == 'm') && (sql[8 + offset] == 'A'
                        || sql[8 + offset] == 'a') && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                        return Keywords.GET_FORMAT;
                    }
                    if (size == 21 && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') && (sql[5 + offset] == 'A'
                        || sql[5 + offset] == 'a') && (sql[6 + offset] == 'S' || sql[6 + offset] == 's') && (
                        sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == 'E'
                        || sql[8 + offset] == 'e') && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r') && (
                        sql[10 + offset] == '_' || sql[10 + offset] == '_') && (sql[11 + offset] == 'P'
                        || sql[11 + offset] == 'p') && (sql[12 + offset] == 'U' || sql[12 + offset] == 'u') && (
                        sql[13 + offset] == 'B' || sql[13 + offset] == 'b') && (sql[14 + offset] == 'L'
                        || sql[14 + offset] == 'l') && (sql[15 + offset] == 'I' || sql[15 + offset] == 'i') && (
                        sql[16 + offset] == 'C' || sql[16 + offset] == 'c') && (sql[17 + offset] == '_'
                        || sql[17 + offset] == '_') && (sql[18 + offset] == 'K' || sql[18 + offset] == 'k') && (
                        sql[19 + offset] == 'E' || sql[19 + offset] == 'e') && (sql[20 + offset] == 'Y'
                        || sql[20 + offset] == 'y')) {
                        return Keywords.GET_MASTER_PUBLIC_KEY;
                    }
                }
            }
        }
        if (size == 6 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') && (sql[2 + offset] == 'O'
            || sql[2 + offset] == 'o') && (sql[3 + offset] == 'B' || sql[3 + offset] == 'b') && (sql[4 + offset] == 'A'
            || sql[4 + offset] == 'a') && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l')) {
            return Keywords.GLOBAL;
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 6 || size > 17) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                return Keywords.GRANTS;
            }
            if (size == 17 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'U'
                || sql[3 + offset] == 'u') && (sql[4 + offset] == 'P' || sql[4 + offset] == 'p') && (
                sql[5 + offset] == '_' || sql[5 + offset] == '_') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'P'
                || sql[8 + offset] == 'p') && (sql[9 + offset] == 'L' || sql[9 + offset] == 'l') && (
                sql[10 + offset] == 'I' || sql[10 + offset] == 'i') && (sql[11 + offset] == 'C'
                || sql[11 + offset] == 'c') && (sql[12 + offset] == 'A' || sql[12 + offset] == 'a') && (
                sql[13 + offset] == 'T' || sql[13 + offset] == 't') && (sql[14 + offset] == 'I'
                || sql[14 + offset] == 'i') && (sql[15 + offset] == 'O' || sql[15 + offset] == 'o') && (
                sql[16 + offset] == 'N' || sql[16 + offset] == 'n')) {
                return Keywords.GROUP_REPLICATION;
            }
        }
        return 0;
    }

    private static int parseH(byte[] sql, int offset, int size) {
        if (size < 4 || size > 9) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (size == 7 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'D'
                || sql[3 + offset] == 'd') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R'
                || sql[6 + offset] == 'r')) {
                return Keywords.HANDLER;
            }
            if (size == 4 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'H'
                || sql[3 + offset] == 'h')) {
                return Keywords.HASH;
            }
        }
        if (size == 4 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'L'
            || sql[2 + offset] == 'l') && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p')) {
            return Keywords.HELP;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 7 || size > 9) {
                return 0;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (size == 9 && (sql[5 + offset] == 'G' || sql[5 + offset] == 'g') && (sql[6 + offset] == 'R'
                            || sql[6 + offset] == 'r') && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (
                            sql[8 + offset] == 'M' || sql[8 + offset] == 'm')) {
                            return Keywords.HISTOGRAM;
                        }
                        if (size == 7 && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (sql[6 + offset] == 'Y'
                            || sql[6 + offset] == 'y')) {
                            return Keywords.HISTORY;
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 5) {
                return 0;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (size == 4) {
                        return Keywords.HOST;
                    }
                    if (size == 5 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                        return Keywords.HOSTS;
                    }
                }
            }
            if (size == 4 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r')) {
                return Keywords.HOUR;
            }
        }
        return 0;
    }

    private static int parseI(byte[] sql, int offset, int size) {
        if (size < 2 || size > 17) {
            return 0;
        }
        if (size == 10 && (sql[1 + offset] == 'D' || sql[1 + offset] == 'd') && (sql[2 + offset] == 'E'
            || sql[2 + offset] == 'e') && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == 'T'
            || sql[4 + offset] == 't') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'F'
            || sql[6 + offset] == 'f') && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'E'
            || sql[8 + offset] == 'e') && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd')) {
            return Keywords.IDENTIFIED;
        }
        if (size == 17 && (sql[1 + offset] == 'G' || sql[1 + offset] == 'g') && (sql[2 + offset] == 'N'
            || sql[2 + offset] == 'n') && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'R'
            || sql[4 + offset] == 'r') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == '_'
            || sql[6 + offset] == '_') && (sql[7 + offset] == 'S' || sql[7 + offset] == 's') && (sql[8 + offset] == 'E'
            || sql[8 + offset] == 'e') && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r') && (sql[10 + offset] == 'V'
            || sql[10 + offset] == 'v') && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e') && (
            sql[12 + offset] == 'R' || sql[12 + offset] == 'r') && (sql[13 + offset] == '_' || sql[13 + offset] == '_')
            && (sql[14 + offset] == 'I' || sql[14 + offset] == 'i') && (sql[15 + offset] == 'D'
            || sql[15 + offset] == 'd') && (sql[16 + offset] == 'S' || sql[16 + offset] == 's')) {
            return Keywords.IGNORE_SERVER_IDS;
        }
        if (size == 6 && (sql[1 + offset] == 'M' || sql[1 + offset] == 'm') && (sql[2 + offset] == 'P'
            || sql[2 + offset] == 'p') && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'R'
            || sql[4 + offset] == 'r') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
            return Keywords.IMPORT;
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 7 || size > 13) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'V' || sql[6 + offset] == 'v')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                return Keywords.INACTIVE;
            }
            if (size == 7 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'X' || sql[4 + offset] == 'x') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'S'
                || sql[6 + offset] == 's')) {
                return Keywords.INDEXES;
            }
            if (size == 12 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                && (sql[7 + offset] == '_' || sql[7 + offset] == '_') && (sql[8 + offset] == 'S'
                || sql[8 + offset] == 's') && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (
                sql[10 + offset] == 'Z' || sql[10 + offset] == 'z') && (sql[11 + offset] == 'E'
                || sql[11 + offset] == 'e')) {
                return Keywords.INITIAL_SIZE;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size == 13 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (
                    sql[6 + offset] == '_' || sql[6 + offset] == '_') && (sql[7 + offset] == 'M'
                    || sql[7 + offset] == 'm') && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (
                    sql[9 + offset] == 'T' || sql[9 + offset] == 't') && (sql[10 + offset] == 'H'
                    || sql[10 + offset] == 'h') && (sql[11 + offset] == 'O' || sql[11 + offset] == 'o') && (
                    sql[12 + offset] == 'D' || sql[12 + offset] == 'd')) {
                    return Keywords.INSERT_METHOD;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (size < 7 || size > 8) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') {
                        if (size == 7 && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'L'
                            || sql[6 + offset] == 'l')) {
                            return Keywords.INSTALL;
                        }
                        if (size == 8 && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'C'
                            || sql[6 + offset] == 'c') && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                            return Keywords.INSTANCE;
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'V' || sql[2 + offset] == 'v') {
                if (size < 7 || size > 9) {
                    return 0;
                }
                if (size == 9 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'S'
                    || sql[4 + offset] == 's') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (
                    sql[6 + offset] == 'B' || sql[6 + offset] == 'b') && (sql[7 + offset] == 'L'
                    || sql[7 + offset] == 'l') && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e')) {
                    return Keywords.INVISIBLE;
                }
                if (size == 7 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'K'
                    || sql[4 + offset] == 'k') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (
                    sql[6 + offset] == 'R' || sql[6 + offset] == 'r')) {
                    return Keywords.INVOKER;
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 2 || size > 9) {
                return 0;
            }
            if (size == 2) {
                return Keywords.IO;
            }
            if (size == 9 && (sql[2 + offset] == '_' || sql[2 + offset] == '_') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h') && (
                sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (sql[8 + offset] == 'D'
                || sql[8 + offset] == 'd')) {
                return Keywords.IO_THREAD;
            }
        }
        if (size == 3 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p') && (sql[2 + offset] == 'C'
            || sql[2 + offset] == 'c')) {
            return Keywords.IPC;
        }
        if (sql[1 + offset] == 'S' || sql[1 + offset] == 's') {
            if (size < 6 || size > 9) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (sql[8 + offset] == 'N'
                || sql[8 + offset] == 'n')) {
                return Keywords.ISOLATION;
            }
            if (size == 6 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'U'
                || sql[3 + offset] == 'u') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
                return Keywords.ISSUER;
            }
        }
        return 0;
    }

    private static int parseJ(byte[] sql, int offset, int size) {
        if (size != 4) {
            return 0;
        }
        if (size == 4 && (sql[1 + offset] == 'S' || sql[1 + offset] == 's') && (sql[2 + offset] == 'O'
            || sql[2 + offset] == 'o') && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n')) {
            return Keywords.JSON;
        }
        return 0;
    }

    private static int parseK(byte[] sql, int offset, int size) {
        if (size != 14) {
            return 0;
        }
        if (size == 14 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'Y'
            || sql[2 + offset] == 'y') && (sql[3 + offset] == '_' || sql[3 + offset] == '_') && (sql[4 + offset] == 'B'
            || sql[4 + offset] == 'b') && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'O'
            || sql[6 + offset] == 'o') && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (sql[8 + offset] == 'K'
            || sql[8 + offset] == 'k') && (sql[9 + offset] == '_' || sql[9 + offset] == '_') && (sql[10 + offset] == 'S'
            || sql[10 + offset] == 's') && (sql[11 + offset] == 'I' || sql[11 + offset] == 'i') && (
            sql[12 + offset] == 'Z' || sql[12 + offset] == 'z') && (sql[13 + offset] == 'E'
            || sql[13 + offset] == 'e')) {
            return Keywords.KEY_BLOCK_SIZE;
        }
        return 0;
    }

    private static int parseL(byte[] sql, int offset, int size) {
        if (size < 4 || size > 10) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 8) {
                return 0;
            }
            if (size == 8 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'G'
                || sql[3 + offset] == 'g') && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                return Keywords.LANGUAGE;
            }
            if (size == 4 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't')) {
                return Keywords.LAST;
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 4 || size > 6) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'V'
                || sql[3 + offset] == 'v') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                return Keywords.LEAVES;
            }
            if (size == 4 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's')) {
                return Keywords.LESS;
            }
            if (size == 5 && (sql[2 + offset] == 'V' || sql[2 + offset] == 'v') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')) {
                return Keywords.LEVEL;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size == 10 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'N'
                || sql[8 + offset] == 'n') && (sql[9 + offset] == 'G' || sql[9 + offset] == 'g')) {
                return Keywords.LINESTRING;
            }
            if (size == 4 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't')) {
                return Keywords.LIST;
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (size < 5 || size > 6) {
                    return 0;
                }
                if (size == 5 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'L'
                    || sql[4 + offset] == 'l')) {
                    return Keywords.LOCAL;
                }
                if (sql[3 + offset] == 'K' || sql[3 + offset] == 'k') {
                    if (size == 6 && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (sql[5 + offset] == 'D'
                        || sql[5 + offset] == 'd')) {
                        return Keywords.LOCKED;
                    }
                    if (size == 5 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                        return Keywords.LOCKS;
                    }
                }
            }
            if (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') {
                if (size == 7 && (sql[3 + offset] == 'F' || sql[3 + offset] == 'f') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Keywords.LOGFILE;
                }
                if (size == 4 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's')) {
                    return Keywords.LOGS;
                }
            }
        }
        return 0;
    }

    private static int parseM(byte[] sql, int offset, int size) {
        if (size < 4 || size > 24) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 6 || size > 24) {
                return 0;
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 6 || size > 23) {
                    return 0;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') {
                            if (size == 6) {
                                return Keywords.MASTER;
                            }
                            if (sql[6 + offset] == '_' || sql[6 + offset] == '_') {
                                if (size < 10 || size > 23) {
                                    return 0;
                                }
                                if (size == 20 && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (
                                    sql[8 + offset] == 'U' || sql[8 + offset] == 'u') && (sql[9 + offset] == 'T'
                                    || sql[9 + offset] == 't') && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                                    && (sql[11 + offset] == '_' || sql[11 + offset] == '_') && (sql[12 + offset] == 'P'
                                    || sql[12 + offset] == 'p') && (sql[13 + offset] == 'O' || sql[13 + offset] == 'o')
                                    && (sql[14 + offset] == 'S' || sql[14 + offset] == 's') && (sql[15 + offset] == 'I'
                                    || sql[15 + offset] == 'i') && (sql[16 + offset] == 'T' || sql[16 + offset] == 't')
                                    && (sql[17 + offset] == 'I' || sql[17 + offset] == 'i') && (sql[18 + offset] == 'O'
                                    || sql[18 + offset] == 'o') && (sql[19 + offset] == 'N'
                                    || sql[19 + offset] == 'n')) {
                                    return Keywords.MASTER_AUTO_POSITION;
                                }
                                if (size == 20 && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (
                                    sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N'
                                    || sql[9 + offset] == 'n') && (sql[10 + offset] == 'N' || sql[10 + offset] == 'n')
                                    && (sql[11 + offset] == 'E' || sql[11 + offset] == 'e') && (sql[12 + offset] == 'C'
                                    || sql[12 + offset] == 'c') && (sql[13 + offset] == 'T' || sql[13 + offset] == 't')
                                    && (sql[14 + offset] == '_' || sql[14 + offset] == '_') && (sql[15 + offset] == 'R'
                                    || sql[15 + offset] == 'r') && (sql[16 + offset] == 'E' || sql[16 + offset] == 'e')
                                    && (sql[17 + offset] == 'T' || sql[17 + offset] == 't') && (sql[18 + offset] == 'R'
                                    || sql[18 + offset] == 'r') && (sql[19 + offset] == 'Y'
                                    || sql[19 + offset] == 'y')) {
                                    return Keywords.MASTER_CONNECT_RETRY;
                                }
                                if (size == 12 && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd') && (
                                    sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (sql[9 + offset] == 'L'
                                    || sql[9 + offset] == 'l') && (sql[10 + offset] == 'A' || sql[10 + offset] == 'a')
                                    && (sql[11 + offset] == 'Y' || sql[11 + offset] == 'y')) {
                                    return Keywords.MASTER_DELAY;
                                }
                                if (sql[7 + offset] == 'H' || sql[7 + offset] == 'h') {
                                    if (size < 11 || size > 23) {
                                        return 0;
                                    }
                                    if (size == 23 && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (
                                        sql[9 + offset] == 'A' || sql[9 + offset] == 'a') && (sql[10 + offset] == 'R'
                                        || sql[10 + offset] == 'r') && (sql[11 + offset] == 'T'
                                        || sql[11 + offset] == 't') && (sql[12 + offset] == 'B'
                                        || sql[12 + offset] == 'b') && (sql[13 + offset] == 'E'
                                        || sql[13 + offset] == 'e') && (sql[14 + offset] == 'A'
                                        || sql[14 + offset] == 'a') && (sql[15 + offset] == 'T'
                                        || sql[15 + offset] == 't') && (sql[16 + offset] == '_'
                                        || sql[16 + offset] == '_') && (sql[17 + offset] == 'P'
                                        || sql[17 + offset] == 'p') && (sql[18 + offset] == 'E'
                                        || sql[18 + offset] == 'e') && (sql[19 + offset] == 'R'
                                        || sql[19 + offset] == 'r') && (sql[20 + offset] == 'I'
                                        || sql[20 + offset] == 'i') && (sql[21 + offset] == 'O'
                                        || sql[21 + offset] == 'o') && (sql[22 + offset] == 'D'
                                        || sql[22 + offset] == 'd')) {
                                        return Keywords.MASTER_HEARTBEAT_PERIOD;
                                    }
                                    if (size == 11 && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (
                                        sql[9 + offset] == 'S' || sql[9 + offset] == 's') && (sql[10 + offset] == 'T'
                                        || sql[10 + offset] == 't')) {
                                        return Keywords.MASTER_HOST;
                                    }
                                }
                                if (sql[7 + offset] == 'L' || sql[7 + offset] == 'l') {
                                    if (size < 14 || size > 15) {
                                        return 0;
                                    }
                                    if (sql[8 + offset] == 'O' || sql[8 + offset] == 'o') {
                                        if (sql[9 + offset] == 'G' || sql[9 + offset] == 'g') {
                                            if (sql[10 + offset] == '_' || sql[10 + offset] == '_') {
                                                if (size == 15 && (sql[11 + offset] == 'F' || sql[11 + offset] == 'f')
                                                    && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i') && (
                                                    sql[13 + offset] == 'L' || sql[13 + offset] == 'l') && (
                                                    sql[14 + offset] == 'E' || sql[14 + offset] == 'e')) {
                                                    return Keywords.MASTER_LOG_FILE;
                                                }
                                                if (size == 14 && (sql[11 + offset] == 'P' || sql[11 + offset] == 'p')
                                                    && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o') && (
                                                    sql[13 + offset] == 'S' || sql[13 + offset] == 's')) {
                                                    return Keywords.MASTER_LOG_POS;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (sql[7 + offset] == 'P' || sql[7 + offset] == 'p') {
                                    if (size < 11 || size > 22) {
                                        return 0;
                                    }
                                    if (size == 15 && (sql[8 + offset] == 'A' || sql[8 + offset] == 'a') && (
                                        sql[9 + offset] == 'S' || sql[9 + offset] == 's') && (sql[10 + offset] == 'S'
                                        || sql[10 + offset] == 's') && (sql[11 + offset] == 'W'
                                        || sql[11 + offset] == 'w') && (sql[12 + offset] == 'O'
                                        || sql[12 + offset] == 'o') && (sql[13 + offset] == 'R'
                                        || sql[13 + offset] == 'r') && (sql[14 + offset] == 'D'
                                        || sql[14 + offset] == 'd')) {
                                        return Keywords.MASTER_PASSWORD;
                                    }
                                    if (size == 11 && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (
                                        sql[9 + offset] == 'R' || sql[9 + offset] == 'r') && (sql[10 + offset] == 'T'
                                        || sql[10 + offset] == 't')) {
                                        return Keywords.MASTER_PORT;
                                    }
                                    if (size == 22 && (sql[8 + offset] == 'U' || sql[8 + offset] == 'u') && (
                                        sql[9 + offset] == 'B' || sql[9 + offset] == 'b') && (sql[10 + offset] == 'L'
                                        || sql[10 + offset] == 'l') && (sql[11 + offset] == 'I'
                                        || sql[11 + offset] == 'i') && (sql[12 + offset] == 'C'
                                        || sql[12 + offset] == 'c') && (sql[13 + offset] == '_'
                                        || sql[13 + offset] == '_') && (sql[14 + offset] == 'K'
                                        || sql[14 + offset] == 'k') && (sql[15 + offset] == 'E'
                                        || sql[15 + offset] == 'e') && (sql[16 + offset] == 'Y'
                                        || sql[16 + offset] == 'y') && (sql[17 + offset] == '_'
                                        || sql[17 + offset] == '_') && (sql[18 + offset] == 'P'
                                        || sql[18 + offset] == 'p') && (sql[19 + offset] == 'A'
                                        || sql[19 + offset] == 'a') && (sql[20 + offset] == 'T'
                                        || sql[20 + offset] == 't') && (sql[21 + offset] == 'H'
                                        || sql[21 + offset] == 'h')) {
                                        return Keywords.MASTER_PUBLIC_KEY_PATH;
                                    }
                                }
                                if (size == 18 && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (
                                    sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (sql[9 + offset] == 'T'
                                    || sql[9 + offset] == 't') && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r')
                                    && (sql[11 + offset] == 'Y' || sql[11 + offset] == 'y') && (sql[12 + offset] == '_'
                                    || sql[12 + offset] == '_') && (sql[13 + offset] == 'C' || sql[13 + offset] == 'c')
                                    && (sql[14 + offset] == 'O' || sql[14 + offset] == 'o') && (sql[15 + offset] == 'U'
                                    || sql[15 + offset] == 'u') && (sql[16 + offset] == 'N' || sql[16 + offset] == 'n')
                                    && (sql[17 + offset] == 'T' || sql[17 + offset] == 't')) {
                                    return Keywords.MASTER_RETRY_COUNT;
                                }
                                if (sql[7 + offset] == 'S' || sql[7 + offset] == 's') {
                                    if (size < 10 || size > 18) {
                                        return 0;
                                    }
                                    if (size == 16 && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (
                                        sql[9 + offset] == 'R' || sql[9 + offset] == 'r') && (sql[10 + offset] == 'V'
                                        || sql[10 + offset] == 'v') && (sql[11 + offset] == 'E'
                                        || sql[11 + offset] == 'e') && (sql[12 + offset] == 'R'
                                        || sql[12 + offset] == 'r') && (sql[13 + offset] == '_'
                                        || sql[13 + offset] == '_') && (sql[14 + offset] == 'I'
                                        || sql[14 + offset] == 'i') && (sql[15 + offset] == 'D'
                                        || sql[15 + offset] == 'd')) {
                                        return Keywords.MASTER_SERVER_ID;
                                    }
                                    if (sql[8 + offset] == 'S' || sql[8 + offset] == 's') {
                                        if (sql[9 + offset] == 'L' || sql[9 + offset] == 'l') {
                                            if (size == 10) {
                                                return Keywords.MASTER_SSL;
                                            }
                                            if (sql[10 + offset] == '_' || sql[10 + offset] == '_') {
                                                if (size < 13 || size > 18) {
                                                    return 0;
                                                }
                                                if (sql[11 + offset] == 'C' || sql[11 + offset] == 'c') {
                                                    if (sql[12 + offset] == 'A' || sql[12 + offset] == 'a') {
                                                        if (size < 13 || size > 17) {
                                                            return 0;
                                                        }
                                                        if (size == 13) {
                                                            return Keywords.MASTER_SSL_CA;
                                                        }
                                                        if (size == 17 && (sql[13 + offset] == 'P'
                                                            || sql[13 + offset] == 'p') && (sql[14 + offset] == 'A'
                                                            || sql[14 + offset] == 'a') && (sql[15 + offset] == 'T'
                                                            || sql[15 + offset] == 't') && (sql[16 + offset] == 'H'
                                                            || sql[16 + offset] == 'h')) {
                                                            return Keywords.MASTER_SSL_CAPATH;
                                                        }
                                                    }
                                                    if (size == 15 && (sql[12 + offset] == 'E'
                                                        || sql[12 + offset] == 'e') && (sql[13 + offset] == 'R'
                                                        || sql[13 + offset] == 'r') && (sql[14 + offset] == 'T'
                                                        || sql[14 + offset] == 't')) {
                                                        return Keywords.MASTER_SSL_CERT;
                                                    }
                                                    if (size == 17 && (sql[12 + offset] == 'I'
                                                        || sql[12 + offset] == 'i') && (sql[13 + offset] == 'P'
                                                        || sql[13 + offset] == 'p') && (sql[14 + offset] == 'H'
                                                        || sql[14 + offset] == 'h') && (sql[15 + offset] == 'E'
                                                        || sql[15 + offset] == 'e') && (sql[16 + offset] == 'R'
                                                        || sql[16 + offset] == 'r')) {
                                                        return Keywords.MASTER_SSL_CIPHER;
                                                    }
                                                    if (sql[12 + offset] == 'R' || sql[12 + offset] == 'r') {
                                                        if (size < 14 || size > 18) {
                                                            return 0;
                                                        }
                                                        if (sql[13 + offset] == 'L' || sql[13 + offset] == 'l') {
                                                            if (size == 14) {
                                                                return Keywords.MASTER_SSL_CRL;
                                                            }
                                                            if (size == 18 && (sql[14 + offset] == 'P'
                                                                || sql[14 + offset] == 'p') && (sql[15 + offset] == 'A'
                                                                || sql[15 + offset] == 'a') && (sql[16 + offset] == 'T'
                                                                || sql[16 + offset] == 't') && (sql[17 + offset] == 'H'
                                                                || sql[17 + offset] == 'h')) {
                                                                return Keywords.MASTER_SSL_CRLPATH;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (size == 14 && (sql[11 + offset] == 'K' || sql[11 + offset] == 'k')
                                                    && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e') && (
                                                    sql[13 + offset] == 'Y' || sql[13 + offset] == 'y')) {
                                                    return Keywords.MASTER_SSL_KEY;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (size == 18 && (sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (
                                    sql[8 + offset] == 'L' || sql[8 + offset] == 'l') && (sql[9 + offset] == 'S'
                                    || sql[9 + offset] == 's') && (sql[10 + offset] == '_' || sql[10 + offset] == '_')
                                    && (sql[11 + offset] == 'V' || sql[11 + offset] == 'v') && (sql[12 + offset] == 'E'
                                    || sql[12 + offset] == 'e') && (sql[13 + offset] == 'R' || sql[13 + offset] == 'r')
                                    && (sql[14 + offset] == 'S' || sql[14 + offset] == 's') && (sql[15 + offset] == 'I'
                                    || sql[15 + offset] == 'i') && (sql[16 + offset] == 'O' || sql[16 + offset] == 'o')
                                    && (sql[17 + offset] == 'N' || sql[17 + offset] == 'n')) {
                                    return Keywords.MASTER_TLS_VERSION;
                                }
                                if (size == 11 && (sql[7 + offset] == 'U' || sql[7 + offset] == 'u') && (
                                    sql[8 + offset] == 'S' || sql[8 + offset] == 's') && (sql[9 + offset] == 'E'
                                    || sql[9 + offset] == 'e') && (sql[10 + offset] == 'R'
                                    || sql[10 + offset] == 'r')) {
                                    return Keywords.MASTER_USER;
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'X' || sql[2 + offset] == 'x') {
                if (size < 8 || size > 24) {
                    return 0;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 24 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (
                        sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'E'
                        || sql[8 + offset] == 'e') && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') && (
                        sql[10 + offset] == 'T' || sql[10 + offset] == 't') && (sql[11 + offset] == 'I'
                        || sql[11 + offset] == 'i') && (sql[12 + offset] == 'O' || sql[12 + offset] == 'o') && (
                        sql[13 + offset] == 'N' || sql[13 + offset] == 'n') && (sql[14 + offset] == 'S'
                        || sql[14 + offset] == 's') && (sql[15 + offset] == '_' || sql[15 + offset] == '_') && (
                        sql[16 + offset] == 'P' || sql[16 + offset] == 'p') && (sql[17 + offset] == 'E'
                        || sql[17 + offset] == 'e') && (sql[18 + offset] == 'R' || sql[18 + offset] == 'r') && (
                        sql[19 + offset] == '_' || sql[19 + offset] == '_') && (sql[20 + offset] == 'H'
                        || sql[20 + offset] == 'h') && (sql[21 + offset] == 'O' || sql[21 + offset] == 'o') && (
                        sql[22 + offset] == 'U' || sql[22 + offset] == 'u') && (sql[23 + offset] == 'R'
                        || sql[23 + offset] == 'r')) {
                        return Keywords.MAX_CONNECTIONS_PER_HOUR;
                    }
                    if (size == 20 && (sql[4 + offset] == 'Q' || sql[4 + offset] == 'q') && (sql[5 + offset] == 'U'
                        || sql[5 + offset] == 'u') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') && (
                        sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (sql[8 + offset] == 'I'
                        || sql[8 + offset] == 'i') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (
                        sql[10 + offset] == 'S' || sql[10 + offset] == 's') && (sql[11 + offset] == '_'
                        || sql[11 + offset] == '_') && (sql[12 + offset] == 'P' || sql[12 + offset] == 'p') && (
                        sql[13 + offset] == 'E' || sql[13 + offset] == 'e') && (sql[14 + offset] == 'R'
                        || sql[14 + offset] == 'r') && (sql[15 + offset] == '_' || sql[15 + offset] == '_') && (
                        sql[16 + offset] == 'H' || sql[16 + offset] == 'h') && (sql[17 + offset] == 'O'
                        || sql[17 + offset] == 'o') && (sql[18 + offset] == 'U' || sql[18 + offset] == 'u') && (
                        sql[19 + offset] == 'R' || sql[19 + offset] == 'r')) {
                        return Keywords.MAX_QUERIES_PER_HOUR;
                    }
                    if (size == 8 && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'W' || sql[6 + offset] == 'w') && (
                        sql[7 + offset] == 'S' || sql[7 + offset] == 's')) {
                        return Keywords.MAX_ROWS;
                    }
                    if (size == 8 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'Z' || sql[6 + offset] == 'z') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Keywords.MAX_SIZE;
                    }
                    if (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') {
                        if (size != 20) {
                            return 0;
                        }
                        if (size == 20 && (sql[5 + offset] == 'P' || sql[5 + offset] == 'p') && (sql[6 + offset] == 'D'
                            || sql[6 + offset] == 'd') && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (
                            sql[8 + offset] == 'T' || sql[8 + offset] == 't') && (sql[9 + offset] == 'E'
                            || sql[9 + offset] == 'e') && (sql[10 + offset] == 'S' || sql[10 + offset] == 's') && (
                            sql[11 + offset] == '_' || sql[11 + offset] == '_') && (sql[12 + offset] == 'P'
                            || sql[12 + offset] == 'p') && (sql[13 + offset] == 'E' || sql[13 + offset] == 'e') && (
                            sql[14 + offset] == 'R' || sql[14 + offset] == 'r') && (sql[15 + offset] == '_'
                            || sql[15 + offset] == '_') && (sql[16 + offset] == 'H' || sql[16 + offset] == 'h') && (
                            sql[17 + offset] == 'O' || sql[17 + offset] == 'o') && (sql[18 + offset] == 'U'
                            || sql[18 + offset] == 'u') && (sql[19 + offset] == 'R' || sql[19 + offset] == 'r')) {
                            return Keywords.MAX_UPDATES_PER_HOUR;
                        }
                        if (size == 20 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E'
                            || sql[6 + offset] == 'e') && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (
                            sql[8 + offset] == '_' || sql[8 + offset] == '_') && (sql[9 + offset] == 'C'
                            || sql[9 + offset] == 'c') && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o') && (
                            sql[11 + offset] == 'N' || sql[11 + offset] == 'n') && (sql[12 + offset] == 'N'
                            || sql[12 + offset] == 'n') && (sql[13 + offset] == 'E' || sql[13 + offset] == 'e') && (
                            sql[14 + offset] == 'C' || sql[14 + offset] == 'c') && (sql[15 + offset] == 'T'
                            || sql[15 + offset] == 't') && (sql[16 + offset] == 'I' || sql[16 + offset] == 'i') && (
                            sql[17 + offset] == 'O' || sql[17 + offset] == 'o') && (sql[18 + offset] == 'N'
                            || sql[18 + offset] == 'n') && (sql[19 + offset] == 'S' || sql[19 + offset] == 's')) {
                            return Keywords.MAX_USER_CONNECTIONS;
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 5 || size > 12) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (
                sql[5 + offset] == 'M' || sql[5 + offset] == 'm')) {
                return Keywords.MEDIUM;
            }
            if (size == 6 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'Y' || sql[5 + offset] == 'y')) {
                return Keywords.MEMORY;
            }
            if (size == 5 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'G'
                || sql[3 + offset] == 'g') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Keywords.MERGE;
            }
            if (size == 12 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'G' || sql[5 + offset] == 'g') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == '_' || sql[7 + offset] == '_') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (
                sql[10 + offset] == 'X' || sql[10 + offset] == 'x') && (sql[11 + offset] == 'T'
                || sql[11 + offset] == 't')) {
                return Keywords.MESSAGE_TEXT;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 6 || size > 11) {
                return 0;
            }
            if (size == 11 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (sql[8 + offset] == 'O'
                || sql[8 + offset] == 'o') && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n') && (
                sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                return Keywords.MICROSECOND;
            }
            if (size == 7 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Keywords.MIGRATE;
            }
            if (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') {
                if (size < 6 || size > 8) {
                    return 0;
                }
                if (size == 6 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') && (sql[4 + offset] == 'T'
                    || sql[4 + offset] == 't') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Keywords.MINUTE;
                }
                if (size == 8 && (sql[3 + offset] == '_' || sql[3 + offset] == '_') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r') && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (
                    sql[6 + offset] == 'W' || sql[6 + offset] == 'w') && (sql[7 + offset] == 'S'
                    || sql[7 + offset] == 's')) {
                    return Keywords.MIN_ROWS;
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 6) {
                return 0;
            }
            if (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') {
                if (size == 4 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')) {
                    return Keywords.MODE;
                }
                if (size == 6 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'F'
                    || sql[4 + offset] == 'f') && (sql[5 + offset] == 'Y' || sql[5 + offset] == 'y')) {
                    return Keywords.MODIFY;
                }
            }
            if (size == 5 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h')) {
                return Keywords.MONTH;
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 5 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size < 10 || size > 15) {
                    return 0;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (size == 15 && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'I'
                            || sql[6 + offset] == 'i') && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (
                            sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (sql[9 + offset] == 'S'
                            || sql[9 + offset] == 's') && (sql[10 + offset] == 'T' || sql[10 + offset] == 't') && (
                            sql[11 + offset] == 'R' || sql[11 + offset] == 'r') && (sql[12 + offset] == 'I'
                            || sql[12 + offset] == 'i') && (sql[13 + offset] == 'N' || sql[13 + offset] == 'n') && (
                            sql[14 + offset] == 'G' || sql[14 + offset] == 'g')) {
                            return Keywords.MULTILINESTRING;
                        }
                        if (sql[5 + offset] == 'P' || sql[5 + offset] == 'p') {
                            if (size < 10 || size > 12) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'O' || sql[6 + offset] == 'o') {
                                if (size == 10 && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (
                                    sql[8 + offset] == 'N' || sql[8 + offset] == 'n') && (sql[9 + offset] == 'T'
                                    || sql[9 + offset] == 't')) {
                                    return Keywords.MULTIPOINT;
                                }
                                if (size == 12 && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (
                                    sql[8 + offset] == 'Y' || sql[8 + offset] == 'y') && (sql[9 + offset] == 'G'
                                    || sql[9 + offset] == 'g') && (sql[10 + offset] == 'O' || sql[10 + offset] == 'o')
                                    && (sql[11 + offset] == 'N' || sql[11 + offset] == 'n')) {
                                    return Keywords.MULTIPOLYGON;
                                }
                            }
                        }
                    }
                }
            }
            if (size == 5 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'X' || sql[4 + offset] == 'x')) {
                return Keywords.MUTEX;
            }
        }
        if (size == 11 && (sql[1 + offset] == 'Y' || sql[1 + offset] == 'y') && (sql[2 + offset] == 'S'
            || sql[2 + offset] == 's') && (sql[3 + offset] == 'Q' || sql[3 + offset] == 'q') && (sql[4 + offset] == 'L'
            || sql[4 + offset] == 'l') && (sql[5 + offset] == '_' || sql[5 + offset] == '_') && (sql[6 + offset] == 'E'
            || sql[6 + offset] == 'e') && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (sql[8 + offset] == 'R'
            || sql[8 + offset] == 'r') && (sql[9 + offset] == 'N' || sql[9 + offset] == 'n') && (sql[10 + offset] == 'O'
            || sql[10 + offset] == 'o')) {
            return Keywords.MYSQL_ERRNO;
        }
        return 0;
    }

    private static int parseN(byte[] sql, int offset, int size) {
        if (size < 2 || size > 10) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 8) {
                return 0;
            }
            if (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') {
                if (size < 4 || size > 5) {
                    return 0;
                }
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 4) {
                        return Keywords.NAME;
                    }
                    if (size == 5 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                        return Keywords.NAMES;
                    }
                }
            }
            if (size == 8 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (
                sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a')
                && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l')) {
                return Keywords.NATIONAL;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c') && (sql[2 + offset] == 'H'
            || sql[2 + offset] == 'h') && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'R'
            || sql[4 + offset] == 'r')) {
            return Keywords.NCHAR;
        }
        if (sql[1 + offset] == 'D' || sql[1 + offset] == 'd') {
            if (size < 3 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'B' || sql[2 + offset] == 'b') {
                if (size == 3) {
                    return Keywords.NDB;
                }
                if (size == 10 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'L'
                    || sql[4 + offset] == 'l') && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u') && (
                    sql[6 + offset] == 'S' || sql[6 + offset] == 's') && (sql[7 + offset] == 'T'
                    || sql[7 + offset] == 't') && (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (
                    sql[9 + offset] == 'R' || sql[9 + offset] == 'r')) {
                    return Keywords.NDBCLUSTER;
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 3 || size > 6) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'D' || sql[5 + offset] == 'd')) {
                return Keywords.NESTED;
            }
            if (size == 5 && (sql[2 + offset] == 'V' || sql[2 + offset] == 'v') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
                return Keywords.NEVER;
            }
            if (size == 3 && (sql[2 + offset] == 'W' || sql[2 + offset] == 'w')) {
                return Keywords.NEW;
            }
            if (size == 4 && (sql[2 + offset] == 'X' || sql[2 + offset] == 'x') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't')) {
                return Keywords.NEXT;
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 2 || size > 9) {
                return 0;
            }
            if (size == 2) {
                return Keywords.NO;
            }
            if (size == 9 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g') && (
                sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (sql[6 + offset] == 'O' || sql[6 + offset] == 'o')
                && (sql[7 + offset] == 'U' || sql[7 + offset] == 'u') && (sql[8 + offset] == 'P'
                || sql[8 + offset] == 'p')) {
                return Keywords.NODEGROUP;
            }
            if (size == 4 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e')) {
                return Keywords.NONE;
            }
            if (size == 6 && (sql[2 + offset] == 'W' || sql[2 + offset] == 'w') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                return Keywords.NOWAIT;
            }
            if (size == 7 && (sql[2 + offset] == '_' || sql[2 + offset] == '_') && (sql[3 + offset] == 'W'
                || sql[3 + offset] == 'w') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'T'
                || sql[6 + offset] == 't')) {
                return Keywords.NO_WAIT;
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 5 || size > 6) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'L'
                || sql[3 + offset] == 'l') && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                return Keywords.NULLS;
            }
            if (size == 6 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'B'
                || sql[3 + offset] == 'b') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
                return Keywords.NUMBER;
            }
        }
        if (size == 8 && (sql[1 + offset] == 'V' || sql[1 + offset] == 'v') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'C'
            || sql[4 + offset] == 'c') && (sql[5 + offset] == 'H' || sql[5 + offset] == 'h') && (sql[6 + offset] == 'A'
            || sql[6 + offset] == 'a') && (sql[7 + offset] == 'R' || sql[7 + offset] == 'r')) {
            return Keywords.NVARCHAR;
        }
        return 0;
    }

    private static int parseO(byte[] sql, int offset, int size) {
        if (size < 3 || size > 12) {
            return 0;
        }
        if (size == 6 && (sql[1 + offset] == 'F' || sql[1 + offset] == 'f') && (sql[2 + offset] == 'F'
            || sql[2 + offset] == 'f') && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
            return Keywords.OFFSET;
        }
        if (size == 3 && (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') && (sql[2 + offset] == 'D'
            || sql[2 + offset] == 'd')) {
            return Keywords.OLD;
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 3 || size > 4) {
                return 0;
            }
            if (size == 3 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e')) {
                return Keywords.ONE;
            }
            if (size == 4 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'Y'
                || sql[3 + offset] == 'y')) {
                return Keywords.ONLY;
            }
        }
        if (sql[1 + offset] == 'P' || sql[1 + offset] == 'p') {
            if (size < 4 || size > 8) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n')) {
                return Keywords.OPEN;
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 7 || size > 8) {
                    return 0;
                }
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') {
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (size == 8 && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (
                                sql[7 + offset] == 'L' || sql[7 + offset] == 'l')) {
                                return Keywords.OPTIONAL;
                            }
                            if (size == 7 && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                                return Keywords.OPTIONS;
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 10 || size > 12) {
                return 0;
            }
            if (size == 10 && (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't') && (sql[9 + offset] == 'Y' || sql[9 + offset] == 'y')) {
                return Keywords.ORDINALITY;
            }
            if (size == 12 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (
                sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (sql[6 + offset] == 'Z' || sql[6 + offset] == 'z')
                && (sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't') && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (
                sql[10 + offset] == 'O' || sql[10 + offset] == 'o') && (sql[11 + offset] == 'N'
                || sql[11 + offset] == 'n')) {
                return Keywords.ORGANIZATION;
            }
        }
        if (size == 6 && (sql[1 + offset] == 'T' || sql[1 + offset] == 't') && (sql[2 + offset] == 'H'
            || sql[2 + offset] == 'h') && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R'
            || sql[4 + offset] == 'r') && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
            return Keywords.OTHERS;
        }
        if (size == 5 && (sql[1 + offset] == 'W' || sql[1 + offset] == 'w') && (sql[2 + offset] == 'N'
            || sql[2 + offset] == 'n') && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R'
            || sql[4 + offset] == 'r')) {
            return Keywords.OWNER;
        }
        return 0;
    }

    private static int parseP(byte[] sql, int offset, int size) {
        if (size < 4 || size > 12) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size == 9 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'K'
                || sql[3 + offset] == 'k') && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (
                sql[5 + offset] == 'K' || sql[5 + offset] == 'k') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == 'Y' || sql[7 + offset] == 'y') && (sql[8 + offset] == 'S'
                || sql[8 + offset] == 's')) {
                return Keywords.PACK_KEYS;
            }
            if (size == 4 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e')) {
                return Keywords.PAGE;
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size < 6 || size > 12) {
                    return 0;
                }
                if (size == 6 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
                    return Keywords.PARSER;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (size < 7 || size > 12) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (size == 7 && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'L'
                            || sql[6 + offset] == 'l')) {
                            return Keywords.PARTIAL;
                        }
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (size < 10 || size > 12) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') {
                                if (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') {
                                    if (sql[8 + offset] == 'N' || sql[8 + offset] == 'n') {
                                        if (size == 12 && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (
                                            sql[10 + offset] == 'N' || sql[10 + offset] == 'n') && (
                                            sql[11 + offset] == 'G' || sql[11 + offset] == 'g')) {
                                            return Keywords.PARTITIONING;
                                        }
                                        if (size == 10 && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')) {
                                            return Keywords.PARTITIONS;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (size == 8 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'W' || sql[4 + offset] == 'w') && (
                sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                && (sql[7 + offset] == 'D' || sql[7 + offset] == 'd')) {
                return Keywords.PASSWORD;
            }
            if (size == 4 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'H'
                || sql[3 + offset] == 'h')) {
                return Keywords.PATH;
            }
        }
        if (size == 5 && (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e')) {
            return Keywords.PHASE;
        }
        if (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') {
            if (size < 6 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') {
                if (sql[3 + offset] == 'G' || sql[3 + offset] == 'g') {
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (size == 6) {
                                return Keywords.PLUGIN;
                            }
                            if (size == 7 && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                                return Keywords.PLUGINS;
                            }
                            if (size == 10 && (sql[6 + offset] == '_' || sql[6 + offset] == '_') && (
                                sql[7 + offset] == 'D' || sql[7 + offset] == 'd') && (sql[8 + offset] == 'I'
                                || sql[8 + offset] == 'i') && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r')) {
                                return Keywords.PLUGIN_DIR;
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't')) {
                return Keywords.POINT;
            }
            if (size == 7 && (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') && (sql[3 + offset] == 'Y'
                || sql[3 + offset] == 'y') && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g') && (
                sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'N'
                || sql[6 + offset] == 'n')) {
                return Keywords.POLYGON;
            }
            if (size == 4 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't')) {
                return Keywords.PORT;
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 4 || size > 11) {
                return 0;
            }
            if (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') {
                if (size < 4 || size > 9) {
                    return 0;
                }
                if (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') {
                    if (size < 8 || size > 9) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') {
                            if (size == 8 && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') && (
                                sql[7 + offset] == 'S' || sql[7 + offset] == 's')) {
                                return Keywords.PRECEDES;
                            }
                            if (size == 9 && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') && (
                                sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'G'
                                || sql[8 + offset] == 'g')) {
                                return Keywords.PRECEDING;
                            }
                        }
                    }
                }
                if (size == 7 && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Keywords.PREPARE;
                }
                if (size == 8 && (sql[3 + offset] == 'S' || sql[3 + offset] == 's') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'V' || sql[6 + offset] == 'v') && (sql[7 + offset] == 'E'
                    || sql[7 + offset] == 'e')) {
                    return Keywords.PRESERVE;
                }
                if (size == 4 && (sql[3 + offset] == 'V' || sql[3 + offset] == 'v')) {
                    return Keywords.PREV;
                }
            }
            if (size == 10 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'V'
                || sql[3 + offset] == 'v') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e') && (sql[9 + offset] == 'S' || sql[9 + offset] == 's')) {
                return Keywords.PRIVILEGES;
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (size < 5 || size > 11) {
                    return 0;
                }
                if (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') {
                    if (size < 7 || size > 11) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'S' || sql[5 + offset] == 's') {
                            if (sql[6 + offset] == 'S' || sql[6 + offset] == 's') {
                                if (size == 7) {
                                    return Keywords.PROCESS;
                                }
                                if (size == 11 && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (
                                    sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (sql[9 + offset] == 'S'
                                    || sql[9 + offset] == 's') && (sql[10 + offset] == 'T'
                                    || sql[10 + offset] == 't')) {
                                    return Keywords.PROCESSLIST;
                                }
                            }
                        }
                    }
                }
                if (sql[3 + offset] == 'F' || sql[3 + offset] == 'f') {
                    if (size < 7 || size > 8) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') {
                            if (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') {
                                if (size == 7) {
                                    return Keywords.PROFILE;
                                }
                                if (size == 8 && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')) {
                                    return Keywords.PROFILES;
                                }
                            }
                        }
                    }
                }
                if (size == 5 && (sql[3 + offset] == 'X' || sql[3 + offset] == 'x') && (sql[4 + offset] == 'Y'
                    || sql[4 + offset] == 'y')) {
                    return Keywords.PROXY;
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
            if (size == 7 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R'
                || sql[6 + offset] == 'r')) {
                return Keywords.QUARTER;
            }
            if (size == 5 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y')) {
                return Keywords.QUERY;
            }
            if (size == 5 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'C'
                || sql[3 + offset] == 'c') && (sql[4 + offset] == 'K' || sql[4 + offset] == 'k')) {
                return Keywords.QUICK;
            }
        }
        return 0;
    }

    private static int parseR(byte[] sql, int offset, int size) {
        if (size < 4 || size > 27) {
            return 0;
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 5 || size > 27) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'D'
                || sql[3 + offset] == 'd') && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (
                sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                && (sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (sql[8 + offset] == 'Y'
                || sql[8 + offset] == 'y')) {
                return Keywords.READ_ONLY;
            }
            if (size == 7 && (sql[2 + offset] == 'B' || sql[2 + offset] == 'b') && (sql[3 + offset] == 'U'
                || sql[3 + offset] == 'u') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'D'
                || sql[6 + offset] == 'd')) {
                return Keywords.REBUILD;
            }
            if (size == 7 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R'
                || sql[6 + offset] == 'r')) {
                return Keywords.RECOVER;
            }
            if (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') {
                if (size < 8 || size > 16) {
                    return 0;
                }
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (size == 8 && (sql[4 + offset] == 'F' || sql[4 + offset] == 'f') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Keywords.REDOFILE;
                    }
                    if (size == 16 && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (sql[5 + offset] == 'B'
                        || sql[5 + offset] == 'b') && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u') && (
                        sql[7 + offset] == 'F' || sql[7 + offset] == 'f') && (sql[8 + offset] == 'F'
                        || sql[8 + offset] == 'f') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (
                        sql[10 + offset] == 'R' || sql[10 + offset] == 'r') && (sql[11 + offset] == '_'
                        || sql[11 + offset] == '_') && (sql[12 + offset] == 'S' || sql[12 + offset] == 's') && (
                        sql[13 + offset] == 'I' || sql[13 + offset] == 'i') && (sql[14 + offset] == 'Z'
                        || sql[14 + offset] == 'z') && (sql[15 + offset] == 'E' || sql[15 + offset] == 'e')) {
                        return Keywords.REDO_BUFFER_SIZE;
                    }
                }
                if (size == 9 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') && (sql[4 + offset] == 'N'
                    || sql[4 + offset] == 'n') && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (
                    sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (sql[7 + offset] == 'N'
                    || sql[7 + offset] == 'n') && (sql[8 + offset] == 'T' || sql[8 + offset] == 't')) {
                    return Keywords.REDUNDANT;
                }
            }
            if (size == 9 && (sql[2 + offset] == 'F' || sql[2 + offset] == 'f') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                && (sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (sql[8 + offset] == 'E'
                || sql[8 + offset] == 'e')) {
                return Keywords.REFERENCE;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size < 5 || size > 14) {
                    return 0;
                }
                if (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') {
                    if (sql[4 + offset] == 'Y' || sql[4 + offset] == 'y') {
                        if (size == 5) {
                            return Keywords.RELAY;
                        }
                        if (size == 8 && (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'O'
                            || sql[6 + offset] == 'o') && (sql[7 + offset] == 'G' || sql[7 + offset] == 'g')) {
                            return Keywords.RELAYLOG;
                        }
                        if (sql[5 + offset] == '_' || sql[5 + offset] == '_') {
                            if (size < 12 || size > 14) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'L' || sql[6 + offset] == 'l') {
                                if (size < 13 || size > 14) {
                                    return 0;
                                }
                                if (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') {
                                    if (sql[8 + offset] == 'G' || sql[8 + offset] == 'g') {
                                        if (sql[9 + offset] == '_' || sql[9 + offset] == '_') {
                                            if (size == 14 && (sql[10 + offset] == 'F' || sql[10 + offset] == 'f') && (
                                                sql[11 + offset] == 'I' || sql[11 + offset] == 'i') && (
                                                sql[12 + offset] == 'L' || sql[12 + offset] == 'l') && (
                                                sql[13 + offset] == 'E' || sql[13 + offset] == 'e')) {
                                                return Keywords.RELAY_LOG_FILE;
                                            }
                                            if (size == 13 && (sql[10 + offset] == 'P' || sql[10 + offset] == 'p') && (
                                                sql[11 + offset] == 'O' || sql[11 + offset] == 'o') && (
                                                sql[12 + offset] == 'S' || sql[12 + offset] == 's')) {
                                                return Keywords.RELAY_LOG_POS;
                                            }
                                        }
                                    }
                                }
                            }
                            if (size == 12 && (sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (
                                sql[7 + offset] == 'H' || sql[7 + offset] == 'h') && (sql[8 + offset] == 'R'
                                || sql[8 + offset] == 'r') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (
                                sql[10 + offset] == 'A' || sql[10 + offset] == 'a') && (sql[11 + offset] == 'D'
                                || sql[11 + offset] == 'd')) {
                                return Keywords.RELAY_THREAD;
                            }
                        }
                    }
                }
                if (size == 6 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd')) {
                    return Keywords.RELOAD;
                }
            }
            if (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') {
                if (size != 6) {
                    return 0;
                }
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (size == 6 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e')) {
                        return Keywords.REMOTE;
                    }
                    if (size == 6 && (sql[4 + offset] == 'V' || sql[4 + offset] == 'v') && (sql[5 + offset] == 'E'
                        || sql[5 + offset] == 'e')) {
                        return Keywords.REMOVE;
                    }
                }
            }
            if (size == 10 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n')
                && (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') && (sql[8 + offset] == 'Z'
                || sql[8 + offset] == 'z') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                return Keywords.REORGANIZE;
            }
            if (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') {
                if (size < 6 || size > 27) {
                    return 0;
                }
                if (size == 6 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
                    return Keywords.REPAIR;
                }
                if (size == 10 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (
                    sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (sql[7 + offset] == 'B'
                    || sql[7 + offset] == 'b') && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l') && (
                    sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                    return Keywords.REPEATABLE;
                }
                if (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') {
                    if (size < 11 || size > 27) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') {
                        if (sql[5 + offset] == 'C' || sql[5 + offset] == 'c') {
                            if (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') {
                                if (sql[7 + offset] == 'T' || sql[7 + offset] == 't') {
                                    if (sql[8 + offset] == 'E' || sql[8 + offset] == 'e') {
                                        if (size < 15 || size > 27) {
                                            return 0;
                                        }
                                        if (sql[9 + offset] == '_' || sql[9 + offset] == '_') {
                                            if (sql[10 + offset] == 'D' || sql[10 + offset] == 'd') {
                                                if (size < 15 || size > 18) {
                                                    return 0;
                                                }
                                                if (sql[11 + offset] == 'O' || sql[11 + offset] == 'o') {
                                                    if (sql[12 + offset] == '_' || sql[12 + offset] == '_') {
                                                        if (size == 15 && (sql[13 + offset] == 'D'
                                                            || sql[13 + offset] == 'd') && (sql[14 + offset] == 'B'
                                                            || sql[14 + offset] == 'b')) {
                                                            return Keywords.REPLICATE_DO_DB;
                                                        }
                                                        if (size == 18 && (sql[13 + offset] == 'T'
                                                            || sql[13 + offset] == 't') && (sql[14 + offset] == 'A'
                                                            || sql[14 + offset] == 'a') && (sql[15 + offset] == 'B'
                                                            || sql[15 + offset] == 'b') && (sql[16 + offset] == 'L'
                                                            || sql[16 + offset] == 'l') && (sql[17 + offset] == 'E'
                                                            || sql[17 + offset] == 'e')) {
                                                            return Keywords.REPLICATE_DO_TABLE;
                                                        }
                                                    }
                                                }
                                            }
                                            if (sql[10 + offset] == 'I' || sql[10 + offset] == 'i') {
                                                if (size < 19 || size > 22) {
                                                    return 0;
                                                }
                                                if (sql[11 + offset] == 'G' || sql[11 + offset] == 'g') {
                                                    if (sql[12 + offset] == 'N' || sql[12 + offset] == 'n') {
                                                        if (sql[13 + offset] == 'O' || sql[13 + offset] == 'o') {
                                                            if (sql[14 + offset] == 'R' || sql[14 + offset] == 'r') {
                                                                if (sql[15 + offset] == 'E'
                                                                    || sql[15 + offset] == 'e') {
                                                                    if (sql[16 + offset] == '_'
                                                                        || sql[16 + offset] == '_') {
                                                                        if (size == 19 && (sql[17 + offset] == 'D'
                                                                            || sql[17 + offset] == 'd') && (
                                                                            sql[18 + offset] == 'B'
                                                                                || sql[18 + offset] == 'b')) {
                                                                            return Keywords.REPLICATE_IGNORE_DB;
                                                                        }
                                                                        if (size == 22 && (sql[17 + offset] == 'T'
                                                                            || sql[17 + offset] == 't') && (
                                                                            sql[18 + offset] == 'A'
                                                                                || sql[18 + offset] == 'a') && (
                                                                            sql[19 + offset] == 'B'
                                                                                || sql[19 + offset] == 'b') && (
                                                                            sql[20 + offset] == 'L'
                                                                                || sql[20 + offset] == 'l') && (
                                                                            sql[21 + offset] == 'E'
                                                                                || sql[21 + offset] == 'e')) {
                                                                            return Keywords.REPLICATE_IGNORE_TABLE;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (size == 20 && (sql[10 + offset] == 'R' || sql[10 + offset] == 'r') && (
                                                sql[11 + offset] == 'E' || sql[11 + offset] == 'e') && (
                                                sql[12 + offset] == 'W' || sql[12 + offset] == 'w') && (
                                                sql[13 + offset] == 'R' || sql[13 + offset] == 'r') && (
                                                sql[14 + offset] == 'I' || sql[14 + offset] == 'i') && (
                                                sql[15 + offset] == 'T' || sql[15 + offset] == 't') && (
                                                sql[16 + offset] == 'E' || sql[16 + offset] == 'e') && (
                                                sql[17 + offset] == '_' || sql[17 + offset] == '_') && (
                                                sql[18 + offset] == 'D' || sql[18 + offset] == 'd') && (
                                                sql[19 + offset] == 'B' || sql[19 + offset] == 'b')) {
                                                return Keywords.REPLICATE_REWRITE_DB;
                                            }
                                            if (sql[10 + offset] == 'W' || sql[10 + offset] == 'w') {
                                                if (size < 23 || size > 27) {
                                                    return 0;
                                                }
                                                if (sql[11 + offset] == 'I' || sql[11 + offset] == 'i') {
                                                    if (sql[12 + offset] == 'L' || sql[12 + offset] == 'l') {
                                                        if (sql[13 + offset] == 'D' || sql[13 + offset] == 'd') {
                                                            if (sql[14 + offset] == '_' || sql[14 + offset] == '_') {
                                                                if (size == 23 && (sql[15 + offset] == 'D'
                                                                    || sql[15 + offset] == 'd') && (
                                                                    sql[16 + offset] == 'O' || sql[16 + offset] == 'o')
                                                                    && (sql[17 + offset] == '_'
                                                                    || sql[17 + offset] == '_') && (
                                                                    sql[18 + offset] == 'T' || sql[18 + offset] == 't')
                                                                    && (sql[19 + offset] == 'A'
                                                                    || sql[19 + offset] == 'a') && (
                                                                    sql[20 + offset] == 'B' || sql[20 + offset] == 'b')
                                                                    && (sql[21 + offset] == 'L'
                                                                    || sql[21 + offset] == 'l') && (
                                                                    sql[22 + offset] == 'E'
                                                                        || sql[22 + offset] == 'e')) {
                                                                    return Keywords.REPLICATE_WILD_DO_TABLE;
                                                                }
                                                                if (size == 27 && (sql[15 + offset] == 'I'
                                                                    || sql[15 + offset] == 'i') && (
                                                                    sql[16 + offset] == 'G' || sql[16 + offset] == 'g')
                                                                    && (sql[17 + offset] == 'N'
                                                                    || sql[17 + offset] == 'n') && (
                                                                    sql[18 + offset] == 'O' || sql[18 + offset] == 'o')
                                                                    && (sql[19 + offset] == 'R'
                                                                    || sql[19 + offset] == 'r') && (
                                                                    sql[20 + offset] == 'E' || sql[20 + offset] == 'e')
                                                                    && (sql[21 + offset] == '_'
                                                                    || sql[21 + offset] == '_') && (
                                                                    sql[22 + offset] == 'T' || sql[22 + offset] == 't')
                                                                    && (sql[23 + offset] == 'A'
                                                                    || sql[23 + offset] == 'a') && (
                                                                    sql[24 + offset] == 'B' || sql[24 + offset] == 'b')
                                                                    && (sql[25 + offset] == 'L'
                                                                    || sql[25 + offset] == 'l') && (
                                                                    sql[26 + offset] == 'E'
                                                                        || sql[26 + offset] == 'e')) {
                                                                    return Keywords.REPLICATE_WILD_IGNORE_TABLE;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (size == 11 && (sql[8 + offset] == 'I' || sql[8 + offset] == 'i') && (
                                        sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (sql[10 + offset] == 'N'
                                        || sql[10 + offset] == 'n')) {
                                        return Keywords.REPLICATION;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (sql[2 + offset] == 'S' || sql[2 + offset] == 's') {
                if (size < 5 || size > 8) {
                    return 0;
                }
                if (size == 5 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'T'
                    || sql[4 + offset] == 't')) {
                    return Keywords.RESET;
                }
                if (size == 8 && (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') && (sql[4 + offset] == 'U'
                    || sql[4 + offset] == 'u') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'C' || sql[6 + offset] == 'c') && (sql[7 + offset] == 'E'
                    || sql[7 + offset] == 'e')) {
                    return Keywords.RESOURCE;
                }
                if (size == 7 && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                    return Keywords.RESPECT;
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (size != 7) {
                        return 0;
                    }
                    if (size == 7 && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (sql[5 + offset] == 'R'
                        || sql[5 + offset] == 'r') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                        return Keywords.RESTART;
                    }
                    if (size == 7 && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (sql[5 + offset] == 'R'
                        || sql[5 + offset] == 'r') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                        return Keywords.RESTORE;
                    }
                }
                if (size == 6 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') && (sql[4 + offset] == 'M'
                    || sql[4 + offset] == 'm') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Keywords.RESUME;
                }
            }
            if (sql[2 + offset] == 'T' || sql[2 + offset] == 't') {
                if (size < 6 || size > 17) {
                    return 0;
                }
                if (size == 6 && (sql[3 + offset] == 'A' || sql[3 + offset] == 'a') && (sql[4 + offset] == 'I'
                    || sql[4 + offset] == 'i') && (sql[5 + offset] == 'N' || sql[5 + offset] == 'n')) {
                    return Keywords.RETAIN;
                }
                if (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') {
                    if (size < 7 || size > 17) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') {
                        if (sql[5 + offset] == 'N' || sql[5 + offset] == 'n') {
                            if (size == 17 && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') && (
                                sql[7 + offset] == 'D' || sql[7 + offset] == 'd') && (sql[8 + offset] == '_'
                                || sql[8 + offset] == '_') && (sql[9 + offset] == 'S' || sql[9 + offset] == 's') && (
                                sql[10 + offset] == 'Q' || sql[10 + offset] == 'q') && (sql[11 + offset] == 'L'
                                || sql[11 + offset] == 'l') && (sql[12 + offset] == 'S' || sql[12 + offset] == 's') && (
                                sql[13 + offset] == 'T' || sql[13 + offset] == 't') && (sql[14 + offset] == 'A'
                                || sql[14 + offset] == 'a') && (sql[15 + offset] == 'T' || sql[15 + offset] == 't') && (
                                sql[16 + offset] == 'E' || sql[16 + offset] == 'e')) {
                                return Keywords.RETURNED_SQLSTATE;
                            }
                            if (size == 7 && (sql[6 + offset] == 'S' || sql[6 + offset] == 's')) {
                                return Keywords.RETURNS;
                            }
                        }
                    }
                }
            }
            if (size == 5 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Keywords.REUSE;
            }
            if (size == 7 && (sql[2 + offset] == 'V' || sql[2 + offset] == 'v') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r') && (
                sql[5 + offset] == 'S' || sql[5 + offset] == 's') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Keywords.REVERSE;
            }
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size < 4 || size > 8) {
                    return 0;
                }
                if (size == 4 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e')) {
                    return Keywords.ROLE;
                }
                if (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') {
                    if (size < 6 || size > 8) {
                        return 0;
                    }
                    if (size == 8 && (sql[4 + offset] == 'B' || sql[4 + offset] == 'b') && (sql[5 + offset] == 'A'
                        || sql[5 + offset] == 'a') && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c') && (
                        sql[7 + offset] == 'K' || sql[7 + offset] == 'k')) {
                        return Keywords.ROLLBACK;
                    }
                    if (size == 6 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (sql[5 + offset] == 'P'
                        || sql[5 + offset] == 'p')) {
                        return Keywords.ROLLUP;
                    }
                }
            }
            if (size == 6 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Keywords.ROTATE;
            }
            if (size == 7 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Keywords.ROUTINE;
            }
            if (sql[2 + offset] == 'W' || sql[2 + offset] == 'w') {
                if (size < 9 || size > 10) {
                    return 0;
                }
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (size == 9 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u') && (
                        sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'T'
                        || sql[8 + offset] == 't')) {
                        return Keywords.ROW_COUNT;
                    }
                    if (size == 10 && (sql[4 + offset] == 'F' || sql[4 + offset] == 'f') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r') && (
                        sql[7 + offset] == 'M' || sql[7 + offset] == 'm') && (sql[8 + offset] == 'A'
                        || sql[8 + offset] == 'a') && (sql[9 + offset] == 'T' || sql[9 + offset] == 't')) {
                        return Keywords.ROW_FORMAT;
                    }
                }
            }
        }
        if (size == 5 && (sql[1 + offset] == 'T' || sql[1 + offset] == 't') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'E'
            || sql[4 + offset] == 'e')) {
            return Keywords.RTREE;
        }
        return 0;
    }

    private static int parseS(byte[] sql, int offset, int size) {
        if (size < 4 || size > 18) {
            return 0;
        }
        if (size == 9 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') && (sql[2 + offset] == 'V'
            || sql[2 + offset] == 'v') && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'P'
            || sql[4 + offset] == 'p') && (sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'I'
            || sql[6 + offset] == 'i') && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'T'
            || sql[8 + offset] == 't')) {
            return Keywords.SAVEPOINT;
        }
        if (sql[1 + offset] == 'C' || sql[1 + offset] == 'c') {
            if (size < 8 || size > 11) {
                return 0;
            }
            if (sql[2 + offset] == 'H' || sql[2 + offset] == 'h') {
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 8 && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd') && (sql[5 + offset] == 'U'
                        || sql[5 + offset] == 'u') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Keywords.SCHEDULE;
                    }
                    if (size == 11 && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') && (sql[5 + offset] == 'A'
                        || sql[5 + offset] == 'a') && (sql[6 + offset] == '_' || sql[6 + offset] == '_') && (
                        sql[7 + offset] == 'N' || sql[7 + offset] == 'n') && (sql[8 + offset] == 'A'
                        || sql[8 + offset] == 'a') && (sql[9 + offset] == 'M' || sql[9 + offset] == 'm') && (
                        sql[10 + offset] == 'E' || sql[10 + offset] == 'e')) {
                        return Keywords.SCHEMA_NAME;
                    }
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 6 || size > 16) {
                return 0;
            }
            if (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') {
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') {
                        if (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') {
                            if (size == 6) {
                                return Keywords.SECOND;
                            }
                            if (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') {
                                if (size < 14 || size > 16) {
                                    return 0;
                                }
                                if (sql[7 + offset] == 'R' || sql[7 + offset] == 'r') {
                                    if (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y') {
                                        if (sql[9 + offset] == '_' || sql[9 + offset] == '_') {
                                            if (size == 16 && (sql[10 + offset] == 'E' || sql[10 + offset] == 'e') && (
                                                sql[11 + offset] == 'N' || sql[11 + offset] == 'n') && (
                                                sql[12 + offset] == 'G' || sql[12 + offset] == 'g') && (
                                                sql[13 + offset] == 'I' || sql[13 + offset] == 'i') && (
                                                sql[14 + offset] == 'N' || sql[14 + offset] == 'n') && (
                                                sql[15 + offset] == 'E' || sql[15 + offset] == 'e')) {
                                                return Keywords.SECONDARY_ENGINE;
                                            }
                                            if (size == 14 && (sql[10 + offset] == 'L' || sql[10 + offset] == 'l') && (
                                                sql[11 + offset] == 'O' || sql[11 + offset] == 'o') && (
                                                sql[12 + offset] == 'A' || sql[12 + offset] == 'a') && (
                                                sql[13 + offset] == 'D' || sql[13 + offset] == 'd')) {
                                                return Keywords.SECONDARY_LOAD;
                                            }
                                            if (size == 16 && (sql[10 + offset] == 'U' || sql[10 + offset] == 'u') && (
                                                sql[11 + offset] == 'N' || sql[11 + offset] == 'n') && (
                                                sql[12 + offset] == 'L' || sql[12 + offset] == 'l') && (
                                                sql[13 + offset] == 'O' || sql[13 + offset] == 'o') && (
                                                sql[14 + offset] == 'A' || sql[14 + offset] == 'a') && (
                                                sql[15 + offset] == 'D' || sql[15 + offset] == 'd')) {
                                                return Keywords.SECONDARY_UNLOAD;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (size == 8 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') && (sql[4 + offset] == 'R'
                    || sql[4 + offset] == 'r') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (sql[7 + offset] == 'Y'
                    || sql[7 + offset] == 'y')) {
                    return Keywords.SECURITY;
                }
            }
            if (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') {
                if (size < 6 || size > 12) {
                    return 0;
                }
                if (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') {
                    if (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') {
                        if (sql[5 + offset] == 'L' || sql[5 + offset] == 'l') {
                            if (size == 6) {
                                return Keywords.SERIAL;
                            }
                            if (size == 12 && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') && (
                                sql[7 + offset] == 'Z' || sql[7 + offset] == 'z') && (sql[8 + offset] == 'A'
                                || sql[8 + offset] == 'a') && (sql[9 + offset] == 'B' || sql[9 + offset] == 'b') && (
                                sql[10 + offset] == 'L' || sql[10 + offset] == 'l') && (sql[11 + offset] == 'E'
                                || sql[11 + offset] == 'e')) {
                                return Keywords.SERIALIZABLE;
                            }
                        }
                    }
                }
                if (size == 6 && (sql[3 + offset] == 'V' || sql[3 + offset] == 'v') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r')) {
                    return Keywords.SERVER;
                }
            }
            if (size == 7 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'N'
                || sql[6 + offset] == 'n')) {
                return Keywords.SESSION;
            }
        }
        if (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') {
            if (size < 5 || size > 8) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'R'
                || sql[3 + offset] == 'r') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Keywords.SHARE;
            }
            if (size == 8 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'D' || sql[4 + offset] == 'd') && (
                sql[5 + offset] == 'O' || sql[5 + offset] == 'o') && (sql[6 + offset] == 'W' || sql[6 + offset] == 'w')
                && (sql[7 + offset] == 'N' || sql[7 + offset] == 'n')) {
                return Keywords.SHUTDOWN;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size != 6) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'G' || sql[2 + offset] == 'g') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'D' || sql[5 + offset] == 'd')) {
                return Keywords.SIGNED;
            }
            if (size == 6 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'P'
                || sql[3 + offset] == 'p') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Keywords.SIMPLE;
            }
        }
        if (size == 4 && (sql[1 + offset] == 'K' || sql[1 + offset] == 'k') && (sql[2 + offset] == 'I'
            || sql[2 + offset] == 'i') && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p')) {
            return Keywords.SKIP;
        }
        if (sql[1 + offset] == 'L' || sql[1 + offset] == 'l') {
            if (size < 4 || size > 5) {
                return 0;
            }
            if (size == 5 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'V'
                || sql[3 + offset] == 'v') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e')) {
                return Keywords.SLAVE;
            }
            if (size == 4 && (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') && (sql[3 + offset] == 'W'
                || sql[3 + offset] == 'w')) {
                return Keywords.SLOW;
            }
        }
        if (size == 8 && (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p') && (sql[4 + offset] == 'S'
            || sql[4 + offset] == 's') && (sql[5 + offset] == 'H' || sql[5 + offset] == 'h') && (sql[6 + offset] == 'O'
            || sql[6 + offset] == 'o') && (sql[7 + offset] == 'T' || sql[7 + offset] == 't')) {
            return Keywords.SNAPSHOT;
        }
        if (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') {
            if (size < 4 || size > 6) {
                return 0;
            }
            if (size == 6 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'K'
                || sql[3 + offset] == 'k') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't')) {
                return Keywords.SOCKET;
            }
            if (size == 4 && (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e')) {
                return Keywords.SOME;
            }
            if (size == 6 && (sql[2 + offset] == 'N' || sql[2 + offset] == 'n') && (sql[3 + offset] == 'A'
                || sql[3 + offset] == 'a') && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                return Keywords.SONAME;
            }
            if (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') {
                if (size == 6 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == 'D'
                    || sql[4 + offset] == 'd') && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                    return Keywords.SOUNDS;
                }
                if (size == 6 && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'C'
                    || sql[4 + offset] == 'c') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e')) {
                    return Keywords.SOURCE;
                }
            }
        }
        if (sql[1 + offset] == 'Q' || sql[1 + offset] == 'q') {
            if (size < 9 || size > 18) {
                return 0;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (sql[3 + offset] == '_' || sql[3 + offset] == '_') {
                    if (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') {
                        if (size < 15 || size > 18) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'F' || sql[5 + offset] == 'f') {
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') {
                                    if (sql[8 + offset] == 'R' || sql[8 + offset] == 'r') {
                                        if (sql[9 + offset] == '_' || sql[9 + offset] == '_') {
                                            if (size == 15 && (sql[10 + offset] == 'G' || sql[10 + offset] == 'g') && (
                                                sql[11 + offset] == 'T' || sql[11 + offset] == 't') && (
                                                sql[12 + offset] == 'I' || sql[12 + offset] == 'i') && (
                                                sql[13 + offset] == 'D' || sql[13 + offset] == 'd') && (
                                                sql[14 + offset] == 'S' || sql[14 + offset] == 's')) {
                                                return Keywords.SQL_AFTER_GTIDS;
                                            }
                                            if (size == 18 && (sql[10 + offset] == 'M' || sql[10 + offset] == 'm') && (
                                                sql[11 + offset] == 'T' || sql[11 + offset] == 't') && (
                                                sql[12 + offset] == 'S' || sql[12 + offset] == 's') && (
                                                sql[13 + offset] == '_' || sql[13 + offset] == '_') && (
                                                sql[14 + offset] == 'G' || sql[14 + offset] == 'g') && (
                                                sql[15 + offset] == 'A' || sql[15 + offset] == 'a') && (
                                                sql[16 + offset] == 'P' || sql[16 + offset] == 'p') && (
                                                sql[17 + offset] == 'S' || sql[17 + offset] == 's')) {
                                                return Keywords.SQL_AFTER_MTS_GAPS;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (sql[4 + offset] == 'B' || sql[4 + offset] == 'b') {
                        if (size < 16 || size > 17) {
                            return 0;
                        }
                        if (size == 16 && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'F'
                            || sql[6 + offset] == 'f') && (sql[7 + offset] == 'O' || sql[7 + offset] == 'o') && (
                            sql[8 + offset] == 'R' || sql[8 + offset] == 'r') && (sql[9 + offset] == 'E'
                            || sql[9 + offset] == 'e') && (sql[10 + offset] == '_' || sql[10 + offset] == '_') && (
                            sql[11 + offset] == 'G' || sql[11 + offset] == 'g') && (sql[12 + offset] == 'T'
                            || sql[12 + offset] == 't') && (sql[13 + offset] == 'I' || sql[13 + offset] == 'i') && (
                            sql[14 + offset] == 'D' || sql[14 + offset] == 'd') && (sql[15 + offset] == 'S'
                            || sql[15 + offset] == 's')) {
                            return Keywords.SQL_BEFORE_GTIDS;
                        }
                        if (size == 17 && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u') && (sql[6 + offset] == 'F'
                            || sql[6 + offset] == 'f') && (sql[7 + offset] == 'F' || sql[7 + offset] == 'f') && (
                            sql[8 + offset] == 'E' || sql[8 + offset] == 'e') && (sql[9 + offset] == 'R'
                            || sql[9 + offset] == 'r') && (sql[10 + offset] == '_' || sql[10 + offset] == '_') && (
                            sql[11 + offset] == 'R' || sql[11 + offset] == 'r') && (sql[12 + offset] == 'E'
                            || sql[12 + offset] == 'e') && (sql[13 + offset] == 'S' || sql[13 + offset] == 's') && (
                            sql[14 + offset] == 'U' || sql[14 + offset] == 'u') && (sql[15 + offset] == 'L'
                            || sql[15 + offset] == 'l') && (sql[16 + offset] == 'T' || sql[16 + offset] == 't')) {
                            return Keywords.SQL_BUFFER_RESULT;
                        }
                    }
                    if (size == 9 && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (sql[5 + offset] == 'A'
                        || sql[5 + offset] == 'a') && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c') && (
                        sql[7 + offset] == 'H' || sql[7 + offset] == 'h') && (sql[8 + offset] == 'E'
                        || sql[8 + offset] == 'e')) {
                        return Keywords.SQL_CACHE;
                    }
                    if (size == 12 && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (sql[5 + offset] == 'O'
                        || sql[5 + offset] == 'o') && (sql[6 + offset] == '_' || sql[6 + offset] == '_') && (
                        sql[7 + offset] == 'C' || sql[7 + offset] == 'c') && (sql[8 + offset] == 'A'
                        || sql[8 + offset] == 'a') && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') && (
                        sql[10 + offset] == 'H' || sql[10 + offset] == 'h') && (sql[11 + offset] == 'E'
                        || sql[11 + offset] == 'e')) {
                        return Keywords.SQL_NO_CACHE;
                    }
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (size < 10 || size > 15) {
                            return 0;
                        }
                        if (size == 10 && (sql[5 + offset] == 'H' || sql[5 + offset] == 'h') && (sql[6 + offset] == 'R'
                            || sql[6 + offset] == 'r') && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (
                            sql[8 + offset] == 'A' || sql[8 + offset] == 'a') && (sql[9 + offset] == 'D'
                            || sql[9 + offset] == 'd')) {
                            return Keywords.SQL_THREAD;
                        }
                        if (sql[5 + offset] == 'S' || sql[5 + offset] == 's') {
                            if (size < 11 || size > 15) {
                                return 0;
                            }
                            if (sql[6 + offset] == 'I' || sql[6 + offset] == 'i') {
                                if (sql[7 + offset] == '_' || sql[7 + offset] == '_') {
                                    if (size == 11 && (sql[8 + offset] == 'D' || sql[8 + offset] == 'd') && (
                                        sql[9 + offset] == 'A' || sql[9 + offset] == 'a') && (sql[10 + offset] == 'Y'
                                        || sql[10 + offset] == 'y')) {
                                        return Keywords.SQL_TSI_DAY;
                                    }
                                    if (size == 12 && (sql[8 + offset] == 'H' || sql[8 + offset] == 'h') && (
                                        sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (sql[10 + offset] == 'U'
                                        || sql[10 + offset] == 'u') && (sql[11 + offset] == 'R'
                                        || sql[11 + offset] == 'r')) {
                                        return Keywords.SQL_TSI_HOUR;
                                    }
                                    if (sql[8 + offset] == 'M' || sql[8 + offset] == 'm') {
                                        if (size < 13 || size > 14) {
                                            return 0;
                                        }
                                        if (size == 14 && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (
                                            sql[10 + offset] == 'N' || sql[10 + offset] == 'n') && (
                                            sql[11 + offset] == 'U' || sql[11 + offset] == 'u') && (
                                            sql[12 + offset] == 'T' || sql[12 + offset] == 't') && (
                                            sql[13 + offset] == 'E' || sql[13 + offset] == 'e')) {
                                            return Keywords.SQL_TSI_MINUTE;
                                        }
                                        if (size == 13 && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (
                                            sql[10 + offset] == 'N' || sql[10 + offset] == 'n') && (
                                            sql[11 + offset] == 'T' || sql[11 + offset] == 't') && (
                                            sql[12 + offset] == 'H' || sql[12 + offset] == 'h')) {
                                            return Keywords.SQL_TSI_MONTH;
                                        }
                                    }
                                    if (size == 15 && (sql[8 + offset] == 'Q' || sql[8 + offset] == 'q') && (
                                        sql[9 + offset] == 'U' || sql[9 + offset] == 'u') && (sql[10 + offset] == 'A'
                                        || sql[10 + offset] == 'a') && (sql[11 + offset] == 'R'
                                        || sql[11 + offset] == 'r') && (sql[12 + offset] == 'T'
                                        || sql[12 + offset] == 't') && (sql[13 + offset] == 'E'
                                        || sql[13 + offset] == 'e') && (sql[14 + offset] == 'R'
                                        || sql[14 + offset] == 'r')) {
                                        return Keywords.SQL_TSI_QUARTER;
                                    }
                                    if (size == 14 && (sql[8 + offset] == 'S' || sql[8 + offset] == 's') && (
                                        sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (sql[10 + offset] == 'C'
                                        || sql[10 + offset] == 'c') && (sql[11 + offset] == 'O'
                                        || sql[11 + offset] == 'o') && (sql[12 + offset] == 'N'
                                        || sql[12 + offset] == 'n') && (sql[13 + offset] == 'D'
                                        || sql[13 + offset] == 'd')) {
                                        return Keywords.SQL_TSI_SECOND;
                                    }
                                    if (size == 12 && (sql[8 + offset] == 'W' || sql[8 + offset] == 'w') && (
                                        sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (sql[10 + offset] == 'E'
                                        || sql[10 + offset] == 'e') && (sql[11 + offset] == 'K'
                                        || sql[11 + offset] == 'k')) {
                                        return Keywords.SQL_TSI_WEEK;
                                    }
                                    if (size == 12 && (sql[8 + offset] == 'Y' || sql[8 + offset] == 'y') && (
                                        sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (sql[10 + offset] == 'A'
                                        || sql[10 + offset] == 'a') && (sql[11 + offset] == 'R'
                                        || sql[11 + offset] == 'r')) {
                                        return Keywords.SQL_TSI_YEAR;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (size == 4 && (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') && (sql[2 + offset] == 'I'
            || sql[2 + offset] == 'i') && (sql[3 + offset] == 'D' || sql[3 + offset] == 'd')) {
            return Keywords.SRID;
        }
        if (sql[1 + offset] == 'T' || sql[1 + offset] == 't') {
            if (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') {
                if (size < 5 || size > 18) {
                    return 0;
                }
                if (size == 7 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'K'
                    || sql[4 + offset] == 'k') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (
                    sql[6 + offset] == 'D' || sql[6 + offset] == 'd')) {
                    return Keywords.STACKED;
                }
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size < 5 || size > 6) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'T' || sql[4 + offset] == 't') {
                        if (size == 5) {
                            return Keywords.START;
                        }
                        if (size == 6 && (sql[5 + offset] == 'S' || sql[5 + offset] == 's')) {
                            return Keywords.STARTS;
                        }
                    }
                }
                if (sql[3 + offset] == 'T' || sql[3 + offset] == 't') {
                    if (size < 6 || size > 18) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'S' || sql[4 + offset] == 's') {
                        if (size < 16 || size > 18) {
                            return 0;
                        }
                        if (sql[5 + offset] == '_' || sql[5 + offset] == '_') {
                            if (size == 17 && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (
                                sql[7 + offset] == 'U' || sql[7 + offset] == 'u') && (sql[8 + offset] == 'T'
                                || sql[8 + offset] == 't') && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (
                                sql[10 + offset] == '_' || sql[10 + offset] == '_') && (sql[11 + offset] == 'R'
                                || sql[11 + offset] == 'r') && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e') && (
                                sql[13 + offset] == 'C' || sql[13 + offset] == 'c') && (sql[14 + offset] == 'A'
                                || sql[14 + offset] == 'a') && (sql[15 + offset] == 'L' || sql[15 + offset] == 'l') && (
                                sql[16 + offset] == 'C' || sql[16 + offset] == 'c')) {
                                return Keywords.STATS_AUTO_RECALC;
                            }
                            if (size == 16 && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p') && (
                                sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'R'
                                || sql[8 + offset] == 'r') && (sql[9 + offset] == 'S' || sql[9 + offset] == 's') && (
                                sql[10 + offset] == 'I' || sql[10 + offset] == 'i') && (sql[11 + offset] == 'S'
                                || sql[11 + offset] == 's') && (sql[12 + offset] == 'T' || sql[12 + offset] == 't') && (
                                sql[13 + offset] == 'E' || sql[13 + offset] == 'e') && (sql[14 + offset] == 'N'
                                || sql[14 + offset] == 'n') && (sql[15 + offset] == 'T' || sql[15 + offset] == 't')) {
                                return Keywords.STATS_PERSISTENT;
                            }
                            if (size == 18 && (sql[6 + offset] == 'S' || sql[6 + offset] == 's') && (
                                sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (sql[8 + offset] == 'M'
                                || sql[8 + offset] == 'm') && (sql[9 + offset] == 'P' || sql[9 + offset] == 'p') && (
                                sql[10 + offset] == 'L' || sql[10 + offset] == 'l') && (sql[11 + offset] == 'E'
                                || sql[11 + offset] == 'e') && (sql[12 + offset] == '_' || sql[12 + offset] == '_') && (
                                sql[13 + offset] == 'P' || sql[13 + offset] == 'p') && (sql[14 + offset] == 'A'
                                || sql[14 + offset] == 'a') && (sql[15 + offset] == 'G' || sql[15 + offset] == 'g') && (
                                sql[16 + offset] == 'E' || sql[16 + offset] == 'e') && (sql[17 + offset] == 'S'
                                || sql[17 + offset] == 's')) {
                                return Keywords.STATS_SAMPLE_PAGES;
                            }
                        }
                    }
                    if (size == 6 && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (sql[5 + offset] == 'S'
                        || sql[5 + offset] == 's')) {
                        return Keywords.STATUS;
                    }
                }
            }
            if (sql[2 + offset] == 'O' || sql[2 + offset] == 'o') {
                if (size < 4 || size > 7) {
                    return 0;
                }
                if (size == 4 && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p')) {
                    return Keywords.STOP;
                }
                if (size == 7 && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'A'
                    || sql[4 + offset] == 'a') && (sql[5 + offset] == 'G' || sql[5 + offset] == 'g') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Keywords.STORAGE;
                }
            }
            if (size == 6 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'N' || sql[4 + offset] == 'n') && (
                sql[5 + offset] == 'G' || sql[5 + offset] == 'g')) {
                return Keywords.STRING;
            }
        }
        if (sql[1 + offset] == 'U' || sql[1 + offset] == 'u') {
            if (size < 5 || size > 15) {
                return 0;
            }
            if (sql[2 + offset] == 'B' || sql[2 + offset] == 'b') {
                if (size < 7 || size > 15) {
                    return 0;
                }
                if (size == 15 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'L'
                    || sql[4 + offset] == 'l') && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (
                    sql[6 + offset] == 'S' || sql[6 + offset] == 's') && (sql[7 + offset] == 'S'
                    || sql[7 + offset] == 's') && (sql[8 + offset] == '_' || sql[8 + offset] == '_') && (
                    sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (sql[10 + offset] == 'R'
                    || sql[10 + offset] == 'r') && (sql[11 + offset] == 'I' || sql[11 + offset] == 'i') && (
                    sql[12 + offset] == 'G' || sql[12 + offset] == 'g') && (sql[13 + offset] == 'I'
                    || sql[13 + offset] == 'i') && (sql[14 + offset] == 'N' || sql[14 + offset] == 'n')) {
                    return Keywords.SUBCLASS_ORIGIN;
                }
                if (size == 7 && (sql[3 + offset] == 'J' || sql[3 + offset] == 'j') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e') && (sql[5 + offset] == 'C' || sql[5 + offset] == 'c') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't')) {
                    return Keywords.SUBJECT;
                }
                if (sql[3 + offset] == 'P' || sql[3 + offset] == 'p') {
                    if (size < 12 || size > 13) {
                        return 0;
                    }
                    if (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') {
                        if (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') {
                            if (sql[6 + offset] == 'T' || sql[6 + offset] == 't') {
                                if (sql[7 + offset] == 'I' || sql[7 + offset] == 'i') {
                                    if (sql[8 + offset] == 'T' || sql[8 + offset] == 't') {
                                        if (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') {
                                            if (sql[10 + offset] == 'O' || sql[10 + offset] == 'o') {
                                                if (sql[11 + offset] == 'N' || sql[11 + offset] == 'n') {
                                                    if (size == 12) {
                                                        return Keywords.SUBPARTITION;
                                                    }
                                                    if (size == 13 && (sql[12 + offset] == 'S'
                                                        || sql[12 + offset] == 's')) {
                                                        return Keywords.SUBPARTITIONS;
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
            if (size == 5 && (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'R' || sql[4 + offset] == 'r')) {
                return Keywords.SUPER;
            }
            if (size == 7 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'P'
                || sql[3 + offset] == 'p') && (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') && (
                sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'D'
                || sql[6 + offset] == 'd')) {
                return Keywords.SUSPEND;
            }
        }
        if (sql[1 + offset] == 'W' || sql[1 + offset] == 'w') {
            if (size == 5 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'P'
                || sql[3 + offset] == 'p') && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                return Keywords.SWAPS;
            }
            if (size == 8 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't') && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (
                sql[5 + offset] == 'H' || sql[5 + offset] == 'h') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e')
                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')) {
                return Keywords.SWITCHES;
            }
        }
        return 0;
    }

    private static int parseT(byte[] sql, int offset, int size) {
        if (size < 4 || size > 15) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 6 || size > 14) {
                return 0;
            }
            if (sql[2 + offset] == 'B' || sql[2 + offset] == 'b') {
                if (sql[3 + offset] == 'L' || sql[3 + offset] == 'l') {
                    if (sql[4 + offset] == 'E' || sql[4 + offset] == 'e') {
                        if (sql[5 + offset] == 'S' || sql[5 + offset] == 's') {
                            if (size < 6 || size > 10) {
                                return 0;
                            }
                            if (size == 6) {
                                return Keywords.TABLES;
                            }
                            if (size == 10 && (sql[6 + offset] == 'P' || sql[6 + offset] == 'p') && (
                                sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (sql[8 + offset] == 'C'
                                || sql[8 + offset] == 'c') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                                return Keywords.TABLESPACE;
                            }
                        }
                        if (sql[5 + offset] == '_' || sql[5 + offset] == '_') {
                            if (size < 10 || size > 14) {
                                return 0;
                            }
                            if (size == 14 && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c') && (
                                sql[7 + offset] == 'H' || sql[7 + offset] == 'h') && (sql[8 + offset] == 'E'
                                || sql[8 + offset] == 'e') && (sql[9 + offset] == 'C' || sql[9 + offset] == 'c') && (
                                sql[10 + offset] == 'K' || sql[10 + offset] == 'k') && (sql[11 + offset] == 'S'
                                || sql[11 + offset] == 's') && (sql[12 + offset] == 'U' || sql[12 + offset] == 'u') && (
                                sql[13 + offset] == 'M' || sql[13 + offset] == 'm')) {
                                return Keywords.TABLE_CHECKSUM;
                            }
                            if (size == 10 && (sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (
                                sql[7 + offset] == 'A' || sql[7 + offset] == 'a') && (sql[8 + offset] == 'M'
                                || sql[8 + offset] == 'm') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e')) {
                                return Keywords.TABLE_NAME;
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size < 4 || size > 9) {
                return 0;
            }
            if (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') {
                if (size != 9) {
                    return 0;
                }
                if (sql[3 + offset] == 'P' || sql[3 + offset] == 'p') {
                    if (size == 9 && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (sql[5 + offset] == 'R'
                        || sql[5 + offset] == 'r') && (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (
                        sql[7 + offset] == 'R' || sql[7 + offset] == 'r') && (sql[8 + offset] == 'Y'
                        || sql[8 + offset] == 'y')) {
                        return Keywords.TEMPORARY;
                    }
                    if (size == 9 && (sql[4 + offset] == 'T' || sql[4 + offset] == 't') && (sql[5 + offset] == 'A'
                        || sql[5 + offset] == 'a') && (sql[6 + offset] == 'B' || sql[6 + offset] == 'b') && (
                        sql[7 + offset] == 'L' || sql[7 + offset] == 'l') && (sql[8 + offset] == 'E'
                        || sql[8 + offset] == 'e')) {
                        return Keywords.TEMPTABLE;
                    }
                }
            }
            if (size == 4 && (sql[2 + offset] == 'X' || sql[2 + offset] == 'x') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't')) {
                return Keywords.TEXT;
            }
        }
        if (sql[1 + offset] == 'H' || sql[1 + offset] == 'h') {
            if (size == 4 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n')) {
                return Keywords.THAN;
            }
            if (size == 15 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'E'
                || sql[3 + offset] == 'e') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                && (sql[7 + offset] == 'P' || sql[7 + offset] == 'p') && (sql[8 + offset] == 'R'
                || sql[8 + offset] == 'r') && (sql[9 + offset] == 'I' || sql[9 + offset] == 'i') && (
                sql[10 + offset] == 'O' || sql[10 + offset] == 'o') && (sql[11 + offset] == 'R'
                || sql[11 + offset] == 'r') && (sql[12 + offset] == 'I' || sql[12 + offset] == 'i') && (
                sql[13 + offset] == 'T' || sql[13 + offset] == 't') && (sql[14 + offset] == 'Y'
                || sql[14 + offset] == 'y')) {
                return Keywords.THREAD_PRIORITY;
            }
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 4 || size > 13) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'S'
                || sql[3 + offset] == 's')) {
                return Keywords.TIES;
            }
            if (sql[2 + offset] == 'M' || sql[2 + offset] == 'm') {
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 4) {
                        return Keywords.TIME;
                    }
                    if (sql[4 + offset] == 'S' || sql[4 + offset] == 's') {
                        if (size < 9 || size > 13) {
                            return 0;
                        }
                        if (sql[5 + offset] == 'T' || sql[5 + offset] == 't') {
                            if (sql[6 + offset] == 'A' || sql[6 + offset] == 'a') {
                                if (sql[7 + offset] == 'M' || sql[7 + offset] == 'm') {
                                    if (sql[8 + offset] == 'P' || sql[8 + offset] == 'p') {
                                        if (size == 9) {
                                            return Keywords.TIMESTAMP;
                                        }
                                        if (size == 12 && (sql[9 + offset] == 'A' || sql[9 + offset] == 'a') && (
                                            sql[10 + offset] == 'D' || sql[10 + offset] == 'd') && (
                                            sql[11 + offset] == 'D' || sql[11 + offset] == 'd')) {
                                            return Keywords.TIMESTAMPADD;
                                        }
                                        if (size == 13 && (sql[9 + offset] == 'D' || sql[9 + offset] == 'd') && (
                                            sql[10 + offset] == 'I' || sql[10 + offset] == 'i') && (
                                            sql[11 + offset] == 'F' || sql[11 + offset] == 'f') && (
                                            sql[12 + offset] == 'F' || sql[12 + offset] == 'f')) {
                                            return Keywords.TIMESTAMPDIFF;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') {
            if (size < 8 || size > 11) {
                return 0;
            }
            if (size == 11 && (sql[2 + offset] == 'A' || sql[2 + offset] == 'a') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'S' || sql[4 + offset] == 's') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'C' || sql[6 + offset] == 'c')
                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == 'I'
                || sql[8 + offset] == 'i') && (sql[9 + offset] == 'O' || sql[9 + offset] == 'o') && (
                sql[10 + offset] == 'N' || sql[10 + offset] == 'n')) {
                return Keywords.TRANSACTION;
            }
            if (size == 8 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'G'
                || sql[3 + offset] == 'g') && (sql[4 + offset] == 'G' || sql[4 + offset] == 'g') && (
                sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R' || sql[6 + offset] == 'r')
                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')) {
                return Keywords.TRIGGERS;
            }
            if (size == 8 && (sql[2 + offset] == 'U' || sql[2 + offset] == 'u') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'C' || sql[4 + offset] == 'c') && (
                sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (sql[6 + offset] == 'T' || sql[6 + offset] == 't')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                return Keywords.TRUNCATE;
            }
        }
        if (sql[1 + offset] == 'Y' || sql[1 + offset] == 'y') {
            if (size < 4 || size > 5) {
                return 0;
            }
            if (sql[2 + offset] == 'P' || sql[2 + offset] == 'p') {
                if (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') {
                    if (size == 4) {
                        return Keywords.TYPE;
                    }
                    if (size == 5 && (sql[4 + offset] == 'S' || sql[4 + offset] == 's')) {
                        return Keywords.TYPES;
                    }
                }
            }
        }
        return 0;
    }

    private static int parseU(byte[] sql, int offset, int size) {
        if (size < 4 || size > 16) {
            return 0;
        }
        if (sql[1 + offset] == 'N' || sql[1 + offset] == 'n') {
            if (size < 5 || size > 16) {
                return 0;
            }
            if (size == 9 && (sql[2 + offset] == 'B' || sql[2 + offset] == 'b') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'U' || sql[4 + offset] == 'u') && (
                sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'D' || sql[6 + offset] == 'd')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'D'
                || sql[8 + offset] == 'd')) {
                return Keywords.UNBOUNDED;
            }
            if (size == 11 && (sql[2 + offset] == 'C' || sql[2 + offset] == 'c') && (sql[3 + offset] == 'O'
                || sql[3 + offset] == 'o') && (sql[4 + offset] == 'M' || sql[4 + offset] == 'm') && (
                sql[5 + offset] == 'M' || sql[5 + offset] == 'm') && (sql[6 + offset] == 'I' || sql[6 + offset] == 'i')
                && (sql[7 + offset] == 'T' || sql[7 + offset] == 't') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (
                sql[10 + offset] == 'D' || sql[10 + offset] == 'd')) {
                return Keywords.UNCOMMITTED;
            }
            if (sql[2 + offset] == 'D' || sql[2 + offset] == 'd') {
                if (size < 8 || size > 16) {
                    return 0;
                }
                if (size == 9 && (sql[3 + offset] == 'E' || sql[3 + offset] == 'e') && (sql[4 + offset] == 'F'
                    || sql[4 + offset] == 'f') && (sql[5 + offset] == 'I' || sql[5 + offset] == 'i') && (
                    sql[6 + offset] == 'N' || sql[6 + offset] == 'n') && (sql[7 + offset] == 'E'
                    || sql[7 + offset] == 'e') && (sql[8 + offset] == 'D' || sql[8 + offset] == 'd')) {
                    return Keywords.UNDEFINED;
                }
                if (sql[3 + offset] == 'O' || sql[3 + offset] == 'o') {
                    if (size == 8 && (sql[4 + offset] == 'F' || sql[4 + offset] == 'f') && (sql[5 + offset] == 'I'
                        || sql[5 + offset] == 'i') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l') && (
                        sql[7 + offset] == 'E' || sql[7 + offset] == 'e')) {
                        return Keywords.UNDOFILE;
                    }
                    if (size == 16 && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (sql[5 + offset] == 'B'
                        || sql[5 + offset] == 'b') && (sql[6 + offset] == 'U' || sql[6 + offset] == 'u') && (
                        sql[7 + offset] == 'F' || sql[7 + offset] == 'f') && (sql[8 + offset] == 'F'
                        || sql[8 + offset] == 'f') && (sql[9 + offset] == 'E' || sql[9 + offset] == 'e') && (
                        sql[10 + offset] == 'R' || sql[10 + offset] == 'r') && (sql[11 + offset] == '_'
                        || sql[11 + offset] == '_') && (sql[12 + offset] == 'S' || sql[12 + offset] == 's') && (
                        sql[13 + offset] == 'I' || sql[13 + offset] == 'i') && (sql[14 + offset] == 'Z'
                        || sql[14 + offset] == 'z') && (sql[15 + offset] == 'E' || sql[15 + offset] == 'e')) {
                        return Keywords.UNDO_BUFFER_SIZE;
                    }
                }
            }
            if (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') {
                if (size < 7 || size > 9) {
                    return 0;
                }
                if (size == 7 && (sql[3 + offset] == 'C' || sql[3 + offset] == 'c') && (sql[4 + offset] == 'O'
                    || sql[4 + offset] == 'o') && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (
                    sql[6 + offset] == 'E' || sql[6 + offset] == 'e')) {
                    return Keywords.UNICODE;
                }
                if (size == 9 && (sql[3 + offset] == 'N' || sql[3 + offset] == 'n') && (sql[4 + offset] == 'S'
                    || sql[4 + offset] == 's') && (sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (
                    sql[6 + offset] == 'A' || sql[6 + offset] == 'a') && (sql[7 + offset] == 'L'
                    || sql[7 + offset] == 'l') && (sql[8 + offset] == 'L' || sql[8 + offset] == 'l')) {
                    return Keywords.UNINSTALL;
                }
            }
            if (size == 7 && (sql[2 + offset] == 'K' || sql[2 + offset] == 'k') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'O' || sql[4 + offset] == 'o') && (
                sql[5 + offset] == 'W' || sql[5 + offset] == 'w') && (sql[6 + offset] == 'N'
                || sql[6 + offset] == 'n')) {
                return Keywords.UNKNOWN;
            }
            if (size == 5 && (sql[2 + offset] == 'T' || sql[2 + offset] == 't') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'L' || sql[4 + offset] == 'l')) {
                return Keywords.UNTIL;
            }
        }
        if (size == 7 && (sql[1 + offset] == 'P' || sql[1 + offset] == 'p') && (sql[2 + offset] == 'G'
            || sql[2 + offset] == 'g') && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') && (sql[4 + offset] == 'A'
            || sql[4 + offset] == 'a') && (sql[5 + offset] == 'D' || sql[5 + offset] == 'd') && (sql[6 + offset] == 'E'
            || sql[6 + offset] == 'e')) {
            return Keywords.UPGRADE;
        }
        if (sql[1 + offset] == 'S' || sql[1 + offset] == 's') {
            if (size < 4 || size > 14) {
                return 0;
            }
            if (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') {
                if (sql[3 + offset] == 'R' || sql[3 + offset] == 'r') {
                    if (size == 4) {
                        return Keywords.USER;
                    }
                    if (size == 14 && (sql[4 + offset] == '_' || sql[4 + offset] == '_') && (sql[5 + offset] == 'R'
                        || sql[5 + offset] == 'r') && (sql[6 + offset] == 'E' || sql[6 + offset] == 'e') && (
                        sql[7 + offset] == 'S' || sql[7 + offset] == 's') && (sql[8 + offset] == 'O'
                        || sql[8 + offset] == 'o') && (sql[9 + offset] == 'U' || sql[9 + offset] == 'u') && (
                        sql[10 + offset] == 'R' || sql[10 + offset] == 'r') && (sql[11 + offset] == 'C'
                        || sql[11 + offset] == 'c') && (sql[12 + offset] == 'E' || sql[12 + offset] == 'e') && (
                        sql[13 + offset] == 'S' || sql[13 + offset] == 's')) {
                        return Keywords.USER_RESOURCES;
                    }
                }
                if (size == 7 && (sql[3 + offset] == '_' || sql[3 + offset] == '_') && (sql[4 + offset] == 'F'
                    || sql[4 + offset] == 'f') && (sql[5 + offset] == 'R' || sql[5 + offset] == 'r') && (
                    sql[6 + offset] == 'M' || sql[6 + offset] == 'm')) {
                    return Keywords.USE_FRM;
                }
            }
        }
        return 0;
    }

    private static int parseV(byte[] sql, int offset, int size) {
        if (size < 4 || size > 10) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 5 || size > 10) {
                return 0;
            }
            if (sql[2 + offset] == 'L' || sql[2 + offset] == 'l') {
                if (size == 10 && (sql[3 + offset] == 'I' || sql[3 + offset] == 'i') && (sql[4 + offset] == 'D'
                    || sql[4 + offset] == 'd') && (sql[5 + offset] == 'A' || sql[5 + offset] == 'a') && (
                    sql[6 + offset] == 'T' || sql[6 + offset] == 't') && (sql[7 + offset] == 'I'
                    || sql[7 + offset] == 'i') && (sql[8 + offset] == 'O' || sql[8 + offset] == 'o') && (
                    sql[9 + offset] == 'N' || sql[9 + offset] == 'n')) {
                    return Keywords.VALIDATION;
                }
                if (size == 5 && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u') && (sql[4 + offset] == 'E'
                    || sql[4 + offset] == 'e')) {
                    return Keywords.VALUE;
                }
            }
            if (size == 9 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'A' || sql[4 + offset] == 'a') && (
                sql[5 + offset] == 'B' || sql[5 + offset] == 'b') && (sql[6 + offset] == 'L' || sql[6 + offset] == 'l')
                && (sql[7 + offset] == 'E' || sql[7 + offset] == 'e') && (sql[8 + offset] == 'S'
                || sql[8 + offset] == 's')) {
                return Keywords.VARIABLES;
            }
        }
        if (size == 4 && (sql[1 + offset] == 'C' || sql[1 + offset] == 'c') && (sql[2 + offset] == 'P'
            || sql[2 + offset] == 'p') && (sql[3 + offset] == 'U' || sql[3 + offset] == 'u')) {
            return Keywords.VCPU;
        }
        if (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') {
            if (size < 4 || size > 7) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'W'
                || sql[3 + offset] == 'w')) {
                return Keywords.VIEW;
            }
            if (size == 7 && (sql[2 + offset] == 'S' || sql[2 + offset] == 's') && (sql[3 + offset] == 'I'
                || sql[3 + offset] == 'i') && (sql[4 + offset] == 'B' || sql[4 + offset] == 'b') && (
                sql[5 + offset] == 'L' || sql[5 + offset] == 'l') && (sql[6 + offset] == 'E'
                || sql[6 + offset] == 'e')) {
                return Keywords.VISIBLE;
            }
        }
        return 0;
    }

    private static int parseW(byte[] sql, int offset, int size) {
        if (size < 4 || size > 13) {
            return 0;
        }
        if (sql[1 + offset] == 'A' || sql[1 + offset] == 'a') {
            if (size < 4 || size > 8) {
                return 0;
            }
            if (size == 4 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'T'
                || sql[3 + offset] == 't')) {
                return Keywords.WAIT;
            }
            if (size == 8 && (sql[2 + offset] == 'R' || sql[2 + offset] == 'r') && (sql[3 + offset] == 'N'
                || sql[3 + offset] == 'n') && (sql[4 + offset] == 'I' || sql[4 + offset] == 'i') && (
                sql[5 + offset] == 'N' || sql[5 + offset] == 'n') && (sql[6 + offset] == 'G' || sql[6 + offset] == 'g')
                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's')) {
                return Keywords.WARNINGS;
            }
        }
        if (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') {
            if (size == 4 && (sql[2 + offset] == 'E' || sql[2 + offset] == 'e') && (sql[3 + offset] == 'K'
                || sql[3 + offset] == 'k')) {
                return Keywords.WEEK;
            }
            if (size == 13 && (sql[2 + offset] == 'I' || sql[2 + offset] == 'i') && (sql[3 + offset] == 'G'
                || sql[3 + offset] == 'g') && (sql[4 + offset] == 'H' || sql[4 + offset] == 'h') && (
                sql[5 + offset] == 'T' || sql[5 + offset] == 't') && (sql[6 + offset] == '_' || sql[6 + offset] == '_')
                && (sql[7 + offset] == 'S' || sql[7 + offset] == 's') && (sql[8 + offset] == 'T'
                || sql[8 + offset] == 't') && (sql[9 + offset] == 'R' || sql[9 + offset] == 'r') && (
                sql[10 + offset] == 'I' || sql[10 + offset] == 'i') && (sql[11 + offset] == 'N'
                || sql[11 + offset] == 'n') && (sql[12 + offset] == 'G' || sql[12 + offset] == 'g')) {
                return Keywords.WEIGHT_STRING;
            }
        }
        if (size == 7 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'T'
            || sql[2 + offset] == 't') && (sql[3 + offset] == 'H' || sql[3 + offset] == 'h') && (sql[4 + offset] == 'O'
            || sql[4 + offset] == 'o') && (sql[5 + offset] == 'U' || sql[5 + offset] == 'u') && (sql[6 + offset] == 'T'
            || sql[6 + offset] == 't')) {
            return Keywords.WITHOUT;
        }
        if (size == 4 && (sql[1 + offset] == 'O' || sql[1 + offset] == 'o') && (sql[2 + offset] == 'R'
            || sql[2 + offset] == 'r') && (sql[3 + offset] == 'K' || sql[3 + offset] == 'k')) {
            return Keywords.WORK;
        }
        if (size == 7 && (sql[1 + offset] == 'R' || sql[1 + offset] == 'r') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'P' || sql[3 + offset] == 'p') && (sql[4 + offset] == 'P'
            || sql[4 + offset] == 'p') && (sql[5 + offset] == 'E' || sql[5 + offset] == 'e') && (sql[6 + offset] == 'R'
            || sql[6 + offset] == 'r')) {
            return Keywords.WRAPPER;
        }
        return 0;
    }

    private static int parseX(byte[] sql, int offset, int size) {
        if (size < 2 || size > 4) {
            return 0;
        }
        if (size == 4 && (sql[1 + offset] == '5' || sql[1 + offset] == '5') && (sql[2 + offset] == '0'
            || sql[2 + offset] == '0') && (sql[3 + offset] == '9' || sql[3 + offset] == '9')) {
            return Keywords.X509;
        }
        if (size == 2 && (sql[1 + offset] == 'A' || sql[1 + offset] == 'a')) {
            return Keywords.XA;
        }
        if (size == 3 && (sql[1 + offset] == 'I' || sql[1 + offset] == 'i') && (sql[2 + offset] == 'D'
            || sql[2 + offset] == 'd')) {
            return Keywords.XID;
        }
        if (size == 3 && (sql[1 + offset] == 'M' || sql[1 + offset] == 'm') && (sql[2 + offset] == 'L'
            || sql[2 + offset] == 'l')) {
            return Keywords.XML;
        }
        return 0;
    }

    private static int parseY(byte[] sql, int offset, int size) {
        if (size != 4) {
            return 0;
        }
        if (size == 4 && (sql[1 + offset] == 'E' || sql[1 + offset] == 'e') && (sql[2 + offset] == 'A'
            || sql[2 + offset] == 'a') && (sql[3 + offset] == 'R' || sql[3 + offset] == 'r')) {
            return Keywords.YEAR;
        }
        return 0;
    }

}

