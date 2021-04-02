import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLCallStatement;
import mysql.charset.MySqlCharset;
import parser.syntax.Parser;
import parser.util.Tuple2;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2021年04月02日
 */
public class MySQLDMLCallParserTest extends AbstractSyntaxTest{
    public void testCall()throws Exception{
        byte[] sql = "call p(?,?)".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        DMLCallStatement calls =(DMLCallStatement) tuple._1().get(0);
        assertNotNull(calls);
        String output = outputMySQL(calls,sql);
        assertEquals(" CALL p(?,?)", output);

        sql="call p(@var1,'@var2',var3)".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        calls =(DMLCallStatement) tuple._1().get(0);
        assertNotNull(calls);
        output = outputMySQL(calls,sql);
        assertEquals(" CALL p(@var1,'@var2',var3)", output);

        sql="call p()".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        calls =(DMLCallStatement) tuple._1().get(0);
        assertNotNull(calls);
        output = outputMySQL(calls,sql);
        assertEquals(" CALL p()", output);

        sql="call p(?)".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        calls =(DMLCallStatement) tuple._1().get(0);
        assertNotNull(calls);
        output = outputMySQL(calls,sql);
        assertEquals(" CALL p(?)", output);
    }
}
