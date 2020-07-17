package parser.syntax;

import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.fragment.ddl.DataType;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.compound.BeginEndStatement;
import parser.ast.stmt.compound.DeclareStatement;
import parser.ast.stmt.compound.condition.ConditionValue;
import parser.ast.stmt.compound.condition.DeclareConditionStatement;
import parser.ast.stmt.compound.condition.DeclareHandlerStatement;
import parser.ast.stmt.compound.cursors.CursorDeclareStatement;
import parser.ast.stmt.compound.flowcontrol.LoopStatement;
import parser.ast.stmt.compound.flowcontrol.RepeatStatement;
import parser.ast.stmt.compound.flowcontrol.WhileStatement;
import parser.lexer.Lexer;
import parser.token.Keywords;
import parser.token.Token;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class MySQLCmpdParser extends AbstractParser {

    private ExprParser exprParser;

    public MySQLCmpdParser(Lexer lexer, ExprParser exprParser) {
        super(lexer);
        this.exprParser = exprParser;
    }

    public BeginEndStatement beginEnd(Identifier beginLabel) throws SQLSyntaxErrorException {
        List<SQLStatement> stmts = new ArrayList<>();
        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.BEGIN) {
            lexer.nextToken();
        }
        while (!(lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.END)
            && lexer.token() != Token.EOF) {
            stmts = Parser.parse(lexer);
        }
        lexer.nextToken();
        if (beginLabel != null && lexer.token() == Token.IDENTIFIER) {
            Identifier endLabel = identifier();
            checkEndLabel(beginLabel, endLabel);
            return new BeginEndStatement(beginLabel, stmts, endLabel);
        }
        return new BeginEndStatement(beginLabel, stmts, null);
    }

    private void checkEndLabel(Identifier beginLabel, Identifier endLabel) throws SQLSyntaxErrorException {
        if (endLabel != null) {
            if (!endLabel.equals(beginLabel)) {
                throw new SQLSyntaxErrorException("End-lable " + endLabel.getIdText() + " without match");
            }
        } else {
            throw new SQLSyntaxErrorException("End-lable " + endLabel.getIdText() + " without match");
        }
    }

    public SQLStatement parseWithIdentifier(Identifier label) throws SQLSyntaxErrorException {
        match(Token.PUNC_COLON);
        switch (lexer.token()) {
            case Token.KW_LOOP:
                return loop(label);
            case Token.KW_REPEAT:
                return repeat(label);
            case Token.KW_WHILE:
                return whileStmt(label);
            case Token.IDENTIFIER:
                switch (lexer.parseKeyword()) {
                    case Keywords.BEGIN:
                        return beginEnd(label);
                    default:
                        break;
                }
            default:
                break;
        }
        return null;
    }

    public SQLStatement loop(Identifier beginLabel) throws SQLSyntaxErrorException {
        List<SQLStatement> stmts = new ArrayList<>();
        if (lexer.token() == Token.KW_LOOP) {
            lexer.nextToken();
        }
        stmts = Parser.parse(lexer);
        matchKeywords(Keywords.END);
        match(Token.KW_LOOP);
        if (beginLabel != null && lexer.token() == Token.IDENTIFIER) {
            Identifier endLabel = identifier();
            checkEndLabel(beginLabel, endLabel);
            return new LoopStatement(beginLabel, stmts, endLabel);
        }
        return new LoopStatement(beginLabel, stmts, null);
    }

    public SQLStatement repeat(Identifier beginLabel) throws SQLSyntaxErrorException {
        match(Token.KW_REPEAT);
        List<SQLStatement> statements = new ArrayList<>();
        statements = Parser.parse(lexer);
        matchKeywords(Keywords.UNTIL);
        Expression utilCondition = exprParser.expression();
        matchKeywords(Keywords.END);
        match(Token.KW_REPEAT);
        if (beginLabel != null && lexer.token() == Token.IDENTIFIER) {
            Identifier endLabel = identifier();
            checkEndLabel(beginLabel, endLabel);
            return new RepeatStatement(beginLabel, statements, utilCondition, endLabel);
        }
        return new RepeatStatement(beginLabel, statements, utilCondition, null);
    }

    public SQLStatement whileStmt(Identifier beginLabel) throws SQLSyntaxErrorException {
        lexer.nextToken();
        Expression whileCondition = exprParser.expression();
        matchKeywords(Keywords.DO);
        List<SQLStatement> statements = new ArrayList<>();
        while (!(lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.END)
            && lexer.token() != Token.EOF) {
            statements = Parser.parse(lexer);
        }
        matchKeywords(Keywords.END);
        match(Token.KW_WHILE);
        if (beginLabel != null && lexer.token() == Token.IDENTIFIER) {
            Identifier endLabel = identifier();
            checkEndLabel(beginLabel, endLabel);
            return new WhileStatement(beginLabel, whileCondition, statements, endLabel);
        }
        return new WhileStatement(beginLabel, whileCondition, statements, null);
    }

    public SQLStatement declare() throws SQLSyntaxErrorException {
        lexer.nextToken();
        if (lexer.token() == Token.KW_CONTINUE || lexer.token() == Token.KW_EXIT || lexer.token() == Token.KW_UNDO) {
            int action = 0;
            List<ConditionValue> conditionValues = null;
            SQLStatement stmt = null;
            if (lexer.token() == Token.KW_CONTINUE) {
                action = DeclareHandlerStatement.CONTINUE;
            } else if (lexer.token() == Token.KW_EXIT) {
                action = DeclareHandlerStatement.EXIT;
            } else {
                action = DeclareHandlerStatement.UNDO;
            }
            lexer.nextToken();
            matchKeywords(Keywords.HANDLER);
            match(Token.KW_FOR);
            conditionValues = new ArrayList<>();
            conditionValues.add(getConditionValue());
            while (lexer.token() == Token.PUNC_COMMA) {
                lexer.nextToken();
                conditionValues.add(getConditionValue());
            }
            stmt = Parser.parse(lexer).get(0);
            if (stmt == null) {
                throw new SQLSyntaxErrorException("unexpected SQL!");
            }
            return new DeclareHandlerStatement(action, conditionValues, stmt);
        }
        Identifier name = identifier(false);
        if (lexer.token() == Token.KW_CURSOR) {
            lexer.nextToken();
            match(Token.KW_FOR);
            if (lexer.token() == Token.KW_SELECT) {
                SQLStatement stmt = Parser.parse(lexer).get(0);
                return new CursorDeclareStatement(name, stmt);
            } else {
                throw new SQLSyntaxErrorException("unexpected SQL!");
            }
        } else if (lexer.token() == Token.KW_CONDITION) {
            lexer.nextToken();
            match(Token.KW_FOR);
            ConditionValue value = getConditionValue();
            return new DeclareConditionStatement(name, value);
        } else {
            List<Identifier> varNames = new ArrayList<>();
            Expression defaultVal = null;
            varNames.add(name);
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                name = identifier(false);
                varNames.add(name);
            }
            MySQLDDLParser mySQLDDLParser = new MySQLDDLParser(lexer, exprParser);
            DataType dataType = mySQLDDLParser.dataType();
            if (lexer.token() == Token.KW_DEFAULT) {
                lexer.nextToken();
                defaultVal = exprParser.expression();
            }
            return new DeclareStatement(varNames, dataType, defaultVal);
        }
    }

    private ConditionValue getConditionValue() throws SQLSyntaxErrorException {
        int type = 0;
        Long mysqlErrorCode = null;
        LiteralString sqlState = null;
        Identifier conditionName = null;
        switch (lexer.token()) {
            case Token.KW_SQLSTATE:
                type = ConditionValue.SQLSTATE;
                if (lexer.nextToken() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.VALUE) {
                    lexer.nextToken();
                }
                sqlState = exprParser.parseString();
                return new ConditionValue(type, sqlState);
            case Token.KW_SQLWARNING:
                lexer.nextToken();
                type = ConditionValue.SQLWARNING;
                return new ConditionValue(type, null);
            case Token.KW_NOT:
                lexer.nextToken();
                matchKeywords(Keywords.FOUND);
                type = ConditionValue.NOT_FOUND;
                return new ConditionValue(type, null);
            case Token.KW_SQLEXCEPTION:
                lexer.nextToken();
                type = ConditionValue.SQLEXCEPTION;
                return new ConditionValue(type, null);
            case Token.LITERAL_NUM_PURE_DIGIT:
                type = ConditionValue.MYSQL_ERROR_CODE;
                mysqlErrorCode = exprParser.longValue();
                return new ConditionValue(type, mysqlErrorCode);
            case Token.IDENTIFIER:
                type = ConditionValue.CONDITION_NAME;
                conditionName = identifier(false);
                return new ConditionValue(type, conditionName);
        }
        return null;
    }

}
