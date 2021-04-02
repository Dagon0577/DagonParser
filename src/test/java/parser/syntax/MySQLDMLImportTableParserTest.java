package parser.syntax;

import junit.framework.TestCase;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLImportTableStatement;
import mysql.charset.MySqlCharset;
import parser.util.Tuple2;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2021年04月02日
 */
public class MySQLDMLImportTableParserTest extends AbstractSyntaxTest {
    public void testImportTable() throws Exception {
        byte[] sql =
                "IMPORT TABLE FROM '/tmp/mysql-files/employees.sdi','/tmp/mysql-files/managers.sdi';"
                        .getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DMLImportTableStatement importTable = (DMLImportTableStatement) tuple._1().get(0);
        String output = outputMySQL(importTable, sql);
        TestCase.assertEquals(
                "IMPORT TABLE FROM '/tmp/mysql-files/employees.sdi','/tmp/mysql-files/managers.sdi'",
                output);

        sql = "IMPORT TABLE FROM '/tmp/mysql-files/employees.sdi';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        importTable = (DMLImportTableStatement) tuple._1().get(0);
        output = outputMySQL(importTable, sql);
        TestCase.assertEquals("IMPORT TABLE FROM '/tmp/mysql-files/employees.sdi'", output);
    }
}
