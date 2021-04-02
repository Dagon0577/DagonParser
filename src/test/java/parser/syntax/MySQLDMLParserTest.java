package parser.syntax;

import junit.framework.TestCase;
import parser.ast.expression.ComparisionExpression;
import parser.ast.expression.Expression;
import parser.ast.expression.LogicalExpression;
import parser.ast.expression.primary.Identifier;
import parser.ast.fragment.GroupBy;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.tableref.IndexHint;
import parser.ast.fragment.tableref.Join;
import parser.ast.fragment.tableref.Subquery;
import parser.ast.fragment.tableref.Table;
import parser.ast.fragment.tableref.TableReference;
import parser.ast.fragment.tableref.Tables;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.dml.DMLDoStatement;
import parser.ast.stmt.dml.DMLSelectStatement;
import parser.lexer.Lexer;
import parser.util.ListUtil;
import mysql.charset.MySqlCharset;
import parser.util.Pair;
import parser.util.Tuple2;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

/**
 *
 * @author Dagon0577
 * @date 2021年04月02日
 *
 */
public class MySQLDMLParserTest extends AbstractSyntaxTest {

    protected MySQLDMLParser getDMLParser(Lexer lexer) {
        ExprParser exp = new ExprParser(lexer);
        return new MySQLDMLSelectParser(lexer, exp);
    }

