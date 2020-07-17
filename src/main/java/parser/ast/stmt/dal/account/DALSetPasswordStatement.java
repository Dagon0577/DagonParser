package parser.ast.stmt.dal.account;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DALSetPasswordStatement implements SQLStatement {
    private final Expression user;
    private final LiteralString authString;
    private final LiteralString currentAuthString;
    private final boolean retainCurrentPassword;

    public DALSetPasswordStatement(Expression user, LiteralString authString, LiteralString currentAuthString,
        boolean retainCurrentPassword) {
        this.user = user;
        this.authString = authString;
        this.currentAuthString = currentAuthString;
        this.retainCurrentPassword = retainCurrentPassword;
    }

    public Expression getUser() {
        return user;
    }

    public LiteralString getAuthString() {
        return authString;
    }

    public LiteralString getCurrentAuthString() {
        return currentAuthString;
    }

    public boolean isRetainCurrentPassword() {
        return retainCurrentPassword;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SET_PASSWORD;
    }

}
