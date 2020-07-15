package parser.ast.expression;

import parser.ast.AST;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class UnaryOperatorExpression implements Expression {
    public static final int MINUS = 1;
    public static final int BIT_INVERT = 2;
    public static final int NEGTATIVE_VALUE = 3;
    public static final int LOGIC_NOT = 4;
    public static final int CAST_BINARY = 5;
    public static final int SUBQUERY_ALL = 6;
    public static final int SUBQUERY_ANY = 7;

    private Expression operand;
    private final int type;
    private final int precedence;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    public UnaryOperatorExpression(Expression operand, int type) {
        this.operand = operand;
        this.type = type;
        this.precedence = getPrecedence(type);
    }

    private static int getPrecedence(int type) {
        switch (type) {
            case MINUS:
            case BIT_INVERT:
            case NEGTATIVE_VALUE:
                return PRECEDENCE_UNARY_OP;
            case LOGIC_NOT:
                return PRECEDENCE_LOGICAL_NOT;
            case CAST_BINARY:
                return PRECEDENCE_BINARY;
            case SUBQUERY_ALL:
            case SUBQUERY_ANY:
                return PRECEDENCE_PRIMARY;
        }
        return 0;
    }

    public Expression getOperand() {
        return operand;
    }

    public String getOperator() {
        switch (type) {
            case MINUS:
                return "-";
            case BIT_INVERT:
                return "~";
            case NEGTATIVE_VALUE:
                return "!";
            case LOGIC_NOT:
                return "NOT";
            case CAST_BINARY:
                return "BINARY";
            case SUBQUERY_ALL:
                return "ALL";
            case SUBQUERY_ANY:
                return "ANY";
        }
        return null;
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
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
    public final Object evaluation(Map<? extends Object, ? extends Object> parameters, byte[] sql) {
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

    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) {
        return UNEVALUATABLE;
    }

    @Override
    public boolean exisitConstantCompare() {
        return operand.exisitConstantCompare();
    }

    @Override
    public boolean exisitConstantOperation() {
        return operand.exisitConstantOperation();
    }
}

