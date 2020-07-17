package parser.ast.stmt.compound.condition;

import parser.ast.AST;
import parser.ast.expression.primary.literal.LiteralString;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class Characteristic implements AST {
    public static final int COMMENT = 1;
    public static final int LANGUAGE_SQL = 2;
    public static final int DETERMINISTIC = 3;
    public static final int NOT_DETERMINISTIC = 4;
    public static final int CONTAINS_SQL = 5;
    public static final int NO_SQL = 6;
    public static final int READS_SQL_DATA = 7;
    public static final int MODIFIES_SQL_DATA = 8;
    public static final int SQL_SECURITY_DEFINER = 9;
    public static final int SQL_SECURITY_INVOKER = 10;

    private final int type;
    private final LiteralString comment;

    public Characteristic(int type, LiteralString comment) {
        this.type = type;
        this.comment = comment;
    }

    public int getType() {
        return type;
    }

    public LiteralString getComment() {
        return comment;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
