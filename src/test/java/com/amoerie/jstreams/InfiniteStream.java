package com.amoerie.jstreams;

import java.util.Iterator;

public class InfiniteStream<E> extends Stream<E> {

    private final E value;

    public InfiniteStream(E value) {
        this.value = value;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public E next() {
                return value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
