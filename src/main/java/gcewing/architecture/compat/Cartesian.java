package gcewing.architecture.compat;

import java.lang.reflect.Array;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Cartesian {

    public static <T> Iterable<T[]> cartesianProduct(Class<T> clazz, Iterable<? extends Iterable<? extends T>> sets) {
        return new Product<>(clazz, (Iterable[]) toArray(Iterable.class, sets));
    }

    public static <T> Iterable<List<T>> cartesianProduct(Iterable<? extends Iterable<? extends T>> sets) {
        /**
         * Convert an Iterable of Arrays (Object[]) to an Iterable of Lists
         */
        return arraysAsLists(cartesianProduct(Object.class, sets));
    }

    private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> arrays) {
        return Iterables.transform(arrays, new GetList<>());
    }

    private static <T> T[] toArray(Class<? super T> clazz, Iterable<? extends T> it) {
        List<T> list = Lists.<T>newArrayList();

        for (T t : it) {
            list.add(t);
        }

        return list.toArray(createArray(clazz, list.size()));
    }

    public static <T> T[] createArray(Class<? super T> p_179319_0_, int p_179319_1_) {
        return (T[]) Array.newInstance(p_179319_0_, p_179319_1_);
    }

}
