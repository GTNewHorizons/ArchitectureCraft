package gcewing.architecture.common.network;

import java.util.HashMap;

class ClassCache extends HashMap<Class, MethodCache> {

    // @Override // Technically doesn't, because HashMao.get() is declared get(Object(), not get(K)
    public MethodCache get(Class key) {
        MethodCache result = super.get(key);
        if (result == null) {
            result = new MethodCache();
            put(key, result);
        }
        return result;
    }

}
