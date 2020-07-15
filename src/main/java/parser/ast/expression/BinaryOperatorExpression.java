package parser.ast.expression;

import parser.ast.AST;
import parser.ast.expression.primary.literal.Literal;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public abstract class BinaryOperatorExpression implements Expression {
    protected Expression leftOprand;
    protected Expression rightOprand;
    protected final int precedence;
    protected final boolean leftCombine;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    protected BinaryOperatorExpression(Expression leftOprand, Expression rightOprand, int precedence) {
        this(leftOprand, rightOprand, precedence, true);
    }

    protected BinaryOperatorExpression(Expression leftOprand, Expression rightOprand, int precedence,
        boolean leftCombine) {
        this.leftOprand = leftOprand;
        this.rightOprand = rightOprand;
        this.precedence = precedence;
        this.leftCombine = leftCombine;
    }

    public Expression getLeftOprand() {
        return leftOprand;
    }

    public Expression getRightOprand() {
        return rightOprand;
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }

    public boolean isLeftCombine() {
        return leftCombine;
    }

    public abstract String getOperator();

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (leftOprand != null) {
            if (leftOprand.equals(from)) {
                leftOprand = (Expression)to;
                result = true;
            } else {
                result |= leftOprand.replace(from, to);
            }
        }
        if (rightOprand != null) {
            if (rightOprand.equals(from)) {
                rightOprand = (Expression)to;
                result = true;
            } else {
                result |= rightOprand.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (leftOprand != null) {
            removed |= leftOprand.removeSchema(schema);
        }
        if (rightOprand != null) {
            removed |= rightOprand.removeSchema(schema);
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
            evaluationCache = evaluationInternal(parameters, sql);
            evaluated = true;
            return evaluationCache;
        }
        return evaluationInternal(parameters, sql);
    }

    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) throws Exception {
        return UNEVALUATABLE;
    }

    @Override
    public boolean exisitConstantCompare() {
        return leftOprand.exisitConstantCompare() || rightOprand.exisitConstantCompare();
    }

    @Override
    public boolean exisitConstantOperation() {
        if (leftOprand instanceof Literal && rightOprand instanceof Literal) {
            return true;
        }
        return leftOprand.exisitConstantOperation() || rightOprand.exisitConstantOperation();
    }
}
