package parser.ast.expression;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.*;
import parser.ast.fragment.ddl.ColumnDefinition;
import parser.ast.fragment.ddl.DataType;
import parser.util.ExprEvalUtils;
import parser.util.ImplicitConversionUtil;
import parser.util.LongUtil;
import parser.util.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLSyntaxErrorException;
import java.util.Map;

import static parser.ast.expression.Expression.PRECEDENCE_COMPARISION;

/**
 * @author Dagon0577
 * @date 2020/6/23
 */
public class ComparisionExpression extends BinaryOperatorExpression {

    public static final int EQUALS = 1;
    public static final int GREATER_THAN = 2;
    public static final int GREATER_THAN_OR_QEUALS = 3;
    public static final int LESS_OR_GREATER_THAN = 4;
    public static final int LESS_THAN = 5;
    public static final int LESS_THAN_OR_EQUALS = 6;
    public static final int NOT_EQUALS = 7;
    public static final int NULL_SAFE_EQUALS = 8;
    public static final int IN = 9;
    public static final int LIKE = 10;
    public static final int REGEXP = 11;
    public static final int SOUNDS_LIKE = 12;

    private final int type;
    private boolean not;
    private Expression escape;

    public ComparisionExpression(Expression leftOprand, Expression rightOprand, int type) {
        super(leftOprand, rightOprand, getPrecedence(type));
        this.type = type;
    }

    public ComparisionExpression setNot(boolean not) {
        this.not = not;
        return this;
    }

    public ComparisionExpression setEscape(Expression escape) {
        this.escape = escape;
        return this;
    }

    private static int getPrecedence(int type) {
        return PRECEDENCE_COMPARISION;
    }

    public boolean isNot() {
        return not;
    }

    public Expression getEscape() {
        return escape;
    }

    public int getType() {
        return type;
    }

