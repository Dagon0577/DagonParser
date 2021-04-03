package parser.ast.stmt.dal.account;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import java.util.List;

/**
 *
 * @author Dagon0577
 * @date 2021年04月03日
 * 
 *       <pre>
 * DROP ROLE [IF EXISTS] role [, role ] ...
 *       </pre>
 */
public class DALDropRoleStatement implements SQLStatement {

    private static final long serialVersionUID = 2819689956178052032L;
    private final boolean ifExists;
    private final List<Expression> roles;

    public DALDropRoleStatement(boolean ifExists, List<Expression> roles) {
        this.ifExists = ifExists;
        this.roles = roles;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public List<Expression> getRoles() {
        return roles;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DROP_ROLE;
    }

}
