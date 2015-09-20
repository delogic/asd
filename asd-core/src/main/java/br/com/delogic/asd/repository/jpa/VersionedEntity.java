package br.com.delogic.asd.repository.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuppressWarnings({ "serial" })
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class VersionedEntity<E> extends BaseEntity<E> {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, name = "CREATION_DATE")
    @CreatedDate
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, name = "MODIFICATION_DATE")
    @LastModifiedDate
    private Date modificationDate;

    @Version
    @Column(nullable = false)
    private Integer version;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}