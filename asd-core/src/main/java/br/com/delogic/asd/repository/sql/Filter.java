package br.com.delogic.asd.repository.sql;

public class Filter {

    private String key;
    private String value;

    public Filter() {

    }

    public Filter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
