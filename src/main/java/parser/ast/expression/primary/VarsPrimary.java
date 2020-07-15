package parser.ast.expression.primary;

import parser.ast.expression.PrimaryExpression;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class VarsPrimary extends PrimaryExpression {
    public static final int SYS_VAR = 1;// @@xxx
    public static final int USR_VAR = 2;// @xxx
    public static final int OLD_ROW = 3;// OLD.xxx
    public static final int NEW_ROW = 4;// NEW.xxx
    public static final int USER = 5;// 'xx'@'xxx' or xxx

    public static final int SCOPE_GLOBAL = 1;
    public static final int SCOPE_SESSION = 2;
    public static final int SCOPE_PERSIST = 3;
    public static final int SCOPE_PERSIST_ONLY = 4;

    private final int type;
    private final Integer scope;
    private final long scopeStr;
    private final byte[] varText;

    public VarsPrimary(int type, Integer scope, long scopeStr, byte[] varText) {
        this.type = type;
        this.scope = scope;
        this.scopeStr = scopeStr;
        this.varText = varText;
    }

    public int getType() {
        return type;
    }

    public Integer getScope() {
        return scope;
    }

    public long getScopeStr() {
        return scopeStr;
    }

    public byte[] getVarText() {
        return varText;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return false;
    }

}

