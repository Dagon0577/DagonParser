package parser.ast.stmt.ddl;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateDatabaseStatement implements SQLStatement {
    private final boolean ifNotExist;
    private final Identifier db;
    private final Identifier charset;
    private final Identifier collate;
    private final Boolean isEncryption;

    public DDLCreateDatabaseStatement(boolean ifNotExist, Identifier db, Identifier charset, Identifier collate,
        Boolean isEncryption) {
        this.ifNotExist = ifNotExist;
        this.db = db;
        this.charset = charset;
        this.collate = collate;
        this.isEncryption = isEncryption;
    }

    public Boolean getEncryption() {
        return isEncryption;
    }

    public boolean isIfNotExist() {
        return ifNotExist;
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
        return SQLType.CREATE_DATABASE;
    }

}
