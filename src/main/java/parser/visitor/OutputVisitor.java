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
import parser.ast.fragment.ddl.alter.AddCheckConstraintDefinition;
import parser.ast.fragment.ddl.alter.AddColumn;
import parser.ast.fragment.ddl.alter.AddForeignKey;
import parser.ast.fragment.ddl.alter.AddKey;
import parser.ast.fragment.ddl.alter.AlterCharacterSet;
import parser.ast.fragment.ddl.alter.AlterCheckConstraintDefination;
import parser.ast.fragment.ddl.alter.AlterColumn;
import parser.ast.fragment.ddl.alter.AlterIndex;
import parser.ast.fragment.ddl.alter.ChangeColumn;
import parser.ast.fragment.ddl.alter.ConvertCharacterSet;
import parser.ast.fragment.ddl.alter.DropCheckConstraintDefination;
import parser.ast.fragment.ddl.alter.DropColumn;
import parser.ast.fragment.ddl.alter.DropForeignKey;
import parser.ast.fragment.ddl.alter.DropIndex;
import parser.ast.fragment.ddl.alter.DropPrimaryKey;
import parser.ast.fragment.ddl.alter.EnableKeys;
import parser.ast.fragment.ddl.alter.Force;
import parser.ast.fragment.ddl.alter.ImportTablespace;
import parser.ast.fragment.ddl.alter.ModifyColumn;
import parser.ast.fragment.ddl.alter.OrderByColumns;
import parser.ast.fragment.ddl.alter.PartitionOperation;
import parser.ast.fragment.ddl.alter.RenameColumn;
import parser.ast.fragment.ddl.alter.RenameIndex;
import parser.ast.fragment.ddl.alter.RenameTo;
import parser.ast.fragment.ddl.alter.WithValidation;
import parser.ast.fragment.tableref.*;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.compound.BeginEndStatement;
import parser.ast.stmt.compound.DeclareStatement;
import parser.ast.stmt.compound.condition.*;
import parser.ast.stmt.compound.cursors.CursorCloseStatement;
import parser.ast.stmt.compound.cursors.CursorDeclareStatement;
import parser.ast.stmt.compound.cursors.CursorFetchStatement;
import parser.ast.stmt.compound.cursors.CursorOpenStatement;
import parser.ast.stmt.compound.flowcontrol.*;
import parser.ast.stmt.dal.DALSetStatement;
import parser.ast.stmt.dal.account.*;
import parser.ast.stmt.dal.resource.DALAlterResourceGroupStatement;
import parser.ast.stmt.dal.resource.DALCreateResourceGroupStatement;
import parser.ast.stmt.dal.resource.DALSetResourceGroupStatement;
import parser.ast.fragment.ddl.alter.Algorithm;
import parser.ast.stmt.ddl.alter.DDLAlterDatabaseStatement;
import parser.ast.stmt.ddl.alter.DDLAlterEventStatement;
import parser.ast.stmt.ddl.alter.DDLAlterFunctionStatement;
import parser.ast.stmt.ddl.alter.DDLAlterInstanceStatement;
import parser.ast.stmt.ddl.alter.DDLAlterLogfileGroupStatement;
import parser.ast.stmt.ddl.alter.DDLAlterProcedureStatement;
import parser.ast.stmt.ddl.alter.DDLAlterServerStatement;
import parser.ast.stmt.ddl.alter.DDLAlterTableStatement;
import parser.ast.fragment.ddl.alter.Lock;
import parser.ast.stmt.ddl.alter.DDLAlterTablespaceStatement;
import parser.ast.stmt.ddl.alter.DDLAlterViewStatement;
import parser.ast.stmt.ddl.create.DDLCreateDatabaseStatement;
import parser.ast.stmt.ddl.create.DDLCreateEventStatement;
import parser.ast.stmt.ddl.create.DDLCreateFunctionStatement;
import parser.ast.stmt.ddl.create.DDLCreateIndexStatement;
import parser.ast.stmt.ddl.create.DDLCreateLogfileGroupStatement;
import parser.ast.stmt.ddl.create.DDLCreateProcedureStatement;
import parser.ast.stmt.ddl.create.DDLCreateServerStatement;
import parser.ast.stmt.ddl.create.DDLCreateSpatialReferenceSystemStatement;
import parser.ast.stmt.ddl.create.DDLCreateTableStatement;
import parser.ast.stmt.ddl.create.DDLCreateTablespaceStatement;
import parser.ast.stmt.ddl.create.DDLCreateTriggerStatement;
import parser.ast.stmt.ddl.create.DDLCreateViewStatement;
import parser.ast.stmt.dml.*;
import parser.ast.stmt.dml.DMLSelectStatement.SelectOption;
import parser.ast.stmt.dml.DMLSelectStatement.OutFile;
import parser.ast.stmt.dml.DMLSelectStatement.LockMode;
import parser.ast.stmt.transactional.BeginStatement;
import parser.ast.stmt.transactional.SetTransactionStatement;
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
    public void visit(DMLUpdateStatement node) {
        print(node.getWithClause());
        appendable.append(t(Token.KW_UPDATE), 2);
        if (node.isLowPriority()) {
            appendable.append(t(Token.KW_LOW_PRIORITY), 2);
        }
        if (node.isIgnore()) {
            appendable.append(t(Token.KW_IGNORE), 2);
        }
        print(node.getTables());
        List<Identifier> partition = node.getPartition();
        if (partition != null) {
            Iterator<Identifier> itor = partition.iterator();
            appendable.append(t(Token.KW_PARTITION), 1).append('(');
            while (itor.hasNext()) {
                appendable.append(itor.next().getIdText());
                if (itor.hasNext()) {
                    appendable.append(',');
                }
            }
            appendable.append(')');
        }
        appendable.append(t(Token.KW_SET), 0);
        boolean isFst = true;
        for (Pair<Identifier, Expression> p : node.getValues()) {
            if (isFst) {
                isFst = false;
            } else {
                appendable.append(',');
            }
            print(p.getKey());
            appendable.append('=');
            print(p.getValue());
        }
        Expression where = node.getWhere();
        if (where != null) {
            appendable.append(t(Token.KW_WHERE), 0);
            print(where);
        }
        OrderBy orderBy = node.getOrderBy();
        if (orderBy != null) {
            appendable.append(' ');
            print(orderBy);
        }
        Limit limit = node.getLimit();
        if (limit != null) {
            appendable.append(' ');
            print(limit);
        }
    }

    @Override
    public void visit(DMLInsertReplaceStatement node) {
        boolean isInsert = node.isInsert();
        if (isInsert) {
            appendable.append(t(Token.KW_INSERT));
        } else {
            appendable.append(t(Token.KW_REPLACE));
        }
        switch (node.getMode()) {
            case DMLInsertReplaceStatement.DELAY:
                appendable.append(t(Token.KW_DELAYED), 1);
                break;
            case DMLInsertReplaceStatement.HIGH:
                if (isInsert) {
                    appendable.append(t(Token.KW_HIGH_PRIORITY), 1);
                }
                break;
            case DMLInsertReplaceStatement.LOW:
                appendable.append(t(Token.KW_LOW_PRIORITY), 1);
                break;
        }
        if (isInsert && node.isIgnore()) {
            appendable.append(t(Token.KW_IGNORE), 1);
        }
        appendable.append(t(Token.KW_INTO), 0);
        print(node.getTable());
        if (node.getTable() != null) {
            appendable.append(" ");
        }
        List<Identifier> partition = node.getPartitions();
        if (partition != null) {
            Iterator<Identifier> itor = partition.iterator();
            appendable.append(t(Token.KW_PARTITION), 1).append('(');
            while (itor.hasNext()) {
                appendable.append(itor.next().getIdText());
                if (itor.hasNext()) {
                    appendable.append(',');
                }
            }
            appendable.append(')');
        }
        List<Identifier> cols = node.getColumns();
        if (cols != null && !cols.isEmpty()) {
            appendable.append('(');
            printList(cols);
            appendable.append(')');
        }

        QueryExpression select = node.getSelect();
        if (select == null) {
            appendable.append(t(Token.KW_VALUES), 2);
            List<RowExpression> rows = node.getRows();
            if (rows != null && !rows.isEmpty()) {
                boolean isFst = true;
                for (RowExpression row : rows) {
                    if (row == null) {
                        continue;
                    }
                    if (isFst) {
                        isFst = false;
                    } else {
                        appendable.append(',');
                    }
                    appendable.append('(');
                    printList(row.getRowExprList());
                    appendable.append(')');
                }
            }
        } else {
            print(select);
        }
        if (isInsert) {
            List<Pair<Identifier, Expression>> dup = node.getDuplicateUpdate();
            if (dup != null && !dup.isEmpty()) {
                appendable.append(t(Token.KW_ON), 1).append(k(Keywords.DUPLICATE), 1).append(t(Token.KW_KEY), 1)
                    .append(t(Token.KW_UPDATE), 0);
                boolean isFst = true;
                for (Pair<Identifier, Expression> p : dup) {
                    if (isFst) {
                        isFst = false;
                    } else {
                        appendable.append(',').append(' ');
                    }
                    print(p.getKey());
                    appendable.append('=');
                    print(p.getValue());
                }
            }
        }
    }

    @Override
    public void visit(DMLDeleteStatement node) {
        print(node.getWithClause());
        appendable.append(t(Token.KW_DELETE), 2);
        if (node.isLowPriority()) {
            appendable.append(t(Token.KW_LOW_PRIORITY), 2);
        }
        if (node.isQuick()) {
            appendable.append(k(Keywords.QUICK), 2);
        }
        if (node.isIgnore()) {
            appendable.append(t(Token.KW_IGNORE), 2);
        }
        Tables tableRefs = node.getTableRefs();
        if (tableRefs == null) {
            appendable.append(t(Token.KW_FROM), 2);
            print(node.getTableNames().get(0));
        } else {
            printList(node.getTableNames());
            appendable.append(t(Token.KW_FROM), 0);
            print(node.getTableRefs());
        }
        List<Identifier> partition = node.getPartition();
        if (partition != null) {
            Iterator<Identifier> itor = partition.iterator();
            appendable.append(t(Token.KW_PARTITION), 1).append('(');
            while (itor.hasNext()) {
                appendable.append(itor.next().getIdText());
                if (itor.hasNext()) {
                    appendable.append(',');
                }
            }
            appendable.append(')');
        }
        Expression where = node.getWhereCondition();
        if (where != null) {
            appendable.append(t(Token.KW_WHERE), 0);
            print(where);
        }
        OrderBy orderBy = node.getOrderBy();
        if (orderBy != null) {
            appendable.append(' ');
            print(orderBy);
        }
        Limit limit = node.getLimit();
        if (limit != null) {
            appendable.append(' ');
            print(limit);
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
        Expression left = node.getLeftOprand();
        Expression right = node.getRightOprand();
        boolean paren = node.isLeftCombine() ? left.getPrecedence() < node.getPrecedence() && left.getPrecedence() > 0 :
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

    @Override
    public void visit(DDLCreateDatabaseStatement node) {
        appendable.append(t(Token.KW_CREATE)).append(t(Token.KW_DATABASE), 0);
        if (node.isIfNotExist()) {
            appendable.append(t(Token.KW_IF)).append(t(Token.KW_NOT), 0).append(t(Token.KW_EXISTS), 2);
        }
        print(node.getDb());
        if (node.getCharset() != null) {
            appendable.append(t(Token.KW_CHARACTER), 0).append(t(Token.KW_SET), 2);
            print(node.getCharset());
        }
        if (node.getCollate() != null) {
            appendable.append(t(Token.KW_COLLATE), 0);
            print(node.getCollate());
        }
        if (node.getEncryption() != null) {
            appendable.append(t(Token.KW_DEFAULT), 0).append(k(Keywords.ENCRYPTION), 2);
            if (node.getEncryption()) {
                appendable.append("'Y'");
            } else {
                appendable.append("'N'");
            }
        }
    }

    @Override
    public void visit(DDLCreateEventStatement node) {
        appendable.append(t(Token.KW_CREATE), 2);
        if (node.getDefiner() != null) {
            appendable.append(k(Keywords.DEFINER)).append('=');
            print(node.getDefiner());
            appendable.append(' ');
        }
        appendable.append(k(Keywords.EVENT), 2);
        if (node.isIfNotExist()) {
            appendable.append(t(Token.KW_IF)).append(t(Token.KW_NOT), 0).append(t(Token.KW_EXISTS), 2);
        }
        print(node.getEvent());
        if (node.getSchedule() != null) {
            appendable.append(' ');
            appendable.append(t(Token.KW_ON), 2);
            appendable.append(k(Keywords.SCHEDULE), 2);
            print(node.getSchedule());
        }
        if (node.getPreserve() != null) {
            appendable.append(' ');
            appendable.append(t(Token.KW_ON), 2);
            appendable.append(k(Keywords.COMPLETION), 2);
            if (!node.getPreserve()) {
                appendable.append(t(Token.KW_NOT), 2);
            }
            appendable.append(k(Keywords.PRESERVE));
        }
        if (node.getEnableType() != null) {
            appendable.append(' ');
            switch (node.getEnableType()) {
                case DDLCreateEventStatement.ENABLE:
                    appendable.append(k(Keywords.ENABLE));
                    break;
                case DDLCreateEventStatement.DISABLE:
                    appendable.append(k(Keywords.DISABLE));
                    break;
                case DDLCreateEventStatement.DISABLE_ON_SLAVE:
                    appendable.append(k(Keywords.DISABLE));
                    appendable.append(t(Token.KW_ON), 0);
                    appendable.append(k(Keywords.SLAVE));
                    break;
            }
        }
        if (node.getComment() != null) {
            appendable.append(k(Keywords.COMMENT), 0);
            print(node.getComment());
        }
        if (node.getEventBody() != null) {
            appendable.append(k(Keywords.DO), 0);
            print(node.getEventBody());
        }
    }

    @Override
    public void visit(DDLCreateFunctionStatement node) {
        appendable.append(t(Token.KW_CREATE));
        if (node.getDefiner() != null) {
            appendable.append(k(Keywords.DEFINER), 1).append('=');
            print(node.getDefiner());
        }
        appendable.append(t(Token.KW_FUNCTION), 0);
        print(node.getName());
        appendable.append('(');
        List<Pair<Identifier, DataType>> params = node.getParameters();
        if (params != null && !params.isEmpty()) {
            boolean first = true;
            for (Pair<Identifier, DataType> param : params) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                print(param.getKey());
                appendable.append(' ');
                print(param.getValue());
            }
        }
        appendable.append(')');
        appendable.append(k(Keywords.RETURNS), 2);
        print(node.getReturns());
        List<Characteristic> list = node.getCharacteristics();
        if (list != null && !list.isEmpty()) {
            appendable.append(' ');
            printList(list, ' ');
        }
        appendable.append(' ');
        print(node.getStmt());
        appendable.isLastSemicolon();
    }

    @Override
    public void visit(DDLCreateIndexStatement node) {
        appendable.append(t(Token.KW_CREATE), 2);
        if (node.getType() != null) {
            switch (node.getType()) {
                case DDLCreateIndexStatement.UNIQUE:
                    appendable.append(t(Token.KW_UNIQUE), 2);
                    break;
                case DDLCreateIndexStatement.FULLTEXT:
                    appendable.append(t(Token.KW_FULLTEXT), 2);
                    break;
                case DDLCreateIndexStatement.SPATIAL:
                    appendable.append(t(Token.KW_SPATIAL), 2);
                    break;
            }
        }
        appendable.append(t(Token.KW_INDEX), 2);
        print(node.getName());
        appendable.append(' ');
        if (node.getIndexType() != null) {
            appendable.append(t(Token.KW_USING));
            switch (node.getIndexType()) {
                case IndexOption.BTREE:
                    appendable.append(k(Keywords.BTREE), 0);
                    break;
                case IndexOption.HASH:
                    appendable.append(k(Keywords.HASH), 0);
                    break;
            }
        }
        appendable.append(t(Token.KW_ON), 2);
        print(node.getTable());
        appendable.append('(');
        List<IndexColumnName> cols = node.getColumns();
        if (cols != null) {
            for (int i = 0, size = cols.size(); i < size; i++) {
                if (i != 0) {
                    appendable.append(',');
                }
                print(cols.get(i));
            }
        }
        appendable.append(')');
        List<IndexOption> options = node.getOptions();
        if (options != null) {
            for (IndexOption option : options) {
                print(option);
            }
        }
        if (node.getAlgorithm() != null) {
            appendable.append(' ');
            switch (node.getAlgorithm()) {
                case DDLCreateIndexStatement.ALGORITHM_DEFAULT:
                    appendable.append(k(Keywords.ALGORITHM)).append('=').append(t(Token.KW_DEFAULT));
                    break;
                case DDLCreateIndexStatement.ALGORITHM_COPY:
                    appendable.append(k(Keywords.ALGORITHM)).append('=').append("COPY");
                    break;
                case DDLCreateIndexStatement.ALGORITHM_INPLACE:
                    appendable.append(k(Keywords.ALGORITHM)).append('=').append("INPLACE");
                    break;
            }
        }
        if (node.getLockOption() != null) {
            appendable.append(' ');
            switch (node.getLockOption()) {
                case DDLCreateIndexStatement.LOCK_DEFAULT:
                    appendable.append(t(Token.KW_LOCK)).append('=').append(t(Token.KW_DEFAULT));
                    break;
                case DDLCreateIndexStatement.LOCK_NONE:
                    appendable.append(t(Token.KW_LOCK)).append('=').append(k(Keywords.NONE));
                    break;
                case DDLCreateIndexStatement.LOCK_SHARED:
                    appendable.append(t(Token.KW_LOCK)).append('=').append("SHARED");
                    break;
                case DDLCreateIndexStatement.LOCK_EXCLUSIVE:
                    appendable.append(t(Token.KW_LOCK)).append('=').append("EXCLUSIVE");
                    break;
            }
        }
    }

    @Override
    public void visit(DDLCreateLogfileGroupStatement node) {
        appendable.append(t(Token.KW_CREATE)).append(k(Keywords.LOGFILE), 0).append(t(Token.KW_GROUP), 2);
        print(node.getName());
        appendable.append(t(Token.KW_ADD), 0).append(k(Keywords.UNDOFILE), 2);
        print(node.getUndoFile());
        if (node.getInitialSize() != null) {
            appendable.append(k(Keywords.INITIAL_SIZE), 1).append('=');
            print(node.getInitialSize());
        }
        if (node.getUndoBufferSize() != null) {
            appendable.append(k(Keywords.UNDO_BUFFER_SIZE), 1).append('=');
            print(node.getUndoBufferSize());
        }
        if (node.getRedoBufferSize() != null) {
            appendable.append(k(Keywords.REDO_BUFFER_SIZE), 1).append('=');
            print(node.getRedoBufferSize());
        }
        if (node.getNodeGroupId() != null) {
            appendable.append(k(Keywords.NODEGROUP), 1).append('=');
            print(node.getNodeGroupId());
        }
        if (node.isWait()) {
            appendable.append(k(Keywords.WAIT), 1);
        }
        if (node.getComment() != null) {
            appendable.append(k(Keywords.COMMENT), 1).append('=');
            print(node.getComment());
        }
        if (node.getEngine() != null) {
            appendable.append(k(Keywords.ENGINE), 1).append('=');
            print(node.getEngine());
        }
    }

    @Override
    public void visit(DDLCreateProcedureStatement node) {
        appendable.append(t(Token.KW_CREATE));
        if (node.getDefiner() != null) {
            appendable.append(k(Keywords.DEFINER), 1).append('=');
            print(node.getDefiner());
        }
        appendable.append(t(Token.KW_PROCEDURE), 0);
        print(node.getName());
        appendable.append('(');
        List<Tuple3<Integer, Identifier, DataType>> params = node.getParameters();
        if (params != null && !params.isEmpty()) {
            boolean first = true;
            for (Tuple3<Integer, Identifier, DataType> param : params) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                if (param._1() != null) {
                    switch (param._1()) {
                        case DDLCreateProcedureStatement.IN:
                            appendable.append(t(Token.KW_IN), 2);
                            break;
                        case DDLCreateProcedureStatement.OUT:
                            appendable.append(t(Token.KW_OUT), 2);
                            break;
                        case DDLCreateProcedureStatement.INOUT:
                            appendable.append(t(Token.KW_INOUT), 2);
                            break;
                    }
                }
                print(param._2());
                appendable.append(' ');
                print(param._3());
            }
        }
        appendable.append(')');
        List<Characteristic> list = node.getCharacteristics();
        if (list != null && !list.isEmpty()) {
            appendable.append(' ');
            printList(list, ' ');
        }
        appendable.append(' ');
        print(node.getStmt());
        appendable.isLastSemicolon();
    }

    @Override
    public void visit(DDLCreateServerStatement node) {
        appendable.append(t(Token.KW_CREATE)).append(k(Keywords.SERVER), 0);
        print(node.getServerName());
        appendable.append(t(Token.KW_FOREIGN), 0).append(k(Keywords.DATA)).append(k(Keywords.WRAPPER), 0);
        print(node.getWrapperName());
        if (node.getOptions() != null && !node.getOptions().isEmpty()) {
            appendable.append(k(Keywords.OPTIONS), 1).append('(');
            boolean first = true;
            for (Pair<Integer, Literal> option : node.getOptions()) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                switch (option.getKey()) {
                    case DDLCreateServerStatement.HOST:
                        appendable.append(k(Keywords.HOST));
                        break;
                    case DDLCreateServerStatement.DATABASE:
                        appendable.append(t(Token.KW_DATABASE));
                        break;
                    case DDLCreateServerStatement.USER:
                        appendable.append(k(Keywords.USER));
                        break;
                    case DDLCreateServerStatement.PASSWORD:
                        appendable.append(k(Keywords.PASSWORD));
                        break;
                    case DDLCreateServerStatement.SOCKET:
                        appendable.append(k(Keywords.SOCKET));
                        break;
                    case DDLCreateServerStatement.OWNER:
                        appendable.append(k(Keywords.OWNER));
                        break;
                    case DDLCreateServerStatement.PORT:
                        appendable.append(k(Keywords.PORT));
                        break;
                }
                appendable.append(' ');
                print(option.getValue());
            }
            appendable.append(')');
        }
    }

    @Override
    public void visit(DDLCreateSpatialReferenceSystemStatement node) {
        appendable.append(t(Token.KW_CREATE), 2);
        if (node.isOrReplace()) {
            appendable.append(t(Token.KW_OR)).append(t(Token.KW_REPLACE), 0);
        }
        appendable.append(t(Token.KW_SPATIAL)).append(" REFERENCE ").append(t(Token.KW_SYSTEM), 2);
        if (node.isIfNotExists()) {
            appendable.append(t(Token.KW_IF)).append(t(Token.KW_NOT), 0).append(t(Token.KW_EXISTS), 2);
        }
        print(node.getSrid());
        if (node.getAttributies() != null) {
            for (Tuple3<Integer, LiteralString, Long> tuple : node.getAttributies()) {
                appendable.append(' ');
                switch (tuple._1()) {
                    case DDLCreateSpatialReferenceSystemStatement.NAME:
                        appendable.append(k(Keywords.NAME), 2);
                        print(tuple._2());
                        break;
                    case DDLCreateSpatialReferenceSystemStatement.DEFINITION:
                        appendable.append(k(Keywords.DEFINITION), 2);
                        print(tuple._2());
                        break;
                    case DDLCreateSpatialReferenceSystemStatement.ORGANIZATION:
                        appendable.append(k(Keywords.ORGANIZATION), 2);
                        print(tuple._2());
                        appendable.append(k(Keywords.IDENTIFIED), 0).append(t(Token.KW_BY), 2);
                        print(tuple._3());
                        break;
                    case DDLCreateSpatialReferenceSystemStatement.DESCRIPTION:
                        appendable.append(k(Keywords.DESCRIPTION), 2);
                        print(tuple._2());
                        break;
                }
            }
        }
    }

    @Override
    public void visit(DDLCreateTablespaceStatement node) {
        appendable.append(t(Token.KW_CREATE));
        if (node.isUndo()) {
            appendable.append(t(Token.KW_UNDO), 1);
        }
        appendable.append(k(Keywords.TABLESPACE), 0);
        print(node.getName());
        if (node.getFileName() != null) {
            appendable.append(t(Token.KW_ADD), 0).append(k(Keywords.DATAFILE), 2);
            print(node.getFileName());
        }
        if (node.getFileBlockSize() != null) {
            appendable.append(k(Keywords.FILE_BLOCK_SIZE), 1).append('=');
            print(node.getFileBlockSize());
        }
        if (node.getEncryption() != null) {
            appendable.append(k(Keywords.ENCRYPTION), 1).append('=');
            if (node.getEncryption()) {
                appendable.append("'Y'");
            } else {
                appendable.append("'N'");
            }
        }
        if (node.getLogFileGroup() != null) {
            appendable.append(t(Token.KW_USE), 1).append(k(Keywords.LOGFILE), 0).append(t(Token.KW_GROUP), 2);
            print(node.getLogFileGroup());
        }
        if (node.getExtentSize() != null) {
            appendable.append(k(Keywords.EXTENT_SIZE), 1).append('=');
            print(node.getExtentSize());
        }
        if (node.getInitialSize() != null) {
            appendable.append(k(Keywords.INITIAL_SIZE), 1).append('=');
            print(node.getInitialSize());
        }
        if (node.getAutoextendSize() != null) {
            appendable.append(k(Keywords.AUTOEXTEND_SIZE), 1).append('=');
            print(node.getAutoextendSize());
        }
        if (node.getMaxSize() != null) {
            appendable.append(k(Keywords.MAX_SIZE), 1).append('=');
            print(node.getMaxSize());
        }
        if (node.getNodeGroupId() != null) {
            appendable.append(k(Keywords.NODEGROUP), 1).append('=');
            print(node.getNodeGroupId());
        }
        if (node.isWait()) {
            appendable.append(k(Keywords.WAIT), 1);
        }
        if (node.getComment() != null) {
            appendable.append(k(Keywords.COMMENT), 1).append('=');
            print(node.getComment());
        }
        if (node.getEngine() != null) {
            appendable.append(k(Keywords.ENGINE), 1).append('=');
            print(node.getEngine());
        }
    }

    @Override
    public void visit(DDLCreateTableStatement node) {
        appendable.append(t(Token.KW_CREATE));
        if (node.isTemporary()) {
            appendable.append(k(Keywords.TEMPORARY), 1);
        }
        appendable.append(t(Token.KW_TABLE), 0);
        if (node.isIfNotExists()) {
            appendable.append(t(Token.KW_IF)).append(t(Token.KW_NOT), 1).append(t(Token.KW_EXISTS), 0);
        }
        print(node.getTableName());
        appendable.append('(').append('\n').append(' ').append(' ');
        List<Pair<Identifier, ColumnDefinition>> columns = node.getColumns();
        if (columns != null) {
            for (int i = 0, size = columns.size(); i < size; i++) {
                if (i != 0) {
                    appendable.append(',').append('\n').append(' ').append(' ');
                }
                Pair<Identifier, ColumnDefinition> column = columns.get(i);
                print(column.getKey());
                appendable.append(' ');
                print(column.getValue());
            }
        }
        if (node.getPrimaryKey() != null) {
            appendable.append(',').append('\n');
            print(node.getPrimaryKey());
        }
        if (node.getUniqueKeys() != null) {
            for (IndexDefinition key : node.getUniqueKeys()) {
                appendable.append(',').append('\n');
                print(key);
            }
        }
        if (node.getKeys() != null) {
            for (IndexDefinition key : node.getKeys()) {
                appendable.append(',').append('\n');
                print(key);
            }
        }
        if (node.getFullTextKeys() != null) {
            for (IndexDefinition key : node.getFullTextKeys()) {
                appendable.append(',').append('\n');
                print(key);
            }
        }
        if (node.getSpatialKeys() != null) {
            for (IndexDefinition key : node.getSpatialKeys()) {
                appendable.append(',').append('\n');
                print(key);
            }
        }
        List<ForeignKeyDefinition> foreignKeyDefs = node.getForeignKeys();
        if (foreignKeyDefs != null) {
            for (int i = 0, size = foreignKeyDefs.size(); i < size; i++) {
                appendable.append(',').append('\n');
                print(foreignKeyDefs.get(i));
            }
        }
        List<Identifier> checksName = node.getChecksName();
        List<Pair<Expression, Boolean>> checks = node.getChecks();
        if (checks != null) {
            for (int i = 0, size = checks.size(); i < size; ++i) {
                Pair<Expression, Boolean> pair = checks.get(i);
                Identifier checkName = checksName.get(i);
                appendable.append(',').append('\n');
                appendable.append(t(Token.KW_CONSTRAINT), 2);
                if (checkName != null) {
                    print(checkName);
                }
                appendable.append(t(Token.KW_CHECK), 1).append('(');
                print(pair.getKey());
                appendable.append(')');
                if (pair.getValue() != null) {
                    if (pair.getValue() == true) {
                        appendable.append(" ENFORCED");
                    } else {
                        appendable.append(t(Token.KW_NOT), 2);
                        appendable.append("ENFORCED");
                    }
                }
            }
        }
        appendable.append('\n').append(')');
        print(node.getTableOptions());
        if (node.getPartitionOptions() != null) {
            appendable.append(' ');
            print(node.getPartitionOptions());
        }
        if (node.getIsIgnore() != null && node.getIsIgnore()) {
            appendable.append(t(Token.KW_IGNORE), 2);
        } else if (node.getIsReplace() != null && node.getIsReplace()) {
            appendable.append(t(Token.KW_REPLACE), 2);
        }
        if (node.getQueryExpression() != null) {
            appendable.append(t(Token.KW_AS), 2);
            print(node.getQueryExpression());
        }
    }

    @Override
    public void visit(DDLCreateTriggerStatement node) {
        appendable.append(t(Token.KW_CREATE));
        if (node.getDefiner() != null) {
            appendable.append(k(Keywords.DEFINER), 1).append('=');
            print(node.getDefiner());
        }
        appendable.append(t(Token.KW_TRIGGER), 0);
        print(node.getName());
        if (node.isBefore()) {
            appendable.append(t(Token.KW_BEFORE), 1);
        } else {
            appendable.append(k(Keywords.AFTER), 1);
        }
        switch (node.getEvent()) {
            case DDLCreateTriggerStatement.INSERT:
                appendable.append(t(Token.KW_INSERT), 1);
                break;
            case DDLCreateTriggerStatement.UPDATE:
                appendable.append(t(Token.KW_UPDATE), 1);
                break;
            case DDLCreateTriggerStatement.DELETE:
                appendable.append(t(Token.KW_DELETE), 1);
                break;
        }
        appendable.append(t(Token.KW_ON), 0);
        print(node.getTable());
        appendable.append(t(Token.KW_FOR), 0).append(t(Token.KW_EACH)).append(t(Token.KW_ROW), 1);
        if (node.getFollows() != null) {
            appendable.append(node.getFollows() ? k(Keywords.FOLLOWS) : k(Keywords.PRECEDES), 0);
            print(node.getOtherName());
        }
        appendable.append(' ');
        print(node.getStmt());
        appendable.isLastSemicolon();
    }

    @Override
    public void visit(DDLCreateViewStatement node) {
        appendable.append(t(Token.KW_CREATE));
        if (node.isOrReplace()) {
            appendable.append(t(Token.KW_OR), 0).append(t(Token.KW_REPLACE));
        }
        if (node.getAlgorithm() != null) {
            switch (node.getAlgorithm()) {
                case DDLCreateViewStatement.UNDEFINED:
                    appendable.append(k(Keywords.ALGORITHM), 1).append('=').append(k(Keywords.UNDEFINED));
                    break;
                case DDLCreateViewStatement.MERGE:
                    appendable.append(k(Keywords.ALGORITHM), 1).append('=').append(k(Keywords.MERGE));
                    break;
                case DDLCreateViewStatement.TEMPTABLE:
                    appendable.append(k(Keywords.ALGORITHM), 1).append('=').append(k(Keywords.TEMPTABLE));
                    break;
            }
        }
        if (node.getDefiner() != null) {
            appendable.append(k(Keywords.DEFINER), 1).append('=');
            print(node.getDefiner());
        }
        if (node.getSqlSecurityDefiner() != null) {
            appendable.append(t(Token.KW_SQL), 0).append(k(Keywords.SECURITY), 2)
                .append(node.getSqlSecurityDefiner() ? k(Keywords.DEFINER) : k(Keywords.INVOKER));
        }
        appendable.append(k(Keywords.VIEW), 0);
        print(node.getName());
        List<Identifier> columnsList = node.getColumns();
        if (columnsList != null && !columnsList.isEmpty()) {
            appendable.append('(');
            boolean isFst = true;
            for (Identifier p : columnsList) {
                if (isFst) {
                    isFst = false;
                } else {
                    appendable.append(',');
                }
                print(p);
            }
            appendable.append(')');
        }
        appendable.append(t(Token.KW_AS), 0);
        print(node.getStmt());
        if (node.isWithCheckOption()) {
            appendable.append(t(Token.KW_WITH), 0);
            if (node.getCascaded() != null) {
                appendable.append(node.getCascaded() ? k(Keywords.CASCADED) : k(Keywords.LOCAL), 2);
            }
            appendable.append(t(Token.KW_CHECK)).append(t(Token.KW_OPTION), 1);
        }
    }

    @Override
    public void visit(DALCreateResourceGroupStatement node) {
        appendable.append(t(Token.KW_CREATE)).append(k(Keywords.RESOURCE), 0).append(t(Token.KW_GROUP), 2);
        print(node.getName());
        appendable.append(k(Keywords.TYPE), 1).append('=');
        if (node.isSystemOrUser()) {
            appendable.append(t(Token.KW_SYSTEM));
        } else {
            appendable.append(k(Keywords.USER));
        }
        if (node.getVcpus() != null) {
            appendable.append(k(Keywords.VCPU), 1).append('=');
            printList(node.getVcpus());
        }
        if (node.getThreadPriority() != null) {
            appendable.append(k(Keywords.THREAD_PRIORITY), 1).append('=');
            print(node.getThreadPriority());
        }
        if (node.getEnable() != null) {
            if (node.getEnable()) {
                appendable.append(k(Keywords.ENABLE), 1);
            } else {
                appendable.append(k(Keywords.DISABLE), 1);
            }
        }

    }

    @Override
    public void visit(DALCreateRoleStatement node) {
        appendable.append(t(Token.KW_CREATE)).append(k(Keywords.ROLE), 0);
        if (node.isIfNotExists()) {
            appendable.append(t(Token.KW_IF)).append(t(Token.KW_NOT), 1).append(t(Token.KW_EXISTS), 0);
        }
        printList(node.getRoles());
    }

    @Override
    public void visit(DALCreateUserStatement node) {
        appendable.append(t(Token.KW_CREATE)).append(k(Keywords.USER), 0);
        if (node.isIfNotExists()) {
            appendable.append(t(Token.KW_IF)).append(t(Token.KW_NOT), 1).append(t(Token.KW_EXISTS), 0);
        }
        boolean first = true;
        for (Pair<Expression, AuthOption> p : node.getUsers()) {
            if (first) {
                first = false;
            } else {
                appendable.append(',');
            }
            print(p.getKey());
            appendable.append(' ');
            print(p.getValue());
        }
        if (node.getRoles() != null && !node.getRoles().isEmpty()) {
            appendable.append(t(Token.KW_DEFAULT), 0).append(k(Keywords.ROLE), 2);
            printList(node.getRoles());
        }
        if (node.getRequireNone() != null) {
            appendable.append(t(Token.KW_REQUIRE), 0).append(k(Keywords.NONE));
        } else if (node.getTlsOptions() != null) {
            appendable.append(t(Token.KW_REQUIRE), 0);
            first = true;
            for (Pair<Integer, LiteralString> p : node.getTlsOptions()) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(t(Token.KW_AND), 0);
                }
                switch (p.getKey()) {
                    case DALCreateUserStatement.TLS_SSL:
                        appendable.append(t(Token.KW_SSL));
                        break;
                    case DALCreateUserStatement.TLS_X509:
                        appendable.append(k(Keywords.X509));
                        break;
                    case DALCreateUserStatement.TLS_CIPHER:
                        appendable.append(k(Keywords.CIPHER), 2);
                        print(p.getValue());
                        break;
                    case DALCreateUserStatement.TLS_ISSUER:
                        appendable.append(k(Keywords.ISSUER), 2);
                        print(p.getValue());
                        break;
                    case DALCreateUserStatement.TLS_SUBJECT:
                        appendable.append(k(Keywords.SUBJECT), 2);
                        print(p.getValue());
                        break;
                }
            }
        }
        if (node.getResourceOptions() != null) {
            appendable.append(t(Token.KW_WITH), 1);
            for (Pair<Integer, Long> p : node.getResourceOptions()) {
                appendable.append(' ');
                switch (p.getKey()) {
                    case DALCreateUserStatement.MAX_QUERIES_PER_HOUR:
                        appendable.append(k(Keywords.MAX_QUERIES_PER_HOUR), 2);
                        break;
                    case DALCreateUserStatement.MAX_UPDATES_PER_HOUR:
                        appendable.append(k(Keywords.MAX_UPDATES_PER_HOUR), 2);
                        break;
                    case DALCreateUserStatement.MAX_CONNECTIONS_PER_HOUR:
                        appendable.append(k(Keywords.MAX_CONNECTIONS_PER_HOUR), 2);
                        break;
                    case DALCreateUserStatement.MAX_USER_CONNECTIONS:
                        appendable.append(k(Keywords.MAX_USER_CONNECTIONS), 2);
                        break;
                }
                appendable.append(p.getValue());
            }
        }
        Tuple3<Integer, Boolean, Long> option = node.getPasswordOption();
        if (option != null) {
            appendable.append(k(Keywords.PASSWORD), 0);
            switch (option._1()) {
                case DALCreateUserStatement.PASSWORD_EXPIRE:
                    appendable.append(k(Keywords.EXPIRE), 2);
                    if (option._2() != null) {
                        if (option._2()) {
                            appendable.append(t(Token.KW_DEFAULT));
                        } else {
                            appendable.append(k(Keywords.NEVER));
                        }
                    } else {
                        appendable.append(t(Token.KW_INTERVAL), 2);
                        print(option._3());
                        appendable.append(k(Keywords.DAY), 1);
                    }
                    break;
                case DALCreateUserStatement.PASSWORD_HISTORY:
                    appendable.append(k(Keywords.HISTORY), 2);
                    if (option._2() != null && option._2()) {
                        appendable.append(t(Token.KW_DEFAULT));
                    } else {
                        print(option._3());
                    }
                    break;
                case DALCreateUserStatement.PASSWORD_REUSE_INTERVAL:
                    appendable.append(k(Keywords.REUSE)).append(t(Token.KW_INTERVAL), 0);
                    if (option._2() != null && option._2()) {
                        appendable.append(t(Token.KW_DEFAULT));
                    } else {
                        print(option._3());
                        appendable.append(k(Keywords.DAY), 1);
                    }
                    break;
                case DALCreateUserStatement.PASSWORD_REQUIRE_CURRENT:
                    appendable.append(t(Token.KW_REQUIRE)).append(k(Keywords.CURRENT), 0);
                    if (option._2() != null) {
                        if (option._2()) {
                            appendable.append(t(Token.KW_DEFAULT));
                        } else {
                            appendable.append(k(Keywords.OPTIONAL));
                        }
                    }
                    break;
            }
        }
        if (node.getLock() != null) {
            if (node.getLock()) {
                appendable.append(k(Keywords.ACCOUNT), 0).append(t(Token.KW_LOCK));
            } else {
                appendable.append(k(Keywords.ACCOUNT), 0).append(t(Token.KW_UNLOCK));
            }
        }
    }

    @Override
    public void visit(DALSetStatement node) {
        appendable.append(t(Token.KW_SET), 2);
        switch (node.getType()) {
            case DALSetStatement.SET_VARS: {
                boolean isFst = true;
                for (Pair<VarsPrimary, Expression> p : node.getAssignmentList()) {
                    if (isFst) {
                        isFst = false;
                    } else {
                        appendable.append(',');
                    }
                    print(p.getKey());
                    appendable.append('=');
                    print(p.getValue());
                }
                break;
            }
            case DALSetStatement.SET_CHARSET: {
                appendable.append(k(Keywords.CHARSET), 2);
                if (node.isDefault()) {
                    appendable.append(t(Token.KW_DEFAULT));
                } else {
                    print(node.getCharset());
                }
                break;
            }
            case DALSetStatement.SET_NAMES: {
                appendable.append(k(Keywords.NAMES), 2);
                if (node.isDefault()) {
                    appendable.append(t(Token.KW_DEFAULT));
                } else {
                    print(node.getCharset());
                    if (node.getCollate() != null) {
                        appendable.append(t(Token.KW_COLLATE), 0);
                        print(node.getCollate());
                    }
                }
                break;
            }
        }
    }

    @Override
    public void visit(DALSetResourceGroupStatement node) {
        appendable.append(t(Token.KW_SET)).append(k(Keywords.RESOURCE), 0).append(t(Token.KW_GROUP), 2);
        print(node.getName());
        List<Long> ids = node.getThreadIds();
        if (ids != null) {
            appendable.append(t(Token.KW_FOR), 0);
            boolean isFst = true;
            for (long id : ids) {
                if (isFst) {
                    isFst = false;
                } else {
                    appendable.append(',');
                }
                appendable.append(id);
            }
        }
    }

    @Override
    public void visit(DALSetDefaultRoleStatement node) {
        appendable.append(t(Token.KW_SET)).append(t(Token.KW_DEFAULT), 0).append(k(Keywords.ROLE), 2);
        if (node.getNoneOrAll() != null) {
            if (node.getNoneOrAll()) {
                appendable.append(k(Keywords.NONE), 2);
            } else {
                appendable.append(t(Token.KW_ALL), 2);
            }
        } else {
            printList(node.getRoles());
            appendable.append(' ');
        }
        appendable.append(t(Token.KW_TO), 2);
        printList(node.getUsers());
    }

    @Override
    public void visit(DALSetPasswordStatement node) {
        appendable.append(t(Token.KW_SET)).append(k(Keywords.PASSWORD), 1);
        if (node.getUser() != null) {
            appendable.append(t(Token.KW_FOR), 0);
            print(node.getUser());
        } else {
            appendable.append('=');
            print(node.getAuthString());
        }
        if (node.getCurrentAuthString() != null) {
            appendable.append(t(Token.KW_REPLACE), 0);
            print(node.getCurrentAuthString());
        }
        if (node.isRetainCurrentPassword()) {
            appendable.append(k(Keywords.RETAIN), 0).append(k(Keywords.CURRENT)).append(k(Keywords.PASSWORD), 1);
        }
    }

    @Override
    public void visit(DALSetRoleStatement node) {
        appendable.append(t(Token.KW_SET)).append(k(Keywords.ROLE), 0);
        if (node.getDefaultOrNone() != null) {
            if (node.getDefaultOrNone()) {
                appendable.append(t(Token.KW_DEFAULT));
            } else {
                appendable.append(k(Keywords.NONE));
            }
        } else if (node.isAll()) {
            appendable.append(t(Token.KW_ALL));
            if (node.getRoles() != null) {
                appendable.append(t(Token.KW_EXCEPT), 0);
                printList(node.getRoles());
            }
        } else {
            printList(node.getRoles());
        }
    }

    @Override
    public void visit(ScheduleDefinition node) {
        if (node.getEveryInterval() != null) {
            appendable.append(k(Keywords.EVERY), 2);
            print(node.getEveryInterval());
            appendable.append(' ');
            printIntervalUnit(node.getEveryIntervalQuantity());
            if (node.getStartsTimestamp() != null) {
                appendable.append(k(Keywords.STARTS), 0);
                print(node.getStartsTimestamp());
                List<Pair<LiteralNumber, Integer>> startsIntervalList = node.getStartsIntervalList();
                if (startsIntervalList != null && !startsIntervalList.isEmpty()) {
                    for (Pair<LiteralNumber, Integer> integerPair : startsIntervalList) {
                        appendable.append(' ').append('+');
                        appendable.append(t(Token.KW_INTERVAL), 0);
                        print(integerPair.getKey());
                        appendable.append(' ');
                        printIntervalUnit(integerPair.getValue());
                    }
                }
            }
            if (node.getEndsTimestamp() != null) {
                appendable.append(k(Keywords.ENDS), 0);
                print(node.getEndsTimestamp());
                List<Pair<LiteralNumber, Integer>> endsIntervalList = node.getEndsIntervalList();
                if (endsIntervalList != null && !endsIntervalList.isEmpty()) {
                    for (Pair<LiteralNumber, Integer> integerPair : endsIntervalList) {
                        appendable.append(' ').append('+');
                        appendable.append(t(Token.KW_INTERVAL), 0);
                        print(integerPair.getKey());
                        appendable.append(' ');
                        printIntervalUnit(integerPair.getValue());
                    }
                }
            }
        } else if (node.getAtTimestamp() != null) {
            appendable.append(k(Keywords.AT), 2);
            print(node.getAtTimestamp());
            List<Pair<LiteralNumber, Integer>> intervalList = node.getIntervalList();
            if (intervalList != null && !intervalList.isEmpty()) {
                for (Pair<LiteralNumber, Integer> integerPair : intervalList) {
                    appendable.append(' ').append('+');
                    appendable.append(t(Token.KW_INTERVAL), 0);
                    print(integerPair.getKey());
                    appendable.append(' ');
                    printIntervalUnit(integerPair.getValue());
                }
            }
        }
    }

    @Override
    public void visit(Characteristic node) {
        switch (node.getType()) {
            case Characteristic.COMMENT:
                appendable.append(k(Keywords.COMMENT), 2);
                print(node.getComment());
                break;
            case Characteristic.LANGUAGE_SQL:
                appendable.append(k(Keywords.LANGUAGE), 2).append(t(Token.KW_SQL));
                break;
            case Characteristic.DETERMINISTIC:
                appendable.append(t(Token.KW_DETERMINISTIC));
                break;
            case Characteristic.NOT_DETERMINISTIC:
                appendable.append(t(Token.KW_NOT), 2).append(t(Token.KW_DETERMINISTIC));
                break;
            case Characteristic.CONTAINS_SQL:
                appendable.append(k(Keywords.CONTAINS), 2).append(t(Token.KW_SQL));
                break;
            case Characteristic.NO_SQL:
                appendable.append(k(Keywords.NO), 2).append(t(Token.KW_SQL));
                break;
            case Characteristic.READS_SQL_DATA:
                appendable.append(t(Token.KW_READS)).append(t(Token.KW_SQL), 0).append(k(Keywords.DATA));
                break;
            case Characteristic.MODIFIES_SQL_DATA:
                appendable.append(t(Token.KW_MODIFIES)).append(t(Token.KW_SQL), 0).append(k(Keywords.DATA));
                break;
            case Characteristic.SQL_SECURITY_DEFINER:
                appendable.append(t(Token.KW_SQL)).append(k(Keywords.SECURITY), 0).append(k(Keywords.DEFINER));
                break;
            case Characteristic.SQL_SECURITY_INVOKER:
                appendable.append(t(Token.KW_SQL)).append(k(Keywords.SECURITY), 0).append(k(Keywords.INVOKER));
                break;
        }
    }

    @Override
    public void visit(IndexDefinition node) {
        switch (node.getKeyType()) {
            case IndexDefinition.PRIMARY: {
                Identifier symbol = node.getSymbol();
                if (symbol != null) {
                    appendable.append(t(Token.KW_CONSTRAINT), 2);
                    print(symbol);
                    appendable.append(t(Token.KW_PRIMARY)).append(t(Token.KW_KEY), 0);
                } else {
                    appendable.append(' ').append(t(Token.KW_PRIMARY), 1).append(t(Token.KW_KEY), 0);
                }
                if (node.getIndexType() != null) {
                    switch (node.getIndexType()) {
                        case IndexOption.BTREE:
                            appendable.append(k(Keywords.BTREE), 2);
                            break;
                        case IndexOption.HASH:
                            appendable.append(k(Keywords.HASH), 2);
                            break;
                    }
                }
                break;
            }
            case IndexDefinition.UNIQUE: {
                Identifier symbol = node.getSymbol();
                if (symbol != null) {
                    appendable.append(t(Token.KW_CONSTRAINT), 2);
                    print(symbol);
                    appendable.append(t(Token.KW_UNIQUE)).append(t(Token.KW_KEY), 0);
                } else {
                    appendable.append(' ').append(t(Token.KW_UNIQUE), 1).append(t(Token.KW_KEY), 0);
                }
                print(node.getIndexName());
                if (node.getIndexType() != null) {
                    switch (node.getIndexType()) {
                        case IndexOption.BTREE:
                            appendable.append(t(Token.KW_USING), 0).append(k(Keywords.BTREE), 2);
                            break;
                        case IndexOption.HASH:
                            appendable.append(t(Token.KW_USING), 0).append(k(Keywords.HASH), 2);
                            break;
                    }
                }
                break;
            }
            case IndexDefinition.KEY: {
                appendable.append(t(Token.KW_KEY), 2);
                print(node.getIndexName());
                if (node.getIndexType() != null) {
                    switch (node.getIndexType()) {
                        case IndexOption.BTREE:
                            appendable.append(t(Token.KW_USING), 0).append(k(Keywords.BTREE), 2);
                            break;
                        case IndexOption.HASH:
                            appendable.append(t(Token.KW_USING), 0).append(k(Keywords.HASH), 2);
                            break;
                    }
                }
                break;
            }
            case IndexDefinition.INDEX: {
                appendable.append(t(Token.KW_INDEX), 2);
                print(node.getIndexName());
                if (node.getIndexType() != null) {
                    switch (node.getIndexType()) {
                        case IndexOption.BTREE:
                            appendable.append(t(Token.KW_USING), 0).append(k(Keywords.BTREE), 2);
                            break;
                        case IndexOption.HASH:
                            appendable.append(t(Token.KW_USING), 0).append(k(Keywords.HASH), 2);
                            break;
                    }
                }
                break;
            }
            case IndexDefinition.FULLTEXT:
                appendable.append(t(Token.KW_FULLTEXT)).append(t(Token.KW_KEY), 0);
                print(node.getIndexName());
                break;
            case IndexDefinition.SPATIAL:
                appendable.append(t(Token.KW_SPATIAL)).append(t(Token.KW_KEY), 0);
                print(node.getIndexName());
                break;
        }
        appendable.append('(');
        List<IndexColumnName> cols = node.getColumns();
        for (int i = 0, size = cols.size(); i < size; i++) {
            if (i != 0) {
                appendable.append(',');
            }
            print(cols.get(i));
        }
        appendable.append(')');
        print(node.getOptions());
    }

    @Override
    public void visit(IndexOption node) {
        if (node.getKeyBlockSize() != null) {
            appendable.append(k(Keywords.KEY_BLOCK_SIZE), 1).append('=');
            print(node.getKeyBlockSize());
        } else if (node.getIndexType() != null) {
            appendable.append(t(Token.KW_USING), 1);
            switch (node.getIndexType()) {
                case IndexOption.BTREE:
                    appendable.append(k(Keywords.BTREE), 1);
                    break;
                case IndexOption.HASH:
                    appendable.append(k(Keywords.HASH), 1);
                    break;
            }
        } else if (node.getParserName() != null) {
            appendable.append(t(Token.KW_WITH), 1).append(k(Keywords.PARSER), 0);
            print(node.getParserName());
        } else if (node.getComment() != null) {
            appendable.append(k(Keywords.COMMENT), 0);
            print(node.getComment());
        } else if (node.getVisible() != null) {
            switch (node.getVisible()) {
                case IndexOption.VISIBLE:
                    appendable.append(k(Keywords.VISIBLE), 1);
                    break;
                case IndexOption.INVISIBLE:
                    appendable.append(k(Keywords.INVISIBLE), 1);
                    break;
            }
        }
    }

    @Override
    public void visit(ForeignKeyDefinition node) {
        Identifier symbol = node.getSymbol();
        if (symbol != null) {
            appendable.append(t(Token.KW_CONSTRAINT), 2);
            print(symbol);
            appendable.append(t(Token.KW_FOREIGN)).append(t(Token.KW_KEY), 0);
        } else {
            appendable.append(' ').append(t(Token.KW_FOREIGN), 1).append(t(Token.KW_KEY), 0);
        }
        print(node.getIndexName());
        appendable.append('(');
        List<Identifier> cols = node.getColumns();
        for (int i = 0, size = cols.size(); i < size; i++) {
            if (i != 0) {
                appendable.append(',');
            }
            print(cols.get(i));
        }
        appendable.append(')');
        print(node.getReferenceDefinition());
    }

    @Override
    public void visit(TableOptions node) {
        Expression exp = node.getAutoIncrement();
        if (exp != null) {
            appendable.append(k(Keywords.AUTO_INCREMENT)).append('=');
            print(exp);
            appendable.append(" ");
        }
        exp = node.getAvgRowLength();
        if (exp != null) {
            appendable.append(k(Keywords.AVG_ROW_LENGTH)).append('=');
            print(exp);
            appendable.append(" ");
        }
        Identifier engine = node.getEngine();
        if (engine != null) {
            appendable.append(k(Keywords.ENGINE)).append('=');
            print(engine);
            appendable.append(" ");
        }
        Identifier id = node.getCharset();
        if (id != null) {
            appendable.append(t(Token.KW_DEFAULT)).append(t(Token.KW_CHARACTER), 1).append(t(Token.KW_SET), 1)
                .append('=');
            print(id);
            appendable.append(" ");
        }
        Integer checkSum = node.getChecksum();
        if (checkSum != null) {
            appendable.append(k(Keywords.CHECKSUM)).append('=').append(checkSum);
            appendable.append(" ");
        }
        id = node.getCollate();
        if (id != null) {
            appendable.append(t(Token.KW_DEFAULT)).append(t(Token.KW_COLLATE), 1).append('=');
            print(id);
            appendable.append(" ");
        }
        LiteralString string = node.getComment();
        if (string != null) {
            appendable.append(k(Keywords.COMMENT)).append('=');
            print(string);
            appendable.append(" ");
        }
        LiteralString compression = node.getCompression();
        if (compression != null) {
            appendable.append(k(Keywords.COMPRESSION)).append('=');
            print(compression);
            appendable.append(" ");
        }
        string = node.getConnection();
        if (string != null) {
            appendable.append(k(Keywords.CONNECTION)).append('=');
            print(string);
            appendable.append(" ");
        }
        string = node.getDataDirectory();
        if (string != null) {
            appendable.append(k(Keywords.DATA), 2).append(k(Keywords.DIRECTORY)).append('=');
            print(string);
            appendable.append(" ");
        }
        string = node.getIndexDirectory();
        if (string != null) {
            appendable.append(t(Token.KW_INDEX), 2).append(k(Keywords.DIRECTORY)).append('=');
            print(string);
            appendable.append(" ");
        }
        Integer delayKeyWrite = node.getDelayKeyWrite();
        if (delayKeyWrite != null) {
            appendable.append(k(Keywords.DELAY_KEY_WRITE)).append('=').append(delayKeyWrite);
            appendable.append(" ");
        }
        Boolean encryption = node.getEncryption();
        if (encryption != null) {
            appendable.append(k(Keywords.ENCRYPTION)).append('=').append(encryption ? "'Y'" : "'N'");
            appendable.append(" ");
        }
        Integer insertMethod = node.getInsertMethod();
        if (insertMethod != null) {
            appendable.append(k(Keywords.INSERT_METHOD)).append('=');
            switch (insertMethod) {
                case TableOptions.INSERT_METHOD_FIRST:
                    appendable.append(k(Keywords.FIRST));
                    break;
                case TableOptions.INSERT_METHOD_LAST:
                    appendable.append(k(Keywords.LAST));
                    break;
                case TableOptions.INSERT_METHOD_NO:
                    appendable.append(k(Keywords.NO));
                    break;
            }
            appendable.append(" ");
        }
        exp = node.getKeyBlockSize();
        if (exp != null) {
            appendable.append(k(Keywords.KEY_BLOCK_SIZE)).append('=');
            print(exp);
            appendable.append(" ");
        }
        Long maxRows = node.getMaxRows();
        if (maxRows != null) {
            appendable.append(k(Keywords.MAX_ROWS)).append('=').append(maxRows);
            appendable.append(" ");
        }
        Long minRows = node.getMinRows();
        if (minRows != null) {
            appendable.append(k(Keywords.MIN_ROWS)).append('=').append(minRows);
            appendable.append(" ");
        }
        Integer packKeys = node.getPackKeys();
        if (packKeys != null) {
            appendable.append(k(Keywords.PACK_KEYS)).append('=');
            switch (packKeys) {
                case TableOptions._DEFAULT:
                    appendable.append(t(Token.KW_DEFAULT));
                    break;
                case TableOptions._0:
                    appendable.append(0);
                    break;
                case TableOptions._1:
                    appendable.append(1);
                    break;
                default:
                    break;
            }
            appendable.append(" ");
        }
        string = node.getPassword();
        if (string != null) {
            appendable.append(k(Keywords.PASSWORD)).append('=');
            print(string);
            appendable.append(" ");
        }
        Integer rowFormat = node.getRowFormat();
        if (rowFormat != null) {
            appendable.append(k(Keywords.ROW_FORMAT)).append('=');
            switch (rowFormat) {
                case TableOptions.ROW_FORMAT_DEFAULT:
                    appendable.append(t(Token.KW_DEFAULT));
                    break;
                case TableOptions.ROW_FORMAT_DYNAMIC:
                    appendable.append(k(Keywords.DYNAMIC));
                    break;
                case TableOptions.ROW_FORMAT_FIXED:
                    appendable.append(k(Keywords.FIXED));
                    break;
                case TableOptions.ROW_FORMAT_COMPRESSED:
                    appendable.append(k(Keywords.COMPRESSED));
                    break;
                case TableOptions.ROW_FORMAT_REDUNDANT:
                    appendable.append(k(Keywords.REDUNDANT));
                    break;
                case TableOptions.ROW_FORMAT_COMPACT:
                    appendable.append(k(Keywords.COMPACT));
                    break;
            }
            appendable.append(" ");
        }
        Integer inte = node.getStatsAutoRecalc();
        if (inte != null) {
            appendable.append(k(Keywords.STATS_AUTO_RECALC)).append('=');
            switch (inte) {
                case TableOptions._DEFAULT:
                    appendable.append(t(Token.KW_DEFAULT));
                    break;
                case TableOptions._0:
                    appendable.append('0');
                    break;
                case TableOptions._1:
                    appendable.append('1');
                    break;
            }
            appendable.append(" ");
        }
        inte = node.getStatsPresistent();
        if (inte != null) {
            appendable.append(k(Keywords.STATS_PERSISTENT)).append('=');
            switch (inte) {
                case TableOptions._DEFAULT:
                    appendable.append(t(Token.KW_DEFAULT));
                    break;
                case TableOptions._0:
                    appendable.append('0');
                    break;
                case TableOptions._1:
                    appendable.append('1');
                    break;
            }
            appendable.append(" ");
        }
        exp = node.getStatSamplePages();
        if (exp != null) {
            appendable.append(k(Keywords.STATS_SAMPLE_PAGES)).append('=');
            print(exp);
            appendable.append(" ");
        }
        id = node.getTablespace();
        if (id != null) {
            appendable.append(k(Keywords.TABLESPACE), 2);
            print(id);
            inte = node.getStorage();
            if (inte != null) {
                appendable.append(k(Keywords.STORAGE), 0);
                switch (inte) {
                    case TableOptions.STORAGE_DISK:
                        appendable.append(k(Keywords.DISK));
                        break;
                    case TableOptions.STORAGE_MEMORY:
                        appendable.append(k(Keywords.MEMORY));
                        break;
                    // case TableOptions.STORAGE_DEFAULT:
                    // appendable.append(t(Token.KW_DEFAULT), 1);
                    // break;
                }
                appendable.append(" ");
            }
        }
        List<Identifier> union = node.getUnion();
        if (union != null && !union.isEmpty()) {
            appendable.append(t(Token.KW_UNION)).append('=').append('(');
            for (int i = 0, size = union.size(); i < size; i++) {
                if (i != 0) {
                    appendable.append(',');
                }
                print(union.get(i));
            }
            appendable.append(')');
            appendable.append(" ");
        }
    }

    @Override
    public void visit(Algorithm node) {
        appendable.append(k(Keywords.ALGORITHM)).append('=');
        switch (node.getType()) {
            case Algorithm.DEFAULT:
                appendable.append(t(Token.KW_DEFAULT));
                break;
            case Algorithm.INSTANT:
                appendable.append("INSTANT");
                break;
            case Algorithm.INPLACE:
                appendable.append("INPLACE");
                break;
            case Algorithm.COPY:
                appendable.append("COPY");
                break;
        }
    }

    @Override
    public void visit(Lock node) {
        appendable.append(t(Token.KW_LOCK)).append('=');
        switch (node.getType()) {
            case Lock.DEFAULT:
                appendable.append(t(Token.KW_DEFAULT));
                break;
            case Lock.NONE:
                appendable.append(k(Keywords.NONE));
                break;
            case Lock.SHARED:
                appendable.append("SHARED");
                break;
            case Lock.EXCLUSIVE:
                appendable.append("EXCLUSIVE");
                break;
        }
    }

    @Override
    public void visit(AuthOption node) {
        if (node.isDiscard()) {
            appendable.append(k(Keywords.DISCARD)).append(k(Keywords.OLD), 0).append(k(Keywords.PASSWORD));
        } else if (node.getAuthPlugin() != null) {
            appendable.append(k(Keywords.IDENTIFIED)).append(t(Token.KW_WITH), 0);
            print(node.getAuthPlugin());
            if (node.getAuthString() != null) {
                appendable.append(t(Token.KW_BY), 0);
                print(node.getAuthString());
                if (node.getCurrentAuthString() != null) {
                    appendable.append(t(Token.KW_REPLACE), 0);
                    print(node.getCurrentAuthString());
                }
                if (node.getRetainCurrent() != null) {
                    appendable.append(k(Keywords.RETAIN), 1).append(k(Keywords.CURRENT), 0)
                        .append(k(Keywords.PASSWORD));
                }
            } else if (node.getHashString() != null) {
                appendable.append(t(Token.KW_AS), 0);
                print(node.getHashString());
            }
        } else {
            appendable.append(k(Keywords.IDENTIFIED)).append(t(Token.KW_BY), 0);
            print(node.getAuthString());
            if (node.getCurrentAuthString() != null) {
                appendable.append(t(Token.KW_REPLACE), 0);
                print(node.getCurrentAuthString());
            }
            if (node.getRetainCurrent() != null) {
                appendable.append(k(Keywords.RETAIN), 1).append(k(Keywords.CURRENT), 0).append(k(Keywords.PASSWORD));
            }
        }
    }

    @Override
    public void visit(BeginStatement node) {
        appendable.append(k(Keywords.BEGIN));
        if (node.isWork()) {
            appendable.append(k(Keywords.WORK), 1);
        }
    }

    @Override
    public void visit(BeginEndStatement node) {
        if (node.getBeginLabel() != null) {
            print(node.getBeginLabel());
            appendable.append(':');
        }
        appendable.append(k(Keywords.BEGIN)).append('\n');
        List<SQLStatement> stmts = node.getStatements();
        for (SQLStatement stmt : stmts) {
            print(stmt);
            appendable.isLastSemicolon();
            appendable.append(';').append('\n');
        }
        appendable.append(k(Keywords.END));
        if (node.getEndLabel() != null) {
            appendable.append(' ');
            print(node.getEndLabel());
        }
        appendable.append(';');
    }

    @Override
    public void visit(DeclareStatement node) {
        appendable.append(t(Token.KW_DECLARE), 2);
        printList(node.getVarNames());
        appendable.append(' ');
        print(node.getDataType());
        if (node.getDefaultVal() != null) {
            appendable.append(t(Token.KW_DEFAULT), 0);
            print(node.getDefaultVal());
        }
    }

    @Override
    public void visit(SetTransactionStatement node) {
        appendable.append(t(Token.KW_SET));
        if (node.isGlobal() != null) {
            if (node.isGlobal()) {
                appendable.append(k(Keywords.GLOBAL), 1);
            } else {
                appendable.append(k(Keywords.SESSION), 1);
            }
        }
        appendable.append(k(Keywords.TRANSACTION), 0);
        List<Integer> list = node.getCharacteristics();
        boolean first = true;
        for (Integer li : list) {
            if (first) {
                first = false;
            } else {
                appendable.append(',');
            }
            switch (li) {
                case SetTransactionStatement.READ_UNCOMMITTED:
                    appendable.append(k(Keywords.ISOLATION)).append(k(Keywords.LEVEL), 0);
                    appendable.append(t(Token.KW_READ)).append(k(Keywords.UNCOMMITTED), 1);
                    break;
                case SetTransactionStatement.READ_COMMITTED:
                    appendable.append(k(Keywords.ISOLATION)).append(k(Keywords.LEVEL), 0);
                    appendable.append(t(Token.KW_READ)).append(k(Keywords.COMMITTED), 1);
                    break;
                case SetTransactionStatement.REPEATABLE_READ:
                    appendable.append(k(Keywords.ISOLATION)).append(k(Keywords.LEVEL), 0);
                    appendable.append(k(Keywords.REPEATABLE)).append(t(Token.KW_READ), 1);
                    break;
                case SetTransactionStatement.SERIALIZABLE:
                    appendable.append(k(Keywords.ISOLATION)).append(k(Keywords.LEVEL), 0);
                    appendable.append(k(Keywords.SERIALIZABLE));
                    break;
                case SetTransactionStatement.READ_WRITE:
                    appendable.append(t(Token.KW_READ)).append(t(Token.KW_WRITE), 1);
                    break;
                case SetTransactionStatement.READ_ONLY:
                    appendable.append(t(Token.KW_READ)).append(k(Keywords.ONLY), 1);
                    break;
            }
        }
    }

    @Override
    public void visit(CaseStatement node) {
        appendable.append(t(Token.KW_CASE), 2);
        print(node.getCaseValue());
        for (Pair<Expression, SQLStatement> li : node.getWhenList()) {
            appendable.append(t(Token.KW_WHEN), 0);
            print(li.getKey());
            appendable.append(t(Token.KW_THEN), 0);
            print(li.getValue());
            appendable.append(';');
        }
        if (node.getElseStmt() != null) {
            appendable.append(t(Token.KW_ELSE), 0);
            print(node.getElseStmt());
            appendable.isLastSemicolon();
            appendable.append(';');
        }
        appendable.append(k(Keywords.END), 0).append(t(Token.KW_CASE)).append(';');
    }

    @Override
    public void visit(IfStatement node) {
        appendable.append(t(Token.KW_IF), 2);
        boolean first = true;
        for (Pair<Expression, List<SQLStatement>> pair : node.getIfStatements()) {
            if (first) {
                first = false;
            } else {
                appendable.append(t(Token.KW_ELSEIF), 0);
            }
            print(pair.getKey());
            appendable.append(t(Token.KW_THEN), 0);
            for (SQLStatement stmt : pair.getValue()) {
                print(stmt);
                appendable.append(';');
            }
        }
        if (node.getElseStatement() != null) {
            appendable.append(t(Token.KW_ELSE), 0);
            for (SQLStatement stmt : node.getElseStatement()) {
                print(stmt);
                appendable.append(';');
            }
        }
        appendable.append(k(Keywords.END), 0).append(t(Token.KW_IF)).append(';');
    }

    @Override
    public void visit(IterateStatement node) {
        appendable.append(t(Token.KW_ITERATE), 2);
        print(node.getLabel());
        appendable.append(';');
    }

    @Override
    public void visit(LeaveStatement node) {
        appendable.append(t(Token.KW_LEAVE), 2);
        print(node.getLabel());
        appendable.append(';');
    }

    @Override
    public void visit(LoopStatement node) {
        if (node.getBeginLabel() != null) {
            print(node.getBeginLabel());
            appendable.append(':');
        }
        appendable.append(t(Token.KW_LOOP)).append('\n');
        printList(node.getStatements(), ';');
        appendable.append(';').append('\n');
        appendable.append(k(Keywords.END)).append(t(Token.KW_LOOP), 1);
        if (node.getEndLabel() != null) {
            appendable.append(' ');
            print(node.getEndLabel());
        }
        appendable.append(';');
    }

    @Override
    public void visit(RepeatStatement node) {
        if (node.getBeginLabel() != null) {
            print(node.getBeginLabel());
            appendable.append(':');
        }
        appendable.append(t(Token.KW_REPEAT)).append('\n');
        printList(node.getStatements(), ';');
        appendable.append(';').append('\n');
        appendable.append(k(Keywords.UNTIL), 2);
        print(node.getUtilCondition());
        appendable.append('\n');
        appendable.append(k(Keywords.END)).append(t(Token.KW_REPEAT), 1);
        if (node.getEndLabel() != null) {
            appendable.append(' ');
            print(node.getEndLabel());
        }
        appendable.append(';');
    }

    @Override
    public void visit(ReturnStatement node) {
        appendable.append(t(Token.KW_RETURN), 2);
        print(node.getLabel());
        appendable.append(';');
    }

    @Override
    public void visit(WhileStatement node) {
        if (node.getBeginLabel() != null) {
            print(node.getBeginLabel());
            appendable.append(':');
        }
        appendable.append(t(Token.KW_WHILE), 2);
        print(node.getWhileCondition());
        appendable.append(k(Keywords.DO), 1);
        appendable.append('\n');
        printList(node.getStatements(), ';');
        appendable.append(';').append('\n');
        appendable.append(k(Keywords.END)).append(t(Token.KW_WHILE), 1);
        if (node.getEndLabel() != null) {
            appendable.append(' ');
            print(node.getEndLabel());
        }
        appendable.append(';');
    }

    protected void printIntervalUnit(int interval) {
        switch (interval) {
            case IntervalUnit.DAY:
                appendable.append(k(Keywords.DAY));
                break;
            case IntervalUnit.DAY_HOUR:
                appendable.append(t(Token.KW_DAY_HOUR));
                break;
            case IntervalUnit.DAY_MICROSECOND:
                appendable.append(t(Token.KW_DAY_MICROSECOND));
                break;
            case IntervalUnit.DAY_MINUTE:
                appendable.append(t(Token.KW_DAY_MINUTE));
                break;
            case IntervalUnit.DAY_SECOND:
                appendable.append(t(Token.KW_DAY_SECOND));
                break;
            case IntervalUnit.HOUR:
                appendable.append(k(Keywords.HOUR));
                break;
            case IntervalUnit.HOUR_MICROSECOND:
                appendable.append(t(Token.KW_HOUR_MICROSECOND));
                break;
            case IntervalUnit.HOUR_MINUTE:
                appendable.append(t(Token.KW_HOUR_MINUTE));
                break;
            case IntervalUnit.HOUR_SECOND:
                appendable.append(t(Token.KW_HOUR_SECOND));
                break;
            case IntervalUnit.MICROSECOND:
                appendable.append(k(Keywords.MICROSECOND));
                break;
            case IntervalUnit.MINUTE:
                appendable.append(k(Keywords.MINUTE));
                break;
            case IntervalUnit.MINUTE_MICROSECOND:
                appendable.append(t(Token.KW_MINUTE_MICROSECOND));
                break;
            case IntervalUnit.MINUTE_SECOND:
                appendable.append(t(Token.KW_MINUTE_SECOND));
                break;
            case IntervalUnit.MONTH:
                appendable.append(k(Keywords.MONTH));
                break;
            case IntervalUnit.QUARTER:
                appendable.append(k(Keywords.QUARTER));
                break;
            case IntervalUnit.SECOND:
                appendable.append(k(Keywords.SECOND));
                break;
            case IntervalUnit.SECOND_MICROSECOND:
                appendable.append(t(Token.KW_SECOND_MICROSECOND));
                break;
            case IntervalUnit.WEEK:
                appendable.append(k(Keywords.WEEK));
                break;
            case IntervalUnit.YEAR:
                appendable.append(k(Keywords.YEAR));
                break;
            case IntervalUnit.YEAR_MONTH:
                appendable.append(t(Token.KW_YEAR_MONTH));
                break;
        }
    }

    @Override
    public void visit(ConditionValue node) {
        switch (node.getType()) {
            case ConditionValue.SQLSTATE:
                appendable.append(t(Token.KW_SQLSTATE), 2);
                print(node.getValue());
                break;
            case ConditionValue.MYSQL_ERROR_CODE:
            case ConditionValue.CONDITION_NAME:
                print(node.getValue());
                break;
            case ConditionValue.SQLWARNING:
                appendable.append(t(Token.KW_SQLWARNING));
                break;
            case ConditionValue.NOT_FOUND:
                appendable.append(t(Token.KW_NOT)).append(k(Keywords.FOUND), 1);
                break;
            case ConditionValue.SQLEXCEPTION:
                appendable.append(t(Token.KW_SQLEXCEPTION));
                break;
        }
    }

    @Override
    public void visit(DeclareConditionStatement node) {
        appendable.append(t(Token.KW_DECLARE), 2);
        print(node.getName());
        appendable.append(t(Token.KW_CONDITION), 0);
        appendable.append(t(Token.KW_FOR), 2);
        print(node.getValue());
    }

    @Override
    public void visit(DeclareHandlerStatement node) {
        appendable.append(t(Token.KW_DECLARE), 2);
        switch (node.getAction()) {
            case DeclareHandlerStatement.CONTINUE:
                appendable.append(t(Token.KW_CONTINUE), 2);
                break;
            case DeclareHandlerStatement.EXIT:
                appendable.append(t(Token.KW_EXIT), 2);
                break;
            case DeclareHandlerStatement.UNDO:
                appendable.append(t(Token.KW_UNDO), 2);
                break;
        }
        appendable.append(k(Keywords.HANDLER)).append(t(Token.KW_FOR), 0);
        printList(node.getConditionValues());
        appendable.append('\n');
        print(node.getStmt());
    }

    @Override
    public void visit(GetDiagnosticsStatement node) {
        appendable.append(t(Token.KW_GET));
        if (node.getIsCurrentOrStacked() != null) {
            if (node.getIsCurrentOrStacked()) {
                appendable.append(k(Keywords.CURRENT), 1);
            } else {
                appendable.append(k(Keywords.STACKED), 1);
            }
        }
        appendable.append(k(Keywords.DIAGNOSTICS), 0);
        List<Pair<Expression, Integer>> list = node.getStatementItems();
        if (list != null && !list.isEmpty()) {
            boolean first = true;
            for (Pair<Expression, Integer> li : list) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                print(li.getKey());
                appendable.append('=');
                switch (li.getValue()) {
                    case GetDiagnosticsStatement.NUMBER:
                        appendable.append(k(Keywords.NUMBER));
                        break;
                    case GetDiagnosticsStatement.ROW_COUNT:
                        appendable.append(k(Keywords.ROW_COUNT));
                        break;
                }
            }
        }
        if (node.getConditionNumber() != null) {
            appendable.append(t(Token.KW_CONDITION), 2);
            print(node.getConditionNumber());
            appendable.append(' ');
        }
        list = node.getConditionItems();
        if (list != null && !list.isEmpty()) {
            boolean first = true;
            for (Pair<Expression, Integer> li : list) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                print(li.getKey());
                appendable.append('=');
                printConditionInfoItem(li.getValue());
            }
        }
    }

    private void printConditionInfoItem(Integer value) {
        switch (value) {
            case GetDiagnosticsStatement.COND_CLASS_ORIGIN:
                appendable.append(k(Keywords.CLASS_ORIGIN));
                break;
            case GetDiagnosticsStatement.COND_SUBCLASS_ORIGIN:
                appendable.append(k(Keywords.SUBCLASS_ORIGIN));
                break;
            case GetDiagnosticsStatement.COND_RETURNED_SQLSTATE:
                appendable.append(k(Keywords.RETURNED_SQLSTATE));
                break;
            case GetDiagnosticsStatement.COND_MESSAGE_TEXT:
                appendable.append(k(Keywords.MESSAGE_TEXT));
                break;
            case GetDiagnosticsStatement.COND_MYSQL_ERRNO:
                appendable.append(k(Keywords.MYSQL_ERRNO));
                break;
            case GetDiagnosticsStatement.COND_CONSTRAINT_CATALOG:
                appendable.append(k(Keywords.CONSTRAINT_CATALOG));
                break;
            case GetDiagnosticsStatement.COND_CONSTRAINT_SCHEMA:
                appendable.append(k(Keywords.CONSTRAINT_SCHEMA));
                break;
            case GetDiagnosticsStatement.COND_CONSTRAINT_NAME:
                appendable.append(k(Keywords.CONSTRAINT_NAME));
                break;
            case GetDiagnosticsStatement.COND_CATALOG_NAME:
                appendable.append(k(Keywords.CATALOG_NAME));
                break;
            case GetDiagnosticsStatement.COND_SCHEMA_NAME:
                appendable.append(k(Keywords.SCHEMA_NAME));
                break;
            case GetDiagnosticsStatement.COND_TABLE_NAME:
                appendable.append(k(Keywords.TABLE_NAME));
                break;
            case GetDiagnosticsStatement.COND_COLUMN_NAME:
                appendable.append(k(Keywords.COLUMN_NAME));
                break;
            case GetDiagnosticsStatement.COND_CURSOR_NAME:
                appendable.append(k(Keywords.CURSOR_NAME));
                break;
        }
    }

    @Override
    public void visit(ResignalStatement node) {
        appendable.append(t(Token.KW_RESIGNAL));
        if (node.getConditionValue() != null) {
            appendable.append(' ');
            print(node.getConditionValue());
        }
        List<Pair<Integer, Literal>> list = node.getInformationItems();
        if (list != null && !list.isEmpty()) {
            appendable.append(t(Token.KW_SET), 0);
            boolean first = true;
            for (Pair<Integer, Literal> li : list) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                printConditionInfoItem(li.getKey());
                appendable.append('=');
                print(li.getValue());
            }
        }
    }

    @Override
    public void visit(SignalStatement node) {
        appendable.append(t(Token.KW_SIGNAL));
        if (node.getConditionValue() != null) {
            appendable.append(' ');
            print(node.getConditionValue());
        }
        List<Pair<Integer, Literal>> list = node.getInformationItems();
        if (list != null && !list.isEmpty()) {
            appendable.append(t(Token.KW_SET), 0);
            boolean first = true;
            for (Pair<Integer, Literal> li : list) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                printConditionInfoItem(li.getKey());
                appendable.append('=');
                print(li.getValue());
            }
        }
    }

    @Override
    public void visit(CursorCloseStatement node) {
        appendable.append(k(Keywords.CLOSE), 2);
        print(node.getName());
    }

    @Override
    public void visit(CursorDeclareStatement node) {
        appendable.append(t(Token.KW_DECLARE), 2);
        print(node.getName());
        appendable.append(t(Token.KW_CURSOR), 0).append(t(Token.KW_FOR), 2);
        print(node.getStmt());
    }

    @Override
    public void visit(CursorFetchStatement node) {
        appendable.append(t(Token.KW_FETCH), 2);
        print(node.getName());
        appendable.append(t(Token.KW_INTO), 0);
        printList(node.getVarNames());
    }

    @Override
    public void visit(CursorOpenStatement node) {
        appendable.append(k(Keywords.OPEN), 2);
        print(node.getName());
    }

    @Override
    public void visit(DDLAlterTableStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(t(Token.KW_TABLE), 0);
        print(node.getName());
        if (node.getAlters() != null && !node.getAlters().isEmpty()) {
            appendable.append(' ');
            printList(node.getAlters());
        }
        if (node.getPartitionOptions() != null) {
            appendable.append(' ');
            print(node.getPartitionOptions());
        }
    }

    @Override
    public void visit(DALAlterUserStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(k(Keywords.USER), 0);
        if (node.isIfExists()) {
            appendable.append(t(Token.KW_IF)).append(t(Token.KW_EXISTS), 0);
        }
        boolean first = true;
        for (Pair<Expression, AuthOption> p : node.getUsers()) {
            if (first) {
                first = false;
            } else {
                appendable.append(',');
            }
            print(p.getKey());
            appendable.append(' ');
            print(p.getValue());
        }
        if (node.getRequireNone() != null && node.getRequireNone()) {
            appendable.append(t(Token.KW_REQUIRE), 0).append(k(Keywords.NONE));
        } else if (node.getTlsOptions() != null) {
            appendable.append(t(Token.KW_REQUIRE), 0);
            first = true;
            for (Pair<Integer, LiteralString> p : node.getTlsOptions()) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(t(Token.KW_AND), 0);
                }
                switch (p.getKey()) {
                    case DALAlterUserStatement.TLS_SSL:
                        appendable.append(t(Token.KW_SSL));
                        break;
                    case DALAlterUserStatement.TLS_X509:
                        appendable.append(k(Keywords.X509));
                        break;
                    case DALAlterUserStatement.TLS_CIPHER:
                        appendable.append(k(Keywords.CIPHER), 2);
                        print(p.getValue());
                        break;
                    case DALAlterUserStatement.TLS_ISSUER:
                        appendable.append(k(Keywords.ISSUER), 2);
                        print(p.getValue());
                        break;
                    case DALAlterUserStatement.TLS_SUBJECT:
                        appendable.append(k(Keywords.SUBJECT), 2);
                        print(p.getValue());
                        break;
                }
            }
        }
        if (node.getResourceOptions() != null) {
            appendable.append(t(Token.KW_WITH), 1);
            for (Pair<Integer, Long> p : node.getResourceOptions()) {
                appendable.append(' ');
                switch (p.getKey()) {
                    case DALAlterUserStatement.MAX_QUERIES_PER_HOUR:
                        appendable.append(k(Keywords.MAX_QUERIES_PER_HOUR), 2);
                        break;
                    case DALAlterUserStatement.MAX_UPDATES_PER_HOUR:
                        appendable.append(k(Keywords.MAX_UPDATES_PER_HOUR), 2);
                        break;
                    case DALAlterUserStatement.MAX_CONNECTIONS_PER_HOUR:
                        appendable.append(k(Keywords.MAX_CONNECTIONS_PER_HOUR), 2);
                        break;
                    case DALAlterUserStatement.MAX_USER_CONNECTIONS:
                        appendable.append(k(Keywords.MAX_USER_CONNECTIONS), 2);
                        break;
                }
                appendable.append(p.getValue());
            }
        }
        Tuple3<Integer, Boolean, Long> option = node.getPasswordOption();
        if (option != null) {
            appendable.append(k(Keywords.PASSWORD), 0);
            switch (option._1()) {
                case DALAlterUserStatement.PASSWORD_EXPIRE:
                    appendable.append(k(Keywords.EXPIRE), 2);
                    if (option._2() != null) {
                        if (option._2()) {
                            appendable.append(t(Token.KW_DEFAULT));
                        } else {
                            appendable.append(k(Keywords.NEVER));
                        }
                    } else {
                        appendable.append(t(Token.KW_INTERVAL), 2);
                        print(option._3());
                        appendable.append(k(Keywords.DAY), 1);
                    }
                    break;
                case DALAlterUserStatement.PASSWORD_HISTORY:
                    appendable.append(k(Keywords.HISTORY), 2);
                    if (option._2() != null && option._2()) {
                        appendable.append(t(Token.KW_DEFAULT));
                    } else {
                        print(option._3());
                    }
                    break;
                case DALAlterUserStatement.PASSWORD_REUSE_INTERVAL:
                    appendable.append(k(Keywords.REUSE)).append(t(Token.KW_INTERVAL), 0);
                    if (option._2() != null && option._2()) {
                        appendable.append(t(Token.KW_DEFAULT));
                    } else {
                        print(option._3());
                        appendable.append(k(Keywords.DAY), 1);
                    }
                    break;
                case DALAlterUserStatement.PASSWORD_REQUIRE_CURRENT:
                    appendable.append(t(Token.KW_REQUIRE)).append(k(Keywords.CURRENT), 0);
                    if (option._2() != null) {
                        if (option._2()) {
                            appendable.append(t(Token.KW_DEFAULT));
                        } else {
                            appendable.append(k(Keywords.OPTIONAL));
                        }
                    }
                    break;
            }
        }
        if (node.getLock() != null) {
            if (node.getLock()) {
                appendable.append(k(Keywords.ACCOUNT), 0).append(t(Token.KW_LOCK));
            } else {
                appendable.append(k(Keywords.ACCOUNT), 0).append(t(Token.KW_UNLOCK));
            }
        }
    }

    @Override
    public void visit(DALAlterResourceGroupStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(k(Keywords.RESOURCE), 0)
            .append(t(Token.KW_GROUP), 2);
        print(node.getName());
        if (node.getVcpus() != null) {
            appendable.append(k(Keywords.VCPU), 1).append('=');
            printList(node.getVcpus());
        }
        if (node.getThreadPriority() != null) {
            appendable.append(k(Keywords.THREAD_PRIORITY), 1).append('=');
            print(node.getThreadPriority());
        }
        if (node.getEnable() != null) {
            if (node.getEnable()) {
                appendable.append(k(Keywords.ENABLE), 1);
            } else {
                appendable.append(k(Keywords.DISABLE), 1);
            }
            if (node.isForce()) {
                appendable.append(t(Token.KW_FORCE), 1);
            }
        }
    }

    @Override
    public void visit(PartitionOperation node) {
        switch (node.getType()) {
            case PartitionOperation.ADD:
                appendable.append(t(Token.KW_ADD)).append(t(Token.KW_PARTITION), 1).append('(');
                print(node.getDefinition());
                appendable.append(')');
                break;
            case PartitionOperation.DROP:
                appendable.append(t(Token.KW_DROP)).append(t(Token.KW_PARTITION), 0);
                printList(node.getNames());
                break;
            case PartitionOperation.DISCARD:
                appendable.append(k(Keywords.DISCARD)).append(t(Token.KW_PARTITION), 0);
                if (node.isAll()) {
                    appendable.append(t(Token.KW_ALL));
                } else {
                    printList(node.getNames());
                }
                appendable.append(k(Keywords.TABLESPACE), 1);
                break;
            case PartitionOperation.IMPORT:
                appendable.append(k(Keywords.IMPORT)).append(t(Token.KW_PARTITION), 0);
                if (node.isAll()) {
                    appendable.append(t(Token.KW_ALL));
                } else {
                    printList(node.getNames());
                }
                appendable.append(k(Keywords.TABLESPACE), 1);
                break;
            case PartitionOperation.TRUNCATE:
                appendable.append(k(Keywords.TRUNCATE)).append(t(Token.KW_PARTITION), 0);
                if (node.isAll()) {
                    appendable.append(t(Token.KW_ALL));
                } else {
                    printList(node.getNames());
                }
                break;
            case PartitionOperation.COALESCE:
                appendable.append(k(Keywords.COALESCE)).append(t(Token.KW_PARTITION), 0);
                print(node.getNumber());
                break;
            case PartitionOperation.REORGANIZE:
                appendable.append(k(Keywords.REORGANIZE)).append(t(Token.KW_PARTITION), 0);
                printList(node.getNames());
                appendable.append(t(Token.KW_INTO)).append('(');
                printList(node.getDefinitions());
                appendable.append(')');
                break;
            case PartitionOperation.EXCHANGE:
                appendable.append(k(Keywords.EXCHANGE)).append(t(Token.KW_PARTITION), 0);
                print(node.getNames().get(0));
                appendable.append(t(Token.KW_WITH), 0).append(t(Token.KW_TABLE), 2);
                print(node.getTable());
                if (node.getWithValidation() != null) {
                    appendable.append(' ').append(
                        node.getWithValidation() ? t(Token.KW_WITH) : k(Keywords.WITHOUT), 2)
                        .append(k(Keywords.VALIDATION));
                }
                break;
            case PartitionOperation.ANALYZE:
                appendable.append("ANALYZE").append(t(Token.KW_PARTITION), 0);
                if (node.isAll()) {
                    appendable.append(t(Token.KW_ALL));
                } else {
                    printList(node.getNames());
                }
                break;
            case PartitionOperation.CHECK:
                appendable.append(t(Token.KW_CHECK)).append(t(Token.KW_PARTITION), 0);
                if (node.isAll()) {
                    appendable.append(t(Token.KW_ALL));
                } else {
                    printList(node.getNames());
                }
                break;
            case PartitionOperation.OPTIMIZE:
                appendable.append(t(Token.KW_OPTIMIZE)).append(t(Token.KW_PARTITION), 0);
                if (node.isAll()) {
                    appendable.append(t(Token.KW_ALL));
                } else {
                    printList(node.getNames());
                }
                break;
            case PartitionOperation.REBUILD:
                appendable.append(k(Keywords.REBUILD)).append(t(Token.KW_PARTITION), 0);
                if (node.isAll()) {
                    appendable.append(t(Token.KW_ALL));
                } else {
                    printList(node.getNames());
                }
                break;
            case PartitionOperation.REPAIR:
                appendable.append(k(Keywords.REPAIR)).append(t(Token.KW_PARTITION), 0);
                if (node.isAll()) {
                    appendable.append(t(Token.KW_ALL));
                } else {
                    printList(node.getNames());
                }
                break;
            case PartitionOperation.REMOVE:
                appendable.append(k(Keywords.REMOVE)).append(k(Keywords.PARTITIONING), 1);
                break;
            case PartitionOperation.UPGRADE:
                appendable.append(k(Keywords.UPGRADE)).append(k(Keywords.PARTITIONING), 1);
                break;
        }
    }

    @Override
    public void visit(RenameColumn node) {
        appendable.append(t(Token.KW_RENAME)).append(t(Token.KW_COLUMN), 0);
        print(node.getOldColumn());
        appendable.append(t(Token.KW_TO), 0);
        print(node.getNewColumn());
    }

    @Override
    public void visit(RenameIndex node) {
        appendable.append(t(Token.KW_RENAME)).append(t(Token.KW_INDEX), 0);
        print(node.getOldIndex());
        appendable.append(t(Token.KW_TO), 0);
        print(node.getNewIndex());
    }

    @Override
    public void visit(RenameTo node) {
        appendable.append(t(Token.KW_RENAME)).append(t(Token.KW_TO), 0);
        print(node.getName());
    }

    @Override
    public void visit(WithValidation node) {
        if (node.isWithout()) {
            appendable.append(k(Keywords.WITHOUT), 2);
        } else {
            appendable.append(t(Token.KW_WITH), 2);
        }
        appendable.append(k(Keywords.VALIDATION));
    }

    @Override
    public void visit(ImportTablespace node) {
        appendable.append(node.isImport() ? k(Keywords.IMPORT) : k(Keywords.DISCARD), 2)
            .append(k(Keywords.TABLESPACE));
    }

    @Override
    public void visit(EnableKeys node) {
        appendable.append(node.isEnable() ? k(Keywords.ENABLE) : k(Keywords.DISABLE), 2)
            .append(t(Token.KW_KEYS));
    }

    @Override
    public void visit(Force node) {
        appendable.append(t(Token.KW_FORCE));
    }

    @Override
    public void visit(AddColumn node) {
        appendable.append(t(Token.KW_ADD)).append(t(Token.KW_COLUMN), 0);
        List<Pair<Identifier, ColumnDefinition>> columns = node.getColumns();
        if (columns.size() == 1) {
            Pair<Identifier, ColumnDefinition> pair = columns.get(0);
            print(pair.getKey());
            appendable.append(' ');
            print(pair.getValue());
            if (node.getFirst() != null) {
                appendable.append(node.getFirst().getKey() ? k(Keywords.FIRST) : k(Keywords.AFTER),
                    0);
                print(node.getFirst().getValue());
            }
        } else {
            boolean first = true;
            for (Pair<Identifier, ColumnDefinition> pair : columns) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                print(pair.getKey());
                appendable.append(' ');
                print(pair.getValue());
            }
        }
    }

    @Override
    public void visit(AddKey node) {
        appendable.append(t(Token.KW_ADD), 2);
        print(node.getDefinition());
    }

    @Override
    public void visit(AddCheckConstraintDefinition node) {
        appendable.append(t(Token.KW_ADD));
        Tuple3<Identifier, Expression, Boolean> tuple = node.getCheckConstraintDefinition();
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
    public void visit(AddForeignKey node) {
        appendable.append(t(Token.KW_ADD), 2);
        print(node.getDefinition());
    }

    @Override
    public void visit(DropCheckConstraintDefination node) {
        appendable.append(t(Token.KW_DROP), 2).append(t(Token.KW_CHECK), 2);
        print(node.getName());
    }

    @Override
    public void visit(DropColumn node) {
        appendable.append(t(Token.KW_DROP)).append(t(Token.KW_COLUMN), 0);
        print(node.getName());
    }

    @Override
    public void visit(DropForeignKey node) {
        appendable.append(t(Token.KW_DROP)).append(t(Token.KW_FOREIGN), 0).append(t(Token.KW_KEY),
            2);
        print(node.getName());
    }

    @Override
    public void visit(DropIndex node) {
        appendable.append(t(Token.KW_DROP)).append(t(Token.KW_INDEX), 0);
        print(node.getName());
    }

    @Override
    public void visit(DropPrimaryKey node) {
        appendable.append(t(Token.KW_DROP)).append(t(Token.KW_PRIMARY), 0).append(t(Token.KW_KEY));
    }
    @Override
    public void visit(ModifyColumn node) {
        appendable.append(k(Keywords.MODIFY)).append(t(Token.KW_COLUMN), 0);
        print(node.getName());
        appendable.append(' ');
        print(node.getDefinition());
        if (node.getFirst() != null) {
            appendable.append(node.getFirst().getKey() ? k(Keywords.FIRST) : k(Keywords.AFTER), 0);
            print(node.getFirst().getValue());
        }
    }

    @Override
    public void visit(OrderByColumns node) {
        appendable.append(t(Token.KW_ORDER)).append(t(Token.KW_BY), 0);
        boolean first = true;
        for (Identifier col : node.getColumns()) {
            if (first) {
                first = false;
            } else {
                appendable.append(',');
            }
            print(col);
        }
    }

    @Override
    public void visit(ChangeColumn node) {
        appendable.append(t(Token.KW_CHANGE)).append(t(Token.KW_COLUMN), 0);
        print(node.getOldColumn());
        appendable.append(' ');
        print(node.getNewColumn());
        appendable.append(' ');
        print(node.getDefinition());
        if (node.getFirst() != null) {
            appendable.append(node.getFirst().getKey() ? k(Keywords.FIRST) : k(Keywords.AFTER), 0);
            print(node.getFirst().getValue());
        }
    }

    @Override
    public void visit(ConvertCharacterSet node) {
        if (node.getConverOrDefault()) {
            appendable.append(t(Token.KW_CONVERT)).append(t(Token.KW_TO), 0);
            appendable.append(t(Token.KW_CHARACTER)).append(t(Token.KW_SET), 0);
            print(node.getCharset());
            if (node.getCollate() != null) {
                appendable.append(t(Token.KW_COLLATE), 0);
                print(node.getCollate());
            }
        } else {
            appendable.append(t(Token.KW_DEFAULT), 2);
            appendable.append(t(Token.KW_CHARACTER)).append(t(Token.KW_SET), 0).append('=');
            print(node.getCharset());
            if (node.getCollate() != null) {
                appendable.append(t(Token.KW_COLLATE), 0).append('=');
                print(node.getCollate());
            }
        }
    }


    @Override
    public void visit(AlterCheckConstraintDefination node) {
        appendable.append(t(Token.KW_ALTER), 2).append(t(Token.KW_CHECK), 2);
        print(node.getName());
        if (!node.getEnforced()) {
            appendable.append(t(Token.KW_NOT), 2);
        }
        appendable.append("ENFORCED");
    }

    @Override
    public void visit(AlterCharacterSet node) {
        appendable.append(t(Token.KW_DEFAULT)).append(t(Token.KW_CHARACTER), 0)
            .append(t(Token.KW_SET)).append('=');
        if (node.getCharset() != null) {
            print(node.getCharset());
        }
        if (node.getCollate() != null) {
            appendable.append(t(Token.KW_COLLATE), 0);
            print(node.getCollate());
        }
    }

    @Override
    public void visit(AlterColumn node) {
        appendable.append(t(Token.KW_ALTER)).append(t(Token.KW_COLUMN), 0);
        print(node.getName());
        if (node.isDropDefault()) {
            appendable.append(t(Token.KW_DROP), 0).append(t(Token.KW_DEFAULT));
        } else {
            appendable.append(t(Token.KW_SET), 0).append(t(Token.KW_DEFAULT), 2);
            print(node.getDefaultVal());
        }
    }

    @Override
    public void visit(AlterIndex node) {
        appendable.append(t(Token.KW_ALTER)).append(t(Token.KW_INDEX), 0);
        print(node.getName());
        if (node.isVisible()) {
            appendable.append(k(Keywords.VISIBLE), 1);
        } else {
            appendable.append(k(Keywords.INVISIBLE), 1);
        }
    }

    @Override
    public void visit(DDLAlterDatabaseStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(t(Token.KW_DATABASE), 0);
        print(node.getDb());
        if (node.getCharset() != null) {
            appendable.append(t(Token.KW_CHARACTER), 0).append(t(Token.KW_SET), 2);
            print(node.getCharset());
        }
        if (node.getCollate() != null) {
            appendable.append(t(Token.KW_COLLATE), 0);
            print(node.getCollate());
        }
        if (node.isEncryption() != null) {
            if (node.isEncryption()) {
                appendable.append(t(Token.KW_DEFAULT), 0).append(k(Keywords.ENCRYPTION), 2)
                    .append("'Y'");
            } else {
                appendable.append(t(Token.KW_DEFAULT), 0).append(k(Keywords.ENCRYPTION), 2)
                    .append("'N'");
            }
        }
    }

    @Override
    public void visit(DDLAlterEventStatement node) {
        appendable.append(t(Token.KW_ALTER), 2);
        if (node.getDefiner() != null) {
            appendable.append(k(Keywords.DEFINER)).append('=');
            print(node.getDefiner());
            appendable.append(' ');
        }
        appendable.append(k(Keywords.EVENT), 2);
        print(node.getEvent());
        if (node.getSchedule() != null) {
            appendable.append(' ');
            appendable.append(t(Token.KW_ON), 2);
            appendable.append(k(Keywords.SCHEDULE), 2);
            print(node.getSchedule());
        }
        if (node.getPreserve() != null) {
            appendable.append(' ');
            appendable.append(t(Token.KW_ON), 2);
            appendable.append(k(Keywords.COMPLETION), 2);
            if (!node.getPreserve()) {
                appendable.append(t(Token.KW_NOT), 2);
            }
            appendable.append(k(Keywords.PRESERVE));
        }
        if (node.getRenameTo() != null) {
            appendable.append(' ');
            appendable.append(t(Token.KW_RENAME), 2);
            appendable.append(t(Token.KW_TO), 2);
            print(node.getRenameTo());
        }
        if (node.getEnableType() != null) {
            appendable.append(' ');
            switch (node.getEnableType()) {
                case DDLAlterEventStatement.ENABLE:
                    appendable.append(k(Keywords.ENABLE));
                    break;
                case DDLAlterEventStatement.DISABLE:
                    appendable.append(k(Keywords.DISABLE));
                    break;
                case DDLAlterEventStatement.DISABLE_ON_SLAVE:
                    appendable.append(k(Keywords.DISABLE));
                    appendable.append(t(Token.KW_ON), 0);
                    appendable.append(k(Keywords.SLAVE));
                    break;
            }
        }
        if (node.getComment() != null) {
            appendable.append(k(Keywords.COMMENT), 0);
            print(node.getComment());
        }
        if (node.getEventBody() != null) {
            appendable.append(k(Keywords.DO), 0);
            print(node.getEventBody());
        }
    }

    @Override
    public void visit(DDLAlterFunctionStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(t(Token.KW_FUNCTION), 0);
        print(node.getName());
        List<Characteristic> list = node.getCharacteristics();
        if (list != null && !list.isEmpty()) {
            appendable.append(' ');
            printList(list, ' ');
        }
    }

    @Override
    public void visit(DDLAlterInstanceStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(k(Keywords.INSTANCE), 0);
        switch (node.getType()) {
            case DDLAlterInstanceStatement.ROTATE_INNODB:
                appendable.append(k(Keywords.ROTATE), 2).append("INNODB")
                    .append(k(Keywords.MASTER), 0).append(t(Token.KW_KEY));
                break;
            case DDLAlterInstanceStatement.ROTATE_BINLOG:
                appendable.append(k(Keywords.ROTATE), 2).append(k(Keywords.BINLOG))
                    .append(k(Keywords.MASTER), 0).append(t(Token.KW_KEY));
                break;
            case DDLAlterInstanceStatement.RELOAD_TLS:
                appendable.append(k(Keywords.RELOAD), 2).append("TLS");
                break;
            case DDLAlterInstanceStatement.RELOAD_TLS_NO:
                appendable.append(k(Keywords.RELOAD), 2).append("TLS").append(k(Keywords.NO), 0)
                    .append(k(Keywords.ROLLBACK), 2).append(t(Token.KW_ON), 2)
                    .append(k(Keywords.ERROR));
                break;
        }
    }

    @Override
    public void visit(DDLAlterLogfileGroupStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(k(Keywords.LOGFILE), 0)
            .append(t(Token.KW_GROUP), 2);
        print(node.getName());
        appendable.append(t(Token.KW_ADD), 0).append(k(Keywords.UNDOFILE), 2);
        print(node.getUndoFile());
        if (node.getInitialSize() != null) {
            appendable.append(k(Keywords.INITIAL_SIZE), 1).append('=');
            print(node.getInitialSize());
        }
        if (node.isWait()) {
            appendable.append(k(Keywords.WAIT), 1);
        }
        if (node.getEngine() != null) {
            appendable.append(k(Keywords.ENGINE), 1).append('=');
            print(node.getEngine());
        }
    }

    @Override
    public void visit(DDLAlterProcedureStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(t(Token.KW_PROCEDURE), 0);
        print(node.getName());
        List<Characteristic> list = node.getCharacteristics();
        if (list != null && !list.isEmpty()) {
            appendable.append(' ');
            printList(list, ' ');
        }
    }

    @Override
    public void visit(DDLAlterServerStatement node) {
        appendable.append(t(Token.KW_ALTER)).append(k(Keywords.SERVER), 0);
        print(node.getServerName());
        if (node.getOptions() != null && !node.getOptions().isEmpty()) {
            appendable.append(k(Keywords.OPTIONS), 0).append('(');
            boolean first = true;
            for (Pair<Integer, Literal> option : node.getOptions()) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                switch (option.getKey()) {
                    case DDLCreateServerStatement.HOST:
                        appendable.append(k(Keywords.HOST));
                        break;
                    case DDLCreateServerStatement.DATABASE:
                        appendable.append(t(Token.KW_DATABASE));
                        break;
                    case DDLCreateServerStatement.USER:
                        appendable.append(k(Keywords.USER));
                        break;
                    case DDLCreateServerStatement.PASSWORD:
                        appendable.append(k(Keywords.PASSWORD));
                        break;
                    case DDLCreateServerStatement.SOCKET:
                        appendable.append(k(Keywords.SOCKET));
                        break;
                    case DDLCreateServerStatement.OWNER:
                        appendable.append(k(Keywords.OWNER));
                        break;
                    case DDLCreateServerStatement.PORT:
                        appendable.append(k(Keywords.PORT));
                        break;
                }
                appendable.append(' ');
                print(option.getValue());
            }
            appendable.append(')');
        }
    }

    @Override
    public void visit(DDLAlterTablespaceStatement node) {
        appendable.append(t(Token.KW_ALTER));
        if (node.isUndo()) {
            appendable.append(t(Token.KW_UNDO), 1);
        }
        appendable.append(k(Keywords.TABLESPACE), 0);
        print(node.getName());
        if (node.getFileName() != null) {
            appendable.append(node.isAddFile() ? t(Token.KW_ADD) : t(Token.KW_DROP), 0)
                .append(k(Keywords.DATAFILE), 2);
            print(node.getFileName());
        }
        if (node.getInitialSize() != null) {
            appendable.append(k(Keywords.INITIAL_SIZE), 1).append('=');
            print(node.getInitialSize());
        }
        if (node.isWait()) {
            appendable.append(k(Keywords.WAIT), 1);
        }
        if (node.getRenameTo() != null) {
            appendable.append(t(Token.KW_RENAME), 0).append(t(Token.KW_TO), 2);
            print(node.getRenameTo());
        }
        if (node.getSetActive() != null) {
            appendable.append(t(Token.KW_SET), 0)
                .append(node.getSetActive() ? k(Keywords.ACTIVE) : k(Keywords.INACTIVE));
        }
        if (node.getEncryption() != null) {
            appendable.append(k(Keywords.ENCRYPTION), 1).append('=');
            if (node.getEncryption()) {
                appendable.append("'Y'");
            } else {
                appendable.append("'N'");
            }
        }
        if (node.getEngine() != null) {
            appendable.append(k(Keywords.ENGINE), 1).append('=');
            print(node.getEngine());
        }
    }

    @Override
    public void visit(DDLAlterViewStatement node) {
        appendable.append(t(Token.KW_ALTER));
        if (node.getAlgorithm() != null) {
            switch (node.getAlgorithm()) {
                case DDLCreateViewStatement.UNDEFINED:
                    appendable.append(k(Keywords.ALGORITHM), 1).append('=')
                        .append(k(Keywords.UNDEFINED));
                    break;
                case DDLCreateViewStatement.MERGE:
                    appendable.append(k(Keywords.ALGORITHM), 1).append('=')
                        .append(k(Keywords.MERGE));
                    break;
                case DDLCreateViewStatement.TEMPTABLE:
                    appendable.append(k(Keywords.ALGORITHM), 1).append('=')
                        .append(k(Keywords.TEMPTABLE));
                    break;
            }
        }
        if (node.getDefiner() != null) {
            appendable.append(k(Keywords.DEFINER), 1).append('=');
            print(node.getDefiner());
        }
        if (node.getSqlSecurityDefiner() != null) {
            appendable.append(t(Token.KW_SQL), 0).append(k(Keywords.SECURITY), 2).append(
                node.getSqlSecurityDefiner() ? k(Keywords.DEFINER) : k(Keywords.INVOKER));
        }
        appendable.append(k(Keywords.VIEW), 0);
        print(node.getName());
        List<Identifier> columnsList = node.getColumns();
        if (columnsList != null && !columnsList.isEmpty()) {
            appendable.append('(');
            boolean isFst = true;
            for (Identifier p : columnsList) {
                if (isFst) {
                    isFst = false;
                } else {
                    appendable.append(',');
                }
                print(p);
            }
            appendable.append(')');
        }
        appendable.append(t(Token.KW_AS), 0);
        print(node.getStmt());
        if (node.isWithCheckOption()) {
            appendable.append(t(Token.KW_WITH), 0);
            if (node.getCascaded() != null) {
                appendable.append(node.getCascaded() ? k(Keywords.CASCADED) : k(Keywords.LOCAL), 2);
            }
            appendable.append(t(Token.KW_CHECK)).append(t(Token.KW_OPTION), 1);
        }
    }

    @Override
    public void visit(DMLCallStatement node) {
        appendable.append(t(Token.KW_CALL), 0);
        print(node.getName());
        appendable.append('(');
        printList(node.getParams());
        appendable.append(')');
    }

    @Override
    public void visit(DMLDoStatement node) {
        appendable.append(k(Keywords.DO), 2);
        printList(node.getExprs());
    }

    @Override
    public void visit(DMLHandlerStatement node) {
        appendable.append(k(Keywords.HANDLER), 2);
        print(node.getTable());
        switch (node.getType()) {
            case DMLHandlerStatement.OPEN: {
                appendable.append(k(Keywords.OPEN), 1);
                String alias = node.getAlias();
                if (alias != null) {
                    appendable.append(t(Token.KW_AS), 0);
                    appendable.append(alias);
                }
                break;
            }
            case DMLHandlerStatement.READ: {
                appendable.append(t(Token.KW_READ), 1);
                if (node.getIndex() != null) {
                    appendable.append(' ');
                    print(node.getIndex());
                }
                Integer operator = node.getOperator();
                if (operator != null) {
                    switch (operator) {
                        case DMLHandlerStatement.EQUALS:
                            appendable.append('=');
                            break;
                        case DMLHandlerStatement.LESS_EQUALS:
                            appendable.append('<').append('=');
                            break;
                        case DMLHandlerStatement.GREAT_EQUALS:
                            appendable.append('>').append('=');
                            break;
                        case DMLHandlerStatement.LESS:
                            appendable.append('<');
                            break;
                        case DMLHandlerStatement.GREAT:
                            appendable.append('>');
                            break;
                    }
                }
                List<Expression> values = node.getValues();
                if (values != null && !values.isEmpty()) {
                    appendable.append('(');
                    printList(values);
                    appendable.append(')');
                }
                Integer order = node.getOrder();
                if (order != null) {
                    switch (order) {
                        case DMLHandlerStatement.FIRST:
                            appendable.append(k(Keywords.FIRST), 1);
                            break;
                        case DMLHandlerStatement.NEXT:
                            appendable.append(k(Keywords.NEXT), 1);
                            break;
                        case DMLHandlerStatement.PREV:
                            appendable.append(k(Keywords.PREV), 1);
                            break;
                        case DMLHandlerStatement.LAST:
                            appendable.append(k(Keywords.LAST), 1);
                            break;
                    }
                }
                Expression where = node.getWhere();
                if (where != null) {
                    appendable.append(t(Token.KW_WHERE), 0);
                    print(where);
                }
                Limit limit = node.getLimit();
                if (limit != null) {
                    appendable.append(' ');
                    print(limit);
                }
            }
            break;
            case DMLHandlerStatement.CLOSE: {
                appendable.append(k(Keywords.CLOSE), 1);
                break;
            }
        }
    }
}

