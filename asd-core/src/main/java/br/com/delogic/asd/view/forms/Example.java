package br.com.delogic.asd.view.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Example {

    public static final Map<String, List<String>> dados = new HashMap<String, List<String>>();
    private static HtmlItemFactory htmlItemFactory;

    public static final FormModel UPLOAD_FORM = new FormModel(htmlItemFactory).addItems(
        new FormItem("text").name("nomeOperador").label("Qual é o nome do operador ?"),
        new FormItem("radio").name("tratamento").label("Qual tipo de tratamento ?"),
        new FormItem("checkbox").name("instrumento").label("Qual instrumento de medição ?"),
        new FormItem("file").name("arquivo1").label("Envie a planilha preenchida"),
        new FormItem("simnao").name("construirDispositivo").label("Construção de dispositivo ?")
        );

    public void exemple() {
        HtmlForm form = UPLOAD_FORM.builder().setOptions(dados).setValues(dados).build();
        form.getItems();
        new HtmlItemFactory() {

            @Override
            public String[] getTypesResolved() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HtmlItem create(FormItem formItem, List<? extends Object> options, List<? extends Object> data) {
                for (Object info : data) {

                }
                return null;
            }
        };
    }
}
