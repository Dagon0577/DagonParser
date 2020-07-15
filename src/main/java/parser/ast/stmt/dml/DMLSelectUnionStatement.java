package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.visitor.Visitor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class DMLSelectUnionStatement extends DMLQueryStatement {
    private final List<DMLSelectStatement> selects;
    private int firstDistinctIndex = 0;
    private OrderBy orderBy;
    private Limit limit;

    public DMLSelectUnionStatement(DMLSelectStatement select) {
        this.selects = new LinkedList<>();
        this.selects.add(select);
    }

    public DMLSelectUnionStatement addSelect(DMLSelectStatement select, boolean unionAll) {
        this.selects.add(select);
        if (!unionAll) {
            firstDistinctIndex = selects.size() - 1;
        }
        return this;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public List<DMLSelectStatement> getSelects() {
        return selects;
    }

    public int getFirstDistinctIndex() {
        return firstDistinctIndex;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SELECT;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (selects != null) {
            Iterator<DMLSelectStatement> iter = selects.iterator();
            int i = 0;
            while (iter.hasNext()) {
                DMLSelectStatement exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        selects.set(i, (DMLSelectStatement)to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        if (orderBy != null) {
            if (orderBy.equals(from)) {
                orderBy = (OrderBy)to;
                result = true;
            } else {
                result |= orderBy.replace(from, to);
            }
        }
        if (limit != null) {
            if (limit.equals(from)) {
                limit = (Limit)to;
                result = true;
            } else {
                result |= limit.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (selects != null) {
            Iterator<DMLSelectStatement> iter = selects.iterator();
            while (iter.hasNext()) {
                DMLSelectStatement exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        if (orderBy != null) {
            removed |= orderBy.removeSchema(schema);
        }
        return removed;
    }
}

