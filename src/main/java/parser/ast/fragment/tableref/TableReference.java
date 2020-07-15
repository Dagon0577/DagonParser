package parser.ast.fragment.tableref;

import parser.ast.AST;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public interface TableReference extends AST {
    int PRECEDENCE_REFS = 0;
    int PRECEDENCE_JOIN = 1;
    int PRECEDENCE_FACTOR = 2;

    int getPrecedence();

    Object removeLastConditionElement();

    boolean isSingleTable();

    boolean replace(AST from, AST to);

    boolean removeSchema(byte[] schema);
}
