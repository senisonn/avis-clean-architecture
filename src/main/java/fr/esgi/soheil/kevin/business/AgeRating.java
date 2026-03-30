package fr.esgi.soheil.kevin.business;

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
public class AgeRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String label;       

    private String colorRgb;   

    @OneToMany(mappedBy = "ageRating")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Game> games;
}
