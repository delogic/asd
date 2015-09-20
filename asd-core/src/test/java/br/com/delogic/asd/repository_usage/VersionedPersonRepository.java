package br.com.delogic.asd.repository_usage;

import org.springframework.stereotype.Repository;

import br.com.delogic.asd.entities.VersionedPerson;
import br.com.delogic.asd.repository.jpa.EntityRepository;

@Repository
public interface VersionedPersonRepository extends EntityRepository<VersionedPerson, Integer> {

}
