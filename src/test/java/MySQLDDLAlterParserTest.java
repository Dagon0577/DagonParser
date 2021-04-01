import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dal.account.DALAlterUserStatement;
import parser.ast.stmt.dal.resource.DALAlterResourceGroupStatement;
import parser.ast.stmt.ddl.alter.DDLAlterDatabaseStatement;
import parser.ast.stmt.ddl.alter.DDLAlterEventStatement;
import parser.ast.stmt.ddl.alter.DDLAlterFunctionStatement;
import parser.ast.stmt.ddl.alter.DDLAlterInstanceStatement;
import parser.ast.stmt.ddl.alter.DDLAlterLogfileGroupStatement;
import parser.ast.stmt.ddl.alter.DDLAlterProcedureStatement;
import parser.ast.stmt.ddl.alter.DDLAlterServerStatement;
import parser.ast.stmt.ddl.alter.DDLAlterTableStatement;
import parser.ast.stmt.ddl.alter.DDLAlterTablespaceStatement;
import parser.ast.stmt.ddl.alter.DDLAlterViewStatement;
import parser.lexer.Lexer;
import mysql.charset.MySqlCharset;
import parser.syntax.Parser;
import parser.util.Tuple2;
import java.util.List;
import junit.framework.Assert;

/**
 * @author Dagon0577
 * @date 2021年04月01日
 */
public class MySQLDDLAlterParserTest extends AbstractSyntaxTest {
    public void testAlterTable() throws Exception {
        byte[] sql = "alTeR table tb_name".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterTableStatement alter = (DDLAlterTableStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE tb_name", output);

        sql = "alter table taaaaa add a int".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa ADD COLUMN a int DEFAULT NULL", output);

        sql = "alter table taaaaa add a int after id ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa ADD COLUMN a int DEFAULT NULL AFTER id", output);

        sql = "alter table taaaaa add a int first ".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa ADD COLUMN a int DEFAULT NULL FIRST ", output);

        sql = "alter table taaaaa add aaaaa int ,add aaabbbb int;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE taaaaa ADD COLUMN aaaaa int DEFAULT NULL,ADD COLUMN aaabbbb int DEFAULT NULL",
                output);

