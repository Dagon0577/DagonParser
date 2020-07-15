package parser.ast;

import parser.visitor.Visitor;

import java.io.Serializable;

/**
 * @author Dagon0577
 * @date 2020/6/23
 */
public interface AST extends Serializable {
    void accept(Visitor visitor);
}
