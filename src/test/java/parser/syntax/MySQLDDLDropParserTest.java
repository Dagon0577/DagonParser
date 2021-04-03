package parser.syntax;

import junit.framework.TestCase;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dal.account.DALDropRoleStatement;
import parser.ast.stmt.dal.account.DALDropUserStatement;
import parser.ast.stmt.dal.resource.DALDropResourceGroupStatement;
import parser.ast.stmt.ddl.drop.DDLDropDatabaseStatement;
import parser.ast.stmt.ddl.drop.DDLDropEventStatement;
import parser.ast.stmt.ddl.drop.DDLDropFunctionStatement;
import parser.ast.stmt.ddl.drop.DDLDropIndexStatement;
import parser.ast.stmt.ddl.drop.DDLDropLogfileGroupStatement;
import parser.ast.stmt.ddl.drop.DDLDropProcedureStatement;
import parser.ast.stmt.ddl.drop.DDLDropServerStatement;
import parser.ast.stmt.ddl.drop.DDLDropSpatialReferenceSystemStatement;
import parser.ast.stmt.ddl.drop.DDLDropTableStatement;
import parser.ast.stmt.ddl.drop.DDLDropTablespaceStatement;
import parser.ast.stmt.ddl.drop.DDLDropTriggerStatement;
import parser.ast.stmt.ddl.drop.DDLDropViewStatement;
import mysql.charset.MySqlCharset;
import parser.util.Tuple2;
import java.util.List;

/**
 * @author Dagon0577
 * @date 2021年04月03日
 */
public class MySQLDDLDropParserTest extends AbstractSyntaxTest {
    public void testDropTable() throws Exception {
        byte[] sql = "drop temporary tabLe if exists tb1,tb2,tb3 restrict".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropTableStatement drop = (DDLDropTableStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP TEMPORARY TABLE IF EXISTS tb1,tb2,tb3 RESTRICT", output);

        sql = "drop temporary tabLe if exists tb1,tb2,tb3 cascade".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropTableStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP TEMPORARY TABLE IF EXISTS tb1,tb2,tb3 CASCADE", output);

        sql = "drop temporary tabLe if exists tb1 cascade".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropTableStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP TEMPORARY TABLE IF EXISTS tb1 CASCADE", output);

        sql = "drop tabLe if exists tb1 cascade".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropTableStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP TABLE IF EXISTS tb1 CASCADE", output);

        sql = "drop temporary tabLe tb1,tb2".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropTableStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP TEMPORARY TABLE tb1,tb2", output);
    }

    public void testDropTablespace() throws Exception {
        byte[] sql = "DROP UNDO TABLESPACE tablespace_name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropTablespaceStatement drop = (DDLDropTablespaceStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP UNDO TABLESPACE tablespace_name", output);

        sql = "DROP TABLESPACE tablespace_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP TABLESPACE tablespace_name", output);

