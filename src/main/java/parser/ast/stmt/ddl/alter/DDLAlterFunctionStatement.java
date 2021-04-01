package parser.ast.stmt.ddl.alter;

import parser.SQLType;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.compound.condition.Characteristic;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER FUNCTION func_name [characteristic ...]
 * 
 * characteristic:
 *     COMMENT 'string'
 *   | LANGUAGE SQL
 *   | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }
 *   | SQL SECURITY { DEFINER | INVOKER }
 *       </pre>
 */
public class DDLAlterFunctionStatement implements SQLStatement {
    private final Identifier name;
    private final List<Characteristic> characteristics;

    public DDLAlterFunctionStatement(Identifier name, List<Characteristic> characteristics) {
        this.name = name;
        this.characteristics = characteristics;
    }

    public Identifier getName() {
        return name;
    }

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.ALTER_FUNCTION;
    }

}
