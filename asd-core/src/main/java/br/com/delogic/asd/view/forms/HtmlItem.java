package br.com.delogic.asd.view.forms;

public abstract class HtmlItem {

    public abstract String render();

    @Override
    public String toString() {
        return render();
    }

}
