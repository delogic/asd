package br.com.delogic.asd.util;

public interface Converter<In, Out> {

    Out to(In in);

}
