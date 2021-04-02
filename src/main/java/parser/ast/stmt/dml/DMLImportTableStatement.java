package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.primary.literal.LiteralString;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月02日
 * 
 *       <pre>
 * IMPORT TABLE FROM sdi_file [, sdi_file] ...
 *       </pre>
 */
public class DMLImportTableStatement implements DMLStatement {

    private static final long serialVersionUID = -5901977704984443859L;
    private long parseInfo;
    private final List<LiteralString> files;

    private byte[] cachedTableName;

    public DMLImportTableStatement(List<LiteralString> files) {
        this.files = files;
    }

    public List<LiteralString> getFiles() {
        return files;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.IMPORT_TABLE;
    }

    @Override
    public void setParseInfo(long parseInfo) {
        this.parseInfo = parseInfo;
    }

    @Override
    public long getParseInfo() {
        return parseInfo;
    }

    @Override
    public boolean replace(AST from, AST to) {
        return false;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return false;
    }

    @Override
    public void setCachedTableName(byte[] cachedTableName) {
        this.cachedTableName = cachedTableName;
    }

    @Override
    public byte[] getCachedTableName() {
        return cachedTableName;
    }

    @Override
    public boolean maybeMoreThanTwoTable() {
        return false;
    }
}
