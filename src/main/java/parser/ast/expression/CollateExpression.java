package parser.ast.expression;

import parser.ast.AST;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class CollateExpression implements Expression {
    private final String collateName;
    private Expression string;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    public CollateExpression(Expression string, String collateName) {
        this.string = string;
        this.collateName = collateName;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_COLLATE;
    }

    public String getCollateName() {
        return collateName;
    }

    public Expression getString() {
        return string;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (string != null) {
            if (string.equals(from)) {
                string = (Expression)to;
                result = true;
            } else {
                result |= string.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        if (string != null) {
            return string.removeSchema(schema);
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
        return string.evaluation(parameters, null);
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
