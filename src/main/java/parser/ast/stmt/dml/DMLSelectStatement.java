package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.VarsPrimary;
import parser.ast.expression.primary.WindowClause;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.fragment.GroupBy;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.tableref.Tables;
import parser.util.Pair;
import parser.visitor.Visitor;

import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/14
 *
 *
 *
 * <pre>
 *  SELECT
 *      [ALL | DISTINCT | DISTINCTROW ]
 *        [HIGH_PRIORITY]
 *        [STRAIGHT_JOIN]
 *        [SQL_SMALL_RESULT] [SQL_BIG_RESULT] [SQL_BUFFER_RESULT]
 *        SQL_NO_CACHE [SQL_CALC_FOUND_ROWS]
 *      select_expr [, select_expr ...]
 *      [FROM table_references
 *        [PARTITION partition_list]
 *      [WHERE where_condition]
 *      [GROUP BY {col_name | expr | position}, ... [WITH ROLLUP]]
 *      [HAVING where_condition]
 *      [WINDOW window_name AS (window_spec)
 *          [, window_name AS (window_spec)] ...]
 *      [ORDER BY {col_name | expr | position}
 *        [ASC | DESC], ... [WITH ROLLUP]]
 *      [LIMIT {[offset,] row_count | row_count OFFSET offset}]
 *      [INTO OUTFILE 'file_name'
 *          [CHARACTER SET charset_name]
 *          export_options
 *        | INTO DUMPFILE 'file_name'
 *        | INTO var_name [, var_name]]
 *      [FOR {UPDATE | SHARE} [OF tbl_name [, tbl_name] ...] [NOWAIT | SKIP LOCKED]
 *        | LOCK IN SHARE MODE]]
 *       </pre>
 */
public class DMLSelectStatement extends DMLQueryStatement {
    public static final class SelectOption implements AST {
        public static final int UNDEF = 0;
        public static final int DUP_STRATEGY_ALL = 1;
        public static final int DUP_STRATEGY_DISTINCT = 2;
        public static final int DUP_STRATEGY_DISTINCTROW = 3;
        public static final int CACHE_STRATEGY_SQL_NO_CACHE = 1;
        public static final int CACHE_STRATEGY_SQL_CACHE = 2;
        public static final int SQL_SMALL_RESULT = 1;
        public static final int SQL_BIG_RESULT = 2;

        public Integer version;
        public int dupStrategy = DUP_STRATEGY_ALL;
        public boolean highPriority;
        public boolean straightJoin;
        public int resultSize = UNDEF;
        public boolean sqlBufferResult;
        public Integer sqlCache;
        public boolean sqlCalcFoundRows;

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static final class LockMode implements AST {
        public static final int FOR_UPDATE = 1;
        public static final int FOR_SHARE = 2;
        public static final int LOCK_IN_SHARE_MODE = 3;
        public static final int NOWAIT = 1;
        public static final int SKIP_LOCKED = 2;

        public int lockType;
        public List<Identifier> tables;
        public int lockAction;

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        public boolean replace(AST from, AST to) {
            boolean result = false;
            if (tables != null) {
                Iterator<Identifier> iter = tables.iterator();
                int i = 0;
                while (iter.hasNext()) {
                    Identifier exp = iter.next();
                    if (exp != null) {
                        if (exp.equals(from)) {
                            tables.set(i, (Identifier)to);
                            result = true;
                        } else {
                            result |= exp.replace(from, to);
                        }
                    }
                    i++;
                }
            }
            return result;
        }

        public boolean removeSchema(byte[] schema) {
            boolean removed = false;
            if (tables != null) {
                Iterator<Identifier> iter = tables.iterator();
                while (iter.hasNext()) {
                    Identifier exp = iter.next();
                    if (exp != null) {
                        removed |= exp.removeSchema(schema);
                    }
                }
            }
            return removed;
        }
    }

    public static final class OutFile implements AST {
        public static final int OUTFILE = 1;
        public static final int DUMPFILE = 2;
        public static final int VAR = 3;
        public static final int PARAM = 4;

        public int type;
        public Identifier outFile;
        public Identifier charset;
        public Identifier dumpFile;
        public Identifier into;
        public List<VarsPrimary> vars;
        public LiteralString fieldsTerminatedBy;
        public LiteralString fieldsEnclosedBy;
        public LiteralString fieldsEscapedBy;
        public LiteralString linesStartingBy;
        public LiteralString linesTerminatedBy;

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    private SelectOption option;
    private List<Pair<Expression, String>> selectExprList;
    private Tables tables;
    private Expression where;
    private GroupBy groupBy;
    private Expression having;
    private List<Pair<Identifier, WindowClause>> windows;
    private OrderBy orderBy;
    private Limit limit;
    private OutFile outFile;
    private LockMode lock;
    private OrderBy outerOrderBy;
    private Limit outerLimit;
    private int selectStart = -1;
    private int selectEnd = -1;

