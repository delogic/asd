package br.com.delogic.asd.repository_usage;

import javax.inject.Inject;

import org.junit.Test;

import br.com.delogic.asd.config.SpringTestBase;
import br.com.delogic.asd.entities.VersionedPerson;

public class VersionedPersonRepositoryTest extends SpringTestBase {

    @Inject
    private VersionedPersonRepository personRepository;

    @Test
    public void teste() {
        VersionedPerson p = new VersionedPerson();
        p.setName("Célio Silva");

        beginTransaction();
        personRepository.create(p);
        commitTransaction();

        assertEquals("Célio Silva", personRepository.findById(p.getId()).getName());
        assertNotNull(p.getCreationDate());
        assertNotNull(p.getModificationDate());

        p.setName("Another Name");

        beginTransaction();
        personRepository.update(p);
        commitTransaction();

        assertNotNull(p.getCreationDate());
        assertNotNull(p.getModificationDate());

        assertEquals("Another Name", personRepository.findById(p.getId()).getName());

        beginTransaction();
        p = personRepository.findById(p.getId());
        personRepository.delete(p);
        commitTransaction();

        assertNull(personRepository.findById(p.getId()));

    }

}
