package fr.esgi.soheil.kevin.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "platform")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private LocalDate releaseDate;

    @ManyToMany(mappedBy = "platforms")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<GameEntity> gameEntities;
}