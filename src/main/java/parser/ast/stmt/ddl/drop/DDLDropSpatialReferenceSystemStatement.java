package parser.ast.stmt.ddl.drop;

import parser.SQLType;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月03日
 * 
 *       <pre>
 * DROP SPATIAL REFERENCE SYSTEM
 *     [IF EXISTS]
 *     srid
 * 
 * srid: 32-bit unsigned integer
 *       </pre>
 */
public class DDLDropSpatialReferenceSystemStatement implements SQLStatement {

    private static final long serialVersionUID = 9207842372166966697L;
    private final boolean ifExists;
    private final long srid;

    public DDLDropSpatialReferenceSystemStatement(boolean ifExists, long srid) {
        this.ifExists = ifExists;
        this.srid = srid;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public long getSrid() {
        return srid;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DROP_SPATIAL_REFERENCE_SYSTEM;
    }

}
