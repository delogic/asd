package br.com.delogic.asd.testes;

public class View {

    
    
    
    
    public static void main(String[] args) {
        View masterView = new View();
        View innerView = masterView.inherit();
        innerView.add(Input.text()); //diretivas ? fragmentos?
        View cadastroView = new View();
        cadastroView
            .add(Campos.labelComInput("nome", "Nome do Cliente"))
            .add(Campos.labelComInput("idade", "Idade"))
            .add(Campos.labelComRadio("sexo", "Nom do sexo"));
        innerView.wrappedBy(masterView);
    }

    private View wrappedBy(View masterView) {
        // TODO Auto-generated method stub
        return this;
    }

    private View add(View view) {
        return this;
    }

    private View inherit() {
        return null;
    }
    
}
