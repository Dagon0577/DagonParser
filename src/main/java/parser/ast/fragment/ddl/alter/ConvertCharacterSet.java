package parser.ast.fragment.ddl.alter;

import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class ConvertCharacterSet implements AlterSpecification {
    private final boolean convertOrDefault;
    private final Identifier charset;
    private final Identifier collate;

    public ConvertCharacterSet(boolean convertOrDefault, Identifier charset, Identifier collate) {
        this.convertOrDefault = convertOrDefault;
        this.charset = charset;
        this.collate = collate;
    }

    public Identifier getCharset() {
        return charset;
    }

    public Identifier getCollate() {
        return collate;
    }

    public boolean getConverOrDefault() {
        return convertOrDefault;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
