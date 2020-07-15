package parser.ast.expression.primary.literal;

import parser.util.BytesUtil;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class LiteralString extends Literal {
    private final byte[] introducer;
    private final long string;
    private final byte[] stringBytes;
    private final boolean nchars;

    public LiteralString(byte[] introducer, long string, boolean nchars) {
        this.introducer = introducer;
        this.string = string;
        this.stringBytes = null;
        this.nchars = nchars;
    }

    public LiteralString(byte[] introducer, byte[] stringBytes, boolean nchars) {
        this.introducer = introducer;
        this.string = 0;
        this.stringBytes = stringBytes;
        this.nchars = nchars;
    }

    public byte[] getIntroducer() {
        return introducer;
    }

    public long getString() {
        return string;
    }

    public byte[] getStringBytes() {
        return stringBytes;
    }

    public boolean isNchars() {
        return nchars;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) {
        if (string == 0)
            return null;
        return new String(BytesUtil.getValue(sql, string));
    }
}
