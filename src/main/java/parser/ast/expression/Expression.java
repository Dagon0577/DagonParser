package parser.ast.expression;

import parser.ast.AST;

import java.util.Map;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public interface Expression extends AST {
    int PRECEDENCE_QUERY = 0;
    int PRECEDENCE_ASSIGNMENT = 1;
    int PRECEDENCE_LOGICAL_OR = 2;
    int PRECEDENCE_LOGICAL_XOR = 3;
    int PRECEDENCE_LOGICAL_AND = 4;
    int PRECEDENCE_LOGICAL_NOT = 5;
    int PRECEDENCE_BETWEEN_AND = 6;
    int PRECEDENCE_COMPARISION = 7;
    int PRECEDENCE_ANY_ALL_SUBQUERY = 8;
    int PRECEDENCE_BIT_OR = 9;
    int PRECEDENCE_BIT_AND = 10;
    int PRECEDENCE_BIT_SHIFT = 11;
    int PRECEDENCE_ARITHMETIC_TERM_OP = 12;
    int PRECEDENCE_ARITHMETIC_FACTOR_OP = 13;
    int PRECEDENCE_BIT_XOR = 14;
    int PRECEDENCE_UNARY_OP = 15;
    int PRECEDENCE_BINARY = 16;
    int PRECEDENCE_COLLATE = 17;
    int PRECEDENCE_PRIMARY = 19;

    int getPrecedence();

    boolean replace(AST from, AST to);

    boolean removeSchema(byte[] schema);

    Object UNEVALUATABLE = new Object();

    Expression setCacheEvalRst(boolean cacheEvalRst);

    Object evaluation(Map<? extends Object, ? extends Object> parameters, byte[] sql) throws Exception;

    boolean exisitConstantCompare();

    boolean exisitConstantOperation();
}
