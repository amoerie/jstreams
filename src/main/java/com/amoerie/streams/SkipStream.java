package com.amoerie.streams;

import java.util.Iterator;

class SkipStream<E> extends Stream<E> {

    private final Stream<E> stream;
    private final int number;

    public SkipStream(Stream<E> stream, int number) {
        this.stream = stream;
        this.number = number;
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> iterator = this.stream.iterator();
        for(int skipped = 0; skipped < number && iterator.hasNext(); skipped++)
            iterator.next();
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
