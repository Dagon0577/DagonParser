package parser.ast.stmt.compound.cursors;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.VarsPrimary;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class CursorFetchStatement implements SQLStatement {
    private final Identifier name;
    private final List<VarsPrimary> varNames;

    public CursorFetchStatement(Identifier name, List<VarsPrimary> varNames) {
        this.name = name;
        this.varNames = varNames;
    }

    public Identifier getName() {
        return this.name;
    }

    public List<VarsPrimary> getVarNames() {
        return varNames;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CURSOR_FETCH;
    }

}
