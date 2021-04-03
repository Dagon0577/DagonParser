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
 * DROP [UNDO] TABLESPACE tablespace_name
 *     [ENGINE [=] engine_name]
 *       </pre>
 */
public class DDLDropTablespaceStatement implements SQLStatement {

    private static final long serialVersionUID = -2052796260117051301L;
    private final boolean undo;
    private final Identifier name;
    private final Identifier engine;

    public DDLDropTablespaceStatement(boolean undo, Identifier name, Identifier engine) {
        this.undo = undo;
        this.name = name;
        this.engine = engine;
    }

    public boolean isUndo() {
        return undo;
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
        return SQLType.DROP_TABLESPACE;
    }

}
