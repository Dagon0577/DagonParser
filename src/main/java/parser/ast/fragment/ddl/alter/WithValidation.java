package parser.ast.fragment.ddl.alter;

import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class WithValidation implements AlterSpecification {
    private final boolean without;

    public WithValidation(boolean without) {
        this.without = without;
    }

    public boolean isWithout() {
        return without;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
