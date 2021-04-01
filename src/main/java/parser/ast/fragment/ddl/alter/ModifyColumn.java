package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.ColumnDefinition;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;
import parser.util.Pair;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class ModifyColumn implements AlterSpecification {
    private final Identifier name;
    private final ColumnDefinition definition;
    private final Pair<Boolean, Identifier> first;

    public ModifyColumn(Identifier name, ColumnDefinition definition,
            Pair<Boolean, Identifier> first) {
        this.name = name;
        this.definition = definition;
        this.first = first;
    }

    public Identifier getName() {
        return name;
    }

    public ColumnDefinition getDefinition() {
        return definition;
    }

    public Pair<Boolean, Identifier> getFirst() {
        return first;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
