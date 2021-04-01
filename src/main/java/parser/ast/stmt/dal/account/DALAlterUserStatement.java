package parser.ast.stmt.dal.account;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import parser.util.Pair;
import parser.util.Tuple3;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER USER [IF EXISTS]
 *     user [auth_option] [, user [auth_option]] ...
 *     [REQUIRE {NONE | tls_option [[AND] tls_option] ...}]
 *     [WITH resource_option [resource_option] ...]
 *     [password_option | lock_option] ...
 * 
 * ALTER USER [IF EXISTS] USER() user_func_auth_option
 * 
 * ALTER USER [IF EXISTS]
 *     user DEFAULT ROLE
 *     {NONE | ALL | role [, role ] ...}
 * 
 * user:
 *     (see Section 6.2.4, “Specifying Account Names”)
 * 
 * auth_option: {
 *     IDENTIFIED BY 'auth_string'
 *         [REPLACE 'current_auth_string']
 *         [RETAIN CURRENT PASSWORD]
 *   | IDENTIFIED WITH auth_plugin
 *   | IDENTIFIED WITH auth_plugin BY 'auth_string'
 *         [REPLACE 'current_auth_string']
 *         [RETAIN CURRENT PASSWORD]
 *   | IDENTIFIED WITH auth_plugin AS 'hash_string'
 *   | DISCARD OLD PASSWORD
 * }
 * 
 * user_func_auth_option: {
 *     IDENTIFIED BY 'auth_string'
 *         [REPLACE 'current_auth_string']
 *         [RETAIN CURRENT PASSWORD]
 *   | DISCARD OLD PASSWORD
 * }
 * 
 * tls_option: {
 *    SSL
 *  | X509
 *  | CIPHER 'cipher'
 *  | ISSUER 'issuer'
 *  | SUBJECT 'subject'
 * }
 * 
 * resource_option: {
 *     MAX_QUERIES_PER_HOUR count
 *   | MAX_UPDATES_PER_HOUR count
 *   | MAX_CONNECTIONS_PER_HOUR count
 *   | MAX_USER_CONNECTIONS count
 * }
 * 
 * password_option: {
 *     PASSWORD EXPIRE [DEFAULT | NEVER | INTERVAL N DAY]
 *   | PASSWORD HISTORY {DEFAULT | N}
 *   | PASSWORD REUSE INTERVAL {DEFAULT | N DAY}
 *   | PASSWORD REQUIRE CURRENT [DEFAULT | OPTIONAL]
 * }
 * 
 * lock_option: {
 *     ACCOUNT LOCK
 *   | ACCOUNT UNLOCK
 * }
 *       </pre>
 */
public class DALAlterUserStatement implements SQLStatement {
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

    private final boolean ifExists;
    private List<Pair<Expression, AuthOption>> users;
    private Boolean requireNone;
    private List<Pair<Integer, LiteralString>> tlsOptions;
    private List<Pair<Integer, Long>> resourceOptions;
    private Tuple3<Integer, Boolean, Long> passwordOption;
    private Boolean lock;

    public DALAlterUserStatement(boolean ifExists, List<Pair<Expression, AuthOption>> users,
            Boolean requireNone, List<Pair<Integer, LiteralString>> tlsOptions,
            List<Pair<Integer, Long>> resourceOptions,
            Tuple3<Integer, Boolean, Long> passwordOption, Boolean lock) {
        this.ifExists = ifExists;
        this.users = users;
        this.requireNone = requireNone;
        this.tlsOptions = tlsOptions;
        this.resourceOptions = resourceOptions;
        this.passwordOption = passwordOption;
        this.lock = lock;
    }

    private AuthOption userFuncAuthOption;

    public DALAlterUserStatement(boolean ifExists, AuthOption userFuncAuthOption) {
        this.ifExists = ifExists;
        this.userFuncAuthOption = userFuncAuthOption;
    }

    private Identifier user;
    private Boolean noneOrAll;
    private List<Expression> roles;

    public DALAlterUserStatement(boolean ifExists, Identifier user, Boolean noneOrAll,
            List<Expression> roles) {
        super();
        this.ifExists = ifExists;
        this.user = user;
        this.noneOrAll = noneOrAll;
        this.roles = roles;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public List<Pair<Expression, AuthOption>> getUsers() {
        return users;
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

    public AuthOption getUserFuncAuthOption() {
        return userFuncAuthOption;
    }

    public Identifier getUser() {
        return user;
    }

    public Boolean getNoneOrAll() {
        return noneOrAll;
    }

    public List<Expression> getRoles() {
        return roles;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.ALTER_USER;
    }

}
