package cn.hotdb.parser.ast.expression.primary.literal;

import java.math.BigInteger;
import java.util.Map;

import cn.hotdb.parser.util.BytesUtil;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:45:40
 * 
 */
public class LiteralBitField extends Literal {
    private final long text;
    private final long introducer;

    public LiteralBitField(long introducer, long bitFieldText) {
        if (bitFieldText == 0)
            throw new IllegalArgumentException("bit text is null");
        this.introducer = introducer;
        this.text = bitFieldText;
    }

    public long getText() {
        return text;
    }

    public long getIntroducer() {
        return introducer;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters,
            byte[] sql) {
        return new BigInteger(new String(BytesUtil.getValue(sql, text)), 2);
    }
}
