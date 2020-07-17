package parser.ast.stmt.dal.resource;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DALSetResourceGroupStatement implements SQLStatement {
    private final Identifier name;
    private final List<Long> threadIds;

    public DALSetResourceGroupStatement(Identifier name, List<Long> threadIds) {
        this.name = name;
        this.threadIds = threadIds;
    }

    public Identifier getName() {
        return name;
    }

    public List<Long> getThreadIds() {
        return threadIds;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SET_RESOURCE;
    }

}
