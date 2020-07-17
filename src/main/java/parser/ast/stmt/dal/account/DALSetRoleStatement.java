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
public class DALSetRoleStatement implements SQLStatement {
    private Boolean defaultOrNone;
    private boolean all;
    private List<Expression> roles;

    public DALSetRoleStatement(Boolean defaultOrNone, boolean all, List<Expression> roles) {
        this.defaultOrNone = defaultOrNone;
        this.all = all;
        this.roles = roles;
    }

    public Boolean getDefaultOrNone() {
        return defaultOrNone;
    }

    public void setDefaultOrNone(Boolean defaultOrNone) {
        this.defaultOrNone = defaultOrNone;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public List<Expression> getRoles() {
        return roles;
    }

    public void setRoles(List<Expression> roles) {
        this.roles = roles;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SET_ROLE;
    }

}
