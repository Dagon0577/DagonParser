package parser.ast.stmt.compound.condition;

import parser.SQLType;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DeclareHandlerStatement implements SQLStatement {
    public static final int CONTINUE = 1;
    public static final int EXIT = 2;
    public static final int UNDO = 3;

    private final int action;
    private final List<ConditionValue> conditionValues;
    private final SQLStatement stmt;

    public DeclareHandlerStatement(int action, List<ConditionValue> conditionValues, SQLStatement stmt) {
        this.action = action;
        this.conditionValues = conditionValues;
        this.stmt = stmt;
    }

    public int getAction() {
        return action;
    }

    public List<ConditionValue> getConditionValues() {
        return conditionValues;
    }

    public SQLStatement getStmt() {
        return stmt;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DECLARE_HANDLER;
    }
}
