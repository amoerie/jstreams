package com.amoerie.jstreams;

import java.util.Iterator;

class IterableStream<E> extends Stream<E> {
    private final Iterable<E> iterable;

    public IterableStream(Iterable<E> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator<E> iterator() {
        return this.iterable.iterator();
    }
}
