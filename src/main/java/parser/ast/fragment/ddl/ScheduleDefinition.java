package parser.ast.fragment.ddl;

import parser.ast.AST;
import parser.ast.expression.Expression;
import parser.ast.expression.primary.literal.LiteralNumber;
import parser.visitor.Visitor;
import parser.util.Pair;

import java.util.List;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class ScheduleDefinition implements AST {
    private final Expression atTimestamp;
    private final List<Pair<LiteralNumber, Integer>> intervalList;
    private final LiteralNumber everyInterval;
    private final Integer everyIntervalQuantity;
    private final Expression startsTimestamp;
    private final Expression endsTimestamp;
    private final List<Pair<LiteralNumber, Integer>> startsIntervalList;
    private final List<Pair<LiteralNumber, Integer>> endsIntervalList;

    public ScheduleDefinition(Expression atTimestamp, List<Pair<LiteralNumber, Integer>> intervalList) {
        this.atTimestamp = atTimestamp;
        this.intervalList = intervalList;
        this.everyInterval = null;
        this.everyIntervalQuantity = null;
        this.startsTimestamp = null;
        this.endsTimestamp = null;
        this.startsIntervalList = null;
        this.endsIntervalList = null;
    }

    public ScheduleDefinition(LiteralNumber everyInterval, Integer everyIntervalQuantity, Expression startsTimestamp,
        Expression endsTimestamp, List<Pair<LiteralNumber, Integer>> startsIntervalList,
        List<Pair<LiteralNumber, Integer>> endsIntervalList) {
        this.everyInterval = everyInterval;
        this.everyIntervalQuantity = everyIntervalQuantity;
        this.startsTimestamp = startsTimestamp;
        this.endsTimestamp = endsTimestamp;
        this.startsIntervalList = startsIntervalList;
        this.endsIntervalList = endsIntervalList;
        this.atTimestamp = null;
        this.intervalList = null;
    }

    public Expression getAtTimestamp() {
        return atTimestamp;
    }

    public List<Pair<LiteralNumber, Integer>> getIntervalList() {
        return intervalList;
    }

    public LiteralNumber getEveryInterval() {
        return everyInterval;
    }

    public Integer getEveryIntervalQuantity() {
        return everyIntervalQuantity;
    }

    public Expression getStartsTimestamp() {
        return startsTimestamp;
    }

    public Expression getEndsTimestamp() {
        return endsTimestamp;
    }

    public List<Pair<LiteralNumber, Integer>> getStartsIntervalList() {
        return startsIntervalList;
    }

    public List<Pair<LiteralNumber, Integer>> getEndsIntervalList() {
        return endsIntervalList;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
