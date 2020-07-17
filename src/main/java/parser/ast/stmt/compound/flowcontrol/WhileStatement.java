package parser.ast.stmt.compound.flowcontrol;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class WhileStatement implements SQLStatement {
    private final Identifier beginLabel;
    private final Expression whileCondition;
    private final List<SQLStatement> statements;
    private final Identifier endLabel;

    public WhileStatement(Identifier beginLabel, Expression whileCondition, List<SQLStatement> statements,
        Identifier endLabel) {
        this.beginLabel = beginLabel;
        this.statements = statements;
        this.whileCondition = whileCondition;
        this.endLabel = endLabel;
    }

    public Identifier getBeginLabel() {
        return beginLabel;
    }

    public Expression getWhileCondition() {
        return whileCondition;
    }

    public List<SQLStatement> getStatements() {
        return statements;
    }

    public Identifier getEndLabel() {
        return endLabel;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.WHILE;
    }
}
