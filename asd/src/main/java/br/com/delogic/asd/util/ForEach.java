package br.com.delogic.asd.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class executes the same functionality as the traditional for "each"
 * loop, with some benefits. The main motivation is to decrease risks avoiding
 * NullPointerException(s) performing these verifications prior to execution
 * allowing the developer to work on real business logic.
 * <p>
 * Some distinctions between {@code ForEach} and tradition "for each" loop:<br>
 * 1. Traditional loop will throw a NullPointerException if the collection is
 * null. {@code ForEach} doesn't.<br>
 * 2. Traditional loop will send you null objects if they are on the collection.
 * {@code ForEach} doesn't. <br>
 * 3. Traditional loop doesn't allow removing from the same list you're
 * iterating through. {@code ForEach} does.<br>
 * 4. Traditional loop doesn't provide the index for current iteration.
 * {@code ForEach} does.
 * <p>
 *
 * @author celio@delogic.com.br
 *
 * @since 11/05/2014
 */
public class ForEach {

    /**
     * Will iterate over the collection, for each non null element. If the
     * collection is null no execution will happen. Changes in the original loop
     * are allowed while iterating.
     *
     * @param collection
     *            to iterate over
     * @param each
     *            function to be executed
     */
    public static final <E> void element(Collection<E> collection, Each<E> each) {

        if (!Has.content(collection)) {
            return;
        }

        int index = 0;
        List<E> elements = new ArrayList<E>(collection);
        for (E element : elements) {
            if (element != null) {
                each.each(element, index);
                index++;
            }
        }
        elements.clear();

    }

    /**
     * Will iterate over the collection, for each non null element. If the
     * collection is null no execution will happen. Changes in the original loop
     * are allowed while iterating.
     *
     * @param array
     *            to iterate over
     * @param each
     *            function to be executed
     */
    public static final <E> void element(E[] array, Each<E> each) {
        if (!Has.content(array)) {
            return;
        }

        int index = 0;
        List<E> elements = Arrays.asList(array);
        for (E element : elements) {
            if (element != null) {
                each.each(element, index);
                index++;
            }
        }
    }

}
