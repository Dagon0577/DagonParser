package parser.ast.fragment.ddl;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class IndexOption implements AST {
    public static final int BTREE = 1;
    public static final int HASH = 2;

    public static final int VISIBLE = 1;
    public static final int INVISIBLE = 2;

    private Expression keyBlockSize;
    private Integer indexType;
    private Identifier parserName;
    private LiteralString comment;
    private Integer visible;

    public IndexOption(Expression keyBlockSize) {
        this.keyBlockSize = keyBlockSize;
    }

    public IndexOption(Integer value, boolean isType) {
        if (isType) {
            this.indexType = value;
        } else {
            this.visible = value;
        }
    }

    public IndexOption(Identifier parserName) {
        this.parserName = parserName;
    }

    public IndexOption(LiteralString comment) {
        this.comment = comment;
    }

    public Expression getKeyBlockSize() {
        return keyBlockSize;
    }

    public Integer getIndexType() {
        return indexType;
    }

    public Identifier getParserName() {
        return parserName;
    }

    public LiteralString getComment() {
        return comment;
    }

    public Integer getVisible() {
        return visible;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
