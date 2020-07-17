package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.ComparisionExpression;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.WithClause;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.tableref.Tables;
import parser.visitor.Visitor;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author huangganyan
 * @date 2020/7/17
 */
public class DMLDeleteStatement implements DMLStatement {
    private long parseInfo;
    private final boolean lowPriority;
    private final boolean quick;
    private final boolean ignore;
    /**
     * tableName[.*]
     */
    private final List<Identifier> tableNames;
    private Tables tableRefs;
    private Expression whereCondition;
    private OrderBy orderBy;
    private Limit limit;
    private final List<Identifier> partition;
    private WithClause with;

    /**
     * 非语法树相关信息
     */
    private List<ComparisionExpression> conditions;
    private byte[] cachedTableName;

    public DMLDeleteStatement(boolean lowPriority, boolean quick, boolean ignore, Identifier tableName,
        List<Identifier> partition) throws SQLSyntaxErrorException {
        this(lowPriority, quick, ignore, tableName, null, null, null, partition);
    }

    public DMLDeleteStatement(boolean lowPriority, boolean quick, boolean ignore, Identifier tableName,
        Expression where) throws SQLSyntaxErrorException {
        this(lowPriority, quick, ignore, tableName, where, null, null, null);
    }

    public DMLDeleteStatement(boolean lowPriority, boolean quick, boolean ignore, Identifier tableName,
        Expression where, OrderBy orderBy, Limit limit, List<Identifier> partition) throws SQLSyntaxErrorException {
        this.lowPriority = lowPriority;
        this.quick = quick;
        this.ignore = ignore;
        this.tableNames = new ArrayList<Identifier>(1);
        this.tableNames.add(tableName);
        this.tableRefs = null;
        this.whereCondition = where;
        this.orderBy = orderBy;
        this.limit = limit;
        this.partition = partition;
    }

    // ------- multi-row delete------------
    public DMLDeleteStatement(boolean lowPriority, boolean quick, boolean ignore, List<Identifier> tableNameList,
        Tables tableRefs) throws SQLSyntaxErrorException {
        this(lowPriority, quick, ignore, tableNameList, tableRefs, null);
    }

    public DMLDeleteStatement(boolean lowPriority, boolean quick, boolean ignore, List<Identifier> tableNameList,
        Tables tableRefs, Expression whereCondition) throws SQLSyntaxErrorException {
        this.lowPriority = lowPriority;
        this.quick = quick;
        this.ignore = ignore;
        if (tableNameList == null || tableNameList.isEmpty()) {
            throw new IllegalArgumentException("argument 'tableNameList' is empty");
        } else if (tableNameList instanceof ArrayList) {
            this.tableNames = tableNameList;
        } else {
            this.tableNames = new ArrayList<Identifier>(tableNameList);
        }
        if (tableRefs == null) {
            throw new IllegalArgumentException("argument 'tableRefs' is null");
        }
        this.tableRefs = tableRefs;
        this.whereCondition = whereCondition;
        this.orderBy = null;
        this.limit = null;
        this.partition = null;
    }

    public List<Identifier> getTableNames() {
        return tableNames;
    }

    public Tables getTableRefs() {
        return tableRefs;
    }

    public Expression getWhereCondition() {
        return whereCondition;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public Limit getLimit() {
        return limit;
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public boolean isQuick() {
        return quick;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public List<Identifier> getPartition() {
        return partition;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DELETE;
    }

    @Override
    public void setParseInfo(long parseInfo) {
        this.parseInfo = parseInfo;
    }

    @Override
    public long getParseInfo() {
        return parseInfo;
    }

    public void setConditions(List<ComparisionExpression> conditions) {
        this.conditions = conditions;
    }

    public List<ComparisionExpression> getConditions() {
        return conditions;
    }

    public void setWithClause(WithClause with) {
        this.with = with;
    }

    public WithClause getWithClause() {
        return with;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (tableNames != null) {
            Iterator<Identifier> iter = tableNames.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        tableNames.set(i, (Identifier)to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        if (tableRefs != null) {
            if (tableRefs.equals(from)) {
                tableRefs = (Tables)to;
                result = true;
            } else {
                result |= tableRefs.replace(from, to);
            }
        }
        if (whereCondition != null) {
            if (whereCondition.equals(from)) {
                whereCondition = (Expression)to;
                result = true;
            } else {
                result |= whereCondition.replace(from, to);
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
        if (partition != null) {
            Iterator<Identifier> iter = partition.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        partition.set(i, (Identifier)to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        if (conditions != null) {
            Iterator<ComparisionExpression> iter = conditions.iterator();
            int i = 0;
            while (iter.hasNext()) {
                ComparisionExpression exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        conditions.set(i, (ComparisionExpression)to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        if (with != null) {
            if (with.equals(from)) {
                with = (WithClause)to;
                result = true;
            } else {
                result |= with.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (tableNames != null) {
            Iterator<Identifier> iter = tableNames.iterator();
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        if (tableRefs != null) {
            removed |= tableRefs.removeSchema(schema);
        }
        if (whereCondition != null) {
            removed |= whereCondition.removeSchema(schema);
        }
        if (orderBy != null) {
            removed |= orderBy.removeSchema(schema);
        }
        if (partition != null) {
            Iterator<Identifier> iter = partition.iterator();
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        if (conditions != null) {
            Iterator<ComparisionExpression> iter = conditions.iterator();
            while (iter.hasNext()) {
                ComparisionExpression exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        if (with != null) {
            removed |= with.removeSchema(schema);
        }
        return removed;
    }

    @Override
    public void setCachedTableName(byte[] affectedTable) {
        this.cachedTableName = affectedTable;
    }

    @Override
    public byte[] getCachedTableName() {
        return cachedTableName;
    }

    @Override
    public boolean maybeMoreThanTwoTable() {
        return tableNames.size() > 1 || (tableRefs != null && !tableRefs.isSingleTable());
    }
}

