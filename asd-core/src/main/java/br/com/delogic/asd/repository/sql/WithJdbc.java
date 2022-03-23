package br.com.delogic.asd.repository.sql;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public interface WithJdbc<T> {

    T with(NamedParameterJdbcTemplate template);

}
