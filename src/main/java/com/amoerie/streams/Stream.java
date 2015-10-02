package com.amoerie.streams;

import java.util.*;

import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Represents a collection of elements that are not known at construction time
 * This is a wrapper around the Iterator class, providing more functional methods than is standard provided by Java,
 * by combining some classic functional paradigms such as map and flatMap with the callback signatures that RxJava provides.
 * @param <E> the type of each element in the stream
 */
public abstract class Stream<E> implements Iterable<E> {

    protected Stream() {}

    /**
     * Creates a new empty stream, containing no elements
     * @param <E> the type of the elements of this stream
     * @return a new empty stream containing no elements
     */
    public static <E> Stream<E> empty() {
        return new EmptyStream<>();
    }

    /**
     * Creates a new singleton stream, containing exactly one element
     * @param element the single element
     * @param <E> the type of the single element
     * @return a new stream containing exactly one element
     */
    public static <E> Stream<E> singleton(E element) {
        return new SingletonStream<>(element);
    }

    /**
     * Creates a new stream from the provided array of elements
     * @param array the array of elements
     * @param <E> the type of the elements
     * @return a new stream containing the elements of the array
     */
    public static <E> Stream<E> create(E[] array) {
        return array != null
                ? create(Arrays.asList(array))
                : Stream.<E>empty();
    }

    /**
     * Creates a new stream from the provided iterable
     * This is a lazy operation, it does not consume the iterable until a consuming operation is called, such as toList()
     * @param iterable an iterable containing elements
     * @param <E> the type of an element
     * @return a new stream containing the elements of the iterable
     */
    public static <E> Stream<E> create(Iterable<E> iterable) {
        return new IterableStream<>(iterable);
    }

    /**
     * Casts every element of this stream to another class
     * @param clazz the class to cast to
     * @param <C> the type of the class to cast to
     * @return a new stream containing every element casted to another class
     */
    public <C> Stream<C> cast(Class<C> clazz) {
        if(clazz == null) throw new IllegalArgumentException("The class to cast to cannot be null");
        return new CastStream<>(this, clazz);
    }

    /**
     * Concatenates this stream with another stream
     * @param other the other stream to concatenate with
     * @return a new stream containing all the elements of this stream and the other stream
     */
    public Stream<E> concat(Stream<E> other) {
        return new FlatStream<>(create(Arrays.asList(this, other)));
    }

    /**
     * Filters the elements of this stream with the given filter.
     * @param filter the predicate that returns true or false for a given element
     * @return a new stream containing only the elements that satisfied the filter
     */
    public Stream<E> filter(Func1<E, Boolean> filter) {
        if(filter == null) throw new IllegalArgumentException("The filter function cannot be null");
        return new FilteredStream<>(this, filter);
    }

    /**
     * Maps each element of this stream to another value
     * @param mapper the function that takes an element as its input and returns any other value
     * @param <R> the type of the element after it has been mapped
     * @return a new stream containing the mapped elements
     */
    public <R> Stream<R> map(Func1<E, R> mapper) {
        if(mapper == null) throw new IllegalArgumentException("The mapping function cannot be null");
        return new MappedStream<>(this, mapper);
    }

    /**
     * Maps each element of this stream to a separate stream, and then flattens the result to one single stream
     * @param mapper the function that turns one element into a stream of values
     * @param <R> the type of one mapped element
     * @return a new stream containing all elements of all the streams the mapper function created
     */
    public <R> Stream<R> flatMap(Func1<E, Stream<R>> mapper) {
        if(mapper == null) throw new IllegalArgumentException("The mapping function cannot be null");
        return new FlatMappedStream<>(this, mapper);
    }

    /**
     * Reduces this stream to a single value by repeatedly applying the same reduction operator to the
     * current value and the next element.
     * For example, to reduce a stream of integers to a sum:
     * numbers.reduce((sum, number) => sum + number, 0)
     * @param reduction the reduction function that turns the current value and the next element into the next value
     * @param <R> the type of the result of the reducted stream
     * @return the final value after reducing every element
     */
    public <R> R reduce(Func2<R, E, R> reduction, R initialValue) {
        if(reduction == null) throw new IllegalArgumentException("The reduction function cannot be null");
        if(initialValue == null) throw new IllegalArgumentException("The initial value cannot be null");
        R value = initialValue;
        for (E e : this) {
            value = reduction.call(value, e);
        }
        return value;
    }

    /**
     * Sorts this stream using the provided comparator. Note that this is a greedy operation, meaning that this will cause
     * the stream to be materialized for it to be sorted.
     * @param comparator the comparator to use as the basis for the sorting
     * @return a new stream containing all elements of this stream in the order as specified by the comparator
     */
    public Stream<E> sort(Comparator<E> comparator) {
        if(comparator == null) throw new IllegalArgumentException("comparator cannot be null");
        List<E> list = toList();
        Collections.sort(list, comparator);
        return create(list);
    }

    /**
     * Sorts this stream based on a property of each element, provided that that property implements Comparable.
     * @param propertySelector the function that extracts a value from an element so it can be used as the basis for the comparison
     * @param <T> the type of the property that is the basis for the comparison
     * @return a new stream containing all elements of this stream sorted by the given property
     */
    public <T extends Comparable<T>> Stream<E> sortBy(final Func1<E, T> propertySelector) {
        if(propertySelector == null) throw new IllegalArgumentException("property selector cannot be null!");
        return sort(new Comparator<E>() {
            @Override
            public int compare(E left, E right) {
                return propertySelector.call(left).compareTo(propertySelector.call(right));
            }
        });
    }

    /**
     * Turns this stream into a list
     * @return a new list containing all the elements of this stream
     */
    public List<E> toList() {
        return reduce(new Func2<List<E>, E, List<E>>() {
            @Override
            public List<E> call(List<E> list, E element) {
                list.add(element);
                return list;
            }
        }, new ArrayList<E>());
    }

    /**
     * Turns this stream into a set
     * @return a new set containing the elements of this stream
     */
    public Set<E> toSet() {
        return reduce(new Func2<Set<E>, E, Set<E>>() {
            @Override
            public Set<E> call(Set<E> set, E element) {
                set.add(element);
                return set;
            }
        }, new HashSet<E>());
    }
}
