package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class AlterIndex implements AlterSpecification {
    private final Identifier name;
    private final boolean visible;

    public AlterIndex(Identifier name, boolean visible) {
        this.name = name;
        this.visible = visible;
    }

    public Identifier getName() {
        return name;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
