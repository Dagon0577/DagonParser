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
                    columns.set(i, (Expression)to);
                    result = true;
                } else {
                    result |= exp.replace(from, to);
                }
            }
            i++;
        }
        if (pattern != null) {
            if (pattern.equals(from)) {
                pattern = (Expression)to;
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
