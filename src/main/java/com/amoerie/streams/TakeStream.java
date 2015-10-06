package com.amoerie.streams;

import java.util.Iterator;
import java.util.NoSuchElementException;

class TakeStream<E> extends Stream<E> {

    private final Stream<E> stream;
    private final int number;

    public TakeStream(Stream<E> stream, int number){
        this.stream = stream;
        this.number = number;
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> iterator = stream.iterator();
        return new Iterator<E>() {
            private int taken = 0;

            @Override
            public boolean hasNext() {
                return taken < number && iterator.hasNext();
            }

            @Override
            public E next() {
                if(taken == number) {
                    throw new NoSuchElementException();
                }
                taken++;
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
