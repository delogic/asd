package br.com.delogic.asd.view.forms;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.com.delogic.asd.content.BaseTest;
import br.com.delogic.asd.view.forms.FormItem;
import br.com.delogic.asd.view.forms.FormModel;
import br.com.delogic.asd.view.forms.HtmlFormBuilder;
import br.com.delogic.asd.view.forms.HtmlItemFactory;
import br.com.delogic.jfunk.Convert;
import br.com.delogic.jfunk.Converter;

public class FormModelTest extends BaseTest {

    private FormModel model;

    @Mock
    private HtmlItemFactory mockHtmlItemFactory;

    private List<FormItem> items;

    private HtmlFormBuilder builder;

    @Before
    public void init() {
        model = new FormModel(mockHtmlItemFactory);
        items = new ArrayList<FormItem>();
    }

    @Test
    public void shouldAddFormItem() {
        givenFormItem("text");
        givenFormItem("radio");
        givenFormItem("checkbox");
        whenAddingFormItems();
        thenFormItemsAmout(3);
        thenFormItemsTypes("text", "radio", "checkbox");
        thenFormItemsIsModifiabled();
        thenHtmlItemFactoriesIsModifiable();
        thenCanRegisterAnotherHtmlItemFactory();
    }

    private void thenHtmlItemFactoriesIsModifiable() {
        model.getHtmlItemFactories().add(mockHtmlItemFactory);
    }

    private void thenCanRegisterAnotherHtmlItemFactory() {
        model.registerHtmlItemFactory(mockHtmlItemFactory);
    }

    private void givenFormItem(String string) {
        items.add(new FormItem(string));
    }

    private void whenAddingFormItems() {
        model.addItems(items.toArray(new FormItem[] {}));
    }

    private void thenFormItemsAmout(int i) {
        assertEquals(i, model.getFormItems().size());
    }

    private void thenFormItemsTypes(String... types) {
        List<String> itemTypes = Convert.from(model.getFormItems()).toListOf(new Converter<FormItem, String>() {
            @Override
            public String to(FormItem in) {
                return in.getType();
            }
        });
        for (String type : types) {
            assertTrue(itemTypes.contains(type));
        }
    }

    private void thenFormItemsIsModifiabled() {
        model.getFormItems().add(new FormItem("text"));
    }

    @Test
    public void shouldCreateBuilder() {
        givenFormItemsAdded("text", "radio", "text");
        givenHtmlItemFactoryTypes("text", "radio");
        whenCreatingBuilder();
        thenBuilderIsCreated();
        thenFormItemsAmout(3);
        thenFormItemsTypes("text", "radio");
        thenCannotRegisterAnotherHtmlItemFactory();
        thenFormItemsIsNotModifiabled();
        thenHtmlItemFactoriesIsNotModifiable();
    }

    private void thenCannotRegisterAnotherHtmlItemFactory() {
        try {
            model.registerHtmlItemFactory(mockHtmlItemFactory);
            fail("should had thrown exception");
        } catch (Exception e) {
        }
    }

    private void givenFormItemsAdded(String... types) {
        for (String type : types) {
            model.addItems(new FormItem(type));
        }
    }

    private void givenHtmlItemFactoryTypes(String... types) {
        Mockito.when(mockHtmlItemFactory.getTypesResolved()).thenReturn(types);
    }

    private void whenCreatingBuilder() {
        builder = model.builder();
    }

    private void thenBuilderIsCreated() {
        assertNotNull(builder);
    }

    private void thenFormItemsIsNotModifiabled() {
        try {
            model.getFormItems().add(new FormItem("text"));
            fail("should had thrown exception");
        } catch (Exception e) {}
    }

    private void thenHtmlItemFactoriesIsNotModifiable() {
        try {
            model.getHtmlItemFactories().add(mockHtmlItemFactory);
            fail("should had thrown exception");
        } catch (Exception e) {}
    }

}
