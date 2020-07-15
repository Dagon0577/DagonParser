package cn.hotdb.parser.ast.expression.primary;

import java.util.List;

import cn.hotdb.parser.ast.AST;
import cn.hotdb.parser.ast.expression.Expression;
import cn.hotdb.parser.ast.fragment.OrderBy;
import cn.hotdb.parser.token.Functions;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午7:13:56
 * 
 */
public class GroupConcat extends FunctionExpression {
    private final boolean distinct;
    private OrderBy orderBy;
    private final String separator;
    private final boolean isNullSeqarator;

    public GroupConcat(byte[] functionName, List<Expression> arguments, boolean distinct,
            OrderBy orderBy, String separator) {
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
                orderBy = (OrderBy) to;
            } else {
                result |= orderBy.replace(from, to);
            }
        }
        return result;
    }
}
