package br.com.delogic.asd.content;

import java.util.Iterator;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.delogic.jfunk.Has;

public class DatabaseIterator implements Iterator<Long>, InitializingBean {

    private String query;
    private JdbcTemplate jdbcTemplate;

    @Inject
    private DataSource dataSource;

    @Override
    public boolean hasNext() {
        return dataSource != null && Has.content(query);
    }

    @Override
    public Long next() {
        return jdbcTemplate.queryForObject(query, Long.class);
    }

    @Override
    public void remove() {
        System.out.println("not implemented");
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
