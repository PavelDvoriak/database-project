package model;

import javax.persistence.*;
/* TODO:
    Override the equals, hashcode methods!
    Add more attributes - reviews, favorites, user photo*, last login*,...
    Resolve adding users with same username - done
    Add user's account options - change user's data / delete account
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

    public User(String uName, String pass, String email) {
        this.username = uName;
        this.password = pass;
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
}
