package parser.ast.expression.primary;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.PrimaryExpression;
import parser.visitor.Visitor;
import parser.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class CaseWhenExpression extends PrimaryExpression {
    private Expression comparee;
    private final List<Pair<Expression, Expression>> whenList;
    private Expression elseResult;

    public CaseWhenExpression(Expression comparee, List<Pair<Expression, Expression>> whenList, Expression elseResult) {
        this.comparee = comparee;
        if (whenList instanceof ArrayList) {
            this.whenList = whenList;
        } else {
            this.whenList = new ArrayList<Pair<Expression, Expression>>(whenList);
        }
        this.elseResult = elseResult;
    }

    public Expression getComparee() {
        return comparee;
    }

    public List<Pair<Expression, Expression>> getWhenList() {
        return whenList;
    }

    public Expression getElseResult() {
        return elseResult;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (comparee != null) {
            if (comparee.equals(from)) {
                comparee = (Expression)to;
                result = true;
            } else {
                result |= comparee.replace(from, to);
            }
        }
        for (Pair<Expression, Expression> p : whenList) {
            if (p.getKey() != null) {
                if (p.getKey().equals(from)) {
                    p.setKey((Expression)to);
                    result = true;
                } else {
                    result |= p.getKey().replace(from, to);
                }
            }
            if (p.getValue() != null) {
                if (p.getValue().equals(from)) {
                    p.setValue((Expression)to);
                    result = true;
                } else {
                    result |= p.getValue().replace(from, to);
                }
            }
        }
        if (elseResult != null) {
            if (elseResult.equals(from)) {
                elseResult = (Expression)to;
                result = true;
            } else {
                result |= elseResult.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (comparee != null) {
            removed |= comparee.removeSchema(schema);
        }
        for (Pair<Expression, Expression> p : whenList) {
            if (p.getKey() != null) {
                removed |= p.getKey().removeSchema(schema);
            }
            if (p.getValue() != null) {
                removed |= p.getValue().removeSchema(schema);
            }
        }
        if (elseResult != null) {
            removed |= elseResult.removeSchema(schema);
        }
        return removed;
    }
}
