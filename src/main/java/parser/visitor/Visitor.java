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
import parser.ast.stmt.compound.BeginEndStatement;
import parser.ast.stmt.compound.DeclareStatement;
import parser.ast.stmt.compound.condition.*;
import parser.ast.stmt.compound.cursors.CursorCloseStatement;
import parser.ast.stmt.compound.cursors.CursorDeclareStatement;
import parser.ast.stmt.compound.cursors.CursorFetchStatement;
import parser.ast.stmt.compound.cursors.CursorOpenStatement;
import parser.ast.stmt.compound.flowcontrol.*;
import parser.ast.stmt.dal.DALSetStatement;
import parser.ast.stmt.dal.account.*;
import parser.ast.stmt.dal.resource.DALCreateResourceGroupStatement;
import parser.ast.stmt.dal.resource.DALSetResourceGroupStatement;
import parser.ast.stmt.ddl.*;
import parser.ast.stmt.ddl.alter.Algorithm;
import parser.ast.stmt.ddl.alter.Lock;
import parser.ast.stmt.dml.*;
import parser.ast.stmt.dml.DMLSelectStatement.SelectOption;
import parser.ast.stmt.dml.DMLSelectStatement.OutFile;
import parser.ast.stmt.dml.DMLSelectStatement.LockMode;
import parser.ast.stmt.transactional.BeginStatement;
import parser.ast.stmt.transactional.SetTransactionStatement;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public interface Visitor {
    public void visit(DMLSelectStatement node);

    public void visit(DMLUpdateStatement node);

    public void visit(DMLInsertReplaceStatement node);

    public void visit(DMLDeleteStatement node);

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

    public void visit(DDLCreateDatabaseStatement node);

    public void visit(DDLCreateEventStatement node);

    public void visit(DDLCreateFunctionStatement node);

    public void visit(DDLCreateIndexStatement node);

    public void visit(DDLCreateLogfileGroupStatement node);

    public void visit(DDLCreateProcedureStatement node);

    public void visit(DDLCreateServerStatement node);

    public void visit(DDLCreateSpatialReferenceSystemStatement node);

    public void visit(DDLCreateTablespaceStatement node);

    public void visit(DDLCreateTableStatement node);

    public void visit(DDLCreateTriggerStatement node);

    public void visit(DDLCreateViewStatement node);

    public void visit(DALCreateResourceGroupStatement node);

    public void visit(DALCreateRoleStatement node);

    public void visit(DALCreateUserStatement node);

    public void visit(DALSetStatement node);

    public void visit(DALSetResourceGroupStatement node);

    public void visit(DALSetDefaultRoleStatement node);

    public void visit(DALSetPasswordStatement node);

    public void visit(DALSetRoleStatement node);

    public void visit(AuthOption node);

    public void visit(ScheduleDefinition node);

    public void visit(Characteristic node);

    public void visit(IndexDefinition node);

    public void visit(IndexOption node);

    public void visit(ForeignKeyDefinition node);

    public void visit(TableOptions node);

    public void visit(Algorithm node);

    public void visit(Lock node);

    public void visit(BeginStatement node);

    public void visit(BeginEndStatement node);

    public void visit(DeclareStatement node);

    public void visit(SetTransactionStatement node);

    public void visit(CaseStatement node);

    public void visit(IfStatement node);

    public void visit(IterateStatement node);

    public void visit(LeaveStatement node);

    public void visit(LoopStatement node);

    public void visit(RepeatStatement node);

    public void visit(ReturnStatement node);

    public void visit(WhileStatement node);

    public void visit(ConditionValue node);

    public void visit(DeclareConditionStatement node);

    public void visit(DeclareHandlerStatement node);

    public void visit(GetDiagnosticsStatement node);

    public void visit(ResignalStatement node);

    public void visit(SignalStatement node);

    public void visit(CursorCloseStatement node);

    public void visit(CursorDeclareStatement node);

    public void visit(CursorFetchStatement node);

    public void visit(CursorOpenStatement node);
}
