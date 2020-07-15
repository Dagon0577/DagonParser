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
public class GroupBy implements AST {
    private final List<Pair<Expression, Boolean>> orderByList;
    private boolean withRollup;

    public GroupBy(Expression expr, boolean asc, boolean withRollup) {
        this.orderByList = new ArrayList<Pair<Expression, Boolean>>(1);
        this.orderByList.add(new Pair<Expression, Boolean>(expr, asc));
        this.withRollup = withRollup;
    }

    public GroupBy() {
        this.orderByList = new LinkedList<Pair<Expression, Boolean>>();
    }

    public List<Pair<Expression, Boolean>> getOrderByList() {
        return this.orderByList;
    }

    public GroupBy addOrderByItem(Expression expr, Boolean asc) {
        orderByList.add(new Pair<Expression, Boolean>(expr, asc));
        return this;
    }

    public boolean isWithRollup() {
        return withRollup;
    }

    public GroupBy setWithRollup() {
        withRollup = true;
        return this;
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

