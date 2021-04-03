package parser.ast.stmt.dal;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月03日
 * 
 *       <pre>
 * LOAD INDEX INTO CACHE
 *   tbl_index_list [, tbl_index_list] ...
 * 
 * tbl_index_list:
 *   tbl_name
 *     [PARTITION (partition_list | ALL)]
 *     [[INDEX|KEY] (index_name[, index_name] ...)]
 *     [IGNORE LEAVES]
 * 
 * partition_list:
 *     partition_name[, partition_name][, ...]
 *       </pre>
 */
public class DALLoadIndexIntoCacheStatement implements SQLStatement {

    private static final long serialVersionUID = 8890743573687280367L;
    private final List<TableIndexList> tableIndexList;

    public DALLoadIndexIntoCacheStatement(List<TableIndexList> tableIndexList) {
        this.tableIndexList = tableIndexList;
    }

    public List<TableIndexList> getTableIndexList() {
        return tableIndexList;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.LOAD_INDEX_INTO_CACHE;
    }

    public class TableIndexList implements AST {

        private static final long serialVersionUID = 8937847712576125091L;
        private final Identifier table;
        private final boolean partitionAll;
        private final List<Identifier> partitions;
        private final List<Identifier> indexs;
        private final boolean ignoreLeaves;

        public TableIndexList(Identifier table, boolean partitionAll, List<Identifier> partitions,
                List<Identifier> indexs, boolean ignoreLeaves) {
            this.table = table;
            this.partitionAll = partitionAll;
            this.partitions = partitions;
            this.indexs = indexs;
            this.ignoreLeaves = ignoreLeaves;
        }

        public Identifier getTable() {
            return table;
        }

        public boolean isPartitionAll() {
            return partitionAll;
        }

        public List<Identifier> getPartitions() {
            return partitions;
        }

        public List<Identifier> getIndexs() {
            return indexs;
        }

        public boolean isIgnoreLeaves() {
            return ignoreLeaves;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

    }


}
