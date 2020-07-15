package cn.hotdb.parser.ast.expression.primary.literal;

import java.util.Map;

import cn.hotdb.parser.util.BytesUtil;
import cn.hotdb.parser.visitor.Visitor;
import cn.hotpu.hotdb.mysql.charset.MySqlCharset;
import cn.hotpu.hotdb.parser.util.ParseString;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:46:01
 * 
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
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters,
            byte[] sql) {
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
