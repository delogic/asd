package br.com.delogic.asd.view;

import java.util.List;

public class HtmlForm {

    private final List<HtmlItem> items;

    public HtmlForm(List<HtmlItem> createHtmlItems) {
        this.items = createHtmlItems;
    }

    public List<HtmlItem> getItems() {
        return items;
    }

}
