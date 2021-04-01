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
public class RenameIndex implements AlterSpecification {
    private final Identifier oldIndex;
    private final Identifier newIndex;

    public RenameIndex(Identifier oldIndex, Identifier newIndex) {
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    public Identifier getOldIndex() {
        return oldIndex;
    }

    public Identifier getNewIndex() {
        return newIndex;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
