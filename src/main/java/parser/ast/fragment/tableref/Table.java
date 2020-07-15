package parser.ast.fragment.tableref;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class Table implements TableReference {
    private Identifier table;
    private String alias;
    private List<IndexHint> hintList;
    private List<Identifier> partitions;

    public Table(Identifier table, String alias, List<IndexHint> hintList, List<Identifier> partitions) {
        this.table = table;
        this.alias = alias;
        this.partitions = partitions;
        if (hintList == null || hintList.isEmpty()) {
            this.hintList = Collections.emptyList();
        } else if (hintList instanceof ArrayList) {
            this.hintList = hintList;
        } else {
            this.hintList = new ArrayList<IndexHint>(hintList);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_FACTOR;
    }

    public Identifier getTable() {
        return table;
    }

    public String getAlias() {
        return alias;
    }

    public List<Identifier> getPartitions() {
        return partitions;
    }

    public List<IndexHint> getHintList() {
        return hintList;
    }

    @Override
    public Object removeLastConditionElement() {
        return null;
    }

    @Override
    public boolean isSingleTable() {
        return true;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (table != null) {
            if (table.equals(from)) {
                table = (Identifier)to;
                result = true;
            } else {
                result |= table.replace(from, to);
            }
        }
        if (partitions != null) {
            Iterator<Identifier> iter = partitions.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Identifier exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        partitions.set(i, (Identifier)to);
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
        if (table != null) {
            removed |= table.removeSchema(schema);
        }
        if (partitions != null) {
            Iterator<Identifier> iter = partitions.iterator();
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
