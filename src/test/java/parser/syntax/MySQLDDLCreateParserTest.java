package parser.syntax;

import junit.framework.Assert;
import junit.framework.TestCase;
import mysql.charset.MySqlCharset;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dal.account.*;
import parser.ast.stmt.dal.resource.*;
import parser.ast.stmt.ddl.create.DDLCreateDatabaseStatement;
import parser.ast.stmt.ddl.create.DDLCreateEventStatement;
import parser.ast.stmt.ddl.create.DDLCreateFunctionStatement;
import parser.ast.stmt.ddl.create.DDLCreateIndexStatement;
import parser.ast.stmt.ddl.create.DDLCreateLogfileGroupStatement;
import parser.ast.stmt.ddl.create.DDLCreateProcedureStatement;
import parser.ast.stmt.ddl.create.DDLCreateServerStatement;
import parser.ast.stmt.ddl.create.DDLCreateSpatialReferenceSystemStatement;
import parser.ast.stmt.ddl.create.DDLCreateTableStatement;
import parser.ast.stmt.ddl.create.DDLCreateTablespaceStatement;
import parser.ast.stmt.ddl.create.DDLCreateTriggerStatement;
import parser.ast.stmt.ddl.create.DDLCreateViewStatement;
import parser.util.Tuple2;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class MySQLDDLCreateParserTest extends AbstractSyntaxTest {

    public void testCreateTable() throws Exception {
        byte[] sql = "crEate temporary tabLe if not exists tb_name".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateTableStatement create = (DDLCreateTableStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase
            .assertEquals("CREATE TEMPORARY TABLE IF NOT EXISTS tb_name(\n" + "  \n" + ")", output);

        sql = ("CREATE TABLE `sm_b_jwy` (\n" + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
            + "  `a` int(10) DEFAULT NULL,\n" + "  `b` decimal(5,2) DEFAULT NULL,\n"
            + "  `c` decimal(5,2) DEFAULT NULL,\n" + "  `d` date DEFAULT NULL,\n" + "  `e` time(6) DEFAULT NULL,\n"
            + "  `f` timestamp(6) NULL DEFAULT CURRENT_TIMESTAMP(6),\n"
            + "  `g` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),\n" + "  `h` year(4) DEFAULT NULL,\n"
            + "  `i` char(20) DEFAULT NULL,\n" + "  `j` varchar(30) DEFAULT NULL,\n" + "  `k` blob,\n" + "  `l` text,\n"
            + "  `m` enum('','null','1','2','3') DEFAULT NULL,\n" + "  `n` set('','null','1','2','3') DEFAULT NULL,\n"
            + "  PRIMARY KEY (`id`)\n" + ") ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8\n").getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE `sm_b_jwy`(\n" + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" + "  `a` int(10) DEFAULT NULL,\n"
                + "  `b` decimal(5,2) DEFAULT NULL,\n" + "  `c` decimal(5,2) DEFAULT NULL,\n"
                + "  `d` date DEFAULT NULL,\n" + "  `e` time(6) DEFAULT NULL,\n"
                + "  `f` timestamp(6) DEFAULT CURRENT_TIMESTAMP(6),\n"
                + "  `g` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),\n" + "  `h` year(4) DEFAULT NULL,\n"
                + "  `i` char(20) DEFAULT NULL,\n" + "  `j` varchar(30) DEFAULT NULL,\n" + "  `k` blob DEFAULT NULL,\n"
                + "  `l` text DEFAULT NULL,\n" + "  `m` enum('','null','1','2','3') DEFAULT NULL,\n"
                + "  `n` set('','null','1','2','3') DEFAULT NULL,\n" + "  PRIMARY KEY (`id`)\n"
                + ")AUTO_INCREMENT=16 ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 ", output);

        sql = "crEate temporary tabLe if not exists tb_name select id from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE TEMPORARY TABLE IF NOT EXISTS tb_name(\n" + "  \n" + ")AS SELECT id FROM t1", output);

        sql = "crEate  tabLe if not exists taaaaa (id int not null) ignore as select id from t1".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS taaaaa(\n" + "  id int NOT NULL\n" + ")IGNORE AS SELECT id FROM t1", output);

        sql = "crEate  tabLe if not exists tbbbb (id int not null) replace as select id from t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS tbbbb(\n" + "  id int NOT NULL\n" + ")REPLACE AS SELECT id FROM t1", output);

        sql = "crEate tabLe if not exists tb_name".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS tb_name(\n" + "  \n" + ")", output);

        sql = "crEate temporary tabLe tb_name".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TEMPORARY TABLE tb_name(\n" + "  \n" + ")", output);

        sql =
            "crEate table if not exists t11 ( id int not null,year_col varchar(8) not null, index index1 using btree (id) key_block_size = 10) auto_increment = 10,avg_row_length=10 partition by linear hash(id);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col varchar(8) NOT NULL,\n"
                + "INDEX index1 USING BTREE (id) KEY_BLOCK_SIZE=10\n"
                + ")AUTO_INCREMENT=10 AVG_ROW_LENGTH=10  PARTITION BY LINEAR HASH(id)", output);

        sql =
            "create table if not exists t11 ( id int not null,year_col char(11) null, key index2 using hash (id)  using hash) default character set = ct1,checksum = 0 partition by key ALGORITHM=1(id,year_col);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col char(11) DEFAULT NULL,\n"
                + "KEY index2 USING HASH (id) USING HASH\n"
                + ")DEFAULT CHARACTER SET=ct1 CHECKSUM=0  PARTITION BY KEY ALGORITHM=1(id,year_col)", output);

        sql =
            "create table if not exists t11 ( id int not null,year_col boolean default false, index index3 using btree (id)  using btree) default COLLATE = 'utf8_bin',checksum = 1,comment 'test' partition by range (id+10);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col boolean DEFAULT FALSE,\n"
                + "INDEX index3 USING BTREE (id) USING BTREE\n"
                + ")CHECKSUM=1 DEFAULT COLLATE='utf8_bin' COMMENT='test'  PARTITION BY RANGE(id+10)", output);

        sql =
            "create table if not exists t11 ( id int not null,year_col int8 not null, index index4 using btree (id)  using btree) default COLLATE = collate4,checksum = 1,comment 'test2' partition by range columns(id,year_col) (partition p1 values less than (1000,2000));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col bigint NOT NULL,\n"
                + "INDEX index4 USING BTREE (id) USING BTREE\n"
                + ")CHECKSUM=1 DEFAULT COLLATE=collate4 COMMENT='test2'  PARTITION BY RANGE COLUMNS(id,year_col)( PARTITION p1 VALUES LESS THAN (1000,2000))",
            output);

        sql =
            "create table if not exists t11 ( id int not null,year_col int8 not null, index index4 using btree (id)  using btree) default COLLATE = collate4,checksum = 1,comment 'test2' partition by range columns(id,year_col) (partition p1 values less than (1000,2000));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col bigint NOT NULL,\n"
                + "INDEX index4 USING BTREE (id) USING BTREE\n"
                + ")CHECKSUM=1 DEFAULT COLLATE=collate4 COMMENT='test2'  PARTITION BY RANGE COLUMNS(id,year_col)( PARTITION p1 VALUES LESS THAN (1000,2000))",
            output);

        sql =
            "create table if not exists t11 ( id int4 not null,year_col int2 not null,time_col int1 not null, index index4 using btree ((id+10/2) asc) comment 'abc1]') COMPRESSION = 'ZLIB',CONNECTION = 'asdasda',DATA DIRECTORY = '/var/appdata/97/idx' partition by LIST columns(id,year_col) (partition p1 VALUES IN (1000,2000) STORAGE ENGINE = innodb comment 'joo*-+><:' DATA DIRECTORY ='/var/appdata/97/idx' INDEX DIRECTORY = '/var/appdata/96/idx'  MAX_ROWS = 10 MIN_ROWS = 5 TABLESPACE = tb1 (SUBPARTITION sub1 STORAGE ENGINe = innodb));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col smallint NOT NULL,\n"
                + "  time_col tinyint NOT NULL,\n" + "INDEX index4 USING BTREE ((id+10/2)) COMMENT 'abc1]'\n"
                + ")COMPRESSION='ZLIB' CONNECTION='asdasda' DATA DIRECTORY='/var/appdata/97/idx'  PARTITION BY LIST COLUMNS(id,year_col)( PARTITION p1 VALUES IN (1000,2000) STORAGE ENGINE=innodb COMMENT='joo*-+><:' DATA DIRECTORY='/var/appdata/97/idx' INDEX DIRECTORY='/var/appdata/96/idx' MAX_ROWS=10 MIN_ROWS=5 TABLESPACE=tb1( SUBPARTITION sub1 STORAGE ENGINE=innodb))",
            output);

        sql =
            "create table if not exists t11 ( id int not null,year_col int8 not null, FULLTEXT index index4 (id(10)desc,(year_col+10)desc)) INDEX DIRECTORY ='/var/appdata/97/idx' delay_key_write=0 ENCRYPTION ='Y',ENGINE = innodb INSERT_METHOD = NO PARTITION BY LIST (id+year_col) (partition p1 values in (1,2) (SUBPARTITION  sub2 comment'sub2' DATA DIRECTORY ='/var/appdata/97/idx' INDEX DIRECTORY = '/var/appdata/97/idx' MAX_ROWS = 10 MIN_ROWS=5 TABLESPACE = tb1));"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col bigint NOT NULL,\n"
                + "FULLTEXT KEY index4(id(10) DESC,(year_col+10) DESC)\n"
                + ")ENGINE=innodb INDEX DIRECTORY='/var/appdata/97/idx' DELAY_KEY_WRITE=0 ENCRYPTION='Y' INSERT_METHOD=NO  PARTITION BY LIST(id+year_col)( PARTITION p1 VALUES IN (1,2)( SUBPARTITION sub2 COMMENT='sub2' DATA DIRECTORY='/var/appdata/97/idx' INDEX DIRECTORY='/var/appdata/97/idx' MAX_ROWS=10 MIN_ROWS=5 TABLESPACE=tb1))",
            output);

        sql =
            "create table if not exists t11 ( id int not null,year_col int not null,time_col int not null, spatial index index6 (id)invisible) KEY_BLOCK_SIZE =10 MAX_ROWS=10 MIN_ROWS=5 PACK_KEYS = 0 PASSWORD='pass' ROW_FORMAT default stats_auto_recalc = 1, STATS_PERSISTENT 1 STATS_SAMPLE_PAGES =100 TABLESPACE tb1 storage memory union = (t1,t2);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col int NOT NULL,\n"
                + "  time_col int NOT NULL,\n" + "SPATIAL KEY index6(id) INVISIBLE\n"
                + ")KEY_BLOCK_SIZE=10 MAX_ROWS=10 MIN_ROWS=5 PACK_KEYS=0 PASSWORD='pass' ROW_FORMAT=DEFAULT STATS_AUTO_RECALC=1 STATS_PERSISTENT=1 STATS_SAMPLE_PAGES=100 TABLESPACE tb1 STORAGE MEMORY UNION=(t1,t2) ",
            output);

        sql =
            "create table if not exists t11 ( id int not null,year_corl int not null ,CONSTRAINT PRIMARY KEY (id) using btree visible comment 'a.');"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_corl int NOT NULL,\n"
            + "  PRIMARY KEY (id) USING BTREE VISIBLE COMMENT 'a.'\n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,year_corl int not null ,CONSTRAINT testkey1 PRIMARY KEY (id) using btree visible comment 'a.');"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_corl int NOT NULL,\n"
            + "CONSTRAINT testkey1PRIMARY KEY (id) USING BTREE VISIBLE COMMENT 'a.'\n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,year_col int not null,CONSTRAINT aa UNIQUE index index7 using BTREE (id(8))visible);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col int NOT NULL,\n"
            + "CONSTRAINT aaUNIQUE KEY index7 USING BTREE (id(8)) VISIBLE\n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,year_col int not null,CONSTRAINT testa FOREIGN KEY index8 (id) REFERENCES t1(id,a) match full on delete RESTRICT);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n" + "  year_col int NOT NULL,\n"
                + "CONSTRAINT testaFOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH FULL ON DELETE RESTRICT \n" + ")",
            output);

        sql =
            "create table if not exists t11 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match PARTIAL on delete CASCADE  );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH PARTIAL ON DELETE CASCADE \n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match SIMPLE on delete SET NULL  );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH SIMPLE ON DELETE SET NULL \n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match SIMPLE on delete NO ACTION  );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH SIMPLE ON DELETE NO ACTION \n" + ")", output);

        sql =
            "create table if not exists t13 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match full on delete set default );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t13(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH FULL ON DELETE SET DEFAULT \n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match SIMPLE on delete NO ACTION  on update RESTRICT );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH SIMPLE ON DELETE NO ACTION ON UPDATE RESTRICT \n"
            + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match SIMPLE on update SET NULL  on delete RESTRICT );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH SIMPLE ON DELETE RESTRICT ON UPDATE SET NULL \n"
            + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match SIMPLE on update CASCADE  );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH SIMPLE ON UPDATE CASCADE \n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match SIMPLE on update SET DEFAULT  );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH SIMPLE ON UPDATE SET DEFAULT \n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,CONSTRAINT FOREIGN KEY index8 (id) REFERENCES t1(id,a) match SIMPLE on update NO ACTION  );"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "  FOREIGN KEY index8(id) REFERENCES t1(id,a) MATCH SIMPLE ON UPDATE NO ACTION \n" + ")", output);

        sql = "create table if not exists t11 ( id int not null,constraint testKey4 CHECK(a<100) not ENFORCED);"
            .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "CONSTRAINT testKey4 CHECK(a<100)NOT ENFORCED\n" + ")", output);

        sql =
            "create table if not exists t11 ( id int not null,constraint testKey4 CHECK(a<100) not ENFORCED,constraint testKey5 CHECK(a<200) not ENFORCED);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "CONSTRAINT testKey4 CHECK(a<100)NOT ENFORCED,\n" + "CONSTRAINT testKey5 CHECK(a<200)NOT ENFORCED\n"
            + ")", output);

        sql = "create table if not exists t11 ( id int not null,constraint testKey4 CHECK(a<100) not ENFORCED);"
            .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE IF NOT EXISTS t11(\n" + "  id int NOT NULL,\n"
            + "CONSTRAINT testKey4 CHECK(a<100)NOT ENFORCED\n" + ")", output);

        sql =
            "create table if not exists t11 ( id1 int not null default (UUID()) COLUMN_FORMAT DEFAULT,id2 int null default(RAND(10)),id3 int AUTO_INCREMENT UNIQUE KEY PRIMARY KEY COLUMN_FORMAT DYNAMIC,id4 int COMMENT 'str' COLLATE latin1_swedish_ci COLUMN_FORMAT FIXED STORAGE MEMORY,id5 int STORAGE DISK REFERENCES t1(id) MATCH FULL ON DELETE RESTRICT ON UPDATE RESTRICT constraint testKey8 CHECK(a<100) not ENFORCED,id6 int not null CHECK(a<500) not ENFORCED,constraint testKey4 CHECK(a<100) not ENFORCED);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLE IF NOT EXISTS t11(\n" + "  id1 int NOT NULL DEFAULT (UUID()) COLUMN_FORMAT DEFAULT,\n"
                + "  id2 int DEFAULT (RAND(10)),\n" + "  id3 int DEFAULT NULL AUTO_INCREMENT COLUMN_FORMAT DYNAMIC,\n"
                + "  id4 int COLLATE latin1_swedish_ci DEFAULT NULL COMMENT 'str' COLUMN_FORMAT FIXED STORAGE MEMORY,\n"
                + "  id5 int DEFAULT NULL STORAGE DISK REFERENCES t1(id) MATCH FULL ON DELETE RESTRICT ON UPDATE RESTRICT  CONSTRAINT testKey8 CHECK(a<100) NOT ENFORCED,\n"
                + "  id6 int NOT NULL CHECK(a<500) NOT ENFORCED,\n" + "  PRIMARY KEY (id3),\n"
                + "CONSTRAINT testKey4 CHECK(a<100)NOT ENFORCED\n" + ")", output);

        sql = "create table test01 (id int)global_unique=1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE test01(\n" + "  id int DEFAULT NULL\n" + ")", output);

        sql = "create table test01 (id int);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTableStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLE test01(\n" + "  id int DEFAULT NULL\n" + ")", output);
    }

    public void testCreateIndex() throws Exception {
        byte[] sql =
            "CREATE UNIQUE INDEX idx1 using btree ON t1 (id) key_block_size=9 VISIBLE ALGORITHM = default LOCK =NONE ;"
                .getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateIndexStatement create = (DDLCreateIndexStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE UNIQUE INDEX idx1 USING BTREE ON t1(id) KEY_BLOCK_SIZE=9 VISIBLE ALGORITHM=DEFAULT LOCK=NONE",
            output);

        sql =
            "CREATE fulltext index idx4 on t1(id,a,b) WITH PARSER ngram ALGORITHM = INPLACE LOCK =default;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateIndexStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FULLTEXT INDEX idx4 ON t1(id,a,b) WITH PARSER ngram ALGORITHM=INPLACE LOCK=DEFAULT",
            output);

        sql = "CREATE SPATIAL index idx5 on t1(id,a)  ALGORITHM = COPY LOCK =SHARED;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateIndexStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SPATIAL INDEX idx5 ON t1(id,a) ALGORITHM=COPY LOCK=SHARED", output);

        sql = "create index idx6 on t1(id desc)  using btree lock = exclusive algorithm = default;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateIndexStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE INDEX idx6 ON t1(id DESC) USING BTREE ALGORITHM=DEFAULT LOCK=EXCLUSIVE", output);

        sql = "create index idx6 using hash on t1(id asc)  using hash lock = exclusive;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateIndexStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE INDEX idx6 USING HASH ON t1(id) USING HASH LOCK=EXCLUSIVE", output);

        sql = "create index idx7 on t1(id) comment 'test' using hash lock = exclusive;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateIndexStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE INDEX idx7 ON t1(id) COMMENT 'test' USING HASH LOCK=EXCLUSIVE", output);

        sql = "create index idx8 on t1((id+10*RAND())) visible using btree;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateIndexStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE INDEX idx8 ON t1((id+10*RAND())) VISIBLE USING BTREE", output);

        sql = "create index idx8 on t1((id+10*RAND()),id,(id-RAND())) invisible using btree;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateIndexStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE INDEX idx8 ON t1((id+10*RAND()),id,(id-RAND())) INVISIBLE USING BTREE", output);
    }

    public void testCreateView() throws Exception {
        byte[] sql = "CREATE VIEW v_today (id,a,b) AS SELECT t1;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateViewStatement create = (DDLCreateViewStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE VIEW v_today(id,a,b) AS SELECT t1", output);

        sql = "CREATE OR REPLACE VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE OR REPLACE VIEW v_today(id,a,b) AS SELECT * FROM t1", output);

        sql = "CREATE ALGORITHM = UNDEFINED VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE ALGORITHM=UNDEFINED VIEW v_today(id,a,b) AS SELECT * FROM t1", output);

        sql = "CREATE ALGORITHM = MERGE VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE ALGORITHM=MERGE VIEW v_today(id,a,b) AS SELECT * FROM t1", output);

        sql = "CREATE ALGORITHM = TEMPTABLE VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE ALGORITHM=TEMPTABLE VIEW v_today(id,a,b) AS SELECT * FROM t1", output);

        sql = "CREATE DEFINER = 'root'@'localhost' VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DEFINER='root'@'localhost' VIEW v_today(id,a,b) AS SELECT * FROM t1", output);

        sql = "CREATE SQL SECURITY DEFINER VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SQL SECURITY DEFINER VIEW v_today(id,a,b) AS SELECT * FROM t1", output);

        sql = "CREATE SQL SECURITY INVOKER VIEW v_today (id,a,b)  AS SELECT * FROM t1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SQL SECURITY INVOKER VIEW v_today(id,a,b) AS SELECT * FROM t1", output);

        sql = "CREATE VIEW v_today (id,a,b) AS SELECT * from t1 WITH CHECK OPTION;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE VIEW v_today(id,a,b) AS SELECT * FROM t1 WITH CHECK OPTION", output);

        sql = "CREATE VIEW v_today (id,a,b) AS SELECT * from t1 WITH CASCADED CHECK OPTION;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE VIEW v_today(id,a,b) AS SELECT * FROM t1 WITH CASCADED CHECK OPTION", output);

        sql = "CREATE VIEW v_today (id,a,b) AS SELECT * from t1 WITH LOCAL CHECK OPTION;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE VIEW v_today(id,a,b) AS SELECT * FROM t1 WITH LOCAL CHECK OPTION", output);

        sql =
            "CREATE  OR REPLACE  ALGORITHM = UNDEFINED DEFINER = 'root'@'localhost' SQL SECURITY DEFINER  VIEW v_today (id,a,b) AS SELECT * from t1 WITH LOCAL CHECK OPTION;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateViewStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER='root'@'localhost' SQL SECURITY DEFINER VIEW v_today(id,a,b) AS SELECT * FROM t1 WITH LOCAL CHECK OPTION",
            output);
    }

    public void testCreateTrigger() throws Exception {
        byte[] sql =
            "CREATE TRIGGER ins_sum BEFORE INSERT ON account FOR EACH ROW SET @sum = @sum + NEW.amount;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateTriggerStatement create = (DDLCreateTriggerStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER ins_sum BEFORE INSERT ON account FOR EACH ROW SET @sum=@sum+NEW.amount",
            output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET @total_tax = (SELECT SUM(tax) FROM taxable_transactions); END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SET @total_tax=(SELECT SUM(tax) FROM taxable_transactions);\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN    SET @name = 43; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n" + "SET @name=43;\n"
                + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN   DECLARE counter INT DEFAULT 0; while1: WHILE counter < 10 DO    SET counter = counter + increment;  END WHILE while1; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "DECLARE counter int DEFAULT 0;\n" + "while1:WHILE counter<10 DO\n" + "SET counter=counter+increment;\n"
            + "END WHILE while1;\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET GLOBAL max_connections = 1000;SET @@GLOBAL.max_connections = 1000;SET SESSION sql_mode = 'TRADITIONAL';SET LOCAL sql_mode = 'TRADITIONAL';SET @@SESSION.sql_mode = 'TRADITIONAL';SET @@LOCAL.sql_mode = 'TRADITIONAL'; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
                + "SET @@GLOBAL.max_connections=1000;\n" + "SET @@GLOBAL.max_connections=1000;\n"
                + "SET @@SESSION.sql_mode='TRADITIONAL';\n" + "SET @@LOCAL.sql_mode='TRADITIONAL';\n"
                + "SET @@SESSION.sql_mode='TRADITIONAL';\n" + "SET @@LOCAL.sql_mode='TRADITIONAL';\n" + "END begin2",
            output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET @@sql_mode = 'TRADITIONAL';SET sql_mode = 'TRADITIONAL'; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SET @@sql_mode='TRADITIONAL';\n" + "SET sql_mode='TRADITIONAL';\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET PERSIST max_connections = 1000;SET @@PERSIST.max_connections = 1000; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SET @@PERSIST.max_connections=1000;\n" + "SET @@PERSIST.max_connections=1000;\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET PERSIST_ONLY max_connections = 1000;SET @@PERSIST_ONLY.max_connections = 1000; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SET @@PERSIST_ONLY.max_connections=1000;\n" + "SET @@PERSIST_ONLY.max_connections=1000;\n"
            + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET PERSIST_ONLY max_connections = 1000;SET @@PERSIST_ONLY.max_connections = 1000; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SET @@PERSIST_ONLY.max_connections=1000;\n" + "SET @@PERSIST_ONLY.max_connections=1000;\n"
            + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET @@SESSION.max_join_size = DEFAULT;SET @@SESSION.max_join_size = @@GLOBAL.max_join_size; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SET @@SESSION.max_join_size=DEFAULT;\n" + "SET @@SESSION.max_join_size=@@GLOBAL.max_join_size;\n"
            + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET @x = 1, SESSION sql_mode = '';SET GLOBAL sort_buffer_size = 1000000, SESSION sort_buffer_size = 1000000;SET @@GLOBAL.sort_buffer_size = 1000000, @@LOCAL.sort_buffer_size = 1000000;SET GLOBAL max_connections = 1000, sort_buffer_size = 1000000; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SET @x=1,@@SESSION.sql_mode='';\n"
            + "SET @@GLOBAL.sort_buffer_size=1000000,@@SESSION.sort_buffer_size=1000000;\n"
            + "SET @@GLOBAL.sort_buffer_size=1000000,@@LOCAL.sort_buffer_size=1000000;\n"
            + "SET @@GLOBAL.max_connections=1000,sort_buffer_size=1000000;\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SET @@GLOBAL.sort_buffer_size = 50000, sort_buffer_size = 1000000; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SET @@GLOBAL.sort_buffer_size=50000,sort_buffer_size=1000000;\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES  tg2 begin2: BEGIN SELECT @@GLOBAL.sql_mode, @@SESSION.sql_mode, @@sql_mode; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "SELECT @@GLOBAL.sql_mode,@@SESSION.sql_mode,@@sql_mode;\n" + "END begin2", output);

        sql =
            "create DEFINER  = 'root'@'localhost' trigger tg1 before insert on t1 for each row follows tg2 begin1: begin   end begin1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='root'@'localhost' TRIGGER tg1 BEFORE INSERT ON t1 FOR EACH ROW FOLLOWS tg2 begin1:BEGIN\n"
                + "END begin1", output);

        sql =
            "create DEFINER  = 'root'@'192.168.174.258' trigger tg1 before insert on t1 for each row follows tg2 begin1: begin   end begin1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='root'@'192.168.174.258' TRIGGER tg1 BEFORE INSERT ON t1 FOR EACH ROW FOLLOWS tg2 begin1:BEGIN\n"
                + "END begin1", output);

        sql =
            "CREATE TRIGGER testref BEFORE INSERT ON tes1 FOR EACH ROW begin2: BEGIN    INSERT INTO tes2 SET a2 = `NEW`.a1;    DELETE FROM tes3 WHERE a3 = NEW.a1;    UPDATE tes4 SET b4 = b4 + 1 WHERE a4 = NEW.a1;    begin1: begin\t\tinsert into tes2 set a2 = NEW.a1+1;    end ;  END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref BEFORE INSERT ON tes1 FOR EACH ROW begin2:BEGIN\n"
            + "INSERT INTO tes2 (a2)VALUES (`NEW`.a1);\n" + "DELETE FROM tes3 WHERE a3=NEW.a1;\n"
            + "UPDATE tes4 SET b4=b4+1 WHERE a4=NEW.a1;\n" + "begin1:BEGIN\n"
            + "INSERT INTO tes2 (a2)VALUES (NEW.a1+1);\n" + "END;\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref2 AFTER INSERT ON tes1 FOR EACH ROW begin2: BEGIN    INSERT INTO tes2 SET a2 = `NEW`.a1; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref2 AFTER INSERT ON tes1 FOR EACH ROW begin2:BEGIN\n"
            + "INSERT INTO tes2 (a2)VALUES (`NEW`.a1);\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER UPDATE ON tes1 FOR EACH ROW begin2: BEGIN    INSERT INTO tes2 SET a2 = `NEW`.a1; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER UPDATE ON tes1 FOR EACH ROW begin2:BEGIN\n"
            + "INSERT INTO tes2 (a2)VALUES (`NEW`.a1);\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW begin2: BEGIN    INSERT INTO tes2 SET a2 = `NEW`.a1; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW begin2:BEGIN\n"
            + "INSERT INTO tes2 (a2)VALUES (`NEW`.a1);\n" + "END begin2", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW follows tg2  BEGIN    INSERT INTO tes2 SET a2 = `NEW`.a1; END;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW FOLLOWS tg2 BEGIN\n"
            + "INSERT INTO tes2 (a2)VALUES (`NEW`.a1);\n" + "END", output);

        sql =
            "CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2: BEGIN    INSERT INTO tes2 SET a2 = `NEW`.a1; END begin2;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTriggerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TRIGGER testref3 AFTER DELETE ON tes1 FOR EACH ROW PRECEDES tg2 begin2:BEGIN\n"
            + "INSERT INTO tes2 (a2)VALUES (`NEW`.a1);\n" + "END begin2", output);
    }

    public void testCreateProcedure() throws Exception {
        byte[] sql =
            "CREATE PROCEDURE simpleproc (OUT param1 INT) BEGIN SELECT COUNT(*) INTO param1 FROM t; END".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateProcedureStatement create = (DDLCreateProcedureStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE PROCEDURE simpleproc(OUT param1 int) BEGIN\n" + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END",
            output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)COMMENT 'Dagon' BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) COMMENT 'Dagon' BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)LANGUAGE SQL BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) LANGUAGE SQL BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)DETERMINISTIC BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) DETERMINISTIC BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)NOT DETERMINISTIC BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) NOT DETERMINISTIC BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)CONTAINS SQL BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) CONTAINS SQL BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)NO SQL BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) NO SQL BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)READS SQL DATA BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) READS SQL DATA BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)MODIFIES SQL DATA BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) MODIFIES SQL DATA BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)SQL SECURITY DEFINER BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) SQL SECURITY DEFINER BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)SQL SECURITY INVOKER BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) SQL SECURITY INVOKER BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            " CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc (OUT param1 INT,IN param2 INT)COMMENT 'Dagon' SQL SECURITY INVOKER  MODIFIES SQL DATA BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateProcedureStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' PROCEDURE simpleproc(OUT param1 int,IN param2 int) COMMENT 'Dagon' SQL SECURITY INVOKER MODIFIES SQL DATA BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);
    }

    public void testCreateFunction() throws Exception {
        byte[] sql =
            "CREATE FUNCTION hello (s CHAR(20))RETURNS CHAR(50) DETERMINISTIC COMMENT 'Dagon' BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateFunctionStatement create = (DDLCreateFunctionStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20))RETURNS char(50) DETERMINISTIC COMMENT 'Dagon' BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE DEFINER='admin'@'localhost' FUNCTION hello (s CHAR(20))RETURNS CHAR(50) COMMENT 'Dagon' BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' FUNCTION hello(s char(20))RETURNS char(50) COMMENT 'Dagon' BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE DEFINER='admin'@'localhost' FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) LANGUAGE SQL BEGIN SELECT COUNT(*) INTO param1 FROM t; END"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' FUNCTION hello(s char(20),param int)RETURNS char(50) LANGUAGE SQL BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) DETERMINISTIC BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) DETERMINISTIC BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) NOT DETERMINISTIC BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) NOT DETERMINISTIC BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) NOT DETERMINISTIC BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) NOT DETERMINISTIC BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) CONTAINS SQL BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) CONTAINS SQL BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) NO SQL BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) NO SQL BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) READS SQL DATA BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) READS SQL DATA BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) MODIFIES SQL DATA BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) MODIFIES SQL DATA BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) SQL SECURITY DEFINER BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) SQL SECURITY DEFINER BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50) SQL SECURITY INVOKER BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE FUNCTION hello(s char(20),param int)RETURNS char(50) SQL SECURITY INVOKER BEGIN\n"
            + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);

        sql =
            "CREATE DEFINER='admin'@'localhost' FUNCTION hello (s CHAR(20),param INT)RETURNS CHAR(50)COMMENT 'Dagon' MODIFIES SQL DATA SQL SECURITY INVOKER BEGIN SELECT COUNT(*) INTO param1 FROM t; END "
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateFunctionStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='admin'@'localhost' FUNCTION hello(s char(20),param int)RETURNS char(50) COMMENT 'Dagon' MODIFIES SQL DATA SQL SECURITY INVOKER BEGIN\n"
                + "SELECT COUNT(*) INTO param1 FROM t;\n" + "END", output);
    }

    public void testCreateDatabase() throws Exception {
        byte[] sql = "CREATE DATABASE test;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateDatabaseStatement create = (DDLCreateDatabaseStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DATABASE test", output);

        sql = "CREATE SCHEMA IF NOT EXISTS test;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateDatabaseStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DATABASE IF NOT EXISTS test", output);

        sql = "CREATE DATABASE IF NOT EXISTS test CHARACTER SET = 'test';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateDatabaseStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DATABASE IF NOT EXISTS test CHARACTER SET 'test'", output);

        sql = "CREATE DATABASE IF NOT EXISTS test DEFAULT CHARACTER SET 'test';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateDatabaseStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DATABASE IF NOT EXISTS test CHARACTER SET 'test'", output);

        sql = "CREATE DATABASE IF NOT EXISTS test COLLATE das;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateDatabaseStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DATABASE IF NOT EXISTS test COLLATE das", output);

        sql = "CREATE DATABASE IF NOT EXISTS test DEFAULT COLLATE = 'daf';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateDatabaseStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DATABASE IF NOT EXISTS test COLLATE 'daf'", output);

        sql = "CREATE SCHEMA IF NOT EXISTS test DEFAULT ENCRYPTION = 'Y';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateDatabaseStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DATABASE IF NOT EXISTS test DEFAULT ENCRYPTION 'Y'", output);

        sql = "CREATE SCHEMA IF NOT EXISTS test DEFAULT ENCRYPTION = 'n';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateDatabaseStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE DATABASE IF NOT EXISTS test DEFAULT ENCRYPTION 'N'", output);
    }

    public void testCreateEvent() throws Exception {
        byte[] sql =
            "CREATE DEFINER = 'root@192.168.174.131' EVENT IF NOT EXISTS event_name ON SCHEDULE EVERY 1 YEAR DO UPDATE aaaa.test SET t1 = t1 + 1;"
                .getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateEventStatement create = (DDLCreateEventStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='root@192.168.174.131' EVENT IF NOT EXISTS event_name ON SCHEDULE EVERY 1 YEAR DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 YEAR DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 YEAR DO UPDATE aaaa.test SET t1=t1+1", output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 QUARTER DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 QUARTER DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 MONTH DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 MONTH DO UPDATE aaaa.test SET t1=t1+1", output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY DO UPDATE aaaa.test SET t1=t1+1", output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 HOUR DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 HOUR DO UPDATE aaaa.test SET t1=t1+1", output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 MINUTE DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 MINUTE DO UPDATE aaaa.test SET t1=t1+1", output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 WEEK DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 WEEK DO UPDATE aaaa.test SET t1=t1+1", output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 SECOND DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 SECOND DO UPDATE aaaa.test SET t1=t1+1", output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 YEAR_MONTH DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 YEAR_MONTH DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY_HOUR DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY_HOUR DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY_MINUTE DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY_MINUTE DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY_SECOND DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY_SECOND DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 HOUR_MINUTE DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 HOUR_MINUTE DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 HOUR_SECOND DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 HOUR_SECOND DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql =
            "CREATE EVENT event_name ON SCHEDULE EVERY 1 MINUTE_SECOND DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 MINUTE_SECOND DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql =
            "CREATE EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP + INTERVAL 4 HOUR DO UPDATE aaaa.test SET t1 = t1 + 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP+INTERVAL 4 HOUR DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql =
            "CREATE EVENT event_name ON SCHEDULE EVERY 12 HOUR ENDS CURRENT_TIMESTAMP + INTERVAL 4 HOUR  + INTERVAL 4 HOUR DO UPDATE aaaa.test SET t1 = t1 + 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE EVENT event_name ON SCHEDULE EVERY 12 HOUR ENDS CURRENT_TIMESTAMP+INTERVAL 4 HOUR+INTERVAL 4 HOUR DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql =
            "CREATE EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP + INTERVAL 4 HOUR ENDS CURRENT_TIMESTAMP + INTERVAL 4 HOUR  + INTERVAL 4 HOUR DO UPDATE aaaa.test SET t1=t1+1"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP+INTERVAL 4 HOUR ENDS CURRENT_TIMESTAMP+INTERVAL 4 HOUR+INTERVAL 4 HOUR DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql =
            "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY ON COMPLETION PRESERVE DO UPDATE aaaa.test SET t1 = t1 + 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY ON COMPLETION PRESERVE DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql =
            "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY ON COMPLETION NOT PRESERVE DO UPDATE aaaa.test SET t1 = t1 + 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY ON COMPLETION NOT PRESERVE DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY ENABLE DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY ENABLE DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY DISABLE DO UPDATE aaaa.test SET t1 = t1 + 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY DISABLE DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY DISABLE ON SLAVE DO UPDATE aaaa.test SET t1 = t1 + 1;"
            .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY DISABLE ON SLAVE DO UPDATE aaaa.test SET t1=t1+1", output);

        sql = "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY COMMENT \"tsring\" DO UPDATE aaaa.test SET t1 = t1 + 1;"
            .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE EVENT event_name ON SCHEDULE EVERY 1 DAY COMMENT \"tsring\" DO UPDATE aaaa.test SET t1=t1+1",
            output);

        sql =
            "CREATE DEFINER = 'root'@'localhost'  EVENT event_name ON SCHEDULE  EVERY 12 HOUR STARTS CURRENT_TIMESTAMP() + INTERVAL 4 HOUR ENDS '2099-02-10 23:59:00' + INTERVAL 4 HOUR ON COMPLETION PRESERVE  ENABLE COMMENT 'string' DO UPDATE aaaa.test SET t1 = t1 + 1;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateEventStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE DEFINER='root'@'localhost' EVENT event_name ON SCHEDULE EVERY 12 HOUR STARTS CURRENT_TIMESTAMP()+INTERVAL 4 HOUR ENDS '2099-02-10 23:59:00'+INTERVAL 4 HOUR ON COMPLETION PRESERVE ENABLE COMMENT 'string' DO UPDATE aaaa.test SET t1=t1+1",
            output);
    }

    public void testCreateLogFileGroup() throws Exception {
        byte[] sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateLogfileGroupStatement create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file'", output);

        sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' INITIAL_SIZE = 10M;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' INITIAL_SIZE=10M", output);

        sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' INITIAL_SIZE 10M;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' INITIAL_SIZE=10M", output);

        sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' UNDO_BUFFER_SIZE = 10M;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' UNDO_BUFFER_SIZE=10M", output);

        sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' REDO_BUFFER_SIZE = 10M;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' REDO_BUFFER_SIZE=10M", output);

        sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' NODEGROUP = 10;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' NODEGROUP=10", output);

        sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' WAIT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' WAIT", output);

        sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' COMMENT = 'asdasd';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' COMMENT='asdasd'", output);

        sql = "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' ENGINE = NDB;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' ENGINE=NDB", output);

        sql =
            "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' NODEGROUP = 10 ENGINE = NDB initial_size=10M WAIT COMMENT ='adasd';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateLogfileGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE LOGFILE GROUP logile_group ADD UNDOFILE 'undo_file' INITIAL_SIZE=10M NODEGROUP=10 WAIT COMMENT='adasd' ENGINE=NDB",
            output);
    }

    public void testCreateResource() throws Exception {
        byte[] sql = "CREATE RESOURCE GROUP group_name TYPE=SYSTEM;".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DALCreateResourceGroupStatement create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=SYSTEM", output);

        sql = "CREATE RESOURCE GROUP group_name TYPE=USER;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=USER", output);

        sql = "CREATE RESOURCE GROUP group_name TYPE=SYSTEM VCPU=1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=SYSTEM VCPU=1", output);

        sql = "CREATE RESOURCE GROUP group_name TYPE=SYSTEM VCPU=0,1,2,3,9,10;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=SYSTEM VCPU=0,1,2,3,9,10", output);

        sql = "CREATE RESOURCE GROUP group_name TYPE=SYSTEM VCPU=0-3,9-10;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=SYSTEM VCPU=0-3,9-10", output);

        sql = "CREATE RESOURCE GROUP group_name TYPE=SYSTEM THREAD_PRIORITY= 10;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=SYSTEM THREAD_PRIORITY=10", output);

        sql = "CREATE RESOURCE GROUP group_name TYPE=SYSTEM ENABLE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=SYSTEM ENABLE", output);

        sql = "CREATE RESOURCE GROUP group_name TYPE=SYSTEM DISABLE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=SYSTEM DISABLE", output);

        sql = "CREATE RESOURCE GROUP group_name TYPE=SYSTEM VCPU=0,1,2,3,9,10 THREAD_PRIORITY= 10 ENABLE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateResourceGroupStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE RESOURCE GROUP group_name TYPE=SYSTEM VCPU=0,1,2,3,9,10 THREAD_PRIORITY=10 ENABLE",
            output);
    }

    public void testCreateRole() throws Exception {
        byte[] sql = "CREATE ROLE 'administrator','developer';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DALCreateRoleStatement create = (DALCreateRoleStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE ROLE 'administrator','developer'", output);

        sql = "CREATE ROLE 'webapp'@'localhost';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateRoleStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE ROLE 'webapp'@'localhost'", output);
    }

    public void testCreateServer() throws Exception {
        byte[] sql = "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(HOST '192.168.174.100');".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateServerStatement create = (DDLCreateServerStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(HOST '192.168.174.100')", output);

        sql = "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(DATABASE 'test');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateServerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(DATABASE 'test')", output);

        sql = "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(USER 'root');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateServerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(USER 'root')", output);

        sql = "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(PASSWORD 'allalala');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateServerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(PASSWORD 'allalala')", output);

        sql = "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(SOCKET '/var/run/mysqld/mysqld.sock');"
            .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateServerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(SOCKET '/var/run/mysqld/mysqld.sock')", output);

        sql = "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(OWNER 'root');".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateServerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(OWNER 'root')", output);

        sql = "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(PORT 3306);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateServerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(PORT 3306)", output);

        sql =
            "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(PASSWORD 'allalala',HOST '192.168.174.100',DATABASE 'test',PORT 3306);"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateServerStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE SERVER server_name FOREIGN DATA WRAPPER das OPTIONS(PASSWORD 'allalala',HOST '192.168.174.100',DATABASE 'test',PORT 3306)",
            output);
    }

    public void testCreateSpatialReferenceSystem() throws Exception {
        byte[] sql = "CREATE SPATIAL REFERENCE SYSTEM 4120 NAME 'srs_name';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateSpatialReferenceSystemStatement create = (DDLCreateSpatialReferenceSystemStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SPATIAL REFERENCE SYSTEM 4120 NAME 'srs_name'", output);

        sql = "CREATE SPATIAL REFERENCE SYSTEM 4120 DEFINITION 'definition';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateSpatialReferenceSystemStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SPATIAL REFERENCE SYSTEM 4120 DEFINITION 'definition'", output);

        sql = "CREATE SPATIAL REFERENCE SYSTEM 4120 ORGANIZATION 'org_name' identified by 4212;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateSpatialReferenceSystemStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SPATIAL REFERENCE SYSTEM 4120 ORGANIZATION 'org_name' IDENTIFIED BY 4212", output);

        sql = "CREATE SPATIAL REFERENCE SYSTEM 4120 description 'asdasd';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateSpatialReferenceSystemStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SPATIAL REFERENCE SYSTEM 4120 DESCRIPTION 'asdasd'", output);

        sql =
            "CREATE SPATIAL REFERENCE SYSTEM 4120 NAME 'srs_name' DEFINITION 'definition' ORGANIZATION 'org_name' IDENTIFIED BY 4212 description 'asdasd';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateSpatialReferenceSystemStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE SPATIAL REFERENCE SYSTEM 4120 NAME 'srs_name' DEFINITION 'definition' ORGANIZATION 'org_name' IDENTIFIED BY 4212 DESCRIPTION 'asdasd'",
            output);

        sql = "CREATE SPATIAL REFERENCE SYSTEM IF NOT EXISTS 4120 NAME 'srs_name';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateSpatialReferenceSystemStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE SPATIAL REFERENCE SYSTEM IF NOT EXISTS 4120 NAME 'srs_name'", output);

        sql = "CREATE OR REPLACE SPATIAL REFERENCE SYSTEM 4120 NAME 'srs_name';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateSpatialReferenceSystemStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE OR REPLACE SPATIAL REFERENCE SYSTEM 4120 NAME 'srs_name'", output);
    }

    public void testCreateTablespace() throws Exception {
        byte[] sql = "CREATE UNDO TABLESPACE tablespace_name ADD DATAFILE 'file_name';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DDLCreateTablespaceStatement create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE UNDO TABLESPACE tablespace_name ADD DATAFILE 'file_name'", output);

        sql = "CREATE TABLESPACE tablespace_name FILE_BLOCK_SIZE = 8912;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLESPACE tablespace_name FILE_BLOCK_SIZE=8912", output);

        sql = "CREATE TABLESPACE tablespace_name ENCRYPTION = 'Y';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLESPACE tablespace_name ENCRYPTION='Y'", output);

        sql = "CREATE TABLESPACE tablespace_name ENCRYPTION='N'".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLESPACE tablespace_name ENCRYPTION='N'", output);

        sql = "CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group", output);

        sql = "CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group EXTENT_SIZE = 10M;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group EXTENT_SIZE=10M", output);

        sql = "CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group INITIAL_SIZE 10K;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group INITIAL_SIZE=10K", output);

        sql = "CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group AUTOEXTEND_SIZE 10M;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group AUTOEXTEND_SIZE=10M",
            output);

        sql = "CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group NODEGROUP 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group NODEGROUP=1", output);

        sql = "CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group WAIT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group WAIT", output);

        sql = "CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group COMMENT = 'asdas';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group COMMENT='asdas'", output);

        sql = "CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group ENGINE = InnoDB;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE TABLESPACE tablespace_name USE LOGFILE GROUP logfile_group ENGINE=InnoDB", output);

        sql = "CREATE UNDO TABLESPACE tablespace_name ADD DATAFILE 'file_name' ENGINE = NDB;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE UNDO TABLESPACE tablespace_name ADD DATAFILE 'file_name' ENGINE=NDB", output);

        sql =
            "CREATE TABLESPACE tablespace_name ADD DATAFILE 'file_name' FILE_BLOCK_SIZE = 8912 ENCRYPTION = 'Y' ENGINE = NDB;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLESPACE tablespace_name ADD DATAFILE 'file_name' FILE_BLOCK_SIZE=8912 ENCRYPTION='Y' ENGINE=NDB",
            output);

        sql =
            "CREATE TABLESPACE tablespace_name ADD DATAFILE 'file_name' USE LOGFILE GROUP logfile_group EXTENT_SIZE = 10M WAIT ENGINE = NDB;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DDLCreateTablespaceStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE TABLESPACE tablespace_name ADD DATAFILE 'file_name' USE LOGFILE GROUP logfile_group EXTENT_SIZE=10M WAIT ENGINE=NDB",
            output);
    }

    public void testCreateUser() throws Exception {
        byte[] sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string';".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DALCreateUserStatement create = (DALCreateUserStatement)tuple._1().get(0);
        String output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string'", output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED WITH auth_plugin;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED WITH auth_plugin", output);

        sql =
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string' DEFAULT ROLE da;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED WITH auth_plugin BY 'auth_string' DEFAULT ROLE da", output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED WITH auth_plugin AS 'auth_string';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED WITH auth_plugin AS 'auth_string'", output);

        sql =
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' ,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' ,das2 IDENTIFIED WITH auth_plugin AS 'auth_string';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string',dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string',das2 IDENTIFIED WITH auth_plugin AS 'auth_string'",
            output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE NONE;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE NONE", output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE SSL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE SSL", output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE CIPHER 'cipher';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE CIPHER 'cipher'",
            output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE ISSUER 'issuer';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE ISSUER 'issuer'",
            output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE SUBJECT 'subject';".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE SUBJECT 'subject'",
            output);

        sql =
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject';"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject'",
            output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_QUERIES_PER_HOUR 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_QUERIES_PER_HOUR 1",
            output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_UPDATES_PER_HOUR 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_UPDATES_PER_HOUR 1",
            output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_CONNECTIONS_PER_HOUR 1;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        Assert
            .assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_CONNECTIONS_PER_HOUR 1",
                output);

        sql = "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_USER_CONNECTIONS 2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_USER_CONNECTIONS 2",
            output);

        sql =
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10",
            output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE DEFAULT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE DEFAULT", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE NEVER ;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE NEVER", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE INTERVAL 2 day;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD EXPIRE INTERVAL 2 DAY", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD HISTORY DEFAULT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD HISTORY DEFAULT", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD HISTORY 2;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD HISTORY 2", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD REUSE INTERVAL 2 day;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD REUSE INTERVAL 2 DAY", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD REUSE INTERVAL DEFAULT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD REUSE INTERVAL DEFAULT", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD REQUIRE CURRENT DEFAULT;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD REQUIRE CURRENT DEFAULT", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD REQUIRE CURRENT OPTIONAL;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' PASSWORD REQUIRE CURRENT OPTIONAL", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string'  ACCOUNT LOCK;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' ACCOUNT LOCK", output);

        sql = "CREATE USER dagon IDENTIFIED BY 'auth_string'  ACCOUNT UNLOCK;".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals("CREATE USER dagon IDENTIFIED BY 'auth_string' ACCOUNT UNLOCK", output);

        sql =
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string' ,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10 PASSWORD REQUIRE CURRENT DEFAULT;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE USER IF NOT EXISTS dagon IDENTIFIED BY 'auth_string',dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10 PASSWORD REQUIRE CURRENT DEFAULT",
            output);

        sql =
            "CREATE USER dagon IDENTIFIED BY 'auth_string' ,dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10 ACCOUNT UNLOCK;"
                .getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        create = (DALCreateUserStatement)tuple._1().get(0);
        output = outputMySQL(create, sql);
        TestCase.assertEquals(
            "CREATE USER dagon IDENTIFIED BY 'auth_string',dasd1 IDENTIFIED WITH auth_plugin BY 'auth_string' REQUIRE CIPHER 'cipher' AND ISSUER 'issuer' AND SUBJECT 'subject' AND SUBJECT 'subject' WITH MAX_QUERIES_PER_HOUR 1 MAX_CONNECTIONS_PER_HOUR 1 MAX_USER_CONNECTIONS 2 MAX_QUERIES_PER_HOUR 10 ACCOUNT UNLOCK",
            output);
    }
}
