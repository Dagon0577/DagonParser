package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.QueryExpression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.RowExpression;
import parser.util.Pair;
import parser.visitor.Visitor;

import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DMLInsertReplaceStatement implements DMLStatement {

    private static final long serialVersionUID = 2806635212490911316L;
    private long parseInfo;
    public static final int UNDEF = 1;
    public static final int LOW = 2;
    public static final int DELAY = 3;
    public static final int HIGH = 4;

    private final boolean isInsert;
    private final int mode;
    private boolean ignore;
    private Identifier table;
    private List<Identifier> columns;
    private final List<RowExpression> rows;
    private QueryExpression select;
    private final List<Pair<Identifier, Expression>> duplicateUpdate;
    private final List<Identifier> partitions;

    private byte[] cachedTableName;

    public DMLInsertReplaceStatement(boolean isInsert, int mode, boolean ignore, Identifier table,
        List<Identifier> columns, List<RowExpression> rows, QueryExpression select,
        List<Pair<Identifier, Expression>> duplicateUpdate, List<Identifier> partitions) {
        this.isInsert = isInsert;
        this.mode = mode;
        this.ignore = ignore;
        this.table = table;
        this.columns = columns;
        this.rows = rows;
        this.select = select;
        this.duplicateUpdate = duplicateUpdate;
        this.partitions = partitions;
    }

    public boolean isInsert() {
        return isInsert;
    }

    public int getMode() {
        return mode;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public Identifier getTable() {
        return table;
    }

    public List<Identifier> getColumns() {
        return columns;
    }

    public void setColumns(List<Identifier> columns) {
        this.columns = columns;
    }

    public List<RowExpression> getRows() {
        return rows;
    }

    public QueryExpression getSelect() {
        return select;
    }

    public List<Pair<Identifier, Expression>> getDuplicateUpdate() {
        return duplicateUpdate;
    }

    public List<Identifier> getPartitions() {
        return partitions;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return isInsert ? SQLType.INSERT : SQLType.REPLACE;
    }

    @Override
    public void setParseInfo(long parseInfo) {
        this.parseInfo = parseInfo;
    }

    @Override
    public long getParseInfo() {
        return parseInfo;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (table != null) {
            if (table.equals(from)) {
                table = (Identifier)to;
                result = true;
            } else {
                result |= table.replace(from, to);
            }
        }
        if (columns != null) {
            Iterator<Identifier> iter = columns.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        columns.set(i, (Identifier)to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        if (rows != null) {
            Iterator<RowExpression> iter = rows.iterator();
            int i = 0;
            while (iter.hasNext()) {
                RowExpression exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        rows.set(i, (RowExpression)to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        if (select != null) {
            if (select.equals(from)) {
                select = (QueryExpression)to;
                result = true;
            } else {
                result |= select.replace(from, to);
            }
        }
        if (duplicateUpdate != null) {
            for (Pair<Identifier, Expression> p : duplicateUpdate) {
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
        if (partitions != null) {
            Iterator<Identifier> iter = partitions.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        partitions.set(i, (Identifier)to);
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

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (table != null) {
            removed |= table.removeSchema(schema);
        }
        if (columns != null) {
            Iterator<Identifier> iter = columns.iterator();
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        if (rows != null) {
            Iterator<RowExpression> iter = rows.iterator();
            while (iter.hasNext()) {
                RowExpression exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        if (select != null) {
            removed |= select.removeSchema(schema);
        }
        if (duplicateUpdate != null) {
            for (Pair<Identifier, Expression> p : duplicateUpdate) {
                if (p.getKey() != null) {
                    removed |= p.getKey().removeSchema(schema);
                }
                if (p.getValue() != null) {
                    removed |= p.getValue().removeSchema(schema);
                }
            }
        }
        if (partitions != null) {
            Iterator<Identifier> iter = partitions.iterator();
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
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
        return false;
    }
}

