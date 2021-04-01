package parser.ast.fragment.ddl.alter;

import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class Algorithm implements AlterSpecification {
    public static final int DEFAULT = 1;
    public static final int INPLACE = 2;
    public static final int COPY = 3;
    public static final int INSTANT = 4;

    private final int type;

    public Algorithm(int type) {
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
