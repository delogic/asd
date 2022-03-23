package br.com.delogic.asd.repository.datasource;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.delogic.asd.config.SpringTestBase;

public class LocalSingleConnectionDatasourceTest extends SpringTestBase {

    @Inject
    @Named("sqlDS")
    private DataSource dataSource;
    private LocalSingleConnectionDataSource localDataSource;
    private NamedParameterJdbcTemplate template;
    private Set<String> prop;

    @Before
    public void setUp() {
        template = new NamedParameterJdbcTemplate(dataSource);
    }

    @After
    public void tearDown() {
        try {
            if (localDataSource != null && !localDataSource.getConnection().isClosed())
                localDataSource.releaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldUseMultipleConnection() throws SQLException, Exception {
        givenDataSource(dataSource);
        givenEnabledNestedLoop("off");
        whenQueryingEnabledNestedLoop(1000);
        thenValuesAre("on", "off");
    }

    @Test
    public void shouldUseSameConnection() throws SQLException, Exception {
        givenDataSource(new LocalSingleConnectionDataSource(dataSource));
        givenEnabledNestedLoop("off");
        whenQueryingEnabledNestedLoop(1000);
        thenValuesAre("off");
    }

    @Test
    public void shouldCloseConnection() throws Exception {
        givenDataSource(new LocalSingleConnectionDataSource(dataSource));
        givenEnabledNestedLoop("off");
        whenQueryingEnabledNestedLoop(1000);
        whenReleasingConnection();
        thenValuesAre("off");
        thenConnectionIsClosed(true);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldNotCloseConnection() throws Exception {
        givenDataSource(new LocalSingleConnectionDataSource(dataSource));
        givenEnabledNestedLoop("off");
        whenQueryingEnabledNestedLoop(1000);
        whenReleasingConnection();
        thenValuesAre("off");
        thenConnectionIsClosed(false);
        // testar Idempotency
        thenConnectionIsClosed(false);
        thenConnectionIsClosed(false);
        whenQueryingEnabledNestedLoop(1000);
        whenReleasingConnection();
        thenValuesAre("off");
        thenConnectionIsClosed(false);
    }

    private void thenConnectionIsClosed(boolean b) throws Exception {
        assertEquals(b, localDataSource.getConnection().isClosed());
    }

    private void whenReleasingConnection() {
        localDataSource.releaseConnection();
    }

    private void givenDataSource(DataSource ds) {
        template = new NamedParameterJdbcTemplate(ds);
        if (ds instanceof LocalSingleConnectionDataSource)
            localDataSource = (LocalSingleConnectionDataSource) ds;
    }

    private void givenEnabledNestedLoop(String string) {
        template.getJdbcOperations().execute("set enable_nestloop=" + string);
    }

    private void whenQueryingEnabledNestedLoop(int iterations) throws Exception {
        prop = new HashSet<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < iterations; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    prop.add(template.queryForObject("select setting from pg_settings where name = 'enable_nestloop'",
                        Collections.<String, Object> emptyMap(),
                        String.class));
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }

    private void thenValuesAre(String... values) {
        assertEquals(values.length, prop.size());
        String[] propArray = (String[]) prop.toArray(new String[prop.size()]);
        Arrays.sort(propArray);
        Arrays.sort(values);
        assertArrayEquals(values, propArray);
    }

}
