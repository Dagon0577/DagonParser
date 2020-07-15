package parser.ast.fragment.tableref;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class Join implements TableReference {
    public static final int INNER = 1;
    public static final int OUTER = 2;
    public static final int NATURAL = 3;
    public static final int STRAIGHT = 4;

    private static List<Identifier> ensureListType(List<Identifier> list) {
        if (list == null)
            return null;
        if (list.isEmpty())
            return Collections.emptyList();
        if (list instanceof ArrayList)
            return list;
        return new ArrayList<Identifier>(list);
    }

    private final int type;
    private TableReference leftTable;
    private TableReference rightTable;
    private Expression onCond;
    private List<Identifier> using;
    private boolean isLeft;
    private boolean isOuter;

    public Join(int type, TableReference leftTable, TableReference rightTable, Expression onCond,
        List<Identifier> using, boolean isLeft, boolean isOuter) {
        this.type = type;
        this.leftTable = leftTable;
        this.rightTable = rightTable;
        this.onCond = onCond;
        this.using = ensureListType(using);
        this.isLeft = isLeft;
        this.isOuter = isOuter;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return TableReference.PRECEDENCE_JOIN;
    }

    public int getType() {
        return type;
    }

    public TableReference getLeftTable() {
        return leftTable;
    }

    public TableReference getRightTable() {
        return rightTable;
    }

    public Expression getOnCond() {
        return onCond;
    }

    public List<Identifier> getUsing() {
        return using;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public boolean isOuter() {
        return isOuter;
    }

    @Override
    public Object removeLastConditionElement() {
        Object obj;
        if (onCond != null) {
            obj = onCond;
            onCond = null;
        } else if (using != null) {
            obj = using;
            using = null;
        } else {
            return null;
        }
        return obj;
    }

    @Override
    public boolean isSingleTable() {
        return false;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (leftTable != null) {
            if (leftTable.equals(from)) {
                leftTable = (TableReference)to;
                result = true;
            } else {
                result |= leftTable.replace(from, to);
            }
        }
        if (rightTable != null) {
            if (rightTable.equals(from)) {
                rightTable = (TableReference)to;
                result = true;
            } else {
                result |= rightTable.replace(from, to);
            }
        }
        if (onCond != null) {
            if (onCond.equals(from)) {
                onCond = (Expression)to;
                result = true;
            } else {
                result |= onCond.replace(from, to);
            }
        }
        if (using != null) {
            Iterator<Identifier> iter = using.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        using.set(i, (Identifier)to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        return result;

    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (leftTable != null) {
            removed |= leftTable.removeSchema(schema);
        }
        if (rightTable != null) {
            removed |= rightTable.removeSchema(schema);
        }
        if (onCond != null) {
            removed |= onCond.removeSchema(schema);
        }
        if (using != null) {
            Iterator<Identifier> iter = using.iterator();
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        return removed;

    }

}

