package parser.ast.stmt.ddl.alter;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.fragment.ddl.ScheduleDefinition;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER
 *     [DEFINER = { user | CURRENT_USER }]
 *     EVENT event_name
 *     [ON SCHEDULE schedule]
 *     [ON COMPLETION [NOT] PRESERVE]
 *     [RENAME TO new_event_name]
 *     [ENABLE | DISABLE | DISABLE ON SLAVE]
 *     [COMMENT 'string']
 *     [DO event_body]
 *     
 * schedule:
 *     AT timestamp [+ INTERVAL interval] ...
 *   | EVERY interval
 *     [STARTS timestamp [+ INTERVAL interval] ...]
 *     [ENDS timestamp [+ INTERVAL interval] ...]
 *       </pre>
 */
public class DDLAlterEventStatement implements SQLStatement {
    public static final int ENABLE = 1;
    public static final int DISABLE = 2;
    public static final int DISABLE_ON_SLAVE = 3;

    private final Expression definer;
    private final Identifier event;
    private final ScheduleDefinition schedule;
    private final Boolean preserve;
    private final Identifier renameTo;
    private final Integer enableType;
    private final LiteralString comment;
    private final SQLStatement eventBody;

    public DDLAlterEventStatement(Expression definer, Identifier event, ScheduleDefinition schedule,
            Boolean preserve, Identifier renameTo, Integer enableType, LiteralString comment,
            SQLStatement eventBody) {
        this.definer = definer;
        this.event = event;
        this.schedule = schedule;
        this.preserve = preserve;
        this.renameTo = renameTo;
        this.enableType = enableType;
        this.comment = comment;
        this.eventBody = eventBody;
    }

    public Expression getDefiner() {
        return definer;
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

    public Identifier getRenameTo() {
        return renameTo;
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
        return SQLType.ALTER_EVENT;
    }

}
