package parser.ast.expression;

import parser.ast.AST;
import parser.ast.expression.primary.literal.Literal;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class TernaryOperatorExpression implements Expression {
    public static final int BETWEEN = 1;
    public static final int LIKE = 2;

    private Expression first;
    private Expression second;
    private Expression third;
    private final int type;
    private final boolean not;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    public TernaryOperatorExpression(Expression first, Expression second, Expression third, int type, boolean not) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.type = type;
        this.not = not;
    }

    public Expression getFirst() {
        return first;
    }

    public Expression getSecond() {
        return second;
    }

    public Expression getThird() {
        return third;
    }

    public boolean isNot() {
        return not;
    }

    public int getType() {
        return type;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        switch (type) {
            case BETWEEN:
                return PRECEDENCE_BETWEEN_AND;
            case LIKE:
                return PRECEDENCE_COMPARISION;
        }
        return 0;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (first != null) {
            if (first.equals(from)) {
                first = (Expression)to;
                result = true;
            } else {
                result |= first.replace(from, to);
            }
        }
        if (second != null) {
            if (second.equals(from)) {
                second = (Expression)to;
                result = true;
            } else {
                result |= second.replace(from, to);
            }
        }
        if (third != null) {
            if (third.equals(from)) {
                third = (Expression)to;
                result = true;
            } else {
                result |= third.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (first != null) {
            removed |= first.removeSchema(schema);
        }
        if (second != null) {
            removed |= second.removeSchema(schema);
        }
        if (third != null) {
            removed |= third.removeSchema(schema);
        }
        return removed;
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
        if ((first instanceof Literal || first == null) && (second instanceof Literal || second == null) && (
            third instanceof Literal || third == null)) {
            return true;
        }
        boolean flag = false;
        if (first != null) {
            flag = flag || first.exisitConstantCompare();
        }
        if (second != null) {
            flag = flag || second.exisitConstantCompare();
        }
        if (third != null) {
            flag = flag || third.exisitConstantCompare();
        }
        return flag;
    }

    @Override
    public boolean exisitConstantOperation() {
        if ((first instanceof Literal || first == null) && (second instanceof Literal || second == null) && (
            third instanceof Literal || third == null)) {
            return true;
        }
        boolean flag = false;
        if (first != null) {
            flag = flag || first.exisitConstantOperation();
        }
        if (second != null) {
            flag = flag || second.exisitConstantOperation();
        }
        if (third != null) {
            flag = flag || third.exisitConstantOperation();
        }
        return flag;
    }
}

