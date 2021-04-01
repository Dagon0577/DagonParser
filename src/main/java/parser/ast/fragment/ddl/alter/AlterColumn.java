package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.Literal;
import parser.visitor.Visitor;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class AlterColumn implements AlterSpecification {
    private final Identifier name;
    private final boolean dropDefault;
    private final Literal defaultVal;

    public AlterColumn(Identifier name, boolean dropDefault, Literal defaultVal) {
        this.name = name;
        this.dropDefault = dropDefault;
        this.defaultVal = defaultVal;
    }

    public Identifier getName() {
        return name;
    }

    public boolean isDropDefault() {
        return dropDefault;
    }

    public Literal getDefaultVal() {
        return defaultVal;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
