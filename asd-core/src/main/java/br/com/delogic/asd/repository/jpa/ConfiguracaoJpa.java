package br.com.delogic.asd.repository.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.delogic.asd.repository.jpa.eclipselink.EclipselinkSlf4jLogger;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "br.com.ribfer.gedor", repositoryFactoryBeanClass = EntityRepositoryFactoryBean.class)
public class ConfiguracaoJpa {

    @Bean
    JpaTransactionManager transacationManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(@Named("sqlDS") DataSource ds) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPersistenceXmlLocation("classpath:/META-INF/persistence.xml");
        emf.setPersistenceUnitName("asd");
        emf.setDataSource(ds);
        emf.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
        emf.setJpaPropertyMap(getPropriedadesEclipselink());
        return emf;
    }

    Map<String, ?> getPropriedadesEclipselink() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eclipselink.logging.level", "ALL");
        map.put("eclipselink.logging.parameters", "true");
        map.put("eclipselink.logging.exceptions", "true");
        map.put("eclipselink.logging.logger", EclipselinkSlf4jLogger.class.getName());
        return map;
    }

}
