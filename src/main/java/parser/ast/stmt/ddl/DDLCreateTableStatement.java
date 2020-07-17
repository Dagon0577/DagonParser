package parser.ast.stmt.ddl;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.QueryExpression;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.*;
import parser.ast.stmt.SQLStatement;
import parser.util.Pair;
import parser.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateTableStatement implements SQLStatement {

    private boolean isTemporary;
    private boolean ifNotExists;
    private Identifier tableName;
    private List<Pair<Identifier, ColumnDefinition>> columns;
    private IndexDefinition primaryKey;
    private List<IndexDefinition> uniqueKeys;
    private List<IndexDefinition> keys;
    private List<IndexDefinition> fullTextKeys;
    private List<IndexDefinition> spatialKeys;
    private List<ForeignKeyDefinition> foreignKeys;
    private List<Pair<Expression, Boolean>> checks;
    private List<Identifier> checksName;
    private TableOptions tableOptions;
    private PartitionOptions partitionOptions;
    private Boolean isIgnore;
    private Boolean isReplace;
    private QueryExpression queryExpression;
    private Identifier like;

    public DDLCreateTableStatement(boolean isTemporary, boolean ifNotExists, Identifier tableName) {
        this.isTemporary = isTemporary;
        this.ifNotExists = ifNotExists;
        this.tableName = tableName;
    }

    public DDLCreateTableStatement(boolean isTemporary, boolean ifNotExists, Identifier tableName,
        List<Pair<Expression, Boolean>> checks, Boolean isIgnore, Boolean isReplace, QueryExpression queryExpression) {
        this.isTemporary = isTemporary;
        this.ifNotExists = ifNotExists;
        this.tableName = tableName;
        this.isIgnore = isIgnore;
        this.isReplace = isReplace;
        this.queryExpression = queryExpression;
        this.checks = checks;
    }

    public DDLCreateTableStatement(boolean isTemporary, boolean ifNotExists, Identifier tableName, Identifier like) {
        this.isTemporary = isTemporary;
        this.ifNotExists = ifNotExists;
        this.tableName = tableName;
        this.like = like;
    }

    public void addChecksName(Identifier checkName) {
        if (checksName == null) {
            checksName = new ArrayList<>();
        }
        checksName.add(checkName);
    }

    public List<Identifier> getChecksName() {
        return checksName;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public void setTemporary(boolean isTemporary) {
        this.isTemporary = isTemporary;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public Identifier getTableName() {
        return tableName;
    }

    public void setTableName(Identifier tableName) {
        this.tableName = tableName;
    }

    public List<Pair<Identifier, ColumnDefinition>> getColumns() {
        return columns;
    }

    public void addColumn(Pair<Identifier, ColumnDefinition> column) {
        if (this.columns == null) {
            this.columns = new ArrayList<>();
        }
        this.columns.add(column);
    }

    public IndexDefinition getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(IndexDefinition primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<IndexDefinition> getUniqueKeys() {
        return uniqueKeys;
    }

    public void addUniqueIndex(IndexDefinition indexDef) {
        if (this.uniqueKeys == null) {
            this.uniqueKeys = new ArrayList<>();
        }
        this.uniqueKeys.add(indexDef);
    }

    public List<IndexDefinition> getKeys() {
        return keys;
    }

    public void addKey(IndexDefinition indexDef) {
        if (this.keys == null) {
            this.keys = new ArrayList<>();
        }
        this.keys.add(indexDef);
    }

    public List<IndexDefinition> getFullTextKeys() {
        return fullTextKeys;
    }

    public void addFullTextKey(IndexDefinition indexDef) {
        if (this.fullTextKeys == null) {
            this.fullTextKeys = new ArrayList<>();
        }
        this.fullTextKeys.add(indexDef);
    }

    public List<IndexDefinition> getSpatialKeys() {
        return spatialKeys;
    }

    public void addSpatialKey(IndexDefinition indexDef) {
        if (this.spatialKeys == null) {
            this.spatialKeys = new ArrayList<>();
        }
        this.spatialKeys.add(indexDef);
    }

    public List<ForeignKeyDefinition> getForeignKeys() {
        return foreignKeys;
    }

    public void addSpatialKey(ForeignKeyDefinition indexDef) {
        if (this.foreignKeys == null) {
            this.foreignKeys = new ArrayList<>();
        }
        this.foreignKeys.add(indexDef);
    }

    public List<Pair<Expression, Boolean>> getChecks() {
        return checks;
    }

    public void addCheck(Pair<Expression, Boolean> pair) {
        if (this.checks == null) {
            this.checks = new ArrayList<>();
        }
        this.checks.add(pair);
    }

    public TableOptions getTableOptions() {
        return tableOptions;
    }

    public void setTableOptions(TableOptions tableOptions) {
        this.tableOptions = tableOptions;
    }

    public PartitionOptions getPartitionOptions() {
        return partitionOptions;
    }

    public void setPartitionOptions(PartitionOptions partitionOptions) {
        this.partitionOptions = partitionOptions;
    }

    public Boolean getIsIgnore() {
        return isIgnore;
    }

    public void setIsIgnore(Boolean isIgnore) {
        this.isIgnore = isIgnore;
    }

    public Boolean getIsReplace() {
        return isReplace;
    }

    public void setIsReplace(Boolean isReplace) {
        this.isReplace = isReplace;
    }

    public QueryExpression getQueryExpression() {
        return queryExpression;
    }

    public void setQueryExpression(QueryExpression queryExpression) {
        this.queryExpression = queryExpression;
    }

    public Identifier getLike() {
        return like;
    }

    public void setLike(Identifier like) {
        this.like = like;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CREATE_TABLE;
    }

}
