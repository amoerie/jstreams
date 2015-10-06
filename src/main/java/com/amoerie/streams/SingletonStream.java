package com.amoerie.streams;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SingletonStream<E> extends Stream<E> {

    private final E element;

    public SingletonStream(E element) {
        this.element = element;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private boolean isConsumed = false;

            @Override
            public boolean hasNext() {
                return !isConsumed;
            }

            @Override
            public E next() {
                if(isConsumed) throw new NoSuchElementException();
                isConsumed = true;
                return SingletonStream.this.element;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
