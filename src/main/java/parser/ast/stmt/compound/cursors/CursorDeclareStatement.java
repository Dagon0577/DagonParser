package parser.ast.stmt.compound.cursors;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class CursorDeclareStatement implements SQLStatement {
    private final Identifier name;
    private final SQLStatement stmt;

    public CursorDeclareStatement(Identifier name, SQLStatement stmt) {
        this.name = name;
        this.stmt = stmt;
    }

    public Identifier getName() {
        return this.name;
    }

    public SQLStatement getStmt() {
        return this.stmt;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CURSOR_DECLARE;
    }
}
