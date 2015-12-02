package br.com.delogic.asd.config;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SpringTestBase extends Assert {

    @Configuration
    @Import(SpringTestConfig.class)
    static class SpringConfigInit {

    }

    @Inject
    private JpaTransactionManager transactionManager;

    private TransactionStatus transactionStatus;

    @PersistenceContext
    protected EntityManager entityManager;

    protected void beginTransaction() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    protected void commitTransaction() {
        if (transactionManager == null || transactionStatus == null) throw new IllegalStateException(
            "A transação não foi aberta e provavelmente o begin() não foi chamado");

        transactionManager.commit(transactionStatus);
    }

    // private final org.slf4j.Logger logger =
    // LoggerFactory.getLogger(EntityRepositoryTest.class);

    static {
        /* desligar o log para melhorar performance */
        // Log4jManager.setLevel(Level.INFO);
    }

}
