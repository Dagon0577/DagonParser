import junit.framework.Assert;
import mysql.charset.MySqlCharset;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLDeleteStatement;
import parser.syntax.Parser;
import parser.util.Tuple2;

import java.util.List;

/**
 * @author huangganyan
 * @date 2020/7/17
 */
public class MySQLDMLDeleteParserTest extends AbstractSyntaxTest {

    public void testDelete() throws Exception {
        byte[] sql = "deLetE LOW_PRIORITY from id1.id , id using t1 a where col1 =? ".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        DMLDeleteStatement delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        String output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE LOW_PRIORITY id1.id,id FROM t1 AS a WHERE col1=?", output);

        sql = "deLetE LOW_PRIORITY QUICK IGNORE FROM t1 PARTITION (p1,p2); ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE LOW_PRIORITY QUICK IGNORE FROM t1 PARTITION(p1,p2)", output);

        sql = "deLetE from id1.id  using t1  ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE id1.id FROM t1", output);

        sql =
            "delete from offer.*,wp_image.* using offer a,wp_image b where a.member_id=b.member_id and a.member_id='abc' "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals(
            "DELETE offer.*,wp_image.* FROM offer AS a,wp_image AS b WHERE a.member_id=b.member_id AND a.member_id='abc'",
            output);

        sql = "deLetE from id1.id where col1='adf' limit 10".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE FROM id1.id WHERE col1='adf' LIMIT 10", output);

        sql = "deLetE from id where col1='adf' ordEr by d liMit 1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE FROM id WHERE col1='adf' ORDER BY d LIMIT 1", output);

        sql = "deLetE id.* from t1,t2 where col1='adf'            and col2=1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE id.* FROM t1,t2 WHERE col1='adf' AND col2=1", output);

        sql = "deLetE id,id.t from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE id,id.t FROM t1", output);

        sql = "deLetE from t1 where t1.id1='abc' order by a limit 5".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE FROM t1 WHERE t1.id1='abc' ORDER BY a LIMIT 5", output);

        sql = "deLetE from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE FROM t1", output);

        sql = "deLetE ignore tb1.*,id1.t from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE IGNORE tb1.*,id1.t FROM t1", output);

        sql = "deLetE quick tb1.*,id1.t from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert.assertEquals("DELETE QUICK tb1.*,id1.t FROM t1", output);

        sql = "DELETE FROM t1, t2 USING t1 INNER JOIN t2 INNER JOIN t3 WHERE t1.id=t2.id AND t2.id=t3.id;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        Assert.assertNotNull(tuple);
        Assert.assertNotNull(tuple._1());
        Assert.assertNull(tuple._2());
        Assert.assertEquals(tuple._1().size(), 1);
        delete = (DMLDeleteStatement)tuple._1().get(0);
        Assert.assertNotNull(delete);
        output = outputMySQL(delete, sql);
        Assert
            .assertEquals("DELETE t1,t2 FROM t1 INNER JOIN t2 INNER JOIN t3 WHERE t1.id=t2.id AND t2.id=t3.id", output);
    }
}