    @Override
    public String getOperator() {
        switch (type) {
            case EQUALS:
                return "=";
            case GREATER_THAN:
                return ">";
            case GREATER_THAN_OR_QEUALS:
                return ">=";
            case LESS_OR_GREATER_THAN:
                return "<>";
            case LESS_THAN:
                return "<";
            case LESS_THAN_OR_EQUALS:
                return "<=";
            case NOT_EQUALS:
                return "!=";
            case NULL_SAFE_EQUALS:
                return "<=>";
            case IN:
                return not ? " NOT IN " : " IN ";
            case LIKE:
                return not ? " NOT LIKE " : " LIKE ";
            case REGEXP:
                return not ? " NOT REGEXP " : " REGEXP ";
            case SOUNDS_LIKE:
                return " SOUNDS LIKE ";
        }
        return null;
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) throws Exception {
        Object left = leftOprand.evaluation(parameters, sql);
        Object right = rightOprand.evaluation(parameters, sql);
        Object idObj = null;
        Object val = null;
        switch (type) {
            case EQUALS:
                if (leftOprand instanceof Identifier && rightOprand instanceof LiteralNull) {
                    return LiteralBoolean.FALSE;
                }
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
                    Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
                    left = pair.getKey();
                    right = pair.getValue();
                }
                return left.equals(right) ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
            case GREATER_THAN:
                if (leftOprand instanceof Identifier) {
                    idObj = leftOprand;
                    val = right;
                }
                if (rightOprand instanceof Identifier) {
                    idObj = rightOprand;
                    val = left;
                }
                if (idObj != null) {
                    Identifier id = (Identifier)idObj;
                    String idStr = new String(id.getIdText());
                    if (parameters != null) {
                        Object obj = parameters.get(idStr);
                        if (obj != null) {
                            if (obj instanceof ColumnDefinition) {
                                ColumnDefinition def = (ColumnDefinition)obj;
                                DataType dt = def.getDataType();
                                try {
                                    long max = ImplicitConversionUtil.getMaxByType(dt);
                                    if (val instanceof BigDecimal) {
                                        BigDecimal maxVal = null;
                                        if (max < 0) {
                                            maxVal = new BigDecimal(LongUtil.convert2UnsignedInteger(max));
                                        } else {
                                            maxVal = new BigDecimal(max);
                                        }
                                        if (((BigDecimal)val).compareTo(maxVal) > 0) {
                                            return false;
                                        }
                                    } else if (val instanceof BigInteger) {
                                        BigInteger maxVal = null;
                                        if (max < 0) {
                                            maxVal = LongUtil.convert2UnsignedInteger(max);
                                        } else {
                                            maxVal = BigInteger.valueOf(max);
                                        }
                                        if (((BigInteger)val).compareTo(maxVal) > 0) {
                                            return false;
                                        }
                                    } else {
                                        if (val instanceof Number) {
                                            long v = ((Number)val).longValue();
                                            if (max > 0 && v > max) {
                                                return false;
                                            }
                                        }
                                    }
                                    long min = ImplicitConversionUtil.getMinByType(dt);
                                    if (val instanceof BigDecimal) {
                                        BigDecimal minVal = new BigDecimal(min);
                                        if (((BigDecimal)val).compareTo(minVal) < 0) {
                                            return true;
                                        }
                                    } else if (val instanceof BigInteger) {
                                        BigInteger minVal = BigInteger.valueOf(min);
                                        if (((BigInteger)val).compareTo(minVal) < 0) {
                                            return true;
                                        }
                                    } else {
                                        if (val instanceof Number) {
                                            long v = ((Number)val).longValue();
                                            if (v < min) {
                                                return true;
                                            }
                                        }
                                    }
                                } catch (IllegalArgumentException e) {
                                    //todo
                                } catch (SQLSyntaxErrorException e) {
                                    //todo
                                }
                            }
                        }
                    }
                }

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
                if (left instanceof String && right instanceof String) {
                    return ((String)left).compareTo((String)right) > 0 ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
                } else {
                    Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
                    left = pair.getKey();
                    right = pair.getValue();
                }
                if (left instanceof BigInteger && right instanceof BigInteger) {
                    return ((BigInteger)left).compareTo(((BigInteger)right)) == 1 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else if (left instanceof BigDecimal && right instanceof BigDecimal) {
                    return ((BigDecimal)left).compareTo(((BigDecimal)right)) == 1 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else {
                    return ((Number)left).longValue() > (((Number)right)).longValue() ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                }
            case GREATER_THAN_OR_QEUALS:
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
                if (left instanceof String && right instanceof String) {
                    return ((String)left).compareTo((String)right) < 0 ? LiteralBoolean.FALSE : LiteralBoolean.TRUE;
                } else {
                    Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
                    left = pair.getKey();
                    right = pair.getValue();
                }
                if (left instanceof BigInteger && right instanceof BigInteger) {
                    return ((BigInteger)left).compareTo(((BigInteger)right)) >= 0 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else if (left instanceof BigDecimal && right instanceof BigDecimal) {
                    return ((BigDecimal)left).compareTo(((BigDecimal)right)) >= 0 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else {
                    return ((Number)left).longValue() >= (((Number)right)).longValue() ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                }
            case LESS_OR_GREATER_THAN:
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
                if (left instanceof String && right instanceof String) {
                    return ((String)left).compareTo((String)right) != 0 ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
                } else {
                    Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
                    left = pair.getKey();
                    right = pair.getValue();
                }
                if (left instanceof BigInteger && right instanceof BigInteger) {
                    return ((BigInteger)left).compareTo(((BigInteger)right)) != 0 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else if (left instanceof BigDecimal && right instanceof BigDecimal) {
                    return ((BigDecimal)left).compareTo(((BigDecimal)right)) != 0 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else {
                    return ((Number)left).longValue() != (((Number)right)).longValue() ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                }
            case LESS_THAN:
                if (leftOprand instanceof Identifier) {
                    idObj = leftOprand;
                    val = right;
                }
                if (rightOprand instanceof Identifier) {
                    idObj = rightOprand;
                    val = left;
                }
                if (idObj != null) {
                    Identifier id = (Identifier)idObj;
                    String idStr = new String(id.getIdText());
                    if (parameters != null) {
                        Object obj = parameters.get(idStr);
                        if (obj != null) {
                            if (obj instanceof ColumnDefinition) {
                                ColumnDefinition def = (ColumnDefinition)obj;
                                DataType dt = def.getDataType();
                                try {
                                    long max = ImplicitConversionUtil.getMaxByType(dt);
                                    if (val instanceof BigDecimal) {
                                        BigDecimal maxVal = null;
                                        if (max < 0) {
                                            maxVal = new BigDecimal(LongUtil.convert2UnsignedInteger(max));
                                        } else {
                                            maxVal = new BigDecimal(max);
                                        }
                                        if (((BigDecimal)val).compareTo(maxVal) > 0) {
                                            return true;
                                        }
                                    } else if (val instanceof BigInteger) {
                                        BigInteger maxVal = null;
                                        if (max < 0) {
                                            maxVal = LongUtil.convert2UnsignedInteger(max);
                                        } else {
                                            maxVal = BigInteger.valueOf(max);
                                        }
                                        if (((BigInteger)val).compareTo(maxVal) > 0) {
                                            return true;
                                        }
                                    } else {
                                        if (val instanceof Number) {
                                            long v = ((Number)val).longValue();
                                            if (max > 0 && v > max) {
                                                return true;
                                            }
                                        }
                                    }
                                    long min = ImplicitConversionUtil.getMinByType(dt);
                                    if (val instanceof BigDecimal) {
                                        BigDecimal minVal = new BigDecimal(min);
                                        if (((BigDecimal)val).compareTo(minVal) < 0) {
                                            return false;
                                        }
                                    } else if (val instanceof BigInteger) {
                                        BigInteger minVal = BigInteger.valueOf(min);
                                        if (((BigInteger)val).compareTo(minVal) < 0) {
                                            return false;
                                        }
                                    } else {
                                        if (val instanceof Number) {
                                            long v = ((Number)val).longValue();
                                            if (v < min) {
                                                return false;
                                            }
                                        }
                                    }
                                } catch (IllegalArgumentException e) {
                                    //todo
                                } catch (SQLSyntaxErrorException e) {
                                    //todo
                                }
                            }
                        }
                    }
                }
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
                if (left instanceof String && right instanceof String) {
                    return ((String)left).compareTo((String)right) < 0 ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
                } else {
                    Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
                    left = pair.getKey();
                    right = pair.getValue();
                }
                if (left instanceof BigInteger && right instanceof BigInteger) {
                    return ((BigInteger)left).compareTo(((BigInteger)right)) < 0 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else if (left instanceof BigDecimal && right instanceof BigDecimal) {
                    return ((BigDecimal)left).compareTo(((BigDecimal)right)) < 0 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else {
                    return ((Number)left).longValue() < (((Number)right)).longValue() ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                }
            case LESS_THAN_OR_EQUALS:
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
                if (left instanceof String && right instanceof String) {
                    return ((String)left).compareTo((String)right) <= 0 ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
                } else {
                    Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
                    left = pair.getKey();
                    right = pair.getValue();
                }
                if (left instanceof BigInteger && right instanceof BigInteger) {
                    return ((BigInteger)left).compareTo(((BigInteger)right)) <= 0 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else if (left instanceof BigDecimal && right instanceof BigDecimal) {
                    return ((BigDecimal)left).compareTo(((BigDecimal)right)) <= 0 ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                } else {
                    return ((Number)left).longValue() <= (((Number)right)).longValue() ? LiteralBoolean.TRUE :
                        LiteralBoolean.FALSE;
                }
            case NOT_EQUALS:
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
                    Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
                    left = pair.getKey();
                    right = pair.getValue();
                }
                return left.equals(right) ? LiteralBoolean.FALSE : LiteralBoolean.TRUE;
            case NULL_SAFE_EQUALS:
                if (left == UNEVALUATABLE || right == UNEVALUATABLE)
                    return UNEVALUATABLE;
                if (left == null)
                    return right == null ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
                if (right == null)
                    return LiteralBoolean.FALSE;
                if (left instanceof Number || right instanceof Number) {
                    Pair<Number, Number> pair = ExprEvalUtils.convertNum2SameLevel(left, right);
                    left = pair.getKey();
                    right = pair.getValue();
                }
                return left.equals(right) ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;

        }
        return null;
    }

    @Override
    public boolean exisitConstantCompare() {
        if (leftOprand instanceof Literal && rightOprand instanceof Literal) {
            return true;
        }
        return leftOprand.exisitConstantCompare() || rightOprand.exisitConstantCompare();
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = super.replace(from, to);
        if (escape != null) {
            if (escape.equals(from)) {
                escape = (Expression)to;
                result = true;
            } else {
                result |= escape.replace(from, to);
            }
        }
        return result;
    }
}
