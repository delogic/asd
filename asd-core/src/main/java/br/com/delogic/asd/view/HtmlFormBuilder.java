package br.com.delogic.asd.view;

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

    @SuppressWarnings("unchecked")
    private List<HtmlItem> createHtmlItems() {
        List<HtmlItem> htmlItems = new LinkedList<HtmlItem>();
        for (FormItem formItem : formItems) {
            String itemName = formItem.getName();
            List<? extends Object> itemData = (List<? extends Object>) data.get(itemName);
            List<? extends Object> itemOptions = (List<Object>) options.get(itemName);
            HtmlItemFactory factory = htmlItemFactories.get(formItem.getType());
            htmlItems.add(factory.create(formItem, itemData, itemOptions));
        }
        return htmlItems;
    }

    public <O extends Object> HtmlFormBuilder setData(Map<String, List<O>> data) {
        this.data = data;
        return this;
    }

    public <O extends Object> HtmlFormBuilder setOptions(Map<String, List<O>> options) {
        this.options = options;
        return this;
    }

}
