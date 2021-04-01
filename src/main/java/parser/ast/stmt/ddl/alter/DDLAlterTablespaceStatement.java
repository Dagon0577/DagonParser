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
 * ALTER [UNDO] TABLESPACE tablespace_name
 *   NDB only:  
 *     {ADD|DROP} DATAFILE 'file_name'
 *     [INITIAL_SIZE [=] size]
 *     [WAIT]
 *   InnoDB and NDB:
 *     [RENAME TO tablespace_name] 
 *   InnoDB only:
 *     [SET {ACTIVE|INACTIVE}]
 *     [ENCRYPTION [=] {'Y' | 'N'}]
 *   InnoDB and NDB:
 *     [ENGINE [=] engine_name]
 *       </pre>
 */
public class DDLAlterTablespaceStatement implements SQLStatement {
    private final boolean undo;
    private final Identifier name;
    private final boolean isAddFile;
    private final LiteralString fileName;
    private final Expression initialSize;
    private final boolean isWait;
    private final Identifier renameTo;
    private final Boolean setActive;
    private final Boolean encryption;
    private final Identifier engine;

    public DDLAlterTablespaceStatement(boolean undo, Identifier name, boolean isAddFile,
            LiteralString fileName, Expression initialSize, boolean isWait, Identifier renameTo,
            Boolean setActive, Boolean encryption, Identifier engine) {
        this.undo = undo;
        this.name = name;
        this.isAddFile = isAddFile;
        this.fileName = fileName;
        this.initialSize = initialSize;
        this.isWait = isWait;
        this.renameTo = renameTo;
        this.setActive = setActive;
        this.encryption = encryption;
        this.engine = engine;
    }

    public boolean isUndo() {
        return undo;
    }

    public Identifier getName() {
        return name;
    }

    public boolean isAddFile() {
        return isAddFile;
    }

    public LiteralString getFileName() {
        return fileName;
    }

    public Expression getInitialSize() {
        return initialSize;
    }

    public boolean isWait() {
        return isWait;
    }

    public Identifier getRenameTo() {
        return renameTo;
    }

    public Boolean getSetActive() {
        return setActive;
    }

    public Boolean getEncryption() {
        return encryption;
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
        return SQLType.ALTER_TABLESPACE;
    }

}
