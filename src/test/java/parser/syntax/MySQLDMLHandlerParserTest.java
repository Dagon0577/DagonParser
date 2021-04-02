package parser.syntax;

import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLHandlerStatement;
import mysql.charset.MySqlCharset;
import parser.util.Tuple2;
import java.util.List;
import junit.framework.Assert;

/**
 * @author Dagon0577
 * @date 2021年04月02日
 */
public class MySQLDMLHandlerParserTest extends AbstractSyntaxTest {
    public void testHandler() throws Exception {
        byte[] sql = "HANDLER tbl_name OPEN;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DMLHandlerStatement handler = (DMLHandlerStatement) tuple._1().get(0);
        String output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name OPEN", output);

        sql = "HANDLER tbl_name OPEN AS handler1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name OPEN AS handler1", output);

        sql = "HANDLER tbl_name READ index_name  = (t0,t1) where id = 1; ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name=(t0,t1) WHERE id=1", output);

        sql = "HANDLER tbl_name READ index_name  <= (t1) where id = 1 LIMIT 10;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name<=(t1) WHERE id=1 LIMIT 10", output);

        sql = "HANDLER tbl_name READ index_name  >= (t1) where id = 1 LIMIT 10;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name>=(t1) WHERE id=1 LIMIT 10", output);

        sql = "HANDLER tbl_name READ index_name  < (t0,t1) where id = 1 LIMIT 10;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name<(t0,t1) WHERE id=1 LIMIT 10", output);

        sql = "HANDLER tbl_name READ index_name  > (t0,t1) WHERE id=1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name>(t0,t1) WHERE id=1", output);

        sql = "HANDLER tbl_name READ index_name FIRST;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name FIRST", output);

        sql = "HANDLER tbl_name READ index_name NEXT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name NEXT", output);

        sql = "HANDLER tbl_name READ index_name PREV;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name PREV", output);

        sql = "HANDLER tbl_name READ index_name LAST WHERE id=1 LIMIT 10;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ index_name LAST WHERE id=1 LIMIT 10", output);

        sql = "HANDLER tbl_name READ FIRST;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ FIRST", output);

        sql = "HANDLER tbl_name READ NEXT WHERE id=1 LIMIT 9;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name READ NEXT WHERE id=1 LIMIT 9", output);

        sql = "HANDLER tbl_name CLOSE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        handler = (DMLHandlerStatement) tuple._1().get(0);
        output = outputMySQL(handler, sql);
        Assert.assertEquals("HANDLER tbl_name CLOSE", output);
    }
}
