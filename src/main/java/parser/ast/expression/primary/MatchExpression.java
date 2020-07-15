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
 * @date 2018年11月16日 下午5:50:34
 * 
 */
public class MatchExpression extends PrimaryExpression {
    public static final int _DEFAULT = 0;
    public static final int IN_BOOLEAN_MODE = 1;
    public static final int IN_NATURAL_LANGUAGE_MODE = 2;
    public static final int IN_NATURAL_LANGUAGE_MODE_WITH_QUERY_EXPANSION = 3;
    public static final int WITH_QUERY_EXPANSION = 4;

    private final List<Expression> columns;
    private Expression pattern;
    private final int modifier;

    public MatchExpression(List<Expression> columns, Expression pattern, int modifier) {
        if (columns == null || columns.isEmpty()) {
            this.columns = Collections.emptyList();
        } else if (columns instanceof ArrayList) {
            this.columns = columns;
        } else {
            this.columns = new ArrayList<Expression>(columns);
        }
        this.pattern = pattern;
        this.modifier = modifier;
    }

    public List<Expression> getColumns() {
        return columns;
    }

    public Expression getPattern() {
        return pattern;
    }

    public int getModifier() {
        return modifier;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        Iterator<Expression> iter = columns.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                if (exp.equals(from)) {
                    columns.set(i, (Expression) to);
                    result = true;
                } else {
                    result |= exp.replace(from, to);
                }
            }
            i++;
        }
        if (pattern != null) {
            if (pattern.equals(from)) {
                pattern = (Expression) to;
                result = true;
            } else {
                result |= pattern.replace(from, to);
            }
        }
        return result;

    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return false;
    }
}
