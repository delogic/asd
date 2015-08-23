package br.com.delogic.asd.data;

/**
 * Represents the execution from a method which might return a value or not. In
 * this case the Result class must be used to guarantee we're always returning a
 * value, hence the Result, with the possibility to have the real value inside.
 *
 * @author celio@delogic.com.br
 *
 * @since 27/06/2014
 * @param <E>
 *            Value
 */
public class Result<E> {

    private final E value;
    private boolean hasValueCalled = false;

    /**
     * Creates a new instance not matter if the value is null or not null
     *
     * @param value
     */
    public Result(E value) {
        this.value = value;
    }

    /**
     * Obtains the value saved inside this result. To get this value first you
     * must check if this result any real value using the method hasValue()
     *
     * @return <E> value
     */
    public E getValue() {
        if (!hasValueCalled) {
            throw new IllegalStateException("To get the value you must first call hasValue method to make sure the value exists");
        }
        return value;
    }

    /**
     * Checks if this result contains a real value or not.
     *
     * @return false if obj == null
     */
    public boolean hasValue() {
        hasValueCalled = true;
        return value != null;
    }

}
