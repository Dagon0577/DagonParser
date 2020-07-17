package parser.ast.stmt.compound.condition;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import parser.util.Pair;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class GetDiagnosticsStatement implements SQLStatement {
    public static final int NUMBER = 1;
    public static final int ROW_COUNT = 2;

    public static final int COND_CLASS_ORIGIN = 1;
    public static final int COND_SUBCLASS_ORIGIN = 2;
    public static final int COND_RETURNED_SQLSTATE = 3;
    public static final int COND_MESSAGE_TEXT = 4;
    public static final int COND_MYSQL_ERRNO = 5;
    public static final int COND_CONSTRAINT_CATALOG = 6;
    public static final int COND_CONSTRAINT_SCHEMA = 7;
    public static final int COND_CONSTRAINT_NAME = 8;
    public static final int COND_CATALOG_NAME = 9;
    public static final int COND_SCHEMA_NAME = 10;
    public static final int COND_TABLE_NAME = 11;
    public static final int COND_COLUMN_NAME = 12;
    public static final int COND_CURSOR_NAME = 13;

    private final Boolean isCurrentOrStacked;
    private final List<Pair<Expression, Integer>> statementItems;
    private final Long conditionNumber;
    private final List<Pair<Expression, Integer>> conditionItems;

    public GetDiagnosticsStatement(Boolean isCurrentOrStacked, List<Pair<Expression, Integer>> statementItems,
        Long conditionNumber, List<Pair<Expression, Integer>> conditionItems) {
        this.isCurrentOrStacked = isCurrentOrStacked;
        this.statementItems = statementItems;
        this.conditionNumber = conditionNumber;
        this.conditionItems = conditionItems;
    }

    public Boolean getIsCurrentOrStacked() {
        return isCurrentOrStacked;
    }

    public List<Pair<Expression, Integer>> getStatementItems() {
        return statementItems;
    }

    public Long getConditionNumber() {
        return conditionNumber;
    }

    public List<Pair<Expression, Integer>> getConditionItems() {
        return conditionItems;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.GET_DIAGNOSTICS;
    }

}
