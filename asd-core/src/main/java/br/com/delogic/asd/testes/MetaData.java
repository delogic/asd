package br.com.delogic.asd.testes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetaData implements Map<String, MetaField> {

    private final Map<String, MetaField> fields = new HashMap<String, MetaField>();
    private final Class<?> type;
    private List<Constraint<?>> constraints;

    public MetaData(Class<?> type) {
        this.type = type;
    }

    public MetaData add(String field, MetaField metaField) {
        put(field, metaField);
        return this;
    }

    public MetaField extend(String field) {
        throw new UnsupportedOperationException("thinking about it");
    }

    public MetaData extend() {
        throw new UnsupportedOperationException("thinking about it");
    }

    public MetaData constraint(Constraint<?> constraint) {
        constraints.add(constraint);
        return this;
    }

    @Override
    public int size() {
        return fields.size();
    }

    @Override
    public boolean isEmpty() {
        return fields.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return fields.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return fields.containsValue(value);
    }

    @Override
    public MetaField get(Object key) {
        return fields.get(key);
    }

    @Override
    public MetaField put(String key, MetaField value) {
        return fields.put(key, value);
    }

    @Override
    public MetaField remove(Object key) {
        return fields.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends MetaField> m) {
        fields.putAll(m);
    }

    @Override
    public void clear() {
        fields.clear();
    }

    @Override
    public Set<String> keySet() {
        return fields.keySet();
    }

    @Override
    public Collection<MetaField> values() {
        return fields.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, MetaField>> entrySet() {
        return fields.entrySet();
    }

    public List<Constraint<?>> getConstraints() {
        if (constraints == null) {
            constraints = new ArrayList<>();
        }
        return constraints;
    }

    public Class<?> getType() {
        return type;
    }

}
