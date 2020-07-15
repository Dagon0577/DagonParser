package cn.hotdb.parser.ast.expression.primary;

import java.util.Map;

import cn.hotdb.parser.ast.expression.PrimaryExpression;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:51:01
 * 
 */
public class PlaceHolder extends PrimaryExpression {
    private final long name;

    public PlaceHolder(long name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public long getName() {
        return name;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return false;
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters,
            byte[] sql) {
        return parameters.get(name);
    }
}
