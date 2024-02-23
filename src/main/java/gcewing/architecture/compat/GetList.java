package gcewing.architecture.compat;

import com.google.common.base.Function;

import java.util.Arrays;
import java.util.List;

class GetList<T> implements Function<Object[], List<T>> {

    GetList() {}

    public List<T> apply(Object[] p_apply_1_) {
        return Arrays.<T>asList((T[]) p_apply_1_);
    }
}
