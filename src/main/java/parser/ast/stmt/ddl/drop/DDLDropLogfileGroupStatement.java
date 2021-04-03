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
 * DROP LOGFILE GROUP logfile_group
 *     ENGINE [=] engine_name
 *       </pre>
 */
public class DDLDropLogfileGroupStatement implements SQLStatement {

    private static final long serialVersionUID = -6910142274383329597L;
    private final Identifier name;
    private final Identifier engine;

    public DDLDropLogfileGroupStatement(Identifier name, Identifier engine) {
        this.name = name;
        this.engine = engine;
    }

    public Identifier getName() {
        return name;
    }

    public Identifier getEngine() {
        return engine;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DROP_LOG_FILE_GROUP;
    }

}
