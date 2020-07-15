package parser;

import parser.ast.stmt.dml.DMLSelectStatement;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class ParseInfo {
    public static int index = 16;
    public static final long NO_TABLE = 1L << index++; // 无表查询
    public static final long SINGLE_TABLE = 1L << index++; // 单表查询
    public static final long UNION = 1L << index++; // UNION
    public static final long SUBQUERY = 1L << index++; // 子查询
    public static final long JOIN = 1L << index++; // JOIN
    public static final long HINT = 1L << index++; // HINT
    public static final long WILDCARD_IN_SELECT = 1L << index++; // SELECT字段中含有通配符*
    public static final long DNID_IN_SELECT = 1L << index++; // SELECT字段中含有DNID
    public static final long DNID_IN_WHERE = 1L << index++; // WHERE条件中含有DNID
    public static final long OR_CONDITION = 1L << index++; // WHERE 条件中含有OR
    public static final long XOR_CONDITION = 1L << index++; // WHERE 条件中含有XOR
    public static final long UNCERTAINTY_FUNCTION = 1L << index++; // 含有非确定性函数
    public static final long AGGREGATION = 1L << index++; // 需要聚合
    public static final long VAR_ASSIGNMENT = 1L << index++; // 存在用户变量赋值
    public static final long ID_WITH_SCHEMA = 1L << index++; // Identifier 包含库名
    public static final long ID_WITH_TABLE = 1L << index++; // Identifier 包含表名

    public static final boolean isNoTable(long info) {
        return (info & NO_TABLE) == NO_TABLE;
    }

    public static final boolean isSingleTable(long info) {
        return (info & SINGLE_TABLE) == SINGLE_TABLE;
    }

    public static final boolean isUnion(long info) {
        return (info & UNION) == UNION;
    }

    public static final boolean isSubquery(long info) {
        return (info & SUBQUERY) == SUBQUERY;
    }

    public static final boolean isJoin(long info) {
        return (info & JOIN) == JOIN;
    }

    public static final boolean isHint(long info) {
        return (info & HINT) == HINT;
    }

    public static final boolean isWildCardInSelect(long info) {
        return (info & WILDCARD_IN_SELECT) == WILDCARD_IN_SELECT;
    }

    public static final boolean isDNIDInSelect(long info) {
        return (info & DNID_IN_SELECT) == DNID_IN_SELECT;
    }

    public static final boolean isDNIDInWhere(long info) {
        return (info & DNID_IN_WHERE) == DNID_IN_WHERE;
    }

    public static final boolean hasOrCondition(long info) {
        return (info & OR_CONDITION) == OR_CONDITION;
    }

    public static final boolean hasXorCondition(long info) {
        return (info & XOR_CONDITION) == XOR_CONDITION;
    }

    public static final boolean hasUncertaintyFunction(long info) {
        return (info & UNCERTAINTY_FUNCTION) == UNCERTAINTY_FUNCTION;
    }

    public static final boolean hasAggregation(long info) {
        return (info & AGGREGATION) == AGGREGATION;
    }

    public static final boolean hasVarAssignment(long info) {
        return (info & VAR_ASSIGNMENT) == VAR_ASSIGNMENT;
    }

    public static final boolean idWithSchema(long info) {
        return (info & ID_WITH_SCHEMA) == ID_WITH_SCHEMA;
    }

    public static final boolean idWithTable(long info) {
        return (info & ID_WITH_TABLE) == ID_WITH_TABLE;
    }

    public static final String getParseInfoStr(long info) {
        StringBuilder sb = new StringBuilder("[");
        sb.append("isNoTable").append(":").append(isNoTable(info)).append(", ");
        sb.append("isSingleTable").append(":").append(isSingleTable(info)).append(", ");
        sb.append("isUnion").append(":").append(isUnion(info)).append(", ");
        sb.append("isSubquery").append(":").append(isSubquery(info)).append(", ");
        sb.append("isJoin").append(":").append(isJoin(info)).append(", ");
        sb.append("isHint").append(":").append(isHint(info)).append(", ");
        sb.append("isWildcardInSelect").append(":").append(isWildCardInSelect(info)).append(", ");
        sb.append("isDNIDInSelect").append(":").append(isDNIDInSelect(info)).append(", ");
        sb.append("isDNIDInWhere").append(":").append(isDNIDInWhere(info)).append(", ");
        sb.append("hasOrCondition").append(":").append(hasOrCondition(info)).append(", ");
        sb.append("hasXorCondition").append(":").append(hasXorCondition(info)).append(", ");
        sb.append("hasUncertaintyFunction").append(":").append(hasUncertaintyFunction(info)).append(", ");
        sb.append("hasAggregation").append(":").append(hasAggregation(info));
        sb.append("hasVarAssignment").append(":").append(hasVarAssignment(info));
        sb.append("idWithSchema").append(":").append(idWithSchema(info));
        sb.append("idWithTable").append(":").append(idWithTable(info));
        sb.append("]");
        return sb.toString();
    }

    //    public static boolean isQuickSelect(long info, DMLSelectStatement select) {
    //        return (isSingleTable(info) || isNoTable(info)) && !idWithSchema(info) && !idWithTable(info) && !isUnion(info)
    //            && !isSubquery(info) && !isJoin(info) && !isHint(info) && !isDNIDInSelect(info) && !isDNIDInWhere(info)
    //            && !hasOrCondition(info) && !hasXorCondition(info) && !hasUncertaintyFunction(info) && !hasAggregation(info)
    //            && !hasVarAssignment(info) && select.getHaving() == null && select.getLimit() == null
    //            && select.getLock() == null && select.getOutFile() == null && select.getWindows() == null
    //            && select.getWithClause() == null;
    //    }
    //
    //    //单表聚合查询，无HINT，DNID，OR，XOR，Lock，File，Having，WithRollup等特殊处理
    //    public static boolean isSimpleSingleTableSelect(long info, DMLSelectStatement select) {
    //        return isSingleTable(info) && !idWithSchema(info) && !isUnion(info) && !isSubquery(info) && !isJoin(info)
    //            && !isHint(info) && !isDNIDInSelect(info) && !isDNIDInWhere(info) && !hasOrCondition(info)
    //            && !hasXorCondition(info) && !hasUncertaintyFunction(info) && !hasVarAssignment(info)
    //            && select.getHaving() == null && select.getLock() == null && select.getOutFile() == null
    //            && select.getWindows() == null && select.getWithClause() == null && (select.getGroupBy() == null || !select
    //            .getGroupBy().isWithRollup());
    //    }
    //
    //    //可以尝试路由到单节点的语句
    //    public static boolean isSimpleSingleNodeSelect(long info, DMLSelectStatement select) {
    //        return isSingleTable(info) && !idWithSchema(info) && !isUnion(info) && !isSubquery(info) && !isJoin(info)
    //            && !isHint(info) && !isDNIDInSelect(info) && !isDNIDInWhere(info) && !hasOrCondition(info)
    //            && !hasXorCondition(info) && !hasUncertaintyFunction(info) && !hasVarAssignment(info)
    //            && select.getLock() == null && select.getOutFile() == null && select.getWindows() == null
    //            && select.getWithClause() == null;
    //    }
    //
    //    public static boolean isQuickInsert(long info, DMLInsertReplaceStatement insert) {
    //        return isSingleTable(info) && !idWithSchema(info) && !isSubquery(info) && !isHint(info) && !isDNIDInSelect(info)
    //            && !isDNIDInWhere(info) && !hasUncertaintyFunction(info) && insert.getSelect() == null;
    //    }
    //
    //    public static boolean isQuickUpdate(long info, DMLUpdateStatement update) {
    //        return isSingleTable(info) && !idWithSchema(info) && !isSubquery(info) && !isHint(info) && !isDNIDInSelect(info)
    //            && !isDNIDInWhere(info) && !hasOrCondition(info) && !hasXorCondition(info) && !hasUncertaintyFunction(info)
    //            && update.getLimit() == null;
    //    }
    //
    //    public static boolean isQuickDelete(long info, DMLDeleteStatement delete) {
    //        return isSingleTable(info) && !idWithSchema(info) && !isSubquery(info) && !isHint(info) && !isDNIDInSelect(info)
    //            && !isDNIDInWhere(info) && !hasOrCondition(info) && !hasXorCondition(info) && !hasUncertaintyFunction(info)
    //            && delete.getLimit() == null;
    //    }
}
