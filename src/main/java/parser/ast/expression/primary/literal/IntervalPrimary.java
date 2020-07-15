package parser.ast.expression.primary.literal;

import parser.ast.expression.Expression;
import parser.ast.expression.PrimaryExpression;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class IntervalPrimary extends PrimaryExpression {
    private final int unit;
    private final Expression quantity;

    public IntervalPrimary(Expression quantity, int unit) {
        if (quantity == null)
            throw new IllegalArgumentException("quantity expression is null");
        if (unit == 0)
            throw new IllegalArgumentException("unit of time is null");
        this.quantity = quantity;
        this.unit = unit;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int getUnit() {
        return unit;
    }

    public Expression getQuantity() {
        return quantity;
    }

}
