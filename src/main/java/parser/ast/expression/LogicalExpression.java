package parser.ast.expression;

import parser.ast.expression.primary.literal.Literal;
import parser.ast.expression.primary.literal.LiteralBoolean;
import parser.util.ExprEvalUtils;
import parser.visitor.Visitor;

import java.sql.SQLSyntaxErrorException;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class LogicalExpression extends PolyadicOperatorExpression {
    private final boolean isAnd;
    private boolean isOracle = false;

    public LogicalExpression(boolean isAnd) {
        super(isAnd ? PRECEDENCE_LOGICAL_AND : PRECEDENCE_LOGICAL_OR);
        this.isAnd = isAnd;
    }

    public boolean isOracle() {
        return isOracle;
    }

    public void setOracle(boolean oracle) {
        isOracle = oracle;
    }

    public boolean isAnd() {
        return isAnd;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getOperator() {
        if (isOracle && !isAnd) {
            return "||";
        } else {
            return isAnd ? "AND" : "OR";
        }
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters) throws Exception {
        Object ret = null;
        for (Expression operand : operands) {
            Object val = operand.evaluation(parameters, null);
            if (val == null || val == UNEVALUATABLE)
                return UNEVALUATABLE;
            if (val instanceof Literal) {
                val = ((Literal)val).evaluation(null, null);
            }
            if (!ExprEvalUtils.obj2bool(val)) {
                if (isAnd) {
                    return LiteralBoolean.FALSE;
                } else {
                    return LiteralBoolean.TRUE;
                }
            } else {
                if (ret != UNEVALUATABLE) {
                    if (isAnd) {
                        ret = LiteralBoolean.TRUE;
                    } else {
                        return LiteralBoolean.FALSE;
                    }
                }
            }
        }
        return ret;
    }
}

