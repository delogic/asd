package br.com.delogic.asd.repository.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Esta entidade foi criada unicamente para poder fazer weaving em BaseEntity e
 * AuditedEntity.
 *
 * @author Célio
 *
 */
@SuppressWarnings({ "serial" })
@Entity
public class WeavingHolder extends VersionedEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPerson")
    @SequenceGenerator(allocationSize = 1, initialValue = 0, name = "seqPerson", sequenceName = "seq_person")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
