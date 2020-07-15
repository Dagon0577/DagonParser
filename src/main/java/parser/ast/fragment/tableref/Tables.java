package parser.ast.fragment.tableref;

import parser.ast.AST;
import parser.visitor.Visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class Tables implements TableReference {
    protected static List<TableReference> ensureListType(List<TableReference> tables) {
        if (tables instanceof ArrayList)
            return tables;
        return new ArrayList<TableReference>(tables);
    }

    private final List<TableReference> tables;

    public Tables(List<TableReference> tables) {
        this.tables = ensureListType(tables);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_REFS;
    }

    public List<TableReference> getTables() {
        return tables;
    }

    @Override
    public Object removeLastConditionElement() {
        if (tables != null && !tables.isEmpty()) {
            return tables.get(tables.size() - 1).removeLastConditionElement();
        }
        return null;
    }

    @Override
    public boolean isSingleTable() {
        if (tables == null) {
            return false;
        }
        int count = 0;
        TableReference first = null;
        for (TableReference ref : tables) {
            if (ref != null && 1 == ++count) {
                first = ref;
            }
        }
        return count == 1 && first.isSingleTable();
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        Iterator<TableReference> iter = tables.iterator();
        int i = 0;
        while (iter.hasNext()) {
            TableReference exp = iter.next();
            if (exp != null) {
                if (exp.equals(from)) {
                    tables.set(i, (TableReference)to);
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
        Iterator<TableReference> iter = tables.iterator();
        while (iter.hasNext()) {
            TableReference exp = iter.next();
            if (exp != null) {
                removed |= exp.removeSchema(schema);
            }
        }
        return removed;
    }

}

