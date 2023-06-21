package sn.sonatel.mfdev.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DemandeExterne {

    @Id
    @GeneratedValue
    private Long id;

    private Date dateDebut;
    private Date dateFin;
    private String profile;
    private Integer nombreStagiaire;

    @Enumerated(EnumType.STRING)
    private EtatDemandeInterne etatDemandeInterne = EtatDemandeInterne.enCours;

    @ManyToOne
    private Manager manager;
}
