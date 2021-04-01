package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class OrderByColumns implements AlterSpecification {
    private final List<Identifier> columns;

    public OrderByColumns(List<Identifier> columns) {
        this.columns = columns;
    }

    public List<Identifier> getColumns() {
        return columns;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
