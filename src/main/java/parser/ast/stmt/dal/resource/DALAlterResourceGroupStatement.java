package parser.ast.stmt.dal.resource;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER RESOURCE GROUP group_name
 *     [VCPU [=] vcpu_spec [, vcpu_spec] ...]
 *     [THREAD_PRIORITY [=] N]
 *     [ENABLE|DISABLE [FORCE]]
 * 
 * vcpu_spec: {N | M - N}
 *       </pre>
 */
public class DALAlterResourceGroupStatement implements SQLStatement {
    private final Identifier name;
    private final List<Expression> vcpus;
    private final Long threadPriority;
    private final Boolean enable;
    private final boolean force;

    public DALAlterResourceGroupStatement(Identifier name, List<Expression> vcpus,
            Long threadPriority, Boolean enable, boolean force) {
        this.name = name;
        this.vcpus = vcpus;
        this.threadPriority = threadPriority;
        this.enable = enable;
        this.force = force;
    }

    public Identifier getName() {
        return name;
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

    public boolean isForce() {
        return force;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.ALTER_RESOURCE;
    }

}
