package parser.ast.expression.primary;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.PrimaryExpression;
import parser.visitor.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class RowExpression extends PrimaryExpression {
    private List<Expression> rowExprList;

    public RowExpression(List<Expression> rowExprList) {
        if (rowExprList == null || rowExprList.isEmpty()) {
            this.rowExprList = Collections.emptyList();
        } else if (rowExprList instanceof ArrayList) {
            this.rowExprList = rowExprList;
        } else {
            this.rowExprList = new ArrayList<Expression>(rowExprList);
        }
    }

    /**
     * @return never null
     */
    public List<Expression> getRowExprList() {
        return rowExprList;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Expression getRowExpr(int index) {
        return rowExprList.get(index);
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        Iterator<Expression> iter = rowExprList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                if (exp.equals(from)) {
                    rowExprList.set(i, (Expression)to);
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
        boolean replaced = false;
        Iterator<Expression> iter = rowExprList.iterator();
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                replaced |= exp.removeSchema(schema);
            }
        }
        return replaced;
    }

    public void setRowExprList(List<Expression> rowValues) {
        this.rowExprList = rowValues;
    }
}
