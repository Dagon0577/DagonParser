package cn.hotdb.parser.ast.expression.primary.literal;

import java.util.Map;

import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:46:11
 * 
 */
public class LiteralNull extends Literal {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters,
            byte[] sql) {
        return null;
    }

}
