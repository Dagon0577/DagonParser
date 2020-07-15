package parser.ast.expression.primary;

import parser.ast.expression.Expression;
import parser.ast.fragment.ddl.DataType;
import parser.token.Functions;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class Convert extends FunctionExpression {
    private final long transcodeName;
    private final DataType type;
    private final long typeName;
    private Expression typeInfo1;
    private Expression typeInfo2;

    public Convert(byte[] functionName, Expression arg, long transcodeName) {
        super(Functions.CONVERT, functionName, wrapList(arg));
        if (0 == transcodeName) {
            throw new IllegalArgumentException("transcodeName is null");
        }
        this.transcodeName = transcodeName;
        this.type = null;
        this.typeName = 0;
        this.typeInfo1 = null;
        this.typeInfo2 = null;
    }

    public Convert(byte[] functionName, Expression expr, DataType type, long typeName, Expression typeInfo1,
        Expression typeInfo2) {
        super(Functions.CONVERT, functionName, wrapList(expr));
        this.type = type;
        this.typeName = typeName;
        this.typeInfo1 = typeInfo1;
        this.typeInfo2 = typeInfo2;
        this.transcodeName = 0;
    }

    public long getTranscodeName() {
        return transcodeName;
    }

    public DataType getType() {
        return type;
    }

    public long getTypeName() {
        return typeName;
    }

    public Expression getTypeInfo1() {
        return typeInfo1;
    }

    public Expression getTypeInfo2() {
        return typeInfo2;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
