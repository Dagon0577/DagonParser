package parser.syntax;

import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLDeleteStatement;
import parser.ast.stmt.dml.DMLStatement;
import parser.lexer.Lexer;
import parser.token.Keywords;
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

    public static List<SQLStatement> parse(Lexer lexer) throws SQLSyntaxErrorException {
        List<SQLStatement> stmt = new ArrayList<>();
        try {
            parse(lexer, stmt);
        } catch (SQLSyntaxErrorException e) {
            throw e;
        }
        return stmt;
    }

    protected static void parse(Lexer lexer, List<SQLStatement> stmts) throws SQLSyntaxErrorException {
        ExprParser exprParser = new ExprParser(lexer);
        while (lexer.token() != Token.EOF) {
            SQLStatement stmt = null;
            boolean isEOF = true;
            stmtSwitch:
            switch (lexer.token()) {
                case Token.KW_SELECT:
                case Token.PUNC_LEFT_PAREN:
                    stmt = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
                    break;
                case Token.KW_CREATE:
                    stmt = new MySQLDDLParser(lexer, exprParser).create();
                    break;
                case Token.KW_SET:
                    stmt = new MySQLDALParser(lexer, exprParser).set();
                    break;
                case Token.KW_DECLARE:
                    stmt = new MySQLCmpdParser(lexer, exprParser).declare();
                    break;
                case Token.KW_UPDATE:
                    stmt = new MySQLDMLParser(lexer, exprParser).update();
                    break;
                case Token.KW_INSERT:
                    stmt = new MySQLDMLParser(lexer, exprParser).insert();
                    break;
                case Token.KW_DELETE:
                    stmt = new MySQLDMLParser(lexer, exprParser).delete();
                    ((DMLDeleteStatement)stmt).setConditions(lexer.getAndResetConditions());
                    break;
                case Token.KW_REPLACE:
                    stmt = new MySQLDMLParser(lexer, exprParser).replace();
                    break;
                case Token.KW_ALTER:
                    stmt = new MySQLDDLParser(lexer, exprParser).alter();
                    break;
                case Token.KW_CALL:
                    stmt = new MySQLDMLParser(lexer, exprParser).call();
                    break;
                case Token.KW_LOAD:
                    stmt = new MySQLDMLParser(lexer, exprParser).load();
                    break;
                case Token.IDENTIFIER: {
                    switch (lexer.parseKeyword()) {
                        case Keywords.END:
                            return;
                        case Keywords.BEGIN:
                            lexer.nextToken();
                            if (lexer.token() == Token.EOF || lexer.token() == Token.PUNC_SEMICOLON) {
                                stmt = new MySQLMTSParser(lexer, exprParser).begin(false);
                            } else if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.WORK) {
                                lexer.nextToken();
                                stmt = new MySQLMTSParser(lexer, exprParser).begin(true);
                            } else {
                                stmt = new MySQLCmpdParser(lexer, exprParser).beginEnd(null);
                            }
                            break stmtSwitch;
                        case Keywords.DO:
                            stmt = new MySQLDMLParser(lexer, exprParser).parseDo();
                            break stmtSwitch;
                        case Keywords.HANDLER:
                            stmt = new MySQLDMLParser(lexer, exprParser).handler();
                            break stmtSwitch;
                        case Keywords.IMPORT:
                            stmt = new MySQLDMLParser(lexer, exprParser).parseImport();
                            break stmtSwitch;
                    }
                    Identifier label = exprParser.identifier();
                    byte[] id = label.getIdText();
                    stmt = new MySQLCmpdParser(lexer, exprParser).parseWithIdentifier(label);
                    break;
                }
            }
            while (lexer.token() == Token.PUNC_SEMICOLON) {
                lexer.nextToken();
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
