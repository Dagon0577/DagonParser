package parser.ast.fragment.ddl.alter;

import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.util.Tuple3;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2021年04月01日
 */
public class AddCheckConstraintDefinition implements AlterSpecification {
    private final Tuple3<Identifier, Expression, Boolean> checkConstraintDefinition;

    public AddCheckConstraintDefinition(
            Tuple3<Identifier, Expression, Boolean> checkConstraintDefinition) {
        this.checkConstraintDefinition = checkConstraintDefinition;
    }

    public Tuple3<Identifier, Expression, Boolean> getCheckConstraintDefinition() {
        return checkConstraintDefinition;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
