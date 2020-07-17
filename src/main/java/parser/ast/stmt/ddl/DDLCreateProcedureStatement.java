package parser.ast.stmt.ddl;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.DataType;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.compound.condition.Characteristic;
import parser.visitor.Visitor;
import parser.util.Tuple3;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateProcedureStatement implements SQLStatement {
    public static final int IN = 1;
    public static final int OUT = 2;
    public static final int INOUT = 3;

    private final Expression definer;
    private final Identifier name;
    private final List<Tuple3<Integer, Identifier, DataType>> parameters;
    private final List<Characteristic> characteristics;
    private final SQLStatement stmt;

    public DDLCreateProcedureStatement(Expression definer, Identifier name,
        List<Tuple3<Integer, Identifier, DataType>> parameters, List<Characteristic> characteristics,
        SQLStatement stmt) {
        this.definer = definer;
        this.name = name;
        this.parameters = parameters;
        this.characteristics = characteristics;
        this.stmt = stmt;
    }

    public Expression getDefiner() {
        return definer;
    }

    public Identifier getName() {
        return name;
    }

    public List<Tuple3<Integer, Identifier, DataType>> getParameters() {
        return parameters;
    }

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public SQLStatement getStmt() {
        return stmt;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.CREATE_PROCEDURE;
    }

}
