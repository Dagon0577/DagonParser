package parser.ast.stmt.compound.flowcontrol;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.stmt.SQLStatement;
import parser.util.Pair;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class CaseStatement implements SQLStatement {
    private final Expression caseValue;
    private final List<Pair<Expression, SQLStatement>> whenList;
    private final SQLStatement elseStmt;

    public CaseStatement(Expression caseValue, List<Pair<Expression, SQLStatement>> whenList, SQLStatement elseStmt) {
        this.caseValue = caseValue;
        this.whenList = whenList;
        this.elseStmt = elseStmt;
    }

    public Expression getCaseValue() {
        return caseValue;
    }

    public List<Pair<Expression, SQLStatement>> getWhenList() {
        return whenList;
    }

    public SQLStatement getElseStmt() {
        return elseStmt;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CASE;
    }
}
