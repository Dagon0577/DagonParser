package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class RenameColumn implements AlterSpecification {
    private final Identifier oldColumn;
    private final Identifier newColumn;

    public RenameColumn(Identifier oldColumn, Identifier newColumn) {
        this.oldColumn = oldColumn;
        this.newColumn = newColumn;
    }

    public Identifier getOldColumn() {
        return oldColumn;
    }

    public Identifier getNewColumn() {
        return newColumn;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
