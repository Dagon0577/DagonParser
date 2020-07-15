package parser.ast.expression.primary;

import parser.ast.expression.PrimaryExpression;
import parser.visitor.Visitor;

/**
 * <pre>
 * over_clause:
 *   {OVER (window_spec) | OVER window_name}
 * </pre>
 *
 * @author Dagon0577
 * @date 2020/7/15
 */
public class OverClause extends PrimaryExpression {
    private FunctionExpression windowFunction;
    private Identifier windowName;
    private WindowClause windowClause;

    public OverClause(FunctionExpression windowFunction, Identifier windowName, WindowClause windowClause) {
        this.windowFunction = windowFunction;
        this.windowName = windowName;
        this.windowClause = windowClause;
    }

    public FunctionExpression getWindowFunction() {
        return windowFunction;
    }

    public Identifier getWindowName() {
        return windowName;
    }

    public WindowClause getWindowClause() {
        return windowClause;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
