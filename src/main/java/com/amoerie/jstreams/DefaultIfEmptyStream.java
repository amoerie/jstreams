package com.amoerie.jstreams;

import java.util.Iterator;

class DefaultIfEmptyStream<E> extends Stream<E> {

    private final E defaultElement;
    private final Stream<E> stream;

    public DefaultIfEmptyStream(final Stream<E> stream, final E defaultElement) {
        this.stream = stream;
        this.defaultElement = defaultElement;
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> iterator = stream.iterator();
        return iterator.hasNext() ? iterator : new SingletonStream<E>(defaultElement).iterator();
    }
}
