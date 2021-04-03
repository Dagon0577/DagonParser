package parser.ast.stmt.ddl.drop;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月03日
 * 
 *       <pre>
 * DROP {DATABASE | SCHEMA} [IF EXISTS] db_name
 *       </pre>
 */
public class DDLDropDatabaseStatement implements SQLStatement {

    private static final long serialVersionUID = 4621152538805171037L;
    private final boolean ifExists;
    private final Identifier name;

    public DDLDropDatabaseStatement(boolean ifExists, Identifier name) {
        this.ifExists = ifExists;
        this.name = name;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public Identifier getName() {
        return name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DROP_DATABASE;
    }

}
