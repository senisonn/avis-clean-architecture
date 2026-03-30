package fr.esgi.soheil.kevin.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "publisher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "publisher")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<GameEntity> gameEntities;
}