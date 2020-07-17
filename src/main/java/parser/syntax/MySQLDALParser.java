package parser.syntax;

import parser.ast.expression.CollateExpression;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.VarsPrimary;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dal.DALSetStatement;
import parser.ast.stmt.dal.account.DALSetDefaultRoleStatement;
import parser.ast.stmt.dal.account.DALSetPasswordStatement;
import parser.ast.stmt.dal.account.DALSetRoleStatement;
import parser.ast.stmt.dal.resource.DALSetResourceGroupStatement;
import parser.ast.stmt.transactional.SetTransactionStatement;
import parser.lexer.Lexer;
import parser.token.Keywords;
import parser.token.Token;
import parser.util.BytesUtil;
import parser.util.Pair;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class MySQLDALParser extends AbstractParser {

    private ExprParser exprParser;

    public MySQLDALParser(Lexer lexer, ExprParser exprParser) {
        super(lexer);
        this.exprParser = exprParser;
    }

    public SQLStatement set() throws SQLSyntaxErrorException {
        match(Token.KW_SET);
        if (lexer.token() == Token.IDENTIFIER) {
            int keyword = lexer.parseKeyword();
            switch (keyword) {
                case Keywords.NAMES: {
                    if (lexer.nextToken() == Token.KW_DEFAULT) {
                        lexer.nextToken();
                        return new DALSetStatement(null, null, true);
                    }
                    LiteralString charsetName = null;
                    LiteralString collationName = null;
                    Expression exp = exprParser.expression();
                    if (exp instanceof CollateExpression) {
                        Expression charExp = ((CollateExpression)exp).getString();
                        if (charExp instanceof LiteralString) {
                            charsetName = (LiteralString)charExp;
                        } else if (exp instanceof Identifier) {
                            charExp = new LiteralString(null, ((Identifier)charExp).getIdText(), false);
                        } else {
                            throw new SQLSyntaxErrorException("Expect String or Identifier");
                        }
                        collationName =
                            new LiteralString(null, ((CollateExpression)exp).getCollateName().getBytes(), false);
                    } else if (exp instanceof LiteralString) {
                        charsetName = (LiteralString)exp;
                    } else if (exp instanceof Identifier) {
                        charsetName = new LiteralString(null, ((Identifier)exp).getIdText(), false);
                    } else {
                        throw new SQLSyntaxErrorException("Expect String or Identifier");
                    }
                    return new DALSetStatement(charsetName, collationName, false);
                }
                case Keywords.CHARSET: {
                    lexer.nextToken();
                    if (lexer.token() == Token.KW_DEFAULT) {
                        lexer.nextToken();
                        return new DALSetStatement(null, true);
                    }
                    LiteralString charsetName = null;
                    Expression exp = exprParser.expression();
                    if (exp instanceof LiteralString) {
                        charsetName = (LiteralString)exp;
                    } else if (exp instanceof Identifier) {
                        charsetName = new LiteralString(null, ((Identifier)exp).getIdText(), false);
                    } else {
                        throw new SQLSyntaxErrorException("Expect String or Identifier");
                    }
                    return new DALSetStatement(charsetName, false);
                }
                case Keywords.TRANSACTION: {
                    return setSetTransactionStatement(null);
                }
                case Keywords.PASSWORD: {
                    lexer.nextToken();
                    Expression user = null;
                    LiteralString authString = null;
                    LiteralString currentAuthString = null;
                    boolean retainCurrentPassword = false;
                    if (lexer.token() == Token.KW_FOR) {
                        lexer.nextToken();
                        user = exprParser.expression();
                    } else {
                        match(Token.OP_EQUALS);
                        authString = exprParser.parseString();
                    }
                    if (lexer.token() == Token.KW_REPLACE) {
                        lexer.nextToken();
                        currentAuthString = exprParser.parseString();
                    }
                    if (lexer.parseKeyword() == Keywords.RETAIN) {
                        lexer.nextToken();
                        matchKeywords(Keywords.CURRENT);
                        matchKeywords(Keywords.PASSWORD);
                        retainCurrentPassword = true;
                    }
                    return new DALSetPasswordStatement(user, authString, currentAuthString, retainCurrentPassword);
                }
                case Keywords.RESOURCE: {
                    lexer.nextToken();
                    match(Token.KW_GROUP);
                    Identifier name = null;
                    List<Long> threadIds = null;
                    name = identifier(false);
                    if (lexer.token() == Token.KW_FOR) {
                        lexer.nextToken();
                        threadIds = new ArrayList<>();
                        threadIds.add(exprParser.longValue());
                        for (; lexer.token() == Token.PUNC_COMMA; ) {
                            lexer.nextToken();
                            threadIds.add(exprParser.longValue());
                        }
                    }
                    return new DALSetResourceGroupStatement(name, threadIds);
                }
                case Keywords.ROLE: {
                    lexer.nextToken();
                    Boolean defaultOrNone = null;
                    boolean all = false;
                    List<Expression> roles = null;
                    if (lexer.token() == Token.KW_DEFAULT) {
                        lexer.nextToken();
                        defaultOrNone = true;
                    } else if (lexer.token() == Token.KW_ALL) {
                        lexer.nextToken();
                        all = true;
                        if (lexer.token() == Token.KW_EXCEPT) {
                            lexer.nextToken();
                            roles = new ArrayList<>();
                            roles.add(exprParser.expression());
                            for (; lexer.token() == Token.PUNC_COMMA; ) {
                                lexer.nextToken();
                                roles.add(exprParser.expression());
                            }
                        }
                    } else if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.NONE) {
                        lexer.nextToken();
                        defaultOrNone = false;
                    } else {
                        roles = new ArrayList<>();
                        roles.add(exprParser.expression());
                        for (; lexer.token() == Token.PUNC_COMMA; ) {
                            lexer.nextToken();
                            roles.add(exprParser.expression());
                        }
                    }
                    return new DALSetRoleStatement(defaultOrNone, all, roles);
                }
            }
        } else if (lexer.token() == Token.KW_CHARACTER) {
            lexer.nextToken();
            match(Token.KW_SET);
            if (lexer.token() == Token.KW_DEFAULT) {
                lexer.nextToken();
                return new DALSetStatement(null, true);
            }
            LiteralString charsetName = null;
            Expression exp = exprParser.expression();
            if (exp instanceof LiteralString) {
                charsetName = (LiteralString)exp;
            } else if (exp instanceof Identifier) {
                charsetName = new LiteralString(null, ((Identifier)exp).getIdText(), false);
            } else {
                throw new SQLSyntaxErrorException("Expect String or Identifier");
            }
            return new DALSetStatement(charsetName, false);
        } else if (lexer.token() == Token.KW_DEFAULT) {
            lexer.nextToken();
            matchKeywords(Keywords.ROLE);
            Boolean noneOrAll = null;
            List<Expression> roles = null;
            List<Expression> users = null;
            if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.NONE) {
                noneOrAll = true;
                lexer.nextToken();
            } else if (lexer.token() == Token.KW_ALL) {
                noneOrAll = false;
                lexer.nextToken();
            } else {
                roles = new ArrayList<>();
                roles.add(exprParser.expression());
                for (; lexer.token() == Token.PUNC_COMMA; ) {
                    lexer.nextToken();
                    roles.add(exprParser.expression());
                }
            }
            match(Token.KW_TO);
            users = new ArrayList<>();
            users.add(exprParser.expression());
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                users.add(exprParser.expression());
            }
            return new DALSetDefaultRoleStatement(noneOrAll, roles, users);
        }

        List<Pair<VarsPrimary, Expression>> assignmentList;
        Object obj = varAssign();
        if (obj instanceof SetTransactionStatement) {
            return (SetTransactionStatement)obj;
        }
        @SuppressWarnings("unchecked") Pair<VarsPrimary, Expression> pair = (Pair<VarsPrimary, Expression>)obj;
        if (lexer.token() != Token.PUNC_COMMA) {
            assignmentList = new ArrayList<Pair<VarsPrimary, Expression>>(1);
            assignmentList.add(pair);
            return new DALSetStatement(assignmentList);
        }
        assignmentList = new LinkedList<Pair<VarsPrimary, Expression>>();
        assignmentList.add(pair);
        for (; lexer.token() == Token.PUNC_COMMA; ) {
            lexer.nextToken();
            pair = (Pair<VarsPrimary, Expression>)varAssign();
            assignmentList.add(pair);
        }
        return new DALSetStatement(assignmentList);
    }

    private Object varAssign() throws SQLSyntaxErrorException {
        VarsPrimary varsPrimary = null;
        Expression expr;
        switch (lexer.token()) {
            case Token.KW_PERSIST: {
                long scopeStr = lexer.tokenInfo();
                lexer.nextToken();
                varsPrimary = new VarsPrimary(VarsPrimary.SYS_VAR, VarsPrimary.SCOPE_PERSIST, scopeStr,
                    identifier(false).getIdText());
                break;
            }
            case Token.KW_PERSIST_ONLY: {
                long scopeStr = lexer.tokenInfo();
                lexer.nextToken();
                varsPrimary = new VarsPrimary(VarsPrimary.SYS_VAR, VarsPrimary.SCOPE_PERSIST_ONLY, scopeStr,
                    identifier(false).getIdText());
                break;
            }
            case Token.IDENTIFIER:
                switch (lexer.parseKeyword()) {
                    case Keywords.TRANSACTION:
                        return setSetTransactionStatement(null);
                    case Keywords.GLOBAL: {
                        long scopeStr = lexer.tokenInfo();
                        lexer.nextToken();
                        if (lexer.parseKeyword() == Keywords.TRANSACTION) {
                            return setSetTransactionStatement(true);
                        }
                        varsPrimary = new VarsPrimary(VarsPrimary.SYS_VAR, VarsPrimary.SCOPE_GLOBAL, scopeStr,
                            lexer.bytesValue());
                        break;
                    }
                    case Keywords.LOCAL:
                    case Keywords.SESSION: {
                        long scopeStr = lexer.tokenInfo();
                        lexer.nextToken();
                        if (lexer.parseKeyword() == Keywords.TRANSACTION) {
                            return setSetTransactionStatement(false);
                        }
                        varsPrimary = new VarsPrimary(VarsPrimary.SYS_VAR, VarsPrimary.SCOPE_SESSION, scopeStr,
                            lexer.bytesValue());
                        break;
                    }
                    case Keywords.NEW: {
                        long scopeStr = lexer.tokenInfo();
                        lexer.nextToken();
                        varsPrimary = new VarsPrimary(VarsPrimary.NEW_ROW, null, scopeStr, lexer.bytesValue());
                        break;
                    }
                    case Keywords.OLD: {
                        long scopeStr = lexer.tokenInfo();
                        lexer.nextToken();
                        varsPrimary = new VarsPrimary(VarsPrimary.OLD_ROW, null, scopeStr, lexer.bytesValue());
                        break;
                    }
                    default:
                        varsPrimary = new VarsPrimary(VarsPrimary.USER, null, -1, lexer.bytesValue());
                        break;
                }
                match(Token.IDENTIFIER);
                break;
            case Token.SYS_VAR:
                varsPrimary = systemVariable();
                break;
            case Token.USR_VAR:
                varsPrimary = new VarsPrimary(VarsPrimary.USR_VAR, null, -1, lexer.bytesValue());
                lexer.nextToken();
                break;
            default:
                throw new SQLSyntaxErrorException("unexpected token for SET statement");
        }
        match(Token.OP_EQUALS, Token.OP_ASSIGN);
        expr = exprParser.expression();
        return new Pair<VarsPrimary, Expression>(varsPrimary, expr);
    }

    private SetTransactionStatement setSetTransactionStatement(Boolean global) throws SQLSyntaxErrorException {
        List<Integer> characteristics = null;
        int isolation_level = 0;
        int access_mode = 0;
        if (lexer.token() == Token.IDENTIFIER) {
            int key = lexer.parseKeyword();
            if (key == Keywords.GLOBAL) {
                global = true;
                lexer.nextToken();
            } else if (key == Keywords.SESSION) {
                global = false;
                lexer.nextToken();
            }
        }
        characteristics = new ArrayList<>();
        do {
            lexer.nextToken();
            if (lexer.parseKeyword() == Keywords.ISOLATION) {
                lexer.nextToken();
                matchKeywords(Keywords.LEVEL);
            }
            switch (lexer.token()) {
                case Token.IDENTIFIER: {
                    if (isolation_level == 1) {
                        throw new SQLSyntaxErrorException(
                            "It is not permitted to specify multiple ISOLATION LEVEL clauses in the same SET TRANSACTION statement.");
                    }
                    int key = lexer.parseKeyword();
                    if (key == Keywords.REPEATABLE) {
                        lexer.nextToken();
                        match(Token.KW_READ);
                        characteristics.add(SetTransactionStatement.REPEATABLE_READ);
                        isolation_level++;
                    } else if (key == Keywords.SERIALIZABLE) {
                        lexer.nextToken();
                        characteristics.add(SetTransactionStatement.SERIALIZABLE);
                        isolation_level++;
                    }
                    break;
                }
                case Token.KW_READ:
                    lexer.nextToken();
                    if (lexer.token() == Token.KW_WRITE) {
                        if (access_mode == 1) {
                            throw new SQLSyntaxErrorException(
                                "It is not permitted to specify multiple access-mode clauses in the same SET TRANSACTION statement.");
                        }
                        characteristics.add(SetTransactionStatement.READ_WRITE);
                        access_mode++;
                    } else if (lexer.token() == Token.IDENTIFIER) {
                        int key = lexer.parseKeyword();
                        if (key == Keywords.COMMITTED) {
                            if (isolation_level == 1) {
                                throw new SQLSyntaxErrorException(
                                    "It is not permitted to specify multiple ISOLATION LEVEL clauses in the same SET TRANSACTION statement.");
                            }
                            characteristics.add(SetTransactionStatement.READ_COMMITTED);
                            isolation_level++;
                        } else if (key == Keywords.UNCOMMITTED) {
                            if (isolation_level == 1) {
                                throw new SQLSyntaxErrorException(
                                    "It is not permitted to specify multiple ISOLATION LEVEL clauses in the same SET TRANSACTION statement.");
                            }
                            characteristics.add(SetTransactionStatement.READ_UNCOMMITTED);
                            isolation_level++;
                        } else if (key == Keywords.ONLY) {
                            if (access_mode == 1) {
                                throw new SQLSyntaxErrorException(
                                    "It is not permitted to specify multiple access-mode clauses in the same SET TRANSACTION statement.");
                            }
                            characteristics.add(SetTransactionStatement.READ_ONLY);
                            access_mode++;
                        } else {
                            throw new SQLSyntaxErrorException("unexpected SQL!");
                        }
                    }
                    lexer.nextToken();
            }
        } while (lexer.token() == Token.PUNC_COMMA);
        return new SetTransactionStatement(global, characteristics);
    }

}
