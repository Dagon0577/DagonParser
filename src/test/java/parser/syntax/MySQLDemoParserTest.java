package parser.syntax;

import mysql.charset.MySqlCharset;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLSelectStatement;
import parser.util.Tuple2;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class MySQLDemoParserTest extends AbstractSyntaxTest {

    public void test() throws Exception {
        //byte流
        byte[] sql = "select id from merchant".getBytes();
        //语法解析（包含词法解析）
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        //获取AST树
        DMLSelectStatement select = (DMLSelectStatement)tuple._1().get(0);
        //遍历输出AST树
        String output = outputMySQL(select, sql);
        assertEquals("SELECT id FROM merchant", output);
    }

}
