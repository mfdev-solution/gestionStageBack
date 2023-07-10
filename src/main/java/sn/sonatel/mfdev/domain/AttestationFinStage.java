package sn.sonatel.mfdev.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AttestationFinStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateEtabli;

    @Enumerated(EnumType.STRING)
    private EtatAttestationFinStage etatAttestationFinStage = EtatAttestationFinStage.enCours;

    @OneToOne
    private ContratStage contratStage;
}