    public DMLSelectStatement(SelectOption option, List<Pair<Expression, String>> selectExprList, Tables tables,
        Expression where, GroupBy groupBy, Expression having, List<Pair<Identifier, WindowClause>> windows,
        OrderBy orderBy, Limit limit, OutFile outFile, LockMode lock) {
        this.option = option;
        this.selectExprList = selectExprList;
        this.tables = tables;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.windows = windows;
        this.orderBy = orderBy;
        this.limit = limit;
        this.outFile = outFile;
        this.lock = lock;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SELECT;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (selectExprList != null) {
            for (Pair<Expression, String> p : selectExprList) {
                if (p.getKey() != null) {
                    if (p.getKey().equals(from)) {
                        p.setKey((Expression)to);
                        result = true;
                    } else {
                        result |= p.getKey().replace(from, to);
                    }
                }
            }
        }
        if (tables != null) {
            if (tables.equals(from)) {
                tables = (Tables)to;
                result = true;
            } else {
                result |= tables.replace(from, to);
            }
        }
        if (where != null) {
            if (where.equals(from)) {
                where = (Expression)to;
                result = true;
            } else {
                result |= where.replace(from, to);
            }
        }
        if (groupBy != null) {
            if (groupBy.equals(from)) {
                groupBy = (GroupBy)to;
                result = true;
            } else {
                result |= groupBy.replace(from, to);
            }
        }
        if (having != null) {
            if (having.equals(from)) {
                having = (Expression)to;
                result = true;
            } else {
                result |= having.replace(from, to);
            }
        }
        if (windows != null) {
            Iterator<Pair<Identifier, WindowClause>> iter = windows.iterator();
            while (iter.hasNext()) {
                Pair<Identifier, WindowClause> exp = iter.next();
                if (exp != null) {
                    Identifier key = exp.getKey();
                    if (key != null && key.equals(from)) {
                        exp.setKey((Identifier)to);
                        result = true;
                    }
                    WindowClause val = exp.getValue();
                    if (val != null) {
                        if (val.equals(from)) {
                            exp.setValue((WindowClause)to);
                            result = true;
                        } else {
                            result |= val.replace(from, to);
                        }
                    }
                }
            }
        }
        if (orderBy != null) {
            if (orderBy.equals(from)) {
                orderBy = (OrderBy)to;
                result = true;
            } else {
                result |= orderBy.replace(from, to);
            }
        }
        if (limit != null) {
            if (limit.equals(from)) {
                limit = (Limit)to;
                result = true;
            } else {
                result |= limit.replace(from, to);
            }
        }
        if (lock != null) {
            if (lock.equals(from)) {
                lock = (LockMode)to;
                result = true;
            } else {
                result |= lock.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (selectExprList != null) {
            for (Pair<Expression, String> p : selectExprList) {
                if (p.getKey() != null) {
                    removed |= p.getKey().removeSchema(schema);
                }
            }
        }
        if (tables != null) {
            removed |= tables.removeSchema(schema);
        }
        if (where != null) {
            removed |= where.removeSchema(schema);
        }
        if (groupBy != null) {
            removed |= groupBy.removeSchema(schema);
        }
        if (having != null) {
            removed |= having.removeSchema(schema);
        }
        if (windows != null) {
            Iterator<Pair<Identifier, WindowClause>> iter = windows.iterator();
            while (iter.hasNext()) {
                Pair<Identifier, WindowClause> exp = iter.next();
                if (exp != null) {
                    removed |= exp.getKey().removeSchema(schema);
                    removed |= exp.getValue().removeSchema(schema);
                }
            }
        }
        if (orderBy != null) {
            removed |= orderBy.removeSchema(schema);
        }
        if (lock != null) {
            removed |= lock.removeSchema(schema);
        }
        return removed;
    }

    public void setOuterOrderBy(OrderBy orderby) {
        this.outerOrderBy = orderby;
    }

    public void setOuterLimit(Limit limit) {
        this.outerLimit = limit;
    }

    public OrderBy getOuterOrderBy() {
        return this.outerOrderBy;
    }

    public Limit getOuterLimit() {
        return this.outerLimit;
    }

    public void setSelectExprList(List<Pair<Expression, String>> selectExprList) {
        this.selectExprList = selectExprList;
    }

    public void setSelectStartAndEnd(int selectStart, int selectEnd) {
        this.selectStart = selectStart;
        this.selectEnd = selectEnd;
    }

    public int getSelectStart() {
        return selectStart;
    }

    public int getSelectEnd() {
        return selectEnd;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public OutFile getOutFile() {
        return outFile;
    }

    public LockMode getLock() {
        return lock;
    }

    public SelectOption getOption() {
        return option;
    }

    public List<Pair<Expression, String>> getSelectExprList() {
        return selectExprList;
    }

    public Tables getTables() {
        return tables;
    }

    public Expression getWhere() {
        return where;
    }

    public GroupBy getGroupBy() {
        return groupBy;
    }

    public Expression getHaving() {
        return having;
    }

    public List<Pair<Identifier, WindowClause>> getWindows() {
        return windows;
    }

}
