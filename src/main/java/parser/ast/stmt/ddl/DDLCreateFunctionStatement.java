package parser.ast.stmt.ddl;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.DataType;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.compound.condition.Characteristic;
import parser.util.Pair;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DDLCreateFunctionStatement implements SQLStatement {
    private final Expression definer;
    private final Identifier name;
    private final List<Pair<Identifier, DataType>> parameters;
    private final DataType returns;
    private final List<Characteristic> characteristics;
    private final SQLStatement stmt;

    public DDLCreateFunctionStatement(Expression definer, Identifier name, List<Pair<Identifier, DataType>> parameters,
        DataType returns, List<Characteristic> characteristics, SQLStatement stmt) {
        this.definer = definer;
        this.name = name;
        this.parameters = parameters;
        this.returns = returns;
        this.characteristics = characteristics;
        this.stmt = stmt;
    }

    public Expression getDefiner() {
        return definer;
    }

    public Identifier getName() {
        return name;
    }

    public List<Pair<Identifier, DataType>> getParameters() {
        return parameters;
    }

    public DataType getReturns() {
        return returns;
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
        return SQLType.CREATE_FUNCTION;
    }

}
