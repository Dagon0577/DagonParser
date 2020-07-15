package parser.ast.stmt.dml;

import parser.ast.AST;
import parser.ast.stmt.SQLStatement;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public interface DMLStatement extends SQLStatement {

    void setParseInfo(long parseInfo);

    long getParseInfo();

    void setCachedTableName(byte[] tableName);

    byte[] getCachedTableName();

    boolean maybeMoreThanTwoTable();

    boolean replace(AST from, AST to);

    boolean removeSchema(byte[] schema);
}
