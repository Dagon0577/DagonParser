package parser.ast.fragment.ddl.alter;

import parser.ast.fragment.ddl.alter.interfaces.AlterSpecification;
import parser.visitor.Visitor;


/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 */
public class ImportTablespace implements AlterSpecification {
    private final boolean isImport;

    public ImportTablespace(boolean isImport) {
        this.isImport = isImport;
    }

    public boolean isImport() {
        return this.isImport;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
