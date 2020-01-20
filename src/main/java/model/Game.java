package model;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * An Entity class that defines Game objects
 * and maps them with the GAME table in relational database using JPA Annotations.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
@Entity
@Table(name = "GAME")
@Check(constraints = "studio NOT NULL OR published NOT NULL")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generatorini")
    @SequenceGenerator(name = "generatorini", sequenceName = "game_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "STUDIO", nullable = false)
    private String studio;

    @Column(name = "PUBLISHED", nullable = false)
    private LocalDate published;

    @Column(name = "AVG_RATING")
    private Double rating;

    public Game() {
    }

    /**
     * A constructor to create an instance of Game object
     * with given attributes
     *
     * @param gName Name of the game
     * @param studio Game Studio that developed the game
     * @param published Game release date
     */
    public Game(String gName, String studio, LocalDate published) {
        this.name = gName;
        this.studio = studio;
        this.published = published;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Method to determine under what circumstances are two Game objects equal.
     *
     * @param o An object to be evaluated
     * @return True if objects are equal, false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        if (id.equals(game.id)) return true;
        return name.equals(game.name) &&
                Objects.equals(studio, game.studio) &&
                Objects.equals(published, game.published);
    }

    /**
     * Method that returns calculated hash value of a Game object.
     *
     * @return Hash code of a Game object represented by number
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, studio, published);
    }
}
