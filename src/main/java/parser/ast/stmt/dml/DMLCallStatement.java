package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月02日
 * 
 *       <pre>
 * CALL sp_name([parameter[,...]])
 * CALL sp_name[()]
 *       </pre>
 */
public class DMLCallStatement implements DMLStatement {
    private long parseInfo;
    private Identifier name;
    private final List<Expression> params;

    public DMLCallStatement(Identifier name, List<Expression> params) {
        this.name = name;
        this.params = params;
    }

    public Identifier getName() {
        return name;
    }

    public List<Expression> getParams() {
        return params;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CALL;
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
        if (name != null) {
            if (name.equals(from)) {
                name = (Identifier) to;
                result = true;
            } else {
                result |= name.replace(from, to);
            }
        }
        if (params != null) {
            Iterator<Expression> iter = params.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Expression exp = iter.next();
                if (exp != null) {
                    if (exp.equals(to)) {
                        params.set(i, (Expression) to);
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
        if (name != null) {
            removed |= name.removeSchema(schema);
        }
        if (params != null) {
            Iterator<Expression> iter = params.iterator();
            while (iter.hasNext()) {
                Expression exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
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
