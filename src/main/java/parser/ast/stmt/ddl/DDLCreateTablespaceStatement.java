package parser.ast.stmt.ddl;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateTablespaceStatement implements SQLStatement {
    private final boolean undo;
    private final Identifier name;
    private final LiteralString fileName;
    private final Expression fileBlockSize;
    private final Boolean encryption;
    private final Identifier logFileGroup;
    private final Expression extentSize;
    private final Expression initialSize;
    private final Expression autoextendSize;
    private final Expression maxSize;
    private final Expression nodeGroupId;
    private final boolean isWait;
    private final LiteralString comment;
    private final Identifier engine;

    public DDLCreateTablespaceStatement(boolean undo, Identifier name, LiteralString fileName, Expression fileBlockSize,
        Boolean encryption, Identifier logFileGroup, Expression extentSize, Expression initialSize,
        Expression autoextendSize, Expression maxSize, Expression nodeGroupId, boolean isWait, LiteralString comment,
        Identifier engine) {
        this.undo = undo;
        this.name = name;
        this.fileName = fileName;
        this.fileBlockSize = fileBlockSize;
        this.encryption = encryption;
        this.logFileGroup = logFileGroup;
        this.extentSize = extentSize;
        this.initialSize = initialSize;
        this.autoextendSize = autoextendSize;
        this.maxSize = maxSize;
        this.nodeGroupId = nodeGroupId;
        this.isWait = isWait;
        this.comment = comment;
        this.engine = engine;
    }

    public boolean isUndo() {
        return undo;
    }

    public Identifier getName() {
        return name;
    }

    public LiteralString getFileName() {
        return fileName;
    }

    public Expression getFileBlockSize() {
        return fileBlockSize;
    }

    public Boolean getEncryption() {
        return encryption;
    }

    public Identifier getLogFileGroup() {
        return logFileGroup;
    }

    public Expression getExtentSize() {
        return extentSize;
    }

    public Expression getInitialSize() {
        return initialSize;
    }

    public Expression getAutoextendSize() {
        return autoextendSize;
    }

    public Expression getMaxSize() {
        return maxSize;
    }

    public Expression getNodeGroupId() {
        return nodeGroupId;
    }

    public boolean isWait() {
        return isWait;
    }

    public LiteralString getComment() {
        return comment;
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
        return SQLType.CREATE_TABLESPACE;
    }

}
