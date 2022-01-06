package com.example.socialnetwork.domain;


import java.io.Serializable;
import java.util.Objects;

public class Entity<ID> implements Serializable {

    /**
     * Entity is the base class of the program
     * every entity has an id
     * two entities are equal if they have the same id
     */
    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    /**
     * getter
     *
     * @return the id of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * setter
     *
     * @param id to be set
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * equal
     *
     * @param o
     * @return true if the entities have the same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    /**
     * hashcode
     *
     * @return the hashcode of the entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * toString method
     *
     * @return
     */
    @Override
    public String toString() {
        return "Entity{" + "id=" + id + '}';
    }
}