package parser.ast.stmt.compound.condition;

import parser.SQLType;
import parser.ast.expression.primary.literal.Literal;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import parser.util.Pair;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class SignalStatement implements SQLStatement {

    private final ConditionValue conditionValue;
    private final List<Pair<Integer, Literal>> informationItems;

    public SignalStatement(ConditionValue conditionValue, List<Pair<Integer, Literal>> informationItems) {
        this.conditionValue = conditionValue;
        this.informationItems = informationItems;
    }

    public ConditionValue getConditionValue() {
        return conditionValue;
    }

    public List<Pair<Integer, Literal>> getInformationItems() {
        return informationItems;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SIGNAL;
    }

}
