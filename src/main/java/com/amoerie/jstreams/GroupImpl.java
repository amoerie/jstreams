package com.amoerie.jstreams;

import java.util.Iterator;

class GroupImpl<TKey, T> extends Group<TKey, T> {

    private final TKey key;
    private final Stream<T> stream;

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
