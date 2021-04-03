package parser.ast.stmt.dal.resource;

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
 * DROP RESOURCE GROUP group_name [FORCE]
 *       </pre>
 */
public class DALDropResourceGroupStatement implements SQLStatement {
    private final Identifier name;
    private final boolean force;

    public DALDropResourceGroupStatement(Identifier name, boolean force) {
        this.name = name;
        this.force = force;
    }

    public Identifier getName() {
        return name;
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
        return SQLType.DROP_RESOURCE;
    }

}
