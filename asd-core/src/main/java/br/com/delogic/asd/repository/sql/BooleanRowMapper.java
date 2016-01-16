package br.com.delogic.asd.repository.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Row mapper created to return Lists of Boolean values.
 *
 * @author celio@delogic.com.br
 *
 * @param <E>
 */

public class BooleanRowMapper <E> implements RowMapper<E>  {
    
    @SuppressWarnings("unchecked")
    public E mapRow(ResultSet rs, int rowNum) throws SQLException {
        Boolean value = rs.getBoolean(1);
        return (E) value;
    }
}
