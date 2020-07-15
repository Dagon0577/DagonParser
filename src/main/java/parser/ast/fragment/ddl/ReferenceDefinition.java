package parser.ast.fragment.ddl;

import parser.ast.AST;
import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 * <pre>
 * reference_definition:
 * REFERENCES tbl_name (key_part,...)
 * [MATCH FULL | MATCH PARTIAL | MATCH SIMPLE]
 * [ON DELETE reference_option]
 * [ON UPDATE reference_option]
 *       </pre>
 */
public class ReferenceDefinition implements AST {
    public static final int MATCH_FULL = 1;
    public static final int MATCH_PARTIAL = 2;
    public static final int MATCH_SIMPLE = 3;

    public static final int RESTRICT = 1;
    public static final int CASCADE = 2;
    public static final int SET_NULL = 3;
    public static final int NO_ACTION = 4;
    public static final int SET_DEFAULT = 5;

    private final Identifier table;
    private final List<IndexColumnName> keys;
    private final Integer match;
    private final Integer onDelete;
    private final Integer onUpdate;

    public ReferenceDefinition(Identifier table, List<IndexColumnName> keys, Integer match, Integer onDelete,
        Integer onUpdate) {
        this.table = table;
        this.keys = keys;
        this.match = match;
        this.onDelete = onDelete;
        this.onUpdate = onUpdate;
    }

    public Identifier getTable() {
        return table;
    }

    public List<IndexColumnName> getKeys() {
        return keys;
    }

    public Integer getMatch() {
        return match;
    }

    public Integer getOnDelete() {
        return onDelete;
    }

    public Integer getOnUpdate() {
        return onUpdate;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
