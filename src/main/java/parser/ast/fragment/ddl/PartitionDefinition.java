package parser.ast.fragment.ddl;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.fragment.SubpartitionDefinition;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/14
 *
 * <pre>
 * partition_definition:
 * PARTITION partition_name
 * [VALUES
 * {LESS THAN {(expr | value_list) | MAXVALUE}
 * |
 * IN (value_list)}]
 * [[STORAGE] ENGINE [=] engine_name]
 * [COMMENT [=] 'string' ]
 * [DATA DIRECTORY [=] 'data_dir']
 * [INDEX DIRECTORY [=] 'index_dir']
 * [MAX_ROWS [=] max_number_of_rows]
 * [MIN_ROWS [=] min_number_of_rows]
 * [TABLESPACE [=] tablespace_name]
 * [(subpartition_definition [, subpartition_definition] ...)]
 *       </pre>
 */
public class PartitionDefinition implements AST {
    private Identifier name;
    private Expression lessThan;
    private List<Expression> lessThanList;
    private Boolean isLessThanMaxvalue;
    private List<Expression> inList;
    private Identifier engine;
    private LiteralString comment;
    private LiteralString dataDir;
    private LiteralString indexDir;
    private Long maxRows;
    private Long minRows;
    private Identifier tablespace;
    private List<SubpartitionDefinition> subpartitionDefinitions;

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public Expression getLessThan() {
        return lessThan;
    }

    public void setLessThan(Expression lessThan) {
        this.lessThan = lessThan;
    }

    public List<Expression> getLessThanList() {
        return lessThanList;
    }

    public void setLessThanList(List<Expression> lessThanList) {
        this.lessThanList = lessThanList;
    }

    public Boolean isLessThanMaxvalue() {
        return isLessThanMaxvalue;
    }

    public void setLessThanMaxvalue(Boolean isLessThanMaxvalue) {
        this.isLessThanMaxvalue = isLessThanMaxvalue;
    }

    public List<Expression> getInList() {
        return inList;
    }

    public void setInList(List<Expression> inList) {
        this.inList = inList;
    }

    public Identifier getEngine() {
        return engine;
    }

    public void setEngine(Identifier engine) {
        this.engine = engine;
    }

    public LiteralString getComment() {
        return comment;
    }

    public void setComment(LiteralString comment) {
        this.comment = comment;
    }

    public LiteralString getDataDir() {
        return dataDir;
    }

    public void setDataDir(LiteralString dataDir) {
        this.dataDir = dataDir;
    }

    public LiteralString getIndexDir() {
        return indexDir;
    }

    public void setIndexDir(LiteralString indexDir) {
        this.indexDir = indexDir;
    }

    public Long getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(Long maxRows) {
        this.maxRows = maxRows;
    }

    public Long getMinRows() {
        return minRows;
    }

    public void setMinRows(Long minRows) {
        this.minRows = minRows;
    }

    public Identifier getTablespace() {
        return tablespace;
    }

    public void setTablespace(Identifier tablespace) {
        this.tablespace = tablespace;
    }

    public List<SubpartitionDefinition> getSubpartitionDefinitions() {
        return subpartitionDefinitions;
    }

    public void setSubpartitionDefinitions(List<SubpartitionDefinition> subpartitionDefinitions) {
        this.subpartitionDefinitions = subpartitionDefinitions;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}

