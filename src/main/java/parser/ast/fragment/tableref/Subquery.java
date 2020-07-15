package parser.ast.fragment.tableref;

import parser.ast.AST;
import parser.ast.expression.QueryExpression;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class Subquery implements TableReference {
    private QueryExpression subquery;
    private String alias;

    public Subquery(QueryExpression subquery, String alias) {
        this.subquery = subquery;
        this.alias = alias;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_FACTOR;
    }

    public QueryExpression getSubquery() {
        return subquery;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public Object removeLastConditionElement() {
        return null;
    }

    @Override
    public boolean isSingleTable() {
        return false;
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

