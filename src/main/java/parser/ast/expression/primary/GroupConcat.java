package parser.ast.expression.primary;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.fragment.OrderBy;
import parser.token.Functions;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class GroupConcat extends FunctionExpression {
    private final boolean distinct;
    private OrderBy orderBy;
    private final String separator;
    private final boolean isNullSeqarator;

    public GroupConcat(byte[] functionName, List<Expression> arguments, boolean distinct, OrderBy orderBy,
        String separator) {
        super(Functions.GROUP_CONCAT, functionName, arguments);
        this.distinct = distinct;
        this.orderBy = orderBy;
        this.isNullSeqarator = separator == null ? true : false;
        this.separator = separator == null ? "," : separator;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public String getSeparator() {
        return separator;
    }

    public boolean isNullSeqarator() {
        return isNullSeqarator;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (orderBy != null) {
            if (orderBy.equals(from)) {
                orderBy = (OrderBy)to;
            } else {
                result |= orderBy.replace(from, to);
            }
        }
        return result;
    }
}
