package parser.syntax;

import parser.ast.stmt.SQLStatement;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangganyan
 * @date 2020/7/14
 */
public class Parser {

    public static Tuple2<List<SQLStatement>, Exception> parse(byte[] sql, Charset charset)
        throws Exception {
        List<SQLStatement> stmts = new ArrayList<>();
        Exception err = null;
        try {
            parse(new Lexer(sql, charset), stmts, false, false, null, false, null, null);
        } catch (Exception e) {
            err = e;
        }
        return new Tuple2<List<SQLStatement>, Exception>(stmts, err);
    }
}
