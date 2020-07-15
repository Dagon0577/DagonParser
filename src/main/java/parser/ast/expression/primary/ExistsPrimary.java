package parser.ast.expression.primary;

import parser.ast.AST;
import parser.ast.expression.PrimaryExpression;
import parser.ast.expression.QueryExpression;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class ExistsPrimary extends PrimaryExpression {
    private QueryExpression subquery;

    public ExistsPrimary(QueryExpression subquery) {
        if (subquery == null) {
            throw new IllegalArgumentException("subquery is null for EXISTS expression");
        }
        this.subquery = subquery;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public QueryExpression getSubquery() {
        return subquery;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (subquery != null) {
            if (subquery.equals(from)) {
                subquery = (QueryExpression)to;
                result = true;
            } else {
                result |= subquery.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        if (subquery != null) {
            return subquery.removeSchema(schema);
        }
        return false;
    }

}
