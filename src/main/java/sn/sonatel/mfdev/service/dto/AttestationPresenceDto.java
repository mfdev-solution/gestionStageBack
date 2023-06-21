package sn.sonatel.mfdev.service.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.sonatel.mfdev.domain.Stagiaire;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttestationPresenceDto {

    private Stagiaire stagiaire;
    private Date dateDebut;
    private Date dateFin;
}
