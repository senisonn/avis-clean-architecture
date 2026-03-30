package fr.esgi.soheil.kevin.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "game")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @PositiveOrZero
    private Float price;

    private LocalDate releaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private PublisherEntity publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "age_rating_id")
    private AgeRatingEntity ageRatingEntity;

    @ManyToMany
    @JoinTable(
            name = "game_platform",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private List<PlatformEntity> platforms;

    @OneToMany(mappedBy = "gameEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ReviewEntity> reviews;
}