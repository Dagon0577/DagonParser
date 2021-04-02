package parser.syntax;

import parser.ParseInfo;
import parser.ast.expression.ComparisionExpression;
import parser.ast.expression.Expression;
import parser.ast.expression.QueryExpression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.ParamMarker;
import parser.ast.expression.primary.RowExpression;
import parser.ast.expression.primary.WithClause;
import parser.ast.expression.primary.WithClause.CTE;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.fragment.GroupBy;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.tableref.*;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dal.DALLoadIndexIntoCacheStatement;
import parser.ast.stmt.dml.*;
import parser.lexer.Lexer;
import parser.token.Keywords;
import parser.token.Token;
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
public class MySQLDMLParser extends AbstractParser {

    protected ExprParser exprParser;

    public MySQLDMLParser(Lexer lexer, ExprParser exprParser) {
        super(lexer);
        this.exprParser = exprParser;
    }

    // parser
    public DMLSelectStatement select() throws SQLSyntaxErrorException {
        MySQLDMLSelectParser parser = new MySQLDMLSelectParser(lexer, exprParser);
        return parser.select();
    }

    public DMLUpdateStatement update() throws SQLSyntaxErrorException {
        match(Token.KW_UPDATE);
        boolean lowPriority = false;
        boolean ignore = false;
        if (lexer.token() == Token.KW_LOW_PRIORITY) {
            lexer.nextToken();
            lowPriority = true;
        }
        if (lexer.token() == Token.KW_IGNORE) {
            lexer.nextToken();
            ignore = true;
        }
        Tables tableRefs = tableRefs();
        List<Identifier> partition = null;
        if (lexer.token() == Token.KW_PARTITION) {
            partition = new ArrayList<>();
            lexer.nextToken();
            match(Token.PUNC_LEFT_PAREN);
            partition.add(identifier());
            while (lexer.token() != Token.EOF) {
                if (lexer.token() == Token.PUNC_COMMA) {
                    lexer.nextToken();
                    partition.add(identifier());
                    continue;
                }
                break;
            }
            match(Token.PUNC_RIGHT_PAREN);
        }
        match(Token.KW_SET);
        List<Pair<Identifier, Expression>> values;
        Identifier col = identifier();
        match(Token.OP_EQUALS, Token.OP_ASSIGN);
        Expression expr = exprParser.expression();
        if (lexer.token() == Token.PUNC_COMMA) {
            values = new LinkedList<Pair<Identifier, Expression>>();
            values.add(new Pair<Identifier, Expression>(col, expr));
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                col = identifier();
                match(Token.OP_EQUALS, Token.OP_ASSIGN);
                expr = exprParser.expression();
                values.add(new Pair<Identifier, Expression>(col, expr));
            }
        } else {
            values = new ArrayList<Pair<Identifier, Expression>>(1);
            values.add(new Pair<Identifier, Expression>(col, expr));
        }
        Expression where = null;
        if (lexer.token() == Token.KW_WHERE) {
            lexer.nextToken();
            exprParser.setInWhere(true);
            where = exprParser.expression();
            exprParser.setInWhere(false);
        }
        OrderBy orderBy = null;
        Limit limit = null;
        orderBy = orderBy();
        limit = limit(true);
        DMLUpdateStatement update =
            new DMLUpdateStatement(lowPriority, ignore, tableRefs, values, where, orderBy, limit, partition);
        return update;
    }

    public DMLInsertReplaceStatement insert() throws SQLSyntaxErrorException {
        match(Token.KW_INSERT);
        int mode = DMLInsertReplaceStatement.UNDEF;
        boolean ignore = false;
        switch (lexer.token()) {
            case Token.KW_LOW_PRIORITY:
                lexer.nextToken();
                mode = DMLInsertReplaceStatement.LOW;
                break;
            case Token.KW_DELAYED:
                lexer.nextToken();
                mode = DMLInsertReplaceStatement.DELAY;
                break;
            case Token.KW_HIGH_PRIORITY:
                lexer.nextToken();
                mode = DMLInsertReplaceStatement.HIGH;
                break;
        }
        if (lexer.token() == Token.KW_IGNORE) {
            ignore = true;
            lexer.nextToken();
        }
        if (lexer.token() == Token.KW_INTO) {
            lexer.nextToken();
        }
        Identifier table = identifier(true);
        lexer.addParseInfo(ParseInfo.SINGLE_TABLE);
        List<Pair<Identifier, Expression>> duplicateUpdate;
        List<Identifier> columns;
        List<RowExpression> rows;
        QueryExpression select = null;
        List<Identifier> partitions = null;
        if (lexer.token() == Token.KW_PARTITION) {
            partitions = new ArrayList<>();
            lexer.nextToken();
            match(Token.PUNC_LEFT_PAREN);
            partitions.add(identifier());
            while (lexer.token() != Token.EOF) {
                if (lexer.token() == Token.PUNC_COMMA) {
                    lexer.nextToken();
                    partitions.add(identifier());
                    continue;
                }
                break;
            }
            match(Token.PUNC_RIGHT_PAREN);
        }
        List<Expression> tempRowValue;
        switch (lexer.token()) {
            case Token.KW_SET:
                lexer.nextToken();
                columns = new LinkedList<Identifier>();
                tempRowValue = new LinkedList<Expression>();
                for (; ; lexer.nextToken()) {
                    Identifier id = identifier();
                    match(Token.OP_EQUALS, Token.OP_ASSIGN);
                    Expression expr = exprParser.expression();
                    columns.add(id);
                    tempRowValue.add(expr);
                    if (lexer.token() != Token.PUNC_COMMA) {
                        break;
                    }
                }
                rows = new ArrayList<RowExpression>(1);
                rows.add(new RowExpression(tempRowValue));
                duplicateUpdate = onDuplicateUpdate();
                return new DMLInsertReplaceStatement(true, mode, ignore, table, columns, rows, null, duplicateUpdate,
                    partitions);
            case Token.IDENTIFIER:
                if (!equalsKeyword(Keywords.VALUE)) {
                    break;
                }
            case Token.KW_VALUES:
                lexer.nextToken();
                columns = null;
                rows = rowList();
                duplicateUpdate = onDuplicateUpdate();
                return new DMLInsertReplaceStatement(true, mode, ignore, table, columns, rows, null, duplicateUpdate,
                    partitions);
            case Token.KW_SELECT:
                columns = null;
                select = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
                duplicateUpdate = onDuplicateUpdate();
                return new DMLInsertReplaceStatement(true, mode, ignore, table, columns, null, select, duplicateUpdate,
                    partitions);
            case Token.PUNC_LEFT_PAREN:
                switch (lexer.nextToken()) {
                    case Token.PUNC_LEFT_PAREN:
                    case Token.KW_SELECT:
                        columns = null;
                        select = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
                        match(Token.PUNC_RIGHT_PAREN);
                        duplicateUpdate = onDuplicateUpdate();
                        return new DMLInsertReplaceStatement(true, mode, ignore, table, columns, null, select,
                            duplicateUpdate, partitions);
                }
                if (lexer.token() != Token.PUNC_RIGHT_PAREN) {
                    columns = idList(false);
                } else {
                    columns = null;
                }
                match(Token.PUNC_RIGHT_PAREN);
                switch (lexer.token()) {
                    case Token.PUNC_LEFT_PAREN:
                    case Token.KW_SELECT:
                        select = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
                        duplicateUpdate = onDuplicateUpdate();
                        return new DMLInsertReplaceStatement(true, mode, ignore, table, columns, null, select,
                            duplicateUpdate, partitions);
                    case Token.KW_VALUES:
                        lexer.nextToken();
                        break;
                    default:
                        matchKeywords(Keywords.VALUE);
                }
                rows = rowList();
                duplicateUpdate = onDuplicateUpdate();
                return new DMLInsertReplaceStatement(true, mode, ignore, table, columns, rows, null, duplicateUpdate,
                    partitions);
        }
        throw new SQLSyntaxErrorException("unexpected token for insert: " + lexer.token());
    }

    public DMLDeleteStatement delete() throws SQLSyntaxErrorException {
        match(Token.KW_DELETE);
        boolean lowPriority = false;
        boolean quick = false;
        boolean ignore = false;
        loopOpt:
        for (; ; lexer.nextToken()) {
            switch (lexer.token()) {
                case Token.KW_LOW_PRIORITY:
                    lowPriority = true;
                    break;
                case Token.KW_IGNORE:
                    ignore = true;
                    break;
                case Token.IDENTIFIER:
                    if (lexer.parseKeyword() == Keywords.QUICK) {
                        quick = true;
                        break;
                    }
                default:
                    break loopOpt;
            }
        }
        List<Identifier> tempList;
        Tables tempRefs;
        Expression tempWhere;
        List<Identifier> partition = null;
        if (lexer.token() == Token.KW_FROM) {
            lexer.nextToken();
            Identifier id = identifier(true);
            tempList = new ArrayList<Identifier>(1);
            tempList.add(id);
            lexer.addParseInfo(ParseInfo.SINGLE_TABLE);
            switch (lexer.token()) {
                case Token.PUNC_COMMA:
                    tempList = buildIdList(id, true);
                    lexer.removeParseInfo(ParseInfo.SINGLE_TABLE);
                case Token.KW_USING:
                    lexer.nextToken();
                    tempRefs = tableRefs();
                    lexer.removeParseInfo(ParseInfo.SINGLE_TABLE);
                    if (lexer.token() == Token.KW_WHERE) {
                        lexer.nextToken();
                        tempWhere = exprParser.expression();
                        return new DMLDeleteStatement(lowPriority, quick, ignore, tempList, tempRefs, tempWhere);
                    }
                    return new DMLDeleteStatement(lowPriority, quick, ignore, tempList, tempRefs);
                case Token.KW_WHERE:
                case Token.KW_ORDER:
                case Token.KW_LIMIT:
                    break;
                case Token.KW_PARTITION:
                    partition = new ArrayList<>();
                    lexer.nextToken();
                    match(Token.PUNC_LEFT_PAREN);
                    partition.add(identifier());
                    while (lexer.token() != Token.EOF) {
                        if (lexer.token() == Token.PUNC_COMMA) {
                            lexer.nextToken();
                            partition.add(identifier());
                            continue;
                        }
                        break;
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                    break;
                default:
                    return new DMLDeleteStatement(lowPriority, quick, ignore, id, partition);
            }
            tempWhere = null;
            OrderBy orderBy = null;
            Limit limit = null;
            if (lexer.token() == Token.KW_WHERE) {
                lexer.nextToken();
                exprParser.setInWhere(true);
                tempWhere = exprParser.expression();
                exprParser.setInWhere(false);
            }
            if (lexer.token() == Token.KW_ORDER) {
                orderBy = orderBy();
            }
            if (lexer.token() == Token.KW_LIMIT) {
                limit = limit(true);
            }
            DMLDeleteStatement delete =
                new DMLDeleteStatement(lowPriority, quick, ignore, id, tempWhere, orderBy, limit, partition);
            return delete;
        }
        tempList = idList(true);
        match(Token.KW_FROM);
        tempRefs = tableRefs();
        if (lexer.token() == Token.KW_WHERE) {
            lexer.nextToken();
            tempWhere = exprParser.expression();
            return new DMLDeleteStatement(lowPriority, quick, ignore, tempList, tempRefs, tempWhere);
        }
        return new DMLDeleteStatement(lowPriority, quick, ignore, tempList, tempRefs);
    }

    public DMLInsertReplaceStatement replace() throws SQLSyntaxErrorException {
        match(Token.KW_REPLACE);
        int mode = DMLInsertReplaceStatement.UNDEF;
        switch (lexer.token()) {
            case Token.KW_LOW_PRIORITY:
                lexer.nextToken();
                mode = DMLInsertReplaceStatement.LOW;
                break;
            case Token.KW_DELAYED:
                lexer.nextToken();
                mode = DMLInsertReplaceStatement.DELAY;
                break;
        }
        if (lexer.token() == Token.KW_INTO) {
            lexer.nextToken();
        }
        Identifier table = identifier(true);
        List<Identifier> columnNameList;
        List<RowExpression> rowList;
        QueryExpression select = null;
        List<Identifier> partition = null;
        if (lexer.token() == Token.KW_PARTITION) {
            partition = new ArrayList<>();
            lexer.nextToken();
            match(Token.PUNC_LEFT_PAREN);
            partition.add(identifier());
            while (lexer.token() != Token.EOF) {
                if (lexer.token() == Token.PUNC_COMMA) {
                    lexer.nextToken();
                    partition.add(identifier());
                    continue;
                }
                break;
            }
            match(Token.PUNC_RIGHT_PAREN);
        }
        List<Expression> tempRowValue;
        switch (lexer.token()) {
            case Token.KW_SET:
                lexer.nextToken();
                columnNameList = new LinkedList<Identifier>();
                tempRowValue = new LinkedList<Expression>();
                for (;; lexer.nextToken()) {
                    Identifier id = identifier();
                    match(Token.OP_EQUALS, Token.OP_ASSIGN);
                    Expression expr = exprParser.expression();
                    columnNameList.add(id);
                    tempRowValue.add(expr);
                    if (lexer.token() != Token.PUNC_COMMA) {
                        break;
                    }
                }
                rowList = new ArrayList<RowExpression>(1);
                rowList.add(new RowExpression(tempRowValue));
                return new DMLInsertReplaceStatement(false, mode, false, table, columnNameList,
                    rowList, null, null, partition);
            case Token.IDENTIFIER:
                if (!equalsKeyword(Keywords.VALUE)) {
                    break;
                }
            case Token.KW_VALUES:
                lexer.nextToken();
                columnNameList = null;
                rowList = rowList();
                return new DMLInsertReplaceStatement(false, mode, false, table, columnNameList,
                    rowList, null, null, partition);
            case Token.KW_SELECT:
                columnNameList = null;
                select = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
                return new DMLInsertReplaceStatement(false, mode, false, table, columnNameList,
                    null, select, null, partition);
            case Token.PUNC_LEFT_PAREN:
                switch (lexer.nextToken()) {
                    case Token.PUNC_LEFT_PAREN:
                    case Token.KW_SELECT:
                        columnNameList = null;
                        select = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
                        match(Token.PUNC_RIGHT_PAREN);
                        return new DMLInsertReplaceStatement(false, mode, false, table,
                            columnNameList, null, select, null, partition);
                }
                columnNameList = idList(false);
                match(Token.PUNC_RIGHT_PAREN);
                switch (lexer.token()) {
                    case Token.PUNC_LEFT_PAREN:
                    case Token.KW_SELECT:
                        select = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
                        return new DMLInsertReplaceStatement(false, mode, false, table,
                            columnNameList, null, select, null, partition);
                    case Token.KW_VALUES:
                        lexer.nextToken();
                        break;
                    default:
                        matchKeywords(Keywords.VALUE);
                }
                rowList = rowList();
                return new DMLInsertReplaceStatement(false, mode, false, table, columnNameList,
                    rowList, null, null, partition);
        }
        throw new SQLSyntaxErrorException("unexpected token for replace: " + lexer.token());
    }

    public DMLCallStatement call() throws SQLSyntaxErrorException {
        match(Token.KW_CALL);
        Identifier procedure = identifier();
        match(Token.PUNC_LEFT_PAREN);
        if (lexer.token() == Token.PUNC_RIGHT_PAREN) {
            lexer.nextToken();
            return new DMLCallStatement(procedure, null);
        }
        List<Expression> arguments;
        Expression expr = exprParser.expression();
        switch (lexer.token()) {
            case Token.PUNC_COMMA:
                arguments = new LinkedList<Expression>();
                arguments.add(expr);
                for (; lexer.token() == Token.PUNC_COMMA;) {
                    lexer.nextToken();
                    expr = exprParser.expression();
                    arguments.add(expr);
                }
                match(Token.PUNC_RIGHT_PAREN);
                return new DMLCallStatement(procedure, arguments);
            case Token.PUNC_RIGHT_PAREN:
                lexer.nextToken();
                arguments = new ArrayList<Expression>(1);
                arguments.add(expr);
                return new DMLCallStatement(procedure, arguments);
            default:
                throw new SQLSyntaxErrorException("expect ',' or ')' after first argument of procedure");
        }
    }

    public DMLDoStatement parseDo() throws SQLSyntaxErrorException {
        matchKeywords(Keywords.DO);
        List<Expression> arguments;
        Expression expr = exprParser.expression();
        if (lexer.token() == Token.EOF || lexer.token() == Token.PUNC_SEMICOLON) {
            arguments = new ArrayList<>(1);
            arguments.add(expr);
            return new DMLDoStatement(arguments);
        }
        arguments = new ArrayList<>();
        arguments.add(expr);
        while (lexer.token() == Token.PUNC_COMMA) {
            lexer.nextToken();
            expr = exprParser.expression();
            arguments.add(expr);
        }
        return new DMLDoStatement(arguments);
    }

    public DMLHandlerStatement handler() throws SQLSyntaxErrorException {
        matchKeywords(Keywords.HANDLER);
        Identifier table = identifier(false);
        switch (lexer.token()) {
            case Token.KW_READ: {
                lexer.nextToken();
                Integer order = null;
                Identifier index = null;
                if (lexer.token() == Token.IDENTIFIER) {
                    int n = getHandlerOrder();
                    if (n > 0) {
                        order = n;
                    } else {
                        index = identifier();
                        n = getHandlerOrder();
                        if (n > 0) {
                            order = n;
                        }
                    }
                }
                Integer operator = null;
                List<Expression> arguments = null;
                switch (lexer.token()) {
                    case Token.OP_EQUALS:
                        operator = DMLHandlerStatement.EQUALS;
                    case Token.OP_LESS_OR_EQUALS:
                        if (operator == null) {
                            operator = DMLHandlerStatement.LESS_EQUALS;
                        }
                    case Token.OP_GREATER_OR_EQUALS:
                        if (operator == null) {
                            operator = DMLHandlerStatement.GREAT_EQUALS;
                        }
                    case Token.OP_LESS_THAN:
                        if (operator == null) {
                            operator = DMLHandlerStatement.LESS;
                        }
                    case Token.OP_GREATER_THAN:
                        if (operator == null) {
                            operator = DMLHandlerStatement.GREAT;
                        }
                        lexer.nextToken();
                        match(Token.PUNC_LEFT_PAREN);
                        Expression expr = exprParser.expression();
                        switch (lexer.token()) {
                            case Token.PUNC_COMMA:
                                arguments = new LinkedList<Expression>();
                                arguments.add(expr);
                                for (; lexer.token() == Token.PUNC_COMMA;) {
                                    lexer.nextToken();
                                    expr = exprParser.expression();
                                    arguments.add(expr);
                                }
                                match(Token.PUNC_RIGHT_PAREN);
                                break;
                            case Token.PUNC_RIGHT_PAREN:
                                lexer.nextToken();
                                arguments = new ArrayList<Expression>(1);
                                arguments.add(expr);
                                break;
                        }
                        break;
                }
                Expression where = null;
                if (lexer.token() == Token.KW_WHERE) {
                    lexer.nextToken();
                    where = exprParser.expression();
                }
                Limit limit = null;
                if (lexer.token() == Token.KW_LIMIT) {
                    limit = limit(true);
                }
                return new DMLHandlerStatement(table, index, operator, arguments, order, where,
                    limit);
            }
            case Token.IDENTIFIER: {
                switch (lexer.parseKeyword()) {
                    case Keywords.CLOSE:
                        lexer.nextToken();
                        return new DMLHandlerStatement(table);
                    case Keywords.OPEN:
                        lexer.nextToken();
                        String alias = null;
                        if (lexer.token() == Token.KW_AS) {
                            alias = as();
                        }
                        return new DMLHandlerStatement(table, alias);
                }
            }
        }
        throw new SQLSyntaxErrorException("expect OPEN | READ | CLOSE");
    }

    public DMLImportTableStatement parseImport() throws SQLSyntaxErrorException {
        matchKeywords(Keywords.IMPORT);
        match(Token.KW_TABLE);
        match(Token.KW_FROM);
        List<LiteralString> files = new LinkedList<>();
        LiteralString expr = exprParser.parseString();
        files.add(expr);
        for (; lexer.token() == Token.PUNC_COMMA;) {
            lexer.nextToken();
            expr = exprParser.parseString();
            files.add(expr);
        }
        return new DMLImportTableStatement(files);
    }

    public SQLStatement load() throws SQLSyntaxErrorException {
        match(Token.KW_LOAD);
        if (lexer.token() == Token.IDENTIFIER) {
            int key = lexer.parseKeyword();
            if (key == Keywords.DATA) {
                lexer.nextToken();
                Integer priority = null;
                if (lexer.token() == Token.KW_LOW_PRIORITY) {
                    lexer.nextToken();
                    priority = DMLLoadDataInFileStatement.LOW_PRIORITY;
                } else if (lexer.token() == Token.IDENTIFIER
                    && equalsKeyword(Keywords.CONCURRENT)) {
                    lexer.nextToken();
                    priority = DMLLoadDataInFileStatement.CONCURRENT;
                }
                Boolean isLocal = null;
                if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.LOCAL)) {
                    isLocal = true;
                    lexer.nextToken();
                }
                match(Token.KW_INFILE);
                LiteralString fileName = exprParser.parseString();
                Integer insertType = null;
                if (lexer.token() == Token.KW_REPLACE) {
                    lexer.nextToken();
                    insertType = DMLLoadDataInFileStatement.REPLACE;
                } else if (lexer.token() == Token.KW_IGNORE) {
                    lexer.nextToken();
                    insertType = DMLLoadDataInFileStatement.IGNORE;
                }
                match(Token.KW_INTO);
                match(Token.KW_TABLE);
                Identifier table = identifier(true);
                List<Identifier> partition = null;
                if (lexer.token() == Token.KW_PARTITION) {
                    lexer.nextToken();
                    match(Token.PUNC_LEFT_PAREN);
                    partition = new ArrayList<>();
                    Identifier part = identifier();
                    partition.add(part);
                    for (; lexer.token() == Token.PUNC_COMMA;) {
                        lexer.nextToken();
                        part = identifier();
                        partition.add(part);
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                Identifier charset = null;
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charset = identifier();
                }
                LiteralString fieldsTerminatedBy = null;
                LiteralString fieldsEnclosedBy = null;
                LiteralString fieldsEscapedBy = null;
                LiteralString linesStartingBy = null;
                LiteralString linesTerminatedBy = null;
                if (lexer.token() == Token.IDENTIFIER) {
                    key = lexer.parseKeyword();
                    if (key == Keywords.FIELDS || key == Keywords.COLUMNS) {
                        lexer.nextToken();
                        if (lexer.token() == Token.KW_TERMINATED) {
                            lexer.nextToken();
                            match(Token.KW_BY);
                            fieldsTerminatedBy = exprParser.parseString();
                        } else if (lexer.token() == Token.KW_ESCAPED) {
                            lexer.nextToken();
                            match(Token.KW_BY);
                            fieldsEscapedBy = exprParser.parseString();
                        } else if (lexer.token() == Token.KW_OPTIONALLY) {
                            lexer.nextToken();
                            match(Token.KW_ENCLOSED);
                            match(Token.KW_BY);
                            fieldsEnclosedBy = exprParser.parseString();
                        } else if (lexer.token() == Token.KW_ENCLOSED) {
                            lexer.nextToken();
                            match(Token.KW_BY);
                            fieldsEnclosedBy = exprParser.parseString();
                        }
                    }
                }
                if (lexer.token() == Token.KW_LINES) {
                    lexer.nextToken();
                    if (lexer.token() == Token.KW_STARTING) {
                        lexer.nextToken();
                        match(Token.KW_BY);
                        linesStartingBy = exprParser.parseString();
                    } else if (lexer.token() == Token.KW_TERMINATED) {
                        lexer.nextToken();
                        match(Token.KW_BY);
                        linesTerminatedBy = exprParser.parseString();
                    }
                }
                Long ignoreLine = null;
                if (lexer.token() == Token.KW_IGNORE) {
                    lexer.nextToken();
                    ignoreLine = exprParser.longValue();
                    match(Token.KW_LINES, Token.KW_ROWS);
                }
                List<Expression> columns = null;
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    columns = new ArrayList<>();
                    Expression expr = exprParser.expression();
                    columns.add(expr);
                    for (; lexer.token() == Token.PUNC_COMMA;) {
                        lexer.nextToken();
                        expr = exprParser.expression();
                        columns.add(expr);
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                List<ComparisionExpression> values = null;
                if (lexer.token() == Token.KW_SET) {
                    lexer.nextToken();
                    values = new ArrayList<>();
                    ComparisionExpression expr = loadComparisionExpression();
                    values.add(expr);
                    for (; lexer.token() == Token.PUNC_COMMA;) {
                        lexer.nextToken();
                        expr = loadComparisionExpression();
                        values.add(expr);
                    }
                }
                return new DMLLoadDataInFileStatement(priority, isLocal, fileName, insertType,
                    table, partition, charset, fieldsTerminatedBy, fieldsEnclosedBy,
                    fieldsEscapedBy, linesStartingBy, linesTerminatedBy, ignoreLine, columns,
                    values);
            } else if (key == Keywords.XML) {
                lexer.nextToken();
                Integer priority = null;
                if (lexer.token() == Token.KW_LOW_PRIORITY) {
                    lexer.nextToken();
                    priority = DMLLoadDataInFileStatement.LOW_PRIORITY;
                } else if (lexer.token() == Token.IDENTIFIER
                    && equalsKeyword(Keywords.CONCURRENT)) {
                    lexer.nextToken();
                    priority = DMLLoadDataInFileStatement.CONCURRENT;
                }
                Boolean isLocal = null;
                if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.LOCAL)) {
                    isLocal = true;
                    lexer.nextToken();
                }
                match(Token.KW_INFILE);
                LiteralString fileName = exprParser.parseString();
                Integer insertType = null;
                if (lexer.token() == Token.KW_REPLACE) {
                    lexer.nextToken();
                    insertType = DMLLoadDataInFileStatement.REPLACE;
                } else if (lexer.token() == Token.KW_IGNORE) {
                    lexer.nextToken();
                    insertType = DMLLoadDataInFileStatement.IGNORE;
                }
                match(Token.KW_INTO);
                match(Token.KW_TABLE);
                Identifier table = identifier(true);
                Identifier charset = null;
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charset = identifier();
                }
                LiteralString identified = null;
                if (lexer.token() == Token.KW_ROWS) {
                    lexer.nextToken();
                    matchKeywords(Keywords.IDENTIFIED);
                    match(Token.KW_BY);
                    identified = exprParser.parseString();
                }
                Long ignoreLine = null;
                if (lexer.token() == Token.KW_IGNORE) {
                    lexer.nextToken();
                    ignoreLine = exprParser.longValue();
                    match(Token.KW_LINES, Token.KW_ROWS);
                }
                List<Expression> columns = null;
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    columns = new ArrayList<>();
                    Expression expr = exprParser.expression();
                    columns.add(expr);
                    for (; lexer.token() == Token.PUNC_COMMA;) {
                        lexer.nextToken();
                        expr = exprParser.expression();
                        columns.add(expr);
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                List<ComparisionExpression> values = null;
                if (lexer.token() == Token.KW_SET) {
                    lexer.nextToken();
                    values = new ArrayList<>();
                    ComparisionExpression expr = loadComparisionExpression();
                    values.add(expr);
                    for (; lexer.token() == Token.PUNC_COMMA;) {
                        lexer.nextToken();
                        expr = loadComparisionExpression();
                        values.add(expr);
                    }
                }
                return new DMLLoadXMLStatement(priority, isLocal, fileName, insertType, table,
                    identified, charset, ignoreLine, columns, values);
            }
        } else if (lexer.token() == Token.KW_INDEX) {
            lexer.nextToken();
            match(Token.KW_INTO);
            matchKeywords(Keywords.CACHE);
            List<DALLoadIndexIntoCacheStatement.TableIndexList> tableIndexLists = new ArrayList<>();
            DALLoadIndexIntoCacheStatement loadIndexIntoCacheStatement =
                new DALLoadIndexIntoCacheStatement(tableIndexLists);
            do {
                if (lexer.token() == Token.PUNC_COMMA) {
                    lexer.nextToken();
                }
                Identifier table = null;
                boolean partitionAll = false;
                List<Identifier> partitions = null;
                List<Identifier> indexs = null;
                boolean ignoreLeaves = false;
                table = identifier(true);
                if (lexer.token() == Token.KW_PARTITION) {
                    lexer.nextToken();
                    match(Token.PUNC_LEFT_PAREN);
                    if (lexer.token() == Token.KW_ALL) {
                        partitionAll = true;
                        lexer.nextToken();
                    } else {
                        partitions = new ArrayList<>();
                        partitions.add(identifier(false));
                        while (lexer.token() == Token.PUNC_COMMA) {
                            lexer.nextToken();
                            partitions.add(identifier(false));
                        }
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                if (lexer.token() == Token.KW_INDEX || lexer.token() == Token.KW_KEY) {
                    lexer.nextToken();
                    indexs = new ArrayList<>();
                    match(Token.PUNC_LEFT_PAREN);
                    indexs.add(identifier(false));
                    while (lexer.token() == Token.PUNC_COMMA) {
                        lexer.nextToken();
                        indexs.add(identifier(false));
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                if (lexer.token() == Token.KW_IGNORE) {
                    lexer.nextToken();
                    matchKeywords(Keywords.LEAVES);
                    ignoreLeaves = true;
                }
                DALLoadIndexIntoCacheStatement.TableIndexList tableIndexList =
                    loadIndexIntoCacheStatement.new TableIndexList(table, partitionAll,
                        partitions, indexs, ignoreLeaves);
                tableIndexLists.add(tableIndexList);
            } while (lexer.token() == Token.PUNC_COMMA);
            return new DALLoadIndexIntoCacheStatement(tableIndexLists);
        }
        throw new SQLSyntaxErrorException("expect DATA | XML | INDEX");
    }

    // protected

    protected DMLSelectStatement selectPrimary() throws SQLSyntaxErrorException {
        switch (lexer.token()) {
            case Token.KW_SELECT:
                return select();
            case Token.PUNC_LEFT_PAREN:
                lexer.nextToken();
                WithClause with = null;
                if (lexer.token() == Token.KW_WITH) {
                    with = withClause();
                }
                if (lexer.token() == Token.KW_SELECT) {
                    DMLSelectStatement select = selectPrimary();
                    select.setInParentheses(true);
                    match(Token.PUNC_RIGHT_PAREN);
                    if (lexer.token() == Token.KW_ORDER) {
                        select.setOuterOrderBy(orderBy());
                    }
                    if (lexer.token() == Token.KW_LIMIT) {
                        select.setOuterLimit(limit(false));
                    }
                    select.setWithClause(with);
                    return select;
                }
            default:
                throw new SQLSyntaxErrorException("unexpected token: " + (lexer.token() == 0 ?
                    (lexer.parseKeyword() == 0 ? lexer.stringValue() : Keywords.getInfo(lexer.token())) :
                    Token.getInfo(lexer.token())));
        }
    }

    protected Limit limit(boolean withoutOffset) throws SQLSyntaxErrorException {
        if (lexer.token() != Token.KW_LIMIT) {
            return null;
        }
        Number num1;
        switch (lexer.nextToken()) {
            case Token.LITERAL_NUM_PURE_DIGIT:
                num1 = lexer.integerValue();
                if (withoutOffset) {
                    lexer.nextToken();
                    return new Limit(0, num1.longValue(), true);
                }
                switch (lexer.nextToken()) {
                    case Token.PUNC_COMMA:
                        switch (lexer.nextToken()) {
                            case Token.LITERAL_NUM_PURE_DIGIT:
                                Number num2 = lexer.integerValue();
                                lexer.nextToken();
                                return new Limit(num1.longValue(), num2.longValue(), false);
                            // case Token.QUESTION_MARK:
                            // paramIndex1 = lexer.paramIndex();
                            // lexer.nextToken();
                            // return new Limit(num1.longValue(), new ParamMarker(paramIndex1),
                            // false);
                            default:
                                throw new SQLSyntaxErrorException("expect digit after , for limit");
                        }
                    case Token.IDENTIFIER:
                        if (equalsKeyword(Keywords.OFFSET)) {
                            switch (lexer.nextToken()) {
                                case Token.LITERAL_NUM_PURE_DIGIT:
                                    Number num2 = lexer.integerValue();
                                    lexer.nextToken();
                                    return new Limit(num2.longValue(), num1.longValue(), false);
                                // case Token.QUESTION_MARK:
                                // paramIndex1 = lexer.paramIndex();
                                // lexer.nextToken();
                                // return new Limit(new ParamMarker(paramIndex1), num1.longValue(),
                                // false);
                                default:
                                    // throw new SQLSyntaxErrorException("expect digit or ? after , for limit");
                                    throw new SQLSyntaxErrorException("expect digit after , for limit");
                            }
                        }
                }
                return new Limit(0, num1.longValue(), true);
            // case Token.QUESTION_MARK:
            // paramIndex1 = lexer.paramIndex();
            // if (withoutOffset) {
            // lexer.nextToken();
            // return new Limit(0, new ParamMarker(paramIndex1), true);
            // }
            // switch (lexer.nextToken()) {
            // case Token.PUNC_COMMA:
            // switch (lexer.nextToken()) {
            // case Token.LITERAL_NUM_PURE_DIGIT:
            // num1 = lexer.integerValue();
            // lexer.nextToken();
            // return new Limit(new ParamMarker(paramIndex1), num1.longValue(),
            // false);
            // case Token.QUESTION_MARK:
            // paramIndex2 = lexer.paramIndex();
            // lexer.nextToken();
            // return new Limit(new ParamMarker(paramIndex1),
            // new ParamMarker(paramIndex2));
            // default:
            // throw new SQLSyntaxErrorException("expect digit or ? after , for limit");
            // }
            // case Token.IDENTIFIER:
            // if (equalsKeyword(Keywords.OFFSET)) {
            // switch (lexer.nextToken()) {
            // case Token.LITERAL_NUM_PURE_DIGIT:
            // num1 = lexer.integerValue();
            // lexer.nextToken();
            // return new Limit(num1.longValue(), new ParamMarker(paramIndex1),
            // false);
            // case Token.QUESTION_MARK:
            // paramIndex2 = lexer.paramIndex();
            // lexer.nextToken();
            // return new Limit(new ParamMarker(paramIndex2),
            // new ParamMarker(paramIndex1));
            // default:
            // throw new SQLSyntaxErrorException("expect digit or ? after , for limit");
            // }
            // }
            // }
            // return new Limit(0, new ParamMarker(paramIndex1), true);
            default:
                throw new SQLSyntaxErrorException("expect digit or ? after limit");
        }
    }

    protected OrderBy orderBy() throws SQLSyntaxErrorException {
        if (lexer.token() != Token.KW_ORDER) {
            return null;
        }
        lexer.addParseInfo(ParseInfo.AGGREGATION);
        lexer.nextToken();
        match(Token.KW_BY);
        Expression expr = exprParser.expression();
        boolean asc = true;
        OrderBy orderBy;
        switch (lexer.token()) {
            case Token.KW_DESC:
                asc = false;
            case Token.KW_ASC:
                if (lexer.nextToken() != Token.PUNC_COMMA) {
                    return new OrderBy(expr, asc);
                }
            case Token.PUNC_COMMA:
                orderBy = new OrderBy();
                orderBy.addOrderByItem(expr, asc);
                break;
            default:
                return new OrderBy(expr, asc);
        }
        for (; lexer.token() == Token.PUNC_COMMA; ) {
            lexer.nextToken();
            asc = true;
            expr = exprParser.expression();
            switch (lexer.token()) {
                case Token.KW_DESC:
                    asc = false;
                case Token.KW_ASC:
                    lexer.nextToken();
            }
            orderBy.addOrderByItem(expr, asc);
        }
        return orderBy;
    }

    protected GroupBy groupBy() throws SQLSyntaxErrorException {
        if (lexer.token() != Token.KW_GROUP) {
            return null;
        }
        lexer.addParseInfo(ParseInfo.AGGREGATION);
        lexer.nextToken();
        match(Token.KW_BY);
        Expression expr = exprParser.expression();
        boolean isAsc = true;
        GroupBy groupBy;
        switch (lexer.token()) {
            case Token.KW_DESC:
                isAsc = false;
            case Token.KW_ASC:
                lexer.nextToken();
            default:
                break;
        }
        switch (lexer.token()) {
            case Token.KW_WITH:
                lexer.nextToken();
                matchKeywords(Keywords.ROLLUP);
                return new GroupBy(expr, isAsc, true);
            case Token.PUNC_COMMA:
                break;
            default:
                return new GroupBy(expr, isAsc, false);
        }
        for (groupBy = new GroupBy().addOrderByItem(expr, isAsc); lexer.token() == Token.PUNC_COMMA; ) {
            lexer.nextToken();
            isAsc = true;
            expr = exprParser.expression();
            switch (lexer.token()) {
                case Token.KW_DESC:
                    isAsc = false;
                case Token.KW_ASC:
                    lexer.nextToken();
                default:
                    break;
            }
            groupBy.addOrderByItem(expr, isAsc);
            if (lexer.token() == Token.KW_WITH) {
                lexer.nextToken();
                matchKeywords(Keywords.ROLLUP);
                return groupBy.setWithRollup();
            }
        }
        return groupBy;
    }

    protected DMLQueryStatement buildUnionSelect(DMLSelectStatement select, boolean isSubQuery)
        throws SQLSyntaxErrorException {
        if (lexer.token() != Token.KW_UNION) {
            if (isSubQuery) {
                select.setInParentheses(true);
            }
            return select;
        }
        lexer.addParseInfo(ParseInfo.UNION);
        boolean hasParenByFirst = select.isInParentheses();
        if (select.getOrderBy() != null && !hasParenByFirst) {
            throw new SQLSyntaxErrorException("Incorrect usage of UNION and ORDER BY");
        }
        boolean existOrderOrLimit = false;
        DMLSelectUnionStatement union = new DMLSelectUnionStatement(select);
        for (; lexer.token() == Token.KW_UNION; ) {
            lexer.nextToken();
            boolean isAll = false;
            switch (lexer.token()) {
                case Token.KW_ALL:
                    isAll = true;
                case Token.KW_DISTINCT:
                    lexer.nextToken();
                    break;
            }
            select = selectPrimary();
            if (hasParenByFirst && !select.isInParentheses()) { // 第一个有括号 后续没括号则抛该错
                throw new SQLSyntaxErrorException("unexpected token " + lexer.token());
            }
            if (existOrderOrLimit) {
                throw new SQLSyntaxErrorException("Incorrect usage of UNION and ORDER BY");
            }
            if ((select.getOrderBy() != null || select.getLimit() != null) && !select
                .isInParentheses()) {// 允许最后一个order by不在括号中
                if (!existOrderOrLimit) {
                    existOrderOrLimit = true;
                }
            }
            if ((select.getOuterOrderBy() != null || select.getOuterLimit() != null) && select.isInParentheses()) {
                if (!existOrderOrLimit) {
                    existOrderOrLimit = true;
                }
            }
            union.addSelect(select, isAll);
        }
        if (existOrderOrLimit) {
            int index = union.getSelects().size() - 1;
            DMLSelectStatement last = union.getSelects().get(index);
            if (last.isInParentheses()) {
                union.setOrderBy(last.getOuterOrderBy());
                union.setLimit(last.getOuterLimit());
                DMLSelectStatement newStmt =
                    new DMLSelectStatement(last.getOption(), last.getSelectExprList(), last.getTables(),
                        last.getWhere(), last.getGroupBy(), last.getHaving(), last.getWindows(), last.getOrderBy(),
                        last.getLimit(), last.getOutFile(), last.getLock());
                newStmt.setInParentheses(true);
                union.getSelects().set(index, newStmt);
            } else {
                union.setOrderBy(last.getOrderBy());
                union.setLimit(last.getLimit());
                DMLSelectStatement newStmt =
                    new DMLSelectStatement(last.getOption(), last.getSelectExprList(), last.getTables(),
                        last.getWhere(), last.getGroupBy(), last.getHaving(), last.getWindows(), null, null,
                        last.getOutFile(), last.getLock());
                newStmt.setInParentheses(false);
                union.getSelects().set(index, newStmt);
            }
        } else {
            if (lexer.token() == Token.KW_ORDER) {
                union.setOrderBy(orderBy());
            }
            if (lexer.token() == Token.KW_LIMIT) {
                union.setLimit(limit(false));
            }
        }
        return union;
    }

    protected Tables tableRefs() throws SQLSyntaxErrorException {
        Tables tables = buildTableReferences(tableReference());
        if (tables.isSingleTable()) {
            lexer.addParseInfo(ParseInfo.SINGLE_TABLE);
        }
        return tables;
    }

    protected String alias() throws SQLSyntaxErrorException {
        if (lexer.token() == Token.KW_AS) {
            lexer.nextToken();
        }
        if (lexer.token() == Token.LITERAL_CHARS) {
            long info = lexer.tokenInfo();
            lexer.nextToken();
            return lexer.stringValue(info);
        } else if (lexer.token() == Token.IDENTIFIER) {
            int start = lexer.getOffset();
            int size = lexer.getSize();
            byte firstByte = lexer.getSQL()[start];
            lexer.nextToken();
            if (lexer.token() == Token.LITERAL_CHARS && firstByte == '_') {
                size += lexer.getSize();
                lexer.nextToken();
            }
            return lexer.stringValue(((long)start << 32) | ((((long)size << 32) >> 32)));
        }
        return null;
    }

    protected String as() throws SQLSyntaxErrorException {
        if (lexer.token() == Token.KW_AS) {
            lexer.nextToken();
        }
        if (lexer.token() == Token.IDENTIFIER) {
            String alia = lexer.stringValue();
            lexer.nextToken();
            if (lexer.token() == Token.LITERAL_CHARS && alia.charAt(0) == '_') {
                StringBuilder alias = new StringBuilder();
                alias.append(alia);
                alias.append(lexer.stringValue());
                lexer.nextToken();
                return alias.toString();
            }
            return alia;
        } else if (lexer.token() == Token.LITERAL_CHARS) {
            String alia = lexer.stringValue();
            lexer.nextToken();
            return alia;
        }
        return null;
    }

    protected List<Identifier> idList(boolean isTable) throws SQLSyntaxErrorException {
        return buildIdList(identifier(isTable), isTable);
    }

    protected List<Identifier> buildIdList(Identifier id, boolean isTable) throws SQLSyntaxErrorException {
        if (lexer.token() != Token.PUNC_COMMA) {
            List<Identifier> list = new ArrayList<Identifier>(1);
            list.add(id);
            return list;
        }
        List<Identifier> list = new LinkedList<Identifier>();
        list.add(id);
        for (; lexer.token() == Token.PUNC_COMMA; ) {
            lexer.nextToken();
            id = identifier(isTable);
            list.add(id);
        }
        return list;
    }

    protected List<Identifier> idNameList() throws SQLSyntaxErrorException {
        List<Identifier> list = new ArrayList<Identifier>();
        if (lexer.token() == Token.PUNC_RIGHT_PAREN) {
            lexer.nextToken();
            return list;
        }
        list.add(identifier());
        if (lexer.token() == Token.PUNC_COMMA) {
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                list.add(identifier());
            }
        }
        match(Token.PUNC_RIGHT_PAREN);
        return list;
    }

    protected List<RowExpression> rowList() throws SQLSyntaxErrorException {
        List<RowExpression> valuesList;
        List<Expression> tempRowValue = rowValue();
        if (lexer.token() == Token.PUNC_COMMA) {
            valuesList = new LinkedList<RowExpression>();
            valuesList.add(new RowExpression(tempRowValue));
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                tempRowValue = rowValue();
                valuesList.add(new RowExpression(tempRowValue));
            }
        } else {
            valuesList = new ArrayList<RowExpression>(1);
            valuesList.add(new RowExpression(tempRowValue));
        }
        return valuesList;
    }

    private WithClause withClause() throws SQLSyntaxErrorException {
        match(Token.KW_WITH);
        boolean isRecursive = false;
        if (lexer.token() == Token.KW_RECURSIVE) {
            lexer.nextToken();
            isRecursive = true;
        }
        List<CTE> ctes = new ArrayList<>();
        CTE cte = parseCET();
        ctes.add(cte);
        for (; lexer.token() == Token.PUNC_COMMA; ) {
            lexer.nextToken();
            cte = parseCET();
            ctes.add(cte);
        }
        return new WithClause(isRecursive, ctes);
    }

    // private
    private CTE parseCET() throws SQLSyntaxErrorException {
        CTE cte = new CTE();
        cte.name = identifier();
        if (lexer.token() == Token.PUNC_LEFT_PAREN) {
            lexer.nextToken();
            cte.columns = new ArrayList<>();
            Identifier column = identifier();
            cte.columns.add(column);
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                column = identifier();
                cte.columns.add(column);
            }
            match(Token.PUNC_RIGHT_PAREN);
        }
        match(Token.KW_AS);
        match(Token.PUNC_LEFT_PAREN);
        cte.subquery = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(true);
        cte.subquery.setInParentheses(true);
        match(Token.PUNC_RIGHT_PAREN);
        return cte;
    }

    private Tables buildTableReferences(TableReference ref) throws SQLSyntaxErrorException {
        List<TableReference> list;
        if (lexer.token() == Token.PUNC_COMMA) {
            list = new LinkedList<TableReference>();
            list.add(ref);
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                ref = tableReference();
                lexer.addParseInfo(ParseInfo.JOIN);
                list.add(ref);
            }
        } else {
            list = new ArrayList<TableReference>(1);
            list.add(ref);
        }
        return new Tables(list);
    }

    private TableReference buildTableReference(TableReference ref) throws SQLSyntaxErrorException {
        for (; ; ) {
            Expression on;
            List<Identifier> using;
            TableReference temp;
            boolean isOut = false;
            boolean isLeft = true;
            switch (lexer.token()) {
                case Token.KW_INNER:
                case Token.KW_CROSS:
                    lexer.nextToken();
                case Token.KW_JOIN:
                    lexer.addParseInfo(ParseInfo.JOIN);
                    lexer.nextToken();
                    temp = tableFactor();
                    switch (lexer.token()) {
                        case Token.KW_ON:
                            lexer.nextToken();
                            on = exprParser.expression();
                            ref = new Join(Join.INNER, ref, temp, on, null, false, false);
                            break;
                        case Token.KW_USING:
                            lexer.nextToken();
                            match(Token.PUNC_LEFT_PAREN);
                            using = idNameList();
                            ref = new Join(Join.INNER, ref, temp, null, using, false, false);
                            break;
                        default:
                            ref = new Join(Join.INNER, ref, temp, null, null, false, false);
                            break;
                    }
                    break;
                case Token.KW_STRAIGHT_JOIN:
                    lexer.addParseInfo(ParseInfo.JOIN);
                    lexer.nextToken();
                    temp = tableFactor();
                    switch (lexer.token()) {
                        case Token.KW_ON:
                            lexer.nextToken();
                            on = exprParser.expression();
                            ref = new Join(Join.STRAIGHT, ref, temp, on, null, false, false);
                            break;
                        default:
                            ref = new Join(Join.STRAIGHT, ref, temp, null, null, false, false);
                            break;
                    }
                    break;
                case Token.KW_RIGHT:
                    isLeft = false;
                case Token.KW_LEFT:
                    lexer.addParseInfo(ParseInfo.JOIN);
                    lexer.nextToken();
                    if (lexer.token() == Token.KW_OUTER) {
                        lexer.nextToken();
                    }
                    match(Token.KW_JOIN);
                    temp = tableReference();
                    switch (lexer.token()) {
                        case Token.KW_ON:
                            lexer.nextToken();
                            on = exprParser.expression();
                            ref = new Join(Join.OUTER, ref, temp, on, null, isLeft, false);
                            break;
                        case Token.KW_USING:
                            lexer.nextToken();
                            match(Token.PUNC_LEFT_PAREN);
                            using = idNameList();
                            ref = new Join(Join.OUTER, ref, temp, null, using, isLeft, false);
                            break;
                        default:
                            Object condition = temp.removeLastConditionElement();
                            if (condition instanceof Expression) {
                                ref = new Join(Join.OUTER, ref, temp, (Expression)condition, null, isLeft, false);
                            } else if (condition instanceof List) {
                                ref = new Join(Join.OUTER, ref, temp, null, (List<Identifier>)condition, isLeft, false);
                            } else {
                                throw new SQLSyntaxErrorException("conditionExpr cannot be null for outer join");
                            }
                            break;
                    }
                    break;
                case Token.KW_NATURAL:
                    lexer.addParseInfo(ParseInfo.JOIN);
                    lexer.nextToken();
                    switch (lexer.token()) {
                        case Token.KW_RIGHT:
                            isLeft = false;
                        case Token.KW_LEFT:
                            lexer.nextToken();
                            if (lexer.token() == Token.KW_OUTER) {
                                lexer.nextToken();
                            }
                            isOut = true;
                        case Token.KW_JOIN:
                            lexer.nextToken();
                            temp = tableFactor();
                            ref = new Join(Join.NATURAL, ref, temp, null, null, isLeft, isOut);
                            break;
                        default:
                            throw new SQLSyntaxErrorException(
                                "unexpected token after NATURAL for natural join:" + lexer.token());
                    }
                    break;
                default:
                    return ref;
            }
        }
    }

    private TableReference tableReference() throws SQLSyntaxErrorException {
        TableReference ref = tableFactor();
        return buildTableReference(ref);
    }

    private TableReference tableFactor() throws SQLSyntaxErrorException {
        String alias = null;
        switch (lexer.token()) {
            case Token.PUNC_LEFT_PAREN:
                lexer.nextToken();
                Object ref = trsOrQuery();
                match(Token.PUNC_RIGHT_PAREN);
                if (ref instanceof QueryExpression) {
                    alias = as();
                    lexer.addParseInfo(ParseInfo.SUBQUERY);
                    return new Subquery((QueryExpression)ref, alias);
                }
                return (Tables)ref;
            case Token.IDENTIFIER: {
                Identifier table = identifier(true);
                List<Identifier> partition = null;
                if (lexer.token() == Token.KW_PARTITION) {
                    partition = new ArrayList<>();
                    lexer.nextToken();
                    match(Token.PUNC_LEFT_PAREN);
                    partition.add(identifier());
                    while (lexer.token() != Token.EOF) {
                        if (lexer.token() == Token.PUNC_COMMA) {
                            lexer.nextToken();
                            partition.add(identifier());
                            continue;
                        }
                        break;
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                alias = as();
                List<IndexHint> hintList = hintList();
                return new Table(table, alias, hintList, partition);
            }
            case Token.QUESTION_MARK: {
                lexer.nextToken();
                List<Identifier> partition = null;
                if (lexer.token() == Token.KW_PARTITION) {
                    partition = new ArrayList<>();
                    lexer.nextToken();
                    match(Token.PUNC_LEFT_PAREN);
                    partition.add(identifier());
                    while (lexer.token() != Token.EOF) {
                        if (lexer.token() == Token.PUNC_COMMA) {
                            lexer.nextToken();
                            partition.add(identifier());
                            continue;
                        }
                        break;
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                alias = as();
                List<IndexHint> hintList = hintList();
                return new Table(new ParamMarker(lexer.paramIndex()), alias, hintList, partition);
            }
            default:
                throw new SQLSyntaxErrorException("unexpected token for tableFactor: " + lexer.token());
        }
    }

    private Object trsOrQuery() throws SQLSyntaxErrorException {
        Object ref;
        switch (lexer.token()) {
            case Token.KW_SELECT:
                DMLSelectStatement select = selectPrimary();
                return buildUnionSelect(select, false);
            case Token.PUNC_LEFT_PAREN:
                lexer.nextToken();
                ref = trsOrQuery();
                match(Token.PUNC_RIGHT_PAREN);
                if (ref instanceof QueryExpression) {
                    if (ref instanceof DMLSelectStatement) {
                        DMLSelectStatement selectAst = (DMLSelectStatement)ref;
                        selectAst.setInParentheses(true);
                        QueryExpression rst = buildUnionSelect(selectAst, false);
                        if (rst != ref) {
                            return rst;
                        }
                    }
                    String alias = as();
                    if (alias != null) {
                        ref = new Subquery((QueryExpression)ref, alias);
                    } else {
                        return ref;
                    }
                }
                // ---- build factor complete---------------
                ref = buildTableReference((TableReference)ref);
                // ---- build ref complete---------------
                break;
            default:
                ref = tableReference();
        }

        List<TableReference> list;
        if (lexer.token() == Token.PUNC_COMMA) {
            list = new LinkedList<TableReference>();
            list.add((TableReference)ref);
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                ref = tableReference();
                list.add((TableReference)ref);
            }
            return new Tables(list);
        }
        list = new ArrayList<TableReference>(1);
        list.add((TableReference)ref);
        return new Tables(list);
    }

    private List<Pair<Identifier, Expression>> onDuplicateUpdate() throws SQLSyntaxErrorException {
        if (lexer.token() != Token.KW_ON) {
            return null;
        }
        lexer.nextToken();
        matchKeywords(Keywords.DUPLICATE);
        match(Token.KW_KEY);
        match(Token.KW_UPDATE);
        List<Pair<Identifier, Expression>> list;
        Identifier col = identifier();
        match(Token.OP_EQUALS, Token.OP_ASSIGN);
        Expression expr = exprParser.expression();
        if (lexer.token() == Token.PUNC_COMMA) {
            list = new LinkedList<Pair<Identifier, Expression>>();
            list.add(new Pair<Identifier, Expression>(col, expr));
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                col = identifier();
                match(Token.OP_EQUALS, Token.OP_ASSIGN);
                expr = exprParser.expression();
                list.add(new Pair<Identifier, Expression>(col, expr));
            }
            return list;
        }
        list = new ArrayList<Pair<Identifier, Expression>>(1);
        list.add(new Pair<Identifier, Expression>(col, expr));
        return list;
    }

    private List<IndexHint> hintList() throws SQLSyntaxErrorException {
        IndexHint hint = hint();
        if (hint == null)
            return null;
        List<IndexHint> list;
        IndexHint hint2 = hint();
        if (hint2 == null) {
            list = new ArrayList<IndexHint>(1);
            list.add(hint);
            return list;
        }
        list = new LinkedList<IndexHint>();
        list.add(hint);
        list.add(hint2);
        for (; (hint2 = hint()) != null; list.add(hint2))
            ;
        return list;
    }

    private IndexHint hint() throws SQLSyntaxErrorException {
        Integer action = null;
        switch (lexer.token()) {
            case Token.KW_USE:
                action = IndexHint.ACTION_USE;
                break;
            case Token.KW_IGNORE:
                action = IndexHint.ACTION_IGNORE;
                break;
            case Token.KW_FORCE:
                action = IndexHint.ACTION_FORCE;
                break;
            default:
                return null;
        }
        boolean isIndex = false;
        switch (lexer.nextToken()) {
            case Token.KW_INDEX:
                isIndex = true;
                break;
            case Token.KW_KEY:
                isIndex = false;
                break;
            default:
                throw new SQLSyntaxErrorException("must be INDEX or KEY for hint type, not " + lexer.token());
        }
        Integer scope = null;
        if (lexer.nextToken() == Token.KW_FOR) {
            switch (lexer.nextToken()) {
                case Token.KW_JOIN:
                    lexer.nextToken();
                    scope = IndexHint.SCOPE_JOIN;
                    break;
                case Token.KW_ORDER:
                    lexer.nextToken();
                    match(Token.KW_BY);
                    scope = IndexHint.SCOPE_ORDER_BY;
                    break;
                case Token.KW_GROUP:
                    lexer.nextToken();
                    match(Token.KW_BY);
                    scope = IndexHint.SCOPE_GROUP_BY;
                    break;
                default:
                    throw new SQLSyntaxErrorException(
                        "must be JOIN or ORDER or GROUP for hint scope, not " + lexer.token());
            }
        }

        match(Token.PUNC_LEFT_PAREN);
        List<Identifier> indexList = idNameList();
        return new IndexHint(action, isIndex, scope, indexList);
    }

    private List<Expression> rowValue() throws SQLSyntaxErrorException {
        match(Token.PUNC_LEFT_PAREN);
        if (lexer.token() == Token.PUNC_RIGHT_PAREN) {
            lexer.nextToken();
            return Collections.emptyList();
        }
        List<Expression> row;
        Expression expr = exprParser.expression();
        if (lexer.token() == Token.PUNC_COMMA) {
            row = new LinkedList<Expression>();
            row.add(expr);
            for (; lexer.token() == Token.PUNC_COMMA; ) {
                lexer.nextToken();
                expr = exprParser.expression();
                row.add(expr);
            }
        } else {
            row = new ArrayList<Expression>(1);
            row.add(expr);
        }
        match(Token.PUNC_RIGHT_PAREN);
        return row;
    }

    private int getHandlerOrder() throws SQLSyntaxErrorException {
        switch (lexer.parseKeyword()) {
            case Keywords.FIRST:
                lexer.nextToken();
                return DMLHandlerStatement.FIRST;
            case Keywords.NEXT:
                lexer.nextToken();
                return DMLHandlerStatement.NEXT;
            case Keywords.PREV:
                lexer.nextToken();
                return DMLHandlerStatement.PREV;
            case Keywords.LAST:
                lexer.nextToken();
                return DMLHandlerStatement.LAST;
        }
        return 0;
    }

    private ComparisionExpression loadComparisionExpression() throws SQLSyntaxErrorException {
        Identifier column = identifier();
        match(Token.OP_EQUALS);
        Expression right = exprParser.expression();
        return new ComparisionExpression(column, right, ComparisionExpression.EQUALS);
    }

}
