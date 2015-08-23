package br.com.delogic.asd.repository.sql;

import br.com.delogic.asd.repository.Criteria;
import br.com.delogic.jfunk.Has;

public class PostgreSqlQueryRangeBuilder implements SqlQueryRangeBuilder {

    public String buildRangeQuery(String query, Criteria criteria) {

        if (Has.content(criteria.getOffset(), criteria.getLimit())) {
            long offset = criteria.getOffset() != null ? criteria.getOffset() : 0;
            long limit = criteria.getLimit() != null ? criteria.getLimit() : Long.MAX_VALUE;
            query += (" Limit " + limit + " offset " + offset);
        }

        return query;
    }

}
