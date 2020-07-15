package parser.lexer;

import mysql.charset.MySqlCharset;
import parser.ParseInfo;
import parser.ast.expression.ComparisionExpression;
import parser.token.KeywordsParser;
import parser.token.Token;
import parser.token.TokenParser;
import parser.util.BytesUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public class Lexer {
    private static int C_STYLE_COMMENT_VERSION = 80014;//mysql version

    private static final boolean[] hexFlags = new boolean[256];
    private static final boolean[] identifierFlags = new boolean[256];
    private static final boolean[] whitespaceFlags = new boolean[256];

    static {
        for (char b = 0; b < hexFlags.length; ++b) {
            if (b >= 'A' && b <= 'F') {
                hexFlags[b] = true;
            } else if (b >= 'a' && b <= 'f') {
                hexFlags[b] = true;
            } else if (b >= '0' && b <= '9') {
                hexFlags[b] = true;
            }
        }

        for (char b = 0; b < identifierFlags.length; ++b) {
            if (b >= 'A' && b <= 'Z') {
                identifierFlags[b] = true;
            } else if (b >= 'a' && b <= 'z') {
                identifierFlags[b] = true;
            } else if (b >= '0' && b <= '9') {
                identifierFlags[b] = true;
            }
        }
        identifierFlags['_'] = true;
        identifierFlags['$'] = true;

        whitespaceFlags[' '] = true;
        whitespaceFlags['\n'] = true;
        whitespaceFlags['\r'] = true;
        whitespaceFlags['\t'] = true;
        whitespaceFlags['\f'] = true;
        whitespaceFlags['\b'] = true;

    }

    private static boolean isHex(byte b) {
        return b < 256 && hexFlags[b];
    }

    private static boolean isDigit(byte b) {
        return b >= '0' && b <= '9';
    }

    private static boolean isIdentifier(byte b) {
        return b < 0 || b > identifierFlags.length || identifierFlags[b];
    }

    private static boolean isWhitespace(byte b) {
        return b > 0 && b <= whitespaceFlags.length && whitespaceFlags[b];
    }

    protected final static byte EOI = 0x1A;
    private long parseInfo;
    private byte[] sql;
    private int length;
    private int pos;
    private byte currentByte;
    private int offsetCache;
    private int sizeCache;
    private int token;

    private List<ComparisionExpression> conditions = new ArrayList<>();
    private byte[] affectedTable;
    private Charset charset;

    public Lexer(byte[] sql, Charset charset) throws SQLSyntaxErrorException {
        this.sql = sql;
        if (charset == null) {
            charset = MySqlCharset.UTF8_FOR_JAVA;
        }
        this.charset = charset;
        this.length = sql.length;
        this.pos = 0;
        this.currentByte = sql[pos];
        nextToken();
    }

    private int version;
    private boolean inCStyleComment;
    private boolean inCStyleCommentIgnore;
    private int paramIndex;
    private int tokenCache;
    private boolean identifierWithOpenQuate;

    public long tokenInfo() {
        return ((long)offsetCache << 32) | ((((long)sizeCache << 32) >> 32));
    }

    public int token() {
        if (tokenCache != 0) {
            return tokenCache;
        }
        return token;
    }

    public int nextToken() throws SQLSyntaxErrorException {
        if (tokenCache != 0) {
            tokenCache = 0;
            return token;
        }
        do {
            skipSeparator();
            token = nextTokenInternal();
        } while (inCStyleComment && inCStyleCommentIgnore || Token.PUNC_C_STYLE_COMMENT_END == token);
        return token;
    }

    private void skipSeparator() {
        for (; !eof(); ) {
            for (; isWhitespace(currentByte); scan())
                ;
            switch (currentByte) {
                case '#':
                    for (; scan() != '\n'; ) {
                        if (eof()) {
                            return;
                        }
                    }
                    scan();
                    continue;
                case '/':
                    if (stillHas(2) && '*' == sql[pos + 1]) {
                        boolean commentSkip;
                        scan(2);
                        commentSkip = true;
                        if (commentSkip) {
                            for (int state = 0; !eof(); scan()) {
                                if (state == 0) {
                                    if ('*' == currentByte) {
                                        state = 1;
                                    }
                                } else {
                                    if ('/' == currentByte) {
                                        scan();
                                        break;
                                    } else if ('*' != currentByte) {
                                        state = 0;
                                    }
                                }
                            }
                            continue;
                        }
                    }
                    return;
                case '-':
                    if (stillHas(3) && '-' == sql[pos + 1] && isWhitespace(sql[pos + 2])) {
                        scan(3);
                        for (; !eof(); scan()) {
                            if ('\n' == currentByte) {
                                scan();
                                break;
                            }
                        }
                        continue;
                    }
                default:
                    return;
            }
        }
    }

    private final boolean eof() {
        return pos >= sql.length;
    }

    private void setToken(int offset, int size, int token) {
        this.offsetCache = offset;
        this.sizeCache = size;
        if (token == Token.IDENTIFIER) {
            identifierWithOpenQuate = false;
        }
    }

    private final byte scan() {
        if (pos == sql.length - 1) {
            currentByte = EOI;
            ++pos;
            return currentByte;
        }
        return currentByte = sql[++pos];
    }

    private final byte scan(int skip) {
        if (pos + skip == sql.length) {
            currentByte = EOI;
            pos += skip;
            return currentByte;
        }
        return currentByte = sql[pos += skip];
    }

    private boolean stillHas(int howMany) {
        return pos + howMany <= length;
    }

    private int nextTokenInternal() throws SQLSyntaxErrorException {
        switch (currentByte) {
            case '0':
                if (pos < length - 1) {
                    switch (sql[pos + 1]) {
                        case 'x':
                            scan(2);
                            scanHexaDecimal(false);
                            return token;
                        case 'b':
                            scan(2);
                            scanBitField(false);
                            return token;
                    }
                }
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                scanNumber();
                return token;
            case '.':
                if (pos < length - 1 && isDigit(sql[pos + 1])) {
                    scanNumber();
                } else {
                    setToken(pos, 1, token = Token.PUNC_DOT);
                    scan();
                }
                return token;
            case '\'':
            case '"':
                scanString(false);
                return token;
            case 'n':
            case 'N':
                if (pos < length - 1 && sql[pos + 1] == '\'') {
                    scan();
                    scanString(true);
                    return token;
                }
                scanIdentifier();
                return token;
            case 'x':
            case 'X':
                if (pos < length - 1 && sql[pos + 1] == '\'') {
                    scan(2);
                    scanHexaDecimal(true);
                    return token;
                }
                scanIdentifier();
                return token;
            case 'b':
            case 'B':
                if (pos < length - 1 && sql[pos + 1] == '\'') {
                    scan(2);
                    scanBitField(true);
                    return token;
                }
                scanIdentifier();
                return token;
            case '@':
                if (pos < length - 1 && sql[pos + 1] == '@') {
                    scanSystemVariable();
                    return token;
                }
                scanUserVariable();
                return token;
            case '?':
                setToken(pos, 1, token = Token.QUESTION_MARK);
                scan();
                ++paramIndex;
                return token;
            case '(':
                setToken(pos, 1, token = Token.PUNC_LEFT_PAREN);
                scan();
                return token;
            case ')':
                setToken(pos, 1, token = Token.PUNC_RIGHT_PAREN);
                scan();
                return token;
            case '[':
                setToken(pos, 1, token = Token.PUNC_LEFT_BRACKET);
                scan();
                return token;
            case ']':
                setToken(pos, 1, token = Token.PUNC_RIGHT_BRACKET);
                scan();
                return token;
            case '{':
                setToken(pos, 1, token = Token.PUNC_LEFT_BRACE);
                scan();
                return token;
            case '}':
                setToken(pos, 1, token = Token.PUNC_RIGHT_BRACE);
                scan();
                return token;
            case ',':
                setToken(pos, 1, token = Token.PUNC_COMMA);
                scan();
                return token;
            case ';':
                setToken(pos, 1, token = Token.PUNC_SEMICOLON);
                scan();
                return token;
            case ':':
                if (pos < length - 1 && sql[pos + 1] == '=') {
                    setToken(pos, 2, token = Token.OP_ASSIGN);
                    scan(2);
                    return token;
                }
                setToken(pos, 1, token = Token.PUNC_COLON);
                scan();
                return token;
            case '=':
                setToken(pos, 1, token = Token.OP_EQUALS);
                scan();
                return token;
            case '~':
                setToken(pos, 1, token = Token.OP_TILDE);
                scan();
                return token;
            case '*':
                if (inCStyleComment && pos < length - 1 && sql[pos + 1] == '/') {
                    inCStyleComment = false;
                    inCStyleCommentIgnore = false;
                    setToken(pos, 2, token = Token.PUNC_C_STYLE_COMMENT_END);
                    scan(2);
                    return token;
                }
                setToken(pos, 1, token = Token.OP_ASTERISK);
                scan();
                return token;
            case '-':
                setToken(pos, 1, token = Token.OP_MINUS);
                scan();
                return token;
            case '+':
                setToken(pos, 1, token = Token.OP_PLUS);
                scan();
                return token;
            case '^':
                setToken(pos, 1, token = Token.OP_CARET);
                scan();
                return token;
            case '/':
                setToken(pos, 1, token = Token.OP_SLASH);
                scan();
                return token;
            case '%':
                setToken(pos, 1, token = Token.OP_PERCENT);
                scan();
                return token;
            case '&':
                if (pos < length - 1 && sql[pos + 1] == '&') {
                    setToken(pos, 2, token = Token.OP_LOGICAL_AND);
                    scan(2);
                    return token;
                }
                setToken(pos, 1, token = Token.OP_AMPERSAND);
                scan();
                return token;
            case '|':
                if (pos < length - 1 && sql[pos + 1] == '|') {
                    setToken(pos, 2, token = Token.OP_LOGICAL_OR);
                    scan(2);
                    return token;
                }
                setToken(pos, 1, token = Token.OP_VERTICAL_BAR);
                scan();
                return token;
            case '!':
                if (pos < length - 1 && sql[pos + 1] == '=') {
                    setToken(pos, 2, token = Token.OP_NOT_EQUALS);
                    scan(2);
                    return token;
                }
                setToken(pos, 1, token = Token.OP_EXCLAMATION);
                scan();
                return token;
            case '>':
                if (pos < length - 1) {
                    switch (sql[pos + 1]) {
                        case '=':
                            setToken(pos, 2, token = Token.OP_GREATER_OR_EQUALS);
                            scan(2);
                            return token;
                        case '>':
                            setToken(pos, 2, token = Token.OP_RIGHT_SHIFT);
                            scan(2);
                            return token;
                        default:
                            setToken(pos, 1, token = Token.OP_GREATER_THAN);
                            scan();
                            return token;
                    }
                }
                setToken(pos, 1, token = Token.OP_GREATER_THAN);
                scan();
                return token;
            case '<':
                if (pos < length - 1) {
                    switch (sql[pos + 1]) {
                        case '=':
                            if (sql[pos + 2] == '>') {
                                setToken(pos, 3, token = Token.OP_NULL_SAFE_EQUALS);
                                scan(3);
                                return token;
                            }
                            setToken(pos, 2, token = Token.OP_LESS_OR_EQUALS);
                            scan(2);
                            return token;
                        case '>':
                            setToken(pos, 2, token = Token.OP_LESS_OR_GREATER);
                            scan(2);
                            return token;
                        case '<':
                            setToken(pos, 2, token = Token.OP_LEFT_SHIFT);
                            scan(2);
                            return token;
                        default:
                            setToken(pos, 1, token = Token.OP_LESS_THAN);
                            scan();
                            return token;
                    }
                }
                setToken(pos, 1, token = Token.OP_LESS_THAN);
                scan();
                return token;
            case '`':
                scanIdentifierWithAccent();
                return token;
            default:
                if (isIdentifier(currentByte)) {
                    scanIdentifier();
                } else if (eof()) {
                    setToken(pos, 1, token = Token.EOF);
                } else {
                    throw new SQLSyntaxErrorException("unsupported character: " + (char)currentByte);
                }
                return token;
        }
    }

    private void scanHexaDecimal(boolean quoteMode) throws SQLSyntaxErrorException {
        offsetCache = pos;
        for (; isHex(currentByte); scan())
            ;
        sizeCache = pos - offsetCache;
        if (quoteMode) {
            if (currentByte != '\'') {
                throw new SQLSyntaxErrorException("invalid char for hex: " + (char)currentByte);
            }
            scan();
        } else if (isIdentifier(currentByte)) {
            scanIdentifierFromNumber(offsetCache - 2, sizeCache + 2);
            return;
        }
        setToken(offsetCache, sizeCache, token = Token.LITERAL_HEX);
    }

    private void scanBitField(boolean quoteMode) throws SQLSyntaxErrorException {
        offsetCache = pos;
        for (; currentByte == '0' || currentByte == '1'; scan())
            ;
        sizeCache = pos - offsetCache;
        if (quoteMode) {
            if (currentByte != '\'') {
                throw new SQLSyntaxErrorException("invalid char for bit: " + (char)currentByte);
            }
            scan();
        } else if (isIdentifier(currentByte)) {
            scanIdentifierFromNumber(offsetCache - 2, sizeCache + 2);
            return;
        }
        setToken(offsetCache, sizeCache, token = Token.LITERAL_BIT);
    }

    private void scanNumber() throws SQLSyntaxErrorException {
        offsetCache = pos;
        sizeCache = 1;
        final boolean fstDot = currentByte == '.';
        boolean dot = fstDot;
        boolean sign = false;
        int state = fstDot ? 1 : 0;

        for (; !eof(); ++sizeCache) {
            scan();
            switch (state) {
                case 0:
                    if (isDigit(currentByte)) {
                    } else if (currentByte == '.') {
                        dot = true;
                        state = 1;
                    } else if (currentByte == 'e' || currentByte == 'E') {
                        state = 3;
                    } else if (isIdentifier(currentByte)) {
                        scanIdentifierFromNumber(offsetCache, sizeCache);
                        return;
                    } else {
                        setToken(offsetCache, sizeCache, token = Token.LITERAL_NUM_PURE_DIGIT);
                        return;
                    }
                    break;
                case 1:
                    if (isDigit(currentByte)) {
                        state = 2;
                    } else if (currentByte == 'e' || currentByte == 'E') {
                        state = 3;
                    } else if (isIdentifier(currentByte) && fstDot) {
                        sizeCache = 1;
                        currentByte = sql[pos = offsetCache + 1];
                        setToken(offsetCache, sizeCache, token = Token.PUNC_DOT);
                        return;
                    } else {
                        setToken(offsetCache, sizeCache, token = Token.LITERAL_NUM_MIX_DIGIT);
                        return;
                    }
                    break;
                case 2:
                    if (isDigit(currentByte)) {
                    } else if (currentByte == 'e' || currentByte == 'E') {
                        state = 3;
                    } else if (isIdentifier(currentByte) && fstDot) {
                        sizeCache = 1;
                        currentByte = sql[pos = offsetCache + 1];
                        setToken(offsetCache, sizeCache, token = Token.PUNC_DOT);
                        return;
                    } else {
                        setToken(offsetCache, sizeCache, token = Token.LITERAL_NUM_MIX_DIGIT);
                        return;
                    }
                    break;
                case 3:
                    if (isDigit(currentByte)) {
                        state = 5;
                    } else if (currentByte == '+' || currentByte == '-') {
                        sign = true;
                        state = 4;
                    } else if (fstDot) {
                        sizeCache = 1;
                        currentByte = sql[pos = offsetCache + 1];
                        setToken(offsetCache, sizeCache, token = Token.PUNC_DOT);
                        return;
                    } else if (!dot) {
                        if (isIdentifier(currentByte)) {
                            scanIdentifierFromNumber(offsetCache, sizeCache);
                        } else {
                            int tok = TokenParser.get(sql, offsetCache, sizeCache);
                            token = tok == 0 ? Token.IDENTIFIER : tok;
                            setToken(offsetCache, sizeCache, token);
                        }
                        return;
                    } else {
                        throw new SQLSyntaxErrorException(
                            "invalid char after '.' and 'e' for as part of number: " + (char)currentByte);
                    }
                    break;
                case 4:
                    if (isDigit(currentByte)) {
                        state = 5;
                        break;
                    } else if (fstDot) {
                        sizeCache = 1;
                        currentByte = sql[pos = offsetCache + 1];
                        setToken(offsetCache, sizeCache, token = Token.PUNC_DOT);
                    } else if (!dot) {
                        currentByte = sql[--pos];
                        --sizeCache;
                        int tok = TokenParser.get(sql, offsetCache, sizeCache);
                        token = tok == 0 ? Token.IDENTIFIER : tok;
                        setToken(offsetCache, sizeCache, token);
                    } else {
                        throw new SQLSyntaxErrorException("expect digit char after SIGN for 'e': " + (char)currentByte);
                    }
                    return;
                case 5:
                    if (isDigit(currentByte)) {
                        break;
                    } else if (isIdentifier(currentByte)) {
                        if (fstDot) {
                            sizeCache = 1;
                            currentByte = sql[pos = offsetCache + 1];
                            setToken(offsetCache, sizeCache, token = Token.PUNC_DOT);
                        } else if (!dot) {
                            if (sign) {
                                currentByte = sql[pos = offsetCache];
                                scanIdentifierFromNumber(pos, 0);
                            } else {
                                scanIdentifierFromNumber(offsetCache, sizeCache);
                            }
                        } else {
                            setToken(offsetCache, sizeCache, token = Token.LITERAL_NUM_MIX_DIGIT);
                        }
                    } else {
                        setToken(offsetCache, sizeCache, token = Token.LITERAL_NUM_MIX_DIGIT);
                    }
                    return;
            }
        }
        switch (state) {
            case 0:
                setToken(offsetCache, sizeCache, token = Token.LITERAL_NUM_PURE_DIGIT);
                return;
            case 1:
                if (fstDot) {
                    setToken(offsetCache, sizeCache, token = Token.PUNC_DOT);
                    return;
                }
            case 2:
            case 5:
                setToken(offsetCache, sizeCache, token = Token.LITERAL_NUM_MIX_DIGIT);
                return;
            case 3:
                if (fstDot) {
                    sizeCache = 1;
                    currentByte = sql[pos = offsetCache + 1];
                    setToken(offsetCache, sizeCache, token = Token.PUNC_DOT);
                } else if (!dot) {
                    int tok = TokenParser.get(sql, offsetCache, sizeCache);
                    token = tok == 0 ? Token.IDENTIFIER : tok;
                    setToken(offsetCache, sizeCache, token);
                } else {
                    throw new SQLSyntaxErrorException("expect digit char after SIGN for 'e': " + (char)currentByte);
                }
                return;
            case 4:
                if (fstDot) {
                    sizeCache = 1;
                    currentByte = sql[pos = offsetCache + 1];
                    setToken(offsetCache, sizeCache, token = Token.PUNC_DOT);
                } else if (!dot) {
                    currentByte = sql[--pos];
                    --sizeCache;
                    int tok = TokenParser.get(sql, offsetCache, sizeCache);
                    token = tok == 0 ? Token.IDENTIFIER : tok;
                    setToken(offsetCache, sizeCache, token);
                } else {
                    throw new SQLSyntaxErrorException("expect digit char after SIGN for 'e': " + (char)currentByte);
                }
                return;
        }
    }

    private void scanString(boolean nchar) throws SQLSyntaxErrorException {
        boolean dq = false;
        if (currentByte == '\'') {
        } else if (currentByte == '"') {
            dq = true;
        } else {
            throw new SQLSyntaxErrorException("first char must be \" or '");
        }
        offsetCache = pos;
        int size = 1;
        if (MySqlCharset.GBK_FOR_JAVA == charset) {
            size = scanStringAsGbk(dq, size);
        } else {
            // 多字节utf8编码不会使用ASCII字节
            size = scanStringAsLatin1(dq, size);
        }
        sizeCache = size;
        setToken(offsetCache, size, token = nchar ? Token.LITERAL_NCHARS : Token.LITERAL_CHARS);
    }

    private void scanIdentifierFromNumber(int initOffset, int initSize) {
        offsetCache = initOffset;
        sizeCache = initSize;
        for (; isIdentifier(currentByte); ++sizeCache) {
            scan();
        }
        int tok = TokenParser.get(sql, offsetCache, sizeCache);
        token = tok == 0 ? Token.IDENTIFIER : tok;
        setToken(offsetCache, sizeCache, token);
    }

    private void scanIdentifier() {
        if (currentByte == '$') {
            if (scan() == '{') {
                scanPlaceHolder();
            } else {
                scanIdentifierFromNumber(pos - 1, 1);
            }
        } else {
            scanIdentifierFromNumber(pos, 0);
        }
    }

    private void scanPlaceHolder() {
        offsetCache = pos + 1;
        sizeCache = 0;
        for (scan(); currentByte != '}' && !eof(); ++sizeCache) {
            scan();
        }
        if (currentByte == '}')
            scan();
        setToken(offsetCache, sizeCache, token = Token.PLACE_HOLDER);
    }

    private int scanStringAsGbk(boolean dq, int size) throws SQLSyntaxErrorException {
        if (dq) {
            loop:
            while (true) {
                byte sc = scan();
                if ((sc & 0xff) > 0x7f) {
                    size++;
                    scan();
                    size++;
                    continue;
                } else {
                    switch (sc) {
                        case '\'':
                            size++;
                            size++;
                            break;
                        case '\\':
                            size++;
                            scan();
                            size++;
                            continue;
                        case '"':
                            size++;
                            scan();
                            if (currentByte == '"') {
                                size++;
                                scan();
                                continue;
                            }
                            break loop;
                        default:
                            if (eof()) {
                                throw new SQLSyntaxErrorException("unclosed string");
                            }
                            size++;
                            continue;
                    }
                }
            }
        } else {
            loop:
            while (true) {
                byte sc = scan();
                if ((sc & 0xff) > 0x7f) {
                    size++;
                    scan();
                    size++;
                    continue;
                } else {
                    switch (sc) {
                        case '\\':
                            size++;
                            scan();
                            size++;
                            continue;
                        case '\'':
                            if (pos + 1 < length && sql[pos + 1] == '\'') {
                                size++;
                                scan();
                                size++;
                                continue;
                            }
                            scan();
                            size++;
                            break loop;
                        default:
                            if (eof()) {
                                throw new SQLSyntaxErrorException("unclosed string");
                            }
                            size++;
                            continue;
                    }
                }
            }
        }
        return size;
    }

    private int scanStringAsLatin1(boolean dq, int size) throws SQLSyntaxErrorException {
        if (dq) {
            loop:
            while (true) {
                switch (scan()) {
                    case '\'':
                        size++;
                        size++;
                        break;
                    case '\\':
                        size++;
                        scan();
                        size++;
                        continue;
                    case '"':
                        size++;
                        scan();
                        if (currentByte == '"') {
                            size++;
                            scan();
                            continue;
                        }
                        break loop;
                    default:
                        if (eof()) {
                            throw new SQLSyntaxErrorException("unclosed string");
                        }
                        size++;
                        continue;
                }
            }
        } else {
            loop:
            while (true) {
                switch (scan()) {
                    case '\\':
                        size++;
                        scan();
                        size++;
                        continue;
                    case '\'':
                        if (pos + 1 < length && sql[pos + 1] == '\'') {
                            size++;
                            scan();
                            size++;
                            continue;
                        }
                        scan();
                        size++;
                        break loop;
                    default:
                        if (eof()) {
                            throw new SQLSyntaxErrorException("unclosed string");
                        }
                        size++;
                        continue;
                }
            }
        }
        return size;
    }

    private void scanIdentifierWithAccent() {
        offsetCache = pos;
        for (; !eof(); ) {
            scan();
            if (currentByte == '`' && scan() != '`') {
                break;
            }
        }
        setToken(offsetCache, pos - offsetCache, token = Token.IDENTIFIER);
        identifierWithOpenQuate = true;
    }

    private void scanSystemVariable() throws SQLSyntaxErrorException {
        if (currentByte != '@' || sql[pos + 1] != '@') {
            throw new SQLSyntaxErrorException("first char must be @@");
        }
        offsetCache = pos + 2;
        sizeCache = 0;
        scan(2);
        if (currentByte == '`') {
            for (++sizeCache; ; ++sizeCache) {
                if (scan() == '`') {
                    ++sizeCache;
                    if (scan() != '`') {
                        break;
                    }
                }
            }
        } else {
            for (; isIdentifier(currentByte); ++sizeCache) {
                scan();
            }
        }
        setToken(offsetCache, sizeCache, token = Token.SYS_VAR);
    }

    private void scanUserVariable() throws SQLSyntaxErrorException {
        if (currentByte != '@') {
            throw new SQLSyntaxErrorException("first char must be @");
        }
        offsetCache = pos;
        sizeCache = 1;

        boolean dq = false;
        switch (scan()) {
            case '"':
                dq = true;
            case '\'':
                loop1:
                for (++sizeCache; ; ++sizeCache) {
                    switch (scan()) {
                        case '\\':
                            ++sizeCache;
                            scan();
                            break;
                        case '"':
                            if (dq) {
                                ++sizeCache;
                                if (scan() == '"') {
                                    break;
                                }
                                break loop1;
                            }
                            break;
                        case '\'':
                            if (!dq) {
                                ++sizeCache;
                                if (scan() == '\'') {
                                    break;
                                }
                                break loop1;
                            }
                            break;
                    }
                }
                break;
            case '`':
                loop1:
                for (++sizeCache; ; ++sizeCache) {
                    switch (scan()) {
                        case '`':
                            ++sizeCache;
                            if (scan() == '`') {
                                break;
                            }
                            break loop1;
                    }
                }
                break;
            default:
                for (; isIdentifier(currentByte) || currentByte == '.'; ++sizeCache) {
                    scan();
                }
        }
        setToken(offsetCache, sizeCache, token = Token.USR_VAR);
    }

    public int getVersion() {
        return version;
    }

    public int paramIndex() {
        return paramIndex;
    }

    public byte[] bytesValue() {
        return BytesUtil.getValue(sql, offsetCache, sizeCache);
    }

    public String stringValue() {
        return new String(BytesUtil.getValue(sql, offsetCache, sizeCache), charset);
    }

    public String stringValueUppercase() {
        return stringValue().toUpperCase();
    }

    public String stringValue(long info) {
        return new String(BytesUtil.getValue(sql, info), charset);
    }

    public String stringValueUppercase(long index) {
        return stringValue(index).toUpperCase();
    }

    public BigDecimal decimalValue() {
        return new BigDecimal(stringValue());
    }

    public int parseKeyword() {
        return KeywordsParser.get(sql, offsetCache, sizeCache);
    }

    public int getOffset() {
        return offsetCache;
    }

    public void addParseInfo(long info) {
        parseInfo |= info;
    }

    public void removeParseInfo(long info) {
        parseInfo &= ~info;
    }

    public long getAndResetParseInfo() {
        long tmp = parseInfo;
        parseInfo = 0;
        return tmp;
    }

    public Number integerValue() {
        byte[] data = BytesUtil.getValue(sql, offsetCache, sizeCache);
        if (data.length < 10 || data.length == 10 && (data[0] < '2' || data[0] == '2' && data[1] == '0')) {
            int rst = 0;
            int end = data.length;
            for (int i = 0; i < end; ++i) {
                rst = (rst << 3) + (rst << 1);
                rst += data[i] - '0';
            }
            return rst;
        } else if (data.length < 19 || data.length == 19 && data[0] < '9') {
            long rst = 0;
            int end = data.length;
            for (int i = 0; i < end; ++i) {
                rst = (rst << 3) + (rst << 1);
                rst += data[i] - '0';
            }
            return rst;
        } else {
            return new BigInteger(new String(data), 10);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("token=").append(Token.getInfo(token)).append(", sqlLeft=")
            .append(new String(sql, offsetCache, sql.length - offsetCache, charset));
        return sb.toString();
    }

    public void addCacheToken(int token) {
        this.tokenCache = token;
    }

    public byte[] getSQL() {
        return sql;
    }

    public int getSize() {
        return sizeCache;
    }

    public int parseKeyword(long info) {
        return KeywordsParser.get(sql, BytesUtil.getOffset(info), BytesUtil.getSize(info));
    }

    public char charAtIndex(long info) {
        return (char)sql[BytesUtil.getOffset(info)];
    }

    protected SQLSyntaxErrorException err(String msg) throws SQLSyntaxErrorException {
        String errMsg = msg + ". " + toString();
        throw new SQLSyntaxErrorException(errMsg);
    }

    public boolean isIdentifierWithOpenQuate() {
        return identifierWithOpenQuate;
    }

    public List<ComparisionExpression> getAndResetConditions() {
        List<ComparisionExpression> tmp = conditions;
        conditions = null;
        return tmp;
    }

    public void addCondition(ComparisionExpression condition) {
        if (ParseInfo.hasOrCondition(parseInfo) || ParseInfo.hasXorCondition(parseInfo)) {
            if (this.conditions != null) {
                this.conditions.clear();
            }
        } else {
            if (this.conditions == null) {
                this.conditions = new ArrayList<>();
            }
            this.conditions.add(condition);
        }
    }

    public byte[] getAndResetAffectedTables() {
        byte[] tmp = affectedTable;
        affectedTable = null;
        return tmp;
    }

    public void addAffectedTable(byte[] tableNameUppercase) {
        if (affectedTable == null) {
            affectedTable = tableNameUppercase;
        } else {
            removeParseInfo(ParseInfo.SINGLE_TABLE);
        }
    }

    public void toTheEnd() {
        setToken(offsetCache, pos - offsetCache, token = Token.EOF);
    }

    public boolean checkAfterFirstSemicolon() {
        if (sql.length > pos) {
            if (sql[pos] != '\r' && sql[pos] != '\t' && sql[pos] != '\n' && sql[pos] != ' ') {
                return false;
            }
        }
        return true;
    }

}
