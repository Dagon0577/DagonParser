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
 * DROP USER [IF EXISTS] user [, user] ...
 *       </pre>
 */
public class DALDropUserStatement implements SQLStatement {

    private static final long serialVersionUID = 4026739275406236325L;
    private final boolean ifExists;
    private final List<Expression> users;

    public DALDropUserStatement(boolean ifExists, List<Expression> users) {
        this.ifExists = ifExists;
        this.users = users;
    }

    public boolean isIfExists() {
        return ifExists;
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
        return SQLType.DROP_USER;
    }

}
