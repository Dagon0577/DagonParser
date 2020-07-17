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
import parser.util.Pair;
import parser.visitor.Visitor;

import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DMLUpdateStatement implements DMLStatement {
    private long parseInfo;
    private final boolean lowPriority;
    private final boolean ignore;
    private Tables tables;
    private List<Pair<Identifier, Expression>> values;
    private Expression where;
    private OrderBy orderBy;
    private Limit limit;
    private final List<Identifier> partition;
    private WithClause with;

    private List<ComparisionExpression> conditions;
    private byte[] cachedTableName;

    public DMLUpdateStatement(boolean lowPriority, boolean ignore, Tables tables,
        List<Pair<Identifier, Expression>> values, Expression where, OrderBy orderBy, Limit limit,
        List<Identifier> partition) {
        this.lowPriority = lowPriority;
        this.ignore = ignore;
        this.tables = tables;
        this.values = values;
        this.where = where;
        this.orderBy = orderBy;
        this.limit = limit;
        this.partition = partition;
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public Tables getTables() {
        return tables;
    }

    public List<Pair<Identifier, Expression>> getValues() {
        return values;
    }

    public Expression getWhere() {
        return where;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public Limit getLimit() {
        return limit;
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
        return SQLType.UPDATE;
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
        if (tables != null) {
            if (tables.equals(from)) {
                tables = (Tables)to;
                result = true;
            } else {
                result |= tables.replace(from, to);
            }
        }
        if (values != null) {
            for (Pair<Identifier, Expression> p : values) {
                if (p.getKey() != null) {
                    if (p.getKey().equals(from)) {
                        p.setKey((Identifier)to);
                        result = true;
                    } else {
                        result |= p.getKey().replace(from, to);
                    }
                }
                if (p.getValue() != null) {
                    if (p.getValue().equals(from)) {
                        p.setValue((Expression)to);
                        result = true;
                    } else {
                        result |= p.getValue().replace(from, to);
                    }
                }
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
        if (tables != null) {
            removed |= tables.removeSchema(schema);
        }
        if (values != null) {
            for (Pair<Identifier, Expression> p : values) {
                if (p.getKey() != null) {
                    removed |= p.getKey().removeSchema(schema);
                }
                if (p.getValue() != null) {
                    removed |= p.getValue().removeSchema(schema);
                }
            }
        }
        if (where != null) {
            removed |= where.removeSchema(schema);
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
    public void setCachedTableName(byte[] cachedTableName) {
        this.cachedTableName = cachedTableName;
    }

    @Override
    public byte[] getCachedTableName() {
        return cachedTableName;
    }

    @Override
    public boolean maybeMoreThanTwoTable() {
        return !tables.isSingleTable();
    }
}

