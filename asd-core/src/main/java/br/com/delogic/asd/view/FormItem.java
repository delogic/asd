package br.com.delogic.asd.view;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormItem {

    protected LinkedHashMap<String, Object> attributes = new LinkedHashMap<String, Object>();
    private String label;
    private String type;
    private String name;

    public FormItem(String type) {
        this.type = type;
    }

    public FormItem(Enum<?> type) {
        this(type.name());
    }

    public FormItem() {}

    public FormItem attribute(String attributeName, Object attributeValue) {
        attributes.put(attributeName, attributeValue);
        return this;
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public String getLabel() {
        return label;
    }

    public FormItem label(String label) {
        this.label = label;
        return this;
    }

    public String getType() {
        return type;
    }

    public FormItem type(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public FormItem name(String name) {
        this.name = name;
        return this;
    }

}
