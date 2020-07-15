package parser.ast.expression.primary;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.PrimaryExpression;
import parser.ast.expression.primary.literal.Literal;
import parser.visitor.Visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class FunctionExpression extends PrimaryExpression {
    protected static final List<Expression> wrapList(Expression expr) {
        List<Expression> list = new ArrayList<Expression>(1);
        list.add(expr);
        return list;
    }

    private final int type;
    private byte[] functionName;
    private final List<Expression> arguments;

    public FunctionExpression(int type, byte[] functionName, List<Expression> arguments) {
        this.type = type;
        this.functionName = functionName;
        // arguments为null的时候说明function后面没括号
        this.arguments = arguments;
    }

    public int getFunctionType() {
        return type;
    }

    public byte[] getFunctionName() {
        return functionName;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        Iterator<Expression> iter = arguments.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                if (exp.equals(from)) {
                    arguments.set(i, (Expression)to);
                    result = true;
                } else {
                    result |= exp.replace(from, to);
                }
            }
            i++;
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        Iterator<Expression> iter = arguments.iterator();
        while (iter.hasNext()) {
            Expression exp = iter.next();
            if (exp != null) {
                removed |= exp.removeSchema(schema);
            }
        }
        return removed;
    }

    @Override
    public Expression setCacheEvalRst(boolean cacheEvalRst) {
        return super.setCacheEvalRst(cacheEvalRst);
    }

    @Override
    public boolean exisitConstantCompare() {
        boolean flag = false;
        for (Expression operand : arguments) {
            if (operand != null) {
                flag = flag || operand.exisitConstantCompare();
            }
        }
        return flag;
    }

    @Override
    public boolean exisitConstantOperation() {
        boolean flag = true;
        for (Expression operand : arguments) {
            if (operand != null) {
                flag = flag && operand instanceof Literal;
            }
        }
        if (flag) {
            return flag;
        }
        flag = false;
        for (Expression operand : arguments) {
            if (operand != null) {
                flag = flag || operand.exisitConstantOperation();
            }
        }
        return false;
    }
}
