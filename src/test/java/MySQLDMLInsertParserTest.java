import mysql.charset.MySqlCharset;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLInsertReplaceStatement;
import parser.syntax.Parser;
import parser.util.Tuple2;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class MySQLDMLInsertParserTest extends AbstractSyntaxTest {

    public void testInsert() throws Exception {
        byte[] sql = "insErt HIGH_PRIORITY intO test.t1 seT t1.id1=?, id2 := '123'".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        DMLInsertReplaceStatement insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull((insert));
        String output = outputMySQL(insert, sql);
        assertEquals("INSERT HIGH_PRIORITY INTO test.t1 (t1.id1,id2)VALUES (?,'123')", output);

        sql = "insErt  IGNORE test.t1 seT t1.id1:=? oN dupLicatE key UPDATE ex.col1=?, col2=12".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT IGNORE INTO test.t1 (t1.id1)VALUES (?) ON DUPLICATE KEY UPDATE ex.col1=?, col2=12",
            output);

        sql = "insErt t1 value (123,?) oN dupLicatE key UPDATE ex.col1=?".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT INTO t1 VALUES (123,?) ON DUPLICATE KEY UPDATE ex.col1=?", output);

        sql = "insErt LOW_PRIORITY t1 valueS (12e-2,1,2), (?),(default)".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT LOW_PRIORITY INTO t1 VALUES (0.12,1,2),(?),(DEFAULT)", output);

        sql = "insErt LOW_PRIORITY t1 select id from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT LOW_PRIORITY INTO t1 SELECT id FROM t1", output);

        sql = "insErt delayed t1 select id from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT DELAYED INTO t1 SELECT id FROM t1", output);

        sql = "insErt LOW_PRIORITY t1 (select id from t1) oN dupLicatE key UPDATE ex.col1=?, col2=12".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT LOW_PRIORITY INTO t1 SELECT id FROM t1 ON DUPLICATE KEY UPDATE ex.col1=?, col2=12",
            output);

        sql = "insErt LOW_PRIORITY t1 (t1.col1) valueS (123),('12''34')".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT LOW_PRIORITY INTO t1 (t1.col1)VALUES (123),('12''34')", output);

        sql =
            "insErt LOW_PRIORITY t1 (col1, t1.col2) VALUE (123,'123\\'4') oN dupLicatE key UPDATE ex.col1=?".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals(
            "INSERT LOW_PRIORITY INTO t1 (col1,t1.col2)VALUES (123,'123\\'4') ON DUPLICATE KEY UPDATE ex.col1=?",
            output);

        sql = "insErt LOW_PRIORITY t1 (col1, t1.col2) select id from t3 oN dupLicatE key UPDATE ex.col1=?".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT LOW_PRIORITY INTO t1 (col1,t1.col2)SELECT id FROM t3 ON DUPLICATE KEY UPDATE ex.col1=?",
            output);

        sql = "insErt LOW_PRIORITY IGNORE intO t1 (col1) ( select id from t3) ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        insert = (DMLInsertReplaceStatement)tuple._1().get(0);
        assertNotNull(insert);
        output = outputMySQL(insert, sql);
        assertEquals("INSERT LOW_PRIORITY IGNORE INTO t1 (col1)(SELECT id FROM t3)", output);
    }
}
