package parser.ast.expression.primary;

import parser.ast.expression.PrimaryExpression;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class DefaultValue extends PrimaryExpression {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return false;
    }

}
