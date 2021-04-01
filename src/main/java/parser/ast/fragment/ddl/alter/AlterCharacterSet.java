package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.visitor.Visitor;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class AlterCharacterSet implements AlterSpecification {
    private final Identifier charset;
    private final Identifier collate;

    public AlterCharacterSet(Identifier charset, Identifier collate) {
        this.charset = charset;
        this.collate = collate;
    }

    public Identifier getCharset() {
        return charset;
    }

    public Identifier getCollate() {
        return collate;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
