package parser.ast.fragment.ddl;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.util.Tuple3;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 *
 * <pre>
 * column_definition:
 *     data_type [NOT NULL | NULL] [DEFAULT {literal | (expr)} ]
 *       [AUTO_INCREMENT] [UNIQUE [KEY]] [[PRIMARY] KEY]
 *       [COMMENT 'string']
 *       [COLLATE collation_name]
 *       [COLUMN_FORMAT {FIXED|DYNAMIC|DEFAULT}]
 *       [STORAGE {DISK|MEMORY|DEFAULT}]
 *       [reference_definition]
 *       [check_constraint_definition]
 *   | data_type
 *       [COLLATE collation_name]
 *       [GENERATED ALWAYS] AS (expr)
 *       [VIRTUAL | STORED] [NOT NULL | NULL]
 *       [UNIQUE [KEY]] [[PRIMARY] KEY]
 *       [COMMENT 'string']
 *       [reference_definition]
 *       [check_constraint_definition]
 *
 * data_type:
 *     (see Chapter 11, Data Types)
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
 * check_constraint_definition:
 *     [CONSTRAINT [symbol]] CHECK (expr) [[NOT] ENFORCED]
 *
 * reference_definition:
 *     REFERENCES tbl_name (key_part,...)
 *       [MATCH FULL | MATCH PARTIAL | MATCH SIMPLE]
 *       [ON DELETE reference_option]
 *       [ON UPDATE reference_option]
 *
 * reference_option:
 *     RESTRICT | CASCADE | SET NULL | NO ACTION | SET DEFAULT
 *       </pre>
 */
public class ColumnDefinition implements AST {
    public static final int STORAGE_DISK = 1;
    public static final int STORAGE_MEMORY = 2;
    public static final int STORAGE_DEFAULT = 3;
    public static final int FORMAT_FIXED = 1;
    public static final int FORMAT_DYNAMIC = 2;
    public static final int FORMAT_DEFAULT = 3;

    private final DataType dataType;
    private final boolean notNull;
    private final Expression defaultVal;
    private final Boolean autoIncrement;
    private final Boolean uniqueKey;
    private final Boolean primaryKey;
    private final LiteralString comment;
    private final Integer columnFormat;
    private final Integer storage;
    private final ReferenceDefinition referenceDefinition;
    private final Expression as;
    private final Boolean virtual;
    private final Boolean stored;
    private final Expression onUpdate;
    private final Tuple3<Identifier, Expression, Boolean> checkConstraintDef;

    public ColumnDefinition(DataType dataType, boolean notNull, Expression defaultVal,
        Boolean autoIncrement, Boolean uniqueKey, Boolean primaryKey, LiteralString comment,
        Integer columnFormat, Integer storage, ReferenceDefinition referenceDefinition,
        Expression as, Boolean virtual, Boolean stored, Expression onUpdate,
        Tuple3<Identifier, Expression, Boolean> checkConstraintDef) {
        this.dataType = dataType;
        this.notNull = notNull;
        this.defaultVal = defaultVal;
        this.autoIncrement = autoIncrement;
        this.uniqueKey = uniqueKey;
        this.primaryKey = primaryKey;
        this.comment = comment;
        this.columnFormat = columnFormat;
        this.storage = storage;
        this.referenceDefinition = referenceDefinition;
        this.as = as;
        this.virtual = virtual;
        this.stored = stored;
        this.onUpdate = onUpdate;
        this.checkConstraintDef = checkConstraintDef;
    }

    public DataType getDataType() {
        return dataType;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public Expression getDefaultVal() {
        return defaultVal;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public Boolean getUniqueKey() {
        return uniqueKey;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public LiteralString getComment() {
        return comment;
    }

    public Integer getColumnFormat() {
        return columnFormat;
    }

    public Integer getStorage() {
        return storage;
    }

    public ReferenceDefinition getReferenceDefinition() {
        return referenceDefinition;
    }

    public Expression getAs() {
        return as;
    }

    public Boolean getVirtual() {
        return virtual;
    }

    public Boolean getStored() {
        return stored;
    }

    public Expression getOnUpdate() {
        return onUpdate;
    }

    public Tuple3<Identifier, Expression, Boolean> getCheckConstraintDef() {
        return checkConstraintDef;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}


