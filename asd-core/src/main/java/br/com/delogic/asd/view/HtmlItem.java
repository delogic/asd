package br.com.delogic.asd.view;

public abstract class HtmlItem {

    public abstract String render();

    @Override
    public String toString() {
        return render();
    }

}
