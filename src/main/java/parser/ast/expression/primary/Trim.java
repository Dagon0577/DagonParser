package cn.hotdb.parser.ast.expression.primary;

import java.util.List;

import cn.hotdb.parser.ast.expression.Expression;
import cn.hotdb.parser.token.Functions;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2019年2月22日 下午4:28:51
 * 
 */
public class Trim extends FunctionExpression {
    public static final int BOTH = 1;
    public static final int LEADING = 2;
    public static final int TRAILING = 3;

    private final Integer specifier;

    public Trim(byte[] functionName, List<Expression> arguments, Integer specifier) {
        super(Functions.TRIM, functionName, arguments);
        this.specifier = specifier;
    }

    public Integer getSpecifier() {
        return specifier;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
