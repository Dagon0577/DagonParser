package cn.hotdb.parser.ast.expression.primary;

import cn.hotdb.parser.ast.expression.PrimaryExpression;
import cn.hotdb.parser.visitor.Visitor;

/**
 * <pre>
 * over_clause:
 *   {OVER (window_spec) | OVER window_name}
 * </pre>
 * @author liuhuanting
 * @date 2019年6月12日 上午11:13:06
 *
 */
public class OverClause extends PrimaryExpression {
    private FunctionExpression windowFunction;
    private Identifier windowName;
    private WindowClause windowClause;

    public OverClause(FunctionExpression windowFunction, Identifier windowName,
            WindowClause windowClause) {
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
