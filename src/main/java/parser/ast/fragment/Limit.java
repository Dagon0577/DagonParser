package parser.ast.fragment;

import parser.ast.AST;
import parser.ast.expression.primary.ParamMarker;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class Limit implements AST {
    private final long offset;
    private final long size;
    private final boolean nullOffset;

    public Limit(long offset, long size, boolean nullOffset) {
        this.offset = offset;
        this.size = size;
        this.nullOffset = nullOffset;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }

    public boolean isNullOffset() {
        return nullOffset;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public boolean replace(AST from, AST to) {
        return false;
    }

}

