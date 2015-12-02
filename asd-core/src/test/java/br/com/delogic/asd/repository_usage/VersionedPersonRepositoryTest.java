package br.com.delogic.asd.repository_usage;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Test;

import br.com.delogic.asd.config.SpringTestBase;
import br.com.delogic.asd.entities.VersionedPerson;

public class VersionedPersonRepositoryTest extends SpringTestBase {

    @Inject
    private VersionedPersonRepository personRepository;

    @Test
    public void shouldSaveEntity() throws InterruptedException {
        VersionedPerson p = new VersionedPerson();
        p.setName("Célio Silva");

        beginTransaction();
        personRepository.create(p);
        commitTransaction();

        assertEquals("Célio Silva", personRepository.findById(p.getId()).getName());
        assertNotNull(p.getCreationDate());
        assertNotNull(p.getModificationDate());

        Date modificationDate = p.getModificationDate();
        Date creationDate = p.getCreationDate();

        Thread.sleep(1);

        p.setName("Another Name");

        beginTransaction();
        personRepository.update(p);
        commitTransaction();

        p = personRepository.findById(p.getId());

        assertNotNull(p.getCreationDate());
        assertEquals(creationDate, p.getCreationDate());

        assertNotNull(p.getModificationDate());
        assertNotEquals(modificationDate, p.getModificationDate());

        assertEquals("Another Name", personRepository.findById(p.getId()).getName());

        beginTransaction();
        p = personRepository.findById(p.getId());
        personRepository.delete(p);
        commitTransaction();

        assertNull(personRepository.findById(p.getId()));

    }

}
