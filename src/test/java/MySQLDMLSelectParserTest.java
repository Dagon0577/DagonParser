import mysql.charset.MySqlCharset;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLSelectStatement;
import parser.ast.stmt.dml.DMLSelectUnionStatement;
import parser.syntax.Parser;
import parser.util.Tuple2;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class MySQLDMLSelectParserTest extends AbstractSyntaxTest {

    public void testSelectUnion() throws Exception {
        byte[] sql =
            "(select id from t1) union all (select id from t2) union all (select id from t3) ordeR By d desC limit 1 offset 1"
                .getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        DMLSelectUnionStatement select = (DMLSelectUnionStatement)tuple._1().get(0);
        String output = outputMySQL(select, sql);
        assertEquals(
            "(SELECT id FROM t1) UNION ALL (SELECT id FROM t2) UNION ALL (SELECT id FROM t3) ORDER BY d DESC LIMIT 1,1",
            output);

        sql = "(select id from t1) union  (select id from t2 order by id) union aLl (select id from t3) ordeR By d asC"
            .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectUnionStatement)tuple._1().get(0);
        output = outputMySQL(select, sql);
        assertEquals(
            "(SELECT id FROM t1) UNION (SELECT id FROM t2 ORDER BY id) UNION ALL (SELECT id FROM t3) ORDER BY d",
            output);

        sql = "(select id from t1) union distInct (select id from t2) union  (select id from t3)".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectUnionStatement)tuple._1().get(0);
        output = outputMySQL(select, sql);
        assertEquals("(SELECT id FROM t1) UNION (SELECT id FROM t2) UNION (SELECT id FROM t3)", output);
    }

    public void testSelect() throws Exception {
        byte[] sql = "SELect t1.id , t2.* from t1, test.t2 where test.t1.id=1 and t1.id=test.t2.id".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        DMLSelectStatement select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        String output = outputMySQL(select, sql);
        assertEquals("SELECT t1.id,t2.* FROM t1,test.t2 WHERE test.t1.id=1 AND t1.id=test.t2.id", output);

        sql =
            "select * from  offer a  straight_join wp_image b use key for join(t1,t2) on a.member_id=b.member_id inner join product_visit c where a.member_id=c.member_id and c.member_id='abc' "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals(
            "SELECT * FROM offer AS a STRAIGHT_JOIN wp_image AS b USE KEY FOR JOIN(t1,t2) ON a.member_id=b.member_id INNER JOIN product_visit AS c WHERE a.member_id=c.member_id AND c.member_id='abc'",
            output);

        sql = "SELect all tb1.id,tb2.id from tb1,tb2 where tb1.id2=tb2.id2".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT tb1.id,tb2.id FROM tb1,tb2 WHERE tb1.id2=tb2.id2", output);

        sql = "SELect distinct high_priority tb1.id,tb2.id from tb1,tb2 where tb1.id2=tb2.id2".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT DISTINCT HIGH_PRIORITY tb1.id,tb2.id FROM tb1,tb2 WHERE tb1.id2=tb2.id2", output);

        sql =
            ("SELect distinctrow high_priority sql_small_result tb1.id,tb2.id " + "from tb1,tb2 where tb1.id2=tb2.id2")
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals(
            "SELECT DISTINCTROW HIGH_PRIORITY SQL_SMALL_RESULT tb1.id,tb2.id" + " FROM tb1,tb2 WHERE tb1.id2=tb2.id2",
            output);

        sql = "SELect  sql_cache id1,id2 from tb1,tb2 where tb1.id1=tb2.id1 ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT SQL_CACHE id1,id2 FROM tb1,tb2 WHERE tb1.id1=tb2.id1", output);

        sql = "SELect  sql_cache id1,max(id2) from tb1 group by id1 having id1>10 order by id3 desc".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT SQL_CACHE id1,max(id2) FROM tb1 GROUP BY id1 HAVING id1>10 ORDER BY id3 DESC", output);

        sql = "SELect  SQL_BUFFER_RESULT tb1.id1,id2 from tb1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT SQL_BUFFER_RESULT tb1.id1,id2 FROM tb1", output);

        sql = "SELect  SQL_no_cache tb1.id1,id2 from tb1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT SQL_NO_CACHE tb1.id1,id2 FROM tb1", output);

        sql = "SELect  SQL_CALC_FOUND_ROWS tb1.id1,id2 from tb1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT SQL_CALC_FOUND_ROWS tb1.id1,id2 FROM tb1", output);

        sql = "SELect 1+1 ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT 1+1", output);

        sql = "SELect t1.* from tb  ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT t1.* FROM tb", output);

        sql = ("SELect distinct high_priority straight_join sql_big_result sql_cache tb1.id,tb2.id "
            + "from tb1,tb2 where tb1.id2=tb2.id2").getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals(
            "SELECT DISTINCT HIGH_PRIORITY STRAIGHT_JOIN SQL_BIG_RESULT SQL_CACHE tb1.id,tb2.id FROM tb1,tb2 WHERE tb1.id2=tb2.id2",
            output);

        sql = "SELect distinct id1,id2 from tb1,tb2 where tb1.id1=tb2.id2 for update".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT DISTINCT id1,id2 FROM tb1,tb2 WHERE tb1.id1=tb2.id2 FOR UPDATE", output);

        sql = "SELect distinct t1.a,t2.b from t1,t2 where t1.a=t2.b for share;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT DISTINCT t1.a,t2.b FROM t1,t2 WHERE t1.a=t2.b FOR SHARE", output);

        sql = "SELECT * FROM t FOR UPDATE SKIP LOCKED;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT * FROM t FOR UPDATE SKIP LOCKED", output);

        sql = "SELECT * FROM t FOR UPDATE OF tbl1,tbl2 SKIP LOCKED;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT * FROM t FOR UPDATE OF tbl1,tbl2 SKIP LOCKED", output);

        sql = "SELECT * FROM t FOR UPDATE NOWAIT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT * FROM t FOR UPDATE NOWAIT", output);

        sql = "SELect distinct id1,id2 from tb1,tb2 where tb1.id1=tb2.id2 lock in share mode".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT DISTINCT id1,id2 FROM tb1,tb2 WHERE tb1.id1=tb2.id2 LOCK IN SHARE MODE", output);

        sql = "SelecT * FrOM employees PARTITION (p1);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT * FROM employees PARTITION(p1)", output);

        sql = "SELECT * FROM employees PARTITION (p0, p2) WHERE lname LIKE 'S%';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT * FROM employees PARTITION(p0,p2) WHERE lname LIKE 'S%'", output);

        sql = "SELECT id, CONCAT(fname, ' ', lname) AS name FROM employees PARTITION (p0) ORDER BY lname;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT id,CONCAT(fname,' ',lname) AS name FROM employees PARTITION(p0) ORDER BY lname", output);

        sql =
            "SELECT store_id, COUNT(department_id) AS c FROM employees PARTITION (p1sp2sp3) GROUP BY store_id WITH ROLLUP HAVING c > 4;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals(
            "SELECT store_id,COUNT(department_id) AS c FROM employees PARTITION(p1sp2sp3) GROUP BY store_id WITH ROLLUP HAVING c>4",
            output);

        sql =
            "SELECT e.id AS 'Employee ID', CONCAT(e.fname, ' ', e.lname) AS Name, s.city AS City, d.name AS department FROM employees AS e JOIN stores PARTITION (p1) AS s ON e.store_id=s.id JOIN departments PARTITION (p0) AS d ON e.department_id=d.id ORDER BY e.lname;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals(
            "SELECT e.id AS 'Employee ID',CONCAT(e.fname,' ',e.lname) AS Name,s.city AS City,d.name AS department FROM employees AS e INNER JOIN stores PARTITION(p1) AS s ON e.store_id=s.id INNER JOIN departments PARTITION(p0) AS d ON e.department_id=d.id ORDER BY e.lname",
            output);

        sql =
            "SELECT t1.a,ROW_NUMBER() OVER (ORDER BY t1.a) AS 'row_number',RANK() OVER (ORDER BY t1.a) AS 'rank',DENSE_RANK() OVER (ORDER BY t1.a) AS 'dense_rank' FROM t1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals(
            "SELECT t1.a,ROW_NUMBER() OVER (ORDER BY t1.a) AS 'row_number',RANK() OVER (ORDER BY t1.a) AS 'rank',DENSE_RANK() OVER (ORDER BY t1.a) AS 'dense_rank' FROM t1",
            output);

        sql =
            "SELECT val,ROW_NUMBER() OVER w AS 'row_number',RANK() OVER w AS 'rank',DENSE_RANK() OVER w AS 'dense_rank' FROM numbers WINDOW w AS (ORDER BY val);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals(
            "SELECT val,ROW_NUMBER() OVER w AS 'row_number',RANK() OVER w AS 'rank',DENSE_RANK() OVER w AS 'dense_rank' FROM numbers WINDOW w AS (ORDER BY val)",
            output);

        sql = "SELECT id, data INTO @x,@y FROM test.t1 LIMIT 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT id,data INTO @x,@y FROM test.t1 LIMIT 1", output);

        sql =
            "SELECT a,b,a+b INTO OUTFILE '/tmp/result.txt' FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\n' FROM test_table;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals(
            "SELECT a,b,a+b INTO OUTFILE '/tmp/result.txt' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n'  FROM test_table",
            output);

        sql = "SELECT id, data INTO DUMPFILE '/tmp/result.txt' FROM test.t1 LIMIT 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        output = outputMySQL(select, sql);
        assertEquals("SELECT id,data INTO DUMPFILE '/tmp/result.txt'  FROM test.t1 LIMIT 1", output);
    }

    public void testSelectChinese() throws Exception {
        byte[] sql = "SELect t1.id , t2.* from t1, test.t2 where test.t1.id='中''‘文' and t1.id=test.t2.id".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        assertNotNull(tuple);
        assertNotNull(tuple._1());
        assertNull(tuple._2());
        assertEquals(tuple._1().size(), 1);
        DMLSelectStatement select = (DMLSelectStatement)tuple._1().get(0);
        assertNotNull(select);
        String output = outputMySQL(select, sql);
        assertEquals("SELECT t1.id,t2.* FROM t1,test.t2 WHERE test.t1.id='中''‘文' AND t1.id=test.t2.id", output);
    }
}
