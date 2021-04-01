package parser.ast.stmt.ddl.create;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.IndexColumnName;
import parser.ast.fragment.ddl.IndexOption;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateIndexStatement implements SQLStatement {
    public static final int UNIQUE = 1;
    public static final int FULLTEXT = 2;
    public static final int SPATIAL = 3;

    public static final int ALGORITHM_DEFAULT = 1;
    public static final int ALGORITHM_INPLACE = 2;
    public static final int ALGORITHM_COPY = 3;
    public static final int ALGORITHM_INSTANT = 4;

    public static final int LOCK_DEFAULT = 1;
    public static final int LOCK_NONE = 2;
    public static final int LOCK_SHARED = 3;
    public static final int LOCK_EXCLUSIVE = 4;

    private final Integer type;
    private final Integer indexType;
    private final Identifier name;
    private final List<IndexOption> options;
    private final Identifier table;
    private final List<IndexColumnName> columns;
    private final Integer algorithm;
    private final Integer lockOption;

    public DDLCreateIndexStatement(Integer type, Integer indexType, Identifier name, List<IndexOption> options,
        Identifier table, List<IndexColumnName> columns, Integer algorithm, Integer lockOption) {
        this.type = type;
        this.name = name;
        this.indexType = indexType;
        this.options = options;
        this.table = table;
        this.columns = columns;
        this.algorithm = algorithm;
        this.lockOption = lockOption;
    }

    public Integer getType() {
        return type;
    }

    public Integer getIndexType() {
        return indexType;
    }

    public Identifier getName() {
        return name;
    }

    public List<IndexOption> getOptions() {
        return options;
    }

    public Identifier getTable() {
        return table;
    }

    public List<IndexColumnName> getColumns() {
        return columns;
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
        return SQLType.CREATE_INDEX;
    }

}
