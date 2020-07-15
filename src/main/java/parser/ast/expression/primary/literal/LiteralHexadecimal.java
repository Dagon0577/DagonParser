package parser.ast.expression.primary.literal;

import mysql.charset.MySqlCharset;
import parser.util.BytesUtil;
import parser.util.ParseString;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class LiteralHexadecimal extends Literal {
    private final long introducer;
    private final long value;

    public LiteralHexadecimal(long introducer, long value) {
        this.introducer = introducer;
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public long getIntroducer() {
        return introducer;
    }

    public long getValue() {
        return value;
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) {
        byte[] v = BytesUtil.getValue(sql, value);
        byte[] bytes = ParseString.hexString2Bytes(v, 1, v.length - 3);
        if (introducer <= 0) {
            return new String(bytes);
        } else {
            String intro = new String(BytesUtil.getValue(sql, introducer));
            return new String(bytes, MySqlCharset.getCharsetForJava(intro.substring(1)));
        }
    }
}
