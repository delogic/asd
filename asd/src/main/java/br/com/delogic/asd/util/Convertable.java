package br.com.delogic.asd.util;

public interface Convertable<IN, OUT> {

    OUT convert(IN in);

}