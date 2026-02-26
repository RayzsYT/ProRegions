package de.rayzs.proregions.plugin.hook;

@FunctionalInterface
public interface HookTask<T, V> {
    V apply(T t, V v);
}
