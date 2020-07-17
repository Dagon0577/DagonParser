package parser.ast.stmt.compound.condition;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class ConditionValue implements AST {
    public static final int MYSQL_ERROR_CODE = 1;
    public static final int SQLSTATE = 2;
    public static final int CONDITION_NAME = 3;
    public static final int SQLWARNING = 4;
    public static final int NOT_FOUND = 5;
    public static final int SQLEXCEPTION = 6;

    private int type;
    private Long mysqlErrorCode;
    private LiteralString sqlState;
    private Identifier conditionName;

    public ConditionValue(int type, Object value) {
        this.type = type;
        if (value == null) {
            return;
        }
        switch (type) {
            case MYSQL_ERROR_CODE:
                mysqlErrorCode = (Long)value;
                break;
            case SQLSTATE:
                sqlState = (LiteralString)value;
                break;
            case CONDITION_NAME:
                conditionName = (Identifier)value;
                break;
            default:
                break;
        }
    }

    public int getType() {
        return type;
    }

    public Object getValue() {
        switch (type) {
            case MYSQL_ERROR_CODE:
                return mysqlErrorCode;
            case SQLSTATE:
                return sqlState;
            case CONDITION_NAME:
                return conditionName;
        }
        return null;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
