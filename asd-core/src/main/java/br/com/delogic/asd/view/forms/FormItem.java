package br.com.delogic.asd.view.forms;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormItem {

    private LinkedHashMap<String, Object> attributes = new LinkedHashMap<String, Object>();
    private String label;
    private String type;
    private String name;
    private int index;

    public FormItem(String type) {
        this.type = type;
    }

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

    public String getName() {
        return name;
    }

    public FormItem name(String name) {
        this.name = name;
        return this;
    }

    public int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

}
