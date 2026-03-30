package fr.esgi.soheil.kevin.business;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "avatar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    
    @OneToOne(mappedBy = "avatar")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Player player;
}
