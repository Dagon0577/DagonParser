package parser.ast.stmt.dal.account;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class AuthOption implements AST {
    private final LiteralString authString;
    private final LiteralString currentAuthString;
    private final Boolean retainCurrent;
    private final Identifier authPlugin;
    private final LiteralString hashString;
    private final boolean discard;

    public AuthOption(LiteralString authString, LiteralString currentAuthString, Boolean retainCurrent,
        Identifier authPlugin, LiteralString hashString, boolean discard) {
        this.authString = authString;
        this.currentAuthString = currentAuthString;
        this.retainCurrent = retainCurrent;
        this.authPlugin = authPlugin;
        this.hashString = hashString;
        this.discard = discard;
    }

    public LiteralString getAuthString() {
        return authString;
    }

    public LiteralString getCurrentAuthString() {
        return currentAuthString;
    }

    public Boolean getRetainCurrent() {
        return retainCurrent;
    }

    public Identifier getAuthPlugin() {
        return authPlugin;
    }

    public LiteralString getHashString() {
        return hashString;
    }

    public boolean isDiscard() {
        return discard;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
