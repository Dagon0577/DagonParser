package parser.syntax;

import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLInsertReplaceStatement;
import mysql.charset.MySqlCharset;
import parser.util.Tuple2;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2021/3/30
 */
public class MySQLDMLReplaceParserTest extends AbstractSyntaxTest {

    public void testReplace() throws Exception {
        byte[] sql = "ReplaCe LOW_PRIORITY intO test.t1 seT t1.id1:=?, id2='123'".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        DMLInsertReplaceStatement replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull((replace));
        String output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO test.t1 (t1.id1,id2)VALUES (?,'123')",
                output);

        sql = "ReplaCe   test.t1 seT t1.id1:=? ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE INTO test.t1 (t1.id1)VALUES (?)", output);

        sql = "ReplaCe t1 value (123,?) ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE INTO t1 VALUES (123,?)", output);

        sql = "ReplaCe LOW_PRIORITY t1 valueS (12e-2), (?)".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO t1 VALUES (0.12),(?)", output);

        sql = "ReplaCe LOW_PRIORITY t1 select id from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO t1 SELECT id FROM t1", output);

        sql = "ReplaCe delayed t1 select id from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE DELAYED INTO t1 SELECT id FROM t1", output);

        sql = "ReplaCe LOW_PRIORITY t1 (select id from t1) ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO t1 SELECT id FROM t1", output);

        sql = "ReplaCe LOW_PRIORITY t1 (select id from t1) ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO t1 SELECT id FROM t1", output);

        sql = "ReplaCe LOW_PRIORITY t1 (t1.col1) valueS (123),('12''34')".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO t1 (t1.col1)VALUES (123),('12''34')", output);

        sql = "ReplaCe LOW_PRIORITY t1 (col1, t1.col2) VALUE (123,'123\\'4') ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO t1 (col1,t1.col2)VALUES (123,'123\\'4')",
                output);

        sql = "REPLACE LOW_PRIORITY t1 (col1, t1.col2) select id from t3 ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO t1 (col1,t1.col2)SELECT id FROM t3",
                output);

        sql = "replace LOW_PRIORITY  intO t1 (col1) ( select id from t3) ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        replace = (DMLInsertReplaceStatement) tuple._1().get(0);
        assertNotNull(replace);
        output = outputMySQL(replace, sql);
        assertEquals("REPLACE LOW_PRIORITY INTO t1 (col1)(SELECT id FROM t3)", output);
    }
}
