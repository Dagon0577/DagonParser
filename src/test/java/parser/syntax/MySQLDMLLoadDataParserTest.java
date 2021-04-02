package parser.syntax;

import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLLoadDataInFileStatement;
import mysql.charset.MySqlCharset;
import parser.util.Tuple2;
import java.util.List;
import junit.framework.Assert;

/**
 * @author Dagon0577
 * @date 2021年04月02日
 */
public class MySQLDMLLoadDataParserTest extends AbstractSyntaxTest {
    public void testLoadDataInFile() throws Exception {
        byte[] sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DMLLoadDataInFileStatement load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        String output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' INTO TABLE tbl_name", output);

        sql = "LOAD DATA LOW_PRIORITY INFILE 'file_name' INTO TABLE tbl_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA LOW_PRIORITY INFILE 'file_name' INTO TABLE tbl_name",
                output);

        sql = "LOAD DATA CONCURRENT INFILE 'file_name' INTO TABLE tbl_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA CONCURRENT INFILE 'file_name' INTO TABLE tbl_name", output);

        sql = "LOAD DATA LOCAL INFILE 'file_name' INTO TABLE tbl_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA LOCAL INFILE 'file_name' INTO TABLE tbl_name", output);

        sql = "LOAD DATA INFILE 'file_name' REPLACE INTO TABLE tbl_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' REPLACE INTO TABLE tbl_name", output);

        sql = "LOAD DATA INFILE 'file_name' IGNORE INTO TABLE tbl_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' IGNORE INTO TABLE tbl_name", output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name PARTITION (p1);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' INTO TABLE tbl_name PARTITION(p1)",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name PARTITION (p1,p2,p3);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' INTO TABLE tbl_name PARTITION(p1,p2,p3)",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name CHARACTER SET 'Dagon';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name CHARACTER SET 'Dagon'", output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name FIELDS TERMINATED BY 'Dagon';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name FIELDS TERMINATED BY 'Dagon'",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name COLUMNS OPTIONALLY ENCLOSED BY 'Dagon';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name FIELDS ENCLOSED BY 'Dagon'",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name COLUMNS ENCLOSED BY 'Dagon';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name FIELDS ENCLOSED BY 'Dagon'",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name COLUMNS ESCAPED BY 'Dagon';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name FIELDS ESCAPED BY 'Dagon'",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name LINES TERMINATED BY 'Dagon';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name LINES TERMINATED BY 'Dagon'",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name LINES STARTING BY 'Dagon';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name LINES STARTING BY 'Dagon'",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name IGNORE 8 LINES;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' INTO TABLE tbl_name IGNORE 8 LINES",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name IGNORE 7 ROWS;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' INTO TABLE tbl_name IGNORE 7 LINES",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name (t1);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' INTO TABLE tbl_name (t1)", output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name (t1,@var1);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' INTO TABLE tbl_name (t1,@var1)", output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name SET column2 = DEFAULT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals("LOAD DATA INFILE 'file_name' INTO TABLE tbl_name SET column2=DEFAULT",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name SET column2 = @var1/100,column3 = CURRENT_TIMESTAMP();"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name SET column2=@var1/100,column3=CURRENT_TIMESTAMP()",
                output);

        sql = "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name COLUMNS ENCLOSED BY 'Dagon' IGNORE 7 ROWS (t1,@var1) SET column2 = @var1/100,column3 = CURRENT_TIMESTAMP();"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA INFILE 'file_name' INTO TABLE tbl_name FIELDS ENCLOSED BY 'Dagon' IGNORE 7 LINES (t1,@var1) SET column2=@var1/100,column3=CURRENT_TIMESTAMP()",
                output);

        sql = "LOAD DATA CONCURRENT INFILE 'file_name' IGNORE INTO TABLE tbl_name PARTITION (p1) CHARACTER SET 'Dagon' COLUMNS ESCAPED BY 'Dagon' LINES STARTING BY 'Dagon' IGNORE 7 ROWS (t1,@var1);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA CONCURRENT INFILE 'file_name' IGNORE INTO TABLE tbl_name PARTITION(p1) CHARACTER SET 'Dagon' FIELDS ESCAPED BY 'Dagon' LINES STARTING BY 'Dagon' IGNORE 7 LINES (t1,@var1)",
                output);

        sql = "LOAD DATA LOW_PRIORITY INFILE 'file_name' REPLACE INTO TABLE tbl_name PARTITION (p1,p2,p3) CHARACTER SET 'Dagon' FIELDS TERMINATED BY 'Dagon' LINES TERMINATED BY 'Dagon' IGNORE 8 LINES (t1) SET column2 = DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        load = (DMLLoadDataInFileStatement) tuple._1().get(0);
        output = outputMySQL(load, sql);
        Assert.assertEquals(
                "LOAD DATA LOW_PRIORITY INFILE 'file_name' REPLACE INTO TABLE tbl_name PARTITION(p1,p2,p3) CHARACTER SET 'Dagon' FIELDS TERMINATED BY 'Dagon' LINES TERMINATED BY 'Dagon' IGNORE 8 LINES (t1) SET column2=DEFAULT",
                output);
    }
}
