package br.com.delogic.asd.view.forms;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import br.com.delogic.asd.content.BaseTest;
import br.com.delogic.asd.view.forms.FormItem;

public class FormItemTest extends BaseTest {

    private String type;
    private String name;
    private String label;
    private FormItem item;
    private HashMap<String, Object> attrs;

    @Before
    public void init() {
        attrs = new HashMap<String, Object>();
    }

    @Test
    public void shouldCreateFormItem() {
        givenType("text");
        givenName("name");
        givenLabel("label");
        givenAttribute("data-bound", "attrDataBound");
        givenAttribute("search-uri", "attrSearchUri");
        whenCreatingFormItem();
        thenTypeIs("text");
        thenNameIs("name");
        thenLabelIs("label");
        thenHasAttribute("data-bound", "attrDataBound");
        thenHasAttribute("search-uri", "attrSearchUri");
    }

    private void thenHasAttribute(String attr, String val) {
        item.getAttributes().containsKey(attr);
        item.getAttributes().containsValue(val);
    }

    private void givenAttribute(String attr, String val) {
        attrs.put(attr, val);
    }

    private void givenType(String string) {
        type = string;
    }

    private void givenName(String string) {
        name = string;
    }

    private void givenLabel(String string) {
        label = string;
    }

    private void whenCreatingFormItem() {
        item = new FormItem(type).name(name).label(label);
    }

    private void thenTypeIs(String string) {
        assertEquals(string, item.getType());
    }

    private void thenNameIs(String string) {
        assertEquals(string, item.getName());
    }

    private void thenLabelIs(String string) {
        assertEquals(string, item.getLabel());
    }

}
