package com.amoerie.jstreams.functions;

/**
 * Represents a consumption operation, returning no result.
 * @param <E> the type of element this consumer can consume
 */
public interface Consumer<E> {
	/**
	 * Consumes the next element
	 * @param e the next element
	 */
	void consume(E e);
}
