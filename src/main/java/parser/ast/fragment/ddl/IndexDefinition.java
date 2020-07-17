package parser.ast.fragment.ddl;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class IndexDefinition implements AST {
    public static final int PRIMARY = 1;
    public static final int UNIQUE = 2;
    public static final int KEY = 3;
    public static final int FULLTEXT = 4;
    public static final int SPATIAL = 5;
    public static final int INDEX = 6;

    private final int keyType;
    private Identifier indexName;
    private final Integer indexType;
    private final List<IndexColumnName> columns;
    private final List<parser.ast.fragment.ddl.IndexOption> options;
    private Identifier symbol;

    public IndexDefinition(int keyType, Identifier indexName, Integer indexType, List<IndexColumnName> columns,
        List<parser.ast.fragment.ddl.IndexOption> options, Identifier symbol) {
        this.keyType = keyType;
        this.indexName = indexName;
        this.indexType = indexType;
        this.columns = columns;
        this.options = options;
        this.symbol = symbol;
    }

    public int getKeyType() {
        return keyType;
    }

    public Identifier getIndexName() {
        return indexName;
    }

    public Integer getIndexType() {
        return indexType;
    }

    public List<IndexColumnName> getColumns() {
        return columns;
    }

    public List<parser.ast.fragment.ddl.IndexOption> getOptions() {
        return options;
    }

    public Identifier getSymbol() {
        return symbol;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
