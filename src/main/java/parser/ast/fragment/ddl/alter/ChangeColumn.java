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
public class ChangeColumn implements AlterSpecification {
    private final Identifier oldColumn;
    private final Identifier newColumn;
    private final ColumnDefinition definition;
    private final Pair<Boolean, Identifier> first;

    public ChangeColumn(Identifier oldColumn, Identifier newColumn, ColumnDefinition definition,
            Pair<Boolean, Identifier> first) {
        this.oldColumn = oldColumn;
        this.newColumn = newColumn;
        this.definition = definition;
        this.first = first;
    }

    public Identifier getOldColumn() {
        return oldColumn;
    }

    public Identifier getNewColumn() {
        return newColumn;
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
