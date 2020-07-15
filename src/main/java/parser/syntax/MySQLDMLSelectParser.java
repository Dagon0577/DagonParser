package parser.syntax;

import parser.ParseInfo;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.*;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.fragment.GroupBy;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.ddl.PartitionOptions;
import parser.ast.fragment.tableref.Dual;
import parser.ast.fragment.tableref.TableReference;
import parser.ast.fragment.tableref.Tables;
import parser.ast.stmt.dml.DMLQueryStatement;
import parser.ast.stmt.dml.DMLSelectStatement;
import parser.ast.stmt.dml.DMLSelectStatement.LockMode;
import parser.ast.stmt.dml.DMLSelectStatement.OutFile;
import parser.ast.stmt.dml.DMLSelectStatement.SelectOption;
import parser.lexer.Lexer;
import parser.token.Keywords;
import parser.token.KeywordsParser;
import parser.token.Token;
import parser.util.Pair;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public class MySQLDMLSelectParser extends MySQLDMLParser {

    public MySQLDMLSelectParser(Lexer lexer, ExprParser exprParser) {
        super(lexer, exprParser);
        exprParser.setSelectParser(this);
    }

    public DMLQueryStatement selectUnion(boolean isSubQuery) throws SQLSyntaxErrorException {
        DMLSelectStatement select = selectPrimary();
        DMLQueryStatement query = buildUnionSelect(select, isSubQuery);
        return query;
    }

    @Override
    public DMLSelectStatement select() throws SQLSyntaxErrorException {
        match(Token.KW_SELECT);
        DMLSelectStatement.SelectOption option = selectOption();
        int selectStart = lexer.getOffset();
        List<Pair<Expression, String>> exprList = selectExprList();
        int selectEnd = lexer.getOffset();
        Tables tables = null;
        Expression where = null;
        GroupBy group = null;
        Expression having = null;
        OrderBy order = null;
        Limit limit = null;
        List<Pair<Identifier, WindowClause>> windows = null;
        DMLSelectStatement.OutFile outFile = null;

        boolean dual = false;
        if (lexer.token() == Token.KW_INTO) {
            outFile = selectOutFile();
        }
        if (lexer.token() == Token.KW_FROM) {
            if (lexer.nextToken() == Token.KW_DUAL) {
                lexer.nextToken();
                dual = true;
                List<TableReference> trs = new ArrayList<TableReference>(1);
                trs.add(new Dual());
                tables = new Tables(trs);
                lexer.addParseInfo(ParseInfo.NO_TABLE);
            } else {
                tables = tableRefs();
            }
        } else {
            lexer.addParseInfo(ParseInfo.NO_TABLE);
        }
        if (lexer.token() == Token.KW_WHERE) {
            lexer.nextToken();
            exprParser.setInWhere(true);
            where = exprParser.expression();
            exprParser.setInWhere(false);
        }
        if (!dual) {
            group = groupBy();
            if (lexer.token() == Token.KW_HAVING) {
                lexer.nextToken();
                having = exprParser.expression();
            }
            windows = windows();
            order = orderBy();
            if (group != null && group.isWithRollup() && order != null) {
                throw new SQLSyntaxErrorException("Incorrect usage of CUBE/ROLLUP and order by");
            }
        }
        limit = limit(false);
        DMLSelectStatement.LockMode lock = null;
        if (!dual) {
            switch (lexer.token()) {
                case Token.KW_FOR:
                    if (lexer.nextToken() == Token.KW_UPDATE) {
                        lock = new LockMode();
                        match(Token.KW_UPDATE);
                        lock.lockType = LockMode.FOR_UPDATE;
                    } else {
                        matchKeywords(Keywords.SHARE);
                        lock = new LockMode();
                        lock.lockType = LockMode.FOR_SHARE;
                    }
                    if (lexer.token() == Token.KW_OF) {
                        lexer.nextToken();
                        List<Identifier> tbs = new ArrayList<>();
                        Identifier tb = identifier(true);
                        tbs.add(tb);
                        for (; lexer.token() == Token.PUNC_COMMA; ) {
                            lexer.nextToken();
                            tb = identifier(true);
                            tbs.add(tb);
                        }
                        lock.tables = tbs;
                    }
                    if (lexer.token() == Token.IDENTIFIER) {
                        int keyword = lexer.parseKeyword();
                        if (keyword == Keywords.NOWAIT) {
                            matchKeywords(Keywords.NOWAIT);
                            lock.lockAction = LockMode.NOWAIT;
                        } else if (keyword == Keywords.SKIP) {
                            matchKeywords(Keywords.SKIP);
                            matchKeywords(Keywords.LOCKED);
                            lock.lockAction = LockMode.SKIP_LOCKED;
                        }
                    }
                    break;
                case Token.KW_LOCK:
                    lexer.nextToken();
                    match(Token.KW_IN);
                    matchKeywords(Keywords.SHARE);
                    matchKeywords(Keywords.MODE);
                    lock = new LockMode();
                    lock.lockType = DMLSelectStatement.LockMode.LOCK_IN_SHARE_MODE;
                    break;
                case Token.KW_PROCEDURE:
                case Token.KW_INTO:
            }
        }
        option.version = lexer.getVersion();
        DMLSelectStatement select =
            new DMLSelectStatement(option, exprList, tables, where, group, having, windows, order, limit, outFile,
                lock);
        select.setSelectStartAndEnd(selectStart, selectEnd);
        return select;
    }

    private DMLSelectStatement.OutFile selectOutFile() throws SQLSyntaxErrorException {
        DMLSelectStatement.OutFile outFile = new DMLSelectStatement.OutFile();
        if (lexer.nextToken() == Token.KW_OUTFILE) {
            outFile.type = DMLSelectStatement.OutFile.OUTFILE;
            lexer.nextToken();
            outFile.outFile = identifier(false);
            if (lexer.token() == Token.KW_CHARACTER) {
                lexer.nextToken();
                match(Token.KW_SET);
                outFile.charset = identifier(false);
            }
            if (lexer.token() == Token.IDENTIFIER) {
                matchKeywords(Keywords.FIELDS);
                for (; lexer.token() == Token.KW_TERMINATED || lexer.token() == Token.KW_ESCAPED
                    || lexer.token() == Token.KW_OPTIONALLY || lexer.token() == Token.KW_ENCLOSED; ) {
                    if (lexer.token() == Token.KW_TERMINATED) {
                        lexer.nextToken();
                        match(Token.KW_BY);
                        outFile.fieldsTerminatedBy = exprParser.parseString();
                    } else if (lexer.token() == Token.KW_ESCAPED) {
                        lexer.nextToken();
                        match(Token.KW_BY);
                        outFile.fieldsEscapedBy = exprParser.parseString();
                    } else if (lexer.token() == Token.KW_OPTIONALLY) {
                        lexer.nextToken();
                        match(Token.KW_ENCLOSED);
                        match(Token.KW_BY);
                        outFile.fieldsEnclosedBy = exprParser.parseString();
                    } else if (lexer.token() == Token.KW_ENCLOSED) {
                        lexer.nextToken();
                        match(Token.KW_BY);
                        outFile.fieldsEnclosedBy = exprParser.parseString();
                    }
                }
            }
            if (lexer.token() == Token.KW_LINES) {
                lexer.nextToken();
                for (; lexer.token() == Token.KW_STARTING || lexer.token() == Token.KW_TERMINATED; ) {
                    if (lexer.token() == Token.KW_STARTING) {
                        lexer.nextToken();
                        match(Token.KW_BY);
                        outFile.linesStartingBy = exprParser.parseString();
                    } else if (lexer.token() == Token.KW_TERMINATED) {
                        lexer.nextToken();
                        match(Token.KW_BY);
                        outFile.linesTerminatedBy = exprParser.parseString();
                    }
                }
            }
        } else if (lexer.token() == Token.IDENTIFIER) {
            if (lexer.parseKeyword() == Keywords.DUMPFILE) {
                outFile.type = DMLSelectStatement.OutFile.DUMPFILE;
                matchKeywords(Keywords.DUMPFILE);
                outFile.dumpFile = identifier(false);
            } else {
                outFile.type = DMLSelectStatement.OutFile.PARAM;
                outFile.into = identifier(false);
            }
        } else {
            outFile.type = DMLSelectStatement.OutFile.VAR;
            List<VarsPrimary> vars = new ArrayList<>();
            VarsPrimary var = new VarsPrimary(VarsPrimary.USR_VAR, null, -1, lexer.bytesValue());
            lexer.nextToken();
            vars.add(var);
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                var = new VarsPrimary(VarsPrimary.USR_VAR, null, -1, lexer.bytesValue());
                lexer.nextToken();
                vars.add(var);
            }
            outFile.vars = vars;
        }
        return outFile;
    }

    private SelectOption selectOption() throws SQLSyntaxErrorException {
        for (SelectOption option = new SelectOption(); ; lexer.nextToken()) {
            outer:
            switch (lexer.token()) {
                case Token.KW_ALL:
                    option.dupStrategy = SelectOption.DUP_STRATEGY_ALL;
                    break outer;
                case Token.KW_DISTINCT:
                    option.dupStrategy = SelectOption.DUP_STRATEGY_DISTINCT;
                    lexer.addParseInfo(ParseInfo.AGGREGATION);
                    break outer;
                case Token.KW_DISTINCTROW:
                    option.dupStrategy = SelectOption.DUP_STRATEGY_DISTINCTROW;
                    break outer;
                case Token.KW_HIGH_PRIORITY:
                    option.highPriority = true;
                    break outer;
                case Token.KW_STRAIGHT_JOIN:
                    option.straightJoin = true;
                    break outer;
                case Token.KW_SQL_SMALL_RESULT:
                    option.resultSize = SelectOption.SQL_SMALL_RESULT;
                    break outer;
                case Token.KW_SQL_BIG_RESULT:
                    option.resultSize = SelectOption.SQL_BIG_RESULT;
                    break outer;
                case Token.KW_SQL_CALC_FOUND_ROWS:
                    option.sqlCalcFoundRows = true;
                    break outer;
                case Token.IDENTIFIER:
                    int key = lexer.parseKeyword();
                    if (key > 0) {
                        switch (key) {
                            case Keywords.SQL_BUFFER_RESULT:
                                if (option.sqlBufferResult) {
                                    return option;
                                }
                                option.sqlBufferResult = true;
                                break outer;
                            case Keywords.SQL_CACHE:
                                if (option.sqlCache != null) {
                                    return option;
                                }
                                option.sqlCache = SelectOption.CACHE_STRATEGY_SQL_CACHE;
                                break outer;
                            case Keywords.SQL_NO_CACHE:
                                if (option.sqlCache != null) {
                                    return option;
                                }
                                option.sqlCache = SelectOption.CACHE_STRATEGY_SQL_NO_CACHE;
                                break outer;
                            default:
                                return option;
                        }
                    }
                default:
                    return option;
            }
        }
    }

    private List<Pair<Expression, String>> selectExprList() throws SQLSyntaxErrorException {
        Expression expr = exprParser.expression();
        if (expr instanceof Identifier) {
            if (expr instanceof Wildcard) {
                lexer.addParseInfo(ParseInfo.WILDCARD_IN_SELECT);
            } else {
                byte[] idInfo = ((Identifier)expr).getIdText();
                int keyword = KeywordsParser.get(idInfo, 0, idInfo.length);
                switch (keyword) {
                    case Keywords.TIME:
                    case Keywords.DATE:
                    case Keywords.TIMESTAMP:
                        if (lexer.token() == Token.LITERAL_CHARS) {
                            long offset = lexer.getOffset();
                            while (lexer.token() == Token.LITERAL_CHARS) {
                                lexer.nextToken();
                            }
                            long nextOffset = lexer.getOffset();
                            long size = nextOffset - offset;
                            long info = (offset << 32) | (((size << 32) >> 32));
                            expr = new LiteralString(idInfo, info, false);
                        }
                        break;
                }
            }
        }
        String alias = alias();
        List<Pair<Expression, String>> list;
        if (lexer.token() == Token.PUNC_COMMA) {
            list = new LinkedList<Pair<Expression, String>>();
            list.add(new Pair<Expression, String>(expr, alias));
        } else {
            list = new ArrayList<Pair<Expression, String>>(1);
            list.add(new Pair<Expression, String>(expr, alias));
            return list;
        }
        for (; lexer.token() == Token.PUNC_COMMA; list.add(new Pair<Expression, String>(expr, alias))) {
            lexer.nextToken();
            expr = exprParser.expression();
            if (expr instanceof Identifier) {
                if (expr instanceof Wildcard) {
                    lexer.addParseInfo(ParseInfo.WILDCARD_IN_SELECT);
                } else {
                    byte[] idInfo = ((Identifier)expr).getIdText();
                    int keyword = KeywordsParser.get(idInfo, 0, idInfo.length);
                    switch (keyword) {
                        case Keywords.TIME:
                        case Keywords.DATE:
                        case Keywords.TIMESTAMP:
                            if (lexer.token() == Token.LITERAL_CHARS) {
                                long offset = lexer.getOffset();
                                while (lexer.token() == Token.LITERAL_CHARS) {
                                    lexer.nextToken();
                                }
                                long nextOffset = lexer.getOffset();
                                long size = nextOffset - offset;
                                long info = (offset << 32) | (((size << 32) >> 32));
                                expr = new LiteralString(idInfo, info, false);
                            }
                            break;
                    }
                }
            }
            alias = alias();
        }
        return list;
    }

    private List<Pair<Identifier, WindowClause>> windows() throws SQLSyntaxErrorException {
        if (lexer.token() != Token.KW_WINDOW) {
            return null;
        }
        lexer.nextToken();
        List<Pair<Identifier, WindowClause>> windows = new ArrayList<>();
        for (; ; ) {
            Identifier windowName = identifier();
            match(Token.KW_AS);
            match(Token.PUNC_LEFT_PAREN);
            WindowClause window = window();
            windows.add(new Pair<>(windowName, window));
            match(Token.PUNC_RIGHT_PAREN);
            if (lexer.token() == Token.PUNC_COMMA) {
                lexer.nextToken();
                continue;
            }
            break;
        }
        return windows;
    }

    private WindowClause window() throws SQLSyntaxErrorException {
        Identifier name = null;
        PartitionOptions partition = null;
        OrderBy orderBy = null;
        Integer frameUnit = null;
        Pair<Integer, Expression> frameStart = null;
        Pair<Integer, Expression> frameEnd = null;
        if (lexer.token() == Token.IDENTIFIER) {
            name = identifier();
        }
        if (lexer.token() == Token.KW_PARTITION) {
            //partition = new MySQLDDLParser(lexer, exprParser).partionOptions();
        }
        if (lexer.token() == Token.KW_ORDER) {
            orderBy = orderBy();
        }
        boolean checkFrame = false;
        if (lexer.token() == Token.KW_RANGE) {
            frameUnit = WindowClause.FRAME_UNIT_RANGE;
            checkFrame = true;
        } else if (lexer.token() == Token.KW_ROWS) {
            frameUnit = WindowClause.FRAME_UNIT_ROWS;
            checkFrame = true;
        }
        if (checkFrame) {
            lexer.nextToken();
            if (lexer.token() == Token.KW_BETWEEN) {
                lexer.nextToken();
                frameStart = frameBound();
                match(Token.KW_AND);
                frameEnd = frameBound();
            } else {
                frameStart = frameBound();
            }
        }
        return new WindowClause(name, partition, orderBy, frameUnit, frameStart, frameEnd);
    }

    private Pair<Integer, Expression> frameBound() throws SQLSyntaxErrorException {
        Integer type = null;
        Expression expr = null;
        switch (lexer.parseKeyword()) {
            case Keywords.UNBOUNDED:
                lexer.nextToken();
                switch (lexer.parseKeyword()) {
                    case Keywords.PRECEDING:
                        lexer.nextToken();
                        type = WindowClause.FRAM_UNBOUNDED_PRECEDING;
                        break;
                    case Keywords.FOLLOWING:
                        lexer.nextToken();
                        type = WindowClause.FRAM_UNBOUNDED_FOLLOWING;
                        break;
                }
                break;
            case Keywords.CURRENT:
                lexer.nextToken();
                match(Token.KW_ROW);
                type = WindowClause.FRAM_CURRENT_ROW;
                break;
        }
        if (type == null) {
            expr = exprParser.expression();
            switch (lexer.parseKeyword()) {
                case Keywords.PRECEDING:
                    lexer.nextToken();
                    type = WindowClause.FRAM_UNBOUNDED_PRECEDING;
                    break;
                case Keywords.FOLLOWING:
                    lexer.nextToken();
                    type = WindowClause.FRAM_UNBOUNDED_FOLLOWING;
                    break;
            }
        }
        return new Pair<Integer, Expression>(type, expr);
    }

    public OverClause over(FunctionExpression expr) throws SQLSyntaxErrorException {
        match(Token.KW_OVER);
        if (lexer.token() == Token.PUNC_LEFT_PAREN) {
            lexer.nextToken();
            WindowClause window = window();
            match(Token.PUNC_RIGHT_PAREN);
            return new OverClause(expr, null, window);
        }
        return new OverClause(expr, identifier(), null);
    }

}
