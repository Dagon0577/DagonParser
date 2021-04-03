package parser.ast.stmt.ddl.drop;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月03日
 * 
 *       <pre>
 * DROP INDEX index_name ON tbl_name
 *     [algorithm_option | lock_option] ...
 * 
 * algorithm_option:
 *     ALGORITHM [=] {DEFAULT|INPLACE|COPY}
 * 
 * lock_option:
 *     LOCK [=] {DEFAULT|NONE|SHARED|EXCLUSIVE}
 *       </pre>
 */
public class DDLDropIndexStatement implements SQLStatement {

    private static final long serialVersionUID = 1750238094323318016L;
    private final Identifier name;
    private final Identifier table;
    private final Integer algorithm;
    private final Integer lockOption;

    public DDLDropIndexStatement(Identifier name, Identifier table, Integer algorithm,
            Integer lockOption) {
        this.name = name;
        this.table = table;
        this.algorithm = algorithm;
        this.lockOption = lockOption;
    }

    public Identifier getName() {
        return name;
    }

    public Identifier getTable() {
        return table;
    }

    public Integer getAlgorithm() {
        return algorithm;
    }

    public Integer getLockOption() {
        return lockOption;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DROP_INDEX;
    }

}
