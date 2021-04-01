package parser.ast.stmt.ddl.alter;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER {DATABASE | SCHEMA} [db_name]
 *     alter_specification ...
 * 
 * alter_specification:
 *     [DEFAULT] CHARACTER SET [=] charset_name
 *   | [DEFAULT] COLLATE [=] collation_name
 *       </pre>
 */
public class DDLAlterDatabaseStatement implements SQLStatement {
    private final Identifier db;
    private final Identifier charset;
    private final Identifier collate;

    private Boolean isEncryption;

    public DDLAlterDatabaseStatement(Identifier db, Identifier charset, Identifier collate,
            Boolean isEncryption) {
        this.db = db;
        this.charset = charset;
        this.collate = collate;
        this.isEncryption = isEncryption;
    }

    public Boolean isEncryption() {
        return isEncryption;
    }

    public Identifier getDb() {
        return db;
    }

    public Identifier getCharset() {
        return charset;
    }

    public Identifier getCollate() {
        return collate;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.ALTER_DATABASE;
    }

}