        sql = "alter table taaaaa add aaaaa int first,add aaabbbb int after aaaaa;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE taaaaa ADD COLUMN aaaaa int DEFAULT NULL FIRST ,ADD COLUMN aaabbbb int DEFAULT NULL AFTER aaaaa",
                output);

        sql = "alter table taaaaa add index testIndex(aaaaa);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa ADD INDEX testIndex(aaaaa)", output);

        sql = "alter table taaaaa add index index3 using btree (key1,key2(5)) key_block_size = 2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE taaaaa ADD INDEX index3 USING BTREE (key1,key2(5)) KEY_BLOCK_SIZE=2",
                output);

        sql = "alter table taaaaa add Key index4 using hash (key1,key2(5)) using btree;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE taaaaa ADD KEY index4 USING HASH (key1,key2(5)) USING BTREE", output);

        sql = "alter table taaaaa add fulltext index index5 (key1,key2) with parser ngram;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE taaaaa ADD FULLTEXT KEY index5(key1,key2) WITH PARSER ngram", output);

        sql = "alter table taaaaa add index index9996(key1) comment 'indexAaA1~!@#$%^&*()_+=-[]\\';/.,<>?:\"{}|/*.';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE taaaaa ADD INDEX index9996(key1) COMMENT 'indexAaA1~!@#$%^&*()_+=-[]\\';/.,<>?:\"{}|/*.'",
                output);

        sql = "alter table taaaaa add key index7 using btree(key1) invisible;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa ADD KEY index7 USING BTREE (key1) INVISIBLE",
                output);

        sql = "alter table taaaaa add spatial key index8 (aaaaa) comment 'test';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa ADD SPATIAL KEY index8(aaaaa) COMMENT 'test'",
                output);

        sql = "alter table test2 add CONSTRAINT testKey PriMary KEY (id) using btree visible;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE test2 ADD CONSTRAINT testKeyPRIMARY KEY (id) USING BTREE VISIBLE",
                output);

        sql = "alter table test2 add CONSTRAINT testKey2 UNIQUE KEY  indexKey using btree (a) COMMENT 'string';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE test2 ADD CONSTRAINT testKey2UNIQUE KEY indexKey USING BTREE (a) COMMENT 'string'",
                output);

        sql = "alter table test2 add CONSTRAINT testKey3 FOREIGN KEY(id) REFERENCES test(id);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE test2 ADD CONSTRAINT testKey3FOREIGN KEY (id) REFERENCES test(id)",
                output);

        sql = "alter table test2 add constraint testKey4 CHECK(a<100) not ENFORCED;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ADD CONSTRAINT testKey4 CHECK(a<100) NOT ENFORCED",
                output);

        sql = "alter table test2 add constraint testKey4 CHECK(a<100) not ENFORCED,add constraint testKey5 CHECK(a<200) not ENFORCED;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE test2 ADD CONSTRAINT testKey4 CHECK(a<100) NOT ENFORCED,ADD CONSTRAINT testKey5 CHECK(a<200) NOT ENFORCED",
                output);

        sql = "alter table test2 drop check testKey4;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 DROP CHECK testKey4", output);

        sql = "alter table test2 alter check testKey4 not ENFORCED;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ALTER CHECK testKey4NOT ENFORCED", output);

        sql = "alter tablE test2 ALGORITHM = default".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ALGORITHM=DEFAULT", output);

        sql = "alter tablE test2 ALGORITHM = instant".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ALGORITHM=INSTANT", output);

        sql = "alter tablE test2 ALGORITHM = inplace".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ALGORITHM=INPLACE", output);

        sql = "alter tablE test2 ALGORITHM = COPY".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ALGORITHM=COPY", output);

        sql = "alter tablE test2 ALGORITHM = COPY".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ALGORITHM=COPY", output);

        sql = "alter table test2 alter column a set default '2'".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ALTER COLUMN a SET DEFAULT '2'", output);

        sql = "alter table test2 alter column a drop default;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ALTER COLUMN a DROP DEFAULT", output);

        sql = "alter table taaaaa alter index index7 visible;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa ALTER INDEX index7 VISIBLE", output);

        sql = "alter table taaaaa alter index index7 invisible;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa ALTER INDEX index7 INVISIBLE", output);

        sql = "alter table taaaaa add key1 char(10) first,add key2 char(20) after key1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE taaaaa ADD COLUMN key1 char(10) DEFAULT NULL FIRST ,ADD COLUMN key2 char(20) DEFAULT NULL AFTER key1",
                output);

        sql = "alter table test2 change column c aa int(10) first;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 CHANGE COLUMN c aa int(10) DEFAULT NULL FIRST ",
                output);

        sql = "alter table test2 change column c aa VARCHAR(45) first;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 CHANGE COLUMN c aa varchar(45) DEFAULT NULL FIRST ",
                output);

        sql = "alter table test2 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE test2 DEFAULT CHARACTER SET=latin1 DEFAULT COLLATE=latin1_swedish_ci ",
                output);

        sql = "alter table test2 CHARACTER SET latin1 COLLATE latin1_swedish_ci;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE test2 DEFAULT CHARACTER SET=latin1 DEFAULT COLLATE=latin1_swedish_ci ",
                output);

        sql = "alter table test2 CONVERT TO CHARACTER SET latin1 COLLATE latin1_swedish_ci;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE test2 CONVERT TO CHARACTER SET latin1 COLLATE latin1_swedish_ci",
                output);

        sql = "alter table test2 enable keys;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 ENABLE KEYS", output);

        sql = "alter table test2 discard tablespace;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 DISCARD TABLESPACE", output);

        sql = "alter table test2 imporT tablespace;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE test2 IMPORT TABLESPACE", output);

        sql = "alter table table1 drop column c;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE table1 DROP COLUMN c", output);

        sql = "alter table table1 drop c;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE table1 DROP COLUMN c", output);

        sql = "alter table taaaaa drop key index7;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa DROP INDEX index7", output);

        sql = "alter table taaaaa drop index index7;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa DROP INDEX index7", output);

        sql = "alter table taaaaa drop foreign key testKey3;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa DROP FOREIGN KEY testKey3", output);

        sql = "alter table taaaaa drop primary key;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa DROP PRIMARY KEY", output);

        sql = "alter table taaaaa force;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE taaaaa FORCE", output);

        sql = "alter table t3 lock = default;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 LOCK=DEFAULT", output);

        sql = "alter table t3 lock = SHARED;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 LOCK=SHARED", output);

        sql = "alter table t3 lock = EXCLUSIVE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 LOCK=EXCLUSIVE", output);

        sql = "alter table t3 lock = NONE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 LOCK=NONE", output);

        sql = "alter table t3 modify column a char(10) first;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 MODIFY COLUMN a char(10) DEFAULT NULL FIRST ", output);

        sql = "alter table t3 order by a,id,b,c,d;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 ORDER BY a,id,b,c,d", output);

        sql = "alter table t3 order by a;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 ORDER BY a", output);

        sql = "alter table t3 RENAME column a to aa;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 RENAME COLUMN a TO aa", output);

        sql = "alter table t3 rename index index6 to index777;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 RENAME INDEX index6 TO index777", output);

        sql = "alter table t3 rename to ta;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 RENAME TO ta", output);

        sql = "alter table t3 with VALIDATION;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 WITH VALIDATION", output);

        sql = "alter table t3 without VALIDATION;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t3 WITHOUT VALIDATION", output);

        sql = "alter table t99 add partition ( PARTITION p3 VALUES LESS THAN MAXVALUE);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION p3 VALUES LESS THAN MAXVALUE)", output);

        sql = "alter table t99 add partition ( PARTITION p3 VALUES LESS THAN (2000));".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ADD PARTITION( PARTITION p3 VALUES LESS THAN (2000))",
                output);

        sql = "alter table t99 add partition  (PARTITION P2 VALUES LESS THAN (3000,3000) MAX_ROWS = 4000);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION P2 VALUES LESS THAN (3000,3000) MAX_ROWS=4000)",
                output);

        sql = "alter table t99 add partition  (PARTITION P2 VALUES LESS THAN (3000,3000) MAX_ROWS = 4000 MIN_ROWS = 1000);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION P2 VALUES LESS THAN (3000,3000) MAX_ROWS=4000 MIN_ROWS=1000)",
                output);

        sql = "alter table t99 add partition  (PARTITION P2 VALUES LESS THAN (3000,3000) INDEX DIRECTORY = '/var/appdata/97/idx' );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION P2 VALUES LESS THAN (3000,3000) INDEX DIRECTORY='/var/appdata/97/idx')",
                output);

        sql = "alter table t99 add partition  (PARTITION p1 VALUES LESS THAN (2000,2000)STORAGE ENGINE = innodb comment 'aaa');"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION p1 VALUES LESS THAN (2000,2000) STORAGE ENGINE=innodb COMMENT='aaa')",
                output);

        sql = "alter table t99 add partition  (PARTITION p1 VALUES LESS THAN (2000,2000) ENGINE = innodb comment 'aaa');"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION p1 VALUES LESS THAN (2000,2000) STORAGE ENGINE=innodb COMMENT='aaa')",
                output);

        sql = "alter table t99 add partition  (PARTITION P2 VALUES LESS THAN (3000,3000) TABLESPACE ts1 );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION P2 VALUES LESS THAN (3000,3000) TABLESPACE=ts1)",
                output);

        sql = "alter table t99 add partition  (PARTITION P2 TABLESPACE ts1 );".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ADD PARTITION( PARTITION P2 TABLESPACE=ts1)", output);

        sql = "alter table t99 add partition  (PARTITION p1 VALUES LESS THAN (2000,2000)STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 4000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION p1 VALUES LESS THAN (2000,2000) STORAGE ENGINE=innodb COMMENT='aaa' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=4000 TABLESPACE=ts1)",
                output);

        sql = "alter table t99 add partition  (PARTITION P2 TABLESPACE ts1 (subpartition p2));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION P2 TABLESPACE=ts1( SUBPARTITION p2))",
                output);

        sql = "alter table t99 add partition  (PARTITION p1 VALUES LESS THAN (2000,2000)STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 4000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1 (SUBPARTITION p2 STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 4000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1 ,SUBPARTITION p3 STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 4000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 ADD PARTITION( PARTITION p1 VALUES LESS THAN (2000,2000) STORAGE ENGINE=innodb COMMENT='aaa' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=4000 TABLESPACE=ts1( SUBPARTITION p2 STORAGE ENGINE=innodb COMMENT='aaa' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=4000 TABLESPACE=ts1, SUBPARTITION p3 STORAGE ENGINE=innodb COMMENT='aaa' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=4000 TABLESPACE=ts1))",
                output);

        sql = "alter table t99 drop partition p2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DROP PARTITION p2", output);

        sql = "alter table t99 drop partition p2,p3;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DROP PARTITION p2,p3", output);

        sql = "alter table t99 discard partition p2 TABLESPACE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DISCARD PARTITION p2 TABLESPACE", output);

        sql = "alter table t99 discard partition p2,p3 TABLESPACE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DISCARD PARTITION p2,p3 TABLESPACE", output);

        sql = "alter table t99 discard partition all TABLESPACE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DISCARD PARTITION ALL TABLESPACE", output);

        sql = "alter table t99 import partition p2 TABLESPACE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 IMPORT PARTITION p2 TABLESPACE", output);

        sql = "alter table t99 import partition p2,p3 TABLESPACE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 IMPORT PARTITION p2,p3 TABLESPACE", output);

        sql = "alter table t99 import partition all TABLESPACE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 IMPORT PARTITION ALL TABLESPACE", output);

        sql = "alter table t99 truncate partition p2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 TRUNCATE PARTITION p2", output);

        sql = "alter table t99 truncate partition p2,p3;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 TRUNCATE PARTITION p2,p3", output);

        sql = "alter table t99 truncate partition all;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 TRUNCATE PARTITION ALL", output);

        sql = "alter table t99 COALESCE PARTITION 9;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 COALESCE PARTITION 9", output);

        sql = "alter table t99 REORGANIZE PARTITION p1,p2,p3 into (PARTITION p1 VALUES LESS THAN (2000,2000),PARTITION p2 VALUES LESS THAN (2000,2000),PARTITION p3 VALUES LESS THAN (2000,2000));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 REORGANIZE PARTITION INTO( PARTITION p1 VALUES LESS THAN (2000,2000), PARTITION p2 VALUES LESS THAN (2000,2000), PARTITION p3 VALUES LESS THAN (2000,2000))",
                output);

        sql = "alter table t99 EXCHANGE PARTITION p1 with table taaaaa with validation;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t99 EXCHANGE PARTITION p1 WITH TABLE taaaaa WITH VALIDATION", output);

        sql = "alter table t99 ANALYZE partition p2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ANALYZE PARTITION p2", output);

        sql = "alter table t99 ANALYZE partition p2,p3;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ANALYZE PARTITION p2,p3", output);

        sql = "alter table t99 ANALYZE partition all;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ANALYZE PARTITION ALL", output);

        sql = "alter table t99 CHECK partition p2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 CHECK PARTITION p2", output);

        sql = "alter table t99 CHECK partition p2,p3;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 CHECK PARTITION p2,p3", output);

        sql = "alter table t99 CHECK partition all;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 CHECK PARTITION ALL", output);

        sql = "alter table t99 OPTIMIZE partition p2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 OPTIMIZE PARTITION p2", output);

        sql = "alter table t99 OPTIMIZE partition p2,p3;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 OPTIMIZE PARTITION p2,p3", output);

        sql = "alter table t99 OPTIMIZE partition all;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 OPTIMIZE PARTITION ALL", output);

        sql = "alter table t99 REBUILD partition p2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 REBUILD PARTITION p2", output);

        sql = "alter table t99 REBUILD partition p2,p3;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 REBUILD PARTITION p2,p3", output);

        sql = "alter table t99 REBUILD partition all;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 REBUILD PARTITION ALL", output);

        sql = "alter table t99 REPAIR partition p2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 REPAIR PARTITION p2", output);

        sql = "alter table t99 REPAIR partition p2,p3;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 REPAIR PARTITION p2,p3", output);

        sql = "alter table t99 REPAIR partition all;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 REPAIR PARTITION ALL", output);

        sql = "alter table t66 REMOVE PARTITIONING;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t66 REMOVE PARTITIONING", output);

        sql = "alter table t99 AUTO_INCREMENT = 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 AUTO_INCREMENT=1 ", output);

        sql = "alter table t99 avg_row_length = 1 AUTO_INCREMENT = 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 AUTO_INCREMENT=1 AVG_ROW_LENGTH=1 ", output);

        sql = "alter table t99 default character set latin1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DEFAULT CHARACTER SET=latin1 ", output);

        sql = "alter table t99 checksum = 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 CHECKSUM=1 ", output);

        sql = "alter table t99 checksum 0;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 CHECKSUM=0 ", output);

        sql = "alter table t99 default collate = latin1_swedish_ci;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DEFAULT COLLATE=latin1_swedish_ci ", output);

        sql = "alter table t99 comment 'iA1~!@#$%^&*()_+=-[]\\';/.,<>?:\"{}|/*.';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 COMMENT='iA1~!@#$%^&*()_+=-[]\\';/.,<>?:\"{}|/*.' ",
                output);

        sql = "alter table t99 COMPRESSION = 'ZLIB';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 COMPRESSION='ZLIB' ", output);

        sql = "alter table t99 COMPRESSION 'LZ4';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 COMPRESSION='LZ4' ", output);

        sql = "alter table t99 COMPRESSION = 'NONE';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 COMPRESSION='NONE' ", output);

        sql = "alter table t99 CONNECTION = 'asdasda';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 CONNECTION='asdasda' ", output);

        sql = "alter table t99 DATA directory = '/var/appdata/95/dat';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DATA DIRECTORY='/var/appdata/95/dat' ", output);

        sql = "alter table t99 index directory = '/var/appdata/97/idx';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 INDEX DIRECTORY='/var/appdata/97/idx' ", output);

        sql = "alter table t99  DELAY_KEY_WRITE =0;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DELAY_KEY_WRITE=0 ", output);

        sql = "alter table t99  DELAY_KEY_WRITE 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 DELAY_KEY_WRITE=1 ", output);

        sql = "alter table t99 ENCRYPTION = 'Y';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ENCRYPTION='Y' ", output);

        sql = "alter table t99 encryption  'N';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ENCRYPTION='N' ", output);

        sql = "alter table t99 engine = innodb;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ENGINE=innodb ", output);

        sql = "alter table t99 insert_method = NO;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 INSERT_METHOD=NO ", output);

        sql = "alter table t99 insert_method = first;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 INSERT_METHOD=FIRST ", output);

        sql = "alter table t99 insert_method last;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 INSERT_METHOD=LAST ", output);

        sql = "alter table t99 key_block_size = 9;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 KEY_BLOCK_SIZE=9 ", output);

        sql = "alter table t99 MAX_ROWS =9999;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 MAX_ROWS=9999 ", output);

        sql = "alter table t99 MIN_ROWS = 9;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 MIN_ROWS=9 ", output);

        sql = "alter table t99 PACK_KEYS = 0;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 PACK_KEYS=0 ", output);

        sql = "alter table t99 PACK_KEYS = 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 PACK_KEYS=1 ", output);

        sql = "alter table t99 PACK_KEYS = DEFAULT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 PACK_KEYS=DEFAULT ", output);

        sql = "alter table t99 PASSWORD = 'Abcd1`23';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 PASSWORD='Abcd1`23' ", output);

        sql = "alter table t99 ROW_FORMAT =default;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ROW_FORMAT=DEFAULT ", output);

        sql = "alter table t99 ROW_FORMAT =dynamic;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ROW_FORMAT=DYNAMIC ", output);

        sql = "alter table t99 ROW_FORMAT =fixed;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ROW_FORMAT=FIXED ", output);

        sql = "alter table t99 ROW_FORMAT =compressed;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ROW_FORMAT=COMPRESSED ", output);

        sql = "alter table t99 ROW_FORMAT =redundant;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ROW_FORMAT=REDUNDANT ", output);

        sql = "alter table t99 ROW_FORMAT =compact;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 ROW_FORMAT=COMPACT ", output);

        sql = "alter table t99 STATS_AUTO_RECALC  = default;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 STATS_AUTO_RECALC=DEFAULT ", output);

        sql = "alter table t99 STATS_AUTO_RECALC  = 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 STATS_AUTO_RECALC=1 ", output);

        sql = "alter table t99 STATS_AUTO_RECALC  = 0;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 STATS_AUTO_RECALC=0 ", output);

        sql = "alter table t99 STATS_PERSISTENT  = default;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 STATS_PERSISTENT=DEFAULT ", output);

        sql = "alter table t99 STATS_PERSISTENT  = 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 STATS_PERSISTENT=1 ", output);

        sql = "alter table t99 STATS_PERSISTENT  = 0;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 STATS_PERSISTENT=0 ", output);

        sql = "alter table t99 STATS_SAMPLE_PAGES  = 2312;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 STATS_SAMPLE_PAGES=2312 ", output);

        sql = "alter table t99 TABLESPACE p1 storage disk;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 TABLESPACE p1 STORAGE DISK ", output);

        sql = "alter table t99 TABLESPACE p2 storage memory;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 TABLESPACE p2 STORAGE MEMORY ", output);

        sql = "alter table t99 TABLESPACE p3".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 TABLESPACE p3", output);

        sql = "alter table t99 union = (t1,t2,t3);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 UNION=(t1,t2,t3) ", output);

        sql = "alter table t99 union = (t1);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 UNION=(t1) ", output);

        sql = "alter table t88 PARTITION BY linear hash ((col1-10)*2);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY LINEAR HASH((col1-10)*2)", output);

        sql = "alter table t88 PARTITION BY hash ((col1-10)*2);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY HASH((col1-10)*2)", output);

        sql = "alter table t88 partition by linear key (col1,col2);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY LINEAR KEY(col1,col2)", output);

        sql = "alter table t88 partition by key (col1,col2);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY KEY(col1,col2)", output);

        sql = "alter table t88 partition by linear key ALGORITHM = 1 (col1);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY LINEAR KEY ALGORITHM=1(col1)", output);

        sql = "alter table t88 partition by key ALGORITHM = 1 (col1);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY KEY ALGORITHM=1(col1)", output);

        sql = "alter table t88 partition by RANGE(col1/10);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY RANGE(col1/10)", output);

        sql = "alter table t88 partition by RANGE columns(col1,col2);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY RANGE COLUMNS(col1,col2)", output);

        sql = "alter table t88 partition by LIST(col1*10);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY LIST(col1*10)", output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2,col3,col4,col5);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2,col3,col4,col5)",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by LINEAR HASH(col1);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY LINEAR HASH(col1)",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by LINEAR KEY(col1,col2);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY LINEAR KEY(col1,col2)",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by LINEAR KEY ALGORITHM = 2 (col1,col2);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY LINEAR KEY ALGORITHM=2(col1,col2)",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by HASH(col1);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY HASH(col1)",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by KEY(col1,col2);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY KEY(col1,col2)",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by KEY ALGORITHM = 2 (col1,col2);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY KEY ALGORITHM=2(col1,col2)",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by KEY ALGORITHM = 2 (col1,col2) subpartitions 10;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY KEY ALGORITHM=2(col1,col2) SUBPARTITIONS 10",
                output);

        sql = "alter table t88 partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by  KEY ALGORITHM = 2 (col1,col2) subpartitions 10(PARTITION P2 VALUES LESS THAN (3000,3000) INDEX DIRECTORY = '/var/appdata/97/idx',PARTITION p1 VALUES LESS THAN (2000,2000)STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 2000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1 (SUBPARTITION p2 STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 1000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1 comment 'asdasd',SUBPARTITION p3 STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 4000 MAX_ROWS=5000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1 comment 'asdasd'));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t88 PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY KEY ALGORITHM=2(col1,col2) SUBPARTITIONS 10( PARTITION P2 VALUES LESS THAN (3000,3000) INDEX DIRECTORY='/var/appdata/97/idx', PARTITION p1 VALUES LESS THAN (2000,2000) STORAGE ENGINE=innodb COMMENT='aaa' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=2000 TABLESPACE=ts1( SUBPARTITION p2 STORAGE ENGINE=innodb COMMENT='asdasd' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=1000 TABLESPACE=ts1, SUBPARTITION p3 STORAGE ENGINE=innodb COMMENT='asdasd' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=5000 MIN_ROWS=4000 TABLESPACE=ts1))",
                output);

        sql = "alter table t99 insert_method = NO,key_block_size = 9;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t99 INSERT_METHOD=NO KEY_BLOCK_SIZE=9 ", output);

        sql = "alter table t88 insert_method = NO ,add b int;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLE t88 INSERT_METHOD=NO ,ADD COLUMN b int DEFAULT NULL",
                output);

        sql = "alter table t9 AUTO_INCREMENT =9 ,add partition (PARTITION p1 VALUES LESS THAN (2000,2000)STORAGE ENGINE =  innodb comment 'aaa'),MIN_ROWS = 9,drop primary key partition by LIST COLUMNS(col1,col2) partitions 9 SUBPARTITION by  KEY ALGORITHM = 2 (col1,col2) subpartitions 10 (PARTITION P2 VALUES LESS THAN (3000,3000) INDEX DIRECTORY = '/var/appdata/97/idx',PARTITION p1 VALUES LESS THAN (2000,2000)STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 4000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1 (SUBPARTITION p2 STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 4000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1 comment 'asdasd',SUBPARTITION p3 STORAGE ENGINE =  innodb comment 'aaa' MIN_ROWS = 4000 MAX_ROWS=4000 DATA directory = '/var/appdata/95/dat' INDEX DIRECTORY = '/var/appdata/97/idx' TABLESPACE = ts1 comment 'asdasd'));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t9 AUTO_INCREMENT=9 ,ADD PARTITION( PARTITION p1 VALUES LESS THAN (2000,2000) STORAGE ENGINE=innodb COMMENT='aaa'),MIN_ROWS=9 ,DROP PRIMARY KEY PARTITION BY LIST COLUMNS(col1,col2) PARTITIONS 9 SUBPARTITION BY KEY ALGORITHM=2(col1,col2) SUBPARTITIONS 10( PARTITION P2 VALUES LESS THAN (3000,3000) INDEX DIRECTORY='/var/appdata/97/idx', PARTITION p1 VALUES LESS THAN (2000,2000) STORAGE ENGINE=innodb COMMENT='aaa' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=4000 TABLESPACE=ts1( SUBPARTITION p2 STORAGE ENGINE=innodb COMMENT='asdasd' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=4000 TABLESPACE=ts1, SUBPARTITION p3 STORAGE ENGINE=innodb COMMENT='asdasd' DATA DIRECTORY='/var/appdata/95/dat' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=4000 MIN_ROWS=4000 TABLESPACE=ts1))",
                output);

        sql = "alter table t9 insert_method = NO MAX_ROWS =9999 MIN_ROWS = 9 PACK_KEYS = 1 PASSWORD = 'aA1~!@#$%^&*()_+=-[]\\';/.,<>?:\"{}|/*.' ROW_FORMAT = default STATS_AUTO_RECALC = default STATS_PERSISTENT = default STATS_SAMPLE_PAGES  = 2312,add b int ;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTableStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLE t9 INSERT_METHOD=NO MAX_ROWS=9999 MIN_ROWS=9 PACK_KEYS=1 PASSWORD='aA1~!@#$%^&*()_+=-[]\\';/.,<>?:\"{}|/*.' ROW_FORMAT=DEFAULT STATS_AUTO_RECALC=DEFAULT STATS_PERSISTENT=DEFAULT STATS_SAMPLE_PAGES=2312 ,ADD COLUMN b int DEFAULT NULL",
                output);
    }

    public void testAlterDatabase() throws Exception {
        byte[] sql = "ALTER DATABASE test DEFAULT CHARACTER SET = 'testDb';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterDatabaseStatement alter = (DDLAlterDatabaseStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER DATABASE test CHARACTER SET 'testDb'", output);

        sql = "ALTER SCHEMA test CHARACTER SET 'testDb';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER DATABASE test CHARACTER SET 'testDb'", output);

        sql = "ALTER DATABASE test DEFAULT COLLATE = 'utf8_bin';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER DATABASE test COLLATE 'utf8_bin'", output);

        sql = "ALTER SCHEMA test  COLLATE 'utf8_unicode_ci';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER DATABASE test COLLATE 'utf8_unicode_ci'", output);

        sql = "ALTER DATABASE test DEFAULT ENCRYPTION = 'Y';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER DATABASE test DEFAULT ENCRYPTION 'Y'", output);

        sql = "ALTER DATABASE aaaa DEFAULT ENCRYPTION \"n\";".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER DATABASE aaaa DEFAULT ENCRYPTION 'N'", output);

        sql = "ALTER DATABASE test  ENCRYPTION 'N' DEFAULT CHARACTER SET = 'testDb'  CHARACTER SET 'testDb' DEFAULT COLLATE = 'utf8_unicode_ci'  DEFAULT CHARACTER SET = 'dasd'  ;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterDatabaseStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER DATABASE test CHARACTER SET 'dasd' COLLATE 'utf8_unicode_ci' DEFAULT ENCRYPTION 'N'",
                output);
    }

    public void testAlterEvent() throws Exception {
        byte[] sql = "ALTER DEFINER  = 'root'@'localhost' EVENT event_name ;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterEventStatement alter = (DDLAlterEventStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER DEFINER='root'@'localhost' EVENT event_name", output);

        sql = "ALTER DEFINER = 'root'@'localhost' EVENT event_name ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER DEFINER='root'@'localhost' EVENT event_name ON SCHEDULE AT CURRENT_TIMESTAMP+INTERVAL 1 HOUR",
                output);

        sql = "ALTER EVENT event_name ON SCHEDULE AT '2006-02-10 23:59:00';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE AT '2006-02-10 23:59:00'", output);

        sql = "ALTER EVENT event_name ON SCHEDULE AT '2006-02-10 23:59:00' + INTERVAL 1 HOUR;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER EVENT event_name ON SCHEDULE AT '2006-02-10 23:59:00'+INTERVAL 1 HOUR",
                output);

        sql = "ALTER EVENT event_name ON SCHEDULE AT '2006-02-10 23:59:00' + INTERVAL 1 HOUR + INTERVAL 2 HOUR;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER EVENT event_name ON SCHEDULE AT '2006-02-10 23:59:00'+INTERVAL 1 HOUR+INTERVAL 2 HOUR",
                output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 YEAR;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 YEAR", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 QUARTER;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 QUARTER", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 MONTH;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 MONTH", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 DAY;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 DAY", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 HOUR;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 HOUR", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 MINUTE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 MINUTE", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 WEEK;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 WEEK", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 SECOND;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 SECOND", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 YEAR_MONTH;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 YEAR_MONTH", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 DAY_HOUR;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 DAY_HOUR", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 DAY_MINUTE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 DAY_MINUTE", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 DAY_SECOND;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 DAY_SECOND", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 HOUR_MINUTE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 HOUR_MINUTE", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 HOUR_SECOND;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 HOUR_SECOND", output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 1 MINUTE_SECOND;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON SCHEDULE EVERY 1 MINUTE_SECOND", output);

        sql = "ALTER EVENT event_name RENAME TO new_event_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name RENAME TO new_event_name", output);

        sql = "ALTER EVENT event_name ENABLE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ENABLE", output);

        sql = "ALTER EVENT event_name DISABLE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name DISABLE", output);

        sql = "ALTER EVENT event_name DISABLE ON SLAVE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name DISABLE ON SLAVE", output);

        sql = "ALTER EVENT event_name COMMENT 'string';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name COMMENT 'string'", output);

        sql = "ALTER EVENT event_name ENABLE DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ENABLE DO UPDATE aaaa.test SET t1=t1+1",
                output);

        sql = "ALTER EVENT event_name ON COMPLETION PRESERVE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON COMPLETION PRESERVE", output);

        sql = "ALTER EVENT event_name ON COMPLETION NOT PRESERVE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER EVENT event_name ON COMPLETION NOT PRESERVE", output);


        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP + INTERVAL 4 HOUR;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP+INTERVAL 4 HOUR",
                output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 12 HOUR ENDS CURRENT_TIMESTAMP + INTERVAL 4 HOUR  + INTERVAL 4 HOUR;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER EVENT event_name ON SCHEDULE EVERY 12 HOUR ENDS CURRENT_TIMESTAMP+INTERVAL 4 HOUR+INTERVAL 4 HOUR",
                output);

        sql = "ALTER EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP + INTERVAL 4 HOUR ENDS CURRENT_TIMESTAMP + INTERVAL 4 HOUR  + INTERVAL 4 HOUR;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP+INTERVAL 4 HOUR ENDS CURRENT_TIMESTAMP+INTERVAL 4 HOUR+INTERVAL 4 HOUR",
                output);

        sql = "ALTER DEFINER = 'root'@'localhost'  EVENT event_name ON SCHEDULE  EVERY 12 HOUR STARTS CURRENT_TIMESTAMP() + INTERVAL 4 HOUR ENDS '2099-02-10 23:59:00' + INTERVAL 4 HOUR+ INTERVAL 12 HOUR ON COMPLETION PRESERVE RENAME TO new_event_name ENABLE COMMENT 'string' DO UPDATE aaaa.test SET t1 = t1 + 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterEventStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER DEFINER='root'@'localhost' EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP()+INTERVAL 4 HOUR ENDS '2099-02-10 23:59:00'+INTERVAL 4 HOUR+INTERVAL 12 HOUR ON COMPLETION PRESERVE RENAME TO new_event_name ENABLE COMMENT 'string' DO UPDATE aaaa.test SET t1=t1+1",
                output);
    }

    public void testAlterFunction() throws Exception {
        byte[] sql = "ALTER FUNCTION func_name COMMENT 'string';".getBytes();
        Lexer lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterFunctionStatement alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name COMMENT 'string'", output);

        sql = "ALTER FUNCTION func_name LANGUAGE SQL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name LANGUAGE SQL", output);

        sql = "ALTER FUNCTION func_name CONTAINS SQL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name CONTAINS SQL", output);

        sql = "ALTER FUNCTION func_name NO SQL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name NO SQL", output);

        sql = "ALTER FUNCTION func_name READS SQL DATA;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name READS SQL DATA", output);

        sql = "ALTER FUNCTION func_name MODIFIES SQL DATA;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name MODIFIES SQL DATA", output);

        sql = "ALTER FUNCTION func_name SQL SECURITY DEFINER;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name SQL SECURITY DEFINER", output);

        sql = "ALTER FUNCTION func_name SQL SECURITY INVOKER;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name SQL SECURITY INVOKER", output);

        sql = "ALTER FUNCTION func_name COMMENT 'string' LANGUAGE SQL CONTAINS SQL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name COMMENT 'string' LANGUAGE SQL CONTAINS SQL",
                output);

        sql = "ALTER FUNCTION func_name COMMENT 'string' LANGUAGE SQL CONTAINS SQL NO SQL  READS SQL DATA SQL SECURITY DEFINER;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER FUNCTION func_name COMMENT 'string' LANGUAGE SQL CONTAINS SQL NO SQL READS SQL DATA SQL SECURITY DEFINER",
                output);

        sql = "ALTER FUNCTION func_name  NO SQL  READS SQL DATA SQL SECURITY DEFINER;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterFunctionStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER FUNCTION func_name NO SQL READS SQL DATA SQL SECURITY DEFINER",
                output);
    }

    public void testAlterInstance() throws Exception {
        byte[] sql = "ALTER INSTANCE ROTATE INNODB MASTER KEY;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterInstanceStatement alter = (DDLAlterInstanceStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER INSTANCE ROTATE INNODB MASTER KEY", output);

        sql = "ALTER INSTANCE ROTATE BINLOG MASTER KEY;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterInstanceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER INSTANCE ROTATE BINLOG MASTER KEY", output);

        sql = "ALTER INSTANCE RELOAD TLS;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterInstanceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER INSTANCE RELOAD TLS", output);

        sql = "ALTER INSTANCE RELOAD TLS NO ROLLBACK ON ERROR;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterInstanceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER INSTANCE RELOAD TLS NO ROLLBACK ON ERROR", output);
    }

    public void testAlterLogfileGroup() throws Exception {
        byte[] sql = "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterLogfileGroupStatement alter = (DDLAlterLogfileGroupStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name'", output);

        sql = "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' initial_size 5G;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterLogfileGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' INITIAL_SIZE=5G",
                output);

        sql = "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' initial_size 5G;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterLogfileGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' INITIAL_SIZE=5G",
                output);

        sql = "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' initial_size = 10M;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterLogfileGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' INITIAL_SIZE=10M",
                output);

        sql = "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' WAIT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterLogfileGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' WAIT",
                output);

        sql = "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' ENGINE = INNODB;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterLogfileGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' ENGINE=INNODB", output);

        sql = "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' WAIT ENGINE = INNODB initial_size = 5;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterLogfileGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER LOGFILE GROUP logfile_group ADD UNDOFILE 'file_name' INITIAL_SIZE=5 WAIT ENGINE=INNODB",
                output);
    }

    public void testAlterProcedure() throws Exception {
        byte[] sql = "ALTER PROCEDURE proc_name;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterProcedureStatement alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name", output);

        sql = "ALTER PROCEDURE proc_name COMMENT N'a string';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name COMMENT N'a string'", output);

        sql = "ALTER PROCEDURE proc_name COMMENT 'a string';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name COMMENT 'a string'", output);

        sql = "ALTER PROCEDURE proc_name COMMENT 'a' ' ' 'string';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name COMMENT 'a' ' ' 'string'", output);

        sql = "ALTER PROCEDURE proc_name LANGUAGE SQL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name LANGUAGE SQL", output);

        sql = "ALTER PROCEDURE proc_name contains sql;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name CONTAINS SQL", output);

        sql = "ALTER PROCEDURE proc_name NO SQL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name NO SQL", output);

        sql = "ALTER PROCEDURE proc_name READS SQL DATA;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name READS SQL DATA", output);

        sql = "ALTER PROCEDURE proc_name modifies sql data;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name MODIFIES SQL DATA", output);

        sql = "ALTER PROCEDURE proc_name SQL SECURITY DEFINER;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name SQL SECURITY DEFINER", output);

        sql = "ALTER PROCEDURE proc_name SQL SECURITY INVOKER;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER PROCEDURE proc_name SQL SECURITY INVOKER", output);

        sql = "ALTER PROCEDURE proc_name COMMENT 'ASDF@#!' LANGUAGE SQL NO SQL SQL SECURITY DEFINER;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER PROCEDURE proc_name COMMENT 'ASDF@#!' LANGUAGE SQL NO SQL SQL SECURITY DEFINER",
                output);

        sql = "ALTER PROCEDURE proc_name NO SQL LANGUAGE SQL  SQL SECURITY DEFINER COMMENT 'ASDF@#!';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterProcedureStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER PROCEDURE proc_name NO SQL LANGUAGE SQL SQL SECURITY DEFINER COMMENT 'ASDF@#!'",
                output);
    }

    public void testAlterResourceGroup() throws Exception {
        byte[] sql = "ALTER RESOURCE GROUP rg1 VCPU = 0-63;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DALAlterResourceGroupStatement alter = (DALAlterResourceGroupStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER RESOURCE GROUP rg1 VCPU=0-63", output);

        sql = "ALTER RESOURCE GROUP rg1 VCPU = 0,0-64,123;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterResourceGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER RESOURCE GROUP rg1 VCPU=0,0-64,123", output);

        sql = "ALTER RESOURCE GROUP rg2 THREAD_PRIORITY = 5;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterResourceGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER RESOURCE GROUP rg2 THREAD_PRIORITY=5", output);

        sql = "ALTER RESOURCE GROUP rg3 DISABLE FORCE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterResourceGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER RESOURCE GROUP rg3 DISABLE FORCE", output);

        sql = "ALTER RESOURCE GROUP rg1 VCPU = 0,0-64,123  THREAD_PRIORITY = 5   DISABLE FORCE;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterResourceGroupStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER RESOURCE GROUP rg1 VCPU=0,0-64,123 THREAD_PRIORITY=5 DISABLE FORCE", output);
    }

    public void testAlterServer() throws Exception {
        byte[] sql = "ALTER SERVER server_name OPTIONS(HOST '192.168.174.100');".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterServerStatement alter = (DDLAlterServerStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER SERVER server_name OPTIONS (HOST '192.168.174.100')", output);

        sql = "ALTER SERVER server_name OPTIONS(DATABASE 'test');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterServerStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER SERVER server_name OPTIONS (DATABASE 'test')", output);

        sql = "ALTER SERVER server_name OPTIONS(USER 'root');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterServerStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER SERVER server_name OPTIONS (USER 'root')", output);

        sql = "ALTER SERVER server_name OPTIONS(PASSWORD 'allalala');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterServerStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER SERVER server_name OPTIONS (PASSWORD 'allalala')", output);

        sql = "ALTER SERVER server_name OPTIONS(SOCKET '/var/run/mysqld/mysqld.sock');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterServerStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER SERVER server_name OPTIONS (SOCKET '/var/run/mysqld/mysqld.sock')", output);

        sql = "ALTER SERVER server_name OPTIONS(OWNER 'root');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterServerStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER SERVER server_name OPTIONS (OWNER 'root')", output);

        sql = "ALTER SERVER server_name OPTIONS(PORT 3306);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterServerStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER SERVER server_name OPTIONS (PORT 3306)", output);

        sql = "ALTER SERVER server_name OPTIONS(PASSWORD 'allalala',HOST '192.168.174.100',DATABASE 'test',PORT 3306);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterServerStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER SERVER server_name OPTIONS (PASSWORD 'allalala',HOST '192.168.174.100',DATABASE 'test',PORT 3306)",
                output);
    }

    public void testAlterTablespace() throws Exception {
        byte[] sql = "ALTER TABLESPACE tablespace_name ADD DATAFILE 'file_name';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterTablespaceStatement alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name ADD DATAFILE 'file_name'", output);

        sql = "ALTER TABLESPACE tablespace_name DROP DATAFILE 'file_name';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name DROP DATAFILE 'file_name'", output);

        sql = "ALTER TABLESPACE tablespace_name INITIAL_SIZE = 10G;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name INITIAL_SIZE=10G", output);

        sql = "ALTER TABLESPACE tablespace_name INITIAL_SIZE 10G".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name INITIAL_SIZE=10G", output);

        sql = "ALTER TABLESPACE tablespace_name WAIT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name WAIT", output);

        sql = "ALTER TABLESPACE tablespace_name RENAME TO tablespace_name;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name RENAME TO tablespace_name", output);

        sql = "ALTER UNDO TABLESPACE tablespace_name SET INACTIVE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER UNDO TABLESPACE tablespace_name SET INACTIVE", output);

        sql = "ALTER UNDO TABLESPACE tablespace_name SET ACTIVE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER UNDO TABLESPACE tablespace_name SET ACTIVE", output);

        sql = "ALTER TABLESPACE tablespace_name ENCRYPTION = 'Y';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name ENCRYPTION='Y'", output);

        sql = "ALTER TABLESPACE tablespace_name ENCRYPTION 'N';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name ENCRYPTION='N'", output);

        sql = "ALTER TABLESPACE tablespace_name ENGINE = INNODB;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name ENGINE=INNODB", output);

        sql = "ALTER TABLESPACE tablespace_name ENGINE NDB;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name ENGINE=NDB", output);

        sql = "ALTER TABLESPACE tablespace_name ADD DATAFILE 'file_name' INITIAL_SIZE = 10G  WAIT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLESPACE tablespace_name ADD DATAFILE 'file_name' INITIAL_SIZE=10G WAIT",
                output);

        sql = "ALTER TABLESPACE tablespace_name  DROP DATAFILE 'file_name' INITIAL_SIZE = 10G  WAIT  ENGINE = NDB ;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER TABLESPACE tablespace_name DROP DATAFILE 'file_name' INITIAL_SIZE=10G WAIT ENGINE=NDB",
                output);

        sql = "ALTER TABLESPACE tablespace_name ENCRYPTION = 'Y' ENGINE NDB;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterTablespaceStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER TABLESPACE tablespace_name ENCRYPTION='Y' ENGINE=NDB", output);
    }

    public void testAlterUser() throws Exception {
        byte[] sql = "ALTER USER IF EXISTS user() IDENTIFIED BY 'auth_string';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DALAlterUserStatement alter = (DALAlterUserStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS user() IDENTIFIED BY 'auth_string'", output);

        sql = "ALTER USER IF EXISTS user() IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS user() IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string'",
                output);

        sql = "ALTER USER IF EXISTS user() IDENTIFIED BY 'auth_string' RETAIN CURRENT PASSWORD;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS user() IDENTIFIED BY 'auth_string' RETAIN CURRENT PASSWORD",
                output);

        sql = "ALTER USER IF EXISTS user() IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS user() IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD",
                output);

        sql = "ALTER USER IF EXISTS user() DISCARD OLD PASSWORD;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS user() DISCARD OLD PASSWORD", output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string'", output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string'"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string'",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' RETAIN CURRENT PASSWORD;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' RETAIN CURRENT PASSWORD",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin", output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string'", output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string'",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string' RETAIN CURRENT PASSWORD;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string' RETAIN CURRENT PASSWORD",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin AS 'auth_string';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED WITH auth_plugin AS 'auth_string'", output);

        sql = "ALTER USER IF EXISTS dagon DISCARD OLD PASSWORD;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS dagon DISCARD OLD PASSWORD", output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD ,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD,das2 IDENTIFIED WITH auth_plugin AS 'auth_string',dazx DISCARD OLD PASSWORD;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD,das2 IDENTIFIED WITH auth_plugin AS 'auth_string',dazx DISCARD OLD PASSWORD",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE NONE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE NONE",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE SSL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE SSL",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE CIPHER 'cipher';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE CIPHER 'cipher'",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE ISSUER 'issuer';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE ISSUER 'issuer'",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE SUBJECT 'subject';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE SUBJECT 'subject'",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject'",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_QUERIES_PER_HOUR 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_QUERIES_PER_HOUR 1",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_UPDATES_PER_HOUR 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_UPDATES_PER_HOUR 1",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_CONNECTIONS_PER_HOUR 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_CONNECTIONS_PER_HOUR 1",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_USER_CONNECTIONS 2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_USER_CONNECTIONS 2",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE DEFAULT",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE NEVER;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE NEVER",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE INTERVAL 2 day;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE INTERVAL 2 DAY",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD HISTORY DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD HISTORY DEFAULT",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD HISTORY 2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD HISTORY 2",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD REUSE INTERVAL 2 day;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD REUSE INTERVAL 2 DAY",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD REUSE INTERVAL DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD REUSE INTERVAL DEFAULT",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD REQUIRE CURRENT DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD REQUIRE CURRENT DEFAULT",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD REQUIRE CURRENT OPTIONAL;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' PASSWORD REQUIRE CURRENT OPTIONAL",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string'  ACCOUNT LOCK;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' ACCOUNT LOCK",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string'  ACCOUNT UNLOCK;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' ACCOUNT UNLOCK",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD ,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10 PASSWORD REQUIRE CURRENT DEFAULT"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10 PASSWORD REQUIRE CURRENT DEFAULT",
                output);

        sql = "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD ,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10 ACCOUNT UNLOCK;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DALAlterUserStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER USER IF EXISTS dagon IDENTIFIED BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REPLACE 'current_auth_string' RETAIN CURRENT PASSWORD REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10 ACCOUNT UNLOCK",
                output);
    }

    public void testAlterView() throws Exception {
        byte[] sql = "ALTER VIEW v_today (id,a,b) AS SELECT * from t1;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLAlterViewStatement alter = (DDLAlterViewStatement) tuple._1().get(0);
        String output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER VIEW v_today(id,a,b) AS SELECT * FROM t1", output);

        sql = "ALTER ALGORITHM = UNDEFINED VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER ALGORITHM=UNDEFINED VIEW v_today(id,a,b) AS SELECT * FROM t1",
                output);

        sql = "ALTER ALGORITHM = MERGE VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER ALGORITHM=MERGE VIEW v_today(id,a,b) AS SELECT * FROM t1",
                output);

        sql = "ALTER ALGORITHM = TEMPTABLE VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER ALGORITHM=TEMPTABLE VIEW v_today(id,a,b) AS SELECT * FROM t1",
                output);

        sql = "ALTER DEFINER = 'root'@'localhost' VIEW v_today (id,a,b)  AS SELECT * FROM t1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER DEFINER='root'@'localhost' VIEW v_today(id,a,b) AS SELECT * FROM t1",
                output);

        sql = "ALTER SQL SECURITY DEFINER VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER SQL SECURITY DEFINER VIEW v_today(id,a,b) AS SELECT * FROM t1",
                output);

        sql = "ALTER SQL SECURITY INVOKER VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER SQL SECURITY INVOKER VIEW v_today(id,a,b) AS SELECT * FROM t1",
                output);

        sql = "ALTER VIEW v_today (id,a,b) AS SELECT * from t1 WITH CHECK OPTION;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals("ALTER VIEW v_today(id,a,b) AS SELECT * FROM t1 WITH CHECK OPTION",
                output);

        sql = "ALTER VIEW v_today (id,a,b) AS SELECT * from t1 WITH CASCADED CHECK OPTION;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER VIEW v_today(id,a,b) AS SELECT * FROM t1 WITH CASCADED CHECK OPTION",
                output);

        sql = "ALTER VIEW v_today (id,a,b) AS SELECT * from t1 WITH LOCAL CHECK OPTION;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER VIEW v_today(id,a,b) AS SELECT * FROM t1 WITH LOCAL CHECK OPTION", output);

        sql = "ALTER ALGORITHM = UNDEFINED DEFINER = 'root'@'localhost' SQL SECURITY DEFINER  VIEW v_today (id,a,b) AS SELECT * from t1 WITH LOCAL CHECK OPTION;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        alter = (DDLAlterViewStatement) tuple._1().get(0);
        output = outputMySQL(alter, sql);
        Assert.assertEquals(
                "ALTER ALGORITHM=UNDEFINED DEFINER='root'@'localhost' SQL SECURITY DEFINER VIEW v_today(id,a,b) AS SELECT * FROM t1 WITH LOCAL CHECK OPTION",
                output);
    }
}
