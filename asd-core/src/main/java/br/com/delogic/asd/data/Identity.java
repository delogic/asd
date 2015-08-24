package br.com.delogic.asd.data;


public abstract class Identity<E> implements Identifiable<E> {

    @Override
    public int hashCode() {
        return getId() != null ? String.valueOf(getId()).hashCode() : super.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Identity)) return false;

        // many objects inherit from this class so we'd better check if they
        // have the same class
        if (!obj.getClass().equals(this.getClass())) return false;

        E thisId = this.getId();
        E thatId = ((Identity<E>) obj).getId();
        return (thisId != null && (thisId == thatId || thisId.equals(thatId)));
    }

    @Override
    public String toString() {
        return "Type:" + this.getClass().getName() + " id:" + getId();
    }

    public abstract void setId(E id);

    public abstract E getId();

}
