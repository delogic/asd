package br.com.delogic.asd.repository.jpa;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import br.com.delogic.jfunk.data.Identifiable;

@SuppressWarnings({ "serial", "unchecked" })
@MappedSuperclass
public abstract class BaseEntity<E> implements Identifiable<E>, Serializable {

    @Override
    public int hashCode() {
        return getId() != null ? String.valueOf(getId()).hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BaseEntity)) return false;

        // many objects inherit from this class so we'd better check if they
        // have the same class
        if (!obj.getClass().equals(this.getClass())) return false;

        E thisId = this.getId();
        E thatId = ((BaseEntity<E>) obj).getId();
        return (thisId != null && (thisId == thatId || thisId.equals(thatId)));
    }

    @Override
    public String toString() {
        return "Type:" + this.getClass().getName() + " id:" + getId();
    }

    public abstract void setId(E id);

    public abstract E getId();

}