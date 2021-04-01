package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.ColumnDefinition;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;
import parser.util.Pair;
import java.util.List;

/**
 * <pre>
 *   | ADD [COLUMN] col_name column_definition
 *         [FIRST | AFTER col_name]
 *   | ADD [COLUMN] (col_name column_definition,...)
 * </pre>
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class AddColumn implements AlterSpecification {
    private final Pair<Boolean, Identifier> first;
    private final List<Pair<Identifier, ColumnDefinition>> columns;

    public AddColumn(Pair<Boolean, Identifier> first,
            List<Pair<Identifier, ColumnDefinition>> columns) {
        this.first = first;
        this.columns = columns;
    }

    public Pair<Boolean, Identifier> getFirst() {
        return first;
    }

    public List<Pair<Identifier, ColumnDefinition>> getColumns() {
        return columns;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
