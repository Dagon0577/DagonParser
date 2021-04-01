package parser.ast.stmt.ddl.create;

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
public class DDLCreateLogfileGroupStatement implements SQLStatement {
    private final Identifier name;
    private final LiteralString undoFile;
    private final Expression initialSize;
    private final Expression undoBufferSize;
    private final Expression redoBufferSize;
    private final Expression nodeGroupId;
    private final boolean isWait;
    private final LiteralString comment;
    private final Identifier engine;

    public DDLCreateLogfileGroupStatement(Identifier name, LiteralString undoFile, Expression initialSize,
        Expression undoBufferSize, Expression redoBufferSize, Expression nodeGroupId, boolean isWait,
        LiteralString comment, Identifier engine) {
        this.name = name;
        this.undoFile = undoFile;
        this.initialSize = initialSize;
        this.undoBufferSize = undoBufferSize;
        this.redoBufferSize = redoBufferSize;
        this.nodeGroupId = nodeGroupId;
        this.isWait = isWait;
        this.comment = comment;
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

    public Expression getUndoBufferSize() {
        return undoBufferSize;
    }

    public Expression getRedoBufferSize() {
        return redoBufferSize;
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
        return SQLType.CREATE_LOG_FILE_GROUP;
    }

}
