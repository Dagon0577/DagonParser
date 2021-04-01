package parser.ast.fragment.ddl.alter;

import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class EnableKeys implements AlterSpecification {
    private final boolean enable;

    public EnableKeys(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
