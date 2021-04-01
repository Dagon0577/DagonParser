package parser.ast.stmt.ddl.alter;

import parser.SQLType;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER INSTANCE instance_action
 *
 * instance_action: {
 *     ROTATE INNODB MASTER KEY
 *   | ROTATE BINLOG MASTER KEY
 *   | RELOAD TLS [NO ROLLBACK ON ERROR]
 * }
 *       </pre>
 */
public class DDLAlterInstanceStatement implements SQLStatement {
    public static final int ROTATE_INNODB = 1;
    public static final int ROTATE_BINLOG = 2;
    public static final int RELOAD_TLS = 3;
    public static final int RELOAD_TLS_NO = 4;

    private final int type;

    public DDLAlterInstanceStatement(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.ALTER_INSTANCE;
    }

}
