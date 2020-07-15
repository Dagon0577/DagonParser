package parser.ast.fragment.ddl;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/14
 * <pre>
 * partition_options:
 *   PARTITION BY
 *   { [LINEAR] HASH(expr)
 *  | [LINEAR] KEY [ALGORITHM={1|2}] (column_list)
 *  | RANGE{(expr) | COLUMNS(column_list)}
 *  | LIST{(expr) | COLUMNS(column_list)} }
 *  [PARTITIONS num]
 *  [SUBPARTITION BY
 *  { [LINEAR] HASH(expr)
 *  | [LINEAR] KEY [ALGORITHM={1|2}] (column_list) }
 *  [SUBPARTITIONS num]
 *  ]
 *  [(partition_definition [, partition_definition] ...)]
 *  </pre>
 */
public class PartitionOptions implements AST {
    private Identifier name;
    private boolean isLiner;
    private Expression hash;
    private boolean isKey;
    private int algorithm;
    private List<Identifier> keyColumns;
    private Expression rangeExpr;
    private List<Identifier> rangeColumns;
    private Expression listExpr;
    private List<Identifier> listColumns;
    private Long partitionNum;
    private boolean isSubpartitionLiner;
    private Expression subpartitionHash;
    private boolean isSubpartitionKey;
    private int subpartitionAlgorithm;
    private List<Identifier> subpartitionKeyColumns;
    private Long subpartitionNum;
    private List<PartitionDefinition> partitionDefinitions;

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public boolean isLiner() {
        return isLiner;
    }

    public void setLiner(boolean isLiner) {
        this.isLiner = isLiner;
    }

    public Expression getHash() {
        return hash;
    }

    public void setHash(Expression hash) {
        this.hash = hash;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
    }

    public List<Identifier> getKeyColumns() {
        return keyColumns;
    }

    public void setKeyColumns(List<Identifier> keyColumns) {
        this.keyColumns = keyColumns;
    }

    public Expression getRangeExpr() {
        return rangeExpr;
    }

    public void setRangeExpr(Expression rangeExpr) {
        this.rangeExpr = rangeExpr;
    }

    public List<Identifier> getRangeColumns() {
        return rangeColumns;
    }

    public void setRangeColumns(List<Identifier> rangeColumns) {
        this.rangeColumns = rangeColumns;
    }

    public Expression getListExpr() {
        return listExpr;
    }

    public void setListExpr(Expression listExpr) {
        this.listExpr = listExpr;
    }

    public List<Identifier> getListColumns() {
        return listColumns;
    }

    public void setListColumns(List<Identifier> listColumns) {
        this.listColumns = listColumns;
    }

    public Long getPartitionNum() {
        return partitionNum;
    }

    public void setPartitionNum(Long partitionNum) {
        this.partitionNum = partitionNum;
    }

    public boolean isSubpartitionLiner() {
        return isSubpartitionLiner;
    }

    public void setSubpartitionLiner(boolean isSubpartitionLiner) {
        this.isSubpartitionLiner = isSubpartitionLiner;
    }

    public Expression getSubpartitionHash() {
        return subpartitionHash;
    }

    public void setSubpartitionHash(Expression subpartitionHash) {
        this.subpartitionHash = subpartitionHash;
    }

    public boolean isSubpartitionKey() {
        return isSubpartitionKey;
    }

    public void setSubpartitionKey(boolean isSubpartitionKey) {
        this.isSubpartitionKey = isSubpartitionKey;
    }

    public int getSubpartitionAlgorithm() {
        return subpartitionAlgorithm;
    }

    public void setSubpartitionAlgorithm(int subpartitionAlgorithm) {
        this.subpartitionAlgorithm = subpartitionAlgorithm;
    }

    public List<Identifier> getSubpartitionKeyColumns() {
        return subpartitionKeyColumns;
    }

    public void setSubpartitionKeyColumns(List<Identifier> subpartitionKeyColumns) {
        this.subpartitionKeyColumns = subpartitionKeyColumns;
    }

    public Long getSubpartitionNum() {
        return subpartitionNum;
    }

    public void setSubpartitionNum(Long subpartitionNum) {
        this.subpartitionNum = subpartitionNum;
    }

    public List<PartitionDefinition> getPartitionDefinitions() {
        return partitionDefinitions;
    }

    public void setPartitionDefinitions(List<PartitionDefinition> partitionDefinitions) {
        this.partitionDefinitions = partitionDefinitions;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}

