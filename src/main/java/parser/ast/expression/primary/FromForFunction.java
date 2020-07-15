package parser.ast.expression.primary;

import parser.ast.expression.Expression;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/4/17 14:37
 */
public class FromForFunction extends FunctionExpression {

    private boolean isFrom = false;
    private boolean isFor = false;

    public FromForFunction(int type, byte[] functionName, List<Expression> arguments) {
        super(type, functionName, arguments);
    }

    public boolean isFrom() {
        return isFrom;
    }

    public void setFrom(boolean from) {
        isFrom = from;
    }

    public boolean isFor() {
        return isFor;
    }

    public void setFor(boolean aFor) {
        isFor = aFor;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
