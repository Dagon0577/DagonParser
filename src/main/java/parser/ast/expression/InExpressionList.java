package parser.ast.expression;

import parser.ast.AST;
import parser.visitor.Visitor;

import java.util.*;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class InExpressionList implements Expression {
    private List<Expression> list;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    public InExpressionList(List<Expression> list) {
        if (list == null || list.size() == 0) {
            this.list = Collections.emptyList();
        } else if (list instanceof ArrayList) {
            this.list = list;
        } else {
            this.list = new ArrayList<Expression>(list);
        }
    }

    public List<Expression> getList() {
        return list;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_PRIMARY;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        Iterator<Expression> iter = list.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                if (exp.equals(from)) {
                    list.set(i, (Expression)to);
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
        Iterator<Expression> iter = list.iterator();
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
    public final Object evaluation(Map<? extends Object, ? extends Object> parameters, byte[] sql) {
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

    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters) {
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