        sql = "DROP TABLESPACE tablespace_name ENGINE=InnoDB;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP TABLESPACE tablespace_name ENGINE=InnoDB", output);

        sql = "DROP UNDO TABLESPACE tablespace_name ENGINE NDB;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP UNDO TABLESPACE tablespace_name ENGINE=NDB", output);
    }

    public void testDropIndex() throws Exception {
        byte[] sql = "drop index index_name on tb1".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropIndexStatement dropIndex = (DDLDropIndexStatement) tuple._1().get(0);
        String output = outputMySQL(dropIndex, sql);
        TestCase.assertEquals("DROP INDEX index_name ON tb1", output);

        sql = "drop index t1 on t1 algorithm = default lock =default;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        dropIndex = (DDLDropIndexStatement) tuple._1().get(0);
        output = outputMySQL(dropIndex, sql);
        TestCase.assertEquals("DROP INDEX t1 ON t1 ALGORITHM=DEFAULT LOCK=DEFAULT", output);

        sql = "drop index t1 on t1 algorithm = INPLACE lock =NONE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        dropIndex = (DDLDropIndexStatement) tuple._1().get(0);
        output = outputMySQL(dropIndex, sql);
        TestCase.assertEquals("DROP INDEX t1 ON t1 ALGORITHM=INPLACE LOCK=NONE", output);

        sql = "drop index t1 on t1 algorithm  COPY lock SHARED;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        dropIndex = (DDLDropIndexStatement) tuple._1().get(0);
        output = outputMySQL(dropIndex, sql);
        TestCase.assertEquals("DROP INDEX t1 ON t1 ALGORITHM=COPY LOCK=SHARED", output);

        sql = "drop index t1 on t1 algorithm = DEFAULT lock =EXCLUSIVE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        dropIndex = (DDLDropIndexStatement) tuple._1().get(0);
        output = outputMySQL(dropIndex, sql);
        TestCase.assertEquals("DROP INDEX t1 ON t1 ALGORITHM=DEFAULT LOCK=EXCLUSIVE", output);

        sql = "drop index t1 on t1 lock NONE algorithm = INPLACE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        dropIndex = (DDLDropIndexStatement) tuple._1().get(0);
        output = outputMySQL(dropIndex, sql);
        TestCase.assertEquals("DROP INDEX t1 ON t1 ALGORITHM=INPLACE LOCK=NONE", output);
    }

    public void testDropTrigger() throws Exception {
        byte[] sql = "drop trigger if exists testref3;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropTriggerStatement dropTrigger = (DDLDropTriggerStatement) tuple._1().get(0);
        String output = outputMySQL(dropTrigger, sql);
        TestCase.assertEquals("DROP TRIGGER IF EXISTS testref3", output);

        sql = "drop trigger if exists test1.testref4;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        dropTrigger = (DDLDropTriggerStatement) tuple._1().get(0);
        output = outputMySQL(dropTrigger, sql);
        TestCase.assertEquals("DROP TRIGGER IF EXISTS test1.testref4", output);

        sql = "drop trigger test1.testref4;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        dropTrigger = (DDLDropTriggerStatement) tuple._1().get(0);
        output = outputMySQL(dropTrigger, sql);
        TestCase.assertEquals("DROP TRIGGER test1.testref4", output);
    }

    public void testDropDatabase() throws Exception {
        byte[] sql = "DROP DATABASE IF EXISTS db_name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropDatabaseStatement drop = (DDLDropDatabaseStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP DATABASE IF EXISTS db_name", output);

        sql = "DROP SCHEMA db_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP DATABASE db_name", output);

        sql = "DROP DATABASE db_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP DATABASE db_name", output);

        sql = "DROP SCHEMA IF EXISTS db_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP DATABASE IF EXISTS db_name", output);
    }

    public void testDropEvent() throws Exception {
        byte[] sql = "DROP EVENT event_name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropEventStatement drop = (DDLDropEventStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP EVENT event_name", output);

        sql = "DROP EVENT IF EXISTS event_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropEventStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP EVENT IF EXISTS event_name", output);
    }

    public void testDropFunction() throws Exception {
        byte[] sql = "DROP FUNCTION IF EXISTS name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropFunctionStatement drop = (DDLDropFunctionStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP FUNCTION IF EXISTS name", output);

        sql = "DROP FUNCTION name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropFunctionStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP FUNCTION name", output);
    }

    public void testDropProcedure() throws Exception {
        byte[] sql = "DROP PROCEDURE IF EXISTS name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropProcedureStatement drop = (DDLDropProcedureStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP PROCEDURE IF EXISTS name", output);

        sql = "DROP PROCEDURE name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropProcedureStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP PROCEDURE name", output);
    }

    public void testDropLogfileGroup() throws Exception {
        byte[] sql = "DROP LOGFILE GROUP logfile_group ENGINE=NDB".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropLogfileGroupStatement drop = (DDLDropLogfileGroupStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP LOGFILE GROUP logfile_group ENGINE=NDB", output);
    }

    public void testDropResourceGroup() throws Exception {
        byte[] sql = "DROP RESOURCE GROUP rg1;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DALDropResourceGroupStatement drop = (DALDropResourceGroupStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP RESOURCE GROUP rg1", output);

        sql = "DROP RESOURCE GROUP rg2 FORCE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DALDropResourceGroupStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP RESOURCE GROUP rg2 FORCE", output);
    }

    public void testDropRole() throws Exception {
        byte[] sql = "DROP ROLE 'webapp'@'localhost';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DALDropRoleStatement drop = (DALDropRoleStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP ROLE 'webapp'@'localhost'", output);

        sql = "DROP ROLE IF EXISTS 'administrator', 'developer';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DALDropRoleStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP ROLE IF EXISTS 'administrator','developer'", output);
    }

    public void testDropServer() throws Exception {
        byte[] sql = "DROP SERVER IF EXISTS server_name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropServerStatement drop = (DDLDropServerStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP SERVER IF EXISTS server_name", output);

        sql = "DROP SERVER server_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropServerStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP SERVER server_name", output);
    }

    public void testDropSpatialReferenceSystem() throws Exception {
        byte[] sql = "DROP SPATIAL REFERENCE SYSTEM 4120;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropSpatialReferenceSystemStatement drop =
                (DDLDropSpatialReferenceSystemStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP SPATIAL REFERENCE SYSTEM 4120", output);

        sql = "DROP SPATIAL REFERENCE SYSTEM IF EXISTS 4120;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropSpatialReferenceSystemStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP SPATIAL REFERENCE SYSTEM IF EXISTS 4120", output);
    }

    public void testDropUser() throws Exception {
        byte[] sql = "DROP USER 'webapp'@'localhost';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DALDropUserStatement drop = (DALDropUserStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP USER 'webapp'@'localhost'", output);

        sql = "DROP USER IF EXISTS 'administrator', 'developer';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DALDropUserStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP USER IF EXISTS 'administrator','developer'", output);
    }

    public void testDropView() throws Exception {
        byte[] sql = "DROP VIEW v1;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLDropViewStatement drop = (DDLDropViewStatement) tuple._1().get(0);
        String output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP VIEW v1", output);

        sql = "DROP VIEW IF EXISTS v1,v2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropViewStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP VIEW IF EXISTS v1,v2", output);

        sql = "DROP VIEW IF EXISTS v1,v2 RESTRICT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropViewStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP VIEW IF EXISTS v1,v2 RESTRICT", output);

        sql = "DROP VIEW IF EXISTS v1,v2 CASCADE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        drop = (DDLDropViewStatement) tuple._1().get(0);
        output = outputMySQL(drop, sql);
        TestCase.assertEquals("DROP VIEW IF EXISTS v1,v2 CASCADE", output);
    }
}
