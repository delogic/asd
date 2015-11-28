package br.com.delogic.asd.view.forms;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

public class FormModel {

    private Map<String, HtmlItemFactory> registeredHtmlItemFactories;
    private List<FormItem> formItems = new LinkedList<FormItem>();
    private List<HtmlItemFactory> htmlItemFactories = new LinkedList<HtmlItemFactory>();

    public FormModel(HtmlItemFactory htmlItemFactory) {
        htmlItemFactories.add(htmlItemFactory);
    }

    public FormModel addItems(FormItem... items) {
        for (FormItem formItem : items) {
            formItems.add(formItem);
        }
        return this;
    }

    public HtmlFormBuilder builder() {
        if (registeredHtmlItemFactories == null) {
            registeredHtmlItemFactories = Collections.unmodifiableMap(createHtmlItemFactories());
            formItems = Collections.unmodifiableList(formItems);
            htmlItemFactories = Collections.unmodifiableList(htmlItemFactories);
            int index = 0;
            for (FormItem formItem : formItems) {
                formItem.setIndex(index++);
            }
        }
        return new HtmlFormBuilder(registeredHtmlItemFactories, formItems);
    }

    private Map<String, HtmlItemFactory> createHtmlItemFactories() {
        Map<String, HtmlItemFactory> facs = new HashMap<String, HtmlItemFactory>();
        for (HtmlItemFactory htmlItemFactory : htmlItemFactories) {
            for (String type : htmlItemFactory.getTypesResolved()) {
                facs.put(type, htmlItemFactory);
            }
        }
        return facs;
    }

    public FormModel registerHtmlItemFactory(HtmlItemFactory htmlFormFactory) {
        Assert.state(registeredHtmlItemFactories == null, "You cannot register an HtmlItemFactory if the builder has already been called");
        htmlItemFactories.add(htmlFormFactory);
        return this;
    }

    public List<FormItem> getFormItems() {
        return formItems;
    }

    public List<HtmlItemFactory> getHtmlItemFactories() {
        return htmlItemFactories;
    }

}
