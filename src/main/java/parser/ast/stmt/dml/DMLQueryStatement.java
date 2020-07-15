package parser.ast.stmt.dml;

import parser.ParseInfo;
import parser.ast.expression.ComparisionExpression;
import parser.ast.expression.Expression;
import parser.ast.expression.QueryExpression;
import parser.ast.expression.primary.WithClause;

import java.util.List;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public abstract class DMLQueryStatement implements QueryExpression, DMLStatement {
    protected WithClause with;
    protected boolean inParentheses;

    protected long parseInfo;
    protected List<ComparisionExpression> conditions;
    private byte[] cachedTableName;

    @Override
    public int getPrecedence() {
        return PRECEDENCE_QUERY;
    }

    @Override
    public void setParseInfo(long parseInfo) {
        this.parseInfo = parseInfo;
    }

    @Override
    public long getParseInfo() {
        return parseInfo;
    }

    public void setConditions(List<ComparisionExpression> conditions) {
        this.conditions = conditions;
    }

    public List<ComparisionExpression> getConditions() {
        return conditions;
    }

    public WithClause getWithClause() {
        return with;
    }

    public void setWithClause(WithClause with) {
        this.with = with;
    }

    public boolean isInParentheses() {
        return inParentheses;
    }

    public void setInParentheses(boolean inParentheses) {
        this.inParentheses = inParentheses;
    }

    @Override
    public Expression setCacheEvalRst(boolean cacheEvalRst) {
        return this;
    }

    @Override
    public Object evaluation(Map<? extends Object, ? extends Object> parameters, byte[] sql) {
        return UNEVALUATABLE;
    }

    @Override
    public void setCachedTableName(byte[] cachedTableName) {
        this.cachedTableName = cachedTableName;
    }

    @Override
    public byte[] getCachedTableName() {
        return cachedTableName;
    }

    @Override
    public boolean maybeMoreThanTwoTable() {
        return ParseInfo.isJoin(parseInfo) || ParseInfo.isUnion(parseInfo) || ParseInfo.isSubquery(parseInfo);
    }

    @Override
    public boolean exisitConstantCompare() {
        return false;
    }

    @Override
    public boolean exisitConstantOperation() {
        return false;
    }
}
