package com.amoerie.streams;

/**
 * Created by alexa on 06-Oct-15.
 */
public abstract class Group<TKey, T> extends Stream<T> {
    public abstract TKey getKey();
}
