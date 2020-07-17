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
public class IfStatement implements SQLStatement {
    private final List<Pair<Expression, List<SQLStatement>>> ifStatements;
    private final List<SQLStatement> elseStatement;

    public IfStatement(List<Pair<Expression, List<SQLStatement>>> ifStatements, List<SQLStatement> elseStatements) {
        this.ifStatements = ifStatements;
        this.elseStatement = elseStatements;
    }

    public List<Pair<Expression, List<SQLStatement>>> getIfStatements() {
        return ifStatements;
    }

    public List<SQLStatement> getElseStatement() {
        return elseStatement;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.IF;
    }
}
