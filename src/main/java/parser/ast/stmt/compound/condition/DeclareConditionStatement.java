package parser.ast.stmt.compound.condition;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DeclareConditionStatement implements SQLStatement {
    private final Identifier name;
    private final ConditionValue value;

    public DeclareConditionStatement(Identifier name, ConditionValue value) {
        this.name = name;
        this.value = value;
    }

    public Identifier getName() {
        return name;
    }

    public ConditionValue getValue() {
        return value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DECLARE_CONDITION;
    }
}
