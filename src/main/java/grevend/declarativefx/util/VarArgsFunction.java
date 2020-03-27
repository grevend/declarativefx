package grevend.declarativefx.util;

@FunctionalInterface
public interface VarArgsFunction<T, R> {

    R apply(T... ts);

}