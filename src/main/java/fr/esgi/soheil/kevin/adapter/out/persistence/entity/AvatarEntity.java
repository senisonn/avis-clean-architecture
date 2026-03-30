package fr.esgi.soheil.kevin.adapter.out.persistence.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "avatar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    
    @OneToOne(mappedBy = "avatarEntity")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PlayerEntity player;
}
