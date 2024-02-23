package gcewing.architecture.compat;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;

class GetList<T> implements Function<Object[], List<T>> {

    GetList() {}

    public List<T> apply(Object[] p_apply_1_) {
        return Arrays.<T>asList((T[]) p_apply_1_);
    }
}
