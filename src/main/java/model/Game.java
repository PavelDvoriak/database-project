package model;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

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
    private double rating;

    public Game() {
    }

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    /*@Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(!(o instanceof  Game)) {
            return false;
        }

        Game g = (Game) o;

        if(g.getId() == id) {
            return true;
        }

        if((g.getName().equals(name) && (g.getStudio().equals(studio) || g.getPublished().equals(published)))) {
            return true;
        }
        return false;
    }

     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        if(id.equals(game.id)) return true;
        return  name.equals(game.name) &&
                Objects.equals(studio, game.studio) &&
                Objects.equals(published, game.published);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, studio, published);
    }
}
