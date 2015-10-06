package com.amoerie.streams;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

import java.util.*;

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
        if(array == null) throw new IllegalArgumentException("Cannot create a stream for this array because it is null");
        return create(Arrays.asList(array));
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
     * Maps each element of this stream to a separate stream, and then flattens the result to one single stream
     * @param mapper the function that turns one element into a stream of values
     * @param <R> the type of one mapped element
     * @return a new stream containing all elements of all the streams the mapper function created
     */
    public <R> Stream<R> flatMap(Func1<E, Stream<R>> mapper) {
        if(mapper == null) throw new IllegalArgumentException("The mapping function cannot be null");
        return new FlatStream<>(new MappedStream<>(this, mapper));
    }

    /**
     * Calculates the amount of elements in this stream
     * @return the length of this stream
     */
    public int length() {
        return reduce(new Func2<Integer, E, Integer>() {
            @Override
            public Integer call(Integer length, E e) {
                return length + 1;
            }
        }, 0);
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
     * Reduces this stream to a single value by repeatedly applying the same reduction operator to the
     * current value and the next element.
     * For example, to reduce a stream of integers to a sum:
     * numbers.reduce((sum, number) => sum + number, 0)
     * @param reducer the reduction function that turns the current value and the next element into the next value
     * @param <R> the type of the result of the reducted stream
     * @return the final value after reducing every element
     */
    public <R> R reduce(Func2<R, E, R> reducer, R initialValue) {
        if(reducer == null) throw new IllegalArgumentException("The reducer function cannot be null");
        if(initialValue == null) throw new IllegalArgumentException("The initial value cannot be null");
        R value = initialValue;
        for (E e : this) {
            value = reducer.call(value, e);
        }
        return value;
    }

    /**
     * Skips a certain number of elements of this stream
     * @param number the number of items to skip
     * @return a new stream containing the remaining elements of this stream after skipping a certain number of elements
     */
    public Stream<E> skip(int number) {
        if(number < 0) throw new IllegalArgumentException("Cannot skip a negative number of elements");
        return new SkipStream<>(this, number);
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
     * Takes a certain number of elements from this stream and drops the remaining elements
     * @param number the number of items to take
     * @return a new stream containing only the first n elements of this stream
     */
    public Stream<E> take(int number) {
        if(number < 0) throw new IllegalArgumentException("Cannot take a negative number of elements");
        return new TakeStream<>(this, number);
    }

    /**
     * Groups this stream into chunks based on the key per element that is retrieved via the keySelector
     * @param keySelector a function that returns the grouping key for a given element
     * @param <K> the type of the key
     * @return a stream containing groups as its elements
     */
    public <K> Stream<Group<K, E>> groupBy(Func1<E, K> keySelector) {
        if(keySelector == null) throw new IllegalArgumentException("cannot group a stream without a key! keySelector cannot be null");
        return new GroupedStream<>(this, keySelector);
    }

    /**
     * Turns this stream into an observable
     * @return a new cold Observable with the elements of this stream
     */
    public Observable<E> toObservable() {
        return Observable.from(this);
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
