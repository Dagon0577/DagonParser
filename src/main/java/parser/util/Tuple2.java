package parser.util;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public class Tuple2<E1, E2> extends Tuple {
    private E1 e1;
    private E2 e2;

    public Tuple2(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public E1 _1() {
        return e1;
    }

    public E2 _2() {
        return e2;
    }

    @Override protected boolean elementEquals(Tuple tuple) {
        if (!(tuple instanceof Tuple2)) {
            return false;
        }
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>)tuple;
        if (same(this.e1, tuple2.e1) && same(this.e2, tuple2.e2)) {
            return true;
        }
        return false;
    }

    @Override public int hashCode() {
        int result = 1;
        result = result * 31 + (e1 == null ? 0 : e1.hashCode());
        result = result * 31 + (e2 == null ? 0 : e2.hashCode());
        return result;
    }

    @Override public String toString() {
        return new StringBuilder().append("{").append(e1 == null ? "[NULL]" : e1).append(":")
            .append(e2 == null ? "[NULL]" : e2).append("}").toString();
    }

    public void setE1(E1 e1) {
        this.e1 = e1;
    }

    public void setE2(E2 e2) {
        this.e2 = e2;
    }

}

