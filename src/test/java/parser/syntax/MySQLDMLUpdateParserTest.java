package parser.syntax;

import junit.framework.TestCase;
import mysql.charset.MySqlCharset;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLUpdateStatement;
import parser.util.Tuple2;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class MySQLDMLUpdateParserTest extends AbstractSyntaxTest {

    public void testUpdate() throws Exception {
        byte[] sql = "upDate LOw_PRIORITY IGNORE test.t1 sEt t1.col1=?, col2=DefaulT".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        TestCase.assertNotNull(tuple);
        TestCase.assertNotNull(tuple._1());
        TestCase.assertNull(tuple._2());
        TestCase.assertEquals(tuple._1().size(), 1);
        DMLUpdateStatement update = (DMLUpdateStatement)tuple._1().get(0);
        TestCase.assertNotNull(update);
        String output = outputMySQL(update, sql);
        TestCase.assertEquals("UPDATE LOW_PRIORITY IGNORE test.t1 SET t1.col1=?,col2=DEFAULT", output);

        sql = "upDate  IGNORE (t1) set col2=DefaulT order bY t1.col2 ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        TestCase.assertNotNull(tuple);
        TestCase.assertNotNull(tuple._1());
        TestCase.assertNull(tuple._2());
        TestCase.assertEquals(tuple._1().size(), 1);
        update = (DMLUpdateStatement)tuple._1().get(0);
        TestCase.assertNotNull(update);
        output = outputMySQL(update, sql);
        TestCase.assertEquals("UPDATE IGNORE t1 SET col2=DEFAULT ORDER BY t1.col2", output);

        sql = "upDate   (test.t1) SET col2=DefaulT order bY t1.col2 limit 10".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        TestCase.assertNotNull(tuple);
        TestCase.assertNotNull(tuple._1());
        TestCase.assertNull(tuple._2());
        TestCase.assertEquals(tuple._1().size(), 1);
        update = (DMLUpdateStatement)tuple._1().get(0);
        TestCase.assertNotNull(update);
        output = outputMySQL(update, sql);
        TestCase.assertEquals("UPDATE test.t1 SET col2=DEFAULT ORDER BY t1.col2 LIMIT 10", output);

        sql = "upDate LOW_PRIORITY  t1, test.t2 SET col2=DefaulT , col2='123''4'".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        TestCase.assertNotNull(tuple);
        TestCase.assertNotNull(tuple._1());
        TestCase.assertNull(tuple._2());
        TestCase.assertEquals(tuple._1().size(), 1);
        update = (DMLUpdateStatement)tuple._1().get(0);
        TestCase.assertNotNull(update);
        output = outputMySQL(update, sql);
        TestCase.assertEquals("UPDATE LOW_PRIORITY t1,test.t2 SET col2=DEFAULT,col2='123''4'", output);

        sql = "upDate LOW_PRIORITY  t1, test.t2 SET col2:=DefaulT , col2='123''4' where id='a'".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        TestCase.assertNotNull(tuple);
        TestCase.assertNotNull(tuple._1());
        TestCase.assertNull(tuple._2());
        TestCase.assertEquals(tuple._1().size(), 1);
        update = (DMLUpdateStatement)tuple._1().get(0);
        TestCase.assertNotNull(update);
        output = outputMySQL(update, sql);
        TestCase.assertEquals("UPDATE LOW_PRIORITY t1,test.t2 SET col2=DEFAULT,col2='123''4' WHERE id='a'", output);
    }
}
