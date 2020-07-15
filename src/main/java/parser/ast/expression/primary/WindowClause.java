package parser.ast.expression.primary;

import parser.ast.expression.Expression;
import parser.ast.expression.PrimaryExpression;
import parser.ast.fragment.OrderBy;
import parser.ast.fragment.ddl.PartitionOptions;
import parser.util.Pair;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @author Dagon0577
 * @date 2020/7/14
 *
 *
 * <pre>
 * window_spec:
 *     [window_name] [partition_clause] [order_clause] [frame_clause]
 *
 * partition_clause:
 *     PARTITION BY expr [, expr] ...
 *
 * order_clause:
 *     ORDER BY expr [ASC|DESC] [, expr [ASC|DESC]] ...
 *
 * frame_clause:
 *     frame_units frame_extent
 *
 * frame_units:
 *     {ROWS | RANGE}
 *
 * frame_extent:
 *     {frame_start | frame_between}
 *
 * frame_between:
 *     BETWEEN frame_start AND frame_end
 *
 * frame_start, frame_end: {
 *     CURRENT ROW
 *   | UNBOUNDED PRECEDING
 *   | UNBOUNDED FOLLOWING
 *   | expr PRECEDING
 *   | expr FOLLOWING
 * }
 * </pre>
 * @date 2020/7/15
 */
public class WindowClause extends PrimaryExpression {
    public static final int FRAME_UNIT_ROWS = 1;
    public static final int FRAME_UNIT_RANGE = 2;

    public static final int FRAM_CURRENT_ROW = 1;
    public static final int FRAM_UNBOUNDED_PRECEDING = 2;
    public static final int FRAM_UNBOUNDED_FOLLOWING = 3;
    public static final int FRAM_PRECEDING = 4;
    public static final int FRAM_FOLLOWING = 5;

    private Identifier name;
    private PartitionOptions partition;
    private OrderBy orderBy;
    private Integer frameUnit;
    private Pair<Integer, Expression> frameStart;
    private Pair<Integer, Expression> frameEnd;

    public WindowClause(Identifier name, PartitionOptions partition, OrderBy orderBy, Integer frameUnit,
        Pair<Integer, Expression> frameStart, Pair<Integer, Expression> frameEnd) {
        this.name = name;
        this.partition = partition;
        this.orderBy = orderBy;
        this.frameUnit = frameUnit;
        this.frameStart = frameStart;
        this.frameEnd = frameEnd;
    }

    public Identifier getName() {
        return name;
    }

    public PartitionOptions getPartition() {
        return partition;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public Integer getFrameUnit() {
        return frameUnit;
    }

    public Pair<Integer, Expression> getFrameStart() {
        return frameStart;
    }

    public Pair<Integer, Expression> getFrameEnd() {
        return frameEnd;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}

