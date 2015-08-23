package br.com.delogic.asd.repository.jpa.eclipselink;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class EclipseLinkJpaConfig {

    private final String persistenceUnitName;
    private final String persistenceXmlLocation;

    public EclipseLinkJpaConfig(String persistenceUnitName, String persistenceXmlLocation) {
        this.persistenceUnitName = persistenceUnitName;
        this.persistenceXmlLocation = persistenceXmlLocation;
    }

    public EclipseLinkJpaConfig(String persistenceUnitName) {
        this(persistenceUnitName, "classpath:/META-INF/persistence.xml");
    }

    @Bean
    protected JpaTransactionManager transacationManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    protected LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPersistenceXmlLocation(persistenceXmlLocation);
        emf.setPersistenceUnitName(persistenceUnitName);
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(getJpaVendorAdapter());
        emf.setJpaPropertyMap(getPropriedadesEclipselink());
        return emf;
    }

    protected JpaVendorAdapter getJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    protected Map<String, ?> getPropriedadesEclipselink() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eclipselink.logging.level", "ALL");
        map.put("eclipselink.logging.parameters", "true");
        map.put("eclipselink.logging.exceptions", "true");
        map.put("eclipselink.logging.logger", EclipselinkSlf4jLogger.class.getName());
        return map;
    }

}
