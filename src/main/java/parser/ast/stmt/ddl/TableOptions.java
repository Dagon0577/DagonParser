package parser.ast.stmt.ddl;

import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.stmt.ddl.alter.AlterSpecification;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class TableOptions implements AlterSpecification {
    public static final int INSERT_METHOD_NO = 1;
    public static final int INSERT_METHOD_FIRST = 2;
    public static final int INSERT_METHOD_LAST = 3;
    public static final int _0 = 0;
    public static final int _1 = 1;
    public static final int _DEFAULT = 3;
    public static final int ROW_FORMAT_DEFAULT = 1;
    public static final int ROW_FORMAT_DYNAMIC = 2;
    public static final int ROW_FORMAT_FIXED = 3;
    public static final int ROW_FORMAT_COMPRESSED = 4;
    public static final int ROW_FORMAT_REDUNDANT = 5;
    public static final int ROW_FORMAT_COMPACT = 6;
    public static final int STORAGE_DISK = 1;
    public static final int STORAGE_MEMORY = 2;

    private Expression autoIncrement;
    private Expression avgRowLength;
    private Identifier charset;
    private Integer checksum;
    private Identifier collate;
    private LiteralString comment;
    private LiteralString compression;
    private LiteralString connection;
    private LiteralString dataDirectory;
    private LiteralString indexDirectory;
    private Integer delayKeyWrite;
    private Boolean encryption;
    private Identifier engine;
    private Integer insertMethod;
    private Expression keyBlockSize;
    private Long maxRows;
    private Long minRows;
    private Integer packKeys;
    private LiteralString password;
    private Integer rowFormat;
    private Integer statsAutoRecalc;
    private Integer statsPresistent;
    private Expression statSamplePages;
    private Identifier tablespace;
    private Integer storage;
    private List<Identifier> union;

    public Expression getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Expression autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Expression getAvgRowLength() {
        return avgRowLength;
    }

    public void setAvgRowLength(Expression avgRowLength) {
        this.avgRowLength = avgRowLength;
    }

    public Identifier getCharset() {
        return charset;
    }

    public void setCharset(Identifier charset) {
        this.charset = charset;
    }

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    public Identifier getCollate() {
        return collate;
    }

    public void setCollate(Identifier collate) {
        this.collate = collate;
    }

    public LiteralString getComment() {
        return comment;
    }

    public void setComment(LiteralString comment) {
        this.comment = comment;
    }

    public LiteralString getCompression() {
        return compression;
    }

    public void setCompression(LiteralString compression) {
        this.compression = compression;
    }

    public LiteralString getConnection() {
        return connection;
    }

    public void setConnection(LiteralString connection) {
        this.connection = connection;
    }

    public LiteralString getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(LiteralString dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public LiteralString getIndexDirectory() {
        return indexDirectory;
    }

    public void setIndexDirectory(LiteralString indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public Integer getDelayKeyWrite() {
        return delayKeyWrite;
    }

    public void setDelayKeyWrite(Integer delayKeyWrite) {
        this.delayKeyWrite = delayKeyWrite;
    }

    public Boolean getEncryption() {
        return encryption;
    }

    public void setEncryption(Boolean encryption) {
        this.encryption = encryption;
    }

    public Identifier getEngine() {
        return engine;
    }

    public void setEngine(Identifier engine) {
        this.engine = engine;
    }

    public Integer getInsertMethod() {
        return insertMethod;
    }

    public void setInsertMethod(Integer insertMethod) {
        this.insertMethod = insertMethod;
    }

    public Expression getKeyBlockSize() {
        return keyBlockSize;
    }

    public void setKeyBlockSize(Expression keyBlockSize) {
        this.keyBlockSize = keyBlockSize;
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

    public Integer getPackKeys() {
        return packKeys;
    }

    public void setPackKeys(Integer packKeys) {
        this.packKeys = packKeys;
    }

    public LiteralString getPassword() {
        return password;
    }

    public void setPassword(LiteralString password) {
        this.password = password;
    }

    public Integer getRowFormat() {
        return rowFormat;
    }

    public void setRowFormat(Integer rowFormat) {
        this.rowFormat = rowFormat;
    }

    public Integer getStatsAutoRecalc() {
        return statsAutoRecalc;
    }

    public void setStatsAutoRecalc(Integer statsAutoRecalc) {
        this.statsAutoRecalc = statsAutoRecalc;
    }

    public Integer getStatsPresistent() {
        return statsPresistent;
    }

    public void setStatsPresistent(Integer statsPresistent) {
        this.statsPresistent = statsPresistent;
    }

    public Expression getStatSamplePages() {
        return statSamplePages;
    }

    public void setStatSamplePages(Expression statSamplePages) {
        this.statSamplePages = statSamplePages;
    }

    public Identifier getTablespace() {
        return tablespace;
    }

    public void setTablespace(Identifier tablespace) {
        this.tablespace = tablespace;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public List<Identifier> getUnion() {
        return union;
    }

    public void setUnion(List<Identifier> union) {
        this.union = union;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
