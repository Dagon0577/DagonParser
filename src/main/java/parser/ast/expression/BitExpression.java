package parser.ast.expression;

import parser.ast.expression.primary.literal.Literal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class BitExpression extends BinaryOperatorExpression {

    public static final int BIT_AND = 1;
    public static final int BIT_OR = 2;
    public static final int BIT_SHIFT = 3;
    public static final int BIT_XOR = 4;

    private final int type;
    private boolean negative;

    public BitExpression(Expression leftOprand, Expression rightOprand, int type) {
        super(leftOprand, rightOprand, getPrecedence(type));
        this.type = type;
    }

    public BitExpression setNegative(boolean negative) {
        this.negative = negative;
        return this;
    }

    private static int getPrecedence(int type) {
        switch (type) {
            case BIT_AND:
                return PRECEDENCE_BIT_AND;
            case BIT_OR:
                return PRECEDENCE_BIT_OR;
            case BIT_SHIFT:
                return PRECEDENCE_BIT_SHIFT;
            case BIT_XOR:
                return PRECEDENCE_BIT_XOR;
        }
        return 0;
    }

    @Override
    public String getOperator() {
        switch (type) {
            case BIT_AND:
                return "&";
            case BIT_OR:
                return "|";
            case BIT_SHIFT:
                return negative ? ">>" : "<<";
            case BIT_XOR:
                return "^";
        }
        return null;
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
        if (left instanceof Number || right instanceof Number) {
            if (type == BIT_OR) {
                if (left instanceof BigInteger || right instanceof BigInteger) {
                    return ((BigInteger)left).or(((BigInteger)right));
                } else if (left instanceof BigDecimal || right instanceof BigDecimal) {
                    return ((BigDecimal)left).toBigInteger().or(((BigDecimal)right).toBigInteger());
                } else {
                    return ((Number)left).longValue() | (((Number)right)).longValue();
                }
            } else if (type == BIT_AND) {
                if (left instanceof BigInteger || right instanceof BigInteger) {
                    return ((BigInteger)left).and(((BigInteger)right));
                } else if (left instanceof BigDecimal || right instanceof BigDecimal) {
                    return ((BigDecimal)left).toBigInteger().and(((BigDecimal)right).toBigInteger());
                } else {
                    return ((Number)left).longValue() & (((Number)right)).longValue();
                }
            } else if (type == BIT_SHIFT) {
                if (this.negative) {
                    return ((Number)left).longValue() >> (((Number)right)).longValue();
                } else {
                    return ((Number)left).longValue() << (((Number)right)).longValue();
                }
            } else {
                return ((Number)left).longValue() ^ (((Number)right)).longValue();
            }
        }
        return UNEVALUATABLE;
    }
}
