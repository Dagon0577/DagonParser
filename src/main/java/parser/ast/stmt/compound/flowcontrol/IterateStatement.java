package parser.ast.stmt.compound.flowcontrol;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class IterateStatement implements SQLStatement {
    private final Identifier label;

    public IterateStatement(Identifier label) {
        this.label = label;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Identifier getLabel() {
        return this.label;
    }

    @Override
    public int getSQLType() {
        return SQLType.ITERATE;
    }
}
