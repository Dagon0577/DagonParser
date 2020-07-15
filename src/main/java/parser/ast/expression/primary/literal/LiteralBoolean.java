package cn.hotdb.parser.ast.expression.primary.literal;

import java.util.Map;

import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:45:48
 * 
 */
public class LiteralBoolean extends Literal {
    public static final Integer TRUE = new Integer(1);
    public static final Integer FALSE = new Integer(0);
    private final boolean value;

    public LiteralBoolean(boolean value) {
        this.value = value;
    }

    public boolean isTrue() {
        return value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters,
            byte[] sql) {
        return value ? TRUE : FALSE;
    }
}
