package br.com.delogic.asd.datetime;

import java.util.Calendar;

public class LocalDateTimeManager implements DateTimeManager {

    @Override
    public Calendar getNow() {
        return Calendar.getInstance();
    }

}
