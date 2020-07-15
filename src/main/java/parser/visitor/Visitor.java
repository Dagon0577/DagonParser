package parser.visitor;

import parser.ast.expression.*;
import parser.ast.expression.primary.*;
import parser.ast.expression.primary.WithClause.CTE;
import parser.ast.expression.primary.literal.*;
import parser.ast.fragment.GroupBy;
import parser.ast.fragment.Limit;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.SubpartitionDefinition;
import parser.ast.fragment.ddl.*;
import parser.ast.fragment.tableref.*;
import parser.ast.stmt.dml.DMLSelectStatement;
import parser.ast.stmt.dml.DMLSelectStatement.SelectOption;
import parser.ast.stmt.dml.DMLSelectStatement.OutFile;
import parser.ast.stmt.dml.DMLSelectStatement.LockMode;
import parser.ast.stmt.dml.DMLSelectUnionStatement;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public interface Visitor {
    public void visit(DMLSelectStatement node);

    public void visit(SelectOption node);

    public void visit(Identifier node);

    public void visit(Table node);

    public void visit(Tables node);

    public void visit(LiteralBitField node);

    public void visit(LiteralBoolean node);

    public void visit(LiteralHexadecimal node);

    public void visit(LiteralNull node);

    public void visit(LiteralNumber node);

    public void visit(LiteralString node);

    public void visit(VarsPrimary node);

    public void visit(OutFile node);

    public void visit(GroupBy node);

    public void visit(Limit node);

    public void visit(OrderBy node);

    public void visit(PartitionDefinition node);

    public void visit(PartitionOptions node);

    public void visit(SubpartitionDefinition node);

    public void visit(WindowClause node);

    public void visit(WithClause node);

    public void visit(CTE node);

    public void visit(LockMode node);

    public void visit(BinaryOperatorExpression node);

    public void visit(DataType node);

    public void visit(Wildcard node);

    public void visit(Dual node);

    public void visit(Join node);

    public void visit(DMLSelectUnionStatement node);

    public void visit(Subquery node);

    public void visit(ParamMarker node);

    public void visit(ColumnDefinition node);

    public void visit(ReferenceDefinition node);

    public void visit(IsExpression node);

    public void visit(IndexColumnName node);

    public void visit(LogicalExpression node);

    public void visit(UnaryOperatorExpression node);

    public void visit(TernaryOperatorExpression node);

    public void visit(CollateExpression node);

    public void visit(InExpressionList node);

    public void visit(ExistsPrimary node);

    public void visit(CaseWhenExpression node);

    public void visit(Cast node);

    public void visit(Char node);

    public void visit(Convert node);

    public void visit(DefaultValue node);

    public void visit(FromForFunction node);

    public void visit(FunctionExpression node);

    public void visit(MatchExpression node);

    public void visit(OverClause node);

    public void visit(PlaceHolder node);

    public void visit(RowExpression node);

    public void visit(IntervalPrimary node);

    public void visit(IndexHint node);
}
