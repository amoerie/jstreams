package com.amoerie.streams;

import java.util.Iterator;

class IterableStream<E> extends Stream<E> {
    private final Iterable<E> iterable;

    public IterableStream(Iterable<E> iterable) {
        if(iterable == null) throw new IllegalArgumentException("iterable cannot be null");
        this.iterable = iterable;
    }

    @Override
    public Iterator<E> iterator() {
        return this.iterable.iterator();
    }
}
