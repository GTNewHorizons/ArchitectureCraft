package gcewing.architecture.common.network;

import java.lang.reflect.Method;

enum HandlerMap {

    SERVER(ServerMessageHandler.class) {

        protected String annotationValue(Object a) {
            return ((ServerMessageHandler) a).value();
        }
    },

    CLIENT(ClientMessageHandler.class) {

        protected String annotationValue(Object a) {
            return ((ClientMessageHandler) a).value();
        }
    };

    protected final Class type;
    protected final ClassCache classCache = new ClassCache();

    HandlerMap(Class type) {
        this.type = type;
    }

    // This method exists because annotation classes can't be extended. :-(
    protected abstract String annotationValue(Object a);

    public Method get(Object handler, String message) {
        Class<? extends Object> cls = handler.getClass();
        MethodCache cache = classCache.get(cls);
        Method meth = cache.get(message);
        if (meth == null) {
            // System.out.printf("BaseDataChannel: Looking for handler for %s in %s\n", message, cls);
            for (Method m : cls.getMethods()) {
                Object a = m.getAnnotation(type);
                if (a != null && annotationValue(a).equals(message)) {
                    cache.put(message, m);
                    meth = m;
                    break;
                }
            }
        }
        return meth;
    }
}
