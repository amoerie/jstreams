package com.amoerie.jstreams.functions;

/**
 * Represents any operation returning no result.
 *
 * @param <E>
 *            the type of element this operation can apply to
 */
public interface Consumer<E> {
	/**
	 * Applies this operation to the element.
	 * 
	 * @param e
	 *            the element
	 */
	void apply(E e);
}
