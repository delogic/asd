package br.com.delogic.asd.repository_usage;

import javax.inject.Inject;

import org.junit.Test;

import br.com.delogic.asd.config.SpringTestBase;
import br.com.delogic.asd.entities.Person;

public class PersonRepositoryTest extends SpringTestBase {

    @Inject
    private PersonRepository personRepository;

    @Test
    public void teste() {
        Person p = new Person();
        p.setName("Célio Silva");

        beginTransaction();
        personRepository.create(p);
        commitTransaction();

        assertEquals("Célio Silva", personRepository.findById(p.getId()).getName());

        p.setName("Another Name");

        beginTransaction();
        personRepository.update(p);
        commitTransaction();

        assertEquals("Another Name", personRepository.findById(p.getId()).getName());

        beginTransaction();
        p = personRepository.findById(p.getId());
        personRepository.delete(p);
        commitTransaction();

        assertNull(personRepository.findById(p.getId()));

    }

}
