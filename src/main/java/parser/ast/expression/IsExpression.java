package parser.ast.expression;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralBoolean;
import parser.ast.expression.primary.literal.LiteralNull;
import parser.ast.expression.primary.literal.LiteralNumber;
import parser.ast.fragment.ddl.ColumnDefinition;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class IsExpression implements Expression {
    public static final int IS_NULL = 1;
    public static final int IS_TRUE = 2;
    public static final int IS_FALSE = 3;
    public static final int IS_UNKNOWN = 4;
    public static final int IS_NOT_NULL = 5;
    public static final int IS_NOT_TRUE = 6;
    public static final int IS_NOT_FALSE = 7;
    public static final int IS_NOT_UNKNOWN = 8;

    private final int mode;
    private Expression operand;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    public IsExpression(Expression operand, int mode) {
        this.operand = operand;
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public Expression getOperand() {
        return operand;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_COMPARISION;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (operand != null) {
            if (operand.equals(from)) {
                operand = (Expression)to;
                result = true;
            } else {
                result |= operand.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        if (operand != null) {
            return operand.removeSchema(schema);
        }
        return false;
    }

    @Override
    public Expression setCacheEvalRst(boolean cacheEvalRst) {
        this.cacheEvalRst = cacheEvalRst;
        return this;
    }

    @Override
    public final Object evaluation(Map<? extends Object, ? extends Object> parameters, byte[] sql) throws Exception {
        if (cacheEvalRst) {
            if (evaluated) {
                return evaluationCache;
            }
            evaluationCache = evaluationInternal(parameters, sql);
            evaluated = true;
            return evaluationCache;
        }
        return evaluationInternal(parameters, sql);
    }

    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) throws Exception {
        Object val = operand.evaluation(parameters, sql);
        if (operand instanceof Identifier && mode == IS_NULL) {
            Identifier id = (Identifier)operand;
            String idStr = new String(id.getIdText());
            if (parameters != null) {
                Object parm = parameters.get(idStr);
                if (parm != null) {
                    if (parm instanceof ColumnDefinition) {
                        ColumnDefinition def = (ColumnDefinition)parm;
                        if (def.isNotNull()) {
                            return 0;
                        }
                    }
                }
            }
        }
        if (val instanceof LiteralNull) {
            val = ((LiteralNull)val).evaluation(parameters, sql);
        } else if (val instanceof LiteralNumber) {
            val = ((LiteralNumber)val).evaluation(parameters, sql);
        } else if (val instanceof LiteralBoolean) {
            val = ((LiteralBoolean)val).evaluation(parameters, sql);
        }
        switch (mode) {
            case IS_NULL:
                return val == null ? true : UNEVALUATABLE;
            case IS_TRUE:
            case IS_NOT_FALSE:
                if (val instanceof Number) {
                    if (((Number)val).doubleValue() > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else if (val instanceof Boolean) {
                    if (((Boolean)val).booleanValue()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
                break;
            case IS_FALSE:
            case IS_NOT_TRUE:
                if (val instanceof Number) {
                    if (((Number)val).doubleValue() > 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (val instanceof Boolean) {
                    if (((Boolean)val).booleanValue()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
                break;
            case IS_NOT_NULL:
                return val == null ? false : UNEVALUATABLE;
        }
        return UNEVALUATABLE;
    }

    @Override
    public boolean exisitConstantCompare() {
        return false;
    }

    @Override
    public boolean exisitConstantOperation() {
        return false;
    }
}

