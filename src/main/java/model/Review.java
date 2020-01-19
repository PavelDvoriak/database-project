package model;

import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne
    private Game game;

    @ManyToOne
    private User user;

    public Review() {}

    public Review(Double rating, String content) {
        this.rating = rating;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
