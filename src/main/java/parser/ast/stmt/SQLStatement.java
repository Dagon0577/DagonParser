package parser.ast.stmt;

import parser.ast.AST;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public interface SQLStatement extends AST {
    int getSQLType();
}
