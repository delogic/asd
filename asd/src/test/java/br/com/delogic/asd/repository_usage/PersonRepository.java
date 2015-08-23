package br.com.delogic.asd.repository_usage;

import org.springframework.stereotype.Repository;

import br.com.delogic.asd.entities.Person;
import br.com.delogic.asd.repository.jpa.EntityRepository;

@Repository
public interface PersonRepository extends EntityRepository<Person, Integer> {

}
