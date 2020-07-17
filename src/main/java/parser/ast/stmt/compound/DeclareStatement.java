package parser.ast.stmt.compound;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.DataType;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class DeclareStatement implements SQLStatement {
    private final List<Identifier> varNames;
    private final DataType dataType;
    private final Expression defaultVal;

    public List<Identifier> getVarNames() {
        return varNames;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Expression getDefaultVal() {
        return defaultVal;
    }

    public DeclareStatement(List<Identifier> varNames, DataType dataType, Expression defaultVal) {
        this.varNames = varNames;
        this.dataType = dataType;
        this.defaultVal = defaultVal;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.DECLARE;
    }

}
