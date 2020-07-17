package parser.ast.stmt.ddl.alter;

import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2019年1月21日 下午3:10:02
 */
public class Lock implements AlterSpecification {
    public static final int DEFAULT = 1;
    public static final int NONE = 2;
    public static final int SHARED = 3;
    public static final int EXCLUSIVE = 4;

    private final int type;

    public Lock(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
