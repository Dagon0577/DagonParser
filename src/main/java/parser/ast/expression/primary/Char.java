package cn.hotdb.parser.ast.expression.primary;

import java.util.List;

import cn.hotdb.parser.ast.expression.Expression;
import cn.hotdb.parser.token.Functions;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月19日 下午1:50:01
 * 
 */
public class Char extends FunctionExpression {
    private final String charset;

    public Char(byte[] functionName, String charset, List<Expression> arguments) {
        super(Functions.CHAR, functionName, arguments);
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
