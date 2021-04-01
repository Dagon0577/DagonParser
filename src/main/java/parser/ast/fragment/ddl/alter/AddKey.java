package parser.ast.fragment.ddl.alter;

import parser.ast.fragment.ddl.IndexDefinition;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class AddKey implements AlterSpecification {
    private final IndexDefinition definition;

    public AddKey(IndexDefinition definition) {
        this.definition = definition;
    }

    public IndexDefinition getDefinition() {
        return definition;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
