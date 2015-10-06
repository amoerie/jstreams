package com.amoerie.streams;

import java.util.Iterator;
import java.util.NoSuchElementException;

class EmptyStream<E> extends Stream<E> {
    private final Iterator<E> emptyIterator;

    public EmptyStream() {
        this.emptyIterator = new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Iterator<E> iterator() {
        return this.emptyIterator;
    }
}
