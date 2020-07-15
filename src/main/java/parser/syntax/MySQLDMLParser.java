package parser.syntax;

import parser.ParseInfo;
import parser.ast.expression.Expression;
import parser.ast.expression.QueryExpression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.ParamMarker;
import parser.ast.expression.primary.WithClause;
import parser.ast.expression.primary.WithClause.CTE;
import parser.ast.fragment.GroupBy;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.tableref.*;
import parser.ast.stmt.dml.DMLQueryStatement;
import parser.ast.stmt.dml.DMLSelectStatement;
import parser.ast.stmt.dml.DMLSelectUnionStatement;
import parser.lexer.Lexer;
import parser.token.Keywords;
import parser.token.Token;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
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

    public DMLSelectStatement select() throws SQLSyntaxErrorException {
        MySQLDMLSelectParser parser = new MySQLDMLSelectParser(lexer, exprParser);
        return parser.select();
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

    protected Tables tableRefs() throws SQLSyntaxErrorException {
        Tables tables = buildTableReferences(tableReference());
        if (tables.isSingleTable()) {
            lexer.addParseInfo(ParseInfo.SINGLE_TABLE);
        }
        return tables;
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

}
