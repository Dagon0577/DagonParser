package parser.syntax;

import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLLoadXMLStatement;
import mysql.charset.MySqlCharset;
import parser.util.Tuple2;
import java.util.List;
import junit.framework.Assert;

/**
 * @author Dagon0577
 * @date 2021年04月02日
 */
public class MySQLDMLLoadXMLParserTest extends AbstractSyntaxTest {
    public void testLoadXML() throws Exception {
        byte[] sql = "LOAD XML INFILE 'file_name' INTO TABLE test.tbl_name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DMLLoadXMLStatement loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        String output = outputMySQL(loadXML, sql);
        Assert.assertEquals("LOAD XML INFILE 'file_name' INTO TABLE test.tbl_name", output);

        sql = "LOAD XML LOW_PRIORITY INFILE 'file_name' INTO TABLE test.tbl_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals("LOAD XML LOW_PRIORITY INFILE 'file_name' INTO TABLE test.tbl_name",
                output);

        sql = "LOAD XML CONCURRENT INFILE 'file_name'  INTO TABLE test.tbl_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals("LOAD XML CONCURRENT INFILE 'file_name' INTO TABLE test.tbl_name",
                output);

        sql = "LOAD XML LOW_PRIORITY LOCAL INFILE 'file_name'  INTO TABLE test.tbl_name;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals(
                "LOAD XML LOW_PRIORITY LOCAL INFILE 'file_name' INTO TABLE test.tbl_name", output);

        sql = "LOAD XML INFILE 'file_name' IGNORE INTO TABLE test.tbl_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals("LOAD XML INFILE 'file_name' IGNORE INTO TABLE test.tbl_name", output);

        sql = "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name (@var1);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals("LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name(@var1)",
                output);

        sql = "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name (@var1,@VAR3);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals(
                "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name(@var1,@VAR3)",
                output);

        sql = "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name SET t1=t1+1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals(
                "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name SET t1=t1+1", output);

        sql = "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name SET t1=DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals(
                "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name SET t1=DEFAULT",
                output);

        sql = "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name SET t1=t1+1,t2=DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals(
                "LOAD XML INFILE 'file_name' REPLACE INTO TABLE test.tbl_name SET t1=t1+1,t2=DEFAULT",
                output);

        sql = "LOAD XML LOW_PRIORITY LOCAL INFILE 'file_name' IGNORE INTO TABLE test.tbl_name  CHARACTER SET 'test' ROWS IDENTIFIED BY '<tagname>' IGNORE 454 LINES (@var1) SET t1=t1+1,t2=DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        loadXML = (DMLLoadXMLStatement) tuple._1().get(0);
        output = outputMySQL(loadXML, sql);
        Assert.assertEquals(
                "LOAD XML LOW_PRIORITY LOCAL INFILE 'file_name' IGNORE INTO TABLE test.tbl_name CHARACTER SET 'test' ROWS IDENTIFIED BY '<tagname>' IGNORE 454 LINES(@var1) SET t1=t1+1,t2=DEFAULT",
                output);
    }
}
