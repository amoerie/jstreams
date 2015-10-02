# Streams

Contains Java 7 compatible streams that are immutable, lazy and chainable.
For the C# developers: Stream looks A LOT like ´IEnumerable`.

This library depends on the RxJava Func1, Func2, Action1, ... signatures for its callbacks.

Important features:

1. Streams are immutable. Once a stream has been created, you cannot change it. The only thing you can do is make new streams starting from that stream. 
2. Streams are lazy. Enumerating the elements of a stream is always postponed as much as possible. Operations which absolutely require iteration, which I call "greedy operators", are `toList`, `toSet`, `toObservable`, `sort`, `sortBy` and `length`. All other operators are lazy, they are delayed until a greedy operator is called.
For example, you can do the following:

      `List<String> firstFiveStrings = Stream.create(infiniteStrings).take(5).toList();`

3. Stream operators are chainable.
Every method on Stream always returns a new Stream

## Documentation

### `public abstract class Stream<E> implements Iterable<E>`

Represents a collection of elements that are not known at construction time This is a wrapper around the Iterator class, providing more functional methods than is standard provided by Java, by combining some classic functional paradigms such as map and flatMap with the callback signatures that RxJava provides.

 * **Parameters:** `<E>` — the type of each element in the stream

### `public static <E> Stream<E> empty()`

Creates a new empty stream, containing no elements

 * **Parameters:** `<E>` — the type of the elements of this stream
 * **Returns:** a new empty stream containing no elements

### `public static <E> Stream<E> singleton(E element)`

Creates a new singleton stream, containing exactly one element

 * **Parameters:**
   * `element` — the single element
   * `<E>` — the type of the single element
 * **Returns:** a new stream containing exactly one element

### `public static <E> Stream<E> create(E[] array)`

Creates a new stream from the provided array of elements

 * **Parameters:**
   * `array` — the array of elements
   * `<E>` — the type of the elements
 * **Returns:** a new stream containing the elements of the array

### `public static <E> Stream<E> create(Iterable<E> iterable)`

Creates a new stream from the provided iterable This is a lazy operation, it does not consume the iterable until a consuming operation is called, such as toList()

 * **Parameters:**
   * `iterable` — an iterable containing elements
   * `<E>` — the type of an element
 * **Returns:** a new stream containing the elements of the iterable

### `public <C> Stream<C> cast(Class<C> clazz)`

Casts every element of this stream to another class

 * **Parameters:**
   * `clazz` — the class to cast to
   * `<C>` — the type of the class to cast to
 * **Returns:** a new stream containing every element casted to another class

### `public Stream<E> concat(Stream<E> other)`

Concatenates this stream with another stream

 * **Parameters:** `other` — the other stream to concatenate with
 * **Returns:** a new stream containing all the elements of this stream and the other stream

### `public Stream<E> filter(Func1<E, Boolean> filter)`

Filters the elements of this stream with the given filter.

 * **Parameters:** `filter` — the predicate that returns true or false for a given element
 * **Returns:** a new stream containing only the elements that satisfied the filter

### `public <R> Stream<R> flatMap(Func1<E, Stream<R>> mapper)`

Maps each element of this stream to a separate stream, and then flattens the result to one single stream

 * **Parameters:**
   * `mapper` — the function that turns one element into a stream of values
   * `<R>` — the type of one mapped element
 * **Returns:** a new stream containing all elements of all the streams the mapper function created

### `public int length()`

Calculates the amount of elements in this stream

 * **Returns:** the length of this stream

### `public <R> Stream<R> map(Func1<E, R> mapper)`

Maps each element of this stream to another value

 * **Parameters:**
   * `mapper` — the function that takes an element as its input and returns any other value
   * `<R>` — the type of the element after it has been mapped
 * **Returns:** a new stream containing the mapped elements

### `public <R> R reduce(Func2<R, E, R> reducer, R initialValue)`

Reduces this stream to a single value by repeatedly applying the same reduction operator to the current value and the next element. For example, to reduce a stream of integers to a sum: numbers.reduce((sum, number) => sum + number, 0)

 * **Parameters:**
   * `reducer` — the reduction function that turns the current value and the next element into the next value
   * `<R>` — the type of the result of the reducted stream
 * **Returns:** the final value after reducing every element

### `public Stream<E> skip(int number)`

Skips a certain number of elements of this stream

 * **Parameters:** `number` — the number of items to skip
 * **Returns:** a new stream containing the remaining elements of this stream after skipping a certain number of elements

### `public Stream<E> sort(Comparator<E> comparator)`

Sorts this stream using the provided comparator. Note that this is a greedy operation, meaning that this will cause the stream to be materialized for it to be sorted.

 * **Parameters:** `comparator` — the comparator to use as the basis for the sorting
 * **Returns:** a new stream containing all elements of this stream in the order as specified by the comparator

### `public <T extends Comparable<T>> Stream<E> sortBy(final Func1<E, T> propertySelector)`

Sorts this stream based on a property of each element, provided that that property implements Comparable.

 * **Parameters:**
   * `propertySelector` — the function that extracts a value from an element so it can be used as the basis for the comparison
   * `<T>` — the type of the property that is the basis for the comparison
 * **Returns:** a new stream containing all elements of this stream sorted by the given property

### `public Stream<E> take(int number)`

Takes a certain number of elements from this stream and drops the remaining elements

 * **Parameters:** `number` — the number of items to take
 * **Returns:** a new stream containing only the first n elements of this stream

### `public Observable<E> toObservable()`

Turns this stream into an observable

 * **Returns:** a new cold Observable with the elements of this stream

### `public List<E> toList()`

Turns this stream into a list

 * **Returns:** a new list containing all the elements of this stream

### `public Set<E> toSet()`

Turns this stream into a set

 * **Returns:** a new set containing the elements of this stream
