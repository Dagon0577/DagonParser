package cn.hotdb.parser.ast.expression.primary;

import cn.hotdb.parser.ast.expression.PrimaryExpression;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午5:50:12
 * 
 */
public class DefaultValue extends PrimaryExpression {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return false;
    }

}
