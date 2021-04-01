package parser.ast.stmt.ddl.alter;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER LOGFILE GROUP logfile_group
 *     ADD UNDOFILE 'file_name'
 *     [INITIAL_SIZE [=] size]
 *     [WAIT]
 *     ENGINE [=] engine_name
 *       </pre>
 */
public class DDLAlterLogfileGroupStatement implements SQLStatement {
    private final Identifier name;
    private final LiteralString undoFile;
    private final Expression initialSize;
    private final boolean isWait;
    private final Identifier engine;

    public DDLAlterLogfileGroupStatement(Identifier name, LiteralString undoFile,
            Expression initialSize, boolean isWait, Identifier engine) {
        this.name = name;
        this.undoFile = undoFile;
        this.initialSize = initialSize;
        this.isWait = isWait;
        this.engine = engine;
    }

    public Identifier getName() {
        return name;
    }

    public LiteralString getUndoFile() {
        return undoFile;
    }

    public Expression getInitialSize() {
        return initialSize;
    }

    public boolean isWait() {
        return isWait;
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
        return SQLType.ALTER_LOG_FILE_GROUP;
    }

}
