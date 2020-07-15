package parser.util;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class Tuple3<E1, E2, E3> extends Tuple {
    private E1 e1;
    private E2 e2;
    private E3 e3;

    public Tuple3(E1 e1, E2 e2, E3 e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public E1 _1() {
        return e1;
    }

    public E2 _2() {
        return e2;
    }

    public E3 _3() {
        return e3;
    }

    public void setE1(E1 e1) {
        this.e1 = e1;
    }

    public void setE2(E2 e2) {
        this.e2 = e2;
    }

    public void setE3(E3 e3) {
        this.e3 = e3;
    }

    @Override
    protected boolean elementEquals(Tuple tuple) {
        if (!(tuple instanceof Tuple3))
            return false;
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>)tuple;
        if (same(this.e1, tuple3.e1) && same(this.e2, tuple3.e2) && same(this.e3, tuple3.e3)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + (e1 == null ? 0 : e1.hashCode());
        result = result * 31 + (e2 == null ? 0 : e2.hashCode());
        result = result * 31 + (e3 == null ? 0 : e3.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{").append(e1 == null ? "[NULL]" : e1).append(":")
            .append(e2 == null ? "[NULL]" : e2).append(":").append(e3 == null ? "[NULL]" : e3).append("}").toString();
    }
}

