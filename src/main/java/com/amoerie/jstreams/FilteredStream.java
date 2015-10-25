package com.amoerie.jstreams;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.amoerie.jstreams.functions.Filter;

class FilteredStream<E> extends Stream<E> {
    private final Stream<E> stream;
    private final Filter<E> filter;

    FilteredStream(final Stream<E> stream, final Filter<E> filter) {
        this.stream = stream;
        this.filter = filter;
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> iterator = stream.iterator();
        return new Iterator<E>() {

            private boolean isNextElementReady;
            private E nextElement;

            private boolean tryPrepareNextElement() {
                while (iterator.hasNext() && !isNextElementReady) {
                    E next = iterator.next();
                    if (filter.apply(next)) {
                        nextElement = next;
                        return isNextElementReady = true;
                    }
                }
                return isNextElementReady;
            }

            @Override
            public boolean hasNext() {
                return tryPrepareNextElement();
            }

            @Override
            public E next() {
                if(!tryPrepareNextElement())
                    throw new NoSuchElementException();
                isNextElementReady = false;
                return nextElement;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
