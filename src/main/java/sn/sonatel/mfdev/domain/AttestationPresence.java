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
@AllArgsConstructor
@NoArgsConstructor
@Table
public class AttestationPresence {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date dateDebut;
    private Date dateFin;

    @Column(columnDefinition = "boolean default false")
    private boolean generated;

    @Enumerated(EnumType.STRING)
    private EtatAttestationPresence etatAttestationPresence = EtatAttestationPresence.enCours;

    @ManyToOne
    private ContratStage contratStage;
}
