package parser.ast.expression.primary;

import parser.ast.expression.PrimaryExpression;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class PlaceHolder extends PrimaryExpression {
    private final long name;

    public PlaceHolder(long name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public long getName() {
        return name;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return false;
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) {
        return parameters.get(name);
    }
}
