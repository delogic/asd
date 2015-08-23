package br.com.delogic.asd.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class convert {

    @SuppressWarnings("unchecked")
    public static final <In, C extends Collection<In>> Convertible<In> from(C collection) {
        Convertible<In> convertible = new Convertible<In>();
        if (Has.content(collection)) {
            convertible.data = (In[]) collection.toArray();
        }
        return convertible;
    }

    public static class Convertible<In> {

        private In[] data;

        public <Out> List<Out> toListOf(final Converter<In, Out> converter) {
            final List<Out> list = new ArrayList<Out>();
            ForEach.element(data, new Each<In>() {
                public void each(In e, int index) {
                    list.add(converter.to(e));
                }
            });
            return list;
        }

        public <Out> Set<Out> toSetOf(final Converter<In, Out> converter) {
            final Set<Out> set = new HashSet<Out>();
            ForEach.element(data, new Each<In>() {
                public void each(In e, int index) {
                    set.add(converter.to(e));
                }
            });
            return set;
        }

    }

}
