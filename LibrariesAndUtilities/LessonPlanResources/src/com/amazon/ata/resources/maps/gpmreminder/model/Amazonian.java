package com.amazon.ata.resources.maps.gpmreminder.model;

/**
 * Represents an Amazonian; an employee of Amazon. For this simple example, that's an alias and an email.
 *
 * For more complicated examples, we might include a job class, a level, etc...
 */
public class Amazonian {

    final String alias;
    final String email;

    /**
     * Constructs an Amazonian
     * @param alias The ID or alias of this Amazonian.
     * @param email The email of this Amazonian.
     */
    public Amazonian(final String alias, final String email) {
        this.alias = alias;
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public String getEmail() {
        return email;
    }
}
