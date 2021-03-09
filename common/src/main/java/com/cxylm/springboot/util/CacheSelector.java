package com.cxylm.springboot.util;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
