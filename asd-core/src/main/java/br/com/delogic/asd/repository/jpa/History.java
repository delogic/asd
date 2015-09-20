package br.com.delogic.asd.repository.jpa;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.history.HistoryPolicy;

public class History implements DescriptorCustomizer {

    public void customize(ClassDescriptor descriptor) {
        HistoryPolicy policy = new HistoryPolicy();
        policy.addHistoryTableName(descriptor.getTableName() + "_HIST");
        policy.addStartFieldName("START_DATE");
        policy.addEndFieldName("END_DATE");
        policy.setShouldHandleWrites(true);
        descriptor.setHistoryPolicy(policy);
    }
}