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
 * DROP TRIGGER [IF EXISTS] [schema_name.]trigger_name
 *       </pre>
 */
public class DDLDropTriggerStatement implements SQLStatement {

    private static final long serialVersionUID = -4022856677780449461L;
    private final boolean ifExists;
    private final Identifier name;

    public DDLDropTriggerStatement(boolean ifExists, Identifier name) {
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
        return SQLType.DROP_TRIGGER;
    }

}
