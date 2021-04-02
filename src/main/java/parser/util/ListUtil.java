package parser.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Dagon0577
 * @date 2021年04月02日
 *
 */
public final class ListUtil {
    @SuppressWarnings("rawtypes")
    public static List<?> createList(Object... objs) {
        return createList(new ArrayList(), objs);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<?> createList(List list, Object... objs) {
        if (objs != null) {
            list.addAll(Arrays.asList(objs));
        }
        return list;
    }

    public static boolean isEquals(List<?> l1, List<?> l2) {
        if (l1 == l2)
            return true;
        if (l1 == null)
            return false;
        if (l1.size() != l2.size())
            return false;
        Iterator<?> iter1 = l1.iterator();
        Iterator<?> iter2 = l2.iterator();
        while (iter1.hasNext()) {
            Object o1 = iter1.next();
            Object o2 = iter2.next();
            if (o1 == o2)
                continue;
            if (o1 == null)
                return false;
            if (!o1.equals(o2))
                return false;
        }
        return true;
    }
}
