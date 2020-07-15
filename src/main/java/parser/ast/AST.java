package parser.ast;

import parser.visitor.Visitor;

import java.io.Serializable;

/**
 * @author huangganyan
 * @date 2020/7/14
 */
public interface AST extends Serializable {
    void accept(Visitor visitor);
}
