package parser.ast.expression.primary;

import parser.token.Token;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class Wildcard extends Identifier {

    public Wildcard(Identifier parent) {
        super(parent, Token.getBytes(Token.OP_ASTERISK), false);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
