package parser.ast.stmt.ddl;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.ReferenceDefinition;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class ForeignKeyDefinition implements AST {
    private Identifier symbol;
    private Identifier indexName;
    private List<Identifier> columns;
    private ReferenceDefinition referenceDefinition;

    public ForeignKeyDefinition(Identifier symbol, Identifier indexName, List<Identifier> columns,
        ReferenceDefinition referenceDefinition) {
        this.symbol = symbol;
        this.indexName = indexName;
        this.columns = columns;
        this.referenceDefinition = referenceDefinition;
    }

    public Identifier getSymbol() {
        return symbol;
    }

    public void setSymbol(Identifier symbol) {
        this.symbol = symbol;
    }

    public Identifier getIndexName() {
        return indexName;
    }

    public void setIndexName(Identifier indexName) {
        this.indexName = indexName;
    }

    public List<Identifier> getColumns() {
        return columns;
    }

    public void setColumns(List<Identifier> columns) {
        this.columns = columns;
    }

    public ReferenceDefinition getReferenceDefinition() {
        return referenceDefinition;
    }

    public void setReferenceDefinition(ReferenceDefinition referenceDefinition) {
        this.referenceDefinition = referenceDefinition;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
