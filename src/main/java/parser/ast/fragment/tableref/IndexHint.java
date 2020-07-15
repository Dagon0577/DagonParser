package parser.ast.fragment.tableref;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class IndexHint implements AST {
    public static final int ACTION_USE = 1;
    public static final int ACTION_IGNORE = 2;
    public static final int ACTION_FORCE = 3;
    public static final int SCOPE_JOIN = 1;
    public static final int SCOPE_GROUP_BY = 2;
    public static final int SCOPE_ORDER_BY = 3;

    private final Integer action;
    private final boolean isIndex;
    private final Integer scope;
    private final List<Identifier> indexList;

    public IndexHint(Integer action, boolean isIndex, Integer scope, List<Identifier> indexList) {
        this.action = action;
        this.isIndex = isIndex;
        this.scope = scope;
        if (indexList == null || indexList.isEmpty()) {
            this.indexList = Collections.emptyList();
        } else if (indexList instanceof ArrayList) {
            this.indexList = indexList;
        } else {
            this.indexList = new ArrayList<Identifier>(indexList);
        }
    }

    public Integer getAction() {
        return action;
    }

    public boolean isIndex() {
        return isIndex;
    }

    public Integer getScope() {
        return scope;
    }

    public List<Identifier> getIndexList() {
        return indexList;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}

