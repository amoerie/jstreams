package com.amoerie.streams;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FlatStream<E> extends Stream<E> {

    private Stream<Stream<E>> streams;

    public FlatStream(Stream<Stream<E>> streams) {
        this.streams = streams;
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<Stream<E>> streamsIterator = streams.iterator();
        return new Iterator<E>() {

            private Iterator<E> nonEmptyStreamIterator;

            private boolean tryEnsureNonEmptyIterator() {
                // if initialized and it has a next value, we're good to go
                if(nonEmptyStreamIterator != null && nonEmptyStreamIterator.hasNext())
                    return true;
                // the current nonEmptyStreamIterator is useless, try load the next one. if this was the last iterator, we're done
                if(!streamsIterator.hasNext())
                    return false;
                nonEmptyStreamIterator = streamsIterator.next().iterator();
                // recursive call because the flatMapper could produce empty iterators, in which case we need to move up to the next one again
                return nonEmptyStreamIterator.hasNext() || tryEnsureNonEmptyIterator();
            }

            @Override
            public boolean hasNext() {
                return tryEnsureNonEmptyIterator();
            }

            @Override
            public E next() {
                if(!tryEnsureNonEmptyIterator()) {
                    throw new NoSuchElementException();
                }
                return nonEmptyStreamIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
