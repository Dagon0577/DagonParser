package parser.ast.stmt.dal;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.VarsPrimary;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import parser.util.Pair;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DALSetStatement implements SQLStatement {
    public static final int SET_VARS = 1;
    public static final int SET_CHARSET = 2;
    public static final int SET_NAMES = 3;

    private final int type;
    private final List<Pair<VarsPrimary, Expression>> assignmentList;
    private final LiteralString charset;
    private final LiteralString collate;
    private final boolean isDefault;

    public DALSetStatement(List<Pair<VarsPrimary, Expression>> assignmentList) {
        this.type = SET_VARS;
        this.assignmentList = assignmentList;
        this.charset = null;
        this.collate = null;
        isDefault = false;
    }

    public DALSetStatement(LiteralString charset, boolean isDefault) {
        this.type = SET_CHARSET;
        this.charset = charset;
        this.isDefault = isDefault;
        this.collate = null;
        this.assignmentList = null;
    }

    public DALSetStatement(LiteralString charset, LiteralString collate, boolean isDefault) {
        this.type = SET_NAMES;
        this.charset = charset;
        this.collate = collate;
        this.isDefault = isDefault;
        this.assignmentList = null;
    }

    public List<Pair<VarsPrimary, Expression>> getAssignmentList() {
        return assignmentList;
    }

    public int getType() {
        return type;
    }

    public LiteralString getCharset() {
        return charset;
    }

    public LiteralString getCollate() {
        return collate;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SET;
    }

    public boolean isDefault() {
        return isDefault;
    }

}
