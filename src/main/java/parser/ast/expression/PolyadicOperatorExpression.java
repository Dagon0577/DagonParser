package parser.ast.expression;

import parser.ast.AST;
import parser.ast.expression.primary.literal.Literal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public abstract class PolyadicOperatorExpression implements Expression {
    protected List<Expression> operands;
    protected final int precedence;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    public PolyadicOperatorExpression(int precedence) {
        this(precedence, true);
    }

    public PolyadicOperatorExpression(int precedence, boolean leftCombine) {
        this(precedence, 4);
    }

    public PolyadicOperatorExpression(int precedence, int initArity) {
        this.precedence = precedence;
        this.operands = new ArrayList<Expression>(initArity);
    }

    public PolyadicOperatorExpression appendOperand(Expression operand) {
        if (operand == null)
            return this;
        if (getClass().isAssignableFrom(operand.getClass()) && precedence == operand.getPrecedence()) {
            PolyadicOperatorExpression sub = (PolyadicOperatorExpression)operand;
            operands.addAll(sub.operands);
        } else {
            operands.add(operand);
        }
        return this;
    }

    public Expression getOperand(int index) {
        if (index >= operands.size()) {
            throw new IllegalArgumentException(
                "only contains " + operands.size() + " operands," + index + " is out of bound");
        }
        return operands.get(index);
    }

    public int getArity() {
        return operands.size();
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }

    public abstract String getOperator();

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        Iterator<Expression> iter = operands.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                if (exp.equals(from)) {
                    operands.set(i, (Expression)to);
                    result = true;
                } else {
                    result |= exp.replace(from, to);
                }
            }
            i++;
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        Iterator<Expression> iter = operands.iterator();
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                removed |= exp.removeSchema(schema);
            }
        }
        return removed;
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
            evaluationCache = evaluationInternal(parameters);
            evaluated = true;
            return evaluationCache;
        }
        return evaluationInternal(parameters);
    }

    protected Object evaluationInternal(Map<? extends Object, ? extends Object> parameters) throws Exception {
        return UNEVALUATABLE;
    }

    @Override
    public boolean exisitConstantCompare() {
        boolean flag = false;
        for (Expression operand : operands) {
            if (operand != null) {
                flag = flag || operand.exisitConstantCompare();
            }
        }
        return flag;
    }

    @Override
    public boolean exisitConstantOperation() {
        boolean flag = true;
        for (Expression operand : operands) {
            if (operand != null) {
                flag = flag && operand instanceof Literal;
            }
        }
        if (flag) {
            return flag;
        }
        flag = false;
        for (Expression operand : operands) {
            if (operand != null) {
                flag = flag || operand.exisitConstantOperation();
            }
        }
        return flag;
    }
}

