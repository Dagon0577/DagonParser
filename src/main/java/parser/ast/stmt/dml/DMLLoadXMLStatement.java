package parser.ast.stmt.dml;

import parser.SQLType;
import parser.ast.AST;
import parser.ast.expression.ComparisionExpression;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.visitor.Visitor;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Dagon0577
 * @date 2021年04月02日
 * 
 *       <pre>
 *  LOAD XML [LOW_PRIORITY | CONCURRENT] [LOCAL] INFILE 'file_name'
 *      [REPLACE | IGNORE]
 *      INTO TABLE [db_name.]tbl_name
 *      [CHARACTER SET charset_name]
 *      [ROWS IDENTIFIED BY '<tagname>']
 *      [IGNORE number {LINES | ROWS}]
 *      [(field_name_or_user_var
 *          [, field_name_or_user_var] ...)]
 *      [SET col_name={expr | DEFAULT},
 *          [, col_name={expr | DEFAULT}] ...]
 *       </pre>
 */
public class DMLLoadXMLStatement implements DMLStatement {

    private static final long serialVersionUID = -1122065316710559567L;
    private long parseInfo;
    public static final int LOW_PRIORITY = 1;
    public static final int CONCURRENT = 2;

    public static final int REPLACE = 1;
    public static final int IGNORE = 2;

    private Integer priority;
    private Boolean isLocal;
    private LiteralString fileName;
    private Integer insertType;
    private Identifier table;
    private LiteralString identified;
    private Identifier charset;
    private Long ignoreLine;
    private List<Expression> columns;
    private List<ComparisionExpression> values;

    private byte[] cachedTableName;

    public DMLLoadXMLStatement(Integer priority, Boolean isLocal, LiteralString fileName,
            Integer insertType, Identifier table, LiteralString identified, Identifier charset,
            Long ignoreLine, List<Expression> columns, List<ComparisionExpression> values) {
        this.priority = priority;
        this.isLocal = isLocal;
        this.fileName = fileName;
        this.insertType = insertType;
        this.table = table;
        this.identified = identified;
        this.charset = charset;
        this.ignoreLine = ignoreLine;
        this.columns = columns;
        this.values = values;
    }

    public Integer getPriority() {
        return priority;
    }

    public Boolean isLocal() {
        return isLocal;
    }

    public LiteralString getFileName() {
        return fileName;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public Identifier getTable() {
        return table;
    }

    public LiteralString getIdentified() {
        return identified;
    }

    public Identifier getCharset() {
        return charset;
    }

    public Long getIgnoreLine() {
        return ignoreLine;
    }

    public List<Expression> getColumns() {
        return columns;
    }

    public List<ComparisionExpression> getValues() {
        return values;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.LOAD_XML;
    }

    @Override
    public void setParseInfo(long parseInfo) {
        this.parseInfo = parseInfo;
    }

    @Override
    public long getParseInfo() {
        return parseInfo;
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (table != null) {
            if (table.equals(from)) {
                table = (Identifier) to;
                result = true;
            } else {
                result = table.replace(from, to);
            }
        }
        if (columns != null) {
            Iterator<Expression> iter = columns.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Expression exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        columns.set(i, (Expression) to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        if (values != null) {
            Iterator<ComparisionExpression> iter = values.iterator();
            int i = 0;
            while (iter.hasNext()) {
                ComparisionExpression exp = iter.next();
                if (exp != null) {
                    if (exp.equals(from)) {
                        values.set(i, (ComparisionExpression) to);
                        result = true;
                    } else {
                        result |= exp.replace(from, to);
                    }
                }
                i++;
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        boolean removed = false;
        if (table != null) {
            removed = table.removeSchema(schema);
        }
        if (columns != null) {
            for (Expression exp : columns) {
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        if (values != null) {
            for (ComparisionExpression exp : values) {
                if (exp != null) {
                    removed |= exp.removeSchema(schema);
                }
            }
        }
        return removed;
    }

    @Override
    public void setCachedTableName(byte[] cachedTableName) {
        this.cachedTableName = cachedTableName;
    }

    @Override
    public byte[] getCachedTableName() {
        return cachedTableName;
    }

    @Override
    public boolean maybeMoreThanTwoTable() {
        return false;
    }
}
