package parser.ast.fragment;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.util.Pair;
import parser.visitor.Visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class OrderBy implements AST {

    private List<Pair<Expression, Boolean>> orderByList;

    public OrderBy(Expression expr, boolean asc) {
        this.orderByList = new ArrayList<Pair<Expression, Boolean>>(1);
        this.orderByList.add(new Pair<Expression, Boolean>(expr, asc));
    }

    public OrderBy() {
        this.orderByList = new LinkedList<Pair<Expression, Boolean>>();
    }

    public OrderBy addOrderByItem(Expression expr, boolean asc) {
        orderByList.add(new Pair<Expression, Boolean>(expr, asc));
        return this;
    }

    public List<Pair<Expression, Boolean>> getOrderByList() {
        return orderByList;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public boolean replace(AST from, AST to) {
        boolean result = false;
        for (Pair<Expression, Boolean> p : orderByList) {
            if (p.getKey() != null) {
                if (p.getKey().equals(from)) {
                    p.setKey((Expression)to);
                    result = true;
                } else {
                    result |= p.getKey().replace(from, to);
                }
            }
        }
        return result;
    }

    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        for (Pair<Expression, Boolean> p : orderByList) {
            if (p.getKey() != null) {
                removed |= p.getKey().removeSchema(schema);
            }
        }
        return removed;
    }

}

