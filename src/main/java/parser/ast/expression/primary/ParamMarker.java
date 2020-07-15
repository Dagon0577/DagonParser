package parser.ast.expression.primary;

import parser.ast.expression.QueryExpression;
import parser.visitor.Visitor;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class ParamMarker extends Identifier implements QueryExpression {
    private final int paramIndex;
    private long alias;
    private boolean cacheEvalRst = true;
    private boolean evaluated;
    private transient Object evaluationCache;

    public ParamMarker(int paramIndex) {
        super(null, null, false);
        this.paramIndex = paramIndex;
    }

    public ParamMarker(int paramIndex, long alias) {
        super(null, null, false);
        this.paramIndex = paramIndex;
        this.alias = alias;
    }

    public long getAlias() {
        return alias;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    @Override
    public int hashCode() {
        return paramIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof ParamMarker) {
            ParamMarker that = (ParamMarker)obj;
            return this.paramIndex == that.paramIndex;
        }
        return false;
    }

    @Override
    public Object evaluationInternal(Map<? extends Object, ? extends Object> parameters, byte[] sql) {
        return parameters.get(paramIndex);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
