package parser.ast.stmt.transactional;

import parser.SQLType;
import parser.visitor.Visitor;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class SetTransactionStatement implements TrxStatement {
    public static final int READ_UNCOMMITTED = 1;
    public static final int READ_COMMITTED = 2;
    public static final int REPEATABLE_READ = 3;
    public static final int SERIALIZABLE = 4;
    public static final int READ_WRITE = 5;
    public static final int READ_ONLY = 6;
    private final Boolean global;
    private final List<Integer> characteristics;

    public SetTransactionStatement(Boolean global, List<Integer> characteristics) {
        this.global = global;
        this.characteristics = characteristics;
    }

    public Boolean isGlobal() {
        return global;
    }

    public List<Integer> getCharacteristics() {
        return characteristics;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.SET_TRANSACTION;
    }

}
