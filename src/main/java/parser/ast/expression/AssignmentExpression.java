package parser.ast.expression;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class AssignmentExpression extends BinaryOperatorExpression {

    public AssignmentExpression(Expression leftOprand, Expression rightOprand) {
        super(leftOprand, rightOprand, Expression.PRECEDENCE_ASSIGNMENT);
    }

    @Override
    public String getOperator() {
        return ":=";
    }

}
