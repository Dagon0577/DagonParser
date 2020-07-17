package parser.ast.stmt.ddl;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateTriggerStatement implements SQLStatement {
    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    private final Expression definer;
    private final Identifier name;
    private final boolean before;
    private final int event;
    private final Identifier table;
    private final Boolean follows;
    private final Identifier otherName;
    private final SQLStatement stmt;

    public DDLCreateTriggerStatement(Expression definer, Identifier name, boolean before, int event, Identifier table,
        Boolean follows, Identifier otherName, SQLStatement stmt) {
        this.definer = definer;
        this.name = name;
        this.before = before;
        this.event = event;
        this.table = table;
        this.follows = follows;
        this.otherName = otherName;
        this.stmt = stmt;
    }

    public Expression getDefiner() {
        return definer;
    }

    public Identifier getName() {
        return name;
    }

    public boolean isBefore() {
        return before;
    }

    public int getEvent() {
        return event;
    }

    public Identifier getTable() {
        return table;
    }

    public Boolean getFollows() {
        return follows;
    }

    public Identifier getOtherName() {
        return otherName;
    }

    public SQLStatement getStmt() {
        return stmt;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CREATE_TRIGGER;
    }

}
