package parser.syntax;

import parser.ParseInfo;
import parser.ast.expression.*;
import parser.ast.expression.primary.FunctionExpression;
import parser.ast.expression.primary.ParamMarker;
import parser.ast.expression.primary.*;
import parser.ast.expression.primary.VarsPrimary;
import parser.ast.expression.primary.literal.*;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.ddl.DataType;
import parser.lexer.Lexer;
import parser.token.*;
import parser.util.BytesUtil;
import parser.util.Pair;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public class ExprParser extends AbstractParser {

    private MySQLDMLSelectParser selectParser;
    private boolean inWhere = false;

    public ExprParser(Lexer lexer) {
        super(lexer);
    }

    public void setSelectParser(MySQLDMLSelectParser selectParser) {
        this.selectParser = selectParser;
    }

    public void setInWhere(boolean inWhere) {
        this.inWhere = inWhere;
    }

    public Expression expression() throws SQLSyntaxErrorException {
        int token = lexer.token();
        if (token == 0) {
            token = lexer.nextToken();
        }
        if (token == Token.EOF) {
            throw new SQLSyntaxErrorException("unexpeced EOF");
        }
        Expression left = logicOrExpression();
        if (lexer.token() == Token.OP_ASSIGN) {
            lexer.nextToken();
            Expression right = expression();
            lexer.addParseInfo(ParseInfo.VAR_ASSIGNMENT);
            return new AssignmentExpression(left, right);
        }
        return left;
    }

    private Expression logicOrExpression() throws SQLSyntaxErrorException {
        LogicalExpression or = null;
        boolean isOracle = false;
        for (Expression expr = logicalXORExpression(); ; ) {
            switch (lexer.token()) {
                case Token.OP_LOGICAL_OR:
                case Token.KW_OR:
                    if (inWhere) {
                        lexer.addParseInfo(ParseInfo.OR_CONDITION);
                    }
                    lexer.nextToken();
                    if (or == null) {
                        or = new LogicalExpression(false);
                        or.appendOperand(expr);
                        expr = or;
                        or.setOracle(isOracle);
                    }
                    Expression newExpr = logicalXORExpression();
                    or.appendOperand(newExpr);
                    break;
                default:
                    return expr;
            }
        }
    }

    private Expression logicalXORExpression() throws SQLSyntaxErrorException {
        for (Expression expr = logicalAndExpression(); ; ) {
            switch (lexer.token()) {
                case Token.KW_XOR:
                    if (inWhere) {
                        lexer.addParseInfo(ParseInfo.XOR_CONDITION);
                    }
                    lexer.nextToken();
                    Expression newExpr = logicalAndExpression();
                    expr = new LogicalXORExpression(expr, newExpr);
                    break;
                default:
                    return expr;
            }
        }
    }

    public Expression logicalAndExpression() throws SQLSyntaxErrorException {
        LogicalExpression and = null;
        for (Expression expr = logicalNotExpression(); ; ) {
            switch (lexer.token()) {
                case Token.OP_LOGICAL_AND:
                case Token.KW_AND:
                    lexer.nextToken();
                    if (and == null) {
                        and = new LogicalExpression(true);
                        and.appendOperand(expr);
                        expr = and;
                    }
                    Expression newExpr = logicalNotExpression();
                    and.appendOperand(newExpr);
                    break;
                default:
                    return expr;
            }
        }
    }

    private Expression logicalNotExpression() throws SQLSyntaxErrorException {
        int not = 0;
        for (; lexer.token() == Token.KW_NOT; ++not) {
            lexer.nextToken();
        }
        Expression expr = comparisionExpression();
        for (; not > 0; --not) {
            expr = new UnaryOperatorExpression(expr, UnaryOperatorExpression.LOGIC_NOT);
        }
        if (inWhere && expr instanceof ComparisionExpression) {
            lexer.addCondition((ComparisionExpression)expr);
        }
        return expr;
    }

    private Expression comparisionExpression() throws SQLSyntaxErrorException {
        Expression temp;
        for (Expression fst = bitOrExpression(-1); ; ) {
            switch (lexer.token()) {
                case Token.KW_NOT: {
                    lexer.nextToken();
                    switch (lexer.token()) {
                        case Token.KW_BETWEEN: {
                            lexer.nextToken();
                            Expression snd = comparisionExpression();
                            match(Token.KW_AND);
                            Expression trd = comparisionExpression();
                            return new TernaryOperatorExpression(fst, snd, trd, TernaryOperatorExpression.BETWEEN,
                                true);
                        }
                        case Token.KW_RLIKE:
                        case Token.KW_REGEXP: {
                            lexer.nextToken();
                            temp = bitOrExpression(-1);
                            fst = new ComparisionExpression(fst, temp, ComparisionExpression.REGEXP).setNot(true);
                            continue;
                        }
                        case Token.KW_LIKE: {
                            lexer.nextToken();
                            temp = bitOrExpression(-1);
                            Expression escape = null;
                            if (equalsKeyword(Keywords.ESCAPE)) {
                                lexer.nextToken();
                                escape = bitOrExpression(-1);
                            }
                            fst = new ComparisionExpression(fst, temp, ComparisionExpression.LIKE).setNot(true)
                                .setEscape(escape);
                            continue;
                        }
                        case Token.KW_IN: {
                            lexer.nextToken();
                            if (lexer.token() != Token.PUNC_LEFT_PAREN && lexer.token() != Token.QUESTION_MARK) {
                                lexer.addCacheToken(Token.KW_IN);
                                return fst;
                            }
                            Expression in = rightOprandOfIn();
                            fst = new ComparisionExpression(fst, in, ComparisionExpression.IN).setNot(true);
                            continue;
                        }
                        case Token.LITERAL_NULL: {
                            lexer.nextToken();
                            return fst;
                        }
                        default:
                            throw new SQLSyntaxErrorException(
                                "unexpect token after NOT: " + Token.getInfo(lexer.token()));
                    }
                }
                case Token.KW_BETWEEN: {
                    lexer.nextToken();
                    Expression snd = comparisionExpression();
                    match(Token.KW_AND);
                    Expression trd = comparisionExpression();
                    return new TernaryOperatorExpression(fst, snd, trd, TernaryOperatorExpression.BETWEEN, false);
                }
                case Token.KW_RLIKE:
                case Token.KW_REGEXP: {
                    lexer.nextToken();
                    temp = bitOrExpression(-1);
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.REGEXP).setNot(false);
                    continue;
                }
                case Token.KW_LIKE: {
                    lexer.nextToken();
                    temp = bitOrExpression(-1);
                    Expression escape = null;
                    if (equalsKeyword(Keywords.ESCAPE)) {
                        lexer.nextToken();
                        escape = bitOrExpression(-1);
                    }
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.LIKE).setNot(false)
                        .setEscape(escape);
                    continue;
                }
                case Token.KW_IN: {
                    lexer.nextToken();
                    if (lexer.token() != Token.PUNC_LEFT_PAREN && lexer.token() != Token.QUESTION_MARK) {
                        lexer.addCacheToken(Token.KW_IN);
                        return fst;
                    }
                    Expression in = rightOprandOfIn();
                    fst = new ComparisionExpression(fst, in, ComparisionExpression.IN).setNot(false);
                    continue;
                }
                case Token.KW_IS: {
                    switch (lexer.nextToken()) {
                        case Token.KW_NOT:
                            switch (lexer.nextToken()) {
                                case Token.LITERAL_NULL:
                                    lexer.nextToken();
                                    fst = new IsExpression(fst, IsExpression.IS_NOT_NULL);
                                    continue;
                                case Token.LITERAL_BOOL_FALSE:
                                    lexer.nextToken();
                                    fst = new IsExpression(fst, IsExpression.IS_NOT_FALSE);
                                    continue;
                                case Token.LITERAL_BOOL_TRUE:
                                    lexer.nextToken();
                                    fst = new IsExpression(fst, IsExpression.IS_NOT_TRUE);
                                    continue;
                                default:
                                    matchKeywords(Keywords.UNKNOWN);
                                    fst = new IsExpression(fst, IsExpression.IS_NOT_UNKNOWN);
                                    continue;
                            }
                        case Token.LITERAL_NULL:
                            lexer.nextToken();
                            fst = new IsExpression(fst, IsExpression.IS_NULL);
                            continue;
                        case Token.LITERAL_BOOL_FALSE:
                            lexer.nextToken();
                            fst = new IsExpression(fst, IsExpression.IS_FALSE);
                            continue;
                        case Token.LITERAL_BOOL_TRUE:
                            lexer.nextToken();
                            fst = new IsExpression(fst, IsExpression.IS_TRUE);
                            continue;
                        default:
                            matchKeywords(Keywords.UNKNOWN);
                            fst = new IsExpression(fst, IsExpression.IS_UNKNOWN);
                            continue;
                    }
                }
                case Token.OP_EQUALS: {
                    lexer.nextToken();
                    temp = anyAllExpression();
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.EQUALS);
                    continue;
                }
                case Token.OP_NULL_SAFE_EQUALS:
                    lexer.nextToken();
                    temp = bitOrExpression(-1);
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.NULL_SAFE_EQUALS);
                    continue;
                case Token.OP_GREATER_OR_EQUALS:
                    lexer.nextToken();
                    temp = anyAllExpression();
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.GREATER_THAN_OR_QEUALS);
                    continue;
                case Token.OP_GREATER_THAN:
                    lexer.nextToken();
                    temp = anyAllExpression();
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.GREATER_THAN);
                    continue;
                case Token.OP_LESS_OR_EQUALS:
                    lexer.nextToken();
                    temp = anyAllExpression();
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.LESS_THAN_OR_EQUALS);
                    continue;
                case Token.OP_LESS_THAN:
                    lexer.nextToken();
                    temp = anyAllExpression();
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.LESS_THAN);
                    continue;
                case Token.OP_LESS_OR_GREATER:
                    lexer.nextToken();
                    temp = anyAllExpression();
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.LESS_OR_GREATER_THAN);
                    continue;
                case Token.OP_NOT_EQUALS:
                    lexer.nextToken();
                    temp = anyAllExpression();
                    fst = new ComparisionExpression(fst, temp, ComparisionExpression.NOT_EQUALS);
                    continue;
                case Token.IDENTIFIER:
                    if (lexer.parseKeyword() == Keywords.SOUNDS) {
                        lexer.nextToken();
                        match(Token.KW_LIKE);
                        temp = bitOrExpression(-1);
                        fst = new ComparisionExpression(fst, temp, ComparisionExpression.SOUNDS_LIKE);
                        continue;
                    }
                default:
                    return fst;
            }
        }
    }

    private Expression anyAllExpression() throws SQLSyntaxErrorException {
        QueryExpression subquery = null;
        switch (lexer.token()) {
            case Token.KW_ALL:
                lexer.nextToken();
                if (lexer.token() == Token.QUESTION_MARK) {
                    int index = lexer.paramIndex();
                    lexer.nextToken();
                    subquery = new ParamMarker(index);
                } else {
                    match(Token.PUNC_LEFT_PAREN);
                    subquery = subQuery();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new UnaryOperatorExpression(subquery, UnaryOperatorExpression.SUBQUERY_ALL);
            default:
                int matchIndex = equalsKeywords(Keywords.SOME, Keywords.ANY);
                if (matchIndex < 0) {
                    return bitOrExpression(-1);
                }
                long consumed = lexer.tokenInfo();
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    subquery = subQuery();
                    match(Token.PUNC_RIGHT_PAREN);
                    return new UnaryOperatorExpression(subquery, UnaryOperatorExpression.SUBQUERY_ANY);
                } else if (lexer.token() == Token.QUESTION_MARK) {
                    int index = lexer.paramIndex();
                    lexer.nextToken();
                    subquery = new ParamMarker(index);
                    return new UnaryOperatorExpression(subquery, UnaryOperatorExpression.SUBQUERY_ANY);
                }
                return bitOrExpression(consumed);
        }
    }

    private Expression rightOprandOfIn() throws SQLSyntaxErrorException {
        if (lexer.token() == Token.QUESTION_MARK) {
            int index = lexer.paramIndex();
            lexer.nextToken();
            return new ParamMarker(index);
        }
        match(Token.PUNC_LEFT_PAREN);
        if (Token.KW_SELECT == lexer.token()) {
            QueryExpression subq = subQuery();
            match(Token.PUNC_RIGHT_PAREN);
            return subq;
        }
        return new InExpressionList(expressionList(new LinkedList<Expression>()));

    }

    private Expression bitOrExpression(long consumed) throws SQLSyntaxErrorException {
        for (Expression expr = bitAndExpression(consumed); ; ) {
            switch (lexer.token()) {
                case Token.OP_VERTICAL_BAR:
                    lexer.nextToken();
                    Expression newExpr = new BitExpression(null, null, BitExpression.BIT_AND);
                    expr = new BitExpression(expr, newExpr, BitExpression.BIT_OR);
                    break;
                default:
                    return expr;
            }
        }
    }

    private Expression bitAndExpression(long consumed) throws SQLSyntaxErrorException {
        for (Expression expr = bitShiftExpression(consumed); ; ) {
            switch (lexer.token()) {
                case Token.OP_AMPERSAND:
                    lexer.nextToken();
                    Expression newExpr = new BitExpression(null, null, BitExpression.BIT_SHIFT);
                    expr = new BitExpression(expr, newExpr, BitExpression.BIT_AND);
                    break;
                default:
                    return expr;
            }
        }
    }

    private Expression bitShiftExpression(long consumed) throws SQLSyntaxErrorException {
        Expression temp;
        for (Expression expr = arithmeticTermOperatorExpression(consumed); ; ) {
            switch (lexer.token()) {
                case Token.OP_LEFT_SHIFT:
                    lexer.nextToken();
                    temp = arithmeticTermOperatorExpression(-1);
                    expr = new BitExpression(expr, temp, BitExpression.BIT_SHIFT).setNegative(false);
                    break;
                case Token.OP_RIGHT_SHIFT:
                    lexer.nextToken();
                    temp = arithmeticTermOperatorExpression(-1);
                    expr = new BitExpression(expr, temp, BitExpression.BIT_SHIFT).setNegative(true);
                    break;
                default:
                    return expr;
            }
        }
    }

    private Expression arithmeticTermOperatorExpression(long consumed) throws SQLSyntaxErrorException {
        Expression temp;
        for (Expression expr = arithmeticFactorOperatorExpression(consumed); ; ) {
            switch (lexer.token()) {
                case Token.OP_PLUS:
                    lexer.nextToken();
                    temp = arithmeticFactorOperatorExpression(-1);
                    expr = new ArithmeticExpression(expr, temp, ArithmeticExpression.ADD);
                    break;
                case Token.OP_MINUS:
                    lexer.nextToken();
                    temp = arithmeticFactorOperatorExpression(-1);
                    expr = new ArithmeticExpression(expr, temp, ArithmeticExpression.SUBTRACT);
                    break;
                default:
                    return expr;
            }
        }
    }

    private Expression arithmeticFactorOperatorExpression(long consumed) throws SQLSyntaxErrorException {
        Expression temp;
        for (Expression expr = bitXORExpression(consumed); ; ) {
            switch (lexer.token()) {
                case Token.OP_ASTERISK:
                    lexer.nextToken();
                    temp = bitXORExpression(-1);
                    expr = new ArithmeticExpression(expr, temp, ArithmeticExpression.MULTIPLY);
                    break;
                case Token.OP_SLASH:
                    lexer.nextToken();
                    temp = bitXORExpression(-1);
                    expr = new ArithmeticExpression(expr, temp, ArithmeticExpression.DIVIDE);
                    break;
                case Token.KW_DIV:
                    lexer.nextToken();
                    temp = bitXORExpression(-1);
                    expr = new ArithmeticExpression(expr, temp, ArithmeticExpression.INTEGET_DIVIDE);
                    break;
                case Token.OP_PERCENT:
                case Token.KW_MOD:
                    lexer.nextToken();
                    temp = bitXORExpression(-1);
                    expr = new ArithmeticExpression(expr, temp, ArithmeticExpression.MOD);
                    break;
                default:
                    return expr;
            }
        }
    }

    private Expression bitXORExpression(long consumed) throws SQLSyntaxErrorException {
        Expression temp;
        for (Expression expr = unaryOpExpression(consumed); ; ) {
            switch (lexer.token()) {
                case Token.OP_CARET:
                    lexer.nextToken();
                    temp = unaryOpExpression(-1);
                    expr = new BitExpression(expr, temp, BitExpression.BIT_XOR);
                    break;
                default:
                    return expr;
            }
        }
    }

    private Expression unaryOpExpression(long consumed) throws SQLSyntaxErrorException {
        if (consumed == -1) {
            Expression expr;
            switch (lexer.token()) {
                case Token.OP_EXCLAMATION:
                    lexer.nextToken();
                    expr = unaryOpExpression(-1);
                    return new UnaryOperatorExpression(expr, UnaryOperatorExpression.NEGTATIVE_VALUE);
                case Token.OP_PLUS:
                    lexer.nextToken();
                    return unaryOpExpression(-1);
                case Token.OP_MINUS:
                    lexer.nextToken();
                    expr = unaryOpExpression(-1);
                    return new UnaryOperatorExpression(expr, UnaryOperatorExpression.MINUS);
                case Token.OP_TILDE:
                    lexer.nextToken();
                    expr = unaryOpExpression(-1);
                    return new UnaryOperatorExpression(expr, UnaryOperatorExpression.BIT_INVERT);
                case Token.KW_BINARY:
                    lexer.nextToken();
                    expr = unaryOpExpression(-1);
                    return new UnaryOperatorExpression(expr, UnaryOperatorExpression.CAST_BINARY);
            }
        }
        return collateExpression(consumed);
    }

    private Expression collateExpression(long consumed) throws SQLSyntaxErrorException {
        for (Expression expr = userExpression(consumed); ; ) {
            if (lexer.token() == Token.KW_COLLATE) {
                lexer.nextToken();
                if (lexer.token() == Token.OP_EQUALS) {
                    lexer.nextToken();
                }
                String name = lexer.stringValue();
                if (name.charAt(0) == '\'' && name.charAt(name.length() - 1) == '\'') {
                    name = name.substring(1, name.length() - 1);
                }
                lexer.nextToken();
                expr = new CollateExpression(expr, name);
                continue;
            }
            return expr;
        }
    }

    private Expression userExpression(long consumed) throws SQLSyntaxErrorException {
        Expression first = primaryExpression(consumed);
        if (lexer.token() == Token.USR_VAR) {
            if (first instanceof LiteralString) {
                int start = BytesUtil.getOffset(((LiteralString)first).getString());
                int size = lexer.getOffset() - start + lexer.getSize();
                lexer.nextToken();
                return new VarsPrimary(VarsPrimary.USER, null, -1,
                    BytesUtil.getValue(lexer.getSQL(), ((long)start << 32) | ((((long)size << 32) >> 32))));
            } else if (first instanceof Identifier) {
                return new VarsPrimary(VarsPrimary.USER, null, -1, ((Identifier)first).getIdText());
            }
        }
        return first;
    }

    public LiteralNumber parseNumber() throws SQLSyntaxErrorException {
        if (lexer.token() == Token.LITERAL_NUM_PURE_DIGIT) {
            Number number = lexer.integerValue();
            lexer.nextToken();
            return new LiteralNumber(number, true);
        } else if (lexer.token() == Token.LITERAL_NUM_MIX_DIGIT) {
            Number number = lexer.decimalValue();
            lexer.nextToken();
            return new LiteralNumber(number, false);
        }
        throw new SQLSyntaxErrorException("unexpected " + Token.getInfo(lexer.token()));
    }

    public Long longValue() throws SQLSyntaxErrorException {
        LiteralNumber number = parseNumber();
        if (number != null) {
            return number.getNumber().longValue();
        }
        return null;
    }

    public Integer intValue() throws SQLSyntaxErrorException {
        LiteralNumber number = parseNumber();
        if (number != null) {
            return number.getNumber().intValue();
        }
        return null;
    }

    public LiteralString parseString() throws SQLSyntaxErrorException {
        boolean isNchars = false;
        if (lexer.token() == Token.LITERAL_NCHARS) {
            isNchars = true;
        } else if (lexer.token() != Token.LITERAL_CHARS) {
            throw new SQLSyntaxErrorException("unexpected " + Token.getInfo(lexer.token()));
        }
        long info = lexer.tokenInfo();
        int start = BytesUtil.getOffset(info);
        int size = 0;
        while (lexer.nextToken() == Token.LITERAL_CHARS) {
            size = lexer.getOffset() - start + lexer.getSize();
            info = ((long)start << 32) | ((((long)size << 32) >> 32));
        }
        return new LiteralString(null, info, isNchars);
    }

    private Expression primaryExpression(long consumed) throws SQLSyntaxErrorException {
        if (consumed != -1) {
            return startedFromIdentifier(consumed);
        }
        Expression tempExpr;
        Expression tempExpr2;
        List<Expression> tempExprList;
        byte[] functionName = lexer.bytesValue();
        switch (lexer.token()) {
            case Token.PLACE_HOLDER: {
                long info = lexer.tokenInfo();
                lexer.nextToken();
                return new PlaceHolder(info);
            }
            case Token.LITERAL_BIT: {
                long info = lexer.tokenInfo();
                lexer.nextToken();
                return new LiteralBitField(-1, info);
            }
            case Token.LITERAL_HEX: {
                long info = lexer.tokenInfo();
                lexer.nextToken();
                return new LiteralHexadecimal(-1, info);
            }
            case Token.LITERAL_BOOL_FALSE: {
                lexer.nextToken();
                return new LiteralBoolean(false);
            }
            case Token.LITERAL_BOOL_TRUE: {
                lexer.nextToken();
                return new LiteralBoolean(true);
            }
            case Token.LITERAL_NULL: {
                lexer.nextToken();
                return new LiteralNull();
            }
            case Token.LITERAL_NCHARS: {
                long info = lexer.tokenInfo();
                lexer.nextToken();
                return new LiteralString(null, info, true);
            }
            case Token.LITERAL_CHARS: {
                long info = lexer.tokenInfo();
                lexer.nextToken();
                return new LiteralString(null, info, false);
            }
            case Token.LITERAL_NUM_PURE_DIGIT: {
                Number number = lexer.integerValue();
                lexer.nextToken();
                return new LiteralNumber(number, true);
            }
            case Token.LITERAL_NUM_MIX_DIGIT: {
                Number number = lexer.decimalValue();
                lexer.nextToken();
                return new LiteralNumber(number, false);
            }
            case Token.QUESTION_MARK: {
                int index = lexer.paramIndex();
                lexer.nextToken();
                return new ParamMarker(index);
            }
            case Token.KW_CASE: {
                lexer.nextToken();
                return caseWhenExpression();
            }
            case Token.KW_INTERVAL: {
                lexer.nextToken();
                return intervalExpression(functionName);
            }
            case Token.KW_EXISTS: {
                lexer.nextToken();
                match(Token.PUNC_LEFT_PAREN);
                if (lexer.token() == Token.QUESTION_MARK) {
                    int index = lexer.paramIndex();
                    lexer.nextToken();
                    tempExpr = new ParamMarker(index);
                } else {
                    tempExpr = subQuery();
                }
                match(Token.PUNC_RIGHT_PAREN);
                return new ExistsPrimary((QueryExpression)tempExpr);
            }
            case Token.USR_VAR: {
                tempExpr = new VarsPrimary(VarsPrimary.USR_VAR, null, -1, lexer.bytesValue());
                if (lexer.nextToken() == Token.OP_ASSIGN) {
                    lexer.nextToken();
                    tempExpr2 = expression();
                    lexer.addParseInfo(ParseInfo.VAR_ASSIGNMENT);
                    return new AssignmentExpression(tempExpr, tempExpr2);
                }
                return tempExpr;
            }
            case Token.SYS_VAR: {
                return systemVariable();
            }
            case Token.KW_MATCH: {
                lexer.nextToken();
                return matchExpression();
            }
            case Token.PUNC_LEFT_PAREN: {
                lexer.nextToken();
                if (lexer.token() == Token.KW_SELECT) {
                    tempExpr = subQuery();
                    match(Token.PUNC_RIGHT_PAREN);
                    return tempExpr;
                }
                tempExpr = expression();
                switch (lexer.token()) {
                    case Token.PUNC_RIGHT_PAREN:
                        lexer.nextToken();
                        return tempExpr;
                    case Token.PUNC_COMMA:
                        lexer.nextToken();
                        tempExprList = new LinkedList<Expression>();
                        tempExprList.add(tempExpr);
                        tempExprList = expressionList(tempExprList);
                        return new RowExpression(tempExprList);
                    default:
                        throw new SQLSyntaxErrorException("unexpected token: " + Token.getInfo(lexer.token()));
                }
            }
            case Token.KW_UTC_DATE: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = Collections.emptyList();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.UTC_DATE, functionName, args);
            }
            case Token.KW_UTC_TIME: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = new ArrayList<>();
                    if (lexer.token() == Token.LITERAL_NUM_PURE_DIGIT) {
                        args.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.UTC_TIME, functionName, args);
            }
            case Token.KW_UTC_TIMESTAMP: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = new ArrayList<>();
                    if (lexer.token() == Token.LITERAL_NUM_PURE_DIGIT) {
                        args.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.UTC_TIMESTAMP, functionName, args);
            }
            case Token.KW_CURRENT_DATE: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = Collections.emptyList();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.CURRENT_DATE, functionName, args);
            }
            case Token.KW_CURRENT_TIME: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = new ArrayList<>();
                    if (lexer.token() == Token.LITERAL_NUM_PURE_DIGIT) {
                        args.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.CURRENT_TIME, functionName, args);
            }
            case Token.KW_CURRENT_TIMESTAMP: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = new ArrayList<>();
                    if (lexer.token() == Token.LITERAL_NUM_PURE_DIGIT) {
                        args.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.CURRENT_TIMESTAMP, functionName, args);
            }
            case Token.KW_LOCALTIME: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = new ArrayList<>();
                    if (lexer.token() == Token.LITERAL_NUM_PURE_DIGIT) {
                        args.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.LOCALTIME, functionName, args);
            }
            case Token.KW_LOCALTIMESTAMP: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = new ArrayList<>();
                    if (lexer.token() == Token.LITERAL_NUM_PURE_DIGIT) {
                        args.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.LOCALTIMESTAMP, functionName, args);
            }
            case Token.KW_CURRENT_USER: {
                List<Expression> args = null;
                lexer.nextToken();
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    args = Collections.emptyList();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new FunctionExpression(Functions.CURRENT_USER, functionName, args);
            }
            case Token.KW_DEFAULT: {
                long info = lexer.tokenInfo();
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    return oridinaryFunction(info);
                }
                return new DefaultValue();
            }
            case Token.KW_DATABASE:
            case Token.KW_IF:
            case Token.KW_INSERT:
            case Token.KW_LEFT:
            case Token.KW_REPEAT:
            case Token.KW_REPLACE:
            case Token.KW_RIGHT:
            case Token.KW_SCHEMA:
            case Token.KW_VALUES: {
                int token = lexer.token();
                long info = lexer.tokenInfo();
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    return oridinaryFunction(info);
                }
                throw new SQLSyntaxErrorException(
                    "keyword not followed by '(' is not expression: " + Token.getInfo(token));
            }
            case Token.KW_MOD: {
                lexer.nextToken();
                match(Token.PUNC_LEFT_PAREN);
                tempExpr = expression();
                match(Token.PUNC_COMMA);
                tempExpr2 = expression();
                match(Token.PUNC_RIGHT_PAREN);
                return new ArithmeticExpression(tempExpr, tempExpr2, ArithmeticExpression.MOD);
            }
            case Token.KW_CHAR: {
                lexer.nextToken();
                match(Token.PUNC_LEFT_PAREN);
                return functionChar(functionName);
            }
            case Token.KW_CONVERT: {
                lexer.nextToken();
                match(Token.PUNC_LEFT_PAREN);
                return functionConvert(functionName);
            }
            case Token.OP_ASTERISK: {
                lexer.nextToken();
                return new Wildcard(null);
            }
            case Token.IDENTIFIER:
            default: {
                long info = lexer.tokenInfo();
                lexer.nextToken();
                return startedFromIdentifier(info);
            }
        }
    }

    private Expression functionConvert(byte[] functionName) throws SQLSyntaxErrorException {
        Expression expr = expression();
        if (lexer.token() == Token.KW_USING) {
            lexer.nextToken();
            long tempStr = lexer.tokenInfo();
            match(Token.IDENTIFIER);
            match(Token.PUNC_RIGHT_PAREN);
            Convert cvt = new Convert(functionName, expr, tempStr);
            return cvt;
        } else {
            match(Token.PUNC_COMMA);
            if (lexer.token() == Token.KW_CHAR) {
                Integer length = null;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                boolean binary = false;
                Identifier charSet = null;
                Identifier collation = null;
                if (lexer.token() == Token.KW_BINARY) {
                    lexer.nextToken();
                    binary = true;
                }
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charSet = identifier();
                } else if (lexer.token() == Token.IDENTIFIER) {
                    if (equalsKeyword(Keywords.CHARSET)) {
                        lexer.nextToken();
                        charSet = identifier();
                    } else {
                        lexer.nextToken();
                        charSet = new Identifier(null, lexer.bytesValue(), lexer.isIdentifierWithOpenQuate());
                    }
                }
                if (lexer.token() == Token.KW_COLLATE) {
                    lexer.nextToken();
                    collation = identifier();
                }
                DataType type =
                    new DataType(DataType.CHAR, false, false, binary, length, null, charSet, collation, null);
                match(Token.PUNC_RIGHT_PAREN);
                Convert cvt = new Convert(functionName, expr, type, -1, null, null);
                return cvt;
            } else {
                Pair<Long, Pair<Expression, Expression>> type = type4specialFunc();
                match(Token.PUNC_RIGHT_PAREN);
                Pair<Expression, Expression> info = type.getValue();
                if (info != null) {
                    Convert cvt = new Convert(functionName, expr, null, type.getKey(), info.getKey(), info.getValue());
                    return cvt;
                } else {
                    Convert cvt = new Convert(functionName, expr, null, type.getKey(), null, null);
                    return cvt;
                }
            }
        }
    }

    private Pair<Long, Pair<Expression, Expression>> type4specialFunc() throws SQLSyntaxErrorException {
        Expression exp1 = null;
        Expression exp2 = null;
        // BINARY[(N)]
        // CHAR[(N)] [charset_info]
        // DATE
        // DATETIME
        // DECIMAL[(M[,D])]
        // JSON
        // NCHAR[(N)]
        // SIGNED [INTEGER]
        // TIME
        // UNSIGNED [INTEGER]
        switch (lexer.token()) {
            case Token.KW_BINARY: {
                long info = lexer.tokenInfo();
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    exp1 = expression();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new Pair<Long, Pair<Expression, Expression>>(info, new Pair<Expression, Expression>(exp1, exp2));
            }
            case Token.KW_DECIMAL: {
                long info = lexer.tokenInfo();
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    exp1 = expression();
                    if (lexer.token() == Token.PUNC_COMMA) {
                        lexer.nextToken();
                        exp2 = expression();
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                return new Pair<Long, Pair<Expression, Expression>>(info, new Pair<Expression, Expression>(exp1, exp2));
            }
            case Token.KW_UNSIGNED: {
                long info = lexer.tokenInfo();
                if (lexer.nextToken() == Token.KW_INTEGER || lexer.token() == Token.KW_INT) {
                    lexer.nextToken();
                }
                return new Pair<Long, Pair<Expression, Expression>>(info, new Pair<Expression, Expression>(null, null));
            }
            case Token.IDENTIFIER: {
                long info = lexer.tokenInfo();
                if (equalsKeyword(Keywords.SIGNED)) {
                    lexer.nextToken();
                    if (lexer.token() == Token.KW_INTEGER || lexer.token() == Token.KW_INT) {
                        lexer.nextToken();
                    }
                } else if (equalsKeywords(Keywords.DATETIME, Keywords.TIME) >= 0) {
                    if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                        lexer.nextToken();
                        exp1 = expression();
                        if (lexer.token() == Token.PUNC_COMMA) {
                            lexer.nextToken();
                            exp2 = expression();
                        }
                        match(Token.PUNC_RIGHT_PAREN);
                    }
                } else if (equalsKeywords(Keywords.DATE, Keywords.JSON) < 0) {
                    throw new SQLSyntaxErrorException("invalide type name: " + lexer.stringValue(info));
                }
                return new Pair<Long, Pair<Expression, Expression>>(info, new Pair<Expression, Expression>(null, null));
            }
            default:
                throw new SQLSyntaxErrorException("invalide type name: " + lexer.stringValueUppercase());
        }
    }

    private Expression functionChar(byte[] functionName) throws SQLSyntaxErrorException {
        Char chr;
        for (List<Expression> tempExprList = new LinkedList<Expression>(); ; ) {
            Expression tempExpr = expression();
            tempExprList.add(tempExpr);
            switch (lexer.token()) {
                case Token.PUNC_COMMA:
                    lexer.nextToken();
                    continue;
                case Token.PUNC_RIGHT_PAREN:
                    lexer.nextToken();
                    chr = new Char(functionName, null, tempExprList);
                    return chr;
                case Token.KW_USING:
                    lexer.nextToken();
                    String tempStr = lexer.stringValue();
                    match(Token.IDENTIFIER);
                    match(Token.PUNC_RIGHT_PAREN);
                    chr = new Char(functionName, tempStr, tempExprList);
                    return chr;
                default:
                    throw new SQLSyntaxErrorException("expect ',' or 'USING' or ')' but is " + lexer.token());
            }
        }
    }

    private FunctionExpression oridinaryFunction(long info) throws SQLSyntaxErrorException {
        byte[] functionName = BytesUtil.getValue(lexer.getSQL(), info);
        int type = FunctionsParser.get(lexer.getSQL(), BytesUtil.getOffset(info), BytesUtil.getSize(info));
        if (type == 0) {
            throw new SQLSyntaxErrorException(lexer.stringValue(info) + "() is not a function");
        } else {
            Expression tempExpr;
            List<Expression> tempExprList;
            switch (type) {
                case Functions.MAX:
                case Functions.MIN:
                case Functions.SUM:
                case Functions.AVG: {
                    boolean distinct = false;
                    match(Token.PUNC_LEFT_PAREN);
                    if (lexer.token() == Token.KW_DISTINCT) {
                        lexer.nextToken();
                        distinct = true;
                    }
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    match(Token.PUNC_RIGHT_PAREN);
                    lexer.addParseInfo(ParseInfo.AGGREGATION);
                    return new GroupFunction(type, functionName, tempExprList, distinct);
                }
                case Functions.STD:
                case Functions.STDDEV:
                case Functions.STDDEV_POP:
                case Functions.STDDEV_SAMP:
                case Functions.VAR_POP:
                case Functions.VAR_SAMP:
                case Functions.VARIANCE:
                case Functions.BIT_AND:
                case Functions.BIT_XOR:
                case Functions.BIT_OR:
                case Functions.JSON_ARRAYAGG: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    match(Token.PUNC_RIGHT_PAREN);
                    lexer.addParseInfo(ParseInfo.AGGREGATION);
                    return new GroupFunction(type, functionName, tempExprList, false);
                }
                case Functions.COUNT: {
                    boolean distinct = false;
                    match(Token.PUNC_LEFT_PAREN);
                    if (lexer.token() == Token.KW_DISTINCT) {
                        lexer.nextToken();
                        distinct = true;
                        for (tempExprList = new LinkedList<Expression>(); ; ) {
                            tempExpr = expression();
                            tempExprList.add(tempExpr);
                            if (lexer.token() == Token.PUNC_COMMA) {
                                lexer.nextToken();
                            } else {
                                break;
                            }
                        }
                    } else {
                        tempExprList = new LinkedList<>();
                        tempExprList.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                    lexer.addParseInfo(ParseInfo.AGGREGATION);
                    return new GroupFunction(type, functionName, tempExprList, distinct);
                }
                case Functions.GROUP_CONCAT: {
                    boolean distinct = false;
                    match(Token.PUNC_LEFT_PAREN);
                    if (lexer.token() == Token.KW_DISTINCT) {
                        lexer.nextToken();
                        distinct = true;
                    }
                    for (tempExprList = new LinkedList<Expression>(); ; ) {
                        tempExpr = expression();
                        tempExprList.add(tempExpr);
                        if (lexer.token() == Token.PUNC_COMMA) {
                            lexer.nextToken();
                        } else {
                            break;
                        }
                    }
                    OrderBy orderby = null;
                    tempExpr = null; // order by
                    String separator = null;
                    switch (lexer.token()) {
                        case Token.KW_ORDER:
                            lexer.nextToken();
                            match(Token.KW_BY);
                            orderby = new OrderBy();
                            tempExpr = expression();
                            boolean asc = true;
                            if (lexer.token() == Token.KW_ASC) {
                                lexer.nextToken();
                            } else if (lexer.token() == Token.KW_DESC) {
                                asc = false;
                                lexer.nextToken();
                            }
                            orderby = new OrderBy(tempExpr, asc);
                            for (; lexer.token() == Token.PUNC_COMMA; ) {
                                lexer.nextToken();
                                asc = true;
                                tempExpr = expression();
                                switch (lexer.token()) {
                                    case Token.KW_DESC:
                                        asc = false;
                                    case Token.KW_ASC:
                                        lexer.nextToken();
                                    default:
                                        break;
                                }
                                orderby.addOrderByItem(tempExpr, asc);
                            }
                            if (lexer.token() != Token.KW_SEPARATOR) {
                                break;
                            }
                        case Token.KW_SEPARATOR:
                            lexer.nextToken();
                            separator = lexer.stringValue();
                            match(Token.LITERAL_CHARS);
                            break;
                        default:
                            break;
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                    lexer.addParseInfo(ParseInfo.AGGREGATION);
                    return new GroupConcat(functionName, tempExprList, distinct, orderby, separator);
                }
                case Functions.JSON_OBJECTAGG: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    match(Token.PUNC_COMMA);
                    tempExprList.add(expression());
                    match(Token.PUNC_RIGHT_PAREN);
                    lexer.addParseInfo(ParseInfo.AGGREGATION);
                    return new GroupFunction(type, functionName, tempExprList, false);
                }
                case Functions.CAST: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExpr = null;
                    tempExpr = expression();
                    match(Token.KW_AS);
                    if (lexer.token() == Token.KW_CHAR) {
                        Integer length = null;
                        if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                            lexer.nextToken();
                            length = intValue();
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                        boolean binary = false;
                        Identifier charSet = null;
                        Identifier collation = null;
                        if (lexer.token() == Token.KW_BINARY) {
                            lexer.nextToken();
                            binary = true;
                        }
                        if (lexer.token() == Token.KW_CHARACTER) {
                            lexer.nextToken();
                            match(Token.KW_SET);
                            charSet = identifier();
                        } else {
                            if (lexer.token() == Token.IDENTIFIER) {
                                int tmp = lexer.parseKeyword();
                                if (tmp == Keywords.CHARSET) {
                                    lexer.nextToken();
                                    charSet = identifier();
                                } else if (tmp == Keywords.ASCII) {
                                    lexer.nextToken();
                                    charSet = new Identifier(null, "latin1".getBytes());
                                } else if (tmp == Keywords.UNICODE) {
                                    lexer.nextToken();
                                    charSet = new Identifier(null, "ucs2".getBytes());
                                }
                            }
                        }
                        if (lexer.token() == Token.KW_COLLATE) {
                            lexer.nextToken();
                            collation = identifier();
                        }
                        DataType t =
                            new DataType(DataType.CHAR, false, false, binary, length, null, charSet, collation, null);
                        match(Token.PUNC_RIGHT_PAREN);
                        return new Cast(functionName, tempExpr, t);
                    } else {
                        Pair<Long, Pair<Expression, Expression>> t = type4specialFunc();
                        match(Token.PUNC_RIGHT_PAREN);
                        Pair<Expression, Expression> p = t.getValue();
                        if (p != null) {
                            return new Cast(functionName, tempExpr, t.getKey(), p.getKey(), p.getValue());
                        } else {
                            return new Cast(functionName, tempExpr, t.getKey(), null, null);
                        }
                    }
                }
                case Functions.SUBSTR:
                case Functions.SUBSTRING: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    if (lexer.token() == Token.PUNC_COMMA) {
                        lexer.nextToken();
                    }
                    if (lexer.token() == Token.KW_FROM) {
                        FromForFunction fromForFunction = new FromForFunction(type, functionName, tempExprList);
                        lexer.nextToken();
                        tempExprList.add(expression());
                        fromForFunction.setFrom(true);
                        if (lexer.token() == Token.KW_FOR) {
                            lexer.nextToken();
                            tempExprList.add(expression());
                            fromForFunction.setFor(true);
                        }
                        match(Token.PUNC_RIGHT_PAREN);
                        return fromForFunction;
                    } else {
                        tempExprList.add(expression());
                        if (lexer.token() == Token.PUNC_COMMA) {
                            lexer.nextToken();
                            tempExprList.add(expression());
                        }
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                    return new FunctionExpression(type, functionName, tempExprList);
                }
                case Functions.TRIM: {
                    //TODO
                    break;
                }
                case Functions.CURDATE:
                case Functions.CURRENT_DATE:
                case Functions.CURTIME:
                case Functions.CURRENT_TIME:
                case Functions.NOW:
                case Functions.CURRENT_TIMESTAMP:
                case Functions.LOCALTIME:
                case Functions.LOCALTIMESTAMP:
                case Functions.SYSDATE:
                case Functions.UNIX_TIMESTAMP:
                case Functions.UTC_DATE:
                case Functions.UTC_TIME:
                case Functions.UTC_TIMESTAMP: {
                    lexer.addParseInfo(ParseInfo.UNCERTAINTY_FUNCTION);
                    break;
                }
                case Functions.EXTRACT: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    if (lexer.token() == Token.IDENTIFIER) {
                        switch (lexer.parseKeyword()) {
                            case Keywords.YEAR:
                            case Keywords.MONTH:
                            case Keywords.DAY:
                            case Keywords.HOUR:
                            case Keywords.MINUTE:
                            case Keywords.SECOND:
                                tempExprList.add(expression());
                                break;
                            default: {
                                {
                                    throw new SQLSyntaxErrorException("unexepceted SQL!");
                                }
                            }
                        }
                    } else {
                        throw new SQLSyntaxErrorException("unexpected SQL!");
                    }
                    FromForFunction fromForFunction = new FromForFunction(type, functionName, tempExprList);
                    match(Token.KW_FROM);
                    fromForFunction.setFrom(true);
                    tempExprList.add(expression());
                    match(Token.PUNC_RIGHT_PAREN);
                    return fromForFunction;
                }
                case Functions.UNHEX:
                case Functions.HEX:
                case Functions.SOUNDEX:
                case Functions.LENGTH: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    match(Token.PUNC_RIGHT_PAREN);

                    return new FunctionExpression(type, functionName, tempExprList);

                }
                case Functions.ROUND: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    if (lexer.token() == Token.PUNC_COMMA) {
                        lexer.nextToken();
                        tempExprList.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                    return new FunctionExpression(type, functionName, tempExprList);
                }
                case Functions.REGEXP_INSTR:
                case Functions.REGEXP_SUBSTR:
                case Functions.REGEXP_REPLACE: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    match(Token.PUNC_COMMA);
                    tempExprList.add(expression());
                    for (; ; ) {
                        if (lexer.token() == Token.PUNC_COMMA) {
                            lexer.nextToken();
                        } else {
                            break;
                        }
                        tempExprList.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                    if ((tempExprList.size() == 6 && type == Functions.REGEXP_SUBSTR) || (tempExprList.size() > 6)) {
                        throw new SQLSyntaxErrorException(
                            "unexpected SQL!Incorrect parameter count in the call to native function");
                    }
                    return new FunctionExpression(type, functionName, tempExprList);
                }
                case Functions.RTRIM:
                case Functions.LTRIM:
                case Functions.INSTR: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    match(Token.PUNC_COMMA);
                    tempExprList.add(expression());
                    match(Token.PUNC_RIGHT_PAREN);
                    return new FunctionExpression(type, functionName, tempExprList);
                }
                case Functions.STR_TO_DATE:
                case Functions.DATE_FORMAT: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    match(Token.PUNC_COMMA);
                    tempExprList.add(expression());
                    match(Token.PUNC_RIGHT_PAREN);
                    return new FunctionExpression(type, functionName, tempExprList);
                }
                case Functions.REPLACE:
                case Functions.RPAD:
                case Functions.LPAD: {
                    match(Token.PUNC_LEFT_PAREN);
                    tempExprList = new LinkedList<>();
                    tempExprList.add(expression());
                    match(Token.PUNC_COMMA);
                    tempExprList.add(expression());
                    if (lexer.token() == Token.PUNC_COMMA) {
                        //mysql
                        lexer.nextToken();
                        tempExprList.add(expression());
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                    return new FunctionExpression(type, functionName, tempExprList);
                }
            }
            match(Token.PUNC_LEFT_PAREN);
            if (lexer.token() == Token.PUNC_RIGHT_PAREN) {
                lexer.nextToken();
                return new FunctionExpression(type, functionName, new LinkedList<Expression>());
            } else {
                return new FunctionExpression(type, functionName, expressionList(new LinkedList<Expression>()));
            }
        }
    }

    private Expression startedFromIdentifier(long consumed) throws SQLSyntaxErrorException {
        Expression tempExpr;
        switch (lexer.token()) {
            case Token.PUNC_DOT: {
                for (tempExpr = new Identifier(null, BytesUtil.getValue(lexer.getSQL(), consumed),
                    lexer.isIdentifierWithOpenQuate()); lexer.token() == Token.PUNC_DOT; ) {
                    switch (lexer.nextToken()) {
                        case Token.IDENTIFIER: {
                            tempExpr = new Identifier((Identifier)tempExpr, lexer.bytesValue(),
                                lexer.isIdentifierWithOpenQuate());
                            lexer.nextToken();
                            break;
                        }
                        case Token.OP_ASTERISK: {
                            lexer.nextToken();
                            return new Wildcard((Identifier)tempExpr);
                        }
                        default:
                            if (tempExpr != null) {
                                tempExpr = new Identifier((Identifier)tempExpr, lexer.bytesValue(),
                                    lexer.isIdentifierWithOpenQuate());
                                lexer.nextToken();
                                break;
                            } else {
                                throw new SQLSyntaxErrorException(
                                    "expect IDENTIFIER or '*' after '.', but is " + Token.getInfo(lexer.token()));
                            }
                    }
                }
                if (tempExpr instanceof Identifier) {
                    Identifier parent = ((Identifier)tempExpr).getParent();
                    if (parent != null) {
                        lexer.addParseInfo(ParseInfo.ID_WITH_TABLE);
                        if (parent.getParent() != null) {
                            lexer.addParseInfo(ParseInfo.ID_WITH_SCHEMA);
                        }
                    }
                }
                return tempExpr;
            }
            case Token.LITERAL_BIT: {
                if (lexer.charAtIndex(consumed) != '_') {
                    return new Identifier(null, BytesUtil.getValue(lexer.getSQL(), consumed),
                        lexer.isIdentifierWithOpenQuate());
                }
                lexer.nextToken();
                return new LiteralBitField(-1, consumed);
            }
            case Token.LITERAL_HEX: {
                if (lexer.charAtIndex(consumed) != '_') {
                    return new Identifier(null, BytesUtil.getValue(lexer.getSQL(), consumed),
                        lexer.isIdentifierWithOpenQuate());
                }
                lexer.nextToken();
                return new LiteralHexadecimal(-1, consumed);
            }
            case Token.LITERAL_CHARS: {
                if (lexer.charAtIndex(consumed) != '_') {
                    return new Identifier(null, BytesUtil.getValue(lexer.getSQL(), consumed),
                        lexer.isIdentifierWithOpenQuate());
                }
                long offset = lexer.getOffset();
                long end = offset + lexer.getSize();
                while (lexer.token() == Token.LITERAL_CHARS) {
                    end = lexer.getOffset() + lexer.getSize();
                    lexer.nextToken();
                }
                long size = end - offset;
                long info = (offset << 32) | (((size << 32) >> 32));
                return new LiteralString(BytesUtil.getValue(lexer.getSQL(), consumed), info, false);
            }
            case Token.PUNC_LEFT_PAREN: {
                FunctionExpression expr = oridinaryFunction(consumed);
                if (lexer.token() == Token.KW_OVER) {
                    OverClause over = selectParser.over(expr);
                    if (over != null) {
                        return over;
                    }
                }
                return expr;
            }
            default:
                return new Identifier(null, BytesUtil.getValue(lexer.getSQL(), consumed),
                    lexer.isIdentifierWithOpenQuate());
        }
    }

    private Expression caseWhenExpression() throws SQLSyntaxErrorException {
        Expression comparee = null;
        if (lexer.token() != Token.KW_WHEN) {
            comparee = expression();
        }
        List<Pair<Expression, Expression>> list = new LinkedList<Pair<Expression, Expression>>();
        for (; lexer.token() == Token.KW_WHEN; ) {
            lexer.nextToken();
            Expression when = expression();
            match(Token.KW_THEN);
            Expression then = expression();
            if (when == null || then == null)
                throw new SQLSyntaxErrorException("when or then is null in CASE WHEN expression");
            list.add(new Pair<Expression, Expression>(when, then));
        }
        if (list.isEmpty()) {
            throw new SQLSyntaxErrorException("at least one WHEN ... THEN branch for CASE ... WHEN syntax");
        }
        Expression elseValue = null;
        switch (lexer.token()) {
            case Token.KW_ELSE:
                lexer.nextToken();
                elseValue = expression();
            default:
                matchKeywords(Keywords.END);
        }
        return new CaseWhenExpression(comparee, list, elseValue);
    }

    private Expression intervalExpression(byte[] functionName) throws SQLSyntaxErrorException {
        Expression fstExpr;
        List<Expression> argList = null;
        if (lexer.token() == Token.PUNC_LEFT_PAREN) {
            if (lexer.nextToken() == Token.KW_SELECT) {
                fstExpr = subQuery();
                match(Token.PUNC_RIGHT_PAREN);
            } else {
                fstExpr = expression();
                if (lexer.token() == Token.PUNC_COMMA) {
                    lexer.nextToken();
                    argList = new LinkedList<Expression>();
                    argList.add(fstExpr);
                    argList = expressionList(argList);
                } else {
                    match(Token.PUNC_RIGHT_PAREN);
                }
            }
        } else {
            fstExpr = expression();
        }
        if (argList != null) {
            return new FunctionExpression(Functions.INTERVAL, functionName, argList);
        }
        return new IntervalPrimary(fstExpr, intervalPrimaryUnit());
    }

    private int intervalPrimaryUnit() throws SQLSyntaxErrorException {
        switch (lexer.token()) {
            case Token.KW_SECOND_MICROSECOND:
                lexer.nextToken();
                return IntervalUnit.SECOND_MICROSECOND;
            case Token.KW_MINUTE_MICROSECOND:
                lexer.nextToken();
                return IntervalUnit.MINUTE_MICROSECOND;
            case Token.KW_MINUTE_SECOND:
                lexer.nextToken();
                return IntervalUnit.MINUTE_SECOND;
            case Token.KW_HOUR_MICROSECOND:
                lexer.nextToken();
                return IntervalUnit.HOUR_MICROSECOND;
            case Token.KW_HOUR_SECOND:
                lexer.nextToken();
                return IntervalUnit.HOUR_SECOND;
            case Token.KW_HOUR_MINUTE:
                lexer.nextToken();
                return IntervalUnit.HOUR_MINUTE;
            case Token.KW_DAY_MICROSECOND:
                lexer.nextToken();
                return IntervalUnit.DAY_MICROSECOND;
            case Token.KW_DAY_SECOND:
                lexer.nextToken();
                return IntervalUnit.DAY_SECOND;
            case Token.KW_DAY_MINUTE:
                lexer.nextToken();
                return IntervalUnit.DAY_MINUTE;
            case Token.KW_DAY_HOUR:
                lexer.nextToken();
                return IntervalUnit.DAY_HOUR;
            case Token.KW_YEAR_MONTH:
                lexer.nextToken();
                return IntervalUnit.YEAR_MONTH;
            case Token.IDENTIFIER:
                int unit = IntervalUnitParser.get(lexer.getSQL(), lexer.getOffset(), lexer.getSize());
                if (unit != 0) {
                    lexer.nextToken();
                    return unit;
                }
            default:
                throw new SQLSyntaxErrorException("literal INTERVAL should end with an UNIT");
        }
    }

    private QueryExpression subQuery() throws SQLSyntaxErrorException {
        if (selectParser == null) {
            selectParser = new MySQLDMLSelectParser(lexer, this);
        }
        lexer.addParseInfo(ParseInfo.SUBQUERY);
        return selectParser.selectUnion(true);
    }

    private Expression matchExpression() throws SQLSyntaxErrorException {
        match(Token.PUNC_LEFT_PAREN);
        List<Expression> colList = expressionList(new LinkedList<Expression>());
        matchKeywords(Keywords.AGAINST);
        match(Token.PUNC_LEFT_PAREN);
        Expression pattern = expression();
        int modifier = MatchExpression._DEFAULT;
        switch (lexer.token()) {
            case Token.KW_WITH:
                lexer.nextToken();
                matchKeywords(Keywords.QUERY);
                matchKeywords(Keywords.EXPANSION);
                modifier = MatchExpression.WITH_QUERY_EXPANSION;
                break;
            case Token.KW_IN:
                switch (lexer.nextToken()) {
                    case Token.KW_NATURAL:
                        lexer.nextToken();
                        matchKeywords(Keywords.LANGUAGE);
                        matchKeywords(Keywords.MODE);
                        if (lexer.token() == Token.KW_WITH) {
                            lexer.nextToken();
                            matchKeywords(Keywords.QUERY);
                            matchKeywords(Keywords.EXPANSION);
                            modifier = MatchExpression.IN_NATURAL_LANGUAGE_MODE_WITH_QUERY_EXPANSION;
                        } else {
                            modifier = MatchExpression.IN_NATURAL_LANGUAGE_MODE;
                        }
                        break;
                    default:
                        matchKeywords(Keywords.BOOLEAN);
                        matchKeywords(Keywords.MODE);
                        modifier = MatchExpression.IN_BOOLEAN_MODE;
                        break;
                }
        }
        match(Token.PUNC_RIGHT_PAREN);
        return new MatchExpression(colList, pattern, modifier);
    }

    private List<Expression> expressionList(List<Expression> exprList) throws SQLSyntaxErrorException {
        for (; ; ) {
            Expression expr = expression();
            exprList.add(expr);
            switch (lexer.token()) {
                case Token.PUNC_COMMA:
                    lexer.nextToken();
                    break;
                case Token.PUNC_RIGHT_PAREN:
                    lexer.nextToken();
                    return exprList;
                default:
                    throw new SQLSyntaxErrorException("unexpected token: " + lexer.token());
            }
        }
    }
}

