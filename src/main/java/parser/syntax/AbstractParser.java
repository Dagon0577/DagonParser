package parser.syntax;

import parser.ParseInfo;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.VarsPrimary;
import parser.ast.expression.primary.Wildcard;
import parser.lexer.Lexer;
import parser.token.Keywords;
import parser.token.Token;
import parser.util.BytesUtil;

import java.sql.SQLSyntaxErrorException;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public abstract class AbstractParser {
    protected final Lexer lexer;

    private static final byte[] VARS_PERSIST = "PERSIST".getBytes();
    private static final byte[] VARS_PERSIST_ONLY = "PERSIST_ONLY".getBytes();

    public AbstractParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public VarsPrimary systemVariable() throws SQLSyntaxErrorException {
        long scopeStr = lexer.tokenInfo();
        byte[] data = lexer.bytesValue();
        Integer scope = null;
        switch (lexer.parseKeyword()) {
            case Keywords.GLOBAL:
                scope = VarsPrimary.SCOPE_GLOBAL;
                break;
            case Keywords.SESSION:
            case Keywords.LOCAL:
                scope = VarsPrimary.SCOPE_SESSION;
                break;
        }
        if (scope == null) {
            if (BytesUtil.equalsIgnoreCase(data, VARS_PERSIST)) {
                scope = VarsPrimary.SCOPE_PERSIST;
            } else if (BytesUtil.equalsIgnoreCase(data, VARS_PERSIST_ONLY)) {
                scope = VarsPrimary.SCOPE_PERSIST_ONLY;
            }
        }
        if (scope != null) {
            lexer.nextToken();
            match(Token.PUNC_DOT);
        }
        Identifier sysVarName = identifier(false);
        return new VarsPrimary(VarsPrimary.SYS_VAR, scope, scopeStr, sysVarName.getIdText());
    }

    protected int match(int... expectToken) throws SQLSyntaxErrorException {
        int token = lexer.token();
        for (int i = 0; i < expectToken.length; i++) {
            if (token == expectToken[i]) {
                if (token != Token.EOF || i < expectToken.length - 1) {
                    lexer.nextToken();
                }
                return i;
            }
        }
        if (expectToken.length == 1) {
            throw new SQLSyntaxErrorException("expect " + Token.getInfo(expectToken[0]));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expectToken.length; ++i) {
            sb.append(" ").append(Token.getInfo(expectToken[i]));
            if (i + 1 < expectToken.length) {
                sb.append(" |");
            }
        }
        throw new SQLSyntaxErrorException("expect " + sb.toString());
    }

    protected int matchKeywords(int... expectKeywords) throws SQLSyntaxErrorException {
        if (lexer.token() != Token.IDENTIFIER) {
            throw new SQLSyntaxErrorException("expect an identifier");
        }
        int token = lexer.parseKeyword();
        if (token > 0) {
            for (int i = 0; i < expectKeywords.length; i++) {
                if (token == expectKeywords[i]) {
                    if (lexer.token() != Token.EOF || i < expectKeywords.length - 1) {
                        lexer.nextToken();
                    }
                    return i;
                }
            }
        }
        if (expectKeywords.length == 1) {
            throw new SQLSyntaxErrorException("expect " + Keywords.getInfo(expectKeywords[0]));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expectKeywords.length; ++i) {
            sb.append(" ").append(Keywords.getInfo(expectKeywords[i]));
            if (i + 1 < expectKeywords.length) {
                sb.append(" |");
            }
        }
        throw new SQLSyntaxErrorException("expect " + sb.toString());
    }

    public Identifier identifier() throws SQLSyntaxErrorException {
        return identifier(false);
    }

    public Identifier identifier(boolean isTable) throws SQLSyntaxErrorException {
        if (lexer.token() == 0) {
            lexer.nextToken();
        }
        Identifier id;
        switch (lexer.token()) {
            case Token.OP_ASTERISK:
                id = new Wildcard(null);
                lexer.nextToken();
                break;
            case Token.IDENTIFIER:
                id = new Identifier(null, lexer.bytesValue(), lexer.isIdentifierWithOpenQuate());
                lexer.nextToken();
                break;
            case Token.LITERAL_NUM_MIX_DIGIT:
            case Token.LITERAL_NUM_PURE_DIGIT:
                throw new SQLSyntaxErrorException("expect identifier");
            default:
                id = new Identifier(null, lexer.bytesValue(), lexer.isIdentifierWithOpenQuate());
                lexer.nextToken();
                break;
        }
        for (; lexer.token() == Token.PUNC_DOT; ) {
            switch (lexer.nextToken()) {
                case Token.OP_ASTERISK:
                    lexer.nextToken();
                    return new Wildcard(id);
                case Token.IDENTIFIER:
                    id = new Identifier(id, lexer.bytesValue(), lexer.isIdentifierWithOpenQuate());
                    lexer.nextToken();
                    break;
                case Token.LITERAL_NUM_MIX_DIGIT:
                case Token.LITERAL_NUM_PURE_DIGIT:
                    throw new SQLSyntaxErrorException("expect identifier");
                default:
                    id = new Identifier(null, lexer.bytesValue(), lexer.isIdentifierWithOpenQuate());
                    lexer.nextToken();
                    break;
            }
        }
        if (isTable) {
            lexer.addAffectedTable(BytesUtil.toUpperCaseWithoutQuote(id.getIdText(), id.isWithOpenQuate()));
            if (id.getParent() != null) {
                lexer.addParseInfo(ParseInfo.ID_WITH_SCHEMA);
            }
        } else if (id.getParent() != null) {
            lexer.addParseInfo(ParseInfo.ID_WITH_TABLE);
            if (id.getParent().getParent() != null) {
                lexer.addParseInfo(ParseInfo.ID_WITH_SCHEMA);
            }
        }
        return id;
    }

    protected boolean equalsKeyword(int expectKeyword) throws SQLSyntaxErrorException {
        return expectKeyword == lexer.parseKeyword();
    }

    protected int equalsKeywords(int... expectKeywords) throws SQLSyntaxErrorException {
        int token = lexer.parseKeyword();
        for (int i = 0; i < expectKeywords.length; i++) {
            if (token == expectKeywords[i]) {
                return i;
            }
        }
        return -1;
    }
}
