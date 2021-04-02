package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.Limit;
import parser.visitor.Visitor;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月02日
 * 
 *       <pre>
 * HANDLER tbl_name OPEN [ [AS] alias]
 * 
 * HANDLER tbl_name READ index_name { = | <= | >= | < | > } (value1,value2,...)
 *     [ WHERE where_condition ] [LIMIT ... ]
 * HANDLER tbl_name READ index_name { FIRST | NEXT | PREV | LAST }
 *     [ WHERE where_condition ] [LIMIT ... ]
 * HANDLER tbl_name READ { FIRST | NEXT }
 *     [ WHERE where_condition ] [LIMIT ... ]
 * 
 * HANDLER tbl_name CLOSE
 *       </pre>
 */
public class DMLHandlerStatement implements DMLStatement {
    private long parseInfo;
    public static final int OPEN = 1;
    public static final int READ = 2;
    public static final int CLOSE = 3;

    public static final int FIRST = 1;
    public static final int NEXT = 2;
    public static final int PREV = 3;
    public static final int LAST = 4;

    public static final int EQUALS = 1;
    public static final int LESS_EQUALS = 2;
    public static final int GREAT_EQUALS = 3;
    public static final int LESS = 4;
    public static final int GREAT = 5;

    private Identifier table;
    private final String alias;
    private final int type;
    private Identifier index;
    private final Integer operator;
    private final List<Expression> values;
    private final Integer order;
    private Expression where;
    private Limit limit;

    private byte[] cachedTableName;


    public DMLHandlerStatement(Identifier table, Identifier index, Integer operator,
            List<Expression> values, Integer order, Expression where, Limit limit) {
        this.table = table;
        this.type = READ;
        this.index = index;
        this.operator = operator;
        this.values = values;
        this.order = order;
        this.where = where;
        this.limit = limit;
        this.alias = null;
    }

    public DMLHandlerStatement(Identifier table) {
        this.table = table;
        this.type = CLOSE;
        this.alias = null;
        this.index = null;
        this.operator = null;
        this.values = null;
        this.order = null;
        this.where = null;
    }

    public DMLHandlerStatement(Identifier table, String alias) {
        this.table = table;
        this.type = OPEN;
        this.alias = alias;
        this.index = null;
        this.operator = null;
        this.values = null;
        this.order = null;
        this.where = null;
    }

    public Identifier getTable() {
        return table;
    }

    public String getAlias() {
        return alias;
    }

    public int getType() {
        return type;
    }

    public Identifier getIndex() {
        return index;
    }

    public Integer getOperator() {
        return operator;
    }

    public List<Expression> getValues() {
        return values;
    }

    public Integer getOrder() {
        return order;
    }

    public Expression getWhere() {
        return where;
    }

    public Limit getLimit() {
        return limit;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.HANDLER;
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
                table = (Identifier) to;
                result = true;
            } else {
                result |= table.replace(from, to);
            }
        }
        if (index != null) {
            if (index.equals(from)) {
                index = (Identifier) to;
                result = true;
            } else {
                result |= index.replace(from, to);
            }
        }
        if (values != null) {
            Iterator<Expression> iter = values.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Expression exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        values.set(i, (Expression) to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        if (where != null) {
            if (where.equals(from)) {
                where = (Expression) to;
                result = true;
            } else {
                result |= where.replace(from, to);
            }
        }
        if (limit != null) {
            if (limit.equals(from)) {
                limit = (Limit) to;
                result = true;
            } else {
                result |= limit.replace(from, to);
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
        if (index != null) {
            removed |= index.removeSchema(schema);
        }
        if (values != null) {
            Iterator<Expression> iter = values.iterator();
            while (iter.hasNext()) {
                Expression exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        if (where != null) {
            removed |= where.removeSchema(schema);
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
