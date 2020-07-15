package cn.hotdb.parser.ast.expression.primary;

import java.util.List;

import cn.hotdb.parser.ast.expression.Expression;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午7:09:33
 * 
 */
public class GroupFunction extends FunctionExpression {

    private final boolean distinct;

    public GroupFunction(int type, byte[] functionName, List<Expression> arguments,
            boolean distinct) {
        super(type, functionName, arguments);
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
