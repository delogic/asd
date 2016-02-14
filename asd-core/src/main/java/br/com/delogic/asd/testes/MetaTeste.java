package br.com.delogic.asd.testes;

public class MetaTeste {

    public static void main(String[] args) {
        System.out.println(UserEntity.META.getClass().getDeclaringClass());
        System.out.println(UserEntity.META.getClass().getEnclosingClass());
        
    }

}
