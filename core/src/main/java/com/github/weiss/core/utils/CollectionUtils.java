package com.github.weiss.core.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CollectionUtils {

    private static final String DELIMITER = ",";

    /**
     * Judge whether a collection is null or size is 0
     *
     * @param c
     * @param <V>
     * @return
     */
    public static <V> boolean isEmpty(Collection<V> c) {
        return (c == null || c.size() == 0);
    }

    /**
     * Join collection to string, separator is {@link #DELIMITER}
     *
     * @param tokens
     * @return
     */
    public static String join(Iterable tokens) {
        return tokens == null ? "" : TextUtils.join(DELIMITER, tokens);
    }

    /**
     * Convert array to list
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> List<T> arrayToList(T... array) {
        return Arrays.asList(array);
    }

    /**
     * Convert array to set
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> Set<T> arrayToSet(T... array) {
        return new HashSet<T>(arrayToList(array));
    }

    /**
     * Convert collection to array
     *
     * @param c
     * @return
     */
    public static Object[] listToArray(Collection<?> c) {
        if (!isEmpty(c)) {
            return c.toArray();
        }
        return null;
    }

    /**
     * Convert list to set
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> Set<T> listToSet(List<T> list) {
        return new HashSet<T>(list);
    }

    /**
     * Convert set to list
     *
     * @param set
     * @param <T>
     * @return
     */
    public static <T> List<T> setToList(Set<T> set) {
        return new ArrayList<T>(set);
    }

    /**
     * Traverse collection
     *
     * @param c
     * @param <T>
     * @return
     */
    public static <T> String traverseCollection(Collection<T> c) {
        if (!isEmpty(c)) {
            int len = c.size();
            StringBuilder builder = new StringBuilder(len);
            int i = 0;
            for (T t : c) {
                if (t == null) {
                    continue;
                }
                builder.append(t.toString());
                i++;
                if (i < len) {
                    builder.append(DELIMITER);
                }
            }
            return builder.toString();
        }
        return null;
    }
}
