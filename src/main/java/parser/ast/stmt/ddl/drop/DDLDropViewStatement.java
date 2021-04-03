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
 * DROP VIEW [IF EXISTS]
 *     view_name [, view_name] ...
 *     [RESTRICT | CASCADE]
 *       </pre>
 */
public class DDLDropViewStatement implements SQLStatement {

    private static final long serialVersionUID = -1768667526601662557L;
    private final boolean ifExists;
    private final List<Identifier> views;
    private final Boolean restrict;

    public DDLDropViewStatement(boolean ifExists, List<Identifier> views, Boolean restrict) {
        this.ifExists = ifExists;
        this.views = views;
        this.restrict = restrict;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public List<Identifier> getViews() {
        return views;
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
        return SQLType.DROP_VIEW;
    }

}
