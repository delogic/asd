package br.com.delogic.asd.property;

import java.util.Map;

public interface PropertyManager {

    void getProperty(AsyncCallback<Map<String, String>> callback, String... propertyName);

}
