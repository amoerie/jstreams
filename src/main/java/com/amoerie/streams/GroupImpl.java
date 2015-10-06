package com.amoerie.streams;

import java.util.Iterator;

/**
 * Created by alexa on 06-Oct-15.
 */
class GroupImpl<TKey, T> extends Group<TKey, T> {

    private TKey key;
    private Stream<T> stream;

    public GroupImpl(TKey key, Stream<T> stream) {
        this.key = key;
        this.stream = stream;
    }

    @Override
    public TKey getKey() {
        return this.key;
    }

    @Override
    public Iterator<T> iterator() {
        return this.stream.iterator();
    }
}
