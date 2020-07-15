package parser.visitor;

import parser.ast.AST;
import parser.ast.expression.*;
import parser.ast.expression.primary.*;
import parser.ast.expression.primary.WithClause.CTE;
import parser.ast.expression.primary.literal.*;
import parser.ast.fragment.GroupBy;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.SubpartitionDefinition;
import parser.ast.fragment.ddl.*;
import parser.ast.fragment.tableref.*;
import parser.ast.stmt.dml.DMLSelectStatement;
import parser.ast.stmt.dml.DMLSelectStatement.SelectOption;
import parser.ast.stmt.dml.DMLSelectStatement.OutFile;
import parser.ast.stmt.dml.DMLSelectStatement.LockMode;
import parser.ast.stmt.dml.DMLSelectUnionStatement;
import parser.token.Functions;
import parser.token.IntervalUnit;
import parser.token.Keywords;
import parser.token.Token;
import parser.util.BytesUtil;
import parser.util.Pair;
import parser.util.Tuple2;
import parser.util.Tuple3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class OutputVisitor implements Visitor {

    private byte[] sql;
    protected final BytesBuilder appendable;
    protected Object[] params; // 替换占位符?的实际参数
    protected int paramStart = 0;
    protected List<Tuple2<Integer, Integer>> paramIndexs; // 由占位符?分割的格式化后的SQL段<位置,长度>
    protected long DNID = 0;
    protected List<Tuple2<String, String>> needSelects;
    protected boolean ignoreLimit;

    public OutputVisitor(byte[] sql) {
        this.sql = sql;
        this.appendable = new BytesBuilder(sql.length);
    }

    public OutputVisitor(byte[] sql, Object... params) {
        this(sql);
        this.params = params;
    }

    public byte[] getData() {
        return appendable.getDataAndFreeMemory();
    }

    public List<Tuple2<Integer, Integer>> getParamIndexs() {
        if (paramStart < appendable.getIndex()) {
            paramIndexs.add(new Tuple2<Integer, Integer>(paramStart, appendable.getIndex() - paramStart));
        }
        return paramIndexs;
    }

    public void setInfo(List<Tuple2<String, String>> neededSelects, boolean ignoreLimit) {
        this.needSelects = neededSelects;
        this.ignoreLimit = ignoreLimit;
    }

    private static byte[] t(int token) {
        return Token.getBytes(token);
    }

    private static byte[] k(int token) {
        return Keywords.getBytes(token);
    }

    @SuppressWarnings("rawtypes")
    protected void print(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof AST) {
            ((AST)obj).accept(this);
        } else if (obj instanceof Collection) {
            for (Object o : (Collection)obj) {
                print(o);
            }
        } else if (obj instanceof Pair) {
            print(((Pair)obj).getKey());
            print(((Pair)obj).getValue());
        } else {
            appendable.append(String.valueOf(obj));
        }
    }

    protected void printList(List<? extends AST> list) {
        printList(list, ',');
    }

    private void printList(List<? extends AST> list, char sep) {
        if (list == null) {
            return;
        }
        boolean isFst = true;
        for (AST arg : list) {
            if (isFst) {
                isFst = false;
            } else {
                appendable.append(sep);
            }
            arg.accept(this);
        }
    }

    public void printSelectList(List<Pair<Expression, String>> list) {
        if (list == null) {
            return;
        }
        boolean isFst = true;
        for (Pair<Expression, String> p : list) {
            if (isFst) {
                isFst = false;
            } else {
                appendable.append(',');
            }
            Expression exp = p.getKey();
            String alias = p.getValue();
            print(exp);
            if (alias != null) {
                appendable.append(t(Token.KW_AS), 0);
                appendable.append(alias);
            }
        }
        appendable.append(' ');
    }

    private void printFrameBound(Pair<Integer, Expression> bound) {
        switch (bound.getKey()) {
            case WindowClause.FRAM_CURRENT_ROW:
                appendable.append(k(Keywords.CURRENT), 2).append(t(Token.KW_ROW));
                break;
            case WindowClause.FRAM_UNBOUNDED_PRECEDING:
                appendable.append(k(Keywords.UNBOUNDED), 2).append(k(Keywords.PRECEDING));
                break;
            case WindowClause.FRAM_UNBOUNDED_FOLLOWING:
                appendable.append(k(Keywords.UNBOUNDED), 2).append(k(Keywords.FOLLOWING));
                break;
            case WindowClause.FRAM_PRECEDING:
                print(bound.getValue());
                appendable.append(k(Keywords.PRECEDING), 1);
                break;
            case WindowClause.FRAM_FOLLOWING:
                print(bound.getValue());
                appendable.append(k(Keywords.FOLLOWING), 1);
                break;
        }
    }

    @Override
    public void visit(DMLSelectStatement node) {
        print(node.getWithClause());
        if (node.isInParentheses()) {
            appendable.append('(');
        }
        appendable.append(t(Token.KW_SELECT), 2);
        print(node.getOption());
        boolean isFst = true;
        List<Pair<Expression, String>> exprList = node.getSelectExprList();
        for (Pair<Expression, String> p : exprList) {
            if (isFst) {
                isFst = false;
            } else {
                appendable.append(',');
            }
            Expression exp = p.getKey();
            String alias = p.getValue();
            print(exp);
            if (alias != null) {
                appendable.append(t(Token.KW_AS), 0);
                appendable.append(alias);
            }
        }
        if (needSelects != null) {
            for (Tuple2<String, String> tuple : needSelects) {
                appendable.append(',');
                appendable.append(tuple._1());
                if (tuple._2() != null) {
                    appendable.append(' ').append(tuple._2());
                }
            }
        }
        OutFile outFile = node.getOutFile();
        if (outFile != null) {
            appendable.append(t(Token.KW_INTO), 0);
            print(outFile);
        }
        Tables from = node.getTables();
        if (from != null) {
            appendable.append(t(Token.KW_FROM), 0);
            print(from);
        }
        Expression where = node.getWhere();
        if (where != null) {
            appendable.append(t(Token.KW_WHERE), 0);
            print(where);
            DNID = 0;
        }
        GroupBy group = node.getGroupBy();
        if (group != null) {
            appendable.append(' ');
            print(group);
        }
        Expression having = node.getHaving();
        if (having != null) {
            appendable.append(t(Token.KW_HAVING), 0);
            print(having);
        }
        List<Pair<Identifier, WindowClause>> windows = node.getWindows();
        if (windows != null) {
            appendable.append(t(Token.KW_WINDOW), 0);
            isFst = true;
            for (Pair<Identifier, WindowClause> pair : windows) {
                if (isFst) {
                    isFst = false;
                } else {
                    appendable.append(',');
                }
                print(pair.getKey());
                appendable.append(t(Token.KW_AS), 0).append('(');
                print(pair.getValue());
                appendable.append(')');
            }
        }
        OrderBy order = node.getOrderBy();
        if (order != null) {
            appendable.append(' ');
            print(order);
        }
        if (!this.ignoreLimit) {
            Limit limit = node.getLimit();
            if (limit != null) {
                appendable.append(' ');
                print(limit);
            }
        }
        print(node.getLock());
        if (node.isInParentheses()) {
            appendable.append(')');
        }
    }

    @Override
    public void visit(SelectOption node) {
        switch (node.dupStrategy) {
            case SelectOption.DUP_STRATEGY_ALL:
                break;
            case SelectOption.DUP_STRATEGY_DISTINCT:
                appendable.append(t(Token.KW_DISTINCT), 2);
                break;
            case SelectOption.DUP_STRATEGY_DISTINCTROW:
                appendable.append(t(Token.KW_DISTINCTROW), 2);
                break;
        }
        if (node.highPriority) {
            appendable.append(t(Token.KW_HIGH_PRIORITY), 2);
        }
        if (node.straightJoin) {
            appendable.append(t(Token.KW_STRAIGHT_JOIN), 2);
        }
        switch (node.resultSize) {
            case SelectOption.SQL_BIG_RESULT:
                appendable.append(t(Token.KW_SQL_BIG_RESULT), 2);
                break;
            case SelectOption.SQL_SMALL_RESULT:
                appendable.append(t(Token.KW_SQL_SMALL_RESULT), 2);
                break;
        }
        if (node.sqlBufferResult) {
            appendable.append(k(Keywords.SQL_BUFFER_RESULT), 2);
        }
        if (node.sqlCache != null) {
            switch (node.sqlCache) {
                case SelectOption.CACHE_STRATEGY_SQL_CACHE:
                    appendable.append(k(Keywords.SQL_CACHE), 2);
                    break;
                case SelectOption.CACHE_STRATEGY_SQL_NO_CACHE:
                    appendable.append(k(Keywords.SQL_NO_CACHE), 2);
                    break;
            }
        }
        if (node.sqlCalcFoundRows) {
            appendable.append(t(Token.KW_SQL_CALC_FOUND_ROWS), 2);
        }
    }

    @Override
    public void visit(Identifier node) {
        Expression parent = node.getParent();
        if (parent != null) {
            print(parent);
            appendable.append('.');
        }
        appendable.append(node.getIdText());
    }

    @Override
    public void visit(LiteralBitField node) {
        long introducer = node.getIntroducer();
        if (introducer > 0) {
            appendable.append(sql, introducer).append(' ');
        }
        appendable.append(sql, node.getText());
    }

    @Override
    public void visit(LiteralBoolean node) {
        if (node.isTrue()) {
            appendable.append(t(Token.LITERAL_BOOL_TRUE));
        } else {
            appendable.append(t(Token.LITERAL_BOOL_FALSE));
        }
    }

    @Override
    public void visit(LiteralHexadecimal node) {
        long introducer = node.getIntroducer();
        if (introducer > 0) {
            appendable.append(sql, introducer).append(' ');
        }
        int size = BytesUtil.getSize(node.getValue());
        if (size > 0) {
            appendable.append('0').append('x').append(sql, node.getValue());
        } else {
            appendable.append("x''");
        }
    }

    @Override
    public void visit(LiteralNull node) {
        appendable.append(t(Token.LITERAL_NULL));
    }

    @Override
    public void visit(LiteralNumber node) {
        appendable.append(node.getNumber());
    }

    @Override
    public void visit(LiteralString node) {
        byte[] introducer = node.getIntroducer();
        if (introducer != null) {
            appendable.append(introducer);
        } else if (node.isNchars()) {
            appendable.append('N');
        }
        byte[] bytes = node.getStringBytes();
        if (bytes != null) {
            appendable.append('\'');
            appendable.append(bytes);
            appendable.append('\'');
        } else {
            long str = node.getString();
            appendable.append(sql, str);
        }
    }

    @Override
    public void visit(GroupBy node) {
        appendable.append(t(Token.KW_GROUP)).append(t(Token.KW_BY), 0);
        boolean isFst = true;
        for (Pair<Expression, Boolean> p : node.getOrderByList()) {
            if (isFst) {
                isFst = false;
            } else {
                appendable.append(',');
            }
            print(p.getKey());
            if (!p.getValue()) {
                appendable.append(t(Token.KW_DESC), 1);
            }
        }
        if (node.isWithRollup()) {
            appendable.append(t(Token.KW_WITH), 1).append(k(Keywords.ROLLUP), 1);
        }
    }

    @Override
    public void visit(OrderBy node) {
        appendable.append(t(Token.KW_ORDER)).append(t(Token.KW_BY), 0);
        boolean isFst = true;
        for (Pair<Expression, Boolean> p : node.getOrderByList()) {
            if (isFst) {
                isFst = false;
            } else {
                appendable.append(',');
            }
            print(p.getKey());
            if (!p.getValue()) {
                appendable.append(t(Token.KW_DESC), 1);
            }
        }
    }

    @Override
    public void visit(Limit node) {
        appendable.append(t(Token.KW_LIMIT), 2);
        // ParamMarker offsetMarker = node.getOffsetParam();
        // ParamMarker sizeMarker = node.getSizeParam();
        long offset = node.getOffset();
        long size = node.getSize();
        // if (offsetMarker != null) {
        // print(offsetMarker);
        // appendable.append(',');
        // if (sizeMarker != null) {
        // print(sizeMarker);
        // } else {
        // appendable.append(size);
        // }
        // } else if (sizeMarker != null) {
        // if (offset != 0) {
        // appendable.append(offset).append(',');
        // }
        // print(sizeMarker);
        // } else
        if (offset == 0) {
            appendable.append(size);
        } else {
            appendable.append(offset).append(',').append(size);
        }
    }

    @Override
    public void visit(Dual node) {
        appendable.append(t(Token.KW_DUAL));
    }

    @Override
    public void visit(Join node) {
        switch (node.getType()) {
            case Join.INNER: {
                TableReference left = node.getLeftTable();
                boolean paren = left.getPrecedence() < node.getPrecedence();
                if (paren) {
                    appendable.append('(');
                }
                print(left);
                if (paren) {
                    appendable.append(')');
                }
                appendable.append(t(Token.KW_INNER), 1).append(t(Token.KW_JOIN), 0);
                TableReference right = node.getRightTable();
                paren = right.getPrecedence() <= node.getPrecedence();
                if (paren) {
                    appendable.append('(');
                }
                print(right);
                if (paren) {
                    appendable.append(')');
                }

                Expression on = node.getOnCond();
                List<Identifier> using = node.getUsing();
                if (on != null) {
                    appendable.append(t(Token.KW_ON), 0);
                    print(on);
                } else if (using != null) {
                    appendable.append(t(Token.KW_USING), 0).append('(');
                    boolean isFst = true;
                    for (Identifier col : using) {
                        if (isFst) {
                            isFst = false;
                        } else {
                            appendable.append(',');
                        }
                        print(col);
                    }
                    appendable.append(')');
                }

                break;
            }
            case Join.NATURAL: {
                TableReference left = node.getLeftTable();
                boolean paren = left.getPrecedence() < node.getPrecedence();
                if (paren) {
                    appendable.append('(');
                }
                print(left);
                if (paren) {
                    appendable.append(')');
                }
                appendable.append(t(Token.KW_NATURAL), 0);
                if (node.isOuter()) {
                    if (node.isLeft()) {
                        appendable.append(t(Token.KW_LEFT), 2);
                    } else {
                        appendable.append(t(Token.KW_RIGHT), 2);
                    }
                }
                appendable.append(t(Token.KW_JOIN), 2);
                TableReference right = node.getRightTable();
                paren = right.getPrecedence() <= node.getPrecedence();
                if (paren) {
                    appendable.append('(');
                }
                print(right);
                if (paren) {
                    appendable.append(')');
                }
                break;
            }
            case Join.OUTER: {
                TableReference left = node.getLeftTable();
                boolean paren = left.getPrecedence() < node.getPrecedence();
                if (paren) {
                    appendable.append('(');
                }
                print(left);
                if (paren) {
                    appendable.append(')');
                }
                if (node.isLeft()) {
                    appendable.append(t(Token.KW_LEFT), 1).append(t(Token.KW_JOIN), 0);
                } else {
                    appendable.append(t(Token.KW_RIGHT), 1).append(t(Token.KW_JOIN), 0);
                }

                TableReference right = node.getRightTable();
                paren = right.getPrecedence() <= node.getPrecedence();
                if (paren) {
                    appendable.append('(');
                }
                print(right);
                if (paren) {
                    appendable.append(')');
                }
                Expression on = node.getOnCond();
                List<Identifier> using = node.getUsing();
                if (on != null) {
                    appendable.append(t(Token.KW_ON), 0);
                    print(on);
                } else if (using != null) {
                    appendable.append(t(Token.KW_USING), 0).append('(');
                    boolean isFst = true;
                    for (Identifier col : using) {
                        if (isFst) {
                            isFst = false;
                        } else {
                            appendable.append(',');
                        }
                        print(col);
                    }
                    appendable.append(')');
                }
                break;
            }
            case Join.STRAIGHT: {
                TableReference left = node.getLeftTable();
                boolean paren = left.getPrecedence() < node.getPrecedence();
                if (paren) {
                    appendable.append('(');
                }
                print(left);
                if (paren) {
                    appendable.append(')');
                }
                appendable.append(t(Token.KW_STRAIGHT_JOIN), 0);
                TableReference right = node.getRightTable();
                paren = right.getPrecedence() <= node.getPrecedence();
                if (paren) {
                    appendable.append('(');
                }
                print(right);
                if (paren) {
                    appendable.append(')');
                }
                Expression on = node.getOnCond();
                if (on != null) {
                    appendable.append(t(Token.KW_ON), 0);
                    print(on);
                }
                break;
            }
        }
    }

    @Override
    public void visit(Subquery node) {
        appendable.append('(');
        QueryExpression query = node.getSubquery();
        print(query);
        appendable.append(')').append(t(Token.KW_AS), 0).append(node.getAlias());
    }

    @Override
    public void visit(Table node) {
        print(node.getTable());
        List<Identifier> partitions = node.getPartitions();
        if (partitions != null) {
            appendable.append(t(Token.KW_PARTITION), 1).append('(');
            int i = 0;
            for (Identifier p : partitions) {
                print(p);
                if (i++ < partitions.size() - 1) {
                    appendable.append(',');
                }
            }
            appendable.append(')');
        }
        String alias = node.getAlias();
        if (alias != null) {
            appendable.append(t(Token.KW_AS), 0).append(alias);
        }
        List<IndexHint> list = node.getHintList();
        if (list != null && !list.isEmpty()) {
            appendable.append(' ');
            printList(list, ' ');
        }
    }

    @Override
    public void visit(Tables node) {
        printList(node.getTables());
    }

    @Override
    public void visit(DataType node) {
        if (node.getLength() != null) {
            appendable.append(node.getTypeName()).append('(');
            appendable.append(node.getLength());
            if (node.getDecimals() != null) {
                appendable.append(',');
                appendable.append(node.getDecimals());
            }
            appendable.append(')');
        } else if (node.isBinary()) {
            appendable.append(node.getTypeName()).append('(');
            appendable.append(t(Token.KW_BINARY), 0);
        } else if (node.getValueList() != null && !node.getValueList().isEmpty()) {
            appendable.append(node.getTypeName()).append('(');
            Iterator<LiteralString> itor = node.getValueList().iterator();
            while (itor.hasNext()) {
                print(itor.next());
                if (itor.hasNext()) {
                    appendable.append(',');
                }
            }
            appendable.append(')');
        } else {
            appendable.append(node.getTypeName());
        }
        if (node.isUnsigned()) {
            appendable.append(t(Token.KW_UNSIGNED), 1);
        }
        if (node.isZerofill()) {
            appendable.append(t(Token.KW_ZEROFILL), 1);
        }
        if (node.getCharset() != null) {
            appendable.append(t(Token.KW_CHARACTER), 1).append(t(Token.KW_SET), 0);
            print(node.getCharset());
        }
        if (node.getCollation() != null) {
            appendable.append(t(Token.KW_COLLATE), 0);
            print(node.getCollation());
        }
    }

    @Override
    public void visit(VarsPrimary node) {
        switch (node.getType()) {
            case VarsPrimary.SYS_VAR: {
                appendable.append('@').append('@');
                if (node.getScope() != null) {
                    appendable.append(sql, node.getScopeStr()).append('.');
                }
                appendable.append(node.getVarText());
                break;
            }
            case VarsPrimary.OLD_ROW: {
                appendable.append(k(Keywords.OLD)).append('.').append(node.getVarText());
                break;
            }
            case VarsPrimary.NEW_ROW:
                appendable.append(k(Keywords.NEW)).append('.').append(node.getVarText());
                break;
            case VarsPrimary.USR_VAR:
            case VarsPrimary.USER: {
                appendable.append(node.getVarText());
                break;
            }
        }
    }

    @Override
    public void visit(OutFile node) {
        switch (node.type) {
            case OutFile.OUTFILE:
                appendable.append(t(Token.KW_OUTFILE), 2);
                print(node.outFile);
                appendable.append(' ');
                if (node.charset != null) {
                    appendable.append(t(Token.KW_CHARACTER), 2).append(t(Token.KW_SET), 2);
                    print(node.charset);
                    appendable.append(' ');
                }
                if (node.fieldsEnclosedBy != null || node.fieldsEscapedBy != null || node.fieldsTerminatedBy != null) {
                    appendable.append(k(Keywords.FIELDS), 2);
                    if (node.fieldsTerminatedBy != null) {
                        appendable.append(t(Token.KW_TERMINATED), 2).append(t(Token.KW_BY), 2);
                        print(node.fieldsTerminatedBy);
                        appendable.append(' ');
                    }
                    if (node.fieldsEnclosedBy != null) {
                        appendable.append(t(Token.KW_ENCLOSED), 2).append(t(Token.KW_BY), 2);
                        print(node.fieldsEnclosedBy);
                        appendable.append(' ');
                    }
                    if (node.fieldsEscapedBy != null) {
                        appendable.append(t(Token.KW_ESCAPED), 2).append(t(Token.KW_BY), 2);
                        print(node.fieldsEscapedBy);
                        appendable.append(' ');
                    }
                }
                if (node.linesStartingBy != null || node.linesTerminatedBy != null) {
                    appendable.append(t(Token.KW_LINES), 2);
                    if (node.linesStartingBy != null) {
                        appendable.append(t(Token.KW_STARTING), 2).append(t(Token.KW_BY), 2);
                        print(node.linesStartingBy);
                        appendable.append(' ');
                    }
                    if (node.linesTerminatedBy != null) {
                        appendable.append(t(Token.KW_TERMINATED), 2).append(t(Token.KW_BY), 2);
                        print(node.linesTerminatedBy);
                        appendable.append(' ');
                    }
                }
                break;
            case OutFile.DUMPFILE:
                appendable.append(k(Keywords.DUMPFILE), 2);
                print(node.dumpFile);
                appendable.append(' ');
                break;
            case OutFile.VAR:
                printList(node.vars);
                break;
            case OutFile.PARAM:
                print(node.into);
                break;
        }
    }

    @Override
    public void visit(PartitionDefinition node) {
        appendable.append(t(Token.KW_PARTITION), 0);
        print(node.getName());
        if (node.getLessThan() != null || node.getLessThanList() != null || node.isLessThanMaxvalue() != null
            || node.getInList() != null) {
            appendable.append(t(Token.KW_VALUES), 1);
            if (node.getLessThan() != null) {
                appendable.append(k(Keywords.LESS), 1).append(k(Keywords.THAN), 0).append('(');
                print(node.getLessThan());
                appendable.append(')');
            } else if (node.getLessThanList() != null) {
                appendable.append(k(Keywords.LESS), 1).append(k(Keywords.THAN), 0).append('(');
                printList(node.getLessThanList());
                appendable.append(')');
            } else if (node.isLessThanMaxvalue() != null) {
                appendable.append(k(Keywords.LESS), 1).append(k(Keywords.THAN), 0).append(t(Token.KW_MAXVALUE));
            } else if (node.getInList() != null) {
                appendable.append(t(Token.KW_IN), 0).append('(');
                printList(node.getInList());
                appendable.append(')');
            }
        }
        if (node.getEngine() != null) {
            appendable.append(k(Keywords.STORAGE), 0).append(k(Keywords.ENGINE)).append('=');
            print(node.getEngine());
        }
        if (node.getComment() != null) {
            appendable.append(k(Keywords.COMMENT), 1).append('=');
            print(node.getComment());
        }
        if (node.getDataDir() != null) {
            appendable.append(k(Keywords.DATA), 0).append(k(Keywords.DIRECTORY)).append('=');
            print(node.getDataDir());
        }
        if (node.getIndexDir() != null) {
            appendable.append(t(Token.KW_INDEX), 0).append(k(Keywords.DIRECTORY)).append('=');
            print(node.getIndexDir());
        }
        if (node.getMaxRows() != null) {
            appendable.append(k(Keywords.MAX_ROWS), 1).append('=');
            print(node.getMaxRows());
        }
        if (node.getMinRows() != null) {
            appendable.append(k(Keywords.MIN_ROWS), 1).append('=');
            print(node.getMinRows());
        }
        if (node.getTablespace() != null) {
            appendable.append(k(Keywords.TABLESPACE), 1).append('=');
            print(node.getTablespace());
        }
        List<SubpartitionDefinition> defs = node.getSubpartitionDefinitions();
        if (defs != null) {
            appendable.append('(');
            printList(defs);
            appendable.append(')');
        }
    }

    @Override
    public void visit(PartitionOptions node) {
        appendable.append(t(Token.KW_PARTITION), 2).append(t(Token.KW_BY));
        if (node.isLiner()) {
            appendable.append(t(Token.KW_LINEAR), 1);
        }
        Expression hash = node.getHash();
        if (hash != null) {
            appendable.append(k(Keywords.HASH), 1).append('(');
            print(hash);
            appendable.append(')');
        } else if (node.isKey()) {
            appendable.append(t(Token.KW_KEY), 1);
            if (node.getAlgorithm() > 0) {
                appendable.append(k(Keywords.ALGORITHM), 1).append('=').append(node.getAlgorithm());
            }
            appendable.append('(');
            List<Identifier> cols = node.getKeyColumns();
            if (cols != null) {
                for (int i = 0, size = cols.size(); i < size; i++) {
                    if (i != 0) {
                        appendable.append(',');
                    }
                    print(cols.get(i));
                }
            }
            appendable.append(')');
        } else if (node.getRangeExpr() != null || node.getRangeColumns() != null) {
            appendable.append(t(Token.KW_RANGE), 1);
            if (node.getRangeExpr() != null) {
                appendable.append('(');
                print(node.getRangeExpr());
                appendable.append(')');
            } else {
                List<Identifier> cols = node.getRangeColumns();
                appendable.append(k(Keywords.COLUMNS), 1).append('(');
                for (int i = 0, size = cols.size(); i < size; i++) {
                    if (i != 0) {
                        appendable.append(',');
                    }
                    print(cols.get(i));
                }
                appendable.append(')');
            }
        } else if (node.getListExpr() != null || node.getListColumns() != null) {
            appendable.append(k(Keywords.LIST), 1);
            if (node.getListExpr() != null) {
                appendable.append('(');
                print(node.getListExpr());
                appendable.append(')');
            } else {
                List<Identifier> cols = node.getListColumns();
                appendable.append(k(Keywords.COLUMNS), 1).append('(');
                for (int i = 0, size = cols.size(); i < size; i++) {
                    if (i != 0) {
                        appendable.append(',');
                    }
                    print(cols.get(i));
                }
                appendable.append(')');
            }
        }
        if (node.getPartitionNum() != null) {
            appendable.append(k(Keywords.PARTITIONS), 0).append(node.getPartitionNum());
        }
        if (node.getSubpartitionHash() != null || node.getSubpartitionKeyColumns() != null
            || node.getSubpartitionNum() != null) {
            appendable.append(k(Keywords.SUBPARTITION), 0).append(t(Token.KW_BY));
            if (node.isSubpartitionLiner()) {
                appendable.append(t(Token.KW_LINEAR), 1);
            }
            if (node.getSubpartitionHash() != null) {
                appendable.append(k(Keywords.HASH), 1).append('(');
                hash = node.getSubpartitionHash();
                print(hash);
                appendable.append(')');
            } else if (node.isSubpartitionKey()) {
                appendable.append(t(Token.KW_KEY), 1);
                if (node.getSubpartitionAlgorithm() > 0) {
                    appendable.append(k(Keywords.ALGORITHM), 1).append('=').append(node.getSubpartitionAlgorithm());
                }
                List<Identifier> cols = node.getSubpartitionKeyColumns();
                if (cols != null) {
                    appendable.append('(');
                    for (int i = 0, size = cols.size(); i < size; i++) {
                        if (i != 0) {
                            appendable.append(',');
                        }
                        print(cols.get(i));
                    }
                    appendable.append(')');
                }
            }
            if (node.getSubpartitionNum() != null) {
                appendable.append(k(Keywords.SUBPARTITIONS), 0).append(node.getSubpartitionNum());
            }
        }
        List<PartitionDefinition> defs = node.getPartitionDefinitions();
        if (defs != null) {
            appendable.append('(');
            printList(defs);
            appendable.append(')');
        }
    }

    @Override
    public void visit(SubpartitionDefinition node) {
        appendable.append(k(Keywords.SUBPARTITION), 0);
        print(node.getLogicalName());
        if (node.getEngine() != null) {
            appendable.append(k(Keywords.STORAGE), 0).append(k(Keywords.ENGINE)).append('=');
            print(node.getEngine());
        }
        if (node.getComment() != null) {
            appendable.append(k(Keywords.COMMENT), 1).append('=');
            print(node.getComment());
        }
        if (node.getDataDir() != null) {
            appendable.append(k(Keywords.DATA), 0).append(k(Keywords.DIRECTORY)).append('=');
            print(node.getDataDir());
        }
        if (node.getIndexDir() != null) {
            appendable.append(t(Token.KW_INDEX), 0).append(k(Keywords.DIRECTORY)).append('=');
            print(node.getIndexDir());
        }
        if (node.getMaxRows() != null) {
            appendable.append(k(Keywords.MAX_ROWS), 1).append('=');
            print(node.getMaxRows());
        }
        if (node.getMinRows() != null) {
            appendable.append(k(Keywords.MIN_ROWS), 1).append('=');
            print(node.getMinRows());
        }
        if (node.getTablespace() != null) {
            appendable.append(k(Keywords.TABLESPACE), 1).append('=');
            print(node.getTablespace());
        }
    }

    @Override
    public void visit(WindowClause node) {
        boolean appended = false;
        Identifier name = node.getName();
        if (name != null) {
            print(name);
            appended = true;
        }
        PartitionOptions partition = node.getPartition();
        if (partition != null) {
            if (appended) {
                appendable.append(' ');
            }
            print(partition);
            appended = true;
        }
        OrderBy order = node.getOrderBy();
        if (order != null) {
            if (appended) {
                appendable.append(' ');
            }
            print(order);
            appended = true;
        }
        Integer frameUnit = node.getFrameUnit();
        if (frameUnit != null) {
            if (appended) {
                appendable.append(' ');
            }
            switch (frameUnit) {
                case WindowClause.FRAME_UNIT_RANGE:
                    appendable.append(t(Token.KW_RANGE), 2);
                    break;
                case WindowClause.FRAME_UNIT_ROWS:
                    appendable.append(t(Token.KW_ROWS), 2);
                    break;
            }
            Pair<Integer, Expression> start = node.getFrameStart();
            Pair<Integer, Expression> end = node.getFrameEnd();
            if (end == null) {
                printFrameBound(start);
            } else {
                appendable.append(t(Token.KW_BETWEEN), 2);
                printFrameBound(start);
                appendable.append(t(Token.KW_AND), 0);
                printFrameBound(end);
            }
        }
    }

    @Override
    public void visit(WithClause node) {
        appendable.append(t(Token.KW_WITH), 2);
        if (node.isRecursive()) {
            appendable.append(t(Token.KW_RECURSIVE), 2);
        }
        List<CTE> cets = node.getCtes();
        if (cets != null) {
            Iterator<CTE> iter = cets.iterator();
            while (iter.hasNext()) {
                print(iter.next());
                if (iter.hasNext()) {
                    appendable.append(',');
                }
            }
        }
    }

    @Override
    public void visit(CTE node) {
        print(node.name);
        if (node.columns != null) {
            appendable.append('(');
            printList(node.columns);
            appendable.append(')');
        }
        if (node.subquery != null) {
            appendable.append(t(Token.KW_AS), 0);
            print(node.subquery);
        }
    }

    @Override
    public void visit(LockMode node) {
        switch (node.lockType) {
            case LockMode.FOR_UPDATE:
                appendable.append(t(Token.KW_FOR), 1).append(t(Token.KW_UPDATE), 1);
                if (node.tables != null && !node.tables.isEmpty()) {
                    appendable.append(t(Token.KW_OF), 0);
                    printList(node.tables);
                }
                if (node.lockAction == LockMode.NOWAIT) {
                    appendable.append(k(Keywords.NOWAIT), 1);
                } else if (node.lockAction == LockMode.SKIP_LOCKED) {
                    appendable.append(k(Keywords.SKIP), 1).append(k(Keywords.LOCKED), 1);
                }
                break;
            case LockMode.FOR_SHARE:
                appendable.append(t(Token.KW_FOR), 1).append(k(Keywords.SHARE), 1);
                if (node.tables != null && !node.tables.isEmpty()) {
                    appendable.append(t(Token.KW_OF), 0);
                    printList(node.tables);
                }
                if (node.lockAction == LockMode.NOWAIT) {
                    appendable.append(k(Keywords.NOWAIT), 1);
                } else if (node.lockAction == LockMode.SKIP_LOCKED) {
                    appendable.append(k(Keywords.SKIP), 1).append(k(Keywords.LOCKED), 1);
                }
                break;
            case LockMode.LOCK_IN_SHARE_MODE:
                appendable.append(t(Token.KW_LOCK), 1).append(t(Token.KW_IN), 1).append(k(Keywords.SHARE), 1)
                    .append(k(Keywords.MODE), 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void visit(BinaryOperatorExpression node) {
        boolean isDNID = false;
        Expression left = node.getLeftOprand();
        Expression right = node.getRightOprand();
        if (DNID != 0) {
            if (left instanceof Identifier && ((Identifier)left).getIdText().length == 4) {
                Identifier name = (Identifier)left;
            }
            if ((!isDNID) && (right instanceof Identifier && ((Identifier)right).getIdText().length == 4)) {
                Identifier name = (Identifier)right;
            }
        }
        if (!isDNID) {
            boolean paren =
                node.isLeftCombine() ? left.getPrecedence() < node.getPrecedence() && left.getPrecedence() > 0 :
                    left.getPrecedence() <= node.getPrecedence() && left.getPrecedence() > 0;
            if (paren || left instanceof IsExpression) {
                appendable.append('(');
            }
            print(left);
            if (paren || left instanceof IsExpression) {
                appendable.append(')');
            }
            appendable.append(node.getOperator());
            paren = node.isLeftCombine() ? right.getPrecedence() <= node.getPrecedence() && right.getPrecedence() > 0 :
                right.getPrecedence() < node.getPrecedence() && right.getPrecedence() > 0;
            if (paren) {
                appendable.append('(');
            }
            print(right);
            if (paren) {
                appendable.append(')');
            }
        } else {
            appendable.append("TRUE");
        }
    }

    @Override
    public void visit(Wildcard node) {
        Expression parent = node.getParent();
        if (parent != null) {
            print(parent);
            appendable.append('.');
        }
        appendable.append(node.getIdText());
    }

    @Override
    public void visit(DMLSelectUnionStatement node) {
        print(node.getWithClause());
        if (node.isInParentheses()) {
            appendable.append('(');
        }
        List<DMLSelectStatement> list = node.getSelects();
        final int fstDist = node.getFirstDistinctIndex();
        int i = 0;
        for (DMLSelectStatement select : list) {
            if (i > 0) {
                appendable.append(t(Token.KW_UNION), 0);
                if (i > fstDist) {
                    appendable.append(t(Token.KW_ALL), 2);
                }
            }
            print(select);
            ++i;
        }
        OrderBy order = node.getOrderBy();
        if (order != null) {
            appendable.append(' ');
            print(order);
        }
        Limit limit = node.getLimit();
        if (limit != null) {
            appendable.append(' ');
            print(limit);
        }
        if (node.isInParentheses()) {
            appendable.append(')');
        }
    }

    @Override
    public void visit(ParamMarker node) {
        if (this.paramIndexs == null) {
            this.paramIndexs = new ArrayList<Tuple2<Integer, Integer>>();
        }
        if (params != null) {
            paramIndexs.add(new Tuple2<Integer, Integer>(paramStart, appendable.getIndex() - paramStart));
            print(params[node.getParamIndex() - 1]);
            paramStart = appendable.getIndex();
        } else {
            paramIndexs.add(new Tuple2<Integer, Integer>(paramStart, appendable.getIndex() - paramStart));
            appendable.append('?');
            paramStart = appendable.getIndex();
        }
        if (node.getAlias() > 0) {
            appendable.append(t(Token.KW_AS), 0).append(sql, node.getAlias());
        }
    }

    @Override
    public void visit(ColumnDefinition node) {
        print(node.getDataType());
        if (node.isNotNull()) {
            appendable.append(t(Token.KW_NOT), 0).append(t(Token.LITERAL_NULL));
        }
        Expression defaultVal = node.getDefaultVal();
        if (defaultVal != null) {
            appendable.append(t(Token.KW_DEFAULT), 0);
            if (defaultVal instanceof FunctionExpression) {
                int type = ((FunctionExpression)defaultVal).getFunctionType();
                switch (type) {
                    case Functions.NOW:
                    case Functions.CURRENT_TIMESTAMP: {
                        print(defaultVal);
                        break;
                    }
                    default: {
                        appendable.append('(');
                        print(defaultVal);
                        appendable.append(')');
                    }
                }
            } else if (defaultVal instanceof Literal) {
                print(defaultVal);
            } else {
                appendable.append('(');
                print(defaultVal);
                appendable.append(')');
            }
        } else if (!node.isNotNull()) {
            appendable.append(t(Token.KW_DEFAULT), 0).append(t(Token.LITERAL_NULL));
        }
        if (node.getAutoIncrement() != null && node.getAutoIncrement().booleanValue()) {
            appendable.append(k(Keywords.AUTO_INCREMENT), 1);
        }
        Expression onUpdate = node.getOnUpdate();
        if (onUpdate != null) {
            appendable.append(t(Token.KW_ON), 0).append(t(Token.KW_UPDATE), 2);
            if (onUpdate instanceof FunctionExpression) {
                if (((FunctionExpression)onUpdate).getArguments() != null) {
                    print(onUpdate);
                } else {
                    appendable.append(((FunctionExpression)defaultVal).getFunctionName());
                }
            } else {
                print(onUpdate);
            }
        }
        LiteralString comment = node.getComment();
        if (comment != null) {
            appendable.append(k(Keywords.COMMENT), 0);
            print(comment);
        }
        Integer colFormat = node.getColumnFormat();
        if (colFormat != null) {
            appendable.append(k(Keywords.COLUMN_FORMAT), 0);
            switch (colFormat) {
                case ColumnDefinition.FORMAT_DEFAULT:
                    appendable.append(t(Token.KW_DEFAULT));
                    break;
                case ColumnDefinition.FORMAT_DYNAMIC:
                    appendable.append(k(Keywords.DYNAMIC));
                    break;
                case ColumnDefinition.FORMAT_FIXED:
                    appendable.append(k(Keywords.FIXED));
                    break;
            }
        }
        Integer storage = node.getStorage();
        if (storage != null) {
            appendable.append(k(Keywords.STORAGE), 0);
            switch (storage) {
                case ColumnDefinition.STORAGE_DEFAULT:
                    appendable.append(t(Token.KW_DEFAULT));
                    break;
                case ColumnDefinition.STORAGE_DISK:
                    appendable.append(k(Keywords.DISK));
                    break;
                case ColumnDefinition.STORAGE_MEMORY:
                    appendable.append(k(Keywords.MEMORY));
                    break;
            }
        }
        print(node.getReferenceDefinition());
        Tuple3<Identifier, Expression, Boolean> tuple = node.getCheckConstraintDef();
        if (tuple != null) {
            if (tuple._1() != null) {
                appendable.append(t(Token.KW_CONSTRAINT), 0);
                print(tuple._1());
            }
            appendable.append(t(Token.KW_CHECK), 1);
            appendable.append('(');
            print(tuple._2());
            appendable.append(')');
            if (tuple._3() != null) {
                if (tuple._3()) {
                    appendable.append(" ENFORCED");
                } else {
                    appendable.append(t(Token.KW_NOT), 0);
                    appendable.append("ENFORCED");
                }
            }
        }
    }

    @Override
    public void visit(ReferenceDefinition node) {
        appendable.append(t(Token.KW_REFERENCES), 0);
        print(node.getTable());
        appendable.append('(');
        List<IndexColumnName> cols = node.getKeys();
        for (int i = 0, size = cols.size(); i < size; i++) {
            if (i != 0) {
                appendable.append(',');
            }
            print(cols.get(i));
        }
        appendable.append(')');
        Integer match = node.getMatch();
        if (match != null) {
            appendable.append(t(Token.KW_MATCH), 1);
            switch (match) {
                case ReferenceDefinition.MATCH_FULL:
                    appendable.append(k(Keywords.FULL), 0);
                    break;
                case ReferenceDefinition.MATCH_PARTIAL:
                    appendable.append(k(Keywords.PARTIAL), 0);
                    break;
                case ReferenceDefinition.MATCH_SIMPLE:
                    appendable.append(k(Keywords.SIMPLE), 0);
                    break;
            }
        }
        Integer onDelete = node.getOnDelete();
        if (onDelete != null) {
            appendable.append(t(Token.KW_ON), 2).append(t(Token.KW_DELETE), 2);
            switch (onDelete) {
                case ReferenceDefinition.RESTRICT:
                    appendable.append(t(Token.KW_RESTRICT), 2);
                    break;
                case ReferenceDefinition.CASCADE:
                    appendable.append(t(Token.KW_CASCADE), 2);
                    break;
                case ReferenceDefinition.SET_NULL:
                    appendable.append(t(Token.KW_SET), 2).append(t(Token.LITERAL_NULL), 2);
                    break;
                case ReferenceDefinition.NO_ACTION:
                    appendable.append(k(Keywords.NO), 2).append(k(Keywords.ACTION), 2);
                    break;
                case ReferenceDefinition.SET_DEFAULT:
                    appendable.append(t(Token.KW_SET), 2).append(t(Token.KW_DEFAULT), 2);
                    break;
            }
        }
        Integer onUpdate = node.getOnUpdate();
        if (onUpdate != null) {
            appendable.append(t(Token.KW_ON), 2).append(t(Token.KW_UPDATE), 2);
            switch (onUpdate) {
                case ReferenceDefinition.RESTRICT:
                    appendable.append(t(Token.KW_RESTRICT), 2);
                    break;
                case ReferenceDefinition.CASCADE:
                    appendable.append(t(Token.KW_CASCADE), 2);
                    break;
                case ReferenceDefinition.SET_NULL:
                    appendable.append(t(Token.KW_SET), 2).append(t(Token.LITERAL_NULL), 2);
                    break;
                case ReferenceDefinition.NO_ACTION:
                    appendable.append(k(Keywords.NO), 2).append(k(Keywords.ACTION), 2);
                    break;
                case ReferenceDefinition.SET_DEFAULT:
                    appendable.append(t(Token.KW_SET), 2).append(t(Token.KW_DEFAULT), 2);
                    break;
            }
        }
    }

    @Override
    public void visit(IsExpression node) {
        Expression comparee = node.getOperand();
        boolean paren = comparee.getPrecedence() < node.getPrecedence() && comparee.getPrecedence() > 0;
        if (paren) {
            appendable.append('(');
        }
        print(comparee);
        if (paren) {
            appendable.append(')');
        }
        appendable.append(t(Token.KW_IS), 1);
        switch (node.getMode()) {
            case IsExpression.IS_NULL:
                appendable.append(t(Token.LITERAL_NULL), 1);
                break;
            case IsExpression.IS_TRUE:
                appendable.append(t(Token.LITERAL_BOOL_TRUE), 1);
                break;
            case IsExpression.IS_FALSE:
                appendable.append(t(Token.LITERAL_BOOL_FALSE), 1);
                break;
            case IsExpression.IS_UNKNOWN:
                appendable.append(k(Keywords.UNKNOWN), 1);
                break;
            case IsExpression.IS_NOT_NULL:
                appendable.append(t(Token.KW_NOT), 1).append(t(Token.LITERAL_NULL), 1);
                break;
            case IsExpression.IS_NOT_TRUE:
                appendable.append(t(Token.KW_NOT), 1).append(t(Token.LITERAL_BOOL_TRUE), 1);
                break;
            case IsExpression.IS_NOT_FALSE:
                appendable.append(t(Token.KW_NOT), 1).append(t(Token.LITERAL_BOOL_FALSE), 1);
                break;
            case IsExpression.IS_NOT_UNKNOWN:
                appendable.append(t(Token.KW_NOT), 1).append(k(Keywords.UNKNOWN), 1);
                break;
            default:
        }
    }

    @Override
    public void visit(IndexColumnName node) {
        print(node.getColumnName());
        Expression length = node.getLength();
        if (length != null) {
            appendable.append('(');
            print(length);
            appendable.append(')');
        }
        if (!node.isAsc()) {
            appendable.append(t(Token.KW_DESC), 1);
        }
    }

    @Override
    public void visit(LogicalExpression node) {
        for (int i = 0, len = node.getArity(); i < len; ++i) {
            if (i > 0) {
                appendable.append(' ').append(node.getOperator()).append(' ');
            }
            Expression operand = node.getOperand(i);
            boolean paren = operand.getPrecedence() < node.getPrecedence() && operand.getPrecedence() > 0;
            if (paren) {
                appendable.append('(');
            }
            print(operand);
            if (paren) {
                appendable.append(')');
            }
        }
    }

    @Override
    public void visit(UnaryOperatorExpression node) {
        appendable.append(node.getOperator());
        boolean paren =
            node.getOperand().getPrecedence() < node.getPrecedence() && node.getOperand().getPrecedence() > 0;
        if (paren) {
            appendable.append('(');
        } else {
            appendable.append(' ');
        }
        print(node.getOperand());
        if (paren) {
            appendable.append(')');
        }
    }

    @Override
    public void visit(TernaryOperatorExpression node) {
        switch (node.getType()) {
            case TernaryOperatorExpression.BETWEEN: {
                Expression comparee = node.getFirst();
                boolean paren = comparee.getPrecedence() <= node.getPrecedence() && comparee.getPrecedence() > 0;
                if (paren) {
                    appendable.append('(');
                }
                print(comparee);
                if (paren) {
                    appendable.append(')');
                }
                if (node.isNot()) {
                    appendable.append(t(Token.KW_NOT), 0).append(t(Token.KW_BETWEEN), 2);
                } else {
                    appendable.append(t(Token.KW_BETWEEN), 0);
                }
                Expression start = node.getSecond();
                paren = start.getPrecedence() < node.getPrecedence() && start.getPrecedence() > 0;
                if (paren) {
                    appendable.append('(');
                }
                print(start);
                if (paren) {
                    appendable.append(')');
                }
                appendable.append(t(Token.KW_AND), 0);
                Expression end = node.getThird();
                paren = end.getPrecedence() < node.getPrecedence() && end.getPrecedence() > 0;
                if (paren) {
                    appendable.append('(');
                }
                print(end);
                if (paren) {
                    appendable.append(')');
                }
                break;
            }
            case TernaryOperatorExpression.LIKE: {
                Expression comparee = node.getFirst();
                boolean paren = comparee.getPrecedence() < node.getPrecedence() && comparee.getPrecedence() > 0;
                if (paren) {
                    appendable.append('(');
                }
                print(comparee);
                if (paren) {
                    appendable.append(')');
                }
                if (node.isNot()) {
                    appendable.append(t(Token.KW_NOT), 1);
                }
                appendable.append(t(Token.KW_LIKE), 0);
                Expression pattern = node.getSecond();
                paren = pattern.getPrecedence() <= node.getPrecedence() && pattern.getPrecedence() > 0;
                if (paren) {
                    appendable.append('(');
                }
                print(pattern);
                if (paren) {
                    appendable.append(')');
                }
                Expression escape = node.getThird();
                if (escape != null) {
                    appendable.append(k(Keywords.ESCAPE), 0);
                    paren = escape.getPrecedence() <= node.getPrecedence() && escape.getPrecedence() > 0;
                    if (paren) {
                        appendable.append('(');
                    }
                    print(escape);
                    if (paren) {
                        appendable.append(')');
                    }
                }
                break;
            }
        }
    }

    @Override
    public void visit(CollateExpression node) {
        Expression string = node.getString();
        boolean paren = string.getPrecedence() < node.getPrecedence() && string.getPrecedence() > 0;
        if (paren) {
            appendable.append('(');
        }
        print(string);
        if (paren) {
            appendable.append(')');
        }
        appendable.append(t(Token.KW_COLLATE), 0).append(node.getCollateName());
    }

    @Override
    public void visit(InExpressionList node) {
        appendable.append('(');
        printList(node.getList());
        appendable.append(')');
    }

    @Override
    public void visit(ExistsPrimary node) {
        appendable.append(t(Token.KW_EXISTS), 2).append('(');
        print(node.getSubquery());
        appendable.append(')');
    }

    @Override
    public void visit(CaseWhenExpression node) {
        appendable.append(t(Token.KW_CASE));
        Expression comparee = node.getComparee();
        if (comparee != null) {
            appendable.append(' ');
            print(comparee);
        }
        List<Pair<Expression, Expression>> whenList = node.getWhenList();
        for (Pair<Expression, Expression> whenthen : whenList) {
            appendable.append(t(Token.KW_WHEN), 0);
            Expression when = whenthen.getKey();
            print(when);
            appendable.append(t(Token.KW_THEN), 0);
            Expression then = whenthen.getValue();
            print(then);
        }
        Expression elseRst = node.getElseResult();
        if (elseRst != null) {
            appendable.append(t(Token.KW_ELSE), 0);
            print(elseRst);
        }
        appendable.append(k(Keywords.END), 1);
    }

    @Override
    public void visit(Cast node) {
        byte[] functionName = node.getFunctionName();
        appendable.append(functionName).append('(');
        print(node.getExpr());
        appendable.append(t(Token.KW_AS), 0);
        long typeName = node.getTypeName();
        if (typeName > 0) {
            appendable.append(sql, typeName);
            Expression info1 = node.getTypeInfo1();
            if (info1 != null) {
                appendable.append('(');
                print(info1);
                Expression info2 = node.getTypeInfo2();
                if (info2 != null) {
                    appendable.append(',');
                    print(info2);
                }
                appendable.append(')');
            }
        } else {
            DataType type = node.getType();
            if (type != null) {
                switch (type.getType()) {
                    case DataType.BINARY: {
                        appendable.append(t(Token.KW_BINARY));
                        if (type.getLength() != null) {
                            appendable.append('(');
                            appendable.append(type.getLength());
                            appendable.append(')');
                        }
                    }
                    break;
                    case DataType.CHAR:
                        print(type);
                        break;
                    case DataType.DATE:
                        appendable.append(k(Keywords.DATE));
                        break;
                    case DataType.DATETIME:
                        print(type);
                        break;
                    case DataType.DECIMAL:
                        print(type);
                        break;
                    case DataType.JSON:
                        appendable.append(k(Keywords.JSON));
                        break;
                    case DataType.INT: {
                        if (type.isUnsigned()) {
                            appendable.append(t(Token.KW_UNSIGNED)).append(t(Token.KW_INTEGER), 1);
                        } else {
                            appendable.append(k(Keywords.SIGNED)).append(t(Token.KW_INTEGER), 1);
                        }
                    }
                    break;
                    case DataType.TIME:
                        print(type);
                        break;
                    default:
                        break;
                }
            }
        }
        appendable.append(')');
    }

    @Override
    public void visit(Char node) {
        byte[] functionName = node.getFunctionName();
        appendable.append(functionName).append('(');
        printList(node.getArguments());
        String charset = node.getCharset();
        if (charset != null) {
            appendable.append(t(Token.KW_USING), 0).append(charset);
        }
        appendable.append(')');
    }

    @Override
    public void visit(Convert node) {
        byte[] functionName = node.getFunctionName();
        appendable.append(functionName).append('(');
        printList(node.getArguments());
        long transcodeName = node.getTranscodeName();
        DataType dataType = node.getType();
        if (transcodeName > 0) {
            appendable.append(t(Token.KW_USING), 0).append(sql, transcodeName);
        } else if (dataType != null) {
            appendable.append(',');
            print(dataType);
        } else {
            appendable.append(',');
            long typeName = node.getTypeName();
            appendable.append(sql, typeName);
            Expression info1 = node.getTypeInfo1();
            if (info1 != null) {
                appendable.append('(');
                print(info1);
                Expression info2 = node.getTypeInfo2();
                if (info2 != null) {
                    appendable.append(',');
                    print(info2);
                }
                appendable.append(')');
            }
        }
        appendable.append(')');
    }

    @Override
    public void visit(DefaultValue node) {
        appendable.append(t(Token.KW_DEFAULT));
    }

    @Override
    public void visit(FromForFunction node) {
        byte[] functionName = node.getFunctionName();
        appendable.append(functionName);
        List<Expression> args = node.getArguments();
        if (args != null) {
            appendable.append('(');
            if (node.isFrom() && args.size() >= 2) {
                if (node.isFor()) {
                    print(args.get(0));
                    appendable.append(t(Token.KW_FROM), 0);
                    print(args.get(1));
                    appendable.append(t(Token.KW_FOR), 0);
                    print(args.get(2));
                } else {
                    print(args.get(0));
                    appendable.append(t(Token.KW_FROM), 0);
                    print(args.get(1));
                }
            }
            appendable.append(')');
        }
    }

    @Override
    public void visit(FunctionExpression node) {
        byte[] functionName = node.getFunctionName();
        appendable.append(functionName);
        List<Expression> args = node.getArguments();
        if (args != null) {
            appendable.append('(');
            printList(node.getArguments());
            appendable.append(')');
        }
    }

    @Override
    public void visit(MatchExpression node) {
        appendable.append(t(Token.KW_MATCH), 2).append('(');
        printList(node.getColumns());
        appendable.append(')').append(k(Keywords.AGAINST), 0).append('(');
        Expression pattern = node.getPattern();
        boolean inparen = containsCompIn(pattern);
        if (inparen) {
            appendable.append('(');
        }
        print(pattern);
        if (inparen) {
            appendable.append(')');
        }
        switch (node.getModifier()) {
            case MatchExpression.IN_BOOLEAN_MODE:
                appendable.append(t(Token.KW_IN), 1).append(k(Keywords.BOOLEAN), 1).append(k(Keywords.MODE), 1);
                break;
            case MatchExpression.IN_NATURAL_LANGUAGE_MODE:
                appendable.append(t(Token.KW_IN), 1).append(t(Token.KW_NATURAL), 1).append(k(Keywords.LANGUAGE), 1)
                    .append(k(Keywords.MODE), 1);
                break;
            case MatchExpression.IN_NATURAL_LANGUAGE_MODE_WITH_QUERY_EXPANSION:
                appendable.append(t(Token.KW_IN), 1).append(t(Token.KW_NATURAL), 1).append(k(Keywords.LANGUAGE), 1)
                    .append(k(Keywords.MODE), 1).append(t(Token.KW_WITH), 1).append(k(Keywords.QUERY), 1)
                    .append(k(Keywords.EXPANSION), 1);
                break;
            case MatchExpression.WITH_QUERY_EXPANSION:
                appendable.append(t(Token.KW_WITH), 1).append(k(Keywords.QUERY), 1).append(k(Keywords.EXPANSION), 1);
                break;
            case MatchExpression._DEFAULT:
                break;
            default:
        }
        appendable.append(')');
    }

    private boolean containsCompIn(Expression pat) {
        if (pat.getPrecedence() > Expression.PRECEDENCE_COMPARISION)
            return false;
        if (pat instanceof BinaryOperatorExpression) {
            if (pat instanceof ComparisionExpression
                && ((ComparisionExpression)pat).getType() == ComparisionExpression.IN) {
                return true;
            }
            BinaryOperatorExpression bp = (BinaryOperatorExpression)pat;
            if (bp.isLeftCombine()) {
                return containsCompIn(bp.getLeftOprand());
            } else {
                return containsCompIn(bp.getLeftOprand());
            }
        } else if (pat instanceof IsExpression) {
            IsExpression is = (IsExpression)pat;
            return containsCompIn(is.getOperand());
        } else if (pat instanceof TernaryOperatorExpression) {
            TernaryOperatorExpression tp = (TernaryOperatorExpression)pat;
            return containsCompIn(tp.getFirst()) || containsCompIn(tp.getSecond()) || containsCompIn(tp.getThird());
        } else if (pat instanceof UnaryOperatorExpression) {
            UnaryOperatorExpression up = (UnaryOperatorExpression)pat;
            return containsCompIn(up.getOperand());
        } else {
            return false;
        }
    }

    @Override
    public void visit(OverClause node) {
        print(node.getWindowFunction());
        appendable.append(t(Token.KW_OVER), 0);
        Identifier name = node.getWindowName();
        if (name != null) {
            print(name);
        } else {
            appendable.append('(');
            print(node.getWindowClause());
            appendable.append(')');
        }
    }

    @Override
    public void visit(PlaceHolder node) {
        appendable.append('$').append('{').append(sql, node.getName()).append('}');
    }

    @Override
    public void visit(RowExpression node) {
        appendable.append('(');
        printList(node.getRowExprList());
        appendable.append(')');
    }

    @Override
    public void visit(IntervalPrimary node) {
        appendable.append(t(Token.KW_INTERVAL), 2);
        Expression quantity = node.getQuantity();
        boolean paren = quantity.getPrecedence() < node.getPrecedence() && quantity.getPrecedence() > 0;
        if (paren) {
            appendable.append('(');
        }
        print(quantity);
        if (paren) {
            appendable.append(')');
        }
        appendable.append(' ').append(IntervalUnit.getInfo(node.getUnit()));
    }

    @Override
    public void visit(IndexHint node) {
        if (node.getAction() != null) {
            switch (node.getAction()) {
                case IndexHint.ACTION_USE:
                    appendable.append(t(Token.KW_USE));
                    break;
                case IndexHint.ACTION_IGNORE:
                    appendable.append(t(Token.KW_IGNORE));
                    break;
                case IndexHint.ACTION_FORCE:
                    appendable.append(t(Token.KW_FORCE));
                    break;
            }
        } else {
            appendable.append(t(Token.KW_USE));
        }
        appendable.append(node.isIndex() ? t(Token.KW_INDEX) : t(Token.KW_KEY), 0);
        if (node.getScope() != null) {
            appendable.append(t(Token.KW_FOR), 2);
            switch (node.getScope()) {
                case IndexHint.SCOPE_JOIN:
                    appendable.append(t(Token.KW_JOIN));
                    break;
                case IndexHint.SCOPE_GROUP_BY:
                    appendable.append(t(Token.KW_GROUP)).append(t(Token.KW_BY), 0);
                    break;
                case IndexHint.SCOPE_ORDER_BY:
                    appendable.append(t(Token.KW_ORDER)).append(t(Token.KW_BY), 0);
                    break;
            }
        }
        appendable.append('(');
        if (node.getIndexList() != null && !node.getIndexList().isEmpty()) {
            printList(node.getIndexList());
        }
        appendable.append(')');
    }
}

