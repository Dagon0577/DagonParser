package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.visitor.Visitor;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月02日
 * 
 *       <pre>
 * DO expr [, expr] ...
 *       </pre>
 */
public class DMLDoStatement implements DMLStatement {
    private long parseInfo;
    private final List<Expression> exprs;

    public DMLDoStatement(List<Expression> exprs) {
        this.exprs = exprs;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public List<Expression> getExprs() {
        return exprs;
    }

    @Override
    public int getSQLType() {
        return SQLType.DO;
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
        Iterator<Expression> iter = exprs.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                if (exp.equals(from)) {
                    exprs.set(i, (Expression) to);
                    result = true;
                } else {
                    result |= exp.replace(from, to);
                }
            }
            i++;
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        Iterator<Expression> iter = exprs.iterator();
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                removed |= exp.removeSchema(schema);
            }
        }
        return removed;
    }

    @Override
    public void setCachedTableName(byte[] affectedTable) {}

    @Override
    public byte[] getCachedTableName() {
        return null;
    }

    @Override
    public boolean maybeMoreThanTwoTable() {
        return false;
    }
}
