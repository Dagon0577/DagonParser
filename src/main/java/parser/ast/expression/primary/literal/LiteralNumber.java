package cn.hotdb.parser.ast.expression.primary.literal;

import java.util.Map;

import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:46:18
 * 
 */
public class LiteralNumber extends Literal {
    private final Number number;
    private final boolean pureDigit;

    public LiteralNumber(Number number) {
        this.number = number;
        this.pureDigit = true;
    }

    public LiteralNumber(Number number, boolean pureDigit) {
        this.number = number;
        this.pureDigit = pureDigit;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Number getNumber() {
        return number;
    }

    public boolean isPureDigit() {
        return pureDigit;
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters,
            byte[] sql) {
        return number;
    }

}
