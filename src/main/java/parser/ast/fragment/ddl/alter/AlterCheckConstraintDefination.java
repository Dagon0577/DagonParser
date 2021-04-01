package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;

/**
 * @author Dagon0577
 * @date 2021年04月01日
 */
public class AlterCheckConstraintDefination implements AlterSpecification {
    private final Identifier name;
    private final boolean enforced;

    public AlterCheckConstraintDefination(Identifier name, boolean enforced) {
        this.name = name;
        this.enforced = enforced;
    }

    public Identifier getName() {
        return name;
    }

    public boolean getEnforced() {
        return enforced;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
