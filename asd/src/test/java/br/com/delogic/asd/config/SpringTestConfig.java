package br.com.delogic.asd.config;

import java.util.Map;

import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mock.web.MockServletContext;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import br.com.delogic.asd.content.ContentController;
import br.com.delogic.asd.content.ContentManager;
import br.com.delogic.asd.content.ContentManagerImpl;
import br.com.delogic.asd.content.TimeIterator;
import br.com.delogic.asd.repository.jpa.EntityRepositoryFactoryBean;
import br.com.delogic.asd.repository.jpa.eclipselink.EclipseLinkJpaConfig;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.delogic.asd", repositoryFactoryBeanClass = EntityRepositoryFactoryBean.class)
public class SpringTestConfig extends EclipseLinkJpaConfig {

    public SpringTestConfig() {
        super("asd");
    }

    @Bean(name = "rootDataSource")
    public DataSource rootDataSource() {
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setDriverClassName(org.postgresql.Driver.class.getName());
        ds.setUrl("jdbc:postgresql://localhost:5432/postgres");
        ds.setUsername("postgres");
        ds.setPassword("manager");
        return ds;
    }

    @Bean(name = "sqlDS")
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

    /**
     * Overwritten so it sets one of the two datasources
     */
    @Override
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Named("sqlDS") DataSource dataSource) {
        return super.entityManagerFactory(dataSource);
    }

    /**
     * Overwritten so it creates the database schema with JPA
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, ?> getPropriedadesEclipselink() {
        Map<String, Object> map = (Map<String, Object>) super.getPropriedadesEclipselink();
        map.put("eclipselink.ddl-generation.output-mode", "both");
        return map;
    }

    /**
     * Overwritten so it creates the database schema with JPA
     */
    @Override
    public JpaVendorAdapter getJpaVendorAdapter() {
        EclipseLinkJpaVendorAdapter adapter = (EclipseLinkJpaVendorAdapter) super.getJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        return adapter;
    }

    @Bean
    public ContentManager contentManager(ApplicationContext ctx, ServletContext servletContext) {
        return new ContentManagerImpl(ctx.getResource("file:#{systemProperties['java.io.tmpdir']}/temp"), new TimeIterator(),
            servletContext);
    }

    @Bean
    public ContentController contentController(ContentManager contentManager) {
        return new ContentController(contentManager);
    }

    @Bean
    public ServletContext mockServletContext() {
        return new MockServletContext("/asd");
    }

}