package fr.esgi.soheil.kevin.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "age_rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgeRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String label;       

    private String colorRgb;   

    @OneToMany(mappedBy = "ageRatingEntity")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<GameEntity> gameEntities;
}
