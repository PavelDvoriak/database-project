package model;

import javax.persistence.*;
import java.util.Objects;

/**
 * An Entity class that defines the User objects
 * and maps them with the USER table in relational database using JPA Annotations.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    public User() {
    }

    /**
     * /**
     * A constructor to create an instance of the User object
     * with given attributes
     *
     * @param uName Username, which user logs in with
     * @param pass  Password to authorize user
     * @param email User's email address
     */
    public User(String uName, String pass, String email) {
        this.username = uName;
        this.password = pass;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method to determine under what circumstances are two User objects equal.
     *
     * @param o An object to be evaluated
     * @return True if objects are equal, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (id.equals(user.getId())) return true;
        return username.equals(user.username) ||
                email.equals(user.email);
    }

    /**
     * Method that returns calculated hash value of a Review object.
     *
     * @return Hash code of a Review object represented by number
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
