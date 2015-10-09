package com.amoerie.jstreams.functions;

/**
 * Represents a mapping operation, turning an element into something, for example by retrieving a property, computing some value or just about anything you can think of.
 * @param <E> the type of element that is put into the mapper
 * @param <R> the type of the result that comes out of the mapper
 */
public interface Mapper<E, R> {
    /**
     * Maps an element to something else
     * @param e the element
     * @return a result that was somehow determined using the element
     */
    R map(E e);
}
