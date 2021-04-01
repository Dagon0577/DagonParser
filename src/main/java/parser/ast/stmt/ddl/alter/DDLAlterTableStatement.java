package parser.ast.stmt.ddl.alter;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.PartitionOptions;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER TABLE tbl_name
 *     [alter_specification [, alter_specification] ...]
 *     [partition_options]
 * 
 * alter_specification:
 *     table_options
 *   | ADD [COLUMN] col_name column_definition
 *         [FIRST | AFTER col_name]
 *   | ADD [COLUMN] (col_name column_definition,...)
 *   | ADD {INDEX|KEY} [index_name]
 *         [index_type] (key_part,...) [index_option] ...
 *   | ADD [CONSTRAINT [symbol]] PRIMARY KEY
 *         [index_type] (key_part,...) [index_option] ...
 *   | ADD [CONSTRAINT [symbol]]
 *         UNIQUE [INDEX|KEY] [index_name]
 *         [index_type] (key_part,...) [index_option] ...
 *   | ADD FULLTEXT [INDEX|KEY] [index_name]
 *         (key_part,...) [index_option] ...
 *   | ADD SPATIAL [INDEX|KEY] [index_name]
 *         (key_part,...) [index_option] ...
 *   | ADD [CONSTRAINT [symbol]]
 *         FOREIGN KEY [index_name] (col_name,...)
 *         reference_definition
 *   | ALGORITHM [=] {DEFAULT|INSTANT|INPLACE|COPY}
 *   | ALTER [COLUMN] col_name {SET DEFAULT literal | DROP DEFAULT}
 *   | ALTER INDEX index_name {VISIBLE | INVISIBLE}
 *   | CHANGE [COLUMN] old_col_name new_col_name column_definition
 *         [FIRST|AFTER col_name]
 *   | [DEFAULT] CHARACTER SET [=] charset_name [COLLATE [=] collation_name]
 *   | CONVERT TO CHARACTER SET charset_name [COLLATE collation_name]
 *   | {DISABLE|ENABLE} KEYS
 *   | {DISCARD|IMPORT} TABLESPACE
 *   | DROP [COLUMN] col_name
 *   | DROP {INDEX|KEY} index_name
 *   | DROP PRIMARY KEY
 *   | DROP FOREIGN KEY fk_symbol
 *   | FORCE
 *   | LOCK [=] {DEFAULT|NONE|SHARED|EXCLUSIVE}
 *   | MODIFY [COLUMN] col_name column_definition
 *         [FIRST | AFTER col_name]
 *   | ORDER BY col_name [, col_name] ...
 *   | RENAME COLUMN old_col_name TO new_col_name
 *   | RENAME {INDEX|KEY} old_index_name TO new_index_name
 *   | RENAME [TO|AS] new_tbl_name
 *   | {WITHOUT|WITH} VALIDATION
 *   | ADD PARTITION (partition_definition)
 *   | DROP PARTITION partition_names
 *   | DISCARD PARTITION {partition_names | ALL} TABLESPACE
 *   | IMPORT PARTITION {partition_names | ALL} TABLESPACE
 *   | TRUNCATE PARTITION {partition_names | ALL}
 *   | COALESCE PARTITION number
 *   | REORGANIZE PARTITION partition_names INTO (partition_definitions)
 *   | EXCHANGE PARTITION partition_name WITH TABLE tbl_name [{WITH|WITHOUT} VALIDATION]
 *   | ANALYZE PARTITION {partition_names | ALL}
 *   | CHECK PARTITION {partition_names | ALL}
 *   | OPTIMIZE PARTITION {partition_names | ALL}
 *   | REBUILD PARTITION {partition_names | ALL}
 *   | REPAIR PARTITION {partition_names | ALL}
 *   | REMOVE PARTITIONING
 *   | UPGRADE PARTITIONING
 * 
 * key_part: {col_name [(length)] | (expr)} [ASC | DESC]
 * 
 * index_type:
 *     USING {BTREE | HASH}
 * 
 * index_option:
 *     KEY_BLOCK_SIZE [=] value
 *   | index_type
 *   | WITH PARSER parser_name
 *   | COMMENT 'string'
 *   | {VISIBLE | INVISIBLE}
 * 
 * table_options:
 *     table_option [[,] table_option] ...
 * 
 * table_option:
 *     AUTO_INCREMENT [=] value
 *   | AVG_ROW_LENGTH [=] value
 *   | [DEFAULT] CHARACTER SET [=] charset_name
 *   | CHECKSUM [=] {0 | 1}
 *   | [DEFAULT] COLLATE [=] collation_name
 *   | COMMENT [=] 'string'
 *   | COMPRESSION [=] {'ZLIB'|'LZ4'|'NONE'}
 *   | CONNECTION [=] 'connect_string'
 *   | {DATA|INDEX} DIRECTORY [=] 'absolute path to directory'
 *   | DELAY_KEY_WRITE [=] {0 | 1}
 *   | ENCRYPTION [=] {'Y' | 'N'}
 *   | ENGINE [=] engine_name
 *   | INSERT_METHOD [=] { NO | FIRST | LAST }
 *   | KEY_BLOCK_SIZE [=] value
 *   | MAX_ROWS [=] value
 *   | MIN_ROWS [=] value
 *   | PACK_KEYS [=] {0 | 1 | DEFAULT}
 *   | PASSWORD [=] 'string'
 *   | ROW_FORMAT [=] {DEFAULT|DYNAMIC|FIXED|COMPRESSED|REDUNDANT|COMPACT}
 *   | STATS_AUTO_RECALC [=] {DEFAULT|0|1}
 *   | STATS_PERSISTENT [=] {DEFAULT|0|1}
 *   | STATS_SAMPLE_PAGES [=] value
 *   | TABLESPACE tablespace_name [STORAGE {DISK|MEMORY|DEFAULT}]
 *   | UNION [=] (tbl_name[,tbl_name]...)
 * 
 * partition_options:
 *     (see CREATE TABLE options)
 *       </pre>
 */
public class DDLAlterTableStatement implements SQLStatement {
    private final Identifier name;
    private final List<AlterSpecification> alters;
    private final PartitionOptions partitionOptions;

    public DDLAlterTableStatement(Identifier name, List<AlterSpecification> alters,
            PartitionOptions partitionOptions) {
        this.name = name;
        this.alters = alters;
        this.partitionOptions = partitionOptions;
    }

    public Identifier getName() {
        return name;
    }

    public List<AlterSpecification> getAlters() {
        return alters;
    }

    public PartitionOptions getPartitionOptions() {
        return partitionOptions;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.ALTER_TABLE;
    }

}
