import junit.framework.TestCase;
import parser.ast.AST;
import parser.visitor.OutputVisitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class AbstractSyntaxTest extends TestCase {

    private static final boolean debug = false;

    protected String outputMySQL(AST node, byte[] sql) {
        OutputVisitor ov = new OutputVisitor(sql);
        //遍历
        node.accept(ov);
        String sb = new String(ov.getData());
        if (debug) {
            System.out.println("DagonParser: " + getClass().getName() + "'s testcase: ");
            System.out.println("    " + sql);
            System.out.println("==>" + sb);
            System.out.println("--------------------------------------------------");
        }
        return sb;
    }
}
