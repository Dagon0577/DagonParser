package parser.ast.expression.primary;

import parser.ast.expression.Expression;
import parser.token.Functions;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class Char extends FunctionExpression {
    private final String charset;

    public Char(byte[] functionName, String charset, List<Expression> arguments) {
        super(Functions.CHAR, functionName, arguments);
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
