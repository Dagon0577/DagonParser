package parser.ast.stmt.ddl;

import parser.SQLType;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import parser.util.Tuple3;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateSpatialReferenceSystemStatement implements SQLStatement {
    public static final int NAME = 1;
    public static final int DEFINITION = 2;
    public static final int ORGANIZATION = 3;
    public static final int DESCRIPTION = 4;

    private final boolean isOrReplace;
    private final boolean ifNotExists;
    private final long srid;
    private final List<Tuple3<Integer, LiteralString, Long>> attributies;

    public DDLCreateSpatialReferenceSystemStatement(boolean isOrReplace, boolean ifNotExists, long srid,
        List<Tuple3<Integer, LiteralString, Long>> attributies) {
        this.isOrReplace = isOrReplace;
        this.ifNotExists = ifNotExists;
        this.srid = srid;
        this.attributies = attributies;
    }

    public boolean isOrReplace() {
        return isOrReplace;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public long getSrid() {
        return srid;
    }

    public List<Tuple3<Integer, LiteralString, Long>> getAttributies() {
        return attributies;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CREATE_SPATIAL_REFERENCE_SYSTEM;
    }

}
