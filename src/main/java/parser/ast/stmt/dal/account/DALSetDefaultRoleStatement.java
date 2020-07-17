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
public class DALSetDefaultRoleStatement implements SQLStatement {
    private final Boolean noneOrAll;
    private final List<Expression> roles;
    private final List<Expression> users;

    public DALSetDefaultRoleStatement(Boolean noneOrAll, List<Expression> roles, List<Expression> users) {
        this.noneOrAll = noneOrAll;
        this.roles = roles;
        this.users = users;
    }

    public Boolean getNoneOrAll() {
        return noneOrAll;
    }

    public List<Expression> getRoles() {
        return roles;
    }

    public List<Expression> getUsers() {
        return users;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SET_DEFAULT_ROLE;
    }

}
