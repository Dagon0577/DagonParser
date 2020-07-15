package parser.ast.fragment.ddl;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralString;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class DataType implements AST {
    public static final int BIT = 1;
    public static final int TINYINT = 2;
    public static final int BOOL = 3;
    public static final int BOOLEAN = 4;
    public static final int SMALLINT = 5;
    public static final int MEDIUMINT = 6;
    public static final int INT = 7;
    public static final int INTEGER = 8;
    public static final int BIGINT = 9;
    public static final int DECIMAL = 10;
    public static final int DEC = 11;
    public static final int NUMERIC = 12;
    public static final int FIXED = 13;
    public static final int FLOAT = 14;
    public static final int DOUBLE = 15;

    public static final int DATE = 16;
    public static final int DATETIME = 17;
    public static final int TIMESTAMP = 18;
    public static final int TIME = 19;
    public static final int YEAR = 20;

    public static final int CHAR = 21;
    public static final int VARCHAR = 22;
    public static final int BINARY = 23;
    public static final int VARBINARY = 24;
    public static final int TINYBLOB = 25;
    public static final int TINYTEXT = 26;
    public static final int BLOB = 27;
    public static final int TEXT = 28;
    public static final int MEDIUMBLOB = 29;
    public static final int MEDIUMTEXT = 30;
    public static final int LONGBLOB = 31;
    public static final int LONGTEXT = 32;
    public static final int ENUM = 33;
    public static final int SET = 34;

    public static final int GEOMETRY = 35;
    public static final int POINT = 36;
    public static final int LINESTRING = 37;
    public static final int POLYGON = 38;
    public static final int MULTIPOINT = 39;
    public static final int MULTILINESTRING = 40;
    public static final int MULTIPOLYGON = 41;
    public static final int GEOMETRYCOLLECTION = 42;

    public static final int JSON = 43;
    public static final int REAL = 44;
    public static final int SERIAL = 45;

    public static final String[] types = new String[48];

    static {
        types[1] = "BIT".toLowerCase();
        types[2] = "TINYINT".toLowerCase();
        types[3] = "BOOL".toLowerCase();
        types[4] = "BOOLEAN".toLowerCase();
        types[5] = "SMALLINT".toLowerCase();
        types[6] = "MEDIUMINT".toLowerCase();
        types[7] = "INT".toLowerCase();
        types[8] = "INTEGER".toLowerCase();
        types[9] = "BIGINT".toLowerCase();
        types[10] = "DECIMAL".toLowerCase();
        types[11] = "DEC".toLowerCase();
        types[12] = "NUMERIC".toLowerCase();
        types[13] = "FIXED".toLowerCase();
        types[14] = "FLOAT".toLowerCase();
        types[15] = "DOUBLE".toLowerCase();
        types[16] = "DATE".toLowerCase();
        types[17] = "DATETIME".toLowerCase();
        types[18] = "TIMESTAMP".toLowerCase();
        types[19] = "TIME".toLowerCase();
        types[20] = "YEAR".toLowerCase();
        types[21] = "CHAR".toLowerCase();
        types[22] = "VARCHAR".toLowerCase();
        types[23] = "BINARY".toLowerCase();
        types[24] = "VARBINARY".toLowerCase();
        types[25] = "TINYBLOB".toLowerCase();
        types[26] = "TINYTEXT".toLowerCase();
        types[27] = "BLOB".toLowerCase();
        types[28] = "TEXT".toLowerCase();
        types[29] = "MEDIUMBLOB".toLowerCase();
        types[30] = "MEDIUMTEXT".toLowerCase();
        types[31] = "LONGBLOB".toLowerCase();
        types[32] = "LONGTEXT".toLowerCase();
        types[33] = "ENUM".toLowerCase();
        types[34] = "SET".toLowerCase();
        types[35] = "GEOMETRY".toLowerCase();
        types[36] = "POINT".toLowerCase();
        types[37] = "LINESTRING".toLowerCase();
        types[38] = "POLYGON".toLowerCase();
        types[39] = "MULTIPOINT".toLowerCase();
        types[40] = "MULTILINESTRING".toLowerCase();
        types[41] = "MULTIPOLYGON".toLowerCase();
        types[42] = "GEOMETRYCOLLECTION".toLowerCase();
        types[43] = "JSON".toLowerCase();
    }

    private final int type;
    private final boolean unsigned;
    private final boolean zerofill;
    private final boolean binary;
    private final Integer length;
    private final Integer decimals;
    private final Identifier charset;
    private Identifier collation;
    private final List<LiteralString> valueList;

    public DataType(int type, boolean unsigned, boolean zerofill, boolean binary, Integer length, Integer decimals,
        Identifier charset, Identifier collation, List<LiteralString> valueList) {
        this.type = type;
        this.unsigned = unsigned;
        this.zerofill = zerofill;
        this.binary = binary;
        this.length = length;
        this.decimals = decimals;
        this.charset = charset;
        this.collation = collation;
        this.valueList = valueList;
    }

    public int getType() {
        return type;
    }

    public boolean isUnsigned() {
        return unsigned;
    }

    public boolean isZerofill() {
        return zerofill;
    }

    public boolean isBinary() {
        return binary;
    }

    public Integer getLength() {
        return length;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public Identifier getCharset() {
        return charset;
    }

    public Identifier getCollation() {
        return collation;
    }

    public void setCollation(Identifier collation) {
        this.collation = collation;
    }

    public List<LiteralString> getValueList() {
        return valueList;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getTypeName() {
        return types[type];
    }

}

