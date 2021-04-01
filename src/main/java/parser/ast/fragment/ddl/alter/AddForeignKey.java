package parser.ast.fragment.ddl.alter;

import parser.ast.fragment.ddl.ForeignKeyDefinition;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class AddForeignKey implements AlterSpecification {
    private final ForeignKeyDefinition definition;

    public AddForeignKey(ForeignKeyDefinition definition) {
        this.definition = definition;
    }

    public ForeignKeyDefinition getDefinition() {
        return definition;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
