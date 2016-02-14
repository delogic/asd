package br.com.delogic.asd.testes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * https://developer.mozilla.org/en-US/docs/Web/Guide/HTML/HTML5/
 * Constraint_validation
 * 
 * @author Celio
 *
 */
public class MetaField {

    private final List<Constraint<?>> constraints = new ArrayList<>();
    private boolean required;
    private Integer maxLength;
    private String pattern;
    private String type;
    private Object min;
    private Object max;

    public MetaField required() {
        required = true;
        return this;
    }

    public boolean isRequired() {
        return required;
    }

    public MetaField maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public MetaField pattern(String regex) {
        this.pattern = regex;
        return this;
    }

    public String getPattern() {
        return pattern;
    }

    public MetaField min(double min) {
        this.min = min;
        return this;
    }

    public Object getMin() {
        throw new UnsupportedOperationException("thinking about it");
    }

    public MetaField max(double max) {
        this.max = max;
        return this;
    }

    public Object getMax() {
        throw new UnsupportedOperationException("thinking about it");
    }

    public MetaField min(Date min) {
        this.min = min;
        return this;
    }

    public MetaField max(Date max) {
        this.max = max;
        return this;
    }

    public MetaField constraint(Constraint<?> constraint) {
        this.constraints.add(constraint);
        return this;
    }

    public MetaField type(String dataType) {
        this.type = dataType;
        return this;
    }

    public String getType() {
        return type;
    }

}
