package parser.ast.stmt.ddl.create;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.Literal;
import parser.ast.stmt.SQLStatement;
import parser.util.Pair;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateServerStatement implements SQLStatement {
    public static final int HOST = 1;
    public static final int DATABASE = 2;
    public static final int USER = 3;
    public static final int PASSWORD = 4;
    public static final int SOCKET = 5;
    public static final int OWNER = 6;
    public static final int PORT = 7;
    private final Identifier serverName;
    private final Identifier wrapperName;
    private final List<Pair<Integer, Literal>> options;

    public DDLCreateServerStatement(Identifier serverName, Identifier wrapperName,
        List<Pair<Integer, Literal>> options) {
        this.serverName = serverName;
        this.wrapperName = wrapperName;
        this.options = options;
    }

    public Identifier getServerName() {
        return serverName;
    }

    public Identifier getWrapperName() {
        return wrapperName;
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
        return SQLType.CREATE_SERVER;
    }

}
