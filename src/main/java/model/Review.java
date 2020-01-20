package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An Entity class that defines Review objects
 * and maps them with the REVIEW table in relational database using JPA Annotations.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revgen")
    @SequenceGenerator(name = "revgen", sequenceName = "rev_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "RATING", nullable = false, columnDefinition = "DOUBLE(2) CHECK (rating >= 0.0 AND rating <= 10")
    private Double rating;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "CREATION_TIME")
    private LocalDateTime timestamp;

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "GAME_ID", referencedColumnName = "ID")
    private Game game;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;


    public Review() {
    }

    /**
     * A constructor to create an instance of the Review object
     * with given attributes
     *
     * @param rating User's rating of the Game object
     * @param content Text content of the Review
     * @param game Game, which the Review belongs to
     * @param user User, whom the Review belongs to
     */
    public Review(Double rating, String content, Game game, User user) {
        this.rating = rating;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.game = game;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Method to determine under what circumstances are two Review objects equal.
     *
     * @param o An object to be evaluated
     * @return True if objects are equal, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        if (id.equals(review.id)) return true;
        return rating.equals(review.rating) &&
                content.equals(review.content) &&
                timestamp.equals(review.timestamp) &&
                game.equals(review.game) &&
                user.equals(review.user);
    }

    /**
     * Method that returns calculated hash value of a Review object.
     *
     * @return Hash code of a Review object represented by number
     */
    @Override
    public int hashCode() {
        return Objects.hash(rating, content, timestamp, game, user);
    }

    /**
     * Method to determine how the text output of Review object looks like.
     *
     * @return Formatted text description of the Review object
     */
    @Override
    public String toString() {
        return user.getUsername() + "\t (" + timestamp + ") rated: " + rating + " and wrote:" +
                "\n\n" + content;
    }
}
