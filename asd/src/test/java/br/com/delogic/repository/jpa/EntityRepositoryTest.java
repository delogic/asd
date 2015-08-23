package br.com.delogic.repository.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.com.delogic.asd.repository.jpa.EclipselinkSlf4jLogger;
import br.com.delogic.entities.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class EntityRepositoryTest extends Assert {

    @Configuration
    @EnableTransactionManagement
    static class Configuracao {

        @Bean(name = "rootDataSource")
        @Order(1)
        public DataSource rootDataSource() {
            org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
            ds.setDriverClassName(org.postgresql.Driver.class.getName());
            ds.setUrl("jdbc:postgresql://localhost:5432/postgres");
            ds.setUsername("postgres");
            ds.setPassword("manager");
            return ds;
        }

        @Bean(name = "sqlDS")
        @Order(2)
        public DataSource sqlDataSource() {
            org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
            ds.setDriverClassName(org.postgresql.Driver.class.getName());
            ds.setUrl("jdbc:postgresql://localhost:5432/asd");
            ds.setUsername("postgres");
            ds.setPassword("manager");
            return ds;
        }

        @Bean
        public InitializingBean criarBanco(@Named("rootDataSource") final DataSource dataSource, final ApplicationContext ctx) {
            return new InitializingBean() {
                @Override
                public void afterPropertiesSet() throws Exception {
                    ResourceDatabasePopulator pop = new ResourceDatabasePopulator();
                    pop.addScripts(
                        ctx.getResource("classpath:banco/excluir-banco.sql"),
                        ctx.getResource("classpath:banco/criar-banco.sql"));
                    pop.setSqlScriptEncoding("UTF-8");
                    pop.populate(dataSource.getConnection());
                }
            };
        }

        @Bean
        public JpaTransactionManager transacationManager(EntityManagerFactory emf) {
            return new JpaTransactionManager(emf);
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Named("sqlDS") DataSource ds) {
            LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
            emf.setPersistenceXmlLocation("classpath:/META-INF/persistence.xml");
            emf.setPersistenceUnitName("asd");
            emf.setDataSource(ds);
            emf.setJpaVendorAdapter(getVendorAdapter());
            emf.setJpaPropertyMap(getPropriedadesEclipselink());
            return emf;
        }

        private Map<String, ?> getPropriedadesEclipselink() {
            Map<String, Object> map = new HashMap<String, Object>();
            // sql-script irá criar somente o script sem mexer no banco
            // database irá alterar o banco mas não irá criar o script
            // both irá alterar o banco e criar o arquivo
            map.put("eclipselink.ddl-generation", "create-tables");
            map.put("eclipselink.ddl-generation.output-mode", "both");
            map.put("eclipselink.logging.level", "ALL");
            map.put("eclipselink.logging.parameters", "true");
            map.put("eclipselink.logging.exceptions", "true");
            map.put("eclipselink.logging.logger", EclipselinkSlf4jLogger.class.getName());
            return map;
        }

        private JpaVendorAdapter getVendorAdapter() {
            EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
            vendorAdapter.setGenerateDdl(true);
            return vendorAdapter;
        }

    }

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    @Named("sqlDS")
    private DataSource dataSource;

    @Inject
    private JpaTransactionManager transactionManager;

    private TransactionStatus transactionStatus;

    Random random = new Random();

    protected void beginTransaction() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    protected void commitTransaction() {
        if (transactionManager == null || transactionStatus == null) throw new IllegalStateException(
            "A transação não foi aberta e provavelmente o begin() não foi chamado");

        transactionManager.commit(transactionStatus);
    }

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(EntityRepositoryTest.class);

    static {
        /* desligar o log para melhorar performance */
        // Log4jManager.setLevel(Level.INFO);
    }

    @Test
    public void teste() {
        Person p = new Person();
        p.setName("Célio Silva");

        beginTransaction();
        entityManager.persist(p);
        entityManager.flush();
        commitTransaction();

    }

}
