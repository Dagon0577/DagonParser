package cn.hotdb.parser.ast.expression.primary;

import cn.hotdb.parser.ast.expression.Expression;
import cn.hotdb.parser.ast.fragment.ddl.DataType;
import cn.hotdb.parser.token.Functions;
import cn.hotdb.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2018年11月16日 下午6:57:22
 * 
 */
public class Cast extends FunctionExpression {
    private final long typeName;
    private Expression typeInfo1;
    private Expression typeInfo2;
    private final DataType type;

    public Cast(byte[] functionName, Expression expr, long typeName, Expression typeInfo1,
            Expression typeInfo2) {
        super(Functions.CAST, functionName, wrapList(expr));
        if (0 == typeName) {
            throw new IllegalArgumentException("typeName is null");
        }
        this.typeName = typeName;
        this.typeInfo1 = typeInfo1;
        this.typeInfo2 = typeInfo2;
        this.type = null;
    }

    public Cast(byte[] functionName, Expression expr, DataType type) {
        super(Functions.CAST, functionName, wrapList(expr));
        this.type = type;
        this.typeName = 0;
        this.typeInfo1 = null;
        this.typeInfo2 = null;
    }

    public Expression getExpr() {
        return getArguments().get(0);
    }

    public long getTypeName() {
        return typeName;
    }

    public DataType getType() {
        return type;
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
