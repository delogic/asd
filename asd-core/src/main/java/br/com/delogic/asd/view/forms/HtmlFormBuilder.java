package br.com.delogic.asd.view.forms;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.delogic.jfunk.pattern.Builder;

public class HtmlFormBuilder implements Builder<HtmlForm> {

    private final Map<String, HtmlItemFactory> htmlItemFactories;
    private final List<FormItem> formItems;
    @SuppressWarnings("rawtypes")
    private Map data;
    @SuppressWarnings("rawtypes")
    private Map options;

    HtmlFormBuilder(Map<String, HtmlItemFactory> htmlItemFactories, List<FormItem> formItems) {
        this.htmlItemFactories = htmlItemFactories;
        this.formItems = formItems;
    }

    @Override
    public HtmlForm build() {
        return new HtmlForm(createHtmlItems());
    }

    private List<HtmlItem> createHtmlItems() {
        List<HtmlItem> htmlItems = new LinkedList<HtmlItem>();
        for (FormItem formItem : formItems) {
            String itemName = formItem.getName();
            HtmlItemFactory factory = htmlItemFactories.get(formItem.getType());
            htmlItems.add(factory.create(formItem, getOptions(itemName), getData(itemName)));
        }
        return htmlItems;
    }

    @SuppressWarnings("unchecked")
    private List<? extends Object> getOptions(String itemName) {
        if (options != null && options.containsKey(itemName)) {
            return (List<? extends Object>) options.get(itemName);
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private List<? extends Object> getData(String itemName) {
        if (data != null && data.containsKey(itemName)) {
            return (List<? extends Object>) data.get(itemName);
        }
        return Collections.emptyList();
    }

    public <O extends Object> HtmlFormBuilder setValues(Map<String, List<O>> data) {
        this.data = data;
        return this;
    }

    public <O extends Object> HtmlFormBuilder setOptions(Map<String, List<O>> options) {
        this.options = options;
        return this;
    }

}
