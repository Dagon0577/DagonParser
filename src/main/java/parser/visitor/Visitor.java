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
import parser.ast.fragment.ddl.alter.AddCheckConstraintDefinition;
import parser.ast.fragment.ddl.alter.AddColumn;
import parser.ast.fragment.ddl.alter.AddForeignKey;
import parser.ast.fragment.ddl.alter.AddKey;
import parser.ast.fragment.ddl.alter.AlterCharacterSet;
import parser.ast.fragment.ddl.alter.AlterCheckConstraintDefination;
import parser.ast.fragment.ddl.alter.AlterColumn;
import parser.ast.fragment.ddl.alter.AlterIndex;
import parser.ast.fragment.ddl.alter.ChangeColumn;
import parser.ast.fragment.ddl.alter.ConvertCharacterSet;
import parser.ast.fragment.ddl.alter.DropCheckConstraintDefination;
import parser.ast.fragment.ddl.alter.DropColumn;
import parser.ast.fragment.ddl.alter.DropForeignKey;
import parser.ast.fragment.ddl.alter.DropIndex;
import parser.ast.fragment.ddl.alter.DropPrimaryKey;
import parser.ast.fragment.ddl.alter.EnableKeys;
import parser.ast.fragment.ddl.alter.Force;
import parser.ast.fragment.ddl.alter.ImportTablespace;
import parser.ast.fragment.ddl.alter.ModifyColumn;
import parser.ast.fragment.ddl.alter.OrderByColumns;
import parser.ast.fragment.ddl.alter.PartitionOperation;
import parser.ast.fragment.ddl.alter.RenameColumn;
import parser.ast.fragment.ddl.alter.RenameIndex;
import parser.ast.fragment.ddl.alter.RenameTo;
import parser.ast.fragment.ddl.alter.WithValidation;
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
import parser.ast.stmt.dal.resource.DALAlterResourceGroupStatement;
import parser.ast.stmt.dal.resource.DALCreateResourceGroupStatement;
import parser.ast.stmt.dal.resource.DALSetResourceGroupStatement;
import parser.ast.fragment.ddl.alter.Algorithm;
import parser.ast.stmt.ddl.alter.DDLAlterDatabaseStatement;
import parser.ast.stmt.ddl.alter.DDLAlterEventStatement;
import parser.ast.stmt.ddl.alter.DDLAlterFunctionStatement;
import parser.ast.stmt.ddl.alter.DDLAlterInstanceStatement;
import parser.ast.stmt.ddl.alter.DDLAlterLogfileGroupStatement;
import parser.ast.stmt.ddl.alter.DDLAlterProcedureStatement;
import parser.ast.stmt.ddl.alter.DDLAlterServerStatement;
import parser.ast.stmt.ddl.alter.DDLAlterTableStatement;
import parser.ast.fragment.ddl.alter.Lock;
import parser.ast.stmt.ddl.alter.DDLAlterTablespaceStatement;
import parser.ast.stmt.ddl.alter.DDLAlterViewStatement;
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

    public void visit(PartitionOperation node);

    public void visit(RenameColumn node);

    public void visit(RenameIndex node);

    public void visit(RenameTo node);

    public void visit(WithValidation node);

    public void visit(ImportTablespace node);

    public void visit(EnableKeys node);

    public void visit(Force node);

    public void visit(AddColumn node);

    public void visit(AddKey node);

    public void visit(AddForeignKey node);

    public void visit(AddCheckConstraintDefinition node);

    public void visit(DropCheckConstraintDefination node);

    public void visit(DropColumn node);

    public void visit(DropForeignKey node);

    public void visit(DropIndex node);

    public void visit(DropPrimaryKey node);

    public void visit(ModifyColumn node);

    public void visit(OrderByColumns node);

    public void visit(ChangeColumn node);

    public void visit(ConvertCharacterSet node);

    public void visit(AlterCheckConstraintDefination node);

    public void visit(AlterCharacterSet node);

    public void visit(AlterColumn node);

    public void visit(AlterIndex node);

    public void visit(DDLAlterDatabaseStatement node);

    public void visit(DDLAlterEventStatement node);

    public void visit(DDLAlterFunctionStatement node);

    public void visit(DDLAlterInstanceStatement node);

    public void visit(DDLAlterLogfileGroupStatement node);

    public void visit(DDLAlterProcedureStatement node);

    public void visit(DDLAlterServerStatement node);

    public void visit(DDLAlterTablespaceStatement node);

    public void visit(DDLAlterViewStatement node);

    public void visit(DDLAlterTableStatement node);

    public void visit(DALAlterUserStatement node);

    public void visit(DALAlterResourceGroupStatement node);

    public void visit(DMLCallStatement node);

    public void visit(DMLDoStatement node);

}
