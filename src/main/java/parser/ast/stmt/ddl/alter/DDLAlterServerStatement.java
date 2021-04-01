package parser.ast.stmt.ddl.alter;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.Literal;
import parser.ast.stmt.SQLStatement;
import parser.util.Pair;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER SERVER  server_name
 *     OPTIONS (option [, option] ...)
 * option:
 *  { HOST character-literal
 *  | DATABASE character-literal
 *  | USER character-literal
 *  | PASSWORD character-literal
 *  | SOCKET character-literal
 *  | OWNER character-literal
 *  | PORT numeric-literal }
 *       </pre>
 */
public class DDLAlterServerStatement implements SQLStatement {
    private final Identifier serverName;
    private final List<Pair<Integer, Literal>> options;

    public DDLAlterServerStatement(Identifier serverName, List<Pair<Integer, Literal>> options) {
        this.serverName = serverName;
        this.options = options;
    }

    public Identifier getServerName() {
        return serverName;
    }

    public List<Pair<Integer, Literal>> getOptions() {
        return options;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.ALTER_SERVER;
    }
}
