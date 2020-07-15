package cn.hotdb.parser.ast.expression.primary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.hotdb.parser.ast.AST;
import cn.hotdb.parser.ast.expression.Expression;
import cn.hotdb.parser.ast.expression.PrimaryExpression;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:51:12
 * 
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
                    rowExprList.set(i, (Expression) to);
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
