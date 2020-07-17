package parser.ast.stmt.transactional;

import parser.SQLType;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class BeginStatement implements TrxStatement {

    private boolean isWork;

    public BeginStatement(boolean isWork) {
        this.isWork = isWork;
    }

    public boolean isWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.BEGIN;
    }

}
