package parser.ast.stmt.dal.resource;

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
public class DALCreateResourceGroupStatement implements SQLStatement {
    private final Identifier name;
    private final boolean systemOrUser;
    private final List<Expression> vcpus;
    private final Long threadPriority;
    private final Boolean enable;

    public DALCreateResourceGroupStatement(Identifier name, boolean systemOrUser, List<Expression> vcpus,
        Long threadPriority, Boolean enable) {
        this.name = name;
        this.systemOrUser = systemOrUser;
        this.vcpus = vcpus;
        this.threadPriority = threadPriority;
        this.enable = enable;
    }

    public Identifier getName() {
        return name;
    }

    public boolean isSystemOrUser() {
        return systemOrUser;
    }

    public List<Expression> getVcpus() {
        return vcpus;
    }

    public Long getThreadPriority() {
        return threadPriority;
    }

    public Boolean getEnable() {
        return enable;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CREATE_RESOURCE;
    }

}
