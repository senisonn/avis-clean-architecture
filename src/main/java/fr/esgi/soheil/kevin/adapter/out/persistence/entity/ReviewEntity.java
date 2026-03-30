package fr.esgi.soheil.kevin.adapter.out.persistence.entity;

import fr.esgi.soheil.kevin.domain.model.ReviewStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("5.0")
    private Float rating;

    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private GameEntity gameEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PlayerEntity player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id") 
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ModeratorEntity moderator;

    public void publish() {
        if (this.status != ReviewStatus.DRAFT)
            throw new IllegalStateException("Only a draft review can be published");
        this.status = ReviewStatus.PUBLISHED;
        this.submittedAt = LocalDateTime.now();
    }

    public void approve(ModeratorEntity mod) {
        if (this.status != ReviewStatus.PUBLISHED)
            throw new IllegalStateException("Only a published review can be approved");
        this.moderator = mod;
        this.status = ReviewStatus.APPROVED;
    }

    public void reject(ModeratorEntity mod) {
        this.moderator = mod;
        this.status = ReviewStatus.REJECTED;
    }
}