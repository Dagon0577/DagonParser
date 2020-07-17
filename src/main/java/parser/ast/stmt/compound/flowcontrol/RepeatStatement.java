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
public class RepeatStatement implements SQLStatement {
    private final Identifier beginLabel;
    private final List<SQLStatement> statements;
    private final Expression utilCondition;
    private final Identifier endLabel;

    public RepeatStatement(Identifier beginLabel, List<SQLStatement> statements, Expression utilCondition,
        Identifier endLabel) {
        this.beginLabel = beginLabel;
        this.statements = statements;
        this.utilCondition = utilCondition;
        this.endLabel = endLabel;
    }

    public Identifier getBeginLabel() {
        return beginLabel;
    }

    public List<SQLStatement> getStatements() {
        return statements;
    }

    public Expression getUtilCondition() {
        return utilCondition;
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
        return SQLType.REPEAT;
    }
}
