package parser.ast.stmt.ddl.create;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.fragment.ddl.ScheduleDefinition;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateEventStatement implements SQLStatement {
    public static final int ENABLE = 1;
    public static final int DISABLE = 2;
    public static final int DISABLE_ON_SLAVE = 3;

    private final Expression definer;
    private final boolean ifNotExist;
    private final Identifier event;
    private final ScheduleDefinition schedule;
    private final Boolean preserve;
    private final Integer enableType;
    private final LiteralString comment;
    private final SQLStatement eventBody;

    public DDLCreateEventStatement(Expression definer, boolean ifNotExist, Identifier event,
        ScheduleDefinition schedule, Boolean preserve, Integer enableType, LiteralString comment,
        SQLStatement eventBody) {
        this.definer = definer;
        this.ifNotExist = ifNotExist;
        this.event = event;
        this.schedule = schedule;
        this.preserve = preserve;
        this.enableType = enableType;
        this.comment = comment;
        this.eventBody = eventBody;
    }

    public Expression getDefiner() {
        return definer;
    }

    public boolean isIfNotExist() {
        return ifNotExist;
    }

    public Identifier getEvent() {
        return event;
    }

    public ScheduleDefinition getSchedule() {
        return schedule;
    }

    public Boolean getPreserve() {
        return preserve;
    }

    public Integer getEnableType() {
        return enableType;
    }

    public LiteralString getComment() {
        return comment;
    }

    public SQLStatement getEventBody() {
        return eventBody;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CREATE_EVENT;
    }

}
