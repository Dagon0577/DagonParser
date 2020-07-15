package parser.ast.fragment.tableref;

import parser.ast.AST;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class Dual implements TableReference {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE_FACTOR;
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
        return false;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return false;
    }

}

