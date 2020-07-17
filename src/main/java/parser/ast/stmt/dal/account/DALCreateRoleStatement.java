package parser.ast.stmt.dal.account;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DALCreateRoleStatement implements SQLStatement {
    private final boolean ifNotExists;
    private final List<Expression> roles;

    public DALCreateRoleStatement(boolean ifNotExists, List<Expression> roles) {
        this.ifNotExists = ifNotExists;
        this.roles = roles;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
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
        return SQLType.CREATE_ROLE;
    }

}
