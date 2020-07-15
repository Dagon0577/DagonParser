package parser.ast.expression;

import parser.ast.AST;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/6/23
 */
public abstract class PrimaryExpression implements Expression {
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    @Override
    public int getPrecedence() {
        return PRECEDENCE_PRIMARY;
    }

    @Override
    public boolean replace(AST from, AST to) {
        return false;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
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
        return false;
    }

    @Override
    public boolean exisitConstantOperation() {
        return false;
    }
}
