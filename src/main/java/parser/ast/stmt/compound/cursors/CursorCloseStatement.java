package parser.ast.stmt.compound.cursors;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class CursorCloseStatement implements SQLStatement {
    private final Identifier name;

    public CursorCloseStatement(Identifier name) {
        this.name = name;
    }

    public Identifier getName() {
        return this.name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CURSOR_CLOSE;
    }

}
