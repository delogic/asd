package br.com.delogic.asd.view.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.com.delogic.asd.content.BaseTest;
import br.com.delogic.jfunk.Convert;
import br.com.delogic.jfunk.Converter;

public class HtmlFormBuilderTest extends BaseTest {

    private List<FormItem> formItems;

    @Mock
    private HtmlItemFactory mockHtmlItemFactory;

    private Map<String, HtmlItemFactory> htmlFactories;

    private HtmlFormBuilder htmlFormBuilder;

    private HtmlForm form;

    private HashMap<String, List<String>> values;

    private HashMap<String, List<String>> options;

    @Before
    public void init() {
        htmlFactories = new HashMap<String, HtmlItemFactory>();
        formItems = new ArrayList<FormItem>();
        values = new HashMap<String, List<String>>();
        options = new HashMap<String, List<String>>();
        htmlFormBuilder = new HtmlFormBuilder(htmlFactories, formItems);
    }

    @Test
    public void shouldBuildHtmlFormWithAllParameters() {
        givenFormItem("text", "userId");
        givenFormItem("text", "userName");
        givenFormItem("radio", "gender");
        givenHtmlItemFactory();
        givenDataFor("userId", "userName", "gender");
        givenOptionsFor("userId", "userName", "gender");
        whenBuildingHtmlForm();
        thenFactoryCalledWith(formItems.get(0), options.get("userId"), values.get("userId"));
        thenFactoryCalledWith(formItems.get(1), options.get("userName"), values.get("userName"));
        thenFactoryCalledWith(formItems.get(2), options.get("gender"), values.get("gender"));
        thenHtmlFormIsCreated();
    }

    private void thenHtmlFormIsCreated() {
        assertNotNull(form);
    }

    private void givenOptionsFor(String ... ids) {
        for (String id : ids) {
            options.put(id, Arrays.asList("op1", "op2", id));
        }
        htmlFormBuilder.setOptions(options);
    }

    private void givenDataFor(String ... ids) {
        for (String id : ids) {
            values.put(id, Arrays.asList("data"));
        }
        htmlFormBuilder.setValues(values);
    }

    private void givenFormItem(String type, String name) {
        FormItem item = new FormItem(type).name(name);
        formItems.add(item);
    }

    private void givenHtmlItemFactory() {
        String[] types = Convert.from(formItems).toListOf(new Converter<FormItem, String>() {
            @Override
            public String to(FormItem in) {
                return in.getType();
            }
        }).toArray(new String[] {});
        Mockito.when(mockHtmlItemFactory.getTypesResolved()).thenReturn(types);
        for (String type : types) {
            htmlFactories.put(type, mockHtmlItemFactory);
        }
    }

    private void whenBuildingHtmlForm() {
        form = htmlFormBuilder.build();
    }

    private void thenFactoryCalledWith(FormItem formItem, List<? extends Object> options, List<? extends Object> data) {
        Mockito.verify(mockHtmlItemFactory).create(Mockito.eq(formItem), Mockito.eq(options), Mockito.eq(data));
    }


    @Test
    public void shouldBuildHtmlFormWithData() {
        givenFormItem("text", "userName");
        givenFormItem("radio", "gender");
        givenHtmlItemFactory();
        givenDataFor("userName");
        whenBuildingHtmlForm();
        thenFactoryCalledWith(formItems.get(0), Collections.emptyList(), values.get("userName"));
        thenFactoryCalledWith(formItems.get(1), Collections.emptyList(), Collections.emptyList());
        thenHtmlFormIsCreated();
    }

    @Test
    public void shouldBuildHtmlFormWithOptions() {
        givenFormItem("text", "userName");
        givenHtmlItemFactory();
        givenOptionsFor("userName");
        whenBuildingHtmlForm();
        thenFactoryCalledWith(formItems.get(0), options.get("userName"), Collections.emptyList());
        thenHtmlFormIsCreated();
    }

    @Test
    public void shouldBuildHtmlForm() {
        givenFormItem("text", "userName");
        givenFormItem("yesno", "likesCake");
        givenHtmlItemFactory();
        whenBuildingHtmlForm();
        thenFactoryCalledWith(formItems.get(0), Collections.emptyList(), Collections.emptyList());
        thenFactoryCalledWith(formItems.get(1), Collections.emptyList(), Collections.emptyList());
        thenHtmlFormIsCreated();
    }

}
