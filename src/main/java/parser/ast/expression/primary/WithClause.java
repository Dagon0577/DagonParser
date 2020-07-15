package parser.ast.expression.primary;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.stmt.dml.DMLQueryStatement;
import parser.visitor.Visitor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/14
 *
 * <pre>
 *  with_clause:
 *      WITH [RECURSIVE]
 *          cte_name [(col_name [, col_name] ...)] AS (subquery)
 *          [, cte_name [(col_name [, col_name] ...)] AS (subquery)] ...
 *       </pre>
 */
public class WithClause implements Expression {
    public static final class CTE implements AST {
        public Identifier name;
        public List<Identifier> columns;
        public DMLQueryStatement subquery;

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        public boolean replace(AST from, AST to) {
            boolean result = false;
            if (name != null) {
                if (name.equals(from)) {
                    name = (Identifier)to;
                    result = true;
                } else {
                    result |= name.replace(from, to);
                }
            }
            Iterator<Identifier> iter = columns.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        columns.set(i, (Identifier)to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
            if (subquery != null) {
                if (subquery.equals(from)) {
                    subquery = (DMLQueryStatement)to;
                    result = true;
                } else {
                    result |= subquery.replace(from, to);
                }
            }
            return result;
        }

        public boolean removeSchema(byte[] schema) {
            boolean removed = false;
            if (name != null) {
                removed |= name.removeSchema(schema);
            }
            Iterator<Identifier> iter = columns.iterator();
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
            if (subquery != null) {
                removed |= subquery.removeSchema(schema);
            }
            return removed;
        }
    }

    private boolean isRecursive;
    private List<CTE> ctes;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    public WithClause(boolean isRecursive, List<CTE> ctes) {
        this.isRecursive = isRecursive;
        this.ctes = ctes;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public List<CTE> getCtes() {
        return ctes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return Expression.PRECEDENCE_PRIMARY;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        Iterator<CTE> iter = ctes.iterator();
        int i = 0;
        while (iter.hasNext()) {
            CTE exp = iter.next();
            if (exp != null) {
                if (exp.equals(from)) {
                    ctes.set(i, (CTE)to);
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
        Iterator<CTE> iter = ctes.iterator();
        while (iter.hasNext()) {
            CTE exp = iter.next();
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
        List<CTE> cteList = getCtes();
        for (CTE cte : cteList) {
            if (cte.subquery != null) {
                if (cte.subquery.exisitConstantCompare()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean exisitConstantOperation() {
        List<CTE> cteList = getCtes();
        for (CTE cte : cteList) {
            if (cte.subquery != null) {
                if (cte.subquery.exisitConstantOperation()) {
                    return true;
                }
            }
        }
        return false;
    }
}

