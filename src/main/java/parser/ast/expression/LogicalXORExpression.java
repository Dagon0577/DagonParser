package parser.ast.expression;

import parser.ast.expression.primary.literal.Literal;
import parser.ast.expression.primary.literal.LiteralBoolean;
import parser.util.ExprEvalUtils;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class LogicalXORExpression extends BinaryOperatorExpression {

    public LogicalXORExpression(Expression leftOprand, Expression rightOprand) {
        super(leftOprand, rightOprand, PRECEDENCE_LOGICAL_XOR);
    }

    @Override
    public String getOperator() {
        return " XOR ";
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) throws Exception {
        Object left = leftOprand.evaluation(parameters, sql);
        Object right = rightOprand.evaluation(parameters, sql);
        if (left == null || right == null)
            return null;
        if (left == UNEVALUATABLE || right == UNEVALUATABLE)
            return UNEVALUATABLE;
        if (left instanceof Literal) {
            left = ((Literal)left).evaluation(null, null);
        }
        if (right instanceof Literal) {
            right = ((Literal)right).evaluation(null, null);
        }
        boolean b1 = ExprEvalUtils.obj2bool(left);
        boolean b2 = ExprEvalUtils.obj2bool(right);
        return b1 != b2 ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
    }
}

