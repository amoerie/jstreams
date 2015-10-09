package com.amoerie.jstreams.functions;

/**
 * Represents a reducing function.
 * @param <E> the type of element that gets put into the reducer
 * @param <R> the type of the final value that is returned
 */
public interface Reducer<E, R> {
    /**
     * Reduces the next element to a single result
     * @param r the result so far of the already reduced elements
     * @param e the next element
     * @return a single result that is composed from the result so far and the next element
     */
    R reduce(R r, E e);
}
