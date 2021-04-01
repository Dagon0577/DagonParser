package parser.ast.stmt.ddl.alter;

import parser.SQLType;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.Identifier;
import parser.ast.stmt.SQLStatement;
import parser.visitor.Visitor;
import java.util.List;

/**
 * 
 * @author Dagon0577
 * @date 2021年04月01日
 * 
 *       <pre>
 * ALTER
 *     [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]
 *     [DEFINER = { user | CURRENT_USER }]
 *     [SQL SECURITY { DEFINER | INVOKER }]
 *     VIEW view_name [(column_list)]
 *     AS select_statement
 *     [WITH [CASCADED | LOCAL] CHECK OPTION]
 *       </pre>
 */
public class DDLAlterViewStatement implements SQLStatement {
    private final Integer algorithm;
    private final Expression definer;
    private final Boolean sqlSecurityDefiner;
    private final Identifier name;
    private final List<Identifier> columns;
    private final SQLStatement stmt;
    private final boolean withCheckOption;
    private final Boolean cascaded;

    public DDLAlterViewStatement(Integer algorithm, Expression definer, Boolean sqlSecurityDefiner,
            Identifier name, List<Identifier> columns, SQLStatement stmt, boolean withCheckOption,
            Boolean cascaded) {
        this.algorithm = algorithm;
        this.definer = definer;
        this.sqlSecurityDefiner = sqlSecurityDefiner;
        this.name = name;
        this.columns = columns;
        this.stmt = stmt;
        this.withCheckOption = withCheckOption;
        this.cascaded = cascaded;
    }

    public Integer getAlgorithm() {
        return algorithm;
    }

    public Expression getDefiner() {
        return definer;
    }

    public Boolean getSqlSecurityDefiner() {
        return sqlSecurityDefiner;
    }

    public Identifier getName() {
        return name;
    }

    public List<Identifier> getColumns() {
        return columns;
    }

    public SQLStatement getStmt() {
        return stmt;
    }

    public boolean isWithCheckOption() {
        return withCheckOption;
    }

    public Boolean getCascaded() {
        return cascaded;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getSQLType() {
        return SQLType.ALTER_VIEW;
    }

}
