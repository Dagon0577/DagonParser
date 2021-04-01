package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.LiteralNumber;
import parser.ast.fragment.ddl.PartitionDefinition;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日 
 * 
 */
public class PartitionOperation implements AlterSpecification {
    public static final int ADD = 1;
    public static final int DROP = 2;
    public static final int DISCARD = 3;
    public static final int IMPORT = 4;
    public static final int TRUNCATE = 5;
    public static final int COALESCE = 6;
    public static final int REORGANIZE = 7;
    public static final int EXCHANGE = 8;
    public static final int ANALYZE = 9;
    public static final int CHECK = 10;
    public static final int OPTIMIZE = 11;
    public static final int REBUILD = 12;
    public static final int REPAIR = 13;
    public static final int REMOVE = 14;
    public static final int UPGRADE = 15;

    private final int type;
    private final PartitionDefinition definition;
    private final List<Identifier> names;
    private final List<PartitionDefinition> definitions;
    private final boolean all;
    private final Identifier table;
    private final Boolean withValidation;
    private final LiteralNumber number;

    public PartitionOperation(int type, PartitionDefinition definition, List<Identifier> names,
            List<PartitionDefinition> definitions, boolean all, Identifier table,
            Boolean withValidation, LiteralNumber number) {
        this.type = type;
        this.definition = definition;
        this.names = names;
        this.definitions = definitions;
        this.all = all;
        this.table = table;
        this.withValidation = withValidation;
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public PartitionDefinition getDefinition() {
        return definition;
    }

    public List<Identifier> getNames() {
        return names;
    }

    public List<PartitionDefinition> getDefinitions() {
        return definitions;
    }

    public boolean isAll() {
        return all;
    }

    public Identifier getTable() {
        return table;
    }

    public Boolean getWithValidation() {
        return withValidation;
    }

    public LiteralNumber getNumber() {
        return number;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


}
