package parser.ast.stmt.prepare;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * <pre>
 * {DEALLOCATE | DROP} PREPARE stmt_name
 * </pre>
 * 
 * @author Dagon0577
 * @date 2021年04月03日
 * 
 */
public class DeallocatePrepareStatement implements SQLStatement {
    private final Identifier name;

    public DeallocatePrepareStatement(Identifier name) {
        this.name = name;
    }

    public Identifier getName() {
        return name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DEALLOCATE_PREPARE;
    }

}
