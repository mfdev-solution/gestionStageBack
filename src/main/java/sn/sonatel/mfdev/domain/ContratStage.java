package sn.sonatel.mfdev.domain;

import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contrat_stage")
public class ContratStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String libele;
    private Date dateDebut;
    private Date dateFin;
    private Long remuneration;
    private Double note;

    @Column(name = "isActivated", columnDefinition = "boolean default true")
    private Boolean isActivated = true;

    @Column(name = "isSigned", columnDefinition = "boolean default true")
    private Boolean isSigned;

    @Column(name = "generated", columnDefinition = "boolean default true")
    private Boolean generated;

    private String url;

    @ManyToOne
    private Stagiaire stagiaire;

    @ManyToOne
    private Gwte gwte;

    @OneToMany(mappedBy = "contratStage")
    private Collection<AttestationPresence> attestationPresences;
}
