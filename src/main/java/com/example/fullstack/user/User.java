package com.example.fullstack.user;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.ZonedDateTime;
import java.util.List;

@Entity /*Hibernate will detect this annotation and map it to a database table.*/
@Table(name = "users") //mapping it to the users table.
public class User extends PanacheEntity {
    /* extend the PanacheEntity class to use basic CRUD and query operations.
    this also allows us to omit the otherwise necessary boilerplate getter and setter methods:
    We declare the JPA columns as public fields and Panache will add the getter/setter methods at runtime
    User class does not contain an @Id annotated JPA column.
    The PanacheEntity base class provides an id field with an automatically generated long value
    that will be mapped to a database sequence.
    */
    @Column(unique = true, nullable = false)
    public String name;

    @Column(nullable = false)
    String password; //this one is not public, but package-private.
    // This implies that when User instance objects get serialized through our HTTP API,
    // this field won’t be included

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    public ZonedDateTime created;

    @Version
    public int version; //This field is used for optimistic locking.
    // It will prevent saving an outdated version of the entity that would otherwise overwrite the most recent changes of a newer version. The optimistic locking features are provided by Hibernate and JPA’s @Version annotation. This field is automatically set by Hibernate upon insertion. It will be checked and automatically incremented upon saving.

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role")
    public List<String> roles;
    //The @ElementCollection annotation is used when mapping very simple non-entity data that is embedded into an entity.
    //we are simply embedding a collection of strings representing the roles the user has
    //EAGER fetch option since we want to always retrieve the list of roles for the user.
    //The @CollectionTable annotation is used to configure the target table of embedded types such as this one.
    // In this case, we’re specifying the name of the target table, user_roles,
    // and the column used to reference the foreign key as a nested @JoinColumn annotation.

}