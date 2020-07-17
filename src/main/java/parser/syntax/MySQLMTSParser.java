package parser.syntax;

import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.transactional.BeginStatement;
import parser.lexer.Lexer;

import java.sql.SQLSyntaxErrorException;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class MySQLMTSParser extends AbstractParser {

    private MySQLDMLParser mySQLDMLParser;
    private ExprParser exprParser;

    public MySQLMTSParser(Lexer lexer, ExprParser exprParser) {
        super(lexer);
        this.exprParser = exprParser;
    }

    public SQLStatement begin(boolean isWork) throws SQLSyntaxErrorException {
        if (isWork) {
            return new BeginStatement(true);
        } else {
            return new BeginStatement(false);
        }
    }
}
