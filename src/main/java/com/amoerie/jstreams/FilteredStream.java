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
            private boolean isNextFilteredElementReady;
            private E nextFilteredElement;

            private void prepareNextFilteredElement() {
                isNextFilteredElementReady = false;
                while (iterator.hasNext() && !isNextFilteredElementReady) {
                    E next = iterator.next();
                    if (filter.apply(next)) {
                        nextFilteredElement = next;
                        isNextFilteredElementReady = true;
                    }
                }
            }

            @Override
            public boolean hasNext() {
                if (!isNextFilteredElementReady) prepareNextFilteredElement();
                return isNextFilteredElementReady;
            }

            @Override
            public E next() {
                if (!isNextFilteredElementReady) prepareNextFilteredElement();
                if (!isNextFilteredElementReady) throw new NoSuchElementException();
                isNextFilteredElementReady = false;
                return nextFilteredElement;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
