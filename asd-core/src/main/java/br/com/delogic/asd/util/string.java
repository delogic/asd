package br.com.delogic.asd.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class string {

    /**
     * Removes characters accents like á à â ã ç replacing them by a a a a c
     *
     * @param s
     * @return A normalized string without accents
     */
    public static synchronized String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

}
