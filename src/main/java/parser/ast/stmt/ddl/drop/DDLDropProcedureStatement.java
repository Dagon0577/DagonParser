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
 * DROP {PROCEDURE | FUNCTION} [IF EXISTS] sp_name
 *       </pre>
 */
public class DDLDropProcedureStatement implements SQLStatement {

    private static final long serialVersionUID = -1328425642202502941L;
    private final boolean ifExists;
    private final Identifier name;

    public DDLDropProcedureStatement(boolean ifExists, Identifier name) {
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
        return SQLType.DROP_PROCEDURE;
    }

}
