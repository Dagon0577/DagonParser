package parser.ast.stmt.ddl.drop;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月03日
 * 
 *       <pre>
 * DROP [TEMPORARY] TABLE [IF EXISTS]
 *     tbl_name [, tbl_name] ...
 *     [RESTRICT | CASCADE]
 *       </pre>
 */
public class DDLDropTableStatement implements SQLStatement {

    private static final long serialVersionUID = -3212993467733643995L;
    private final boolean temporary;
    private final boolean ifExists;
    private final List<Identifier> tables;
    private final Boolean restrict;

    public DDLDropTableStatement(boolean temporary, boolean ifExists, List<Identifier> tables,
            Boolean restrict) {
        this.temporary = temporary;
        this.ifExists = ifExists;
        this.tables = tables;
        this.restrict = restrict;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public List<Identifier> getTables() {
        return tables;
    }

    public Boolean getRestrict() {
        return restrict;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DROP_TABLE;
    }

}
