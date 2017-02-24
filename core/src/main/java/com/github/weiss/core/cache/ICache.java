package com.github.weiss.core.cache;

/**
 * Created by Weiss on 2017/1/10.
 */

public interface ICache {

    void put(String key, Object value);

    Object get(String key);

    void remove(String key);

    boolean contains(String key);

    void clear();

}
