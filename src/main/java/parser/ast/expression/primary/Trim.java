package parser.ast.expression.primary;

import parser.ast.expression.Expression;
import parser.token.Functions;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class Trim extends FunctionExpression {
    public static final int BOTH = 1;
    public static final int LEADING = 2;
    public static final int TRAILING = 3;

    private final Integer specifier;

    public Trim(byte[] functionName, List<Expression> arguments, Integer specifier) {
        super(Functions.TRIM, functionName, arguments);
        this.specifier = specifier;
    }

    public Integer getSpecifier() {
        return specifier;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
