package parser.ast.stmt.dal.account;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.SQLStatement;
import parser.util.Tuple3;
import parser.visitor.Visitor;
import parser.util.Pair;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DALCreateUserStatement implements SQLStatement {
    public static final int TLS_SSL = 1;
    public static final int TLS_X509 = 2;
    public static final int TLS_CIPHER = 3;
    public static final int TLS_ISSUER = 4;
    public static final int TLS_SUBJECT = 5;

    public static final int MAX_QUERIES_PER_HOUR = 1;
    public static final int MAX_UPDATES_PER_HOUR = 2;
    public static final int MAX_CONNECTIONS_PER_HOUR = 3;
    public static final int MAX_USER_CONNECTIONS = 4;

    public static final int PASSWORD_EXPIRE = 1;
    public static final int PASSWORD_HISTORY = 2;
    public static final int PASSWORD_REUSE_INTERVAL = 3;
    public static final int PASSWORD_REQUIRE_CURRENT = 4;

    private final boolean ifNotExists;
    private final List<Pair<Expression, AuthOption>> users;
    private final List<Expression> roles;
    private final Boolean requireNone;
    private final List<Pair<Integer, LiteralString>> tlsOptions;
    private final List<Pair<Integer, Long>> resourceOptions;
    private final Tuple3<Integer, Boolean, Long> passwordOption;
    private final Boolean lock;

    public DALCreateUserStatement(boolean ifNotExists, List<Pair<Expression, AuthOption>> users, List<Expression> roles,
        Boolean requireNone, List<Pair<Integer, LiteralString>> tlsOptions, List<Pair<Integer, Long>> resourceOptions,
        Tuple3<Integer, Boolean, Long> passwordOption, Boolean lock) {
        this.ifNotExists = ifNotExists;
        this.users = users;
        this.roles = roles;
        this.requireNone = requireNone;
        this.tlsOptions = tlsOptions;
        this.resourceOptions = resourceOptions;
        this.passwordOption = passwordOption;
        this.lock = lock;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public List<Pair<Expression, AuthOption>> getUsers() {
        return users;
    }

    public List<Expression> getRoles() {
        return roles;
    }

    public Boolean getRequireNone() {
        return requireNone;
    }

    public List<Pair<Integer, LiteralString>> getTlsOptions() {
        return tlsOptions;
    }

    public List<Pair<Integer, Long>> getResourceOptions() {
        return resourceOptions;
    }

    public Tuple3<Integer, Boolean, Long> getPasswordOption() {
        return passwordOption;
    }

    public Boolean getLock() {
        return lock;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CREATE_USER;
    }

}
