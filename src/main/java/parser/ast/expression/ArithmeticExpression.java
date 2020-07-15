package parser.ast.expression;

import parser.ast.expression.primary.literal.Literal;
import parser.util.BinaryOperandCalculator;
import parser.util.ExprEvalUtils;
import parser.util.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class ArithmeticExpression extends BinaryOperatorExpression implements BinaryOperandCalculator {

    public static final int ADD = 1;
    public static final int SUBTRACT = 2;
    public static final int MULTIPLY = 3;
    public static final int DIVIDE = 4;
    public static final int INTEGET_DIVIDE = 5;
    public static final int MOD = 6;
    private final int type;

    public ArithmeticExpression(Expression leftOprand, Expression rightOprand, int type) {
        super(leftOprand, rightOprand, getPrecedence(type));
        this.type = type;
    }

    private static int getPrecedence(int type) {
        switch (type) {
            case ADD:
            case SUBTRACT:
                return PRECEDENCE_ARITHMETIC_TERM_OP;
            case MULTIPLY:
            case DIVIDE:
            case INTEGET_DIVIDE:
            case MOD:
                return PRECEDENCE_ARITHMETIC_FACTOR_OP;
        }
        return 0;
    }

    @Override
    public String getOperator() {
        switch (type) {
            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case INTEGET_DIVIDE:
                return " DIV ";
            case MOD:
                return "%";
        }
        return null;
    }

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
        Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
        return ExprEvalUtils.calculate(this, pair.getKey(), pair.getValue());
    }

    @Override
    public Number calculate(Integer integer1, Integer integer2) {
        if (integer1 == null || integer2 == null)
            return null;
        int i1 = integer1.intValue();
        int i2 = integer2.intValue();
        switch (type) {
            case ADD:
                if (i1 == 0)
                    return integer2;
                if (i2 == 0)
                    return integer1;
                if (i1 >= 0 && i2 <= 0 || i1 <= 0 && i2 >= 0) {
                    return new Integer(i1 + i2);
                }
                int rst = i1 + i2;
                if (i1 > 0 && rst < i1 || i1 < 0 && rst > i1) {
                    return new Long((long)i1 + (long)i2);
                }
                return new Integer(rst);
            case SUBTRACT:
                if (i2 == 0)
                    return integer1;
                if (i1 == 0) {
                    if (i2 == Integer.MIN_VALUE) {
                        return new Long(-(long)i2);
                    }
                    return new Integer(-i2);
                }
                if (i1 >= 0 && i2 >= 0 || i1 <= 0 && i2 <= 0) {
                    return new Integer(i1 - i2);
                }
                rst = i1 - i2;
                if (i1 > 0 && rst < i1 || i1 < 0 && rst > i1) {
                    return new Long((long)i1 - (long)i2);
                }
                return new Integer(rst);
            case MULTIPLY:
                if (i1 == 0 || i2 == 0) {
                    return 0;
                }
                return new Long((long)i1 * (long)i2);
            case DIVIDE:
                if (integer1 == null || integer2 == null || integer2 == 0) {
                    throw new UnsupportedOperationException();
                }
                return BigDecimal.valueOf(integer1).divide(BigDecimal.valueOf(integer2), 16, RoundingMode.HALF_UP);
            case INTEGET_DIVIDE:
                if (integer1 == null || integer2 == null || integer2 == 0) {
                    throw new UnsupportedOperationException();
                }
                return integer1 / integer2;
            case MOD:
                if (i2 == 0)
                    return null;
                return i1 % i2;
        }
        return null;
    }

    @Override
    public Number calculate(Long long1, Long long2) {
        if (long1 == null || long2 == null)
            return null;
        long l1 = long1.longValue();
        long l2 = long2.longValue();
        switch (type) {
            case ADD:
                if (l1 == 0L)
                    return long2;
                if (l2 == 0L)
                    return long1;
                if (l1 >= 0L && l2 <= 0L || l1 <= 0L && l2 >= 0L) {
                    return new Long(l1 + l2);
                }
                long rst = l1 + l2;
                if (l1 > 0L && rst < l1 || l1 < 0L && rst > l1) {
                    BigInteger bi1 = BigInteger.valueOf(l1);
                    BigInteger bi2 = BigInteger.valueOf(l2);
                    return bi1.add(bi2);
                }
                return new Long(rst);
            case SUBTRACT:
                if (l2 == 0L)
                    return long1;
                if (l1 == 0L) {
                    if (l2 == Long.MIN_VALUE) {
                        return BigInteger.valueOf(l2).negate();
                    }
                    return new Long(-l2);
                }
                if (l1 >= 0L && l2 >= 0L || l1 <= 0L && l2 <= 0L) {
                    return new Long(l1 - l2);
                }
                rst = l1 - l2;
                if (l1 > 0L && rst < l1 || l1 < 00L && rst > l1) {
                    BigInteger bi1 = BigInteger.valueOf(l1);
                    BigInteger bi2 = BigInteger.valueOf(l2);
                    return bi1.subtract(bi2);
                }
                return new Long(rst);
            case MULTIPLY:
                if (l1 == 0 || l2 == 0) {
                    return 0;
                }
                return new Long((long)l1 * (long)l2);
            case DIVIDE:
                if (long1 == null || long2 == null || long2 == 0) {
                    throw new UnsupportedOperationException();
                }
                return BigDecimal.valueOf(long1).divide(BigDecimal.valueOf(long2), 16, RoundingMode.HALF_UP);
            case INTEGET_DIVIDE:
                if (long1 == null || long2 == null || long2 == 0) {
                    throw new UnsupportedOperationException();
                }
                return long1 / long2;
            case MOD:
                if (long1 == null || long2 == null)
                    return null;
                int i1 = long1.intValue();
                int i2 = long2.intValue();
                if (i2 == 0)
                    return null;
                return i1 % i2;
        }
        return null;
    }

    @Override
    public Number calculate(BigInteger bigint1, BigInteger bigint2) {
        switch (type) {
            case ADD:
                if (bigint1 == null || bigint2 == null)
                    return null;
                return bigint1.add(bigint2);
            case SUBTRACT:
                if (bigint1 == null || bigint2 == null)
                    return null;
                return bigint1.subtract(bigint2);
            case MULTIPLY:
                if (bigint1 == null || bigint2 == null) {
                    return null;
                }
                return bigint1.multiply(bigint2);
            case DIVIDE:
                if (bigint1 == null || bigint2 == null || bigint2.compareTo(BigInteger.ZERO) == 0) {
                    throw new UnsupportedOperationException();
                }
                return new BigDecimal(bigint1).divide(new BigDecimal(bigint2), 16, RoundingMode.HALF_UP);
            case INTEGET_DIVIDE:
                if (bigint1 == null || bigint2 == null || bigint2.equals(BigInteger.ZERO)) {
                    throw new UnsupportedOperationException();
                }
                return bigint1.divide(bigint2);
            case MOD:
                if (bigint1 == null || bigint2 == null)
                    return null;
                int comp = bigint2.compareTo(BigInteger.ZERO);
                if (comp == 0) {
                    return null;
                } else if (comp < 0) {
                    return bigint1.negate().mod(bigint2).negate();
                } else {
                    return bigint1.mod(bigint2);
                }
        }
        return null;
    }

    @Override
    public Number calculate(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        switch (type) {
            case ADD:
                if (bigDecimal1 == null || bigDecimal2 == null)
                    return null;
                return bigDecimal1.add(bigDecimal2);
            case SUBTRACT:
                if (bigDecimal1 == null || bigDecimal2 == null)
                    return null;
                return bigDecimal1.subtract(bigDecimal2);
            case MULTIPLY:
                if (bigDecimal1 == null || bigDecimal2 == null) {
                    return null;
                }
                return bigDecimal1.multiply(bigDecimal2);
            case DIVIDE:
                if (bigDecimal1 == null || bigDecimal2 == null || bigDecimal2.compareTo(BigDecimal.ZERO) == 0) {
                    throw new UnsupportedOperationException();
                }
                return bigDecimal1.divide(bigDecimal2, 16, RoundingMode.HALF_UP);
            case INTEGET_DIVIDE:
                if (bigDecimal1 == null || bigDecimal2 == null || bigDecimal2.equals(BigDecimal.ZERO)) {
                    throw new UnsupportedOperationException();
                }
                return bigDecimal1.divideToIntegralValue(bigDecimal2);
            case MOD:
                if (bigDecimal1 == null || bigDecimal2 == null || bigDecimal2.equals(BigDecimal.ZERO)) {
                    return null;
                }
                BigDecimal d = bigDecimal1.divideToIntegralValue(bigDecimal2);
                return bigDecimal1.subtract(bigDecimal2.multiply(d));
        }
        return null;
    }

    @Override
    public boolean exisitConstantOperation() {
        if (leftOprand instanceof Literal && rightOprand instanceof Literal) {
            return true;
        }
        return leftOprand.exisitConstantOperation() || rightOprand.exisitConstantOperation();
    }
}
