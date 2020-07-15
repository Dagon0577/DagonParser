package parser.syntax;

import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLStatement;
import parser.lexer.Lexer;
import parser.token.Token;
import parser.util.Tuple2;

import java.nio.charset.Charset;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public class Parser {

    public static Tuple2<List<SQLStatement>, Exception> parse(byte[] sql, Charset charset) throws Exception {
        List<SQLStatement> stmts = new ArrayList<>();
        Exception err = null;
        try {
            parse(new Lexer(sql, charset), stmts);
        } catch (Exception e) {
            err = e;
        }
        return new Tuple2<List<SQLStatement>, Exception>(stmts, err);
    }

    protected static void parse(Lexer lexer, List<SQLStatement> stmts) throws SQLSyntaxErrorException {
        ExprParser exprParser = new ExprParser(lexer);
        while (lexer.token() != Token.EOF) {
            SQLStatement stmt = null;
            boolean isEOF = true;
            switch (lexer.token()) {
                case Token.KW_SELECT:
                case Token.PUNC_LEFT_PAREN:
                    stmt = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
            }
            if (isEOF) {
                while (lexer.token() == Token.PUNC_SEMICOLON) {
                    lexer.nextToken();
                }
            }
            long parseInfo = lexer.getAndResetParseInfo();
            if (stmt == null) {
                break;
            }
            if (stmt instanceof DMLStatement) {
                ((DMLStatement)stmt).setParseInfo(parseInfo);
                ((DMLStatement)stmt).setCachedTableName(lexer.getAndResetAffectedTables());
            }
            stmts.add(stmt);
        }
    }
}
