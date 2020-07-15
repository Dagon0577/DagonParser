package parser.ast.fragment;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/14
 * <pre>
 * subpartition_definition:
 * SUBPARTITION logical_name
 * [[STORAGE] ENGINE [=] engine_name]
 * [COMMENT [=] 'string' ]
 * [DATA DIRECTORY [=] 'data_dir']
 * [INDEX DIRECTORY [=] 'index_dir']
 * [MAX_ROWS [=] max_number_of_rows]
 * [MIN_ROWS [=] min_number_of_rows]
 * [TABLESPACE [=] tablespace_name]
 *       </pre>
 */
public class SubpartitionDefinition implements AST {
    private Identifier logicalName;
    private Identifier engine;
    private LiteralString comment;
    private LiteralString dataDir;
    private LiteralString indexDir;
    private Long maxRows;
    private Long minRows;
    private Identifier tablespace;

    public Identifier getLogicalName() {
        return logicalName;
    }

    public void setLogicalName(Identifier logicalName) {
        this.logicalName = logicalName;
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

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}

