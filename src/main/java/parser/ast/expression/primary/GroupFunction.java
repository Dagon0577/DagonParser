package parser.ast.expression.primary;

import parser.ast.expression.Expression;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class GroupFunction extends FunctionExpression {

    private final boolean distinct;

    public GroupFunction(int type, byte[] functionName, List<Expression> arguments, boolean distinct) {
        super(type, functionName, arguments);
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
