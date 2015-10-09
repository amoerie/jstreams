package com.amoerie.jstreams.functions;

/**
 * Represents a filter operation (also commonly known as a predicate)
 * @param <E> the type of element this filter can apply to
 */
public interface Filter<E> {
    /**
     * Applies this filter to the element.
     * @param e the element
     * @return true if the element satisfies this filter or false otherwise
     */
    boolean apply(E e);
}
