package parser.ast.stmt.compound.flowcontrol;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class LoopStatement implements SQLStatement {

    private final Identifier beginLabel;
    private final List<SQLStatement> statements;
    private final Identifier endLabel;

    public LoopStatement(Identifier beginLabel, List<SQLStatement> statements, Identifier endLabel) {
        this.beginLabel = beginLabel;
        this.statements = statements;
        this.endLabel = endLabel;
    }

    public Identifier getBeginLabel() {
        return beginLabel;
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
        return SQLType.LOOP;
    }
}