    public void testOrderBy() throws SQLSyntaxErrorException {
        byte[] sql = "order by c1 asc, c2 desc  , c3 ".getBytes();
        Lexer lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        MySQLDMLParser parser = getDMLParser(lexer);
        OrderBy orderBy = parser.orderBy();
        String output = outputMySQL(orderBy, sql);
        ListUtil.isEquals(
                ListUtil.createList(
                        new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), true),
                        new Pair<Expression, Boolean>(new Identifier(null, "c2".getBytes()), false),
                        new Pair<Expression, Boolean>(new Identifier(null, "c3".getBytes()), true)),
                orderBy.getOrderByList());
        TestCase.assertEquals("ORDER BY c1,c2 DESC,c3", output);

        sql = "order by c1   ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        orderBy = parser.orderBy();
        output = outputMySQL(orderBy, sql);
        ListUtil.isEquals(
                ListUtil.createList(
                        new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), true)),
                orderBy.getOrderByList());
        TestCase.assertEquals("ORDER BY c1", output);

        sql = "order by c1 asc  ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        orderBy = parser.orderBy();
        output = outputMySQL(orderBy, sql);
        ListUtil.isEquals(
                ListUtil.createList(
                        new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), true)),
                orderBy.getOrderByList());
        TestCase.assertEquals("ORDER BY c1", output);

        sql = "order by c1 desc  ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        orderBy = parser.orderBy();
        output = outputMySQL(orderBy, sql);
        ListUtil.isEquals(ListUtil.createList(
                new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), false)),
                orderBy.getOrderByList());
        TestCase.assertEquals("ORDER BY c1 DESC", output);


    }

    public void testLimit() throws SQLSyntaxErrorException {
        byte[] sql = "limit 1,2".getBytes();
        Lexer lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        MySQLDMLParser parser = getDMLParser(lexer);
        Limit limit = parser.limit(lexer.getOffset() > 0);
        String output = outputMySQL(limit, sql);
        TestCase.assertEquals(1, limit.getOffset());
        TestCase.assertEquals(2, limit.getSize());
        TestCase.assertEquals("LIMIT 1,2", output);

        sql = "limit 9 f".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        limit = parser.limit(lexer.getOffset() > 0);
        output = outputMySQL(limit, sql);
        TestCase.assertEquals(0, limit.getOffset());
        TestCase.assertEquals(9, limit.getSize());
        TestCase.assertEquals("LIMIT 9", output);

        sql = "limit 9 ofFset 0".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        limit = parser.limit(lexer.getOffset() > 0);
        output = outputMySQL(limit, sql);
        TestCase.assertEquals(0, limit.getOffset());
        TestCase.assertEquals(9, limit.getSize());
        TestCase.assertEquals("LIMIT 9", output);

    }

    public void testGroupBy() throws SQLSyntaxErrorException {
        byte[] sql = "group by c1 asc, c2 desc  , c3 with rollup".getBytes();
        Lexer lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        MySQLDMLParser parser = getDMLParser(lexer);
        GroupBy groupBy = parser.groupBy();
        String output = outputMySQL(groupBy, sql);
        ListUtil.isEquals(
                ListUtil.createList(
                        new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), true),
                        new Pair<Expression, Boolean>(new Identifier(null, "c2".getBytes()), false),
                        new Pair<Expression, Boolean>(new Identifier(null, "c3".getBytes()), true)),
                groupBy.getOrderByList());
        TestCase.assertEquals("GROUP BY c1,c2 DESC,c3 WITH ROLLUP", output);

        sql = "group by c1 asc, c2 desc  , c3 ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        groupBy = parser.groupBy();
        output = outputMySQL(groupBy, sql);
        ListUtil.isEquals(
                ListUtil.createList(
                        new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), true),
                        new Pair<Expression, Boolean>(new Identifier(null, "c2".getBytes()), false),
                        new Pair<Expression, Boolean>(new Identifier(null, "c3".getBytes()), true)),
                groupBy.getOrderByList());
        TestCase.assertEquals("GROUP BY c1,c2 DESC,c3", output);

        sql = "group by c1   ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        groupBy = parser.groupBy();
        output = outputMySQL(groupBy, sql);
        ListUtil.isEquals(
                ListUtil.createList(
                        new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), true)),
                groupBy.getOrderByList());
        TestCase.assertEquals("GROUP BY c1", output);

        sql = "group by c1 asc  ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        groupBy = parser.groupBy();
        output = outputMySQL(groupBy, sql);
        ListUtil.isEquals(
                ListUtil.createList(
                        new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), true)),
                groupBy.getOrderByList());
        TestCase.assertEquals("GROUP BY c1", output);

        sql = "group by c1 desc  ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        groupBy = parser.groupBy();
        output = outputMySQL(groupBy, sql);
        ListUtil.isEquals(ListUtil.createList(
                new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), false)),
                groupBy.getOrderByList());
        TestCase.assertEquals("GROUP BY c1 DESC", output);

        sql = "group by c1 with rollup  ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        groupBy = parser.groupBy();
        output = outputMySQL(groupBy, sql);
        ListUtil.isEquals(
                ListUtil.createList(
                        new Pair<Expression, Boolean>(new Identifier(null, "c1".getBytes()), true)),
                groupBy.getOrderByList());
        TestCase.assertEquals("GROUP BY c1 WITH ROLLUP", output);
    }

    public void testTR1() throws SQLSyntaxErrorException {
        byte[] sql = "(select * from  `select`) as `select`".getBytes();
        Lexer lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        MySQLDMLParser parser = getDMLParser(lexer);
        Tables trs = parser.tableRefs();
        String output = outputMySQL(trs, sql);
        List<TableReference> list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Subquery.class, list.get(0).getClass());
        TestCase.assertEquals("(SELECT * FROM `select`) AS `select`", output);

        sql = "(((selecT * from `any`)union (select  `select` from  `from` order by dd)) as 'a1', (((t2)))), t3"
                .getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(2, list.size());
        TestCase.assertEquals(Tables.class, list.get(0).getClass());
        TestCase.assertEquals(Table.class, list.get(1).getClass());
        list = ((Tables) list.get(0)).getTables();
        TestCase.assertEquals(2, list.size());
        TestCase.assertEquals(Subquery.class, list.get(0).getClass());
        TestCase.assertEquals(Tables.class, list.get(1).getClass());
        TestCase.assertEquals(
                "((SELECT * FROM `any`) UNION (SELECT `select` FROM `from` ORDER BY dd)) AS 'a1',t2,t3",
                output);

        sql = "(t1)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TableReference tr = list.get(0);
        TestCase.assertEquals(Tables.class, tr.getClass());
        list = ((Tables) tr).getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("t1", output);

        sql = "(t1,t2,(t3))".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Tables.class, list.get(0).getClass());
        tr = list.get(0);
        TestCase.assertEquals(Tables.class, tr.getClass());
        list = ((Tables) tr).getTables();
        TestCase.assertEquals(3, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals(Table.class, list.get(1).getClass());
        TestCase.assertEquals(Table.class, list.get(1).getClass());
        TestCase.assertEquals("t1,t2,t3", output);

        sql = "(tb1 as t1)inner join (tb2 as t2) on t1.name=t2.name".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        tr = ((Join) list.get(0)).getLeftTable();
        TestCase.assertEquals(Tables.class, tr.getClass());
        list = ((Tables) tr).getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        Expression ex = ((Join) (trs.getTables()).get(0)).getOnCond();
        TestCase.assertEquals(ex.getClass(), ComparisionExpression.class);
        TestCase.assertEquals("(tb1 AS t1) INNER JOIN (tb2 AS t2) ON t1.name=t2.name",
                output);

        sql = "(tb1 as t1)inner join tb2 as t2 using (c1)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        List<Identifier> using_list = ((Join) (trs.getTables()).get(0)).getUsing();
        TestCase.assertEquals(1, using_list.size());
        TestCase.assertEquals("(tb1 AS t1) INNER JOIN tb2 AS t2 USING (c1)", output);

        sql = "(tb1 as t1)inner join tb2 as t2 using (c1,c2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        using_list = ((Join) (trs.getTables()).get(0)).getUsing();
        TestCase.assertEquals(2, using_list.size());
        TestCase.assertEquals("(tb1 AS t1) INNER JOIN tb2 AS t2 USING (c1,c2)", output);

        sql = "tb1 as t1 use index (i1,i2,i3)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        List<IndexHint> hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        IndexHint indexhint = hintlist.get(0);
        TestCase.assertEquals(3, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals("tb1 AS t1 USE INDEX (i1,i2,i3)", output);

        sql = "tb1 as t1 use index (i1,i2,i3),tb2 as t2 use index (i1,i2,i3)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(2, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(3, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        hintlist = ((Table) (trs.getTables()).get(1)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(3, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(
                "tb1 AS t1 USE INDEX (i1,i2,i3),tb2 AS t2 USE INDEX (i1,i2,i3)",
                output);

        sql = "tb1 as t1".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertEquals("t1", ((Table) (trs.getTables()).get(0)).getAlias());
        TestCase.assertEquals("tb1 AS t1", output);

        sql = "tb1 t1".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertEquals("t1", ((Table) (trs.getTables()).get(0)).getAlias());
        TestCase.assertEquals("tb1 AS t1", output);

        sql = "tb1,tb2,tb3".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(3, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertEquals(null, ((Table) (trs.getTables()).get(0)).getAlias());
        TestCase.assertEquals("tb2",
                new String(((Table) (trs.getTables()).get(1)).getTable().getIdText()));
        TestCase.assertEquals("tb3",
                new String(((Table) (trs.getTables()).get(2)).getTable().getIdText()));
        TestCase.assertEquals("tb1,tb2,tb3", output);

        sql = "tb1 use key for join (i1,i2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertFalse(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_JOIN, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 USE KEY FOR JOIN(i1,i2)", output);

        sql = "tb1 use index for group by(i1,i2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_GROUP_BY, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 USE INDEX FOR GROUP BY (i1,i2)", output);

        sql = ("tb1 use key for order by (i1,i2) use key for group by () "
                + "ignore index for group by (i1,i2)").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertNull(((Table) (trs.getTables()).get(0)).getAlias());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(3, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertFalse(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_ORDER_BY, indexhint.getScope().intValue());
        indexhint = hintlist.get(1);
        TestCase.assertEquals(0, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertFalse(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_GROUP_BY, indexhint.getScope().intValue());
        indexhint = hintlist.get(2);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_IGNORE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_GROUP_BY, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 USE KEY FOR ORDER BY (i1,i2) "
                + "USE KEY FOR GROUP BY () IGNORE INDEX FOR GROUP BY (i1,i2)", output);

        sql = "tb1 use index for order by (i1,i2) force index for group by (i1)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertNull(((Table) (trs.getTables()).get(0)).getAlias());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(2, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_ORDER_BY, indexhint.getScope().intValue());
        indexhint = hintlist.get(1);
        TestCase.assertEquals(1, indexhint.getIndexList().size());
        TestCase.assertEquals(IndexHint.ACTION_FORCE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_GROUP_BY, indexhint.getScope().intValue());
        TestCase.assertEquals(
                "tb1 USE INDEX FOR ORDER BY (i1,i2) FORCE INDEX FOR GROUP BY (i1)", output);

        sql = "tb1 ignore key for join (i1,i2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertNull(((Table) (trs.getTables()).get(0)).getAlias());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_IGNORE, indexhint.getAction().intValue());
        TestCase.assertFalse(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_JOIN, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 IGNORE KEY FOR JOIN(i1,i2)", output);

        sql = "tb1 ignore index for group by (i1,i2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertNull(((Table) (trs.getTables()).get(0)).getAlias());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_IGNORE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_GROUP_BY, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 IGNORE INDEX FOR GROUP BY (i1,i2)", output);

        sql = "(offer  a  straight_join wp_image b use key for join(t1,t2) on a.member_id=b.member_id inner join product_visit c )"
                .getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals(
                "offer AS a STRAIGHT_JOIN wp_image AS b USE KEY FOR JOIN(t1,t2) ON a.member_id=b.member_id INNER JOIN product_visit AS c",
                output);

        sql = "tb1 ignore index for order by(i1)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertNull(((Table) (trs.getTables()).get(0)).getAlias());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(1, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_IGNORE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_ORDER_BY, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 IGNORE INDEX FOR ORDER BY (i1)", output);

        sql = "tb1 force key for group by (i1,i2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertNull(((Table) (trs.getTables()).get(0)).getAlias());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_FORCE, indexhint.getAction().intValue());
        TestCase.assertFalse(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_GROUP_BY, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 FORCE KEY FOR GROUP BY (i1,i2)", output);

        sql = "tb1 force index for group by (i1,i2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertNull(((Table) (trs.getTables()).get(0)).getAlias());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_FORCE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_GROUP_BY, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 FORCE INDEX FOR GROUP BY (i1,i2)", output);

        sql = "tb1 force index for join (i1,i2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals("tb1",
                new String(((Table) (trs.getTables()).get(0)).getTable().getIdText()));
        TestCase.assertNull(((Table) (trs.getTables()).get(0)).getAlias());
        hintlist = ((Table) (trs.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_FORCE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_JOIN, indexhint.getScope().intValue());
        TestCase.assertEquals("tb1 FORCE INDEX FOR JOIN(i1,i2)", output);

        sql = ("(tb1 force index for join (i1,i2) )left outer join tb2 as t2 "
                + "use index (i1,i2,i3) on t1.id1=t2.id1").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        TestCase.assertTrue((((Join) list.get(0)).isLeft()));
        Tables ltr = (Tables) ((Join) list.get(0)).getLeftTable();
        TestCase.assertEquals(1, ltr.getTables().size());
        TestCase.assertEquals(Table.class, ltr.getTables().get(0).getClass());
        TestCase.assertNull(((Table) (ltr.getTables().get(0))).getAlias());
        TestCase.assertEquals("tb1",
                new String(((Table) (ltr.getTables().get(0))).getTable().getIdText()));
        hintlist = ((Table) (ltr.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_FORCE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_JOIN, indexhint.getScope().intValue());
        Table rtf = (Table) ((Join) list.get(0)).getRightTable();
        TestCase.assertEquals("t2", rtf.getAlias());
        TestCase.assertEquals("tb2", new String(rtf.getTable().getIdText()));
        hintlist = rtf.getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(3, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertNull(indexhint.getScope());
        TestCase.assertEquals(ComparisionExpression.class,
                ((Join) list.get(0)).getOnCond().getClass());
        TestCase.assertEquals("(tb1 FORCE INDEX FOR JOIN(i1,i2)) "
                + "LEFT JOIN tb2 AS t2 USE INDEX (i1,i2,i3) ON t1.id1=t2.id1",
                output);

        sql = (" (((tb1 force index for join (i1,i2),tb3),tb4),tb5) "
                + "left outer join (tb2 as t2 use index (i1,i2,i3)) using(id1)").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        TestCase.assertTrue((((Join) list.get(0)).isLeft()));
        ltr = (Tables) ((Join) list.get(0)).getLeftTable();
        TestCase.assertEquals(2, ltr.getTables().size());
        TestCase.assertEquals(Tables.class, ltr.getTables().get(0).getClass());
        Tables ltr1 = (Tables) (ltr.getTables()).get(0);
        TestCase.assertEquals(2, ltr1.getTables().size());
        TestCase.assertEquals(Tables.class, ltr1.getTables().get(0).getClass());
        Tables ltr2 = (Tables) (ltr1.getTables()).get(0);
        TestCase.assertEquals(2, ltr2.getTables().size());
        TestCase.assertEquals(Table.class, ltr2.getTables().get(0).getClass());
        TestCase.assertNull(((Table) (ltr2.getTables().get(0))).getAlias());
        TestCase.assertEquals("tb1",
                new String(((Table) (ltr2.getTables().get(0))).getTable().getIdText()));
        hintlist = ((Table) (ltr2.getTables()).get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(2, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_FORCE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertEquals(IndexHint.SCOPE_JOIN, indexhint.getScope().intValue());
        TestCase.assertEquals(Table.class, ltr2.getTables().get(1).getClass());
        TestCase.assertEquals("tb3",
                new String(((Table) (ltr2.getTables().get(1))).getTable().getIdText()));
        TestCase.assertEquals(Table.class, ltr1.getTables().get(1).getClass());
        TestCase.assertEquals("tb4",
                new String(((Table) (ltr1.getTables().get(1))).getTable().getIdText()));
        TestCase.assertEquals(Table.class, ltr.getTables().get(1).getClass());
        TestCase.assertEquals("tb5",
                new String(((Table) (ltr.getTables().get(1))).getTable().getIdText()));
        Tables rtr = (Tables) ((Join) list.get(0)).getRightTable();
        TestCase.assertEquals("t2", ((Table) rtr.getTables().get(0)).getAlias());
        TestCase.assertEquals("tb2",
                new String(((Table) rtr.getTables().get(0)).getTable().getIdText()));
        hintlist = ((Table) rtr.getTables().get(0)).getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(3, indexhint.getIndexList().size());
        TestCase.assertEquals("i1", new String(indexhint.getIndexList().get(0).getIdText()));
        TestCase.assertEquals("i2", new String(indexhint.getIndexList().get(1).getIdText()));
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertTrue(indexhint.isIndex());
        TestCase.assertNull(indexhint.getScope());
        using_list = ((Join) (trs.getTables()).get(0)).getUsing();
        TestCase.assertEquals(1, using_list.size());
        TestCase.assertEquals(
                "(tb1 FORCE INDEX FOR JOIN(i1,i2),tb3,tb4,tb5) "
                        + "LEFT JOIN (tb2 AS t2 USE INDEX (i1,i2,i3)) USING (id1)",
                output);

        sql = ("(tb1 force index for join (i1,i2),tb3) "
                + "left outer join tb2 as t2 use index (i1,i2,i3) using(id1,id2)").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals(
                "(tb1 FORCE INDEX FOR JOIN(i1,i2),tb3) "
                        + "LEFT JOIN tb2 AS t2 USE INDEX (i1,i2,i3) USING (id1,id2)",
                output);

        sql = "(tb1 force index for join (i1,i2),tb3) left outer join (tb2 as t2 use index (i1,i2,i3)) using(id1,id2)"
                .getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals(
                "(tb1 FORCE INDEX FOR JOIN(i1,i2),tb3) "
                        + "LEFT JOIN (tb2 AS t2 USE INDEX (i1,i2,i3)) USING (id1,id2)",
                output);

        sql = "tb1 as t1 cross join tb2 as t2 use index(i1)using(id1)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 AS t1 INNER JOIN tb2 AS t2 USE INDEX (i1) USING (id1)",
                output);

        sql = "(tb1 as t1) cross join tb2 as t2 use index(i1)using(id1)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("(tb1 AS t1) INNER JOIN tb2 AS t2 USE INDEX (i1) USING (id1)",
                output);

        sql = "tb1 as _latin't1' cross join tb2 as t2 use index(i1)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 AS _latin't1' INNER JOIN tb2 AS t2 USE INDEX (i1)", output);

        sql = "((select '  @  from' from  `from`)) as t1 cross join tb2 as t2 use index()"
                .getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        Subquery lsf = (Subquery) ((Join) list.get(0)).getLeftTable();
        TestCase.assertEquals("t1", lsf.getAlias());
        TestCase.assertEquals(DMLSelectStatement.class, lsf.getSubquery().getClass());
        rtf = (Table) ((Join) list.get(0)).getRightTable();
        TestCase.assertEquals("t2", rtf.getAlias());
        hintlist = rtf.getHintList();
        TestCase.assertEquals(1, hintlist.size());
        indexhint = hintlist.get(0);
        TestCase.assertEquals(IndexHint.ACTION_USE, indexhint.getAction().intValue());
        TestCase.assertEquals(true, indexhint.isIndex());
        TestCase.assertEquals(null, indexhint.getScope());
        TestCase.assertEquals("tb2", new String(rtf.getTable().getIdText()));
        TestCase.assertEquals(
                "((SELECT '  @  from' FROM `from`)) AS t1 " + "INNER JOIN tb2 AS t2 USE INDEX ()",
                output);

        sql = "(tb1 as t1) straight_join (tb2 as t2)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("(tb1 AS t1) STRAIGHT_JOIN (tb2 AS t2)", output);

        sql = "tb1 straight_join tb2 as t2 on tb1.id=tb2.id".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 STRAIGHT_JOIN tb2 AS t2 ON tb1.id=tb2.id", output);

        sql = "tb1 left outer join tb2 on tb1.id=tb2.id".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 LEFT JOIN tb2 ON tb1.id=tb2.id", output);

        sql = "tb1 left outer join tb2 using(id)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 LEFT JOIN tb2 USING (id)", output);

        sql = "(tb1 right outer join tb2 using()) join tb3 on tb1.id=tb2.id and tb2.id=tb3.id"
                .getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        ltr = (Tables) ((Join) list.get(0)).getLeftTable();
        TestCase.assertEquals(1, ltr.getTables().size());
        Table lltrf = (Table) ((Join) ltr.getTables().get(0)).getLeftTable();
        TestCase.assertEquals(null, lltrf.getAlias());
        TestCase.assertEquals("tb1", new String(lltrf.getTable().getIdText()));
        using_list = ((Join) ltr.getTables().get(0)).getUsing();
        TestCase.assertEquals(0, using_list.size());
        rtf = (Table) ((Join) list.get(0)).getRightTable();
        TestCase.assertEquals(null, rtf.getAlias());
        hintlist = rtf.getHintList();
        TestCase.assertEquals(0, hintlist.size());
        TestCase.assertEquals("tb3", new String(rtf.getTable().getIdText()));
        TestCase.assertEquals(LogicalExpression.class, ((Join) list.get(0)).getOnCond().getClass());
        TestCase.assertEquals(
                "(tb1 RIGHT JOIN tb2 USING ()) "
                        + "INNER JOIN tb3 ON tb1.id=tb2.id AND tb2.id=tb3.id",
                output);

        sql = ("tb1 right outer join tb2 using(id1,id2) "
                + "join (tb3,tb4) on tb1.id=tb2.id and tb2.id=tb3.id").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        Join loj = (Join) ((Join) list.get(0)).getLeftTable();
        lltrf = (Table) loj.getLeftTable();
        TestCase.assertNull(lltrf.getAlias());
        TestCase.assertEquals("tb1", new String(lltrf.getTable().getIdText()));
        using_list = loj.getUsing();
        TestCase.assertEquals(2, using_list.size());
        rtr = (Tables) ((Join) list.get(0)).getRightTable();
        TestCase.assertEquals(2, rtr.getTables().size());
        TestCase.assertEquals("tb3",
                new String(((Table) (rtr.getTables().get(0))).getTable().getIdText()));
        TestCase.assertEquals("tb4",
                new String(((Table) (rtr.getTables().get(1))).getTable().getIdText()));
        TestCase.assertEquals(LogicalExpression.class, ((Join) list.get(0)).getOnCond().getClass());
        TestCase.assertEquals("tb1 RIGHT JOIN tb2 USING (id1,id2) "
                + "INNER JOIN (tb3,tb4) ON tb1.id=tb2.id AND tb2.id=tb3.id",
                output);

        sql = "tb1 left outer join tb2 join tb3 using(id)".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 LEFT JOIN (tb2 INNER JOIN tb3) USING (id)", output);

        sql = "tb1 right join tb2 on tb1.id = tb2.id".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 RIGHT JOIN tb2 ON tb1.id=tb2.id", output);

        sql = "tb1 natural right join tb2 ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 NATURAL RIGHT JOIN tb2", output);

        sql = "tb1 natural right outer join tb2 natural left outer join tb3".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        Join lnj = (Join) ((Join) list.get(0)).getLeftTable();
        lltrf = (Table) lnj.getLeftTable();
        TestCase.assertNull(lltrf.getAlias());
        TestCase.assertEquals("tb1", new String(lltrf.getTable().getIdText()));
        Table rltrf = (Table) lnj.getRightTable();
        TestCase.assertNull(rltrf.getAlias());
        TestCase.assertEquals("tb2", new String(rltrf.getTable().getIdText()));
        rtf = (Table) ((Join) list.get(0)).getRightTable();
        TestCase.assertNull(rtf.getAlias());
        TestCase.assertEquals("tb3", new String(rtf.getTable().getIdText()));
        TestCase.assertEquals("tb1 NATURAL RIGHT JOIN tb2 NATURAL LEFT JOIN tb3", output);

        sql = "tb1 natural left outer join tb2 ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("tb1 NATURAL LEFT JOIN tb2", output);

        sql = "(tb1  t1) natural  join (tb2 as t2) ".getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        TestCase.assertEquals("(tb1 AS t1) NATURAL JOIN (tb2 AS t2)", output);

        sql = ("(select (select * from tb1) from `select` "
                + "where `any`=any(select id2 from tb2))any  ").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(Subquery.class, list.get(0).getClass());
        TestCase.assertEquals("any", ((Subquery) list.get(0)).getAlias());
        TestCase.assertEquals("(SELECT (SELECT * FROM tb1) FROM `select` "
                + "WHERE `any`=ANY (SELECT id2 FROM tb2)) AS any", output);

        sql = ("((tb1),(tb3 as t3,`select`),tb2 use key for join (i1,i2))"
                + " left join tb4 join tb5 using ()").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        TestCase.assertEquals(Tables.class, ((Join) list.get(0)).getLeftTable().getClass());
        TestCase.assertEquals(Join.class, ((Join) list.get(0)).getRightTable().getClass());
        list = ((Tables) ((Join) list.get(0)).getLeftTable()).getTables();
        list = ((Tables) list.get(1)).getTables();
        TestCase.assertEquals(2, list.size());
        TestCase.assertEquals("(tb1,tb3 AS t3,`select`,tb2 USE KEY FOR JOIN(i1,i2))"
                + " LEFT JOIN (tb4 INNER JOIN tb5) USING ()", output);

        sql = ("((select  `select` from  `from` ) tb1),(tb3 as t3,`select`),tb2 use key for join (i1,i2) "
                + "left join tb4 using (i1,i2)straight_join tb5").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(3, list.size());
        TestCase.assertEquals(Tables.class, list.get(0).getClass());
        TestCase.assertEquals(Tables.class, list.get(1).getClass());
        TestCase.assertEquals(Join.class, list.get(2).getClass());
        list = ((Tables) list.get(0)).getTables();
        TestCase.assertEquals(Subquery.class, list.get(0).getClass());
        list = trs.getTables();
        list = ((Tables) list.get(1)).getTables();
        TestCase.assertEquals(Table.class, list.get(0).getClass());
        TestCase.assertEquals(Table.class, list.get(1).getClass());
        list = trs.getTables();
        Join sj = (Join) list.get(2);
        TestCase.assertEquals(Join.class, sj.getLeftTable().getClass());
        TestCase.assertEquals(Table.class, sj.getRightTable().getClass());
        Join oj = (Join) sj.getLeftTable();
        using_list = oj.getUsing();
        TestCase.assertEquals(2, using_list.size());
        TestCase.assertEquals(
                "((SELECT `select` FROM `from`)) AS tb1,tb3 AS t3,`select`,tb2 USE KEY FOR JOIN(i1,i2) LEFT JOIN tb4 USING (i1,i2) STRAIGHT_JOIN tb5",
                output);

        sql = ("(`select`,(tb1 as t1 use index for join()ignore key for group by (i1)))"
                + "join tb2 on cd1=any " + "right join " + "tb3 straight_join "
                + "(tb4 use index() left outer join (tb6,tb7) on id3=all(select `all` from `all`)) "
                + " on id2=any(select * from any) using  (i1)").getBytes();
        lexer = new Lexer(sql, MySqlCharset.UTF8_FOR_JAVA);
        parser = getDMLParser(lexer);
        trs = parser.tableRefs();
        output = outputMySQL(trs, sql);
        list = trs.getTables();
        TestCase.assertEquals(1, list.size());
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        using_list = ((Join) list.get(0)).getUsing();
        TestCase.assertEquals(1, using_list.size());
        TestCase.assertEquals(Join.class, ((Join) (list.get(0))).getLeftTable().getClass());
        TestCase.assertEquals(Join.class, ((Join) (list.get(0))).getRightTable().getClass());
        Join rsj = (Join) ((Join) (list.get(0))).getRightTable();
        TestCase.assertEquals(Table.class, rsj.getLeftTable().getClass());
        TestCase.assertEquals(Tables.class, rsj.getRightTable().getClass());
        list = ((Tables) rsj.getRightTable()).getTables();
        TestCase.assertEquals(Join.class, list.get(0).getClass());
        TestCase.assertEquals(
                "(`select`,tb1 AS t1 USE INDEX FOR JOIN() IGNORE KEY FOR GROUP BY (i1)) "
                        + "INNER JOIN tb2 ON cd1=any RIGHT JOIN (tb3 STRAIGHT_JOIN (tb4 USE INDEX () "
                        + "LEFT JOIN (tb6,tb7) ON id3=ALL (SELECT `all` FROM `all`)) ON id2=ANY (SELECT * FROM any))"
                        + " USING (i1)",
                output);
    }

    public void testDo() throws Exception {
        byte[] sql = "DO SLEEP(5);".getBytes();
        Tuple2<List<SQLStatement>, Exception> tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        DMLDoStatement dostmt = (DMLDoStatement) tuple._1().get(0);
        String output = outputMySQL(dostmt, sql);
        TestCase.assertEquals("DO SLEEP(5)", output);

        sql = "DO SLEEP(5),SLEEP(10);".getBytes();
        tuple = Parser.parse(sql, MySqlCharset.UTF8_FOR_JAVA);
        dostmt = (DMLDoStatement) tuple._1().get(0);
        output = outputMySQL(dostmt, sql);
        TestCase.assertEquals("DO SLEEP(5),SLEEP(10)", output);
    }
}
