package br.com.delogic.asd.view;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FormModel {

    private final Map<String, HtmlItemFactory> registeredHtmlItemFactories = new LinkedHashMap<String, HtmlItemFactory>();
    private final List<FormItem> formItems = new LinkedList<FormItem>();

    public FormModel addElements(FormItem... items) {
        for (FormItem formItem : items) {
            formItems.add(formItem);
        }
        return this;
    }

    public HtmlFormBuilder builder() {
        return new HtmlFormBuilder(Collections.unmodifiableMap(registeredHtmlItemFactories), Collections.unmodifiableList(formItems));
    }

    public FormModel addHtmlFactory(HtmlItemFactory htmlFormFactory) {
        for (String type : htmlFormFactory.getTypesResolved()) {
            registeredHtmlItemFactories.put(type, htmlFormFactory);
        }
        return this;
    }

}
