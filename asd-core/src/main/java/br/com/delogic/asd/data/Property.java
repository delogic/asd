package br.com.delogic.asd.data;

/**
 * Property class used to return lists of objects which have only an Id and a
 * Value like a list to be used in an html select element.
 *
 * @author celio@delogic.com.br
 *
 * @since 18/05/2014
 * @param <I>
 *            Id this each property
 * @param <V>
 *            Value of each property
 */
public class Property<I, V> extends Identity<I> {

    private I id;
    private V value;

    public Property(I id, V value) {
        this.id = id;
        this.value = value;
    }

    public Property() {}

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}
